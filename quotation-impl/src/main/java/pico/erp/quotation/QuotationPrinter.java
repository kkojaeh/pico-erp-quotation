package pico.erp.quotation;

import pico.erp.quotation.data.QuotationPrintSheetOptions;
import pico.erp.shared.data.ContentInputStream;

public interface QuotationPrinter {

  ContentInputStream printSheet(QuotationAggregator quotation, QuotationPrintSheetOptions options);

}
