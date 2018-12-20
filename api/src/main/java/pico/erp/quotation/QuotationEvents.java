package pico.erp.quotation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pico.erp.shared.event.Event;

public interface QuotationEvents {

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  class CanceledEvent implements Event {

    public final static String CHANNEL = "event.quotation.canceled";

    private QuotationId quotationId;

    public String channel() {
      return CHANNEL;
    }

  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  class DraftedEvent implements Event {

    public final static String CHANNEL = "event.quotation.drafted";

    private QuotationId quotationId;

    public String channel() {
      return CHANNEL;
    }

  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  class UpdatedEvent implements Event {

    public final static String CHANNEL = "event.quotation.updated";

    private QuotationId quotationId;

    public String channel() {
      return CHANNEL;
    }

  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  class NextDraftedEvent implements Event {

    public final static String CHANNEL = "event.quotation.next-drafted";

    private QuotationId quotationId;

    public String channel() {
      return CHANNEL;
    }

  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  class CommittedEvent implements Event {

    public final static String CHANNEL = "event.quotation.committed";

    private QuotationId quotationId;

    public String channel() {
      return CHANNEL;
    }

  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  class DestroyedEvent implements Event {

    public final static String CHANNEL = "event.quotation.destroyed";

    private QuotationId quotationId;

    public String channel() {
      return CHANNEL;
    }

  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  class ExpiredEvent implements Event {

    public final static String CHANNEL = "event.quotation.expired";

    private QuotationId quotationId;

    public String channel() {
      return CHANNEL;
    }

  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  class PreparedEvent implements Event {

    public final static String CHANNEL = "event.quotation.prepared";

    private QuotationId quotationId;

    public String channel() {
      return CHANNEL;
    }

  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  class MemberChangedEvent implements Event {

    public final static String CHANNEL = "event.quotation.member-changed";

    private QuotationId quotationId;

    public String channel() {
      return CHANNEL;
    }

  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  class AdditionChangedEvent implements Event {

    public final static String CHANNEL = "event.quotation.addition-changed";

    private QuotationId quotationId;

    public String channel() {
      return CHANNEL;
    }

  }
}
