package pico.erp.quotation.item.addition;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pico.erp.quotation.QuotationEvents;

@SuppressWarnings("unused")
@Component
@Transactional
public class QuotationItemAdditionEventListener {

  private static final String LISTENER_NAME = "listener.quotation-item-addition-event-listener";

  @Autowired
  private QuotationItemAdditionServiceLogic quotationItemAdditionService;

  @EventListener
  @JmsListener(destination = LISTENER_NAME + "." + QuotationEvents.NextDraftedEvent.CHANNEL)
  public void onQuotationNextDrafted(QuotationEvents.NextDraftedEvent event) {

    quotationItemAdditionService.nextDraft(
      QuotationItemAdditionServiceLogic.NextDraftRequest.builder()
        .quotationId(event.getQuotationId())
        .build()
    );
  }


}
