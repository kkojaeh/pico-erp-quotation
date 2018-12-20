package pico.erp.quotation.item;

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
import javax.persistence.Index;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import pico.erp.item.ItemId;
import pico.erp.quotation.QuotationId;
import pico.erp.shared.TypeDefinitions;
import pico.erp.shared.data.Auditor;

@Data
@Entity(name = "QuotationItem")
@Table(name = "QOT_QUOTATION_ITEM", indexes = {
  @Index(columnList = "QUOTATION_ID")
})
@FieldDefaults(level = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class QuotationItemEntity implements Serializable {

  private static final long serialVersionUID = 1L;

  @EmbeddedId
  @AttributeOverrides({
    @AttributeOverride(name = "value", column = @Column(name = "ID", length = TypeDefinitions.UUID_BINARY_LENGTH))
  })
  QuotationItemId id;

  @AttributeOverrides({
    @AttributeOverride(name = "value", column = @Column(name = "QUOTATION_ID", length = TypeDefinitions.UUID_BINARY_LENGTH))
  })
  QuotationId quotationId;

  @AttributeOverrides({
    @AttributeOverride(name = "value", column = @Column(name = "ITEM_ID", length = TypeDefinitions.UUID_BINARY_LENGTH))
  })
  ItemId itemId;

  @Column(length = TypeDefinitions.DESCRIPTION_LENGTH)
  String description;

  @Column(length = TypeDefinitions.REMARK_LENGTH)
  String remark;

  @Column(scale = 2)
  BigDecimal quantity;

  @Column(precision = 19, scale = 2)
  BigDecimal discountedUnitPrice;

  @Column(precision = 19, scale = 2)
  BigDecimal finalizedUnitPrice;

  @Column(precision = 19, scale = 2)
  BigDecimal originalUnitPrice;

  @Column(precision = 19, scale = 2)
  BigDecimal directLaborUnitPrice;

  @Column(precision = 19, scale = 2)
  BigDecimal indirectLaborUnitPrice;

  @Column(precision = 19, scale = 2)
  BigDecimal indirectMaterialUnitPrice;

  @Column(precision = 19, scale = 2)
  BigDecimal directMaterialUnitPrice;

  @Column(precision = 19, scale = 2)
  BigDecimal indirectExpensesUnitPrice;

  @Column(precision = 7, scale = 5)
  BigDecimal additionalRate;

  @Column(precision = 7, scale = 5)
  BigDecimal discountRate;

  /**
   * 최종 금액 최종 단가와 수량의 곱
   */
  @Column(scale = 2)
  BigDecimal finalizedAmount;

  @Column(scale = 2)
  BigDecimal discountedAmount;

  @Column(scale = 2)
  BigDecimal originalAmount;

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

  boolean unitPriceManuallyFixed;


}
