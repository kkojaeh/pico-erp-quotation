package pico.erp.quotation;

import javax.persistence.Id;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import pico.erp.shared.data.Role;

@RequiredArgsConstructor
public enum QuotationRoles implements Role {

  QUOTATION_MANAGER,
  QUOTATION_ACCESSOR;

  @Id
  @Getter
  private final String id = name();

}
