package pico.erp.quotation.core;

import java.util.Optional;
import java.util.stream.Stream;
import javax.validation.constraints.NotNull;
import pico.erp.quotation.data.QuotationAdditionId;
import pico.erp.quotation.data.QuotationId;
import pico.erp.quotation.domain.QuotationAddition;

public interface QuotationAdditionRepository {

  QuotationAddition create(@NotNull QuotationAddition quotationAddition);

  void deleteBy(@NotNull QuotationAdditionId id);

  boolean exists(@NotNull QuotationAdditionId id);

  Stream<QuotationAddition> findAllBy(@NotNull QuotationId quotationId);

  Optional<QuotationAddition> findBy(@NotNull QuotationAdditionId id);

  void update(@NotNull QuotationAddition quotationAddition);

}
