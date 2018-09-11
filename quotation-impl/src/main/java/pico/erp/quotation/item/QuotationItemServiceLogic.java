package pico.erp.quotation.item;

import java.util.List;
import java.util.stream.Collectors;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import pico.erp.quotation.QuotationMapper;
import pico.erp.quotation.data.QuotationId;
import pico.erp.quotation.item.QuotationItemMessages.ApplyItemAdditionRequest;
import pico.erp.quotation.item.QuotationItemRequests.CreateRequest;
import pico.erp.quotation.item.QuotationItemRequests.DeleteRequest;
import pico.erp.quotation.item.QuotationItemRequests.FixUnitPriceRequest;
import pico.erp.quotation.item.QuotationItemRequests.UpdateRequest;
import pico.erp.quotation.item.addition.QuotationItemAdditionRepository;
import pico.erp.quotation.item.data.QuotationItemData;
import pico.erp.quotation.item.data.QuotationItemId;
import pico.erp.shared.Public;
import pico.erp.shared.event.EventPublisher;

@SuppressWarnings("Duplicates")
@Service
@Public
@Transactional
@Validated
public class QuotationItemServiceLogic implements QuotationItemService {

  @Autowired
  private EventPublisher eventPublisher;

  @Autowired
  private QuotationMapper mapper;

  @Autowired
  private QuotationItemRepository quotationItemRepository;

  @Autowired
  private QuotationItemAdditionRepository quotationItemAdditionRepository;

  @Override
  public QuotationItemData create(CreateRequest request) {
    val item = new QuotationItem();
    val response = item.apply(mapper.map(request));
    val additionRates = quotationItemAdditionRepository.findAllBy(item.getQuotation().getId())
      .collect(Collectors.toList());
    val responseApplyItemAddition = item
      .apply(new ApplyItemAdditionRequest(additionRates));
    val created = quotationItemRepository.create(item);
    eventPublisher.publishEvents(response.getEvents());
    eventPublisher.publishEvents(responseApplyItemAddition.getEvents());
    return mapper.map(created);
  }

  @Override
  public void delete(DeleteRequest request) {
    val item = quotationItemRepository.findBy(request.getId())
      .orElseThrow(QuotationItemExceptions.NotFoundException::new);
    val response = item.apply(mapper.map(request));
    quotationItemRepository.deleteBy(item.getId());
    eventPublisher.publishEvents(response.getEvents());
  }

  @Override
  public boolean exists(QuotationItemId id) {
    return quotationItemRepository.exists(id);
  }

  @Override
  public void fixUnitPrice(FixUnitPriceRequest request) {
    val item = quotationItemRepository.findBy(request.getId())
      .orElseThrow(QuotationItemExceptions.NotFoundException::new);
    val response = item.apply(mapper.map(request));
    quotationItemRepository.update(item);
    eventPublisher.publishEvents(response.getEvents());
  }

  @Override
  public QuotationItemData get(QuotationItemId id) {
    return quotationItemRepository.findBy(id)
      .map(mapper::map)
      .orElseThrow(QuotationItemExceptions.NotFoundException::new);

  }

  @Override
  public List<QuotationItemData> getAll(QuotationId quotationId) {
    return quotationItemRepository.findAllBy(quotationId)
      .map(mapper::map)
      .collect(Collectors.toList());
  }

  @Override
  public void update(UpdateRequest request) {
    val item = quotationItemRepository.findBy(request.getId())
      .orElseThrow(QuotationItemExceptions.NotFoundException::new);
    val response = item.apply(mapper.map(request));
    quotationItemRepository.update(item);
    eventPublisher.publishEvents(response.getEvents());
  }
}
