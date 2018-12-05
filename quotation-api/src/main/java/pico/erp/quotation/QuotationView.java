package pico.erp.quotation;

import java.time.OffsetDateTime;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pico.erp.company.CompanyId;
import pico.erp.project.ProjectId;
import pico.erp.shared.data.Auditor;
import pico.erp.user.UserId;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class QuotationView {

  QuotationId id;

  int revision;

  String name;

  ProjectId projectId;

  CompanyId customerId;

  UserId managerId;

  OffsetDateTime committedDate;

  QuotationStatusKind status;

  Auditor createdBy;

  OffsetDateTime createdDate;

  Auditor lastModifiedBy;

  OffsetDateTime lastModifiedDate;

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class Filter {

    String name;

    ProjectId projectId;

    UserId managerId;

    CompanyId customerId;

    Set<QuotationStatusKind> statuses;

    OffsetDateTime startCreatedDate;

    OffsetDateTime endCreatedDate;

  }

}
