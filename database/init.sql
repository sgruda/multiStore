GRANT ALL PRIVILEGES ON DATABASE docker TO docker;

---------------------------------------------------------------------------------------------------
create table id_generator
(
	id bigint not null
		constraint id_generator_pk
			primary key,
	class_name varchar(32) not null,
	id_range bigint not null
);

alter table id_generator owner to docker;

create unique index id_generator_class_name_uindex
	on id_generator (class_name);

create table access_level
(
	id bigint not null
		constraint access_level_pk
			primary key,
	role_name varchar(16) not null
		constraint ukqlnxh10tkw82tcit5ksy3k1p4
			unique
);

alter table access_level owner to docker;

create unique index access_level_id_uindex
	on access_level (id);

create unique index access_level_role_name_uindex
	on access_level (role_name);

create table account_data
(
	id bigint not null
		constraint account_data_pkey
			primary key,
	email varchar(32) not null,
	firstname varchar(32) not null,
	lastname varchar(32) not null,
	veryfication_token varchar(32),
	constraint uk6s47d2qjycu5ns46lid98vgqp
		unique (email, veryfication_token)
);

alter table account_data owner to docker;

create table account_login_data
(
	active boolean not null,
	confirmed boolean not null,
	password varchar(64) not null,
	username varchar(32) not null
		constraint ukk41jqe0wcdyea4467lwflvwlf
			unique,
	version bigint not null,
	id bigint not null
		constraint account_login_data_pkey
			primary key
		constraint fkkg1nbdr6ag51o9ru05qrxhxul
			references account_data
);

alter table account_login_data owner to docker;

create table forgot_password_token
(
	id bigint not null
		constraint forgot_password_token_pkey
			primary key,
	expire_date timestamp not null,
	hash varchar(64) not null
		constraint uk_gqsdx7jsd43tlmhn0klc1wasg
			unique,
	version bigint not null,
	account_id bigint not null
		constraint uk_cf7via02khkvf4eufblqxcj2j
			unique
		constraint fk26u2y5lrl66ud5s7f2w9du361
			references account_data
);

alter table forgot_password_token owner to docker;

create table account_access_level_mapping
(
	account_id bigint not null
		constraint fkphw47ewtcdrfv28x2lssvqkb4
			references account_data,
	access_level_id bigint not null
		constraint fk4uhckp9doykcndaqlsgg2ltw
			references access_level,
	constraint account_access_level_mapping_pkey
		primary key (account_id, access_level_id)
);

alter table account_access_level_mapping owner to docker;





insert into id_generator values (1,'account_login_data',50);
insert into id_generator values (2,'access_level',50);
insert into id_generator values (3,'forgot_password_token',50);
INSERT INTO access_level VALUES(3, 'ROLE_CLIENT');
INSERT INTO access_level VALUES(2, 'ROLE_EMPLOYEE');
INSERT INTO access_level VALUES(1,'ROLE_ADMIN');