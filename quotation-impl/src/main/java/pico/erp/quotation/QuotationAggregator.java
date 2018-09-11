package pico.erp.quotation;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import lombok.val;
import pico.erp.attachment.data.AttachmentId;
import pico.erp.comment.data.CommentSubjectId;
import pico.erp.company.data.CompanyData;
import pico.erp.project.data.ProjectData;
import pico.erp.quotation.QuotationMessages.VerifyRequest;
import pico.erp.quotation.QuotationMessages.VerifyResponse;
import pico.erp.quotation.addition.QuotationAddition;
import pico.erp.quotation.data.QuotationCode;
import pico.erp.quotation.data.QuotationExpiryPolicyKind;
import pico.erp.quotation.data.QuotationId;
import pico.erp.quotation.data.QuotationStatusKind;
import pico.erp.quotation.item.QuotationItem;
import pico.erp.quotation.item.addition.QuotationItemAddition;
import pico.erp.shared.data.Auditor;
import pico.erp.user.data.UserData;

@Getter
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PROTECTED)
public class QuotationAggregator extends Quotation {

  List<QuotationItem> items;

  List<QuotationItemAddition> itemAdditions;

  List<QuotationAddition> additions;

  @Builder(builderMethodName = "aggregatorBuilder")
  public QuotationAggregator(QuotationId id, QuotationCode code,
    QuotationId previousId, CommentSubjectId commentSubjectId, int revision,
    ProjectData projectData, CompanyData customerData, String name,
    QuotationExpiryPolicyKind expiryPolicy, UserData managerData,
    Auditor committer, OffsetDateTime committedDate, Auditor canceler,
    OffsetDateTime canceledDate, QuotationStatusKind status, String protectedDescription,
    String publicDescription, AttachmentId attachmentId, OffsetDateTime expirationDate,
    boolean committable, boolean preparable, BigDecimal totalAdditionAmount,
    BigDecimal totalAmount, BigDecimal totalItemAmount,
    BigDecimal totalItemDiscountedAmount, BigDecimal totalItemDiscountedRate,
    BigDecimal totalItemOriginalAmount,
    List<QuotationItem> items,
    List<QuotationItemAddition> itemAdditions,
    List<QuotationAddition> additions) {
    super(id, code, previousId, commentSubjectId, revision, projectData, customerData, name,
      expiryPolicy, managerData, committer, committedDate, canceler, canceledDate, status,
      protectedDescription, publicDescription, attachmentId, expirationDate, committable,
      preparable, totalAdditionAmount, totalAmount, totalItemAmount, totalItemDiscountedAmount,
      totalItemDiscountedRate, totalItemOriginalAmount);
    this.items = items;
    this.itemAdditions = itemAdditions;
    this.additions = additions;
  }

  public VerifyResponse apply(VerifyRequest request) {
    //val items = request.getItems();
    //val additions = request.getAdditions();
    totalItemOriginalAmount = items.stream()
      .map(item -> item.getOriginalAmount())
      .reduce(BigDecimal.ZERO, (accumulator, amount) -> accumulator.add(
        Optional.ofNullable(amount).orElse(BigDecimal.ZERO)
      ))
      .setScale(2, BigDecimal.ROUND_HALF_UP);
    totalItemDiscountedAmount = items.stream()
      .map(item -> item.getDiscountedAmount())
      .reduce(BigDecimal.ZERO, (accumulator, amount) -> accumulator.add(
        Optional.ofNullable(amount).orElse(BigDecimal.ZERO)
      ))
      .setScale(2, BigDecimal.ROUND_HALF_UP);
    totalItemAmount = items.stream()
      .map(item -> item.getFinalizedAmount())
      .reduce(BigDecimal.ZERO, (accumulator, amount) -> accumulator.add(
        Optional.ofNullable(amount).orElse(BigDecimal.ZERO)
      ))
      .setScale(2, BigDecimal.ROUND_HALF_UP);

    if (totalItemOriginalAmount.setScale(0, BigDecimal.ROUND_HALF_UP).equals(BigDecimal.ZERO)) {
      totalItemDiscountedRate = BigDecimal.ZERO;
    } else {
      totalItemDiscountedRate = totalItemOriginalAmount.subtract(totalItemDiscountedAmount)
        .divide(totalItemOriginalAmount, 4, BigDecimal.ROUND_HALF_UP);
    }
    totalAdditionAmount = additions.stream()
      .map(additional -> Optional.ofNullable(additional.getAmount()).orElse(BigDecimal.ZERO))
      .reduce(BigDecimal.ZERO, (accumulator, amount) -> accumulator.add(amount));
    totalAmount = totalItemAmount.add(totalAdditionAmount);
    val itemCommittable = items.stream().allMatch(i -> i.isCommittable());
    preparable = status.isPreparable() && itemCommittable;
    committable = status.isCommittable() && itemCommittable;
    return new VerifyResponse(Collections.emptyList());
  }
}
