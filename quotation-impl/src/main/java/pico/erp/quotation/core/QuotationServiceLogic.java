package pico.erp.quotation.core;

import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import pico.erp.quotation.QuotationExceptions.NotFoundException;
import pico.erp.quotation.QuotationRequests.CancelRequest;
import pico.erp.quotation.QuotationRequests.CommitRequest;
import pico.erp.quotation.QuotationRequests.DeleteRequest;
import pico.erp.quotation.QuotationRequests.DraftRequest;
import pico.erp.quotation.QuotationRequests.ExpireRequest;
import pico.erp.quotation.QuotationRequests.NextDraftRequest;
import pico.erp.quotation.QuotationRequests.PrepareRequest;
import pico.erp.quotation.QuotationRequests.PrintSheetRequest;
import pico.erp.quotation.QuotationRequests.UpdateRequest;
import pico.erp.quotation.QuotationService;
import pico.erp.quotation.data.QuotationData;
import pico.erp.quotation.data.QuotationId;
import pico.erp.quotation.domain.Quotation;
import pico.erp.shared.Public;
import pico.erp.shared.data.ContentInputStream;
import pico.erp.shared.event.EventPublisher;

@SuppressWarnings("Duplicates")
@Service
@Public
@Transactional
@Validated
public class QuotationServiceLogic implements QuotationService {

  @Autowired
  QuotationPrinter exporter;

  @Autowired
  private QuotationRepository quotationRepository;

  @Autowired
  private EventPublisher eventPublisher;

  @Autowired
  private QuotationMapper mapper;

  @Autowired
  private QuotationPrintMapper quotationPrintMapper;

  @Override
  public void cancel(CancelRequest request) {
    val quotation = quotationRepository.findBy(request.getId())
      .orElseThrow(NotFoundException::new);
    val response = quotation.apply(mapper.map(request));
    quotationRepository.update(quotation);
    eventPublisher.publishEvents(response.getEvents());
  }


  @Override
  public void commit(CommitRequest request) {
    val quotation = quotationRepository.findBy(request.getId())
      .orElseThrow(NotFoundException::new);
    val response = quotation.apply(mapper.map(request));
    quotationRepository.update(quotation);
    eventPublisher.publishEvents(response.getEvents());
  }

  @Override
  public void prepare(PrepareRequest request) {
    val quotation = quotationRepository.findBy(request.getId())
      .orElseThrow(NotFoundException::new);
    val response = quotation.apply(mapper.map(request));
    quotationRepository.update(quotation);
    eventPublisher.publishEvents(response.getEvents());
  }

  @Override
  public void delete(DeleteRequest request) {
    val quotation = quotationRepository.findBy(request.getId())
      .orElseThrow(NotFoundException::new);
    val response = quotation.apply(mapper.map(request));
    quotationRepository.update(quotation);
    eventPublisher.publishEvents(response.getEvents());

  }

  @Override
  public QuotationData draft(DraftRequest request) {
    val quotation = new Quotation();
    val response = quotation.apply(mapper.map(request));
    val created = quotationRepository.create(quotation);
    eventPublisher.publishEvents(response.getEvents());
    return mapper.map(created);
  }

  @Override
  public boolean exists(QuotationId id) {
    return quotationRepository.exists(id);
  }

  @Override
  public void expire(ExpireRequest request) {
    quotationRepository.findAllExpireCandidateBeforeThan(request.getFixedDate())
      .forEach(quotation -> {
        val response = quotation.apply(mapper.map(request));
        quotationRepository.update(quotation);
        eventPublisher.publishEvents(response.getEvents());
      });
  }

  @Override
  public QuotationData get(QuotationId id) {
    return quotationRepository.findBy(id)
      .map(mapper::map)
      .orElseThrow(NotFoundException::new);
  }

  @Override
  public QuotationData nextDraft(NextDraftRequest request) {
    val quotation = quotationRepository.findBy(request.getId())
      .orElseThrow(NotFoundException::new);
    val response = quotation.apply(mapper.map(request));

    val nextDrafted = quotationRepository.create(response.getNextDrafted());
    quotationRepository.update(quotation);

    eventPublisher.publishEvents(response.getEvents());

    return mapper.map(nextDrafted);
  }

  @Override
  public ContentInputStream printSheet(PrintSheetRequest request) {
    val quotation = quotationRepository.findAggregatorBy(request.getId())
      .orElseThrow(NotFoundException::new);
    return exporter.printSheet(quotationPrintMapper.map(quotation), request.getOptions());
  }

  @Override
  public void update(UpdateRequest request) {
    val quotation = quotationRepository.findBy(request.getId())
      .orElseThrow(NotFoundException::new);
    val response = quotation.apply(mapper.map(request));
    quotationRepository.update(quotation);
    eventPublisher.publishEvents(response.getEvents());
  }

}
