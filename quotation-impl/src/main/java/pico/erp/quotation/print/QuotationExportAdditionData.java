package pico.erp.quotation.print;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QuotationExportAdditionData {

  String name;

  String description;

  BigDecimal quantity;

  BigDecimal unitPrice;

  String remark;

  BigDecimal amount;

}
