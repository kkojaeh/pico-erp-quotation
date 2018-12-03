package pico.erp.quotation.item;

import java.util.List;
import java.util.stream.Collectors;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import pico.erp.item.ItemId;
import pico.erp.quotation.QuotationId;
import pico.erp.quotation.QuotationRepository;
import pico.erp.quotation.item.QuotationItemMessages.ApplyItemAdditionRequest;
import pico.erp.quotation.item.QuotationItemRequests.CreateRequest;
import pico.erp.quotation.item.QuotationItemRequests.DeleteRequest;
import pico.erp.quotation.item.QuotationItemRequests.FixUnitPriceRequest;
import pico.erp.quotation.item.QuotationItemRequests.UpdateRequest;
import pico.erp.quotation.item.addition.QuotationItemAdditionRepository;
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
  private QuotationItemMapper mapper;

  @Autowired
  private QuotationRepository quotationRepository;

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

  public void applyItemAdditions(ApplyItemAdditionsRequest request) {
    val itemAdditions = quotationItemAdditionRepository.findAllBy(request.getQuotationId())
      .collect(Collectors.toList());
    quotationItemRepository.findAllBy(request.getQuotationId())
      .forEach(item -> {
        val response = item
          .apply(new QuotationItemMessages.ApplyItemAdditionRequest(itemAdditions));
        quotationItemRepository.update(item);
        eventPublisher.publishEvents(response.getEvents());
      });
  }

  @Override
  public void update(UpdateRequest request) {
    val item = quotationItemRepository.findBy(request.getId())
      .orElseThrow(QuotationItemExceptions.NotFoundException::new);
    val response = item.apply(mapper.map(request));
    quotationItemRepository.update(item);
    eventPublisher.publishEvents(response.getEvents());
  }

  @Override
  public List<QuotationItemData> getAll(ItemId itemId) {
    return quotationItemRepository.findAllBy(itemId)
      .map(mapper::map)
      .collect(Collectors.toList());
  }

  public void nextDraft(NextDraftRequest request) {
    val quotation = quotationRepository.findBy(request.getQuotationId())
      .orElseThrow(pico.erp.quotation.QuotationExceptions.NotFoundException::new);

    quotationItemRepository.findAllBy(quotation.getPreviousId())
      .forEach(item -> {
        val response = item.apply(new QuotationItemMessages.NextDraftRequest(quotation));
        quotationItemRepository.create(response.getNextDrafted());
        eventPublisher.publishEvents(response.getEvents());
      });
  }

  @Getter
  @Builder
  public static class NextDraftRequest {

    @Valid
    @NotNull
    QuotationId quotationId;

  }

  @Getter
  @Builder
  public static class ApplyItemAdditionsRequest {

    @Valid
    @NotNull
    QuotationId quotationId;

  }

}
