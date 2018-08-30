package pico.erp.quotation.impl;

import java.util.Optional;
import java.util.stream.Collectors;
import lombok.val;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
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
import pico.erp.quotation.core.QuotationAdditionRepository;
import pico.erp.quotation.core.QuotationItemAdditionRepository;
import pico.erp.quotation.core.QuotationItemRepository;
import pico.erp.quotation.domain.Quotation;
import pico.erp.quotation.domain.QuotationAddition;
import pico.erp.quotation.domain.QuotationAggregator;
import pico.erp.quotation.domain.QuotationItem;
import pico.erp.quotation.domain.QuotationItemAddition;
import pico.erp.quotation.domain.QuotationUnitPrice;
import pico.erp.quotation.impl.jpa.QuotationAdditionEntity;
import pico.erp.quotation.impl.jpa.QuotationEntity;
import pico.erp.quotation.impl.jpa.QuotationItemAdditionEntity;
import pico.erp.quotation.impl.jpa.QuotationItemEntity;
import pico.erp.quotation.impl.jpa.QuotationUnitPriceEmbeddable;
import pico.erp.user.UserService;
import pico.erp.user.data.UserData;
import pico.erp.user.data.UserId;

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

  @Autowired
  private QuotationAdditionRepository quotationAdditionRepository;

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

  protected QuotationItem map(QuotationItemEntity entity) {
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

  protected Quotation map(QuotationEntity entity) {
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
  protected abstract QuotationItemEntity map(QuotationItem item);

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
  protected abstract QuotationEntity map(Quotation quotation);

  protected BomData mapBom(ItemId itemId) {
    return Optional.ofNullable(itemId)
      .map(bomService::get)
      .orElse(null);
  }

  protected QuotationItemAddition map(QuotationItemAdditionEntity entity) {
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
  protected abstract QuotationItemAdditionEntity map(QuotationItemAddition itemAddition);

  @Mappings({
    @Mapping(target = "createdBy", ignore = true),
    @Mapping(target = "createdDate", ignore = true)
  })
  protected abstract QuotationAdditionEntity map(QuotationAddition addition);

  protected QuotationAddition map(QuotationAdditionEntity entity) {
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

  protected QuotationAggregator mapAggregator(QuotationEntity entity) {
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

  protected abstract QuotationUnitPriceEmbeddable map(QuotationUnitPrice unitPrice);

  protected QuotationUnitPrice map(QuotationUnitPriceEmbeddable unitPrice) {
    return QuotationUnitPrice.builder()
      .original(unitPrice.getOriginal())
      .directLabor(unitPrice.getDirectLabor())
      .indirectLabor(unitPrice.getIndirectLabor())
      .directMaterial(unitPrice.getDirectMaterial())
      .indirectMaterial(unitPrice.getIndirectMaterial())
      .indirectExpenses(unitPrice.getIndirectExpenses())
      .additionalRate(unitPrice.getAdditionalRate())
      .discountRate(unitPrice.getDiscountRate())
      .build();
  }


  public abstract void pass(QuotationEntity from, @MappingTarget QuotationEntity to);

  public abstract void pass(QuotationItemEntity from, @MappingTarget QuotationItemEntity to);

  public abstract void pass(QuotationAdditionEntity from,
    @MappingTarget QuotationAdditionEntity to);

  public abstract void pass(QuotationItemAdditionEntity from,
    @MappingTarget QuotationItemAdditionEntity to);
}
