package pico.erp.quotation;

import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pico.erp.shared.event.EventPublisher;

@SuppressWarnings("unused")
@Component
@Transactional
public class QuotationEventListener {

  private static final String LISTENER_NAME = "listener.quotation-event-listener";

  @Autowired
  private QuotationRepository quotationRepository;

  @Autowired
  private EventPublisher eventPublisher;

  @EventListener
  @JmsListener(destination = LISTENER_NAME + "."
    + QuotationEvents.MemberChangedEvent.CHANNEL)
  public void onQuotationMemberChanged(
    QuotationEvents.MemberChangedEvent event) {

    val aggregator = quotationRepository.findAggregatorBy(event.getQuotationId()).get();
    if (aggregator.canModify()) {
      val response = aggregator.apply(new QuotationMessages.VerifyRequest());
      quotationRepository.update(aggregator);
      eventPublisher.publishEvents(response.getEvents());
    }
  }

}
