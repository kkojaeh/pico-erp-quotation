package pico.erp.quotation.domain;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pico.erp.quotation.QuotationExceptions.CannotModifyException;
import pico.erp.quotation.QuotationItemAdditionEvents;
import pico.erp.quotation.data.QuotationItemAdditionId;

@EqualsAndHashCode(of = "id")
@Getter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class QuotationItemAddition {

  QuotationItemAdditionId id;

  Quotation quotation;

  String name;

  String description;

  String remark;

  BigDecimal additionalRate;

  public QuotationItemAdditionMessages.CreateResponse apply(
    QuotationItemAdditionMessages.CreateRequest request) {
    if (!request.getQuotation().canModify()) {
      throw new CannotModifyException();
    }
    this.id = request.getId();
    this.quotation = request.getQuotation();
    this.name = request.getName();
    this.description = request.getDescription();
    this.remark = request.getRemark();
    this.additionalRate = request.getAdditionalRate();
    return new QuotationItemAdditionMessages.CreateResponse(
      Arrays.asList(new QuotationItemAdditionEvents.CreatedEvent(this.id)));
  }

  public QuotationItemAdditionMessages.UpdateResponse apply(
    QuotationItemAdditionMessages.UpdateRequest request) {
    if (!this.quotation.canModify()) {
      throw new CannotModifyException();
    }
    this.name = request.getName();
    this.description = request.getDescription();
    this.remark = request.getRemark();
    this.additionalRate = request.getAdditionalRate();
    return new QuotationItemAdditionMessages.UpdateResponse(
      Arrays.asList(new QuotationItemAdditionEvents.UpdatedEvent(this.id)));
  }

  public QuotationItemAdditionMessages.DeleteResponse apply(
    QuotationItemAdditionMessages.DeleteRequest request) {
    if (!this.quotation.canModify()) {
      throw new CannotModifyException();
    }
    return new QuotationItemAdditionMessages.DeleteResponse(
      Arrays.asList(new QuotationItemAdditionEvents.DeletedEvent(this.id)));
  }

  public QuotationItemAdditionMessages.NextDraftResponse apply(
    QuotationItemAdditionMessages.NextDraftRequest request) {
    QuotationItemAddition draft = toBuilder().build();
    draft.id = QuotationItemAdditionId.generate();
    draft.quotation = request.getQuotation();
    return new QuotationItemAdditionMessages.NextDraftResponse(draft, Collections.emptyList());
  }

}
