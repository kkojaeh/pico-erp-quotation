package pico.erp.quotation;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.stream.Stream;
import javax.validation.constraints.NotNull;
import org.springframework.stereotype.Repository;

@Repository
public interface QuotationRepository {

  long countByCreatedThisMonth();

  Quotation create(@NotNull Quotation quotation);

  void deleteBy(@NotNull QuotationId id);

  boolean exists(@NotNull QuotationId id);

  Stream<Quotation> findAllExpireCandidateBeforeThan(@NotNull OffsetDateTime fixedDate);

  Optional<Quotation> findBy(@NotNull QuotationId id);

  Optional<QuotationAggregator> findAggregatorBy(@NotNull QuotationId id);

  void update(@NotNull Quotation quotation);

}
