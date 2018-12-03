package pico.erp.quotation.addition;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.event.EventListener;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pico.erp.bom.BomService;
import pico.erp.quotation.QuotationEvents;

@SuppressWarnings("unused")
@Component
@Transactional
public class QuotationAdditionEventListener {

  private static final String LISTENER_NAME = "listener.quotation-addition-event-listener";

  @Lazy
  @Autowired
  private BomService bomService;

  @Autowired
  private QuotationAdditionServiceLogic quotationAdditionService;

  @EventListener
  @JmsListener(destination = LISTENER_NAME + "." + QuotationEvents.NextDraftedEvent.CHANNEL)
  public void onQuotationNextDrafted(QuotationEvents.NextDraftedEvent event) {
    quotationAdditionService.nextDraft(
      QuotationAdditionServiceLogic.NextDraftRequest.builder()
        .quotationId(event.getQuotationId())
        .build()
    );
  }

  @EventListener
  @JmsListener(destination = LISTENER_NAME + "." + QuotationEvents.PreparedEvent.CHANNEL)
  public void onQuotationPrepared(QuotationEvents.PreparedEvent event) {
    quotationAdditionService.generate(
      QuotationAdditionServiceLogic.GenerateRequest.builder()
        .quotationId(event.getQuotationId())
        .build()
    );
  }


}
