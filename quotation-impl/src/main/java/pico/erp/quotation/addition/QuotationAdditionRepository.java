package pico.erp.quotation.addition;

import java.util.Optional;
import java.util.stream.Stream;
import javax.validation.constraints.NotNull;
import pico.erp.quotation.addition.data.QuotationAdditionId;
import pico.erp.quotation.data.QuotationId;

public interface QuotationAdditionRepository {

  QuotationAddition create(@NotNull QuotationAddition quotationAddition);

  void deleteBy(@NotNull QuotationAdditionId id);

  boolean exists(@NotNull QuotationAdditionId id);

  Stream<QuotationAddition> findAllBy(@NotNull QuotationId quotationId);

  Optional<QuotationAddition> findBy(@NotNull QuotationAdditionId id);

  void update(@NotNull QuotationAddition quotationAddition);

}
