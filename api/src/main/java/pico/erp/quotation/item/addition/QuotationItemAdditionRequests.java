package pico.erp.quotation.item.addition;

import java.math.BigDecimal;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pico.erp.quotation.QuotationId;
import pico.erp.shared.TypeDefinitions;

public interface QuotationItemAdditionRequests {

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  class CreateRequest {

    @Valid
    @NotNull
    QuotationItemAdditionId id;

    @Valid
    @NotNull
    QuotationId quotationId;

    @NotNull
    @Size(max = TypeDefinitions.NAME_LENGTH)
    String name;

    @Size(max = TypeDefinitions.DESCRIPTION_LENGTH)
    String description;

    @Size(max = TypeDefinitions.REMARK_LENGTH)
    String remark;

    @Min(0)
    @Max(1)
    BigDecimal additionalRate;

  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  class UpdateRequest {

    @Valid
    @NotNull
    QuotationItemAdditionId id;

    @NotNull
    @Size(max = TypeDefinitions.NAME_LENGTH)
    String name;

    @Size(max = TypeDefinitions.DESCRIPTION_LENGTH)
    String description;

    @Size(max = TypeDefinitions.REMARK_LENGTH)
    String remark;

    @Min(0)
    @Max(1)
    BigDecimal additionalRate;

  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  class DeleteRequest {

    @Valid
    @NotNull
    QuotationItemAdditionId id;

  }
}
