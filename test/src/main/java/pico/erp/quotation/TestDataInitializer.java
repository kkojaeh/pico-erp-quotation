package pico.erp.quotation;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import kkojaeh.spring.boot.component.SpringBootComponentReadyEvent;
import lombok.Data;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import pico.erp.quotation.addition.QuotationAdditionRequests;
import pico.erp.quotation.addition.QuotationAdditionService;
import pico.erp.quotation.item.QuotationItemRequests;
import pico.erp.quotation.item.QuotationItemService;
import pico.erp.quotation.item.addition.QuotationItemAdditionRequests;
import pico.erp.quotation.item.addition.QuotationItemAdditionService;

//@Transactional
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@Configuration
@Profile({"test-data"})
public class TestDataInitializer implements ApplicationListener<SpringBootComponentReadyEvent> {

  @Autowired
  private QuotationService quotationService;

  @Autowired
  private QuotationItemService quotationItemService;

  @Autowired
  private QuotationItemAdditionService quotationItemAdditionService;

  @Autowired
  private QuotationAdditionService quotationAdditionService;

  @Autowired
  private DataProperties dataProperties;

  @SneakyThrows
  @Override
  public void onApplicationEvent(SpringBootComponentReadyEvent event) {
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
