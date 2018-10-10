package pico.erp.quotation.item.addition;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import pico.erp.quotation.Quotation;
import pico.erp.quotation.QuotationId;
import pico.erp.quotation.QuotationMapper;

@Mapper
public abstract class QuotationItemAdditionMapper {

  @Lazy
  @Autowired
  QuotationMapper quotationMapper;

  @Mappings({
    @Mapping(target = "quotation", source = "quotationId")
  })
  public abstract QuotationItemAdditionMessages.CreateRequest map(
    QuotationItemAdditionRequests.CreateRequest request);

  public abstract QuotationItemAdditionMessages.UpdateRequest map(
    QuotationItemAdditionRequests.UpdateRequest request);

  public abstract QuotationItemAdditionMessages.DeleteRequest map(
    QuotationItemAdditionRequests.DeleteRequest request);

  public abstract QuotationItemAdditionData map(
    QuotationItemAddition itemAdditionRate);

  protected Quotation map(QuotationId quotationId) {
    return quotationMapper.map(quotationId);
  }

}
