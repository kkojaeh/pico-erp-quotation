package pico.erp.quotation;

import java.util.Optional;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
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
import pico.erp.quotation.addition.QuotationAdditionRepository;
import pico.erp.quotation.item.QuotationItemRepository;
import pico.erp.quotation.item.addition.QuotationItemAdditionRepository;
import pico.erp.shared.data.Auditor;
import pico.erp.user.UserData;
import pico.erp.user.UserId;
import pico.erp.user.UserService;

@Mapper
public abstract class QuotationMapper {

  @Lazy
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

  @Lazy
  @Autowired
  private QuotationAdditionRepository quotationAdditionRepository;

  @Lazy
  @Autowired
  private QuotationItemRepository quotationItemRepository;

  @Lazy
  @Autowired
  private QuotationItemAdditionRepository quotationItemAdditionRepository;


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

  public Quotation jpa(QuotationEntity entity) {
    return Quotation.builder()
      .code(entity.getCode())
      .id(entity.getId())
      .previousId(entity.getPreviousId())
      .commentSubjectId(entity.getCommentSubjectId())
      .revision(entity.getRevision())
      .project(map(entity.getProjectId()))
      .customer(map(entity.getCustomerId()))
      .name(entity.getName())
      .expiryPolicy(entity.getExpiryPolicy())
      .manager(map(entity.getManagerId()))
      .committedDate(entity.getCommittedDate())
      .status(entity.getStatus())
      .publicDescription(entity.getPublicDescription())
      .protectedDescription(entity.getProtectedDescription())
      .attachmentId(entity.getAttachmentId())
      .expirationDate(entity.getExpirationDate())
      .totalAdditionAmount(entity.getTotalAdditionAmount())
      .totalAmount(entity.getTotalAmount())
      .totalItemAmount(entity.getTotalItemAmount())
      .totalItemDiscountedAmount(entity.getTotalItemDiscountedAmount())
      .totalItemDiscountedRate(entity.getTotalItemDiscountedRate())
      .totalItemOriginalAmount(entity.getTotalItemOriginalAmount())
      .committable(entity.isCommittable())
      .preparable(entity.isPreparable())
      .build();
  }

  public abstract QuotationMessages.DeleteRequest map(QuotationRequests.DeleteRequest request);

  @Mappings({
    @Mapping(target = "quotationCodeGenerator", expression = "java(quotationCodeGenerator)")
  })
  public abstract QuotationMessages.NextDraftRequest map(
    QuotationRequests.NextDraftRequest request);

  public abstract QuotationMessages.PrintSheetRequest map(
    QuotationRequests.PrintSheetRequest request);

  @Mappings({
    @Mapping(target = "projectId", source = "project.id"),
    @Mapping(target = "projectName", source = "project.name"),
    @Mapping(target = "customerId", source = "customer.id"),
    @Mapping(target = "customerName", source = "customer.name"),
    @Mapping(target = "managerId", source = "manager.id"),
    @Mapping(target = "managerName", source = "manager.name"),
    @Mapping(target = "createdBy", ignore = true),
    @Mapping(target = "createdDate", ignore = true),
    @Mapping(target = "lastModifiedBy", ignore = true),
    @Mapping(target = "lastModifiedDate", ignore = true)
  })
  public abstract QuotationEntity jpa(Quotation quotation);

  public QuotationAggregator jpaAggregator(QuotationEntity entity) {
    return QuotationAggregator.aggregatorBuilder()
      .code(entity.getCode())
      .id(entity.getId())
      .previousId(entity.getPreviousId())
      .commentSubjectId(entity.getCommentSubjectId())
      .revision(entity.getRevision())
      .project(map(entity.getProjectId()))
      .customer(map(entity.getCustomerId()))
      .name(entity.getName())
      .expiryPolicy(entity.getExpiryPolicy())
      .manager(map(entity.getManagerId()))
      .committedDate(entity.getCommittedDate())
      .status(entity.getStatus())
      .publicDescription(entity.getPublicDescription())
      .protectedDescription(entity.getProtectedDescription())
      .attachmentId(entity.getAttachmentId())
      .expirationDate(entity.getExpirationDate())
      .totalAdditionAmount(entity.getTotalAdditionAmount())
      .totalAmount(entity.getTotalAmount())
      .totalItemAmount(entity.getTotalItemAmount())
      .totalItemDiscountedAmount(entity.getTotalItemDiscountedAmount())
      .totalItemDiscountedRate(entity.getTotalItemDiscountedRate())
      .totalItemOriginalAmount(entity.getTotalItemOriginalAmount())
      .items(
        quotationItemRepository.findAllBy(entity.getId()).collect(Collectors.toList())
      )
      .itemAdditions(
        quotationItemAdditionRepository.findAllBy(entity.getId()).collect(Collectors.toList())
      )
      .additions(
        quotationAdditionRepository.findAllBy(entity.getId()).collect(Collectors.toList())
      )
      .committable(entity.isCommittable())
      .preparable(entity.isPreparable())
      .build();
  }


  public abstract QuotationMessages.ExpireRequest map(QuotationRequests.ExpireRequest request);

  @Mappings({
    @Mapping(target = "project", source = "projectId"),
    @Mapping(target = "customer", source = "customerId"),
    @Mapping(target = "manager", source = "managerId"),
    @Mapping(target = "quotationCodeGenerator", expression = "java(quotationCodeGenerator)")
  })
  public abstract QuotationMessages.DraftRequest map(QuotationRequests.DraftRequest request);

  @Mappings({
    @Mapping(target = "project", source = "projectId"),
    @Mapping(target = "customer", source = "customerId"),
    @Mapping(target = "manager", source = "managerId")
  })
  public abstract QuotationMessages.UpdateRequest map(QuotationRequests.UpdateRequest request);

  @Mappings({
    @Mapping(target = "projectId", source = "project.id"),
    @Mapping(target = "customerId", source = "customer.id"),
    @Mapping(target = "managerId", source = "manager.id")
  })
  public abstract QuotationData map(Quotation quotation);

  public abstract void pass(QuotationEntity from, @MappingTarget QuotationEntity to);


}
