package pico.erp.quotation;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class QuotationCodeGeneratorImpl implements QuotationCodeGenerator {

  @Autowired
  private QuotationRepository quotationRepository;

  @Override
  public QuotationCode generate(Quotation quotation) {
    val now = LocalDateTime.now();
    LocalDateTime begin = now.with(TemporalAdjusters.firstDayOfMonth())
      .with(LocalTime.MIN);
    LocalDateTime end = now.with(TemporalAdjusters.lastDayOfMonth())
      .with(LocalTime.MAX);
    long count = quotationRepository.countCreatedBetween(begin, end);
    String code = String
      .format("Q%03d%02d-%04d", now.getYear() % 100, now.getMonthValue(),
        count + 1)
      .toUpperCase();
    return QuotationCode.from(code);
  }

}
