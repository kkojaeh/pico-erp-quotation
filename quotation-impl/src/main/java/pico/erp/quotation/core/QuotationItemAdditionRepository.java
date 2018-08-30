package pico.erp.quotation.core;

import java.util.Optional;
import java.util.stream.Stream;
import javax.validation.constraints.NotNull;
import pico.erp.quotation.data.QuotationId;
import pico.erp.quotation.data.QuotationItemAdditionId;
import pico.erp.quotation.domain.QuotationItemAddition;

public interface QuotationItemAdditionRepository {

  QuotationItemAddition create(@NotNull QuotationItemAddition quotationItemAddition);

  void deleteBy(@NotNull QuotationItemAdditionId id);

  boolean exists(@NotNull QuotationItemAdditionId id);

  Stream<QuotationItemAddition> findAllBy(@NotNull QuotationId quotationId);

  Optional<QuotationItemAddition> findBy(@NotNull QuotationItemAdditionId id);

  void update(@NotNull QuotationItemAddition quotationItemAddition);

}
