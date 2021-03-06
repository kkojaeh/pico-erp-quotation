package pico.erp.quotation.addition;

import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import pico.erp.quotation.QuotationId;
import pico.erp.quotation.addition.QuotationAdditionRequests.CreateRequest;
import pico.erp.quotation.addition.QuotationAdditionRequests.DeleteRequest;
import pico.erp.quotation.addition.QuotationAdditionRequests.GenerateByProcessPreparationRequest;
import pico.erp.quotation.addition.QuotationAdditionRequests.NextDraftRequest;
import pico.erp.quotation.addition.QuotationAdditionRequests.UpdateRequest;

public interface QuotationAdditionService {

  QuotationAdditionData create(@Valid CreateRequest request);

  void delete(@Valid DeleteRequest request);

  boolean exists(@NotNull QuotationAdditionId id);

  void generate(GenerateByProcessPreparationRequest request);

  QuotationAdditionData get(@NotNull QuotationAdditionId id);

  List<QuotationAdditionData> getAll(QuotationId quotationId);

  void nextDraft(NextDraftRequest request);

  void update(@Valid UpdateRequest request);

}
