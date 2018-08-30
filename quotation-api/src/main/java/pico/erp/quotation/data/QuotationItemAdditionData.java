package pico.erp.quotation.data;

import java.math.BigDecimal;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class QuotationItemAdditionData {

  QuotationItemAdditionId id;

  String name;

  String description;

  String remark;

  BigDecimal additionalRate;

}
