package pico.erp.quotation.item.addition;

import java.util.List;
import java.util.stream.Collectors;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import pico.erp.quotation.QuotationId;
import pico.erp.quotation.item.addition.QuotationItemAdditionRequests.CreateRequest;
import pico.erp.quotation.item.addition.QuotationItemAdditionRequests.DeleteRequest;
import pico.erp.quotation.item.addition.QuotationItemAdditionRequests.UpdateRequest;
import pico.erp.shared.Public;
import pico.erp.shared.event.EventPublisher;

@SuppressWarnings("Duplicates")
@Service
@Public
@Transactional
@Validated
public class QuotationItemAdditionServiceLogic implements QuotationItemAdditionService {

  @Autowired
  private EventPublisher eventPublisher;

  @Autowired
  private QuotationItemAdditionMapper mapper;

  @Autowired
  private QuotationItemAdditionRepository quotationItemAdditionRepository;

  @Override
  public QuotationItemAdditionData create(CreateRequest request) {
    val itemAddition = new QuotationItemAddition();
    val response = itemAddition.apply(mapper.map(request));
    if (quotationItemAdditionRepository.exists(request.getId())) {
      throw new QuotationItemAdditionExceptions.AlreadyExistsException();
    }
    val created = quotationItemAdditionRepository.create(itemAddition);
    eventPublisher.publishEvents(response.getEvents());
    return mapper.map(created);
  }

  @Override
  public void delete(DeleteRequest request) {

    val itemAddition = quotationItemAdditionRepository.findBy(request.getId())
      .orElseThrow(QuotationItemAdditionExceptions.NotFoundException::new);
    val response = itemAddition.apply(mapper.map(request));
    quotationItemAdditionRepository.deleteBy(itemAddition.getId());
    eventPublisher.publishEvents(response.getEvents());
  }

  @Override
  public boolean exists(QuotationItemAdditionId id) {
    return quotationItemAdditionRepository.exists(id);
  }

  @Override
  public QuotationItemAdditionData get(QuotationItemAdditionId id) {
    return quotationItemAdditionRepository.findBy(id)
      .map(mapper::map)
      .orElseThrow(QuotationItemAdditionExceptions.NotFoundException::new);
  }

  @Override
  public List<QuotationItemAdditionData> getAll(QuotationId quotationId) {
    return quotationItemAdditionRepository.findAllBy(quotationId)
      .map(mapper::map)
      .collect(Collectors.toList());
  }

  @Override
  public void update(UpdateRequest request) {
    val itemAddition = quotationItemAdditionRepository.findBy(request.getId())
      .orElseThrow(QuotationItemAdditionExceptions.NotFoundException::new);
    val response = itemAddition.apply(mapper.map(request));
    quotationItemAdditionRepository.update(itemAddition);
    eventPublisher.publishEvents(response.getEvents());
  }
}
