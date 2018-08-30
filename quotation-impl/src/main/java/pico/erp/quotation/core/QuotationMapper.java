package pico.erp.quotation.core;

import java.util.Optional;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.AuditorAware;
import pico.erp.bom.BomService;
import pico.erp.bom.data.BomData;
import pico.erp.company.CompanyService;
import pico.erp.company.data.CompanyData;
import pico.erp.company.data.CompanyId;
import pico.erp.item.ItemService;
import pico.erp.item.data.ItemData;
import pico.erp.item.data.ItemId;
import pico.erp.project.ProjectService;
import pico.erp.project.data.ProjectData;
import pico.erp.project.data.ProjectId;
import pico.erp.quotation.QuotationAdditionRequests;
import pico.erp.quotation.QuotationExceptions.NotFoundException;
import pico.erp.quotation.QuotationItemAdditionRequests;
import pico.erp.quotation.QuotationItemRequests;
import pico.erp.quotation.QuotationItemRequests.CreateRequest;
import pico.erp.quotation.QuotationItemRequests.FixUnitPriceRequest;
import pico.erp.quotation.QuotationRequests;
import pico.erp.quotation.data.QuotationAdditionData;
import pico.erp.quotation.data.QuotationData;
import pico.erp.quotation.data.QuotationId;
import pico.erp.quotation.data.QuotationItemAdditionData;
import pico.erp.quotation.data.QuotationItemData;
import pico.erp.quotation.data.QuotationUnitPriceData;
import pico.erp.quotation.domain.Quotation;
import pico.erp.quotation.domain.QuotationAddition;
import pico.erp.quotation.domain.QuotationAdditionMessages;
import pico.erp.quotation.domain.QuotationItem;
import pico.erp.quotation.domain.QuotationItemAddition;
import pico.erp.quotation.domain.QuotationItemAdditionMessages;
import pico.erp.quotation.domain.QuotationItemMessages;
import pico.erp.quotation.domain.QuotationMessages;
import pico.erp.quotation.domain.QuotationMessages.CancelRequest;
import pico.erp.quotation.domain.QuotationMessages.DeleteRequest;
import pico.erp.quotation.domain.QuotationMessages.DraftRequest;
import pico.erp.quotation.domain.QuotationMessages.ExpireRequest;
import pico.erp.quotation.domain.QuotationMessages.NextDraftRequest;
import pico.erp.quotation.domain.QuotationMessages.PrintSheetRequest;
import pico.erp.quotation.domain.QuotationMessages.UpdateRequest;
import pico.erp.quotation.domain.QuotationUnitPrice;
import pico.erp.shared.data.Auditor;
import pico.erp.user.UserService;
import pico.erp.user.data.UserData;
import pico.erp.user.data.UserId;

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

  protected abstract QuotationItemAdditionData map(
    QuotationItemAddition itemAdditionRate);

  public abstract QuotationItemMessages.FixUnitPriceRequest map(
    FixUnitPriceRequest request);

  protected abstract QuotationAdditionData map(QuotationAddition addition);

  protected QuotationUnitPrice map(QuotationUnitPriceData data) {
    return QuotationUnitPrice.builder()
      .original(data.getOriginal())
      .directLabor(data.getDirectLabor())
      .indirectLabor(data.getIndirectLabor())
      .indirectMaterial(data.getIndirectMaterial())
      .directMaterial(data.getDirectMaterial())
      .indirectExpenses(data.getIndirectExpenses())
      .discountRate(data.getDiscountRate())
      .build();
  }

  public abstract ExpireRequest map(QuotationRequests.ExpireRequest request);

  protected BomData mapBom(ItemId itemId) {
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
