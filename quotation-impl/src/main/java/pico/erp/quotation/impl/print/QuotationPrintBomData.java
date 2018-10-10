package pico.erp.quotation.impl.print;

import lombok.Builder;
import lombok.Getter;
import lombok.experimental.Delegate;
import pico.erp.bom.BomHierarchyData;
import pico.erp.item.ItemData;
import pico.erp.item.spec.ItemSpecData;
import pico.erp.process.ProcessData;


@Builder
public class QuotationPrintBomData {

  @Delegate
  BomHierarchyData bom;

  @Getter
  ItemData item;

  @Getter
  ProcessData process;


  @Getter
  ItemSpecData itemSpec;

  @Getter
  int level;

}
