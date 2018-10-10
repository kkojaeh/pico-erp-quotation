package pico.erp.quotation;

import java.util.HashMap;
import java.util.Properties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.util.UriTemplate;
import pico.erp.attachment.category.AttachmentCategory;
import pico.erp.attachment.category.AttachmentCategory.AttachmentCategoryImpl;
import pico.erp.attachment.category.AttachmentCategoryId;
import pico.erp.audit.AuditConfiguration;
import pico.erp.comment.subject.type.CommentSubjectType;
import pico.erp.comment.subject.type.CommentSubjectType.CommentSubjectTypeImpl;
import pico.erp.comment.subject.type.CommentSubjectTypeId;
import pico.erp.shared.ApplicationStarter;
import pico.erp.shared.Public;
import pico.erp.shared.SpringBootConfigs;
import pico.erp.shared.data.Role;
import pico.erp.shared.impl.ApplicationImpl;

@Slf4j
@SpringBootConfigs
public class QuotationApplication implements ApplicationStarter {

  public static final String CONFIG_NAME = "quotation/application";

  public static final String CONFIG_NAME_PROPERTY = "spring.config.name=quotation/application";

  public static final Properties DEFAULT_PROPERTIES = new Properties();

  static {
    DEFAULT_PROPERTIES.put("spring.config.name", CONFIG_NAME);
  }

  public static SpringApplication application() {
    return new SpringApplicationBuilder(QuotationApplication.class)
      .properties(DEFAULT_PROPERTIES)
      .web(false)
      .build();
  }

  public static void main(String[] args) {
    application().run(args);
  }

  @Override
  public int getOrder() {
    return 7;
  }

  @Public
  @Bean
  public AttachmentCategory attachmentCategory() {
    return new AttachmentCategoryImpl(AttachmentCategoryId.from("quotation"), "견적");
  }

  @Bean
  @Public
  public CommentSubjectType commentSubjectType(
    final @Value("${quotation.comment.uri}") String template) {
    return new CommentSubjectTypeImpl(
      CommentSubjectTypeId.from("quotation"),
      info -> new UriTemplate(template).expand(new HashMap<String, String>() {
        {
          put("subjectId", info.getSubjectId().getValue());
          put("commentId", info.getId().getValue());
        }
      }));
  }

  @Override
  public boolean isWeb() {
    return false;
  }

  @Bean
  @Public
  public Role quotationAccessorRole() {
    return ROLE.QUOTATION_ACCESSOR;
  }

  @Bean
  @Public
  public Role quotationManagerRole() {
    return ROLE.QUOTATION_MANAGER;
  }

  @Bean
  @Public
  public AuditConfiguration auditConfiguration() {
    return AuditConfiguration.builder()
      .packageToScan("pico.erp.quotation")
      .entity(ROLE.class)
      .build();
  }

  @Override
  public pico.erp.shared.Application start(String... args) {
    return new ApplicationImpl(application().run(args));
  }

}
