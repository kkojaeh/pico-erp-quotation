package pico.erp.quotation.item;

import kkojaeh.spring.boot.component.ComponentAutowired;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pico.erp.bom.BomEvents;
import pico.erp.bom.BomEvents.DeterminedEvent;
import pico.erp.bom.BomId;
import pico.erp.bom.BomService;
import pico.erp.quotation.QuotationEvents;
import pico.erp.quotation.QuotationServiceLogic;

@SuppressWarnings("unused")
@Component
@Transactional
public class QuotationItemEventListener {

  private static final String LISTENER_NAME = "listener.quotation-item-event-listener";

  @Autowired
  private QuotationItemServiceLogic quotationItemService;

  @Autowired
  private QuotationServiceLogic quotationService;

  @ComponentAutowired
  private BomService bomService;


  @EventListener
  @JmsListener(destination = LISTENER_NAME + "." + BomEvents.DeterminedEvent.CHANNEL)
  public void onBomDetermined(DeterminedEvent event) {
    verifyQuotationBy(event.getBomId());
  }

  @EventListener
  @JmsListener(destination = LISTENER_NAME + "." + BomEvents.EstimatedUnitCostChangedEvent.CHANNEL)
  public void onBomEstimatedUnitCostChanged(BomEvents.EstimatedUnitCostChangedEvent event) {
    verifyQuotationBy(event.getBomId());
  }

  @EventListener
  @JmsListener(destination = LISTENER_NAME + "." + BomEvents.NextRevisionCreatedEvent.CHANNEL)
  public void onBomNextRevisionCreated(BomEvents.NextRevisionCreatedEvent event) {
    verifyQuotationBy(event.getBomId());
  }

  @EventListener
  @JmsListener(destination = LISTENER_NAME + "."
    + QuotationEvents.AdditionChangedEvent.CHANNEL)
  public void onQuotationAdditionChanged(
    QuotationEvents.AdditionChangedEvent event) {
    quotationItemService.applyItemAdditions(
      QuotationItemServiceLogic.ApplyItemAdditionsRequest.builder()
        .quotationId(event.getQuotationId())
        .build()
    );
  }

  @EventListener
  @JmsListener(destination = LISTENER_NAME + "." + QuotationEvents.NextDraftedEvent.CHANNEL)
  public void onQuotationNextDrafted(QuotationEvents.NextDraftedEvent event) {
    quotationItemService.nextDraft(
      QuotationItemServiceLogic.NextDraftRequest.builder()
        .quotationId(event.getQuotationId())
        .build()
    );
  }

  private void verifyQuotationBy(BomId id) {
    val bom = bomService.get(id);

    quotationItemService.getAll(bom.getItemId())
      .forEach(item -> {
        quotationService.verify(
          QuotationServiceLogic.VerifyRequest.builder()
            .id(item.getQuotationId())
            .build()
        );
      });
  }


}
