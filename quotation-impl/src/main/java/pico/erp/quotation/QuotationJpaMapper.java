package pico.erp.quotation;

import java.util.Optional;
import java.util.stream.Collectors;
import lombok.val;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
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
import pico.erp.quotation.addition.QuotationAddition;
import pico.erp.quotation.addition.QuotationAdditionEntity;
import pico.erp.quotation.addition.QuotationAdditionRepository;
import pico.erp.quotation.item.QuotationItem;
import pico.erp.quotation.item.QuotationItemEntity;
import pico.erp.quotation.item.QuotationItemRepository;
import pico.erp.quotation.item.addition.QuotationItemAddition;
import pico.erp.quotation.item.addition.QuotationItemAdditionEntity;
import pico.erp.quotation.item.addition.QuotationItemAdditionRepository;
import pico.erp.user.UserData;
import pico.erp.user.UserId;
import pico.erp.user.UserService;

@Mapper
public abstract class QuotationJpaMapper {

  @Lazy
  @Autowired
  private BomService bomService;

  @Lazy
  @Autowired
  private CompanyService companyService;

  @Lazy
  @Autowired
  private UserService userService;

  @Lazy
  @Autowired
  private ItemService itemService;

  @Lazy
  @Autowired
  private ProjectService projectService;

  @Lazy
  @Autowired
  private QuotationAdditionRepository quotationAdditionRepository;

  @Lazy
  @Autowired
  private QuotationItemRepository quotationItemRepository;

  @Lazy
  @Autowired
  private QuotationItemAdditionRepository quotationItemAdditionRepository;

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

  public QuotationItem map(QuotationItemEntity entity) {
    val itemId = entity.getItemId();
    val bomData = bomService.exists(itemId) ? bomService.get(itemId) : null;
    return QuotationItem.builder()
      .quotation(map(entity.getQuotation()))
      .bomData(bomData)
      .itemData(map(entity.getItemId()))
      .description(entity.getDescription())
      .remark(entity.getRemark())
      .quantity(entity.getQuantity())
      .originalUnitPrice(entity.getOriginalUnitPrice())
      .directLaborUnitPrice(entity.getDirectLaborUnitPrice())
      .indirectLaborUnitPrice(entity.getIndirectLaborUnitPrice())
      .indirectMaterialUnitPrice(entity.getIndirectMaterialUnitPrice())
      .directMaterialUnitPrice(entity.getDirectMaterialUnitPrice())
      .indirectExpensesUnitPrice(entity.getIndirectExpensesUnitPrice())
      .additionalRate(entity.getAdditionalRate())
      .discountRate(entity.getDiscountRate())
      .id(entity.getId())
      .bomData(mapBom(entity.getItemId()))
      .unitPriceManuallyFixed(entity.isUnitPriceManuallyFixed())
      .build();
  }

  protected ItemData map(ItemId itemId) {
    return Optional.ofNullable(itemId)
      .map(itemService::get)
      .orElse(null);
  }

  public Quotation map(QuotationEntity entity) {
    return Quotation.builder()
      .code(entity.getCode())
      .id(entity.getId())
      .previousId(entity.getPreviousId())
      .commentSubjectId(entity.getCommentSubjectId())
      .revision(entity.getRevision())
      .projectData(map(entity.getProjectId()))
      .customerData(map(entity.getCustomerId()))
      .name(entity.getName())
      .expiryPolicy(entity.getExpiryPolicy())
      .managerData(map(entity.getManagerId()))
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
      /*.items(
        entity.getItems().stream().map(this::map).collect(Collectors.toList())
      )
      .itemAdditions(
        entity.getItemAdditions().stream().map(this::map).collect(Collectors.toList())
      )
      .additions(
        entity.getAdditions().stream().map(this::map).collect(Collectors.toList())
      )*/
      .build();
  }

  @Mappings({
    //@Mapping(target = "bomId", source = "bomData.id"),
    @Mapping(target = "itemId", source = "itemData.id"),
    @Mapping(target = "createdBy", ignore = true),
    @Mapping(target = "createdDate", ignore = true)
  })
  public abstract QuotationItemEntity map(QuotationItem item);

  @Mappings({
    @Mapping(target = "projectId", source = "projectData.id"),
    @Mapping(target = "projectName", source = "projectData.name"),
    @Mapping(target = "customerId", source = "customerData.id"),
    @Mapping(target = "customerName", source = "customerData.name"),
    @Mapping(target = "managerId", source = "managerData.id"),
    @Mapping(target = "managerName", source = "managerData.name"),
    @Mapping(target = "createdBy", ignore = true),
    @Mapping(target = "createdDate", ignore = true),
    @Mapping(target = "lastModifiedBy", ignore = true),
    @Mapping(target = "lastModifiedDate", ignore = true)
  })
  public abstract QuotationEntity map(Quotation quotation);

  protected BomData mapBom(ItemId itemId) {
    return Optional.ofNullable(itemId)
      .map(bomService::get)
      .orElse(null);
  }

  public QuotationItemAddition map(QuotationItemAdditionEntity entity) {
    return QuotationItemAddition.builder()
      .quotation(map(entity.getQuotation()))
      .id(entity.getId())
      .name(entity.getName())
      .description(entity.getDescription())
      .remark(entity.getRemark())
      .additionalRate(entity.getAdditionalRate())
      .build();
  }

  @Mappings({
    @Mapping(target = "createdBy", ignore = true),
    @Mapping(target = "createdDate", ignore = true)
  })
  public abstract QuotationItemAdditionEntity map(QuotationItemAddition itemAddition);

  @Mappings({
    @Mapping(target = "createdBy", ignore = true),
    @Mapping(target = "createdDate", ignore = true)
  })
  public abstract QuotationAdditionEntity map(QuotationAddition addition);

  public QuotationAddition map(QuotationAdditionEntity entity) {
    return QuotationAddition.builder()
      .quotation(map(entity.getQuotation()))
      .id(entity.getId())
      .name(entity.getName())
      .description(entity.getDescription())
      .remark(entity.getRemark())
      .quantity(entity.getQuantity())
      .unitPrice(entity.getUnitPrice())
      .build();
  }

  public QuotationAggregator mapAggregator(QuotationEntity entity) {
    return QuotationAggregator.aggregatorBuilder()
      .code(entity.getCode())
      .id(entity.getId())
      .previousId(entity.getPreviousId())
      .commentSubjectId(entity.getCommentSubjectId())
      .revision(entity.getRevision())
      .projectData(map(entity.getProjectId()))
      .customerData(map(entity.getCustomerId()))
      .name(entity.getName())
      .expiryPolicy(entity.getExpiryPolicy())
      .managerData(map(entity.getManagerId()))
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


  public abstract void pass(QuotationEntity from, @MappingTarget QuotationEntity to);

  public abstract void pass(QuotationItemEntity from, @MappingTarget QuotationItemEntity to);

  public abstract void pass(QuotationAdditionEntity from,
    @MappingTarget QuotationAdditionEntity to);

  public abstract void pass(QuotationItemAdditionEntity from,
    @MappingTarget QuotationItemAdditionEntity to);
}
