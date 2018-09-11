package pico.erp.quotation.impl.print;

import java.math.BigDecimal;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pico.erp.shared.data.UnitKind;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QuotationPrintItemData {

  List<QuotationPrintBomData> boms;

  String name;

  String description;

  UnitKind unit;

  BigDecimal finalizedAmount;

  BigDecimal quantity;

  String remark;

  BigDecimal discountedUnitPrice;

  BigDecimal finalizedUnitPrice;

  BigDecimal originalUnitPrice;

  BigDecimal directLaborUnitPrice;

  BigDecimal indirectLaborUnitPrice;

  BigDecimal indirectMaterialUnitPrice;

  BigDecimal directMaterialUnitPrice;

  BigDecimal indirectExpensesUnitPrice;

  BigDecimal additionalRate;

  BigDecimal discountRate;


}


