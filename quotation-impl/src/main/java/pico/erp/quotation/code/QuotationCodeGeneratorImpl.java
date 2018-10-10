package pico.erp.quotation.code;

import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pico.erp.quotation.Quotation;
import pico.erp.quotation.QuotationCode;
import pico.erp.quotation.QuotationRepository;

@Component
public class QuotationCodeGeneratorImpl implements QuotationCodeGenerator {

  @Autowired
  private QuotationRepository quotationRepository;

  @Override
  public QuotationCode generate(Quotation quotation) {
    LocalDate now = LocalDate.now();
    long count = quotationRepository.countByCreatedThisMonth();
    String value = String
      .format("%d%s-%04d", now.getYear() % 100, Integer.toString(now.getMonthValue(), 16), count);
    return QuotationCode.from(value);
  }

}
