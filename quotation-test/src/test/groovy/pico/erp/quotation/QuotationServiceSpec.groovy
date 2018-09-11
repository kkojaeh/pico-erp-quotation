package pico.erp.quotation

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.test.annotation.Rollback
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional
import pico.erp.company.data.CompanyId
import pico.erp.project.data.ProjectId
import pico.erp.quotation.data.QuotationExpiryPolicyKind
import pico.erp.quotation.data.QuotationId
import pico.erp.quotation.data.QuotationStatusKind
import pico.erp.quotation.item.QuotationItemService
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

}
