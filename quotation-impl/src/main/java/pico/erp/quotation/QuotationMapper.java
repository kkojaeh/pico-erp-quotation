package pico.erp.quotation;

import java.util.Optional;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.AuditorAware;
import pico.erp.bom.BomData;
import pico.erp.bom.BomService;
import pico.erp.company.CompanyData;
import pico.erp.company.CompanyId;
import pico.erp.company.CompanyService;
import pico.erp.item.ItemData;
import pico.erp.item.ItemId;
import pico.erp.item.ItemService;
import pico.erp.project.ProjectData;
import pico.erp.project.ProjectId;
import pico.erp.project.ProjectService;
import pico.erp.quotation.QuotationExceptions.NotFoundException;
import pico.erp.quotation.QuotationMessages.CancelRequest;
import pico.erp.quotation.QuotationMessages.DeleteRequest;
import pico.erp.quotation.QuotationMessages.DraftRequest;
import pico.erp.quotation.QuotationMessages.ExpireRequest;
import pico.erp.quotation.QuotationMessages.NextDraftRequest;
import pico.erp.quotation.QuotationMessages.PrintSheetRequest;
import pico.erp.quotation.QuotationMessages.UpdateRequest;
import pico.erp.quotation.addition.QuotationAddition;
import pico.erp.quotation.addition.QuotationAdditionMessages;
import pico.erp.quotation.addition.QuotationAdditionRequests;
import pico.erp.quotation.addition.data.QuotationAdditionData;
import pico.erp.quotation.data.QuotationData;
import pico.erp.quotation.data.QuotationId;
import pico.erp.quotation.item.QuotationItem;
import pico.erp.quotation.item.QuotationItemMessages;
import pico.erp.quotation.item.QuotationItemRequests;
import pico.erp.quotation.item.QuotationItemRequests.CreateRequest;
import pico.erp.quotation.item.QuotationItemRequests.FixUnitPriceRequest;
import pico.erp.quotation.item.addition.QuotationItemAddition;
import pico.erp.quotation.item.addition.QuotationItemAdditionMessages;
import pico.erp.quotation.item.addition.QuotationItemAdditionRequests;
import pico.erp.quotation.item.addition.data.QuotationItemAdditionData;
import pico.erp.quotation.item.data.QuotationItemData;
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
  private BomService bomService;

  @Lazy
  @Autowired
  private ItemService itemService;

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
    @Mapping(target = "quotation", source = "quotationId")
  })
  public abstract QuotationAdditionMessages.CreateRequest map(
    QuotationAdditionRequests.CreateRequest request);


  @Mappings({
    @Mapping(target = "quotation", source = "quotationId"),
    @Mapping(target = "itemData", source = "itemId"),
    @Mapping(target = "bomData", source = "itemId")
  })
  public abstract QuotationItemMessages.CreateRequest map(
    CreateRequest request);

  @Mappings({
    @Mapping(target = "quotation", source = "quotationId")
  })
  public abstract QuotationItemAdditionMessages.CreateRequest map(
    QuotationItemAdditionRequests.CreateRequest request);

  @Mappings({
    @Mapping(target = "canceler", expression = "java(auditorAware.getCurrentAuditor())")
  })
  public abstract CancelRequest map(QuotationRequests.CancelRequest request);


  public abstract QuotationAdditionMessages.UpdateRequest map(
    QuotationAdditionRequests.UpdateRequest request);

  @Mappings({
    @Mapping(target = "bomData", source = "itemId")
  })
  public abstract QuotationItemMessages.UpdateRequest map(
    QuotationItemRequests.UpdateRequest request);

  public abstract QuotationItemAdditionMessages.UpdateRequest map(
    QuotationItemAdditionRequests.UpdateRequest request);

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

  protected ItemData map(ItemId itemId) {
    return Optional.ofNullable(itemId)
      .map(itemService::get)
      .orElse(null);
  }

  protected Quotation map(QuotationId quotationId) {
    return quotationRepository.findBy(quotationId)
      .orElseThrow(NotFoundException::new);
  }

  public abstract QuotationAdditionMessages.DeleteRequest map(
    QuotationAdditionRequests.DeleteRequest request);

  @Mappings({
    @Mapping(target = "projectData", source = "projectId"),
    @Mapping(target = "customerData", source = "customerId"),
    @Mapping(target = "managerData", source = "managerId"),
    @Mapping(target = "quotationCodeGenerator", expression = "java(quotationCodeGenerator)")
  })
  public abstract DraftRequest map(QuotationRequests.DraftRequest request);

  public abstract DeleteRequest map(QuotationRequests.DeleteRequest request);

  @Mappings({
    @Mapping(target = "quotationCodeGenerator", expression = "java(quotationCodeGenerator)")
  })
  public abstract NextDraftRequest map(QuotationRequests.NextDraftRequest request);

  public abstract PrintSheetRequest map(QuotationRequests.PrintSheetRequest request);

  public abstract QuotationItemMessages.DeleteRequest map(
    QuotationItemRequests.DeleteRequest request);

  public abstract QuotationItemAdditionMessages.DeleteRequest map(
    QuotationItemAdditionRequests.DeleteRequest request);

  @Mappings({
    @Mapping(target = "itemId", source = "itemData.id")
  })
  public abstract QuotationItemData map(QuotationItem item);

  @Mappings({
    @Mapping(target = "projectData", source = "projectId"),
    @Mapping(target = "customerData", source = "customerId"),
    @Mapping(target = "managerData", source = "managerId")
  })
  public abstract UpdateRequest map(QuotationRequests.UpdateRequest request);

  @Mappings({
    @Mapping(target = "projectId", source = "projectData.id"),
    @Mapping(target = "customerId", source = "customerData.id"),
    @Mapping(target = "managerId", source = "managerData.id"),
    @Mapping(target = "modifiable", expression = "java(quotation.canModify())")
  })
  public abstract QuotationData map(Quotation quotation);

  public abstract QuotationItemAdditionData map(
    QuotationItemAddition itemAdditionRate);

  public abstract QuotationItemMessages.FixUnitPriceRequest map(
    FixUnitPriceRequest request);

  public abstract QuotationAdditionData map(QuotationAddition addition);

  public abstract ExpireRequest map(QuotationRequests.ExpireRequest request);

  public BomData mapBom(ItemId itemId) {
    if (itemId == null) {
      return null;
    }
    if (bomService.exists(itemId)) {
      return bomService.get(itemId);
    } else {
      return null;
    }
  }

}
