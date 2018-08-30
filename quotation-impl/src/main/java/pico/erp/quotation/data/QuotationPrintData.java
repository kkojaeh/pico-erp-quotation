package pico.erp.quotation.data;

import java.time.OffsetDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pico.erp.company.data.CompanyData;
import pico.erp.project.data.ProjectData;
import pico.erp.user.data.UserData;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QuotationPrintData {

  QuotationCode code;

  String name;

  int revision;

  CompanyData supplierData;

  CompanyData customerData;

  ProjectData projectData;

  QuotationExpiryPolicyKind expiryPolicy;

  UserData managerData;

  String publicDescription;

  OffsetDateTime committedDate;

  List<QuotationPrintItemData> items;

  List<QuotationPrintItemAdditionRateData> itemAdditions;

  List<QuotationExportAdditionData> additions;

}
