package pico.erp.quotation


import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.test.annotation.Rollback
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional
import pico.erp.company.data.CompanyId
import pico.erp.item.data.ItemId
import pico.erp.project.data.ProjectId
import pico.erp.quotation.data.QuotationExpiryPolicyKind
import pico.erp.quotation.data.QuotationId
import pico.erp.quotation.data.QuotationItemId
import pico.erp.quotation.data.QuotationPrintSheetOptions
import pico.erp.shared.IntegrationConfiguration
import pico.erp.user.data.UserId
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

  def quotationId = QuotationId.from("test")

  def setup() {
    quotationService.draft(new QuotationRequests.DraftRequest(
      id: quotationId,
      name: "테스트 견적",
      expiryPolicy: QuotationExpiryPolicyKind.IN_HALF,
      projectId: ProjectId.from("sample-project1"),
      customerId: CompanyId.from("CUST1"),
      managerId: UserId.from("ysh")
    ))
  }


  def "견적에 BOM 품목을 추가"() {
    when:
    quotationItemService.create(
      new QuotationItemRequests.CreateRequest(
        id: QuotationItemId.from("quotation-item-1"),
        quotationId: quotationId,
        itemId: ItemId.from("item-1"),
        quantity: 1,
        discountRate: 0.15,
        description: "BOM 품목 설명",
        remark: "존재하지 않는 BOM"
      )
    )
    def quotation = quotationService.get(quotationId)
    def items = quotationItemService.getAll(quotationId)
    then:
    items.size() == 1
  }

  def "견적에 입력 품목을 추가하여 금액 확인"() {
    when:
    quotationItemService.create(
      new QuotationItemRequests.CreateRequest(
        id: QuotationItemId.from("quotation-item-1"),
        quotationId: quotationId,
        itemId: ItemId.from("item-1"),
        quantity: 1,
        discountRate: 0.15,
        description: "직접 입력 품목 설명",
        remark: "품목 없이 직접입력함"
      )
    )

    quotationItemService.fixUnitPrice(
      new QuotationItemRequests.FixUnitPriceRequest(
        id: QuotationItemId.from("quotation-item-1"),
        originalUnitPrice: 500,
        directLaborUnitPrice: 100,
        indirectLaborUnitPrice: 100,
        directMaterialUnitPrice: 100,
        indirectMaterialUnitPrice: 100,
        indirectExpensesUnitPrice: 100
      )
    )

    quotationItemService.create(
      new QuotationItemRequests.CreateRequest(
        id: QuotationItemId.from("quotation-item-2"),
        quotationId: quotationId,
        itemId: ItemId.from("item-2"),
        quantity: 2,
        discountRate: 0.20,
        description: "직접 입력 품목 설명2",
        remark: "품목 없이 직접입력함2"
      )
    )

    quotationItemService.fixUnitPrice(
      new QuotationItemRequests.FixUnitPriceRequest(
        id: QuotationItemId.from("quotation-item-2"),
        originalUnitPrice: 400,
        directLaborUnitPrice: 100,
        indirectLaborUnitPrice: 100,
        directMaterialUnitPrice: 100,
        indirectMaterialUnitPrice: 50,
        indirectExpensesUnitPrice: 50
      )
    )
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
