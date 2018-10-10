package pico.erp.quotation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class QuotationStatusCountPerMonthAggregateView {

  QuotationStatusKind status;

  Long count;

  Integer yearMonth;

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class Filter {

    int year;

  }

}
