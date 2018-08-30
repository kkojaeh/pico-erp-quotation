package pico.erp.quotation.core;

import java.util.List;
import java.util.stream.Collectors;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import pico.erp.quotation.QuotationAdditionExceptions.NotFoundException;
import pico.erp.quotation.QuotationAdditionRequests.CreateRequest;
import pico.erp.quotation.QuotationAdditionRequests.DeleteRequest;
import pico.erp.quotation.QuotationAdditionRequests.UpdateRequest;
import pico.erp.quotation.QuotationAdditionService;
import pico.erp.quotation.data.QuotationAdditionData;
import pico.erp.quotation.data.QuotationAdditionId;
import pico.erp.quotation.data.QuotationId;
import pico.erp.quotation.domain.QuotationAddition;
import pico.erp.shared.Public;
import pico.erp.shared.event.EventPublisher;

@SuppressWarnings("Duplicates")
@Service
@Public
@Transactional
@Validated
public class QuotationAdditionServiceLogic implements QuotationAdditionService {

  @Autowired
  private EventPublisher eventPublisher;

  @Autowired
  private QuotationMapper mapper;

  @Autowired
  private QuotationAdditionRepository quotationAdditionRepository;

  @Override
  public QuotationAdditionData create(CreateRequest request) {
    val addition = new QuotationAddition();
    val response = addition.apply(mapper.map(request));
    val created = quotationAdditionRepository.create(addition);
    eventPublisher.publishEvents(response.getEvents());
    return mapper.map(created);
  }

  @Override
  public void delete(DeleteRequest request) {
    val addition = quotationAdditionRepository.findBy(request.getId())
      .orElseThrow(NotFoundException::new);
    val response = addition.apply(mapper.map(request));
    quotationAdditionRepository.deleteBy(addition.getId());
    eventPublisher.publishEvents(response.getEvents());
  }

  @Override
  public boolean exists(QuotationAdditionId id) {
    return quotationAdditionRepository.exists(id);
  }

  @Override
  public QuotationAdditionData get(QuotationAdditionId id) {
    return quotationAdditionRepository.findBy(id)
      .map(mapper::map)
      .orElseThrow(NotFoundException::new);
  }

  @Override
  public List<QuotationAdditionData> getAll(QuotationId quotationId) {
    return quotationAdditionRepository.findAllBy(quotationId)
      .map(mapper::map)
      .collect(Collectors.toList());
  }

  @Override
  public void update(UpdateRequest request) {
    val item = quotationAdditionRepository.findBy(request.getId())
      .orElseThrow(NotFoundException::new);
    val response = item.apply(mapper.map(request));
    quotationAdditionRepository.update(item);
    eventPublisher.publishEvents(response.getEvents());
  }
}
