package pico.erp.quotation.core;

import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pico.erp.quotation.QuotationAdditionEvents;
import pico.erp.quotation.QuotationAdditionExceptions.NotFoundException;
import pico.erp.quotation.QuotationItemEvents;
import pico.erp.quotation.data.QuotationAdditionId;
import pico.erp.quotation.data.QuotationItemId;
import pico.erp.quotation.domain.Quotation;
import pico.erp.quotation.domain.QuotationMessages;
import pico.erp.shared.event.EventPublisher;

@SuppressWarnings("unused")
@Component
@Transactional
public class QuotationEventListener {

  private static final String LISTENER_NAME = "listener.quotation-event-listener";

  @Autowired
  private QuotationRepository quotationRepository;

  @Autowired
  private QuotationItemRepository quotationItemRepository;

  @Autowired
  private QuotationAdditionRepository quotationAdditionRepository;

  @Autowired
  private EventPublisher eventPublisher;

  private void onQuotationAdditionChanged(QuotationAdditionId id) {
    val addition = quotationAdditionRepository.findBy(id)
      .orElseThrow(NotFoundException::new);
    verifyQuotation(addition.getQuotation());
  }

  @EventListener
  @JmsListener(destination = LISTENER_NAME + "."
    + QuotationAdditionEvents.CreatedEvent.CHANNEL)
  public void onQuotationAdditionCreated(
    QuotationAdditionEvents.CreatedEvent event) {
    onQuotationAdditionChanged(event.getQuotationAdditionId());
  }

  @EventListener
  @JmsListener(destination = LISTENER_NAME + "."
    + QuotationAdditionEvents.DeletedEvent.CHANNEL)
  public void onQuotationAdditionDeleted(
    QuotationAdditionEvents.DeletedEvent event) {
    onQuotationAdditionChanged(event.getQuotationAdditionId());
  }

  @EventListener
  @JmsListener(destination = LISTENER_NAME + "."
    + QuotationAdditionEvents.UpdatedEvent.CHANNEL)
  public void onQuotationAdditionUpdated(
    QuotationAdditionEvents.UpdatedEvent event) {
    onQuotationAdditionChanged(event.getQuotationAdditionId());
  }

  private void onQuotationItemChanged(QuotationItemId id) {
    val item = quotationItemRepository.findBy(id)
      .orElseThrow(pico.erp.quotation.QuotationItemExceptions.NotFoundException::new);
    verifyQuotation(item.getQuotation());
  }

  @EventListener
  @JmsListener(destination = LISTENER_NAME + "."
    + QuotationItemEvents.CreatedEvent.CHANNEL)
  public void onQuotationItemCreated(
    QuotationItemEvents.CreatedEvent event) {
    onQuotationItemChanged(event.getQuotationItemId());
  }

  @EventListener
  @JmsListener(destination = LISTENER_NAME + "."
    + QuotationItemEvents.DeletedEvent.CHANNEL)
  public void onQuotationItemDeleted(
    QuotationItemEvents.DeletedEvent event) {
    onQuotationItemChanged(event.getQuotationItemId());
  }

  @EventListener
  @JmsListener(destination = LISTENER_NAME + "."
    + QuotationItemEvents.UpdatedEvent.CHANNEL)
  public void onQuotationItemUpdated(
    QuotationItemEvents.UpdatedEvent event) {
    onQuotationItemChanged(event.getQuotationItemId());
  }

  private void verifyQuotation(Quotation quotation) {

    if (!quotation.canModify()) {
      return;
    }

    val aggregator = quotationRepository.findAggregatorBy(quotation.getId()).get();

    val response = aggregator.apply(new QuotationMessages.VerifyRequest());
    quotationRepository.update(aggregator);
    eventPublisher.publishEvents(response.getEvents());
  }

}
