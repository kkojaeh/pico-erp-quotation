package pico.erp.quotation.core;

import java.util.Optional;
import java.util.stream.Stream;
import javax.validation.constraints.NotNull;
import pico.erp.item.data.ItemId;
import pico.erp.quotation.data.QuotationId;
import pico.erp.quotation.data.QuotationItemId;
import pico.erp.quotation.domain.QuotationItem;

public interface QuotationItemRepository {

  QuotationItem create(@NotNull QuotationItem quotationItem);

  void deleteBy(@NotNull QuotationItemId id);

  boolean exists(@NotNull QuotationItemId id);

  Stream<QuotationItem> findAllBy(@NotNull QuotationId quotationId);

  Stream<QuotationItem> findAllBy(@NotNull ItemId itemId);

  Optional<QuotationItem> findBy(@NotNull QuotationItemId id);

  void update(@NotNull QuotationItem quotationItem);

}
