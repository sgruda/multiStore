GRANT ALL PRIVILEGES ON DATABASE multistore TO root;
---------------------------------------------------------------------------------------------------
create table access_level
(
    id        bigint      not null
        constraint access_level_pkey
            primary key,
    role_name varchar(16) not null
        constraint ukqlnxh10tkw82tcit5ksy3k1p4
            unique
);

alter table access_level
    owner to root;

create table basket
(
    id      bigint not null
        constraint basket_pkey
            primary key,
    version bigint not null
);

alter table basket
    owner to root;

create table account_data
(
    id                     bigint       not null
        constraint account_data_pkey
            primary key,
    active                 boolean      not null,
    email                  varchar(32)  not null
        constraint uk_6nyd9ykqgjm7n4ngreynnly8t
            unique,
    first_name             varchar(32)  not null,
    last_name              varchar(32)  not null,
    provider               varchar(255) not null,
    provider_id            varchar(255),
    version                bigint       not null,
    authentication_data_id bigint,
    basket_id              bigint
        constraint fkq30gie5po2akplh6ge0boy9p1
            references basket
);

alter table account_data
    owner to root;

create table category
(
    id            bigint      not null
        constraint category_pkey
            primary key,
    category_name varchar(16) not null
        constraint uklroeo5fvfdeg4hpicn4lw7x9b
            unique,
    version       bigint      not null
);

alter table category
    owner to root;

create table forgot_password_token
(
    id          bigint      not null
        constraint forgot_password_token_pkey
            primary key,
    expire_date timestamp   not null,
    hash        varchar(64) not null
        constraint uk_gqsdx7jsd43tlmhn0klc1wasg
            unique,
    version     bigint      not null,
    account_id  bigint      not null
        constraint uk_cf7via02khkvf4eufblqxcj2j
            unique
        constraint fk26u2y5lrl66ud5s7f2w9du361
            references account_data
);

alter table forgot_password_token
    owner to root;

create table authentication_data
(
    id                       bigint       not null
        constraint authentication_data_pkey
            primary key,
    email_verified           boolean      not null,
    password                 varchar(60)  not null,
    username                 varchar(32)  not null
        constraint uk_bbvlbdpgqb81arjatdxvo6e0f
            unique,
    version                  bigint       not null,
    veryfication_token       varchar(255) not null,
    forgot_password_token_id bigint
        constraint fkaiutrsj0e7ma9i15kenx8yxtr
            references forgot_password_token,
    constraint ukqwsiih4ebn73junrlqgc7drwe
        unique (veryfication_token, username)
);

alter table authentication_data
    owner to root;

alter table account_data
    add constraint fk9fq7f9e4tkkiya8n63h847yb8
        foreign key (authentication_data_id) references authentication_data;

create table id_generator
(
    class_name varchar(255) not null
        constraint id_generator_pkey
            primary key,
    id_range   bigint
);

alter table id_generator
    owner to root;

create table product
(
    id          bigint           not null
        constraint product_pkey
            primary key,
    active      boolean          not null,
    description varchar(512)     not null,
    in_store    integer          not null,
    price       double precision not null,
    title       varchar(32)      not null
        constraint u24nm5sf12bh9o0w38a4hotdubi
                unique,
    type        varchar(255)     not null,
    version     bigint           not null,
    category_id bigint           not null
        constraint fk1mtsbur82frn64de7balymq9s
            references category
);

alter table product
    owner to root;

create table ordered_items
(
    id             bigint  not null
        constraint ordered_items_pkey
            primary key,
    identifier     varchar(36)  not null
        constraint fsfnvsfasfgkutwgwns1353ews
            unique,
    ordered_number integer not null,
    version        bigint  not null,
    product_id     bigint  not null
        constraint fkenvq2gngkutwji39t3vns1ews
            references product
);

alter table ordered_items
    owner to root;

create table promotion
(
    id          bigint           not null
        constraint promotion_pkey
            primary key,
    active      boolean          not null,
    discount    double precision not null,
    name        varchar(32)      not null
        constraint uktnm59112bh9o0828a4hotdubi
            unique,
    version     bigint           not null,
    category_id bigint           not null
        constraint fkok7am2wl7u75y5ssfbcmwcs16
            references category
);

alter table promotion
    owner to root;

create table status
(
    id          bigint      not null
        constraint status_pkey
            primary key,
    status_name varchar(16) not null
        constraint ukikty98aye7nunxe4f25a39efl
            unique
);

alter table status
    owner to root;

create table "order"
(
    id          bigint           not null
        constraint order_pkey
            primary key,
    identifier     varchar(36)  not null
        constraint fsfnvsfasdsgsfgkutwgwns1353ews
            unique,
    order_date  timestamp        not null,
    total_price double precision not null,
    version     bigint           not null,
    account_id  bigint           not null
        constraint fki6cs7o73ap4ulywem3of1k6nt
            references account_data,
    status_id   bigint           not null
        constraint fk1j6h5yblbp2gxa9h3gcqiudtb
            references status
);

alter table "order"
    owner to root;

create table account_access_level_mapping
(
    account_id      bigint not null
        constraint fkphw47ewtcdrfv28x2lssvqkb4
            references account_data,
    access_level_id bigint not null
        constraint fk4uhckp9doykcndaqlsgg2ltw
            references access_level,
    constraint account_access_level_mapping_pkey
        primary key (account_id, access_level_id)
);

alter table account_access_level_mapping
    owner to root;

create table ordered_items_basket_mapping
(
    basket_id        bigint not null
        constraint fk94m4jaj97vkf9gq9pip5kmyr0
            references basket,
    ordered_items_id bigint not null
        constraint fkbas5vb38pcq1g583fx46qa1w0
            references ordered_items,
    constraint ordered_items_basket_mapping_pkey
        primary key (basket_id, ordered_items_id)
);

alter table ordered_items_basket_mapping
    owner to root;

create table ordered_items_order_mapping
(
    order_id         bigint not null
        constraint fksksskxntlobxy8d6yyh820yxg
            references "order",
    ordered_items_id bigint not null
        constraint fk5oo83b66x8n7pqxh2m3e8wp55
            references ordered_items,
    constraint ordered_items_order_mapping_pkey
        primary key (order_id, ordered_items_id)
);

alter table ordered_items_order_mapping
    owner to root;





-- USERS AND PRIVILEGES
CREATE USER mok WITH PASSWORD 'mok123';
GRANT SELECT, UPDATE ON id_generator TO mok;
GRANT SELECT, INSERT, UPDATE, DELETE ON forgot_password_token TO mok;
GRANT SELECT, INSERT, UPDATE, DELETE ON authentication_data TO mok;
GRANT SELECT, INSERT, UPDATE, DELETE ON account_data TO mok;
GRANT SELECT ON access_level TO mok;
GRANT SELECT, INSERT, UPDATE, DELETE ON account_access_level_mapping TO mok;
GRANT SELECT, INSERT, DELETE ON basket TO mok;

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
GRANT SELECT, INSERT, UPDATE, DELETE ON ordered_items_order_mapping TO moz;
GRANT SELECT, INSERT, UPDATE ON ordered_items TO moz;
GRANT SELECT, INSERT, UPDATE, DELETE ON ordered_items_basket_mapping TO moz;
GRANT SELECT ON product TO moz;
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
INSERT INTO id_generator VALUES ('ordered_items',50);

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