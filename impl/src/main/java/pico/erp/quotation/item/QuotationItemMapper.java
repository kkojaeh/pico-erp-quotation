package pico.erp.quotation.item;

import java.util.Optional;
import kkojaeh.spring.boot.component.ComponentAutowired;
import lombok.val;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import pico.erp.bom.BomData;
import pico.erp.bom.BomService;
import pico.erp.item.ItemData;
import pico.erp.item.ItemId;
import pico.erp.item.ItemService;
import pico.erp.quotation.Quotation;
import pico.erp.quotation.QuotationId;
import pico.erp.quotation.QuotationMapper;
import pico.erp.quotation.item.QuotationItemRequests.CreateRequest;
import pico.erp.quotation.item.QuotationItemRequests.FixUnitPriceRequest;

@Mapper
public abstract class QuotationItemMapper {

  @ComponentAutowired
  private BomService bomService;

  @ComponentAutowired
  private ItemService itemService;

  @Lazy
  @Autowired
  private QuotationMapper quotationMapper;

  protected BomData bom(ItemId itemId) {
    return Optional.ofNullable(itemId)
      .map(id -> bomService.exists(id) ? bomService.get(id) : null)
      .orElse(null);
  }

  public QuotationItem jpa(QuotationItemEntity entity) {
    val itemId = entity.getItemId();
    val bom = bomService.exists(itemId) ? bomService.get(itemId) : null;
    return QuotationItem.builder()
      .quotation(map(entity.getQuotationId()))
      .bom(bom)
      .item(map(entity.getItemId()))
      .description(entity.getDescription())
      .remark(entity.getRemark())
      .quantity(entity.getQuantity())
      .originalUnitPrice(entity.getOriginalUnitPrice())
      .directLaborUnitPrice(entity.getDirectLaborUnitPrice())
      .indirectLaborUnitPrice(entity.getIndirectLaborUnitPrice())
      .indirectMaterialUnitPrice(entity.getIndirectMaterialUnitPrice())
      .directMaterialUnitPrice(entity.getDirectMaterialUnitPrice())
      .indirectExpensesUnitPrice(entity.getIndirectExpensesUnitPrice())
      .additionalRate(entity.getAdditionalRate())
      .discountRate(entity.getDiscountRate())
      .id(entity.getId())
      .bom(bom(entity.getItemId()))
      .unitPriceManuallyFixed(entity.isUnitPriceManuallyFixed())
      .build();
  }

  public abstract QuotationItemMessages.DeleteRequest map(
    QuotationItemRequests.DeleteRequest request);

  public abstract QuotationItemMessages.FixUnitPriceRequest map(
    FixUnitPriceRequest request);

  protected ItemData map(ItemId itemId) {
    return Optional.ofNullable(itemId)
      .map(itemService::get)
      .orElse(null);
  }

  protected Quotation map(QuotationId quotationId) {
    return quotationMapper.map(quotationId);
  }

  @Mappings({
    //@Mapping(target = "bomId", source = "bom.id"),
    @Mapping(target = "quotationId", source = "quotation.id"),
    @Mapping(target = "itemId", source = "item.id"),
    @Mapping(target = "createdBy", ignore = true),
    @Mapping(target = "createdDate", ignore = true)
  })
  public abstract QuotationItemEntity jpa(QuotationItem item);

  @Mappings({
    @Mapping(target = "quotationId", source = "quotation.id"),
    @Mapping(target = "itemId", source = "item.id")
  })
  public abstract QuotationItemData map(QuotationItem item);

  @Mappings({
    @Mapping(target = "bom", source = "itemId")
  })
  public abstract QuotationItemMessages.UpdateRequest map(
    QuotationItemRequests.UpdateRequest request);

  @Mappings({
    @Mapping(target = "quotation", source = "quotationId"),
    @Mapping(target = "item", source = "itemId"),
    @Mapping(target = "bom", source = "itemId")
  })
  public abstract QuotationItemMessages.CreateRequest map(
    CreateRequest request);

  public abstract void pass(QuotationItemEntity from, @MappingTarget QuotationItemEntity to);


}
