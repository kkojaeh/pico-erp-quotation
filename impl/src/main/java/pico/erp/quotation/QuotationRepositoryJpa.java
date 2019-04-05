package pico.erp.quotation;

import java.time.OffsetDateTime;
import java.util.EnumSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
interface QuotationEntityRepository extends CrudRepository<QuotationEntity, QuotationId> {

  @Query("SELECT COUNT(q) FROM Quotation q WHERE q.createdDate >= :begin AND q.createdDate <= :end")
  long countCreatedBetween(@Param("begin") OffsetDateTime begin,
    @Param("end") OffsetDateTime end);

  @Query("SELECT q FROM Quotation q WHERE q.expirationDate < :fixedDate AND q.status in (:statuses)")
  Stream<QuotationEntity> findAllExpireCandidateBeforeThan(
    @Param("fixedDate") OffsetDateTime fixedDate,
    @Param("statuses") Set<QuotationStatusKind> statuses);

  @Query("SELECT CASE WHEN COUNT(q) > 0 THEN true ELSE false END FROM Quotation q WHERE q.code = :code")
  boolean exists(@Param("code") QuotationCode code);

}

@Repository
@Transactional
public class QuotationRepositoryJpa implements QuotationRepository {

  @Autowired
  private QuotationEntityRepository repository;

  @Autowired
  private QuotationMapper mapper;

  @Override
  public long countCreatedBetween(OffsetDateTime begin, OffsetDateTime end) {
    return repository.countCreatedBetween(begin, end);
  }

  @Override
  public Quotation create(Quotation quotation) {
    val entity = mapper.jpa(quotation);
    val created = repository.save(entity);
    return mapper.jpa(created);
  }

  @Override
  public void deleteBy(QuotationId id) {
    repository.deleteById(id);
  }

  @Override
  public boolean exists(QuotationId id) {
    return repository.existsById(id);
  }

  @Override
  public boolean exists(QuotationCode code) {
    return repository.exists(code);
  }

  @Override
  public Optional<QuotationAggregator> findAggregatorBy(QuotationId id) {
    return repository.findById(id)
      .map(mapper::jpaAggregator);
  }

  @Override
  public Stream<Quotation> findAllExpireCandidateBeforeThan(OffsetDateTime fixedDate) {
    return repository.findAllExpireCandidateBeforeThan(fixedDate,
      EnumSet.of(QuotationStatusKind.COMMITTED, QuotationStatusKind.IN_PROCEED))
      .map(mapper::jpa);
  }

  @Override
  public Optional<Quotation> findBy(QuotationId id) {
    return repository.findById(id)
      .map(mapper::jpa);
  }

  @Override
  public void update(Quotation quotation) {
    val entity = repository.findById(quotation.getId()).get();
    mapper.pass(mapper.jpa(quotation), entity);
    repository.save(entity);
  }
}
