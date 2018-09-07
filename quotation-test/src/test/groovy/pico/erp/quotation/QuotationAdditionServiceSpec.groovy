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
import pico.erp.quotation.data.QuotationAdditionId
import pico.erp.quotation.data.QuotationExpiryPolicyKind
import pico.erp.quotation.data.QuotationId
import pico.erp.quotation.data.QuotationItemId
import pico.erp.shared.IntegrationConfiguration
import pico.erp.user.data.UserId
import spock.lang.Specification

@SpringBootTest(classes = [IntegrationConfiguration])
@Transactional
@Rollback
@ActiveProfiles("test")
@Configuration
@ComponentScan("pico.erp.config")
class QuotationAdditionServiceSpec extends Specification {

  @Autowired
  QuotationService quotationService

  @Autowired
  QuotationItemService quotationItemService

  @Autowired
  QuotationAdditionService quotationAdditionService

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


  def "견적에 추가 금액을 추가하여 금액 확인"() {
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
    quotationAdditionService.create(
      new QuotationAdditionRequests.CreateRequest(
        id: QuotationAdditionId.from("quotation-addition-1"),
        quotationId: quotationId,
        name: "기초비",
        description: "아웃박스 수지판비",
        quantity: 2,
        unitPrice: 120000
      )
    )
    quotationAdditionService.create(
      new QuotationAdditionRequests.CreateRequest(
        id: QuotationAdditionId.from("quotation-addition-2"),
        quotationId: quotationId,
        name: "기초비",
        description: "RRP 목형",
        quantity: 1,
        unitPrice: 150000
      )
    )
    def quotation = quotationService.get(quotationId)

    then:

    quotation.totalAdditionAmount == (120000 * 2) + (150000 * 1)

  }

}