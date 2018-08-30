package pico.erp.quotation.data;

import java.math.BigDecimal;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class QuotationUnitPriceData {


  /**
   * 계산 전 단가
   */
  @NotNull
  BigDecimal original;


  /**
   * 누적된 직접 노무비
   */
  @NotNull
  BigDecimal directLabor;

  /**
   * 누적된 간접 노무비
   */
  @NotNull
  BigDecimal indirectLabor;

  /**
   * 누적된 간접 재료비
   */
  @NotNull
  BigDecimal indirectMaterial;

  /**
   * 직접 재료 원가
   */
  @NotNull
  BigDecimal directMaterial;

  /**
   * 누적된 간접 경비
   */
  @NotNull
  BigDecimal indirectExpenses;


  /**
   * 할인율 0 ~ 1
   */
  BigDecimal discountRate;

  /**
   * 최종 단가
   */
  BigDecimal finalized;


  /**
   * 할인율 적용 단가
   */
  BigDecimal discounted;

}
