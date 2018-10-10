package pico.erp.quotation.item.addition;

import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import pico.erp.quotation.QuotationId;
import pico.erp.quotation.item.addition.QuotationItemAdditionRequests.CreateRequest;
import pico.erp.quotation.item.addition.QuotationItemAdditionRequests.DeleteRequest;
import pico.erp.quotation.item.addition.QuotationItemAdditionRequests.UpdateRequest;

public interface QuotationItemAdditionService {

  QuotationItemAdditionData create(@Valid CreateRequest request);

  void delete(@Valid DeleteRequest request);

  boolean exists(@NotNull QuotationItemAdditionId id);

  QuotationItemAdditionData get(@NotNull QuotationItemAdditionId id);

  List<QuotationItemAdditionData> getAll(QuotationId quotationId);

  void update(@Valid UpdateRequest request);

}
