package pico.erp.quotation;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import pico.erp.attachment.AttachmentId;
import pico.erp.comment.subject.CommentSubjectId;
import pico.erp.company.CompanyId;
import pico.erp.project.ProjectId;
import pico.erp.user.UserId;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class QuotationData {

  QuotationId id;

  QuotationCode code;

  QuotationId previousId;

  CommentSubjectId commentSubjectId;

  int revision;

  ProjectId projectId;

  CompanyId customerId;

  String name;

  QuotationExpiryPolicyKind expiryPolicy;

  UserId managerId;

  OffsetDateTime committedDate;

  QuotationStatusKind status;

  String protectedDescription;

  String publicDescription;

  /**
   * 총 금액
   */
  BigDecimal totalAmount;

  /**
   * 총 품목금액
   */
  BigDecimal totalItemAmount;

  /**
   * 총 부가(기초비/별도청구) 금액
   */
  BigDecimal totalAdditionAmount;

  /**
   * 총 품목 할인율
   */
  BigDecimal totalItemDiscountedRate;

  /**
   * 총 품목 할인금액
   */
  BigDecimal totalItemDiscountedAmount;

  /*List<QuotationItemData> items;

  List<QuotationItemAdditionData> itemAdditions;

  List<QuotationAdditionData> additions;*/

  boolean committable;

  boolean cancelable;

  boolean sheetPrintable;

  boolean modifiable;

  boolean nextDraftable;

  AttachmentId attachmentId;

  OffsetDateTime expirationDate;

  boolean preparable;

}
