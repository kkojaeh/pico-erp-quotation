package pico.erp.quotation;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import pico.erp.shared.data.Menu;
import pico.erp.shared.data.MenuCategory;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum MENU implements Menu {

  QUOTATION_MANAGEMENT("/quotation", "view_list", MenuCategory.CUSTOMER);

  String url;

  String icon;

  MenuCategory category;

  public String getId() {
    return name();
  }

}
