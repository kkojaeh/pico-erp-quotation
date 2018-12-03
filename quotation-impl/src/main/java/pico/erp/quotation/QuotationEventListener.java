package pico.erp.quotation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@SuppressWarnings("unused")
@Component
@Transactional
public class QuotationEventListener {

  private static final String LISTENER_NAME = "listener.quotation-event-listener";

  @Autowired
  private QuotationServiceLogic quotationService;

  @EventListener
  @JmsListener(destination = LISTENER_NAME + "."
    + QuotationEvents.MemberChangedEvent.CHANNEL)
  public void onQuotationMemberChanged(
    QuotationEvents.MemberChangedEvent event) {

    quotationService.verify(
      QuotationServiceLogic.VerifyRequest.builder()
        .id(event.getQuotationId())
        .build()
    );
  }

}
