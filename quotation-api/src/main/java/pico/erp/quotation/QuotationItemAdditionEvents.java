package pico.erp.quotation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pico.erp.quotation.data.QuotationItemAdditionId;
import pico.erp.shared.event.Event;

public interface QuotationItemAdditionEvents {

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  class CreatedEvent implements Event {

    public final static String CHANNEL = "event.quotation-item-addition.created";

    private QuotationItemAdditionId quotationItemAdditionId;

    public String channel() {
      return CHANNEL;
    }

  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  class UpdatedEvent implements Event {

    public final static String CHANNEL = "event.quotation-item-addition.updated";

    private QuotationItemAdditionId quotationItemAdditionId;

    public String channel() {
      return CHANNEL;
    }

  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  class DeletedEvent implements Event {

    public final static String CHANNEL = "event.quotation-item-addition.deleted";

    private QuotationItemAdditionId quotationItemAdditionId;

    public String channel() {
      return CHANNEL;
    }

  }

}
