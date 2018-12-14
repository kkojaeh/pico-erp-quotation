package pico.erp.quotation.addition;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import pico.erp.bom.BomService;
import pico.erp.bom.process.BomProcessService;
import pico.erp.process.ProcessService;
import pico.erp.process.preparation.ProcessPreparationService;
import pico.erp.quotation.QuotationExceptions;
import pico.erp.quotation.QuotationId;
import pico.erp.quotation.QuotationRepository;
import pico.erp.quotation.addition.QuotationAdditionExceptions.NotFoundException;
import pico.erp.quotation.addition.QuotationAdditionRequests.CreateRequest;
import pico.erp.quotation.addition.QuotationAdditionRequests.DeleteRequest;
import pico.erp.quotation.addition.QuotationAdditionRequests.GenerateByProcessPreparationRequest;
import pico.erp.quotation.addition.QuotationAdditionRequests.NextDraftRequest;
import pico.erp.quotation.addition.QuotationAdditionRequests.UpdateRequest;
import pico.erp.quotation.item.QuotationItemRepository;
import pico.erp.shared.Public;
import pico.erp.shared.event.EventPublisher;

@SuppressWarnings("Duplicates")
@Service
@Public
@Transactional
@Validated
public class QuotationAdditionServiceLogic implements QuotationAdditionService {

  @Autowired
  private EventPublisher eventPublisher;

  @Autowired
  private QuotationAdditionMapper mapper;

  @Autowired
  private QuotationRepository quotationRepository;

  @Autowired
  private QuotationItemRepository quotationItemRepository;

  @Autowired
  private QuotationAdditionRepository quotationAdditionRepository;

  @Lazy
  @Autowired
  private BomService bomService;

  @Lazy
  @Autowired
  private BomProcessService bomProcessService;

  @Lazy
  @Autowired
  private ProcessService processService;

  @Lazy
  @Autowired
  private ProcessPreparationService processPreparationService;

  @Override
  public QuotationAdditionData create(CreateRequest request) {
    val addition = new QuotationAddition();
    val response = addition.apply(mapper.map(request));
    val created = quotationAdditionRepository.create(addition);
    eventPublisher.publishEvents(response.getEvents());
    return mapper.map(created);
  }

  @Override
  public void delete(DeleteRequest request) {
    val addition = quotationAdditionRepository.findBy(request.getId())
      .orElseThrow(NotFoundException::new);
    val response = addition.apply(mapper.map(request));
    quotationAdditionRepository.deleteBy(addition.getId());
    eventPublisher.publishEvents(response.getEvents());
  }

  @Override
  public boolean exists(QuotationAdditionId id) {
    return quotationAdditionRepository.exists(id);
  }

  @Override
  public QuotationAdditionData get(QuotationAdditionId id) {
    return quotationAdditionRepository.findBy(id)
      .map(mapper::map)
      .orElseThrow(NotFoundException::new);
  }

  @Override
  public List<QuotationAdditionData> getAll(QuotationId quotationId) {
    return quotationAdditionRepository.findAllBy(quotationId)
      .map(mapper::map)
      .collect(Collectors.toList());
  }

  @Override
  public void update(UpdateRequest request) {
    val item = quotationAdditionRepository.findBy(request.getId())
      .orElseThrow(NotFoundException::new);
    val response = item.apply(mapper.map(request));
    quotationAdditionRepository.update(item);
    eventPublisher.publishEvents(response.getEvents());
  }

  @Override
  public void generate(GenerateByProcessPreparationRequest request) {
    val quotation = quotationRepository.findBy(request.getQuotationId())
      .orElseThrow(QuotationExceptions.NotFoundException::new);

    quotationItemRepository.findAllBy(quotation.getId())
      .forEach(item -> {
        val hierarchyBom = bomService.getHierarchy(item.getBom().getId());
        hierarchyBom.visitPostOrder((bom, parents) -> {
          val bomId = bom.getId();
          bomProcessService.getAll(bomId).forEach(bomProcess -> {
            val processId = bomProcess.getProcessId();
            val preparations = processPreparationService.getAll(processId);
            preparations.forEach(preparation -> {
              val addition = new QuotationAddition();
              val response = addition.apply(
                QuotationAdditionMessages.CreateRequest.builder()
                  .id(QuotationAdditionId.generate())
                  .quotation(quotation)
                  .name(preparation.getName())
                  .description(preparation.getDescription())
                  .quantity(BigDecimal.ONE)
                  .unitPrice(preparation.getChargeCost())
                  .build()
              );
              quotationAdditionRepository.create(addition);
              eventPublisher.publishEvents(response.getEvents());
            });
          });
        });
      });
  }

  @Override
  public void nextDraft(NextDraftRequest request) {
    val quotation = quotationRepository.findBy(request.getQuotationId())
      .orElseThrow(QuotationExceptions.NotFoundException::new);

    quotationAdditionRepository.findAllBy(quotation.getPreviousId())
      .forEach(addition -> {
        val response = addition.apply(new QuotationAdditionMessages.NextDraftRequest(quotation));
        quotationAdditionRepository.create(response.getNextDrafted());
        eventPublisher.publishEvents(response.getEvents());
      });
  }

}
