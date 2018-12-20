package pico.erp.quotation.item;

import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import pico.erp.item.ItemId;
import pico.erp.quotation.QuotationId;
import pico.erp.quotation.item.QuotationItemRequests.CreateRequest;
import pico.erp.quotation.item.QuotationItemRequests.DeleteRequest;
import pico.erp.quotation.item.QuotationItemRequests.FixUnitPriceRequest;
import pico.erp.quotation.item.QuotationItemRequests.UpdateRequest;

public interface QuotationItemService {

  QuotationItemData create(@Valid CreateRequest request);

  void delete(@Valid DeleteRequest request);

  boolean exists(@NotNull QuotationItemId id);

  void fixUnitPrice(FixUnitPriceRequest request);

  QuotationItemData get(@NotNull QuotationItemId id);

  List<QuotationItemData> getAll(QuotationId quotationId);

  List<QuotationItemData> getAll(ItemId itemId);

  void update(@Valid UpdateRequest request);

}
