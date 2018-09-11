package pico.erp.quotation.jpa;

import java.util.Optional;
import java.util.stream.Stream;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pico.erp.quotation.data.QuotationId;
import pico.erp.quotation.item.addition.QuotationItemAddition;
import pico.erp.quotation.item.addition.QuotationItemAdditionRepository;
import pico.erp.quotation.item.addition.data.QuotationItemAdditionId;

@Repository
interface QuotationItemAdditionEntityRepository extends
  CrudRepository<QuotationItemAdditionEntity, QuotationItemAdditionId> {

  /*
    @Query("SELECT q FROM Quotation q JOIN q.items i WHERE treat(i as BomQuotationItemAddition).bomId = :bomId")
    */

  @Query("SELECT qia FROM QuotationItemAddition qia WHERE qia.quotation.id = :quotationId")
  Stream<QuotationItemAdditionEntity> findAllBy(@Param("quotationId") QuotationId quotationId);

}

@Repository
@Transactional
public class QuotationItemAdditionRepositoryJpa implements QuotationItemAdditionRepository {

  @Autowired
  private QuotationItemAdditionEntityRepository repository;

  @Autowired
  private QuotationJpaMapper mapper;

  @Override
  public QuotationItemAddition create(QuotationItemAddition quotationAddition) {
    val entity = mapper.map(quotationAddition);
    val created = repository.save(entity);
    return mapper.map(created);
  }

  @Override
  public void deleteBy(QuotationItemAdditionId id) {
    repository.delete(id);
  }

  @Override
  public boolean exists(QuotationItemAdditionId id) {
    return repository.exists(id);
  }

  @Override
  public Stream<QuotationItemAddition> findAllBy(QuotationId quotationId) {
    return repository.findAllBy(quotationId)
      .map(mapper::map);
  }

  @Override
  public Optional<QuotationItemAddition> findBy(QuotationItemAdditionId id) {
    return Optional.ofNullable(repository.findOne(id))
      .map(mapper::map);
  }

  @Override
  public void update(QuotationItemAddition quotationAddition) {
    val entity = repository.findOne(quotationAddition.getId());
    mapper.pass(mapper.map(quotationAddition), entity);
    repository.save(entity);
  }
}
