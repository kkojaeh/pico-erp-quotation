package pico.erp.quotation.print;

import java.time.OffsetDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pico.erp.company.CompanyData;
import pico.erp.project.ProjectData;
import pico.erp.quotation.QuotationCode;
import pico.erp.quotation.QuotationExpiryPolicyKind;
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

  CompanyData customer;

  ProjectData project;

  QuotationExpiryPolicyKind expiryPolicy;

  UserData manager;

  String publicDescription;

  OffsetDateTime committedDate;

  List<QuotationPrintItemData> items;

  List<QuotationPrintItemAdditionRateData> itemAdditions;

  List<QuotationExportAdditionData> additions;

}
