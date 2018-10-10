package pico.erp.quotation.item.addition;

import java.util.Optional;
import java.util.stream.Stream;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pico.erp.quotation.QuotationId;

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
  private QuotationItemAdditionMapper mapper;

  @Override
  public QuotationItemAddition create(QuotationItemAddition quotationAddition) {
    val entity = mapper.jpa(quotationAddition);
    val created = repository.save(entity);
    return mapper.jpa(created);
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
      .map(mapper::jpa);
  }

  @Override
  public Optional<QuotationItemAddition> findBy(QuotationItemAdditionId id) {
    return Optional.ofNullable(repository.findOne(id))
      .map(mapper::jpa);
  }

  @Override
  public void update(QuotationItemAddition quotationAddition) {
    val entity = repository.findOne(quotationAddition.getId());
    mapper.pass(mapper.jpa(quotationAddition), entity);
    repository.save(entity);
  }
}
