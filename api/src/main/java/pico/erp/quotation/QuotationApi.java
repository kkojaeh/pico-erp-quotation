package pico.erp.quotation;

import javax.persistence.Id;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import pico.erp.shared.ApplicationId;
import pico.erp.shared.data.Role;

public final class QuotationApi {

  public static final ApplicationId ID = ApplicationId.from("quotation");

  @RequiredArgsConstructor
  public enum Roles implements Role {

    QUOTATION_MANAGER,
    QUOTATION_ACCESSOR;

    @Id
    @Getter
    private final String id = name();

  }
}
