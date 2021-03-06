package pico.erp.quotation.addition;

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
interface QuotationAdditionEntityRepository extends
  CrudRepository<QuotationAdditionEntity, QuotationAdditionId> {

  /*
    @Query("SELECT q FROM Quotation q JOIN q.items i WHERE treat(i as BomQuotationAddition).bomId = :bomId")
    */

  @Query("SELECT qa FROM QuotationAddition qa WHERE qa.quotationId = :quotationId")
  Stream<QuotationAdditionEntity> findAllBy(@Param("quotationId") QuotationId quotationId);

}

@Repository
@Transactional
public class QuotationAdditionRepositoryJpa implements QuotationAdditionRepository {

  @Autowired
  private QuotationAdditionEntityRepository repository;

  @Autowired
  private QuotationAdditionMapper mapper;

  @Override
  public QuotationAddition create(QuotationAddition quotationAddition) {
    val entity = mapper.jpa(quotationAddition);
    val created = repository.save(entity);
    return mapper.jpa(created);
  }

  @Override
  public void deleteBy(QuotationAdditionId id) {
    repository.deleteById(id);
  }

  @Override
  public boolean exists(QuotationAdditionId id) {
    return repository.existsById(id);
  }

  @Override
  public Stream<QuotationAddition> findAllBy(QuotationId quotationId) {
    return repository.findAllBy(quotationId)
      .map(mapper::jpa);
  }

  @Override
  public Optional<QuotationAddition> findBy(QuotationAdditionId id) {
    return repository.findById(id)
      .map(mapper::jpa);
  }

  @Override
  public void update(QuotationAddition quotationAddition) {
    val entity = repository.findById(quotationAddition.getId()).get();
    mapper.pass(mapper.jpa(quotationAddition), entity);
    repository.save(entity);
  }
}
