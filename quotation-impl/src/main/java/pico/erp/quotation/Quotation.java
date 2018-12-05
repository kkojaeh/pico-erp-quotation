package pico.erp.quotation;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import lombok.val;
import pico.erp.attachment.AttachmentId;
import pico.erp.audit.annotation.Audit;
import pico.erp.comment.subject.CommentSubjectId;
import pico.erp.company.CompanyData;
import pico.erp.project.ProjectData;
import pico.erp.quotation.QuotationExceptions.CannotCancelException;
import pico.erp.quotation.QuotationExceptions.CannotCommitException;
import pico.erp.quotation.QuotationExceptions.CannotExpireException;
import pico.erp.quotation.QuotationExceptions.CannotNextDraftException;
import pico.erp.quotation.QuotationMessages.CancelRequest;
import pico.erp.quotation.QuotationMessages.CancelResponse;
import pico.erp.quotation.QuotationMessages.CommitRequest;
import pico.erp.quotation.QuotationMessages.CommitResponse;
import pico.erp.quotation.QuotationMessages.DeleteRequest;
import pico.erp.quotation.QuotationMessages.DeleteResponse;
import pico.erp.quotation.QuotationMessages.DraftRequest;
import pico.erp.quotation.QuotationMessages.DraftResponse;
import pico.erp.quotation.QuotationMessages.ExpireRequest;
import pico.erp.quotation.QuotationMessages.ExpireResponse;
import pico.erp.quotation.QuotationMessages.NextDraftRequest;
import pico.erp.quotation.QuotationMessages.NextDraftResponse;
import pico.erp.quotation.QuotationMessages.PrepareRequest;
import pico.erp.quotation.QuotationMessages.PrepareResponse;
import pico.erp.quotation.QuotationMessages.PrintSheetRequest;
import pico.erp.quotation.QuotationMessages.PrintSheetResponse;
import pico.erp.quotation.QuotationMessages.UpdateRequest;
import pico.erp.quotation.QuotationMessages.UpdateResponse;
import pico.erp.shared.data.Auditor;
import pico.erp.shared.event.Event;
import pico.erp.user.UserData;

@Builder(toBuilder = true)
@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "id")
@Audit(alias = "quotation")
public class Quotation {

  QuotationId id;

  QuotationCode code;

  QuotationId previousId;

  CommentSubjectId commentSubjectId;

  int revision;

  ProjectData project;

  CompanyData customer;

  String name;

  QuotationExpiryPolicyKind expiryPolicy;

  UserData manager;

  Auditor committer;

  OffsetDateTime committedDate;

  Auditor canceler;

  OffsetDateTime canceledDate;

  QuotationStatusKind status;

  String protectedDescription;

  String publicDescription;

  AttachmentId attachmentId;

  OffsetDateTime expirationDate;

  boolean committable;

  boolean preparable;

  /**
   * 총 부가(기초비/별도청구) 금액
   */
  BigDecimal totalAdditionAmount;

  /**
   * 총 금액 <p> 총 품목 금액 + 총 부가 금액 </p>
   */
  BigDecimal totalAmount;

  /**
   * 총 품목금액
   */
  BigDecimal totalItemAmount;

  /**
   * 총 품목 할인된 금액
   */
  BigDecimal totalItemDiscountedAmount;

  /**
   * 총 품목 할인율
   */
  BigDecimal totalItemDiscountedRate;

  /**
   * 총 품목 최초 금액
   */
  BigDecimal totalItemOriginalAmount;

  public Quotation() {
    committable = false;
    preparable = false;
  }

  public CancelResponse apply(CancelRequest request) {
    if (!this.isCancelable()) {
      throw new CannotCancelException();
    }
    this.status = QuotationStatusKind.CANCELED;
    this.canceler = request.getCanceler();
    this.canceledDate = OffsetDateTime.now();
    return new CancelResponse(
      Arrays.asList(new QuotationEvents.CanceledEvent(this.id)));
  }

  public CommitResponse apply(CommitRequest request) {
    if (!this.isCommittable()) {
      throw new CannotCommitException();
    }
    this.status = QuotationStatusKind.COMMITTED;
    this.committer = request.getCommitter();
    this.committedDate = OffsetDateTime.now();
    this.expirationDate = expiryPolicy.resolveExpirationDate(this.committedDate);
    this.committable = false;
    this.preparable = false;
    return new CommitResponse(
      Arrays.asList(new QuotationEvents.CommittedEvent(this.id))
    );
  }

  public DraftResponse apply(DraftRequest request) {
    id = request.getId();
    revision = 1;
    name = request.getName();
    status = QuotationStatusKind.DRAFT;
    expiryPolicy = request.getExpiryPolicy();
    project = request.getProject();
    customer = request.getCustomer();
    manager = request.getManager();
    commentSubjectId = CommentSubjectId.from(id.getValue().toString());
    protectedDescription = request.getProtectedDescription();
    publicDescription = request.getPublicDescription();
    attachmentId = request.getAttachmentId();
    code = request.getCodeGenerator().generate(this);
    return new DraftResponse(
      Arrays.asList(new QuotationEvents.DraftedEvent(this.id))
    );
  }

  public NextDraftResponse apply(NextDraftRequest request) {
    // 제출 상태에서만 다음 버전을 생성할 수 있다
    if (!isNextDraftable()) {
      throw new CannotNextDraftException();
    }
    Quotation draft = toBuilder().build();
    draft.id = request.getNextId();
    draft.previousId = id;
    draft.revision = revision + 1;
    draft.status = QuotationStatusKind.IN_NEGOTIATION;
    draft.expirationDate = null;
    draft.committedDate = null;
    draft.committable = true;
    draft.preparable = false;
    draft.code = request.getCodeGenerator().generate(draft);

    status = QuotationStatusKind.DESTROYED;
    return new NextDraftResponse(draft, Arrays.asList(
      new QuotationEvents.DestroyedEvent(this.id),
      new QuotationEvents.NextDraftedEvent(draft.id)
    ));
  }

  public PrintSheetResponse apply(PrintSheetRequest request) {
    return new PrintSheetResponse(Collections.emptyList());
  }

  public UpdateResponse apply(UpdateRequest request) {
    name = request.getName();
    status = QuotationStatusKind.DRAFT;
    expiryPolicy = request.getExpiryPolicy();
    project = request.getProject();
    customer = request.getCustomer();
    manager = request.getManager();
    protectedDescription = request.getProtectedDescription();
    publicDescription = request.getPublicDescription();
    attachmentId = request.getAttachmentId();
    return new UpdateResponse(
      Arrays.asList(new QuotationEvents.UpdatedEvent(this.id))
    );
  }

  public DeleteResponse apply(DeleteRequest request) {
    return new DeleteResponse(Collections.emptyList());
  }

  public ExpireResponse apply(ExpireRequest request) {
    if (!status.isExpirable()) {
      throw new CannotExpireException();
    }
    status = status.getExpiredStatus();
    val events = new HashSet<Event>();
    if (status == QuotationStatusKind.CANCELED) {
      events.add(new QuotationEvents.CanceledEvent(this.id));
    } else if (status == QuotationStatusKind.EXPIRED) {
      events.add(new QuotationEvents.ExpiredEvent(this.id));
    }
    return new ExpireResponse(events);
  }

  public PrepareResponse apply(PrepareRequest request) {
    if (!status.isPreparable()) {
      throw new QuotationExceptions.CannotPrepareException();
    }
    status = QuotationStatusKind.PREPARED;
    preparable = false;
    committable = true;
    return new PrepareResponse(
      Arrays.asList(new QuotationEvents.PreparedEvent(this.id))
    );
  }

  public boolean isUpdatable() {
    return status == QuotationStatusKind.DRAFT || status == QuotationStatusKind.IN_NEGOTIATION
      || status == QuotationStatusKind.PREPARED;
  }

  public boolean isCancelable() {
    return status.isCancelable();
  }

  public boolean isNextDraftable() {
    return status == QuotationStatusKind.COMMITTED;
  }

  public boolean isSheetPrintable() {
    return status == QuotationStatusKind.COMMITTED;
  }


}
