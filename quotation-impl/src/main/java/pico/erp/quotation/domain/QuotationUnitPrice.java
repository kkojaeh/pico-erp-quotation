package pico.erp.quotation.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import java.math.BigDecimal;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import pico.erp.quotation.domain.QuotationUnitPrice.QuotationUnitPriceBuilder;

@Data
@AllArgsConstructor
@Builder(toBuilder = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonDeserialize(builder = QuotationUnitPriceBuilder.class)
public class QuotationUnitPrice {


  /**
   * 계산 전 단가
   */
  @Builder.Default
  BigDecimal original = BigDecimal.ZERO;


  /**
   * 누적된 직접 노무비
   */
  @Builder.Default
  BigDecimal directLabor = BigDecimal.ZERO;

  /**
   * 누적된 간접 노무비
   */
  @Builder.Default
  BigDecimal indirectLabor = BigDecimal.ZERO;

  /**
   * 누적된 간접 재료비
   */
  @Builder.Default
  BigDecimal indirectMaterial = BigDecimal.ZERO;

  /**
   * 직접 재료 원가
   */
  @Builder.Default
  BigDecimal directMaterial = BigDecimal.ZERO;

  /**
   * 누적된 간접 경비
   */
  @Builder.Default
  BigDecimal indirectExpenses = BigDecimal.ZERO;

  /**
   * 추가 단가율
   */
  @Builder.Default
  BigDecimal additionalRate = BigDecimal.ZERO;

  /**
   * 할인율 0 ~ 1
   */
  @Builder.Default
  BigDecimal discountRate = BigDecimal.ZERO;


  /**
   * 할인율이 적용된 단가 최초 단가 - (최초 단가 * 할인율)
   */
  public BigDecimal getDiscountedUnitPrice() {
    BigDecimal price = getOriginal();
    return price.subtract(price.multiply(getDiscountRate()));
  }

  /**
   * 할인후 진접 노무비 단가
   */
  public BigDecimal getDiscountedDirectLabor() {
    BigDecimal price = getDirectLabor();
    return price.add(price.multiply(getAdditionalRate()));
  }

  /**
   * 할인후 직접 재료비 단가
   */
  public BigDecimal getDiscountedDirectMaterial() {
    BigDecimal price = getDirectMaterial();
    return price.subtract(price.multiply(getDiscountRate()));
  }

  /**
   * 할인후 간접 경비 단가
   */
  public BigDecimal getDiscountedIndirectExpenses() {
    BigDecimal price = getIndirectExpenses();
    return price.subtract(price.multiply(getDiscountRate()));
  }

  /**
   * 할인후 간접 노무비 단가
   */
  public BigDecimal getDiscountedIndirectLabor() {
    BigDecimal price = getIndirectLabor();
    return price.subtract(price.multiply(getDiscountRate()));
  }

  /**
   * 할인후 간접 재료비 단가
   */
  public BigDecimal getDiscountedIndirectMaterial() {
    BigDecimal price = getIndirectMaterial();
    return price.subtract(price.multiply(getDiscountRate()));
  }

  /**
   * 최종 단가 할인율이 적용된 단가에 추가 단가 및 추가 단가율을 적용 단가 (최초 단가 - (최초 단가 * 할인율)) + ((최초 단가 - (최초 단가 * 할인율)) *
   * 부가단가율) + 부가 단가
   */
  public BigDecimal getFinalizedUnitPrice() {
    BigDecimal discounted = this.getDiscountedUnitPrice();
    return discounted.add(discounted.multiply(getAdditionalRate()));
  }

  /**
   * 최종 직접 노무비 단가
   */
  public BigDecimal getFinalizedDirectLabor() {
    BigDecimal price = getDirectLabor();
    return price.add(price.multiply(getAdditionalRate()));
  }

  /**
   * 최종 직접 재료비 단가
   */
  public BigDecimal getFinalizedDirectMaterial() {
    BigDecimal price = getDirectMaterial();
    return price.add(price.multiply(getAdditionalRate()));
  }

  /**
   * 최종 간접 경비 단가
   */
  public BigDecimal getFinalizedIndirectExpenses() {
    BigDecimal price = getIndirectExpenses();
    return price.add(price.multiply(getAdditionalRate()));
  }

  /**
   * 최종 간접 노무비 단가
   */
  public BigDecimal getFinalizedIndirectLabor() {
    BigDecimal price = getIndirectLabor();
    return price.add(price.multiply(getAdditionalRate()));
  }

  /**
   * 최종 간접 재료비 단가
   */
  public BigDecimal getFinalizedIndirectMaterial() {
    BigDecimal price = getIndirectMaterial();
    return price.add(price.multiply(getAdditionalRate()));
  }

  @JsonPOJOBuilder(withPrefix = "")
  public static class QuotationUnitPriceBuilder {

  }

}
