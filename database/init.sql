GRANT ALL PRIVILEGES ON DATABASE multistore TO postgres;
---------------------------------------------------------------------------------------------------
create table access_level
(
    id        bigint      not null
        constraint access_level_pkey
            primary key,
    role_name varchar(16) not null
        constraint unique_role_name_access_level
            unique
);

alter table access_level
    owner to postgres;

create table basket
(
    id      bigint not null
        constraint basket_pkey
            primary key,
    version bigint not null
);

alter table basket
    owner to postgres;

create table account_data
(
    id                     bigint       not null
        constraint account_data_pkey
            primary key,
    active                 boolean      not null,
    email                  varchar(32)  not null
        constraint unique_email_account_data
            unique,
    first_name             varchar(32)  not null,
    last_name              varchar(32)  not null,
    provider               varchar(255) not null,
    provider_id            varchar(255),
    version                bigint       not null,
    authentication_data_id bigint,
    basket_id              bigint
        constraint fk_basket_id_account_data
            references basket
);

alter table account_data
    owner to postgres;

create table category
(
    id            bigint      not null
        constraint category_pkey
            primary key,
    category_name varchar(16) not null
        constraint unique_category_name_category
            unique,
    version       bigint      not null
);

alter table category
    owner to postgres;

create table forgot_password_token
(
    id          bigint      not null
        constraint forgot_password_token_pkey
            primary key,
    expire_date timestamp   not null,
    hash        varchar(64) not null
        constraint unique_hash_forgot_password_token
            unique,
    version     bigint      not null,
    account_id  bigint      not null
        constraint unique_account_id_forgot_password_token
            unique
        constraint account_id_references_to_account_data
            references account_data
);

alter table forgot_password_token
    owner to postgres;

create table authentication_data
(
    id                       bigint       not null
        constraint authentication_data_pkey
            primary key,
    email_verified           boolean      not null,
    password                 varchar(60)  not null,
    username                 varchar(32)  not null
        constraint unique_username_authentication_data
            unique,
    version                  bigint       not null,
    veryfication_token       varchar(255) not null,
    forgot_password_token_id bigint
        constraint forgot_password_token_id_references_to_forgot_password_token_authentication_data
            references forgot_password_token,
    constraint unique_veryfication_token_username_authentication_data
        unique (veryfication_token, username)
);

alter table authentication_data
    owner to postgres;

alter table account_data
    add constraint fk_authetication_data_id_account_data
        foreign key (authentication_data_id) references authentication_data;

create table id_generator
(
    class_name varchar(255) not null
        constraint id_generator_pkey
            primary key,
    id_range   bigint
);

alter table id_generator
    owner to postgres;

create table product
(
    id          bigint           not null
        constraint product_pkey
            primary key,
    active      boolean          not null,
    description varchar(512)     not null,
    in_store    integer          not null,
    price       double precision not null,
    title       varchar(64)      not null
        constraint unique_title_product
                unique,
    type        varchar(255)     not null,
    version     bigint           not null,
    category_id bigint           not null
        constraint category_id_references_to_category_product
            references category
);

alter table product
    owner to postgres;

create table ordered_item
(
    id             bigint  not null
        constraint ordered_item_pkey
            primary key,
    identifier     varchar(36)  not null
        constraint unique_identifier_ordered_item
            unique,
    ordered_number integer not null,
    version        bigint  not null,
    product_id     bigint  not null
        constraint product_id_references_to_product_ordered_item
            references product
);

alter table ordered_item
    owner to postgres;

create table promotion
(
    id          bigint           not null
        constraint promotion_pkey
            primary key,
    active      boolean          not null,
    discount    double precision not null,
    name        varchar(32)      not null
        constraint unique_name_promotion
            unique,
    version     bigint           not null,
    category_id bigint           not null
        constraint category_id_references_to_category_promotion
            references category
);

alter table promotion
    owner to postgres;

create table status
(
    id          bigint      not null
        constraint status_pkey
            primary key,
    status_name varchar(16) not null
        constraint unique_status_name_status
            unique
);

alter table status
    owner to postgres;

create table "order"
(
    id          bigint           not null
        constraint order_pkey
            primary key,
    identifier     varchar(36)  not null
        constraint unique_identifier_order
            unique,
    order_date  timestamp        not null,
    total_price double precision not null,
    version     bigint           not null,
    account_id  bigint           not null
        constraint account_id_references_to_account_data_order
            references account_data,
    status_id   bigint           not null
        constraint status_id_references_to_status_order
            references status,
        address     varchar(64)  not null
    );

alter table "order"
    owner to postgres;

create table account_access_level_mapping
(
    account_id      bigint not null
        constraint account_id_references_to_account_data_account_access_level_mapping
            references account_data,
    access_level_id bigint not null
        constraint access_level_id_references_to_access_level_account_access_level_mapping
            references access_level,
    constraint account_access_level_mapping_pkey
        primary key (account_id, access_level_id)
);

alter table account_access_level_mapping
    owner to postgres;

create table ordered_item_basket_mapping
(
    basket_id        bigint not null
        constraint basket_id_references_to_basket_ordered_item_basket_mapping
            references basket,
    ordered_item_id bigint not null
        constraint ordered_item_id_references_to_ordered_item_ordered_item_basket_mapping
            references ordered_item,
    constraint ordered_item_basket_mapping_pkey
        primary key (basket_id, ordered_item_id)
);

alter table ordered_item_basket_mapping
    owner to postgres;

create table ordered_item_order_mapping
(
    order_id         bigint not null
        constraint fksksskxntlobxy8d6yyh820yxg
            references "order",
    ordered_item_id bigint not null
        constraint ordered_item_id_references_to_ordered_item_ordered_item_order_mapping
            references ordered_item,
    constraint ordered_item_order_mapping_pkey
        primary key (order_id, ordered_item_id)
);

alter table ordered_item_order_mapping
    owner to postgres;





-- USERS AND PRIVILEGES
CREATE USER mok WITH PASSWORD 'mok123';
GRANT SELECT, UPDATE ON id_generator TO mok;
GRANT SELECT, INSERT, UPDATE, DELETE ON forgot_password_token TO mok;
GRANT SELECT, INSERT, UPDATE, DELETE ON authentication_data TO mok;
GRANT SELECT, INSERT, UPDATE, DELETE ON account_data TO mok;
GRANT SELECT ON access_level TO mok;
GRANT SELECT, INSERT, UPDATE, DELETE ON account_access_level_mapping TO mok;
GRANT SELECT, INSERT, DELETE ON basket TO mok;
GRANT SELECT ON ordered_item_basket_mapping TO mok;
GRANT SELECT ON ordered_item to mok;
GRANT SELECT ON product to mok;
GRANT SELECT ON category to mok;

CREATE USER mop WITH PASSWORD 'mop123';
GRANT SELECT, UPDATE ON id_generator TO mop;
GRANT SELECT, UPDATE ON category TO mop;
GRANT SELECT, INSERT, UPDATE ON product TO mop;
GRANT SELECT, INSERT, UPDATE, DELETE ON promotion TO mop;


CREATE USER moz WITH PASSWORD 'moz123';
GRANT SELECT, UPDATE ON id_generator TO moz;
GRANT SELECT, UPDATE ON authentication_data TO moz;
GRANT SELECT, UPDATE ON account_data TO moz;
GRANT SELECT ON access_level TO moz;
GRANT SELECT ON account_access_level_mapping TO moz;
GRANT SELECT ON status TO moz;
GRANT SELECT, INSERT, UPDATE ON "order" TO moz;
GRANT SELECT, INSERT, UPDATE, DELETE ON ordered_item_order_mapping TO moz;
GRANT SELECT, INSERT, UPDATE ON ordered_item TO moz;
GRANT SELECT, INSERT, UPDATE, DELETE ON ordered_item_basket_mapping TO moz;
GRANT SELECT, UPDATE ON product TO moz;
GRANT SELECT ON promotion to moz;
GRANT SELECT ON category TO moz;
GRANT SELECT, INSERT, UPDATE ON basket TO moz;



INSERT INTO id_generator VALUES ('account_data',100);
INSERT INTO id_generator VALUES ('authentication_data',100);
INSERT INTO id_generator VALUES ('access_level',50);
INSERT INTO id_generator VALUES ('forgot_password_token',50);

INSERT INTO id_generator VALUES ('product',100);
INSERT INTO id_generator VALUES ('category',50);
INSERT INTO id_generator VALUES ('promotion',50);
INSERT INTO id_generator VALUES ('order',50);
INSERT INTO id_generator VALUES ('status',50);
INSERT INTO id_generator VALUES ('basket',100);
INSERT INTO id_generator VALUES ('ordered_item',100);

INSERT INTO access_level VALUES(3, 'ROLE_CLIENT');
INSERT INTO access_level VALUES(2, 'ROLE_EMPLOYEE');
INSERT INTO access_level VALUES(1, 'ROLE_ADMIN');

INSERT INTO category VALUES(1, 'fantasy', 0);
INSERT INTO category VALUES(2, 'action', 0);
INSERT INTO category VALUES(3, 'adventure', 0);
INSERT INTO category VALUES(4, 'history', 0);
INSERT INTO category VALUES(5, 'science', 0);
INSERT INTO category VALUES(6, 'fiction', 0);
INSERT INTO category VALUES(7, 'detective', 0);
INSERT INTO category VALUES(8, 'document', 0);
INSERT INTO category VALUES(9, 'novel', 0);

INSERT INTO status VALUES(1, 'submitted');
INSERT INTO status VALUES(2, 'prepared');
INSERT INTO status VALUES(3, 'send');
INSERT INTO status VALUES(4, 'delivered');



INSERT INTO authentication_data (id, email_verified, password, username, version, veryfication_token, forgot_password_token_id)
VALUES (1, true, '$2a$10$DzKdlc8z6OB.woJOkmZsIeO9P6SWxOltsnVoWGurzNrXlTyS45kS6', 'admin', 0, 'ec45e9a5-ea8d-40ca-9ee3-e382dd9e5dd4', null);
INSERT INTO basket (id, version) VALUES (1, 0);
INSERT INTO account_data (id, active, email, first_name, last_name, provider, provider_id, version, authentication_data_id, basket_id)
VALUES (1, true, 'jKowalski@gmail.com', 'Jan', 'Kowalski', 'system', null, 0, 1, 1);
INSERT INTO account_access_level_mapping (account_id, access_level_id)
VALUES (1, 1);

INSERT INTO authentication_data (id, email_verified, password, username, version, veryfication_token, forgot_password_token_id)
VALUES (2, true, '$2a$10$DzKdlc8z6OB.woJOkmZsIeO9P6SWxOltsnVoWGurzNrXlTyS45kS6', 'employee', 0, 'ec45aaa5-ae5t-35yt-0lzq-e382dd9e5dd4', null);
INSERT INTO basket (id, version) VALUES (2, 0);
INSERT INTO account_data (id, active, email, first_name, last_name, provider, provider_id, version, authentication_data_id, basket_id)
VALUES (2, true, 'stanislaw.nowak@gmail.com', 'Stanislaw', 'Nowak', 'system', null, 0, 2, 2);
INSERT INTO account_access_level_mapping (account_id, access_level_id)
VALUES (2, 2);

INSERT INTO authentication_data (id, email_verified, password, username, version, veryfication_token, forgot_password_token_id)
VALUES (3, true, '$2a$10$DzKdlc8z6OB.woJOkmZsIeO9P6SWxOltsnVoWGurzNrXlTyS45kS6', 'client', 0, 'ty43aaa5-rf3g-35yt-66cv-e382dd9e5dd4', null);
INSERT INTO basket (id, version) VALUES (3, 0);
INSERT INTO account_data (id, active, email, first_name, last_name, provider, provider_id, version, authentication_data_id, basket_id)
VALUES (3, true, 'zygmunt.august@gmail.com', 'Zygmunt', 'August', 'system', null, 0, 3, 3);
INSERT INTO account_access_level_mapping (account_id, access_level_id)
VALUES (3, 3);


INSERT INTO product (id, active, description, in_store, price, title, type, version, category_id)
VALUES (1, true, 'Niesamowita historia niszczyciela światów.', 100, 54.44, 'Imperium ciszy', 'book', 0, 6);
INSERT INTO product (id, active, description, in_store, price, title, type, version, category_id)
VALUES (2, true, 'O podróży po skarb.', 50, 34.44, 'Hobbit', 'book', 0, 1);
INSERT INTO product (id, active, description, in_store, price, title, type, version, category_id)
VALUES (3, true, 'Historia tajnego agenta, który cierpni na amnezję i próbuje poznać swoją tożsamość.', 75, 20.0, 'Tożsamość Bourne', 'movie', 0, 2);

INSERT INTO ordered_item (id, identifier,ordered_number, version, product_id)
VALUES (1, 'ec45e9a5-1234-40ca-aaaa-e382dd9e5dd4', 3, 0, 2);
INSERT INTO ordered_item (id, identifier,ordered_number, version, product_id)
VALUES (2, 'ec45e9a5-8907-40ca-ffff-e382dd9e5dd4', 1, 0, 3);


INSERT INTO ordered_item_basket_mapping (basket_id, ordered_item_id)
VALUES (3, 1);
INSERT INTO ordered_item_basket_mapping (basket_id, ordered_item_id)
VALUES (3, 2);