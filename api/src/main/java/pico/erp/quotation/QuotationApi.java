package pico.erp.quotation;

import javax.persistence.Id;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import pico.erp.shared.data.Role;

public final class QuotationApi {

  @RequiredArgsConstructor
  public enum Roles implements Role {

    QUOTATION_MANAGER,

    QUOTATION_ACCESSOR;

    @Id
    @Getter
    private final String id = name();

  }
}
