package pico.erp.quotation.item.addition;

import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pico.erp.quotation.QuotationEvents;
import pico.erp.quotation.QuotationExceptions.NotFoundException;
import pico.erp.quotation.QuotationRepository;
import pico.erp.shared.event.EventPublisher;

@SuppressWarnings("unused")
@Component
@Transactional
public class QuotationItemAdditionEventListener {

  private static final String LISTENER_NAME = "listener.quotation-item-addition-event-listener";

  @Autowired
  private QuotationRepository quotationRepository;

  @Autowired
  private QuotationItemAdditionRepository quotationItemAdditionRepository;

  @Autowired
  private EventPublisher eventPublisher;

  @EventListener
  @JmsListener(destination = LISTENER_NAME + "." + QuotationEvents.NextDraftedEvent.CHANNEL)
  public void onQuotationNextDrafted(QuotationEvents.NextDraftedEvent event) {
    val quotation = quotationRepository.findBy(event.getQuotationId())
      .orElseThrow(NotFoundException::new);

    quotationItemAdditionRepository.findAllBy(quotation.getPreviousId())
      .forEach(item -> {
        val response = item
          .apply(new QuotationItemAdditionMessages.NextDraftRequest(quotation));
        quotationItemAdditionRepository.create(response.getNextDrafted());
        eventPublisher.publishEvents(response.getEvents());
      });
  }


}
