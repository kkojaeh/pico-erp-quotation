package pico.erp.quotation;

import java.time.LocalDateTime;
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

  QuotationCode code;

  int revision;

  String name;

  ProjectId projectId;

  CompanyId customerId;

  UserId managerId;

  LocalDateTime committedDate;

  QuotationStatusKind status;

  Auditor createdBy;

  LocalDateTime createdDate;

  Auditor lastModifiedBy;

  LocalDateTime lastModifiedDate;

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class Filter {

    String name;

    String code;

    ProjectId projectId;

    UserId managerId;

    CompanyId customerId;

    Set<QuotationStatusKind> statuses;

    LocalDateTime startCreatedDate;

    LocalDateTime endCreatedDate;

  }

}
