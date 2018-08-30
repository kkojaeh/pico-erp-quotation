ALTER TABLE qot_quotation_addition ADD created_by_id varchar(50) NULL;
ALTER TABLE qot_quotation_addition ADD created_by_name varchar(50) NULL;
ALTER TABLE qot_quotation_addition ADD created_date datetime;

ALTER TABLE qot_quotation_item ADD created_by_id varchar(50) NULL;
ALTER TABLE qot_quotation_item ADD created_by_name varchar(50) NULL;
ALTER TABLE qot_quotation_item ADD created_date datetime;

ALTER TABLE qot_quotation_item_addition_rate ADD created_by_id varchar(50) NULL;
ALTER TABLE qot_quotation_item_addition_rate ADD created_by_name varchar(50) NULL;
ALTER TABLE qot_quotation_item_addition_rate ADD created_date datetime;
