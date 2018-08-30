package pico.erp.quotation.impl;

import java.util.Optional;
import java.util.stream.Stream;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pico.erp.quotation.core.QuotationAdditionRepository;
import pico.erp.quotation.data.QuotationAdditionId;
import pico.erp.quotation.data.QuotationId;
import pico.erp.quotation.domain.QuotationAddition;
import pico.erp.quotation.impl.jpa.QuotationAdditionEntity;

@Repository
interface QuotationAdditionEntityRepository extends
  CrudRepository<QuotationAdditionEntity, QuotationAdditionId> {

  /*
    @Query("SELECT q FROM Quotation q JOIN q.items i WHERE treat(i as BomQuotationAddition).bomId = :bomId")
    */

  @Query("SELECT qa FROM QuotationAddition qa WHERE qa.quotation.id = :quotationId")
  Stream<QuotationAdditionEntity> findAllBy(@Param("quotationId") QuotationId quotationId);

}

@Repository
@Transactional
public class QuotationAdditionRepositoryJpa implements QuotationAdditionRepository {

  @Autowired
  private QuotationAdditionEntityRepository repository;

  @Autowired
  private QuotationJpaMapper mapper;

  @Override
  public QuotationAddition create(QuotationAddition quotationAddition) {
    val entity = mapper.map(quotationAddition);
    val created = repository.save(entity);
    return mapper.map(created);
  }

  @Override
  public void deleteBy(QuotationAdditionId id) {
    repository.delete(id);
  }

  @Override
  public boolean exists(QuotationAdditionId id) {
    return repository.exists(id);
  }

  @Override
  public Stream<QuotationAddition> findAllBy(QuotationId quotationId) {
    return repository.findAllBy(quotationId)
      .map(mapper::map);
  }

  @Override
  public Optional<QuotationAddition> findBy(QuotationAdditionId id) {
    return Optional.ofNullable(repository.findOne(id))
      .map(mapper::map);
  }

  @Override
  public void update(QuotationAddition quotationAddition) {
    val entity = repository.findOne(quotationAddition.getId());
    mapper.pass(mapper.map(quotationAddition), entity);
    repository.save(entity);
  }
}
