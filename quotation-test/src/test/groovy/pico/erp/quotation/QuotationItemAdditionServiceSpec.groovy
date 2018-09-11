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
import pico.erp.quotation.item.QuotationItemRequests
import pico.erp.quotation.item.QuotationItemService
import pico.erp.quotation.item.addition.QuotationItemAdditionRequests
import pico.erp.quotation.item.addition.QuotationItemAdditionService
import pico.erp.quotation.item.addition.data.QuotationItemAdditionId
import pico.erp.quotation.item.data.QuotationItemId
import pico.erp.shared.IntegrationConfiguration
import pico.erp.user.data.UserId
import spock.lang.Specification

@SpringBootTest(classes = [IntegrationConfiguration])
@Transactional
@Rollback
@ActiveProfiles("test")
@Configuration
@ComponentScan("pico.erp.config")
class QuotationItemAdditionServiceSpec extends Specification {

  @Autowired
  QuotationService quotationService

  @Autowired
  QuotationItemService quotationItemService

  @Autowired
  QuotationItemAdditionService quotationItemAdditionService

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


  def "견적에 추가 단가 적용 항목을 추가하여 금액 확인"() {
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
    def item = quotationItemService.get(QuotationItemId.from("quotation-item-1"))

    then:

    quotation.totalItemAmount == ((item.originalAmount - (item.originalAmount * 0.15)) * 1.16).setScale(2, BigDecimal.ROUND_HALF_UP)

  }

}
