package pico.erp.quotation.code;

import pico.erp.quotation.Quotation;
import pico.erp.quotation.QuotationCode;

public interface QuotationCodeGenerator {

  QuotationCode generate(Quotation quotation);
}
