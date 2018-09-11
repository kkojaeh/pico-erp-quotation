package pico.erp.quotation.item;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import lombok.val;
import pico.erp.bom.data.BomData;
import pico.erp.bom.data.BomUnitCostData;
import pico.erp.item.data.ItemData;
import pico.erp.quotation.Quotation;
import pico.erp.quotation.QuotationExceptions.CannotModifyException;
import pico.erp.quotation.item.QuotationItemMessages.ApplyItemAdditionRequest;
import pico.erp.quotation.item.QuotationItemMessages.ApplyItemAdditionResponse;
import pico.erp.quotation.item.QuotationItemMessages.FixUnitPriceRequest;
import pico.erp.quotation.item.QuotationItemMessages.FixUnitPriceResponse;
import pico.erp.quotation.item.data.QuotationItemId;
import pico.erp.shared.event.Event;

@Getter
@ToString
@EqualsAndHashCode(of = "id")
@FieldDefaults(level = AccessLevel.PROTECTED)
@Builder(toBuilder = true)
@AllArgsConstructor
public class QuotationItem {

  QuotationItemId id;

  Quotation quotation;

  String description;

  String remark;

  BigDecimal quantity;

  ItemData itemData;

  BomData bomData;

  /**
   * 계산 전 단가
   */
  BigDecimal originalUnitPrice;

  /**
   * 누적된 직접 노무비
   */
  BigDecimal directLaborUnitPrice;

  /**
   * 누적된 간접 노무비
   */
  BigDecimal indirectLaborUnitPrice;

  /**
   * 누적된 간접 재료비
   */
  BigDecimal indirectMaterialUnitPrice;

  /**
   * 직접 재료 원가
   */
  BigDecimal directMaterialUnitPrice;

  /**
   * 누적된 간접 경비
   */
  BigDecimal indirectExpensesUnitPrice;

  /**
   * 추가 단가율
   */
  BigDecimal additionalRate;

  /**
   * 할인율 0 ~ 1
   */
  BigDecimal discountRate;

  boolean unitPriceManuallyFixed;

  public QuotationItem() {
    originalUnitPrice = BigDecimal.ZERO;
    directLaborUnitPrice = BigDecimal.ZERO;
    indirectLaborUnitPrice = BigDecimal.ZERO;
    indirectMaterialUnitPrice = BigDecimal.ZERO;
    directMaterialUnitPrice = BigDecimal.ZERO;
    indirectExpensesUnitPrice = BigDecimal.ZERO;
    additionalRate = BigDecimal.ZERO;
    discountRate = BigDecimal.ZERO;
  }

  public QuotationItemMessages.CreateResponse apply(
    QuotationItemMessages.CreateRequest request) {
    if (!request.getQuotation().canModify()) {
      throw new CannotModifyException();
    }
    this.id = request.getId();
    this.quotation = request.getQuotation();
    this.description = request.getDescription();
    this.quantity = request.getQuantity();
    this.itemData = request.getItemData();
    this.bomData = request.getBomData();
    this.remark = request.getRemark();
    this.discountRate = request.getDiscountRate();
    applyUnitPrices();
    return new QuotationItemMessages.CreateResponse(
      Arrays.asList(new QuotationItemEvents.CreatedEvent(this.id)));
  }

  public QuotationItemMessages.UpdateResponse apply(
    QuotationItemMessages.UpdateRequest request) {
    if (!this.quotation.canModify()) {
      throw new CannotModifyException();
    }
    this.description = request.getDescription();
    this.quantity = request.getQuantity();
    this.bomData = request.getBomData();
    this.remark = request.getRemark();
    this.discountRate = request.getDiscountRate();
    applyUnitPrices();
    return new QuotationItemMessages.UpdateResponse(
      Arrays.asList(new QuotationItemEvents.UpdatedEvent(this.id)));
  }

  public QuotationItemMessages.DeleteResponse apply(
    QuotationItemMessages.DeleteRequest request) {
    if (!this.quotation.canModify()) {
      throw new CannotModifyException();
    }
    return new QuotationItemMessages.DeleteResponse(
      Arrays.asList(new QuotationItemEvents.DeletedEvent(this.id)));
  }

  public QuotationItemMessages.NextDraftResponse apply(
    QuotationItemMessages.NextDraftRequest request) {
    QuotationItem draft = toBuilder().build();
    draft.id = QuotationItemId.generate();
    draft.quotation = request.getQuotation();
    return new QuotationItemMessages.NextDraftResponse(draft, Collections.emptyList());
  }

  public ApplyItemAdditionResponse apply(
    ApplyItemAdditionRequest request) {
    if (!this.quotation.canModify()) {
      throw new CannotModifyException();
    }
    val sumOfAdditionRate = request.getItemAdditions().stream()
      .map(itemAddition -> itemAddition.getAdditionalRate())
      .reduce(BigDecimal.ZERO, (accumulator, rate) -> accumulator.add(rate));
    val events = new LinkedList<Event>();
    if (!sumOfAdditionRate.equals(additionalRate)) {
      additionalRate = sumOfAdditionRate;
      events.add(new QuotationItemEvents.UpdatedEvent(this.id));
    }
    return new ApplyItemAdditionResponse(events);
  }

  public QuotationItemMessages.VerifyResponse apply(
    QuotationItemMessages.VerifyRequest request) {
    if (!this.quotation.canModify()) {
      throw new CannotModifyException();
    }
    applyUnitPrices();
    return new QuotationItemMessages.VerifyResponse(
      Arrays.asList(new QuotationItemEvents.UpdatedEvent(this.id)));
  }

  public FixUnitPriceResponse apply(
    FixUnitPriceRequest request) {
    if (!this.quotation.canModify()) {
      throw new CannotModifyException();
    }
    unitPriceManuallyFixed = true;
    originalUnitPrice = request.getOriginalUnitPrice();
    directMaterialUnitPrice = request.getDirectMaterialUnitPrice();
    indirectMaterialUnitPrice = request.getIndirectMaterialUnitPrice();
    directLaborUnitPrice = request.getDirectLaborUnitPrice();
    indirectLaborUnitPrice = request.getIndirectLaborUnitPrice();
    indirectExpensesUnitPrice = request.getIndirectExpensesUnitPrice();
    return new FixUnitPriceResponse(
      Arrays.asList(new QuotationItemEvents.UpdatedEvent(this.id)));
  }

  private void applyUnitPrices() {
    if (bomData != null && !unitPriceManuallyFixed) {
      BomUnitCostData unitCost = bomData.getEstimatedAccumulatedUnitCost();
      originalUnitPrice = unitCost.getTotal();
      directMaterialUnitPrice = unitCost.getDirectMaterial();
      indirectMaterialUnitPrice = unitCost.getIndirectMaterial();
      directLaborUnitPrice = unitCost.getDirectLabor();
      indirectLaborUnitPrice = unitCost.getIndirectLabor();
      indirectExpensesUnitPrice = unitCost.getIndirectExpenses();
    }
  }

  public BigDecimal getDiscountedAmount() {
    return Optional.ofNullable(quantity)
      .map(quantity -> getDiscountedUnitPrice().multiply(quantity))
      .orElse(null);
  }

  /**
   * 할인율이 적용된 단가 최초 단가 - (최초 단가 * 할인율)
   */
  public BigDecimal getDiscountedUnitPrice() {
    return originalUnitPrice.subtract(originalUnitPrice.multiply(discountRate));
  }

  public BigDecimal getFinalizedAmount() {
    return Optional.ofNullable(quantity)
      .map(quantity -> getFinalizedUnitPrice().multiply(quantity).setScale(2, BigDecimal.ROUND_HALF_UP))
      .orElse(null);
  }

  /**
   * 최종 단가 할인율이 적용된 단가에 추가 단가 및 추가 단가율을 적용 단가 (최초 단가 - (최초 단가 * 할인율)) + ((최초 단가 - (최초 단가 * 할인율)) *
   * 부가단가율) + 부가 단가
   */
  public BigDecimal getFinalizedUnitPrice() {
    BigDecimal discounted = this.getDiscountedUnitPrice();
    return discounted.add(discounted.multiply(additionalRate)).setScale(2, BigDecimal.ROUND_HALF_UP);
  }

  public BigDecimal getOriginalAmount() {
    return Optional.ofNullable(quantity)
      .map(quantity -> getOriginalUnitPrice().multiply(quantity).setScale(2, BigDecimal.ROUND_HALF_UP))
      .orElse(null);
  }

  public boolean isCommittable() {
    if (bomData != null && bomData.isStable()) {
      return true;
    }
    return false;
  }

}
