package pico.erp.quotation.item.data;

import java.math.BigDecimal;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import pico.erp.item.data.ItemId;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class QuotationItemData {

  QuotationItemId id;

  ItemId itemId;

  /*  String name;*/

  String description;

  String remark;

  /*  UnitKind unit;*/

  BigDecimal quantity;

  BigDecimal originalAmount;

  BigDecimal discountedAmount;

  BigDecimal finalizedAmount;

  BigDecimal originalUnitPrice;

  BigDecimal discountedUnitPrice;

  BigDecimal finalizedUnitPrice;

  /**
   * 누적된 직접 노무비
   */
  BigDecimal directLaborUnitPrice;

  /**
   * 누적된 간접 노무비
   */
  BigDecimal indirectLaborUnitPrice;

  /**
   * 누적된 간접 재료비
   */
  BigDecimal indirectMaterialUnitPrice;

  /**
   * 직접 재료 원가
   */
  BigDecimal directMaterialUnitPrice;

  /**
   * 누적된 간접 경비
   */
  BigDecimal indirectExpensesUnitPrice;

  BigDecimal discountRate;

}
