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
import pico.erp.quotation.data.QuotationData
import pico.erp.quotation.data.QuotationExpiryPolicyKind
import pico.erp.quotation.data.QuotationId
import pico.erp.quotation.data.QuotationItemAdditionId
import pico.erp.quotation.data.QuotationItemId
import pico.erp.quotation.data.QuotationStatusKind
import pico.erp.shared.IntegrationConfiguration
import pico.erp.user.data.UserId
import spock.lang.Specification

import java.time.OffsetDateTime

@SpringBootTest(classes = [IntegrationConfiguration])
@Transactional
@Rollback
@ActiveProfiles("test")
@Configuration
@ComponentScan("pico.erp.config")
class QuotationServiceSpec extends Specification {

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

  def "제출 후 만료 시간이 지나면 취소 처리 된다"() {
    when:

    quotationService.prepare(new QuotationRequests.PrepareRequest(id: quotationId))
    quotationService.commit(new QuotationRequests.CommitRequest(id: quotationId))
    quotationService.expire(new QuotationRequests.ExpireRequest(OffsetDateTime.now().plusMonths(6)))
    def q = quotationService.get(quotationId)

    then:
    q.status == QuotationStatusKind.CANCELED
  }

  def "아이디로 존재하는 견적 확인"() {
    when:
    def exists = quotationService.exists(quotationId)

    then:
    exists == true
  }

  def "아이디로 존재하지 않는 견적 확인"() {
    when:
    def exists = quotationService.exists(QuotationId.from("unknown"))

    then:
    exists == false
  }

  def "아이디로 존재하는 견적을 조회"() {
    when:
    def q = quotationService.get(quotationId)

    then:
    q.name == "테스트 견적"
    q.revision == 1
    q.expiryPolicy == QuotationExpiryPolicyKind.IN_HALF
  }

  def "아이디로 존재하지 않는 견적을 조회"() {
    when:
    quotationService.get(QuotationId.from("unknown"))

    then:
    thrown(QuotationExceptions.NotFoundException)
  }



  /*





  def "견적에 부가 금액 항목을 추가하여 금액 확인"() {
    when:
    quotationService.addItem(
      new QuotationService.AddItemRequest(
        id: quotation.id,
        item: new QuotationService.InputItemRequestData(
          id: QuotationItemId.generate(),
          itemId: ItemId.from("item-1"),
          unit: UnitKind.EA,
          name: "직접 입력 품목",
          quantity: 1,
          unitPrice: new QuotationUnitPriceData(
            original: 500,
            directLabor: 100,
            indirectLabor: 100,
            directMaterial: 100,
            indirectMaterial: 100,
            indirectExpenses: 100,
            discountRate: 0.15
          ),
          description: "직접 입력 품목 설명",
          remark: "품목 없이 직접입력함"
        )
      )
    )
    quotationService.addItem(
      new QuotationService.AddItemRequest(
        id: quotation.id,
        item: new QuotationService.InputItemRequestData(
          id: QuotationItemId.generate(),
          itemId: ItemId.from("item-2"),
          name: "직접 입력 품목2",
          unit: UnitKind.EA,
          quantity: 2,
          unitPrice: new QuotationUnitPriceData(
            original: 400,
            directLabor: 100,
            indirectLabor: 100,
            directMaterial: 100,
            indirectMaterial: 50,
            indirectExpenses: 50,
            discountRate: 0.20,
          ),
          description: "직접 입력 품목 설명2",
          remark: "품목 없이 직접입력함2"
        )
      )
    )
    quotationService.addItemAdditionRate(
      new QuotationService.AddItemAdditionRateRequest(
        id: quotation.id,
        itemAdditionRate: new QuotationService.ItemAdditionRateRequestData(
          id: QuotationItemAdditionId.generate(),
          name: "이윤 적용율(7%)",
          rate: 0.07
        )
      )
    )
    quotationService.addItemAdditionRate(
      new QuotationService.AddItemAdditionRateRequest(
        id: quotation.id,
        itemAdditionRate: new QuotationService.ItemAdditionRateRequestData(
          id: QuotationItemAdditionId.generate(),
          name: "일반 관리비(9%)",
          rate: 0.09
        )
      )
    )

    quotationService.addAddition(
      new QuotationService.AddAdditionRequest(
        id: quotation.id,
        addition: new QuotationService.AdditionRequestData(
          id: QuotationAdditionId.generate(),
          name: "기초비",
          description: "아웃박스 수지판비",
          quantity: 2,
          unitPrice: 120000
        )
      )
    )
    quotationService.addAddition(
      new QuotationService.AddAdditionRequest(
        id: quotation.id,
        addition: new QuotationService.AdditionRequestData(
          id: QuotationAdditionId.generate(),
          name: "기초비",
          description: "RRP 목형",
          quantity: 1,
          unitPrice: 150000
        )
      )
    )
    def q = quotationService.get(quotation.id)

    def totalItemAmount = ((500 * 1 * 0.85) + (400 * 2 * 0.8)) * 1.16
    def totalAdditionAmount = (120000 * 2) + (150000 * 1)
    def totalAmount = totalItemAmount + totalAdditionAmount

    then:

    q.totalItemAmount == totalItemAmount
    q.totalAdditionAmount == totalAdditionAmount
    q.totalAmount == totalAmount

  }

  def "자세한 옵션으로 출력"() {
    when:
    def inputStream = quotationService.printSheet(
      new QuotationService.PrintSheetRequest(
        id: QuotationId.from("quotation-1"),
        options: new QuotationPrintSheetOptions()
      )
    )
    then:
    IOUtils.toByteArray(inputStream).length > 0
  }

  */

}
