package pico.erp.quotation.impl.jpa;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class QuotationUnitPriceEmbeddable {

  /////

  /**
   * 계산 전 단가
   */
  @Column(scale = 2)
  BigDecimal original = BigDecimal.ZERO;


  /**
   * 누적된 직접 노무비
   */
  @Column(scale = 2)
  BigDecimal directLabor = BigDecimal.ZERO;

  /**
   * 누적된 간접 노무비
   */
  @Column(scale = 2)
  BigDecimal indirectLabor = BigDecimal.ZERO;

  /**
   * 누적된 간접 재료비
   */
  @Column(scale = 2)
  BigDecimal indirectMaterial = BigDecimal.ZERO;

  /**
   * 직접 재료 원가
   */
  @Column(scale = 2)
  BigDecimal directMaterial = BigDecimal.ZERO;

  /**
   * 누적된 간접 경비
   */
  @Column(scale = 2)
  BigDecimal indirectExpenses = BigDecimal.ZERO;

  /**
   * 추가 단가율
   */
  @Column(precision = 7, scale = 5)
  BigDecimal additionalRate = BigDecimal.ZERO;

  /**
   * 할인율 0 ~ 1
   */
  @Column(precision = 7, scale = 5)
  BigDecimal discountRate = BigDecimal.ZERO;


}
