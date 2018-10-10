package pico.erp.quotation.impl.print;

import java.time.OffsetDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pico.erp.company.CompanyData;
import pico.erp.project.ProjectData;
import pico.erp.quotation.data.QuotationCode;
import pico.erp.quotation.data.QuotationExpiryPolicyKind;
import pico.erp.user.UserData;

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
