package pico.erp.quotation.core;

import pico.erp.quotation.data.QuotationPrintData;
import pico.erp.quotation.data.QuotationPrintSheetOptions;
import pico.erp.shared.data.ContentInputStream;

public interface QuotationPrinter {

  ContentInputStream printSheet(QuotationPrintData data, QuotationPrintSheetOptions options);

}
