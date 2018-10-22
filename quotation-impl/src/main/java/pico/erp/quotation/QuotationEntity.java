package pico.erp.quotation;


import java.io.Serializable;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Index;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import pico.erp.attachment.AttachmentId;
import pico.erp.comment.subject.CommentSubjectId;
import pico.erp.company.CompanyId;
import pico.erp.project.ProjectId;
import pico.erp.shared.TypeDefinitions;
import pico.erp.shared.data.Auditor;
import pico.erp.user.UserId;

@Entity(name = "Quotation")
@Table(name = "QOT_QUOTATION", indexes = {
  @Index(name = "QOT_QUOTATION_STATUS_IDX", columnList = "STATUS")
})
@Data
@EqualsAndHashCode(of = "id")
@FieldDefaults(level = AccessLevel.PRIVATE)
@EntityListeners(AuditingEntityListener.class)
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QuotationEntity implements Serializable {

  private static final long serialVersionUID = 1L;

  @EmbeddedId
  @AttributeOverrides({
    @AttributeOverride(name = "value", column = @Column(name = "ID", length = TypeDefinitions.UUID_BINARY_LENGTH))
  })
  QuotationId id;

  @AttributeOverrides({
    @AttributeOverride(name = "value", column = @Column(name = "CODE", length = TypeDefinitions.CODE_LENGTH))
  })
  QuotationCode code;

  @AttributeOverrides({
    @AttributeOverride(name = "value", column = @Column(name = "PREVIOUS_ID", length = TypeDefinitions.UUID_BINARY_LENGTH))
  })
  QuotationId previousId;

  @AttributeOverrides({
    @AttributeOverride(name = "value", column = @Column(name = "COMMENT_SUBJECT_ID", length = TypeDefinitions.ID_LENGTH))
  })
  CommentSubjectId commentSubjectId;

  @Column
  int revision;

  @AttributeOverrides({
    @AttributeOverride(name = "value", column = @Column(name = "PROJECT_ID", length = TypeDefinitions.UUID_BINARY_LENGTH))
  })
  ProjectId projectId;

  @Column(length = TypeDefinitions.NAME_LENGTH)
  String projectName;

  @AttributeOverrides({
    @AttributeOverride(name = "value", column = @Column(name = "CUSTOMER_ID", length = TypeDefinitions.ID_LENGTH))
  })
  CompanyId customerId;

  @Column(length = TypeDefinitions.NAME_LENGTH)
  String customerName;

  @Column(length = TypeDefinitions.NAME_LENGTH)
  String name;

  @Column(length = TypeDefinitions.ENUM_LENGTH)
  @Enumerated(EnumType.STRING)
  QuotationExpiryPolicyKind expiryPolicy;

  @AttributeOverrides({
    @AttributeOverride(name = "value", column = @Column(name = "MANAGER_ID", length = TypeDefinitions.ID_LENGTH))
  })
  UserId managerId;

  @Column(length = TypeDefinitions.NAME_LENGTH)
  String managerName;

  @Column
  OffsetDateTime committedDate;

  @Column(length = 20)
  @Enumerated(EnumType.STRING)
  QuotationStatusKind status;

  @Column(scale = 2)
  BigDecimal totalAmount;

  @Column(scale = 2)
  BigDecimal totalItemAmount;

  @Column(scale = 2)
  BigDecimal totalAdditionAmount;

  @Column(precision = 7, scale = 5)
  BigDecimal totalItemDiscountedRate;

  @Column(scale = 2)
  BigDecimal totalItemDiscountedAmount;

  @Column(scale = 2)
  BigDecimal totalItemOriginalAmount;

  @Column(length = TypeDefinitions.DESCRIPTION_LENGTH)
  String protectedDescription;

  @Column(length = TypeDefinitions.DESCRIPTION_LENGTH)
  String publicDescription;

  @Column
  OffsetDateTime expirationDate;

  @Embedded
  @AttributeOverrides({
    @AttributeOverride(name = "id", column = @Column(name = "CREATED_BY_ID", updatable = false, length = TypeDefinitions.ID_LENGTH)),
    @AttributeOverride(name = "name", column = @Column(name = "CREATED_BY_NAME", updatable = false, length = TypeDefinitions.NAME_LENGTH))
  })
  @CreatedBy
  Auditor createdBy;

  @CreatedDate
  @Column(updatable = false)
  OffsetDateTime createdDate;

  @Embedded
  @AttributeOverrides({
    @AttributeOverride(name = "id", column = @Column(name = "LAST_MODIFIED_BY_ID", length = TypeDefinitions.ID_LENGTH)),
    @AttributeOverride(name = "name", column = @Column(name = "LAST_MODIFIED_BY_NAME", length = TypeDefinitions.NAME_LENGTH))
  })
  @LastModifiedBy
  Auditor lastModifiedBy;

  @LastModifiedDate
  OffsetDateTime lastModifiedDate;

  /*@Builder.Default
  @OneToMany(mappedBy = "quotation")
  //@JoinColumn(name = "QUOTATION_ID")
  @OrderBy("createdDate DESC")
  List<QuotationItemAdditionEntity> itemAdditions = new LinkedList<>();

  @Builder.Default
  @OneToMany(mappedBy = "quotation")
  //@JoinColumn(name = "QUOTATION_ID")
  @OrderBy("createdDate DESC")
  List<QuotationAdditionEntity> additions = new LinkedList<>();

  @Builder.Default
  @OneToMany(mappedBy = "quotation")
  //@JoinColumn(name = "QUOTATION_ID")
  @OrderBy("createdDate DESC")
  List<QuotationItemEntity> items = new LinkedList<>();*/

  @Embedded
  @AttributeOverrides({
    @AttributeOverride(name = "id", column = @Column(name = "COMMITTER_ID", length = TypeDefinitions.ID_LENGTH)),
    @AttributeOverride(name = "name", column = @Column(name = "COMMITTER_NAME", length = TypeDefinitions.NAME_LENGTH))
  })
  Auditor committer;

  @Embedded
  @AttributeOverrides({
    @AttributeOverride(name = "id", column = @Column(name = "CANCELER_ID", length = TypeDefinitions.ID_LENGTH)),
    @AttributeOverride(name = "name", column = @Column(name = "CANCELER_NAME", length = TypeDefinitions.NAME_LENGTH))
  })
  Auditor canceler;

  @AttributeOverrides({
    @AttributeOverride(name = "value", column = @Column(name = "ATTACHMENT_ID", length = TypeDefinitions.ID_LENGTH))
  })
  AttachmentId attachmentId;

  boolean committable;

  boolean preparable;

}
