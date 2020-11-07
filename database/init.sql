GRANT ALL PRIVILEGES ON DATABASE docker TO docker;
---------------------------------------------------------------------------------------------------
create table id_generator
(
    class_name varchar(255) not null
        constraint id_generator_pkey
            primary key,
    id_range   bigint
);

alter table id_generator
    owner to docker;


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
    owner to docker;

create table account_data
(
    id                     bigint      not null
        constraint account_data_pkey
            primary key,
    active                 boolean     not null,
    email                  varchar(32) not null
        constraint uk_6nyd9ykqgjm7n4ngreynnly8t
            unique,
    first_name             varchar(32) not null,
    last_name              varchar(32) not null,
    provider               varchar(255),
    provider_id            varchar(255),
    version                bigint      not null,
    authentication_data_id bigint
);

alter table account_data
    owner to docker;

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
    owner to docker;

create table authentication_data
(
    id                       bigint       not null
        constraint authentication_data_pkey
            primary key,
    email_verified           boolean      not null,
    password                 varchar(64)  not null,
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
    owner to docker;

alter table account_data
    add constraint fk9fq7f9e4tkkiya8n63h847yb8
        foreign key (authentication_data_id) references authentication_data;

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
    owner to docker;





INSERT INTO id_generator VALUES ('account_data',50);
INSERT INTO id_generator VALUES ('authentication_data',50);
INSERT INTO id_generator VALUES ('access_level',50);
INSERT INTO id_generator VALUES ('forgot_password_token',50);
INSERT INTO access_level VALUES(3, 'ROLE_CLIENT');
INSERT INTO access_level VALUES(2, 'ROLE_EMPLOYEE');
INSERT INTO access_level VALUES(1,'ROLE_ADMIN');

INSERT INTO authentication_data (id, email_verified, password, username, version, veryfication_token, forgot_password_token_id)
VALUES (1, true, '$2a$10$DzKdlc8z6OB.woJOkmZsIeO9P6SWxOltsnVoWGurzNrXlTyS45kS6', 'admin', 0, 'ec45e9a5-ea8d-40ca-9ee3-e382dd9e5dd4', null);
INSERT INTO account_data (id, active, email, first_name, last_name, provider, provider_id, version, authentication_data_id)
VALUES (1, true, 'jKowalski@gmail.com', 'Jan', 'Kowalski', 'system', null, 0, 1);
INSERT INTO account_access_level_mapping (account_id, access_level_id)
VALUES (1, 1);

INSERT INTO authentication_data (id, email_verified, password, username, version, veryfication_token, forgot_password_token_id)
VALUES (2, true, '$2a$10$DzKdlc8z6OB.woJOkmZsIeO9P6SWxOltsnVoWGurzNrXlTyS45kS6', 'manager', 0, 'ec45aaa5-ae5t-35yt-0lzq-e382dd9e5dd4', null);
INSERT INTO account_data (id, active, email, first_name, last_name, provider, provider_id, version, authentication_data_id)
VALUES (2, true, 'stanislaw.nowak@gmail.com', 'Stanislaw', 'Nowak', 'system', null, 0, 2);
INSERT INTO account_access_level_mapping (account_id, access_level_id)
VALUES (2, 2);

INSERT INTO authentication_data (id, email_verified, password, username, version, veryfication_token, forgot_password_token_id)
VALUES (3, true, '$2a$10$DzKdlc8z6OB.woJOkmZsIeO9P6SWxOltsnVoWGurzNrXlTyS45kS6', 'client', 0, 'ty43aaa5-rf3g-35yt-66cv-e382dd9e5dd4', null);
INSERT INTO account_data (id, active, email, first_name, last_name, provider, provider_id, version, authentication_data_id)
VALUES (3, true, 'zygmunt.august@gmail.com', 'Zygmunt', 'August', 'system', null, 0, 3);
INSERT INTO account_access_level_mapping (account_id, access_level_id)
VALUES (3, 3);