package pico.erp.quotation.addition;

import java.math.BigDecimal;
import java.util.Optional;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.event.EventListener;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pico.erp.bom.BomService;
import pico.erp.process.PreprocessService;
import pico.erp.process.ProcessService;
import pico.erp.quotation.QuotationEvents;
import pico.erp.quotation.QuotationExceptions.NotFoundException;
import pico.erp.quotation.QuotationRepository;
import pico.erp.quotation.addition.data.QuotationAdditionId;
import pico.erp.quotation.item.QuotationItemRepository;
import pico.erp.shared.event.EventPublisher;

@SuppressWarnings("unused")
@Component
@Transactional
public class QuotationAdditionEventListener {

  private static final String LISTENER_NAME = "listener.quotation-addition-event-listener";

  @Autowired
  private QuotationRepository quotationRepository;

  @Autowired
  private QuotationAdditionRepository quotationAdditionRepository;

  @Autowired
  private QuotationItemRepository quotationItemRepository;

  @Autowired
  private EventPublisher eventPublisher;

  @Lazy
  @Autowired
  private BomService bomService;

  @Lazy
  @Autowired
  private ProcessService processService;

  @Lazy
  @Autowired
  private PreprocessService preprocessService;

  @EventListener
  @JmsListener(destination = LISTENER_NAME + "." + QuotationEvents.NextDraftedEvent.CHANNEL)
  public void onQuotationNextDrafted(QuotationEvents.NextDraftedEvent event) {
    val quotation = quotationRepository.findBy(event.getQuotationId())
      .orElseThrow(NotFoundException::new);

    quotationAdditionRepository.findAllBy(quotation.getPreviousId())
      .forEach(addition -> {
        val response = addition.apply(new QuotationAdditionMessages.NextDraftRequest(quotation));
        quotationAdditionRepository.create(response.getNextDrafted());
        eventPublisher.publishEvents(response.getEvents());
      });
  }

  @EventListener
  @JmsListener(destination = LISTENER_NAME + "." + QuotationEvents.PreparedEvent.CHANNEL)
  public void onQuotationPrepared(QuotationEvents.PreparedEvent event) {
    val quotation = quotationRepository.findBy(event.getQuotationId())
      .orElseThrow(NotFoundException::new);

    quotationItemRepository.findAllBy(quotation.getId())
      .forEach(item -> {
        val hierarchyBom = bomService.getHierarchy(item.getBomData().getId());
        hierarchyBom.visit((bom, level) -> {
          Optional.ofNullable(bom.getProcessId())
            .ifPresent(processId -> {
              val preprocesses = preprocessService.getAll(bom.getProcessId());
              preprocesses.forEach(preprocess -> {
                val addition = new QuotationAddition();
                val response = addition.apply(
                  QuotationAdditionMessages.CreateRequest.builder()
                    .id(QuotationAdditionId.generate())
                    .quotation(quotation)
                    .name(preprocess.getName())
                    .description(preprocess.getDescription())
                    .quantity(BigDecimal.ONE)
                    .unitPrice(preprocess.getChargeCost())
                    .build()
                );
                quotationAdditionRepository.create(addition);
                eventPublisher.publishEvents(response.getEvents());
              });
            });
        });
      });
  }


}
