package pico.erp.quotation;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import lombok.Data;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Profile;
import pico.erp.quotation.addition.QuotationAdditionRequests;
import pico.erp.quotation.addition.QuotationAdditionService;
import pico.erp.quotation.item.QuotationItemRequests;
import pico.erp.quotation.item.QuotationItemService;
import pico.erp.quotation.item.addition.QuotationItemAdditionRequests;
import pico.erp.quotation.item.addition.QuotationItemAdditionService;
import pico.erp.shared.ApplicationInitializer;

//@Transactional
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@Configuration
@Profile({"test-data"})
public class TestDataInitializer implements ApplicationInitializer {

  @Lazy
  @Autowired
  private QuotationService quotationService;

  @Lazy
  @Autowired
  private QuotationItemService quotationItemService;

  @Lazy
  @Autowired
  private QuotationItemAdditionService quotationItemAdditionService;

  @Lazy
  @Autowired
  private QuotationAdditionService quotationAdditionService;

  @Autowired
  private DataProperties dataProperties;

  @Override
  @SneakyThrows
  public void initialize() {
    dataProperties.quotationDrafts.forEach(quotationService::draft);
    dataProperties.quotationItems.stream().forEach(quotationItemService::create);
    dataProperties.quotationItemAdditions.stream().forEach(quotationItemAdditionService::create);
    dataProperties.quotationAdditions.stream().forEach(quotationAdditionService::create);
    TimeUnit.MILLISECONDS.sleep(500L);
    dataProperties.preparedQuotations.forEach(quotationService::prepare);
    TimeUnit.MILLISECONDS.sleep(500L);
    dataProperties.committedQuotations.forEach(quotationService::commit);
  }

  @Data
  @Configuration
  @ConfigurationProperties("data")
  public static class DataProperties {

    List<QuotationRequests.DraftRequest> quotationDrafts = new LinkedList<>();

    List<QuotationItemRequests.CreateRequest> quotationItems = new LinkedList<>();

    List<QuotationItemAdditionRequests.CreateRequest> quotationItemAdditions = new LinkedList<>();

    List<QuotationAdditionRequests.CreateRequest> quotationAdditions = new LinkedList<>();

    List<QuotationRequests.PrepareRequest> preparedQuotations = new LinkedList<>();

    List<QuotationRequests.CommitRequest> committedQuotations = new LinkedList<>();

  }

}
