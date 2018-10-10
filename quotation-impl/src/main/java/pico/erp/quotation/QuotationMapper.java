package pico.erp.quotation;

import java.util.Optional;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.AuditorAware;
import pico.erp.company.CompanyData;
import pico.erp.company.CompanyId;
import pico.erp.company.CompanyService;
import pico.erp.project.ProjectData;
import pico.erp.project.ProjectId;
import pico.erp.project.ProjectService;
import pico.erp.quotation.QuotationExceptions.NotFoundException;
import pico.erp.quotation.code.QuotationCodeGenerator;
import pico.erp.shared.data.Auditor;
import pico.erp.user.UserData;
import pico.erp.user.UserId;
import pico.erp.user.UserService;

@Mapper
public abstract class QuotationMapper {

  @Autowired
  protected QuotationCodeGenerator quotationCodeGenerator;

  @Autowired
  protected AuditorAware<Auditor> auditorAware;


  @Lazy
  @Autowired
  private CompanyService companyService;

  @Lazy
  @Autowired
  private UserService userService;

  @Lazy
  @Autowired
  private ProjectService projectService;

  @Lazy
  @Autowired
  private QuotationRepository quotationRepository;


  @Mappings({
    @Mapping(target = "canceler", expression = "java(auditorAware.getCurrentAuditor())")
  })
  public abstract QuotationMessages.CancelRequest map(QuotationRequests.CancelRequest request);


  @Mappings({
    @Mapping(target = "committer", expression = "java(auditorAware.getCurrentAuditor())")
  })
  public abstract QuotationMessages.CommitRequest map(QuotationRequests.CommitRequest request);

  public abstract QuotationMessages.PrepareRequest map(QuotationRequests.PrepareRequest request);

  protected ProjectData map(ProjectId projectId) {
    return Optional.ofNullable(projectId)
      .map(projectService::get)
      .orElse(null);
  }

  protected CompanyData map(CompanyId companyId) {
    return Optional.ofNullable(companyId)
      .map(companyService::get)
      .orElse(null);
  }

  protected UserData map(UserId userId) {
    return Optional.ofNullable(userId)
      .map(userService::get)
      .orElse(null);
  }


  public Quotation map(QuotationId quotationId) {
    return quotationRepository.findBy(quotationId)
      .orElseThrow(NotFoundException::new);
  }


  @Mappings({
    @Mapping(target = "projectData", source = "projectId"),
    @Mapping(target = "customerData", source = "customerId"),
    @Mapping(target = "managerData", source = "managerId"),
    @Mapping(target = "quotationCodeGenerator", expression = "java(quotationCodeGenerator)")
  })
  public abstract QuotationMessages.DraftRequest map(QuotationRequests.DraftRequest request);

  public abstract QuotationMessages.DeleteRequest map(QuotationRequests.DeleteRequest request);

  @Mappings({
    @Mapping(target = "quotationCodeGenerator", expression = "java(quotationCodeGenerator)")
  })
  public abstract QuotationMessages.NextDraftRequest map(
    QuotationRequests.NextDraftRequest request);

  public abstract QuotationMessages.PrintSheetRequest map(
    QuotationRequests.PrintSheetRequest request);


  @Mappings({
    @Mapping(target = "projectData", source = "projectId"),
    @Mapping(target = "customerData", source = "customerId"),
    @Mapping(target = "managerData", source = "managerId")
  })
  public abstract QuotationMessages.UpdateRequest map(QuotationRequests.UpdateRequest request);

  @Mappings({
    @Mapping(target = "projectId", source = "projectData.id"),
    @Mapping(target = "customerId", source = "customerData.id"),
    @Mapping(target = "managerId", source = "managerData.id"),
    @Mapping(target = "modifiable", expression = "java(quotation.canModify())")
  })
  public abstract QuotationData map(Quotation quotation);


  public abstract QuotationMessages.ExpireRequest map(QuotationRequests.ExpireRequest request);


}
