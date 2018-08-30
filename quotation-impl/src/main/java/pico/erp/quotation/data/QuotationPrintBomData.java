package pico.erp.quotation.data;

import lombok.Builder;
import lombok.Getter;
import lombok.experimental.Delegate;
import pico.erp.bom.data.BomHierarchyData;
import pico.erp.item.data.ItemData;
import pico.erp.item.data.ItemSpecData;
import pico.erp.process.data.ProcessData;


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
