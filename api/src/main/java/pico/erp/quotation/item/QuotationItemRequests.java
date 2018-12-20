package pico.erp.quotation.item;

import java.math.BigDecimal;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pico.erp.item.ItemId;
import pico.erp.quotation.QuotationId;
import pico.erp.shared.TypeDefinitions;

public interface QuotationItemRequests {

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  class CreateRequest {

    @Valid
    @NotNull
    QuotationItemId id;

    @Valid
    @NotNull
    QuotationId quotationId;

    @Valid
    @NotNull
    ItemId itemId;


    @Min(0)
    BigDecimal quantity;

    @Valid
    @NotNull
    BigDecimal discountRate;

    @Size(max = TypeDefinitions.DESCRIPTION_LENGTH)
    String description;

    @Size(max = TypeDefinitions.REMARK_LENGTH)
    String remark;

  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  class UpdateRequest {

    @Valid
    @NotNull
    QuotationItemId id;

    @Valid
    @NotNull
    ItemId itemId;

    @Min(0)
    BigDecimal quantity;

    @Valid
    @NotNull
    BigDecimal discountRate;

    @Size(max = TypeDefinitions.DESCRIPTION_LENGTH)
    String description;

    @Size(max = TypeDefinitions.REMARK_LENGTH)
    String remark;

  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  class DeleteRequest {

    @Valid
    @NotNull
    QuotationItemId id;

  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  class FixUnitPriceRequest {

    @Valid
    @NotNull
    QuotationItemId id;

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
}
