GRANT ALL PRIVILEGES ON DATABASE docker TO docker;

---------------------------------------------------------------------------------------------------
create table id_generator (
  id bigint not null
  constraint id_generator_pk primary key,
  class_name varchar(32) not null,
  id_range bigint not null
);
alter table id_generator owner to docker;
create unique index id_generator_class_name_uindex on id_generator (class_name);

---------------------------------------------------------------------------------------------------
create table account_login_data (
  id bigint not null
  constraint account_login_data_pk primary key,
  login varchar(32) not null,
  password char(64) not null,
  active boolean default true not null,
  confirmed boolean default false not null,
  version bigint default 1 not null
);
alter table account_login_data owner to docker;
create unique index account_login_data_login_uindex on account_login_data (login);

---------------------------------------------------------------------------------------------------
create table account_data (
  id bigint not null
  constraint account_data_pk primary key
  constraint account_data_account_login_data_id_fk
  references account_login_data,
  firstname varchar(32) not null,
  lastname varchar(32) not null,
  email varchar(32) not null,
  veryfication_token char(32)
);
alter table account_data owner to docker;
create unique index account_data_email_uindex on account_data (email);
create unique index account_data_veryfication_token_uindex on account_data (veryfication_token);

---------------------------------------------------------------------------------------------------
create table access_level (
    id        bigint      not null
    constraint access_level_pk primary key,
    role_name varchar(16) not null
);

alter table access_level owner to docker;
create unique index access_level_id_uindex on access_level (id);
create unique index access_level_role_name_uindex on access_level (role_name);

create table account_access_level_mapping (
    id              bigint not null
    constraint account_access_level_mapping_pk primary key,
    account_id      bigint not null
    constraint account_id_fk REFERENCES account_login_data,
    access_level_id bigint not null
    constraint access_level_id_fk REFERENCES access_level
);

alter table account_access_level_mapping owner to docker;
-- create unique index account_access_level_mapping_id_uindex on account_access_level_mapping (id);
create unique index unique_account_id_access_level_id on account_access_level_mapping (account_id, access_level_id);

---------------------------------------------------------------------------------------------------
create table forgot_password_token (
  id bigint not null
  constraint forgot_password_token_pk primary key,
  expire_date timestamp with time zone not null,
  hash char(64) not null,
  account_id bigint not null
  constraint forgot_password_token_account_login_data_id_fk references account_login_data,
  version bigint default 1 not null
);
alter table forgot_password_token owner to docker;
create unique index forgot_password_token_account_id_uindex on forgot_password_token (account_id);
create unique index forgot_password_token_hash_uindex on forgot_password_token (hash);

---------------------------------------------------------------------------------------------------


---------------------------------------------------------------------------------------------------
---------------------------------------------------------------------------------------------------
---------------------------------------------------------------------------------------------------
---------------------------------------------------------------------------------------------------
insert into id_generator values (1,'account_login_data',50);
insert into id_generator values (2,'access_level',50);
insert into id_generator values (3,'forgot_password_token',50);