package pico.erp.quotation.print;

import pico.erp.quotation.QuotationAggregator;
import pico.erp.quotation.QuotationPrintSheetOptions;
import pico.erp.shared.data.ContentInputStream;

public interface QuotationPrinter {

  ContentInputStream printSheet(QuotationAggregator quotation, QuotationPrintSheetOptions options);

}
