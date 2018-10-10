package pico.erp.quotation.item;

import java.util.Optional;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
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

  @Lazy
  @Autowired
  private BomService bomService;

  @Lazy
  @Autowired
  private ItemService itemService;

  @Lazy
  @Autowired
  private QuotationMapper quotationMapper;

  protected BomData bom(ItemId itemId) {
    return Optional.ofNullable(itemId)
      .map(id -> bomService.exists(id) ? bomService.get(id) : null)
      .orElse(null);
  }

  @Mappings({
    @Mapping(target = "quotation", source = "quotationId"),
    @Mapping(target = "itemData", source = "itemId"),
    @Mapping(target = "bomData", source = "itemId")
  })
  public abstract QuotationItemMessages.CreateRequest map(
    CreateRequest request);

  @Mappings({
    @Mapping(target = "bomData", source = "itemId")
  })
  public abstract QuotationItemMessages.UpdateRequest map(
    QuotationItemRequests.UpdateRequest request);

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
    @Mapping(target = "itemId", source = "itemData.id")
  })
  public abstract QuotationItemData map(QuotationItem item);


}
