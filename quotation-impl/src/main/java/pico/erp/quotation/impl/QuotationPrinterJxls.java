package pico.erp.quotation.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import lombok.Cleanup;
import lombok.SneakyThrows;
import lombok.val;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.jxls.common.Context;
import org.jxls.transform.poi.PoiTransformer;
import org.jxls.util.JxlsHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import pico.erp.quotation.QuotationAggregator;
import pico.erp.quotation.QuotationPrinter;
import pico.erp.quotation.QuotationRepository;
import pico.erp.quotation.data.QuotationPrintSheetOptions;
import pico.erp.shared.ExportHelper;
import pico.erp.shared.data.ContentInputStream;

@Component
public class QuotationPrinterJxls implements QuotationPrinter {

  @Value("${quotation.print.sheet-template}")
  private Resource sheetTemplate;

  @Autowired
  private ExportHelper exportHelper;

  @Autowired
  private QuotationPrintMapper quotationPrintMapper;

  @Autowired
  private QuotationRepository quotationRepository;

  private static OutputStream NOOP_OUTPUT_STREAM = new OutputStream() {
    @Override
    public void write(int b) throws IOException {
    }
  };

  @SneakyThrows
  @Override
  public ContentInputStream printSheet(QuotationAggregator quotation,
    QuotationPrintSheetOptions options) {

    val data = quotationPrintMapper.map(quotation);

    @Cleanup
    InputStream jxlsTemplate = sheetTemplate.getInputStream();

    Context context = new Context();
    context.putVar("data", data);
    context.putVar("options", options);
    context.putVar("helper", exportHelper);

    Workbook workbook = WorkbookFactory.create(jxlsTemplate);

    PoiTransformer transformer = PoiTransformer.createTransformer(workbook);
    transformer.setOutputStream(NOOP_OUTPUT_STREAM);

    JxlsHelper.getInstance().processTemplate(context, transformer);

    if (!options.isIncludedBom()) {
      workbook.removeSheetAt(workbook.getSheetIndex("bom"));
    }
    if (!options.isDetailedUnitPrice()) {
      workbook.removeSheetAt(workbook.getSheetIndex("prices"));
    }
    for (Sheet sheet : workbook) {
      sheet.protectSheet(data.getCode().getValue());
    }
    @Cleanup
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    workbook.write(baos);
    return ContentInputStream.builder()
      .name(
        String.format("%s_%s_%s.%s",
          data.getCode().getValue(),
          data.getName(),
          DateTimeFormatter.ofPattern("yyyyMMdd").format(LocalDate.now()),
          "xlsx"
        )
      )
      .contentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
      .contentLength(baos.size())
      .inputStream(new ByteArrayInputStream(baos.toByteArray()))
      .build();
  }

}
