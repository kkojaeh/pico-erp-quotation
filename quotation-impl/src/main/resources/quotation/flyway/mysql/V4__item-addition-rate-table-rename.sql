ALTER TABLE qot_quotation_item_addition CHANGE unit_price_rate rate decimal(7,5);

ALTER TABLE qot_quotation_item_addition DROP unit_price;

rename table qot_quotation_item_addition to qot_quotation_item_addition_rate;
