package pico.erp.quotation

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.test.annotation.Rollback
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional
import pico.erp.item.ItemId
import pico.erp.quotation.item.QuotationItemId
import pico.erp.quotation.item.QuotationItemRequests
import pico.erp.quotation.item.QuotationItemService
import pico.erp.shared.IntegrationConfiguration
import spock.lang.Specification

@SpringBootTest(classes = [IntegrationConfiguration])
@Transactional
@Rollback
@ActiveProfiles("test")
@Configuration
@ComponentScan("pico.erp.config")
class QuotationItemServiceSpec extends Specification {

  @Autowired
  QuotationService quotationService

  @Autowired
  QuotationItemService quotationItemService

  def quotationId = QuotationId.from("quotation-test")

  def id1 = QuotationItemId.from("quotation-item-1")

  def id2 = QuotationItemId.from("quotation-item-2")

  def itemId1 = ItemId.from("item-1")

  def itemId2 = ItemId.from("item-2")

  def setup() {

  }

  def addItem1() {
    quotationItemService.create(
      new QuotationItemRequests.CreateRequest(
        id: id1,
        quotationId: quotationId,
        itemId: itemId1,
        quantity: 1,
        discountRate: 0.15,
        description: "BOM 품목 설명",
        remark: "존재하지 않는 BOM"
      )
    )
  }

  def addItem2() {
    quotationItemService.create(
      new QuotationItemRequests.CreateRequest(
        id: id2,
        quotationId: quotationId,
        itemId: itemId2,
        quantity: 2,
        discountRate: 0.20,
        description: "직접 입력 품목 설명2",
        remark: "품목 없이 직접입력함2"
      )
    )
  }

  def fixUnitPrice1() {
    quotationItemService.fixUnitPrice(
      new QuotationItemRequests.FixUnitPriceRequest(
        id: id1,
        originalUnitPrice: 500,
        directLaborUnitPrice: 100,
        indirectLaborUnitPrice: 100,
        directMaterialUnitPrice: 100,
        indirectMaterialUnitPrice: 100,
        indirectExpensesUnitPrice: 100
      )
    )
  }

  def fixUnitPrice2() {
    quotationItemService.fixUnitPrice(
      new QuotationItemRequests.FixUnitPriceRequest(
        id: id2,
        originalUnitPrice: 400,
        directLaborUnitPrice: 100,
        indirectLaborUnitPrice: 100,
        directMaterialUnitPrice: 100,
        indirectMaterialUnitPrice: 50,
        indirectExpensesUnitPrice: 50
      )
    )
  }


  def "조회 - 아이디로 조회"() {
    when:
    addItem1()
    def item = quotationItemService.get(id1)

    then:
    item.id == id1
    item.quotationId == quotationId
    item.itemId == itemId1
    item.quantity == 1
    item.discountRate == 0.15
    item.description == "BOM 품목 설명"
    item.remark == "존재하지 않는 BOM"
  }

  def "조회 - 견적 아이디로 조회"() {
    when:
    addItem1()
    def items = quotationItemService.getAll(quotationId)
    then:
    items.size() == 1
  }

  def "견적에 입력 품목을 추가하여 금액 확인"() {
    when:

    addItem1()
    addItem2()
    fixUnitPrice1()
    fixUnitPrice2()


    def q = quotationService.get(quotationId)
    def totalItemAmount = (500 * 1 * 0.85) + (400 * 2 * 0.8)
    def totalItemOriginalAmount = (500 * 1) + (400 * 2)
    def totalItemDiscountAmount = (500 * 1 * 0.15) + (400 * 2 * 0.2)

    then:

    q.totalItemAmount == totalItemAmount
    q.totalItemDiscountedAmount == totalItemOriginalAmount - totalItemDiscountAmount
    q.totalItemDiscountedRate == (totalItemDiscountAmount / totalItemOriginalAmount).setScale(4, BigDecimal.ROUND_HALF_UP)
  }


  def "자세한 옵션으로 출력"() {
    when:
    def inputStream = quotationService.printSheet(
      new QuotationRequests.PrintSheetRequest(
        id: QuotationId.from("quotation-1"),
        options: new QuotationPrintSheetOptions()
      )
    )
    then:
    inputStream != null
  }

}
