package pico.erp.quotation.item;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;
import pico.erp.bom.BomData;
import pico.erp.item.ItemData;
import pico.erp.quotation.Quotation;
import pico.erp.quotation.item.addition.QuotationItemAddition;
import pico.erp.quotation.item.data.QuotationItemId;
import pico.erp.shared.TypeDefinitions;
import pico.erp.shared.event.Event;

public interface QuotationItemMessages {

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  class CreateRequest {

    @Valid
    @NotNull
    QuotationItemId id;

    @NotNull
    Quotation quotation;

    @NotNull
    ItemData itemData;

    BomData bomData;

    @NotNull
    BigDecimal discountRate;

    @Min(0)
    BigDecimal quantity;

    @Size(max = TypeDefinitions.DESCRIPTION_LENGTH)
    String description;

    @Size(max = TypeDefinitions.REMARK_LENGTH)
    String remark;

  }

  @Value
  class CreateResponse {

    Collection<Event> events;

  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  class UpdateRequest {

    BomData bomData;

    @NotNull
    BigDecimal discountRate;

    @Min(0)
    BigDecimal quantity;

    @Size(max = TypeDefinitions.DESCRIPTION_LENGTH)
    String description;

    @Size(max = TypeDefinitions.REMARK_LENGTH)
    String remark;

  }

  @Value
  class UpdateResponse {

    Collection<Event> events;

  }

  @Data
  class DeleteRequest {

  }

  @Value
  class DeleteResponse {

    Collection<Event> events;
  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  class NextDraftRequest {

    @NotNull
    Quotation quotation;

  }

  @Value
  class NextDraftResponse {

    QuotationItem nextDrafted;

    Collection<Event> events;

  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  class ApplyItemAdditionRequest {

    @NotNull
    List<QuotationItemAddition> itemAdditions;

  }

  @Value
  class ApplyItemAdditionResponse {

    Collection<Event> events;

  }

  @Data
  class VerifyRequest {

  }

  @Value
  class VerifyResponse {

    Collection<Event> events;

  }

  @Data
  class FixUnitPriceRequest {

    /**
     * 계산 전 단가
     */
    @NotNull
    BigDecimal originalUnitPrice;

    /**
     * 누적된 직접 노무비
     */
    @NotNull
    BigDecimal directLaborUnitPrice;

    /**
     * 누적된 간접 노무비
     */
    @NotNull
    BigDecimal indirectLaborUnitPrice;

    /**
     * 누적된 간접 재료비
     */
    @NotNull
    BigDecimal indirectMaterialUnitPrice;

    /**
     * 직접 재료 원가
     */
    @NotNull
    BigDecimal directMaterialUnitPrice;

    /**
     * 누적된 간접 경비
     */
    @NotNull
    BigDecimal indirectExpensesUnitPrice;

  }

  @Value
  class FixUnitPriceResponse {

    Collection<Event> events;

  }

}
