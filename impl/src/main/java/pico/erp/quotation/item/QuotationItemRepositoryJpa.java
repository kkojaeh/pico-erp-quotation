package pico.erp.quotation.item;

import java.util.Optional;
import java.util.stream.Stream;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pico.erp.item.ItemId;
import pico.erp.quotation.QuotationId;

@Repository
interface QuotationItemEntityRepository extends
  CrudRepository<QuotationItemEntity, QuotationItemId> {

  /*
    @Query("SELECT q FROM Quotation q JOIN q.items i WHERE treat(i as BomQuotationItem).bomId = :bomId")
    */
  @Query("SELECT qi FROM QuotationItem qi WHERE qi.itemId = :itemId")
  Stream<QuotationItemEntity> findAllBy(@Param("itemId") ItemId itemId);

  @Query("SELECT qi FROM QuotationItem qi WHERE qi.quotationId = :quotationId")
  Stream<QuotationItemEntity> findAllBy(@Param("quotationId") QuotationId quotationId);

}

@Repository
@Transactional
public class QuotationItemRepositoryJpa implements QuotationItemRepository {

  @Autowired
  private QuotationItemEntityRepository repository;

  @Autowired
  private QuotationItemMapper mapper;

  @Override
  public QuotationItem create(QuotationItem quotationItem) {
    val entity = mapper.jpa(quotationItem);
    val created = repository.save(entity);
    return mapper.jpa(created);
  }

  @Override
  public void deleteBy(QuotationItemId id) {
    repository.deleteById(id);
  }

  @Override
  public boolean exists(QuotationItemId id) {
    return repository.existsById(id);
  }

  @Override
  public Stream<QuotationItem> findAllBy(QuotationId quotationId) {
    return repository.findAllBy(quotationId)
      .map(mapper::jpa);
  }

  @Override
  public Stream<QuotationItem> findAllBy(ItemId itemId) {
    return repository.findAllBy(itemId)
      .map(mapper::jpa);
  }

  @Override
  public Optional<QuotationItem> findBy(QuotationItemId id) {
    return repository.findById(id)
      .map(mapper::jpa);
  }

  @Override
  public void update(QuotationItem quotationItem) {
    val entity = repository.findById(quotationItem.getId()).get();
    mapper.pass(mapper.jpa(quotationItem), entity);
    repository.save(entity);
  }
}
