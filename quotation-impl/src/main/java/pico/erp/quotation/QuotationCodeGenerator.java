package pico.erp.quotation;

import pico.erp.quotation.data.QuotationCode;

public interface QuotationCodeGenerator {

  QuotationCode generate(Quotation quotation);
}
