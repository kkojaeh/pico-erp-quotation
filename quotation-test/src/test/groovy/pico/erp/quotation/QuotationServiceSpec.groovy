package pico.erp.quotation

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.test.annotation.Rollback
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional
import pico.erp.company.CompanyId
import pico.erp.project.ProjectId
import pico.erp.quotation.item.QuotationItemService
import pico.erp.shared.IntegrationConfiguration
import pico.erp.user.UserId
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

  def id = QuotationId.from("test")

  def unknownId = QuotationId.from("unknown")

  def projectId = ProjectId.from("sample-project1")

  def customerId = CompanyId.from("CUST1")

  def managerId = UserId.from("ysh")

  def setup() {
    quotationService.draft(new QuotationRequests.DraftRequest(
      id: id,
      name: "테스트 견적",
      expiryPolicy: QuotationExpiryPolicyKind.IN_HALF,
      projectId: projectId,
      customerId: customerId,
      managerId: managerId
    ))
  }

  def "만료 - 제출 후 만료 시간이 지나면 취소"() {
    when:

    quotationService.prepare(new QuotationRequests.PrepareRequest(id: id))
    quotationService.commit(new QuotationRequests.CommitRequest(id: id))
    quotationService.expire(new QuotationRequests.ExpireRequest(OffsetDateTime.now().plusMonths(6)))
    def q = quotationService.get(id)

    then:
    q.status == QuotationStatusKind.CANCELED
  }

  def "존재 - 아이디로 확인"() {
    when:
    def exists = quotationService.exists(id)

    then:
    exists == true
  }

  def "존재 - 존재하지 않는 아이디로 확인"() {
    when:
    def exists = quotationService.exists(unknownId)

    then:
    exists == false
  }

  def "조회 - 아이디로 조회"() {
    when:
    def q = quotationService.get(id)

    then:
    q.name == "테스트 견적"
    q.revision == 1
    q.expiryPolicy == QuotationExpiryPolicyKind.IN_HALF
    q.projectId == projectId
    q.customerId == customerId
    q.managerId == managerId
    q.name == "테스트 견적"
  }

  def "조회 - 존재하지 않는 아이디로 조회"() {
    when:
    quotationService.get(unknownId)

    then:
    thrown(QuotationExceptions.NotFoundException)
  }

}
