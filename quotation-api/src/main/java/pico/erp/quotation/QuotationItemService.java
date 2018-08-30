package pico.erp.quotation;

import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import pico.erp.quotation.QuotationItemRequests.CreateRequest;
import pico.erp.quotation.QuotationItemRequests.DeleteRequest;
import pico.erp.quotation.QuotationItemRequests.FixUnitPriceRequest;
import pico.erp.quotation.QuotationItemRequests.UpdateRequest;
import pico.erp.quotation.data.QuotationId;
import pico.erp.quotation.data.QuotationItemData;
import pico.erp.quotation.data.QuotationItemId;

public interface QuotationItemService {

  QuotationItemData create(@Valid CreateRequest request);

  void delete(@Valid DeleteRequest request);

  boolean exists(@NotNull QuotationItemId id);

  void fixUnitPrice(FixUnitPriceRequest request);

  QuotationItemData get(@NotNull QuotationItemId id);

  List<QuotationItemData> getAll(QuotationId quotationId);

  void update(@Valid UpdateRequest request);

}
