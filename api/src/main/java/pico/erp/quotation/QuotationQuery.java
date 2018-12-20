package pico.erp.quotation;

import java.util.List;
import javax.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pico.erp.quotation.QuotationStatusCountPerMonthAggregateView.Filter;
import pico.erp.shared.data.LabeledValuable;

public interface QuotationQuery {

  List<QuotationStatusCountPerMonthAggregateView> aggregateCountStatusPerMonth(
    Filter options);

  List<? extends LabeledValuable> asLabels(@NotNull String keyword, long limit);

  Page<QuotationView> retrieve(@NotNull QuotationView.Filter filter,
    @NotNull Pageable pageable);

}
