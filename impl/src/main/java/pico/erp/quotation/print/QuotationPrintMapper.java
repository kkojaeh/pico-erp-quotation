package pico.erp.quotation.print;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import kkojaeh.spring.boot.component.ComponentAutowired;
import lombok.val;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import pico.erp.bom.BomData;
import pico.erp.bom.BomHierarchyData;
import pico.erp.bom.BomService;
import pico.erp.company.CompanyService;
import pico.erp.item.ItemData;
import pico.erp.item.ItemId;
import pico.erp.item.ItemService;
import pico.erp.item.spec.ItemSpecData;
import pico.erp.item.spec.ItemSpecId;
import pico.erp.item.spec.ItemSpecService;
import pico.erp.process.ProcessData;
import pico.erp.process.ProcessId;
import pico.erp.process.ProcessService;
import pico.erp.quotation.QuotationAggregator;
import pico.erp.quotation.addition.QuotationAddition;
import pico.erp.quotation.item.QuotationItem;
import pico.erp.quotation.item.addition.QuotationItemAddition;

@Mapper(imports = BigDecimal.class)
public abstract class QuotationPrintMapper {

  @ComponentAutowired
  protected CompanyService companyService;

  @ComponentAutowired
  private BomService bomService;

  @ComponentAutowired
  private ItemService itemService;

  @ComponentAutowired
  private ProcessService processService;

  @ComponentAutowired
  private ItemSpecService itemSpecService;

  protected ItemData map(ItemId itemId) {
    return Optional.ofNullable(itemId)
      .map(itemService::get)
      .orElse(null);
  }

  protected ProcessData map(ProcessId processId) {
    return Optional.ofNullable(processId)
      .map(processService::get)
      .orElse(null);
  }

  protected ItemSpecData map(ItemSpecId itemSpecId) {
    return Optional.ofNullable(itemSpecId)
      .map(itemSpecService::get)
      .orElse(null);
  }

  @Mappings({
    @Mapping(target = "supplierData", expression = "java(companyService.getOwner())")
  })
  public abstract QuotationPrintData map(QuotationAggregator quotationAggregator);

  protected BomHierarchyData map(BomData bom) {
    if (bom != null) {
      return bomService.getHierarchy(bom.getId());
    }
    return null;
  }

  protected List<QuotationPrintBomData> map(BomHierarchyData data) {
    final List<QuotationPrintBomData> boms = new LinkedList<>();
    data.visitInOrder((bom, parents) -> {
      val processes = processService.getAll(bom.getItemId());
      if (processes.isEmpty()) {
        boms.add(
          QuotationPrintBomData.builder()
            .bom(bom)
            .level(parents.size())
            .item(map(bom.getItemId()))
            .process(null)
            .itemSpec(map(bom.getItemSpecId()))
            .build()
        );
      } else {
        processes.forEach(process -> {
          boms.add(
            QuotationPrintBomData.builder()
              .bom(bom)
              .level(parents.size())
              .item(map(bom.getItemId()))
              .process(process)
              .itemSpec(map(bom.getItemSpecId()))
              .build()
          );
        });
      }
    });
    return boms;
  }

  @Mappings({
    @Mapping(target = "rate", expression = "java(itemAdditionRate.getAdditionalRate().multiply(new BigDecimal(100)).setScale(2, BigDecimal.ROUND_HALF_UP) + \" %\")")
  })
  protected abstract QuotationPrintItemAdditionRateData map(
    QuotationItemAddition itemAdditionRate);

  protected abstract QuotationExportAdditionData map(QuotationAddition addition);

  @Mappings({
    @Mapping(target = "boms", source = "bom"),
    @Mapping(target = "name", source = "item.name"),
    @Mapping(target = "unit", source = "item.unit")
  })
  public abstract QuotationPrintItemData map(QuotationItem item);

}
