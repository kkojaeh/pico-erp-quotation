create table qot_quotation (
	id binary(16) not null,
	attachment_id binary(16),
	canceler_id varchar(50),
	canceler_name varchar(50),
	code varchar(20),
	comment_subject_id varchar(50),
	committable bit not null,
	committed_date datetime,
	committer_id varchar(50),
	committer_name varchar(50),
	created_by_id varchar(50),
	created_by_name varchar(50),
	created_date datetime,
	customer_id varchar(50),
	customer_name varchar(50),
	expiration_date datetime,
	expiry_policy varchar(20),
	last_modified_by_id varchar(50),
	last_modified_by_name varchar(50),
	last_modified_date datetime,
	manager_id varchar(50),
	manager_name varchar(50),
	name varchar(50),
	preparable bit not null,
	previous_id binary(16),
	project_id binary(16),
	project_name varchar(50),
	protected_description varchar(200),
	public_description varchar(200),
	revision integer,
	status varchar(20),
	total_addition_amount decimal(19,2),
	total_amount decimal(19,2),
	total_item_amount decimal(19,2),
	total_item_discounted_amount decimal(19,2),
	total_item_discounted_rate decimal(7,5),
	total_item_original_amount decimal(19,2),
	primary key (id)
) engine=InnoDB;

create table qot_quotation_addition (
	id binary(16) not null,
	created_by_id varchar(50),
	created_by_name varchar(50),
	created_date datetime,
	description varchar(200),
	name varchar(50),
	quantity decimal(19,5),
	quotation_id binary(16),
	remark varchar(50),
	unit_price decimal(19,2),
	primary key (id)
) engine=InnoDB;

create table qot_quotation_item (
	id binary(16) not null,
	additional_rate decimal(7,5),
	created_by_id varchar(50),
	created_by_name varchar(50),
	created_date datetime,
	description varchar(200),
	direct_labor_unit_price decimal(19,2),
	direct_material_unit_price decimal(19,2),
	discount_rate decimal(7,5),
	discounted_amount decimal(19,2),
	discounted_unit_price decimal(19,2),
	finalized_amount decimal(19,2),
	finalized_unit_price decimal(19,2),
	indirect_expenses_unit_price decimal(19,2),
	indirect_labor_unit_price decimal(19,2),
	indirect_material_unit_price decimal(19,2),
	item_id binary(16),
	original_amount decimal(19,2),
	original_unit_price decimal(19,2),
	quantity decimal(19,2),
	quotation_id binary(16),
	remark varchar(50),
	unit_price_manually_fixed bit not null,
	primary key (id)
) engine=InnoDB;

create table qot_quotation_item_addition (
	id binary(16) not null,
	additional_rate decimal(7,5),
	created_by_id varchar(50),
	created_by_name varchar(50),
	created_date datetime,
	description varchar(200),
	name varchar(50),
	quotation_id binary(16),
	remark varchar(50),
	primary key (id)
) engine=InnoDB;

create index IDX3fhj6mxk8xjl8dcrjpil5rhhr
	on qot_quotation (status);

create index IDX5bmtiq79erjay6mup1d3i9rcv
	on qot_quotation_addition (quotation_id);

create index IDXbpi3qrai8cja8rywn212qbqag
	on qot_quotation_item (quotation_id);

create index IDXfhk4ly2r3dg1sqdg4ge5xmvws
	on qot_quotation_item_addition (quotation_id);
