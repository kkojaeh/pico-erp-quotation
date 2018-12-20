package pico.erp.quotation;

import java.time.OffsetDateTime;
import java.util.function.Function;
import pico.erp.shared.data.LocalizedNameable;

public enum QuotationExpiryPolicyKind implements LocalizedNameable {

  NONE(commitDate -> null),

  IN_MONTH(commitDate -> commitDate.plusMonths(1)),

  IN_QUARTER(commitDate -> commitDate.plusMonths(3)),

  IN_HALF(commitDate -> commitDate.plusMonths(6)),

  IN_YEAR(commitDate -> commitDate.plusYears(1));

  private final Function<OffsetDateTime, OffsetDateTime> mapper;

  QuotationExpiryPolicyKind(Function<OffsetDateTime, OffsetDateTime> mapper) {
    this.mapper = mapper;
  }

  public OffsetDateTime resolveExpirationDate(OffsetDateTime commitDate) {
    return mapper.apply(commitDate);
  }

}
