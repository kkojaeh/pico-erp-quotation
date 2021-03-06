package pico.erp.quotation.addition;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Index;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import pico.erp.quotation.QuotationId;
import pico.erp.shared.TypeDefinitions;
import pico.erp.shared.data.Auditor;

@Data
@Entity(name = "QuotationAddition")
@Table(name = "QOT_QUOTATION_ADDITION", indexes = {
  @Index(columnList = "QUOTATION_ID")
})
@FieldDefaults(level = AccessLevel.PRIVATE)
@EntityListeners(AuditingEntityListener.class)
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class QuotationAdditionEntity {

  @EmbeddedId
  @AttributeOverrides({
    @AttributeOverride(name = "value", column = @Column(name = "ID", length = TypeDefinitions.UUID_BINARY_LENGTH))
  })
  QuotationAdditionId id;

  @AttributeOverrides({
    @AttributeOverride(name = "value", column = @Column(name = "QUOTATION_ID", length = TypeDefinitions.UUID_BINARY_LENGTH))
  })
  QuotationId quotationId;

  @Column(length = TypeDefinitions.NAME_LENGTH)
  String name;

  @Column(length = TypeDefinitions.DESCRIPTION_LENGTH)
  String description;

  @Column(length = TypeDefinitions.REMARK_LENGTH)
  String remark;

  @Column(precision = 19, scale = 5)
  BigDecimal quantity;

  @Column(scale = 2)
  BigDecimal unitPrice;

  @Embedded
  @AttributeOverrides({
    @AttributeOverride(name = "id", column = @Column(name = "CREATED_BY_ID", updatable = false, length = TypeDefinitions.ID_LENGTH)),
    @AttributeOverride(name = "name", column = @Column(name = "CREATED_BY_NAME", updatable = false, length = TypeDefinitions.NAME_LENGTH))
  })
  @CreatedBy
  Auditor createdBy;

  @Column(updatable = false)
  OffsetDateTime createdDate;

  @PrePersist
  private void onCreate() {
    createdDate = OffsetDateTime.now();
  }

}
