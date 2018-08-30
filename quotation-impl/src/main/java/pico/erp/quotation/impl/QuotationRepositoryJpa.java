package pico.erp.quotation.impl;

import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.temporal.TemporalAdjusters;
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
import pico.erp.quotation.core.QuotationRepository;
import pico.erp.quotation.data.QuotationId;
import pico.erp.quotation.data.QuotationStatusKind;
import pico.erp.quotation.domain.Quotation;
import pico.erp.quotation.domain.QuotationAggregator;
import pico.erp.quotation.impl.jpa.QuotationEntity;

@Repository
interface QuotationEntityRepository extends CrudRepository<QuotationEntity, QuotationId> {

  @Query("SELECT COUNT(q) FROM Quotation q WHERE q.createdDate >= :begin AND q.createdDate <= :end")
  long countByCreatedDateBetween(@Param("begin") OffsetDateTime begin,
    @Param("end") OffsetDateTime end);

  @Query("SELECT q FROM Quotation q WHERE q.expirationDate < :fixedDate AND q.status in (:statuses)")
  Stream<QuotationEntity> findAllExpireCandidateBeforeThan(
    @Param("fixedDate") OffsetDateTime fixedDate,
    @Param("statuses") Set<QuotationStatusKind> statuses);

}

@Repository
@Transactional
public class QuotationRepositoryJpa implements QuotationRepository {

  @Autowired
  private QuotationEntityRepository repository;

  @Autowired
  private QuotationJpaMapper mapper;


  @Override
  public long countByCreatedThisMonth() {
    OffsetDateTime begin = OffsetDateTime.now().with(TemporalAdjusters.firstDayOfMonth())
      .with(LocalTime.MIN);
    OffsetDateTime end = OffsetDateTime.now().with(TemporalAdjusters.lastDayOfMonth())
      .with(LocalTime.MAX);
    return repository.countByCreatedDateBetween(begin, end);
  }

  @Override
  public Quotation create(Quotation quotation) {
    val entity = mapper.map(quotation);
    val created = repository.save(entity);
    return mapper.map(created);
  }

  @Override
  public void deleteBy(QuotationId id) {
    repository.delete(id);
  }

  @Override
  public boolean exists(QuotationId id) {
    return repository.exists(id);
  }

  @Override
  public Optional<QuotationAggregator> findAggregatorBy(QuotationId id) {
    return Optional.ofNullable(repository.findOne(id))
      .map(mapper::mapAggregator);
  }

  @Override
  public Stream<Quotation> findAllExpireCandidateBeforeThan(OffsetDateTime fixedDate) {
    return repository.findAllExpireCandidateBeforeThan(fixedDate,
      EnumSet.of(QuotationStatusKind.COMMITTED, QuotationStatusKind.IN_PROCEED))
      .map(mapper::map);
  }

  @Override
  public Optional<Quotation> findBy(QuotationId id) {
    return Optional.ofNullable(repository.findOne(id))
      .map(mapper::map);
  }

  @Override
  public void update(Quotation quotation) {
    val entity = repository.findOne(quotation.getId());
    mapper.pass(mapper.map(quotation), entity);
    repository.save(entity);
  }
}
