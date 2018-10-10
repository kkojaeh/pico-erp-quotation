package pico.erp.quotation.addition;


import java.math.BigDecimal;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class QuotationAdditionData {

  QuotationAdditionId id;

  String name;

  String description;

  BigDecimal quantity;

  BigDecimal unitPrice;

  String remark;

  BigDecimal amount;

}
