package pico.erp.quotation;

import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import pico.erp.quotation.QuotationItemAdditionRequests.CreateRequest;
import pico.erp.quotation.QuotationItemAdditionRequests.DeleteRequest;
import pico.erp.quotation.QuotationItemAdditionRequests.UpdateRequest;
import pico.erp.quotation.data.QuotationId;
import pico.erp.quotation.data.QuotationItemAdditionData;
import pico.erp.quotation.data.QuotationItemAdditionId;

public interface QuotationItemAdditionService {

  QuotationItemAdditionData create(@Valid CreateRequest request);

  void delete(@Valid DeleteRequest request);

  boolean exists(@NotNull QuotationItemAdditionId id);

  QuotationItemAdditionData get(@NotNull QuotationItemAdditionId id);

  List<QuotationItemAdditionData> getAll(QuotationId quotationId);

  void update(@Valid UpdateRequest request);

}
