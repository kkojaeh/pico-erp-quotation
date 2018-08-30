package pico.erp.quotation;

import java.time.OffsetDateTime;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pico.erp.attachment.data.AttachmentId;
import pico.erp.company.data.CompanyId;
import pico.erp.project.data.ProjectId;
import pico.erp.quotation.data.QuotationExpiryPolicyKind;
import pico.erp.quotation.data.QuotationId;
import pico.erp.quotation.data.QuotationPrintSheetOptions;
import pico.erp.shared.TypeDefinitions;
import pico.erp.user.data.UserId;

public interface QuotationRequests {

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  class ExpireRequest {

    /**
     * 지정 기준시간보다 예전 데이터를 삭제 테스트를 위해 @Past 를 사용하지 않는다
     */
    @NotNull
    OffsetDateTime fixedDate;

  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  class CancelRequest {

    @Valid
    @NotNull
    QuotationId id;

  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  class CommitRequest {

    @Valid
    @NotNull
    QuotationId id;

  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  class PrepareRequest {

    @Valid
    @NotNull
    QuotationId id;

  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  class DraftRequest {

    @Valid
    @NotNull
    QuotationId id;

    @Valid
    @NotNull
    ProjectId projectId;

    @Valid
    @NotNull
    CompanyId customerId;

    @Size(max = TypeDefinitions.NAME_LENGTH)
    @NotNull
    String name;

    @NotNull
    QuotationExpiryPolicyKind expiryPolicy;

    @Valid
    @NotNull
    UserId managerId;

    @Size(max = TypeDefinitions.DESCRIPTION_LENGTH)
    String protectedDescription;

    @Size(max = TypeDefinitions.DESCRIPTION_LENGTH)
    String publicDescription;

    @Valid
    AttachmentId attachmentId;

  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  class NextDraftRequest {

    @Valid
    @NotNull
    QuotationId id;

    @Valid
    @NotNull
    QuotationId nextId;

  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  class PrintSheetRequest {

    @Valid
    @NotNull
    QuotationId id;

    QuotationPrintSheetOptions options;

  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  class UpdateRequest {

    @Valid
    @NotNull
    QuotationId id;

    @Valid
    @NotNull
    ProjectId projectId;

    @Valid
    @NotNull
    CompanyId customerId;

    @Size(max = TypeDefinitions.NAME_LENGTH)
    String name;

    @NotNull
    QuotationExpiryPolicyKind expiryPolicy;

    @Valid
    @NotNull
    UserId managerId;

    @Size(max = TypeDefinitions.DESCRIPTION_LENGTH)
    String protectedDescription;

    @Size(max = TypeDefinitions.DESCRIPTION_LENGTH)
    String publicDescription;

    @Valid
    AttachmentId attachmentId;

  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  class DeleteRequest {

    @Valid
    @NotNull
    QuotationId id;

  }
}
