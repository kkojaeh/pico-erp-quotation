package pico.erp.quotation;

import java.util.Collection;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;
import lombok.Value;
import pico.erp.attachment.AttachmentId;
import pico.erp.company.CompanyData;
import pico.erp.project.ProjectData;
import pico.erp.shared.TypeDefinitions;
import pico.erp.shared.data.Auditor;
import pico.erp.shared.event.Event;
import pico.erp.user.UserData;

public interface QuotationMessages {

  @Data
  class ExpireRequest {

  }

  @Data
  class CancelRequest {

    Auditor canceler;

  }

  @Data
  class CommitRequest {

    Auditor committer;

  }

  @Data
  class DraftRequest {

    @Valid
    @NotNull
    QuotationId id;

    @Valid
    @NotNull
    ProjectData project;

    @Valid
    @NotNull
    CompanyData customer;

    @Size(max = TypeDefinitions.NAME_LENGTH)
    @NotNull
    String name;

    @NotNull
    QuotationExpiryPolicyKind expiryPolicy;

    @Valid
    @NotNull
    UserData manager;

    @Size(max = TypeDefinitions.DESCRIPTION_LENGTH)
    String protectedDescription;

    @Size(max = TypeDefinitions.DESCRIPTION_LENGTH)
    String publicDescription;

    @Valid
    AttachmentId attachmentId;

    @NotNull
    QuotationCodeGenerator codeGenerator;

  }

  @Data
  class NextDraftRequest {

    @Valid
    @NotNull
    QuotationId nextId;

    @NotNull
    QuotationCodeGenerator codeGenerator;

  }

  @Data
  class PrintSheetRequest {

    QuotationPrintSheetOptions options;

  }


  @Data
  class UpdateRequest {

    @Valid
    @NotNull
    ProjectData project;

    @Valid
    @NotNull
    CompanyData customer;

    @Size(max = TypeDefinitions.NAME_LENGTH)
    String name;

    @NotNull
    QuotationExpiryPolicyKind expiryPolicy;

    @Valid
    @NotNull
    UserData manager;

    @Size(max = TypeDefinitions.DESCRIPTION_LENGTH)
    String protectedDescription;

    @Size(max = TypeDefinitions.DESCRIPTION_LENGTH)
    String publicDescription;

    @Valid
    AttachmentId attachmentId;

  }

  @Data
  class DeleteRequest {

  }

  @Value
  class ExpireResponse {

    Collection<Event> events;
  }

  @Value
  class CancelResponse {

    Collection<Event> events;

  }

  @Value
  class CommitResponse {

    Collection<Event> events;

  }

  @Value
  class DraftResponse {

    Collection<Event> events;

  }

  @Value
  class PrintSheetResponse {

    Collection<Event> events;

  }

  @Value
  class UpdateResponse {

    Collection<Event> events;

  }

  @Value
  class DeleteResponse {

    Collection<Event> events;
  }

  @Value
  class NextDraftResponse {

    Quotation nextDrafted;

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
  class PrepareRequest {

  }

  @Value
  class PrepareResponse {

    Collection<Event> events;

  }


}
