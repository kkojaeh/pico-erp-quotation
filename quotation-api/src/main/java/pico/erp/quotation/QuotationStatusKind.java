package pico.erp.quotation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import pico.erp.shared.data.LocalizedNameable;

@AllArgsConstructor
public enum QuotationStatusKind implements LocalizedNameable {

  /**
   * 주문 진행 후 만료
   */
  EXPIRED(false, false, false, false, null),

  /**
   * 재견적을 통해 파기된 상태
   */
  DESTROYED(false, false, false, false, null),

  /**
   * 취소
   */
  CANCELED(false, false, false, false, null),

  /**
   * 작성중
   */
  DRAFT(true, false, true, false, null),

  /**
   * 제출 준비 완료
   */
  PREPARED(false, true, true, false, null),

  /**
   * 견적제출
   */
  COMMITTED(false, false, true, true, QuotationStatusKind.CANCELED),

  /**
   * 협상중(재견적중)
   */
  IN_NEGOTIATION(false, true, true, false, null),

  /**
   * 주문 진행
   */
  IN_PROCEED(false, false, false, true, QuotationStatusKind.EXPIRED);


  @Getter
  private final boolean preparable;

  @Getter
  private final boolean committable;

  @Getter
  private final boolean cancelable;

  @Getter
  private final boolean expirable;

  @Getter
  private final QuotationStatusKind expiredStatus;

}
