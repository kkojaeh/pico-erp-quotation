package pico.erp.quotation.item.addition;

import java.math.BigDecimal;
import java.util.Collection;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;
import pico.erp.quotation.Quotation;
import pico.erp.shared.TypeDefinitions;
import pico.erp.shared.event.Event;

public interface QuotationItemAdditionMessages {

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  class CreateRequest {

    @Valid
    @NotNull
    QuotationItemAdditionId id;

    @NotNull
    Quotation quotation;

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

  @Value
  class CreateResponse {

    Collection<Event> events;

  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  class UpdateRequest {

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

    QuotationItemAddition nextDrafted;

    Collection<Event> events;

  }

}
