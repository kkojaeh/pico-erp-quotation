package pico.erp.quotation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pico.erp.quotation.data.QuotationAdditionId;
import pico.erp.shared.event.Event;

public interface QuotationAdditionEvents {

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  class CreatedEvent implements Event {

    public final static String CHANNEL = "event.quotation-addition.created";

    private QuotationAdditionId quotationAdditionId;

    public String channel() {
      return CHANNEL;
    }

  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  class UpdatedEvent implements Event {

    public final static String CHANNEL = "event.quotation-addition.updated";

    private QuotationAdditionId quotationAdditionId;

    public String channel() {
      return CHANNEL;
    }

  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  class DeletedEvent implements Event {

    public final static String CHANNEL = "event.quotation-addition.deleted";

    private QuotationAdditionId quotationAdditionId;

    public String channel() {
      return CHANNEL;
    }

  }

}
