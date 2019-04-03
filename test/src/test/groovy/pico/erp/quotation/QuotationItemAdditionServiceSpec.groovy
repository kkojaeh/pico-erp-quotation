package pico.erp.quotation

import kkojaeh.spring.boot.component.SpringBootTestComponent
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.Rollback
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional
import pico.erp.item.ItemId
import pico.erp.quotation.item.QuotationItemId
import pico.erp.quotation.item.QuotationItemRequests
import pico.erp.quotation.item.QuotationItemService
import pico.erp.quotation.item.addition.QuotationItemAdditionId
import pico.erp.quotation.item.addition.QuotationItemAdditionRequests
import pico.erp.quotation.item.addition.QuotationItemAdditionService
import pico.erp.shared.ComponentDefinitionServiceLoaderTestComponentSiblingsSupplier
import pico.erp.shared.TestParentApplication
import spock.lang.Specification

@SpringBootTest(classes = [QuotationApplication, TestConfig])
@SpringBootTestComponent(parent = TestParentApplication, siblingsSupplier = ComponentDefinitionServiceLoaderTestComponentSiblingsSupplier.class)
@Transactional
@Rollback
@ActiveProfiles("test")
class QuotationItemAdditionServiceSpec extends Specification {

  @Autowired
  QuotationService quotationService

  @Autowired
  QuotationItemService quotationItemService

  @Autowired
  QuotationItemAdditionService quotationItemAdditionService

  def quotationId = QuotationId.from("quotation-test")

  def quotationItemId = QuotationItemId.from("quotation-item-1")

  def setup() {
    quotationItemService.create(
      new QuotationItemRequests.CreateRequest(
        id: quotationItemId,
        quotationId: quotationId,
        itemId: ItemId.from("item-1"),
        quantity: 1,
        discountRate: 0.15,
        description: "BOM 품목 설명",
        remark: "존재하지 않는 BOM"
      )
    )
  }


  def "견적에 추가 단가 적용 항목을 추가하여 금액 확인"() {
    when:

    quotationItemAdditionService.create(
      new QuotationItemAdditionRequests.CreateRequest(
        id: QuotationItemAdditionId.from("quotation-item-addition-1"),
        quotationId: quotationId,
        name: "이윤 적용율(7%)",
        additionalRate: 0.07
      )
    )
    quotationItemAdditionService.create(
      new QuotationItemAdditionRequests.CreateRequest(
        id: QuotationItemAdditionId.from("quotation-item-addition-2"),
        quotationId: quotationId,
        name: "일반 관리비(9%)",
        additionalRate: 0.09
      )
    )
    def quotation = quotationService.get(quotationId)
    def item = quotationItemService.get(quotationItemId)

    then:

    quotation.totalItemAmount == ((item.originalAmount - (item.originalAmount * 0.15)) * 1.16).setScale(2, BigDecimal.ROUND_HALF_UP)

  }

}
