package pico.erp.quotation.print;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QuotationPrintItemAdditionRateData {

  String name;

  String description;

  String rate;

  String remark;

}
