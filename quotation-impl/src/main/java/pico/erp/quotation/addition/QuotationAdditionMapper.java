package pico.erp.quotation.addition;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import pico.erp.quotation.Quotation;
import pico.erp.quotation.QuotationId;
import pico.erp.quotation.QuotationMapper;

@Mapper
public abstract class QuotationAdditionMapper {

  @Lazy
  @Autowired
  private QuotationMapper quotationMapper;

  @Mappings({
    @Mapping(target = "quotation", source = "quotationId")
  })
  public abstract QuotationAdditionMessages.CreateRequest map(
    QuotationAdditionRequests.CreateRequest request);

  public abstract QuotationAdditionMessages.UpdateRequest map(
    QuotationAdditionRequests.UpdateRequest request);

  public abstract QuotationAdditionMessages.DeleteRequest map(
    QuotationAdditionRequests.DeleteRequest request);

  public abstract QuotationAdditionData map(QuotationAddition addition);

  protected Quotation map(QuotationId quotationId) {
    return quotationMapper.map(quotationId);
  }

}
