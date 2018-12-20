package pico.erp.quotation;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import pico.erp.quotation.QuotationRequests.CancelRequest;
import pico.erp.quotation.QuotationRequests.CommitRequest;
import pico.erp.quotation.QuotationRequests.DeleteRequest;
import pico.erp.quotation.QuotationRequests.DraftRequest;
import pico.erp.quotation.QuotationRequests.ExpireRequest;
import pico.erp.quotation.QuotationRequests.NextDraftRequest;
import pico.erp.quotation.QuotationRequests.PrepareRequest;
import pico.erp.quotation.QuotationRequests.PrintSheetRequest;
import pico.erp.quotation.QuotationRequests.UpdateRequest;
import pico.erp.shared.data.ContentInputStream;

public interface QuotationService {


  /**
   * 견적 취소
   * <p/>
   * 견적이 취소 되어 더이상 사용하지 않는다
   * <p/>
   * 취소 상태에서는 변경이 불가
   */
  void cancel(@Valid @NotNull CancelRequest request);


  /**
   * 견적 제출.
   * <p/>
   * 견적 제출 상태로 변경되며 제출 상태에서는 변경이 불가
   */
  void commit(@Valid @NotNull CommitRequest request);

  void prepare(@Valid @NotNull PrepareRequest request);

  void delete(@Valid @NotNull DeleteRequest request);

  /**
   * 견적을 작성한다
   */
  QuotationData draft(@Valid @NotNull DraftRequest request);

  /**
   * 견적의 존재여부를 확인한다
   */
  boolean exists(@Valid @NotNull QuotationId id);

  /**
   * 제출 이후 처리 안된 내용을 만료 시킴
   */
  void expire(@Valid @NotNull ExpireRequest request);

  /**
   * 견적을 조회한다
   */
  QuotationData get(@Valid @NotNull QuotationId id);

  /**
   * 기존의 견적의 다음 버전을 작성한다
   * <p/>
   * 기존의 견적 정보가 복사된다
   */
  QuotationData nextDraft(@Valid @NotNull NextDraftRequest request);

  ContentInputStream printSheet(@Valid @NotNull PrintSheetRequest request);

  void update(@Valid @NotNull UpdateRequest request);

}
