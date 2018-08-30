package pico.erp.quotation;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.persistence.Id;
import javax.persistence.Transient;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import pico.erp.shared.data.Menu;
import pico.erp.shared.data.Role;

@RequiredArgsConstructor
public enum ROLE implements Role {

  QUOTATION_MANAGER(Stream.of(MENU.QUOTATION_MANAGEMENT).collect(Collectors.toSet())),
  QUOTATION_ACCESSOR(Collections.emptySet());

  @Id
  @Getter
  private final String id = name();

  @Transient
  @Getter
  @NonNull
  private Set<Menu> menus;
}
