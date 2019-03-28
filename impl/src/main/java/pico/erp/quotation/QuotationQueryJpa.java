package pico.erp.quotation;

import static org.springframework.util.StringUtils.isEmpty;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import kkojaeh.spring.boot.component.Give;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import pico.erp.quotation.QuotationStatusCountPerMonthAggregateView.Filter;
import pico.erp.shared.LabeledValue;
import pico.erp.shared.QExtendedLabeledValue;
import pico.erp.shared.data.LabeledValuable;
import pico.erp.shared.jpa.QueryDslJpaSupport;

@Service
@Give
@Transactional(readOnly = true)
@Validated
public class QuotationQueryJpa implements QuotationQuery {

  private final QQuotationEntity quotation = QQuotationEntity.quotationEntity;

  @PersistenceContext
  private EntityManager entityManager;

  @Autowired
  private QueryDslJpaSupport queryDslJpaSupport;

  public List<QuotationStatusCountPerMonthAggregateView> aggregateCountStatusPerMonth(
    Filter options) {

    val query = new JPAQuery<QuotationStatusCountPerMonthAggregateView>(entityManager);
    val select = Projections
      .bean(QuotationStatusCountPerMonthAggregateView.class,
        quotation.createdDate.yearMonth().as("yearMonth"),
        quotation.status,
        quotation.status.count().as("count")
      );

    query.select(select);
    query.from(quotation);
    query.where(quotation.createdDate.year().eq(options.getYear()));
    query.groupBy(quotation.createdDate.yearMonth(), quotation.status);
    return query.fetch();
  }

  @Override
  public List<? extends LabeledValuable> asLabels(String keyword, long limit) {
    val query = new JPAQuery<LabeledValue>(entityManager);
    val select = new QExtendedLabeledValue(
      quotation.id.value.as("value"),
      quotation.name.as("label"),
      quotation.code.value.as("subLabel"),
      Expressions.as(Expressions.nullExpression(), "stamp")
    );
    query.select(select);
    query.from(quotation);
    query
      .where(quotation.name.likeIgnoreCase(queryDslJpaSupport.toLikeKeyword("%", keyword, "%"))
        .and(quotation.status.notIn(QuotationStatusKind.DESTROYED)));
    query.limit(limit);
    query.orderBy(quotation.createdDate.desc());
    return query.fetch();
  }

  @Override
  public Page<QuotationView> retrieve(QuotationView.Filter filter, Pageable pageable) {
    val query = new JPAQuery<QuotationView>(entityManager);
    val select = Projections.bean(QuotationView.class,
      quotation.id,
      quotation.code,
      quotation.name,
      quotation.revision,
      quotation.projectId,
      quotation.customerId,
      quotation.managerId,
      quotation.committedDate,
      quotation.status,
      quotation.createdBy,
      quotation.createdDate,
      quotation.lastModifiedBy,
      quotation.lastModifiedDate
    );

    query.select(select);
    query.from(quotation);

    val builder = new BooleanBuilder();

    if (!isEmpty(filter.getName())) {
      builder.and(
        quotation.name
          .likeIgnoreCase(queryDslJpaSupport.toLikeKeyword("%", filter.getName(), "%")));
    }
    if (!isEmpty(filter.getCode())) {
      builder.and(
        quotation.code.value
          .likeIgnoreCase(queryDslJpaSupport.toLikeKeyword("%", filter.getCode(), "%")));
    }
    if (filter.getProjectId() != null) {
      builder.and(quotation.projectId.eq(filter.getProjectId()));
    }
    if (filter.getCustomerId() != null) {
      builder.and(quotation.customerId.eq(filter.getCustomerId()));
    }
    if (filter.getManagerId() != null) {
      builder.and(quotation.managerId.eq(filter.getManagerId()));
    }
    if (filter.getStartCreatedDate() != null) {
      builder.and(quotation.createdDate.goe(filter.getStartCreatedDate()));
    }
    if (filter.getEndCreatedDate() != null) {
      builder.and(quotation.createdDate.loe(filter.getEndCreatedDate()));
    }
    if (filter.getStatuses() != null && !filter.getStatuses().isEmpty()) {
      builder.and(quotation.status.in(filter.getStatuses()));
    }
    query.where(builder);
    return queryDslJpaSupport.paging(query, pageable, select);
  }
}
