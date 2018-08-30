create table qot_quotation (
	id varchar(50) not null,
	attachment_id varchar(50),
	canceler_id varchar(50),
	canceler_name varchar(50),
	code varchar(20),
	comment_subject_id varchar(50),
	committed_date datetime,
	committer_id varchar(50),
	committer_name varchar(50),
	created_by_id varchar(50),
	created_by_name varchar(50),
	created_date datetime,
	customer_id varchar(50),
	customer_name varchar(50),
	expiry_policy varchar(20),
	last_modified_by_id varchar(50),
	last_modified_by_name varchar(50),
	last_modified_date datetime,
	manager_id varchar(50),
	manager_name varchar(50),
	name varchar(50),
	previous_id varchar(50),
	project_id varchar(50),
	project_name varchar(50),
	protected_description varchar(200),
	public_description varchar(200),
	revision integer,
	status varchar(20),
	total_addition_amount decimal(19,2),
	total_amount decimal(19,2),
	total_item_amount decimal(19,2),
	total_item_discount_amount decimal(19,2),
	total_item_discount_rate decimal(7,5),
	primary key (id)
) engine=InnoDB;

create table qot_quotation_addition (
	id varchar(50) not null,
	description varchar(200),
	name varchar(50),
	quantity decimal(19,5),
	remark varchar(50),
	unit_price decimal(19,2),
	quotation_id varchar(50),
	primary key (id)
) engine=InnoDB;

create table qot_quotation_item (
	type varchar(31) not null,
	id varchar(50) not null,
	description varchar(200),
	discounted_amount decimal(19,2),
	finalized_amount decimal(19,2),
	name varchar(50),
	original_amount decimal(19,2),
	quantity decimal(19,2),
	remark varchar(50),
	unit varchar(20),
	unit_price_additional decimal(19,2),
	unit_price_additional_rate decimal(7,5),
	unit_price_direct_labor decimal(19,2),
	unit_price_direct_material decimal(19,2),
	unit_price_discount_rate decimal(7,5),
	unit_price_indirect_expenses decimal(19,2),
	unit_price_indirect_labor decimal(19,2),
	unit_price_indirect_material decimal(19,2),
	unit_price_original decimal(19,2),
	bom_id varchar(50),
	quotation_id varchar(50),
	primary key (id)
) engine=InnoDB;

create table qot_quotation_item_addition (
	type varchar(31) not null,
	id varchar(50) not null,
	description varchar(200),
	name varchar(50),
	remark varchar(50),
	unit_price decimal(19,2),
	unit_price_rate decimal(19,2),
	quotation_id varchar(50),
	primary key (id)
) engine=InnoDB;

create index QOT_QUOTATION_STATUS_IDX
	on qot_quotation (status);

alter table qot_quotation_addition
	add constraint FKxn8vns472mlg95drnflvmgv2 foreign key (quotation_id)
	references qot_quotation (id);

alter table qot_quotation_item
	add constraint FKhh5xb2tmerm8wd4s33xtv57s1 foreign key (quotation_id)
	references qot_quotation (id);

alter table qot_quotation_item_addition
	add constraint FKlsy3fe1onjisvkoat72ectqbi foreign key (quotation_id)
	references qot_quotation (id);
