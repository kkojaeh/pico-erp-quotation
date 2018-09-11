package pico.erp.quotation.addition;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pico.erp.quotation.Quotation;
import pico.erp.quotation.QuotationExceptions.CannotModifyException;
import pico.erp.quotation.addition.data.QuotationAdditionId;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@EqualsAndHashCode(of = "id")
public class QuotationAddition {

  QuotationAdditionId id;

  Quotation quotation;

  String name;

  String description;

  BigDecimal quantity;

  BigDecimal unitPrice;

  String remark;

  public QuotationAdditionMessages.CreateResponse apply(
    QuotationAdditionMessages.CreateRequest request) {
    if (!request.getQuotation().canModify()) {
      throw new CannotModifyException();
    }
    this.id = request.getId();
    this.quotation = request.getQuotation();
    this.name = request.getName();
    this.description = request.getDescription();
    this.quantity = request.getQuantity();
    this.unitPrice = request.getUnitPrice();
    this.remark = request.getRemark();
    return new QuotationAdditionMessages.CreateResponse(
      Arrays.asList(new QuotationAdditionEvents.CreatedEvent(this.id)));
  }

  public QuotationAdditionMessages.UpdateResponse apply(
    QuotationAdditionMessages.UpdateRequest request) {
    if (!this.quotation.canModify()) {
      throw new CannotModifyException();
    }
    this.name = request.getName();
    this.description = request.getDescription();
    this.quantity = request.getQuantity();
    this.unitPrice = request.getUnitPrice();
    this.remark = request.getRemark();
    return new QuotationAdditionMessages.UpdateResponse(
      Arrays.asList(new QuotationAdditionEvents.UpdatedEvent(this.id)));
  }

  public QuotationAdditionMessages.DeleteResponse apply(
    QuotationAdditionMessages.DeleteRequest request) {
    if (!this.quotation.canModify()) {
      throw new CannotModifyException();
    }
    return new QuotationAdditionMessages.DeleteResponse(
      Arrays.asList(new QuotationAdditionEvents.DeletedEvent(this.id)));
  }

  public QuotationAdditionMessages.NextDraftResponse apply(
    QuotationAdditionMessages.NextDraftRequest request) {
    QuotationAddition draft = toBuilder().build();
    draft.id = QuotationAdditionId.generate();
    draft.quotation = request.getQuotation();
    return new QuotationAdditionMessages.NextDraftResponse(draft, Collections.emptyList());
  }

  public BigDecimal getAmount() {
    if (quantity != null && unitPrice != null) {
      return quantity.multiply(unitPrice);
    }
    return null;
  }


}
