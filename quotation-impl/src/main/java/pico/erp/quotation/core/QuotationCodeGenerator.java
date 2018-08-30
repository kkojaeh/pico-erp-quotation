package pico.erp.quotation.core;

import pico.erp.quotation.data.QuotationCode;
import pico.erp.quotation.domain.Quotation;

public interface QuotationCodeGenerator {

  QuotationCode generate(Quotation quotation);
}
