package pico.erp.quotation;

import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import pico.erp.quotation.QuotationAdditionRequests.CreateRequest;
import pico.erp.quotation.QuotationAdditionRequests.DeleteRequest;
import pico.erp.quotation.QuotationAdditionRequests.UpdateRequest;
import pico.erp.quotation.data.QuotationAdditionData;
import pico.erp.quotation.data.QuotationAdditionId;
import pico.erp.quotation.data.QuotationId;

public interface QuotationAdditionService {

  QuotationAdditionData create(@Valid CreateRequest request);

  void delete(@Valid DeleteRequest request);

  boolean exists(@NotNull QuotationAdditionId id);

  QuotationAdditionData get(@NotNull QuotationAdditionId id);

  List<QuotationAdditionData> getAll(QuotationId quotationId);

  void update(@Valid UpdateRequest request);

}
