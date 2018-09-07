package pico.erp.quotation.core;

import java.util.stream.Collectors;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.event.EventListener;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pico.erp.bom.BomEvents.DeterminedEvent;
import pico.erp.bom.BomEvents.EstimatedUnitCostChangedEvent;
import pico.erp.bom.BomEvents.NextRevisionCreatedEvent;
import pico.erp.bom.BomService;
import pico.erp.bom.data.BomId;
import pico.erp.quotation.QuotationEvents;
import pico.erp.quotation.QuotationItemAdditionEvents;
import pico.erp.quotation.QuotationItemAdditionExceptions;
import pico.erp.quotation.QuotationItemAdditionExceptions.NotFoundException;
import pico.erp.quotation.data.QuotationItemAdditionId;
import pico.erp.quotation.domain.QuotationItemMessages;
import pico.erp.quotation.domain.QuotationItemMessages.ApplyItemAdditionRequest;
import pico.erp.quotation.domain.QuotationMessages;
import pico.erp.shared.event.EventPublisher;

@SuppressWarnings("unused")
@Component
@Transactional
public class QuotationItemEventListener {

  private static final String LISTENER_NAME = "listener.quotation-item-event-listener";

  @Autowired
  private QuotationRepository quotationRepository;

  @Autowired
  private QuotationItemRepository quotationItemRepository;

  @Autowired
  private QuotationItemAdditionRepository quotationItemAdditionRepository;

  @Autowired
  private EventPublisher eventPublisher;

  @Lazy
  @Autowired
  private BomService bomService;


  @EventListener
  @JmsListener(destination = LISTENER_NAME + "." + DeterminedEvent.CHANNEL)
  public void onBomDetermined(DeterminedEvent event) {
    verifyQuotationBy(event.getBomId());
  }

  @EventListener
  @JmsListener(destination = LISTENER_NAME + "." + EstimatedUnitCostChangedEvent.CHANNEL)
  public void onBomEstimatedUnitCostChanged(EstimatedUnitCostChangedEvent event) {
    val bom = bomService.get(event.getBomId());
    quotationItemRepository.findAllBy(bom.getItemId())
      .forEach(item -> {
        if (item.getQuotation().canModify()) {
          val response = item.apply(new QuotationItemMessages.VerifyRequest());
          quotationItemRepository.update(item);
          eventPublisher.publishEvents(response.getEvents());
        }
      });
  }

  private void onQuotationItemAdditionChanged(QuotationItemAdditionId id) {
    val itemAddition = quotationItemAdditionRepository
      .findBy(id)
      .orElseThrow(QuotationItemAdditionExceptions.NotFoundException::new);
    val quotation = itemAddition.getQuotation();
    if (!quotation.canModify()) {
      return;
    }
    val itemAdditions = quotationItemAdditionRepository.findAllBy(quotation.getId())
      .collect(Collectors.toList());
    quotationItemRepository.findAllBy(quotation.getId())
      .forEach(item -> {
        val response = item
          .apply(new ApplyItemAdditionRequest(itemAdditions));
        quotationItemRepository.update(item);
        eventPublisher.publishEvents(response.getEvents());
      });
  }

  @EventListener
  @JmsListener(destination = LISTENER_NAME + "." + QuotationEvents.NextDraftedEvent.CHANNEL)
  public void onQuotationNextDrafted(QuotationEvents.NextDraftedEvent event) {
    val quotation = quotationRepository.findBy(event.getQuotationId())
      .orElseThrow(pico.erp.quotation.QuotationExceptions.NotFoundException::new);

    quotationItemRepository.findAllBy(quotation.getPreviousId())
      .forEach(item -> {
        val response = item.apply(new QuotationItemMessages.NextDraftRequest(quotation));
        quotationItemRepository.create(response.getNextDrafted());
        eventPublisher.publishEvents(response.getEvents());
      });
  }

  @EventListener
  @JmsListener(destination = LISTENER_NAME + "."
    + QuotationItemAdditionEvents.CreatedEvent.CHANNEL)
  public void onQuotationItemAdditionCreated(
    QuotationItemAdditionEvents.CreatedEvent event) {
    onQuotationItemAdditionChanged(event.getQuotationItemAdditionId());
  }

  @EventListener
  @JmsListener(destination = LISTENER_NAME + "."
    + QuotationItemAdditionEvents.DeletedEvent.CHANNEL)
  public void onQuotationItemAdditionDeleted(
    QuotationItemAdditionEvents.DeletedEvent event) {
    onQuotationItemAdditionChanged(event.getQuotationItemAdditionId());
  }

  @EventListener
  @JmsListener(destination = LISTENER_NAME + "."
    + QuotationItemAdditionEvents.UpdatedEvent.CHANNEL)
  public void onQuotationItemAdditionUpdated(
    QuotationItemAdditionEvents.UpdatedEvent event) {
    onQuotationItemAdditionChanged(event.getQuotationItemAdditionId());
  }

  @EventListener
  @JmsListener(destination = LISTENER_NAME + "." + NextRevisionCreatedEvent.CHANNEL)
  public void onBomNextRevisionCreated(NextRevisionCreatedEvent event) {
    verifyQuotationBy(event.getBomId());
  }

  private void verifyQuotationBy(BomId id) {
    val bom = bomService.get(id);

    quotationItemRepository.findAllBy(bom.getItemId())
      .forEach(item -> {
        if (item.getQuotation().canModify()) {
          val aggregator = quotationRepository.findAggregatorBy(item.getQuotation().getId())
            .orElseThrow(pico.erp.quotation.QuotationExceptions.NotFoundException::new);
          val response = aggregator.apply(new QuotationMessages.VerifyRequest());
          quotationRepository.update(aggregator);
          eventPublisher.publishEvents(response.getEvents());
        }
      });
  }


}
