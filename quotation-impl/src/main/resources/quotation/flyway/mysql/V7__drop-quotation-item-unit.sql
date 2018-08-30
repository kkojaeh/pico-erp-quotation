ALTER TABLE qot_quotation_item DROP unit;

ALTER TABLE qot_quotation_item DROP name;

ALTER TABLE qot_quotation_item_addition_rate CHANGE rate additional_rate decimal(7,5);

rename table qot_quotation_item_addition_rate to qot_quotation_item_addition;

ALTER TABLE qot_quotation ADD total_item_original_amount decimal(19,2) NULL;

ALTER TABLE qot_quotation_item DROP unit_price_additional;

ALTER TABLE qot_quotation_item CHANGE unit_price_additional_rate additional_rate decimal(7,5);
ALTER TABLE qot_quotation_item CHANGE unit_price_direct_labor direct_labor_unit_price decimal(19,2);
ALTER TABLE qot_quotation_item CHANGE unit_price_direct_material direct_material_unit_price decimal(19,2);
ALTER TABLE qot_quotation_item CHANGE unit_price_discount_rate discount_rate decimal(7,5);
ALTER TABLE qot_quotation_item CHANGE unit_price_indirect_expenses indirect_expenses_unit_price decimal(19,2);
ALTER TABLE qot_quotation_item CHANGE unit_price_indirect_labor indirect_labor_unit_price decimal(19,2);
ALTER TABLE qot_quotation_item CHANGE unit_price_indirect_material indirect_material_unit_price decimal(19,2);
ALTER TABLE qot_quotation_item CHANGE unit_price_original original_unit_price decimal(19,2);

ALTER TABLE qot_quotation_item ADD unit_price_manually_fixed bit DEFAULT 0 NOT NULL;

ALTER TABLE qot_quotation ADD committable bit DEFAULT 0 NOT NULL;
