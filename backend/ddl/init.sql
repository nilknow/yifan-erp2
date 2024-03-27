create table public.product
(
    id       bigserial
        constraint product_pk
            primary key,
    name     varchar(64),
    count    bigint default 0 not null,
    category varchar(32)      not null,
    constraint product_name_category_uk
        unique (name, category)
);

create table material
(
    id                    bigserial
        constraint material_pk
            primary key,
    name                  varchar(64) not null
        constraint material_name_uk2
            unique,
    count                 bigint      not null,
    inventory_count_alert bigint,
    category              varchar(64)
);

create table alert
(
    id         bigserial,
    content    varchar(2000)     not null,
    state      integer default 0 not null,
    email_sent integer default 0 not null
);

comment on table alert is '预警表，包括库存预警';

comment on column alert.state is '0: unread 1:working on it 2:solved';

comment on column alert.email_sent is '0: not sent 1: sent';

create table product_material_rel
(
    id             bigserial
        constraint product_material_rel_pk
            primary key,
    product_id     bigint not null
        constraint product_material_rel_product_id_fk
            references product,
    material_id    bigint not null
        constraint product_material_rel_material_id_fk
            references material,
    material_count bigint not null,
    constraint product_material_rel_pk2
        unique (product_id, material_id)
);

create table login_user
(
    id       bigserial
        constraint login_user_pk
            primary key,
    username varchar  not null
        constraint login_user_username_uk2
            unique,
    password char(68) not null
);

ALTER TABLE login_user
    ADD COLUMN update_time TIMESTAMP DEFAULT now();

ALTER TABLE login_user
    ADD COLUMN company_id bigint ;

alter table login_user
    add constraint fk_company_id
    foreign key (company_id)
    references company(id);

create table authority
(
    id   bigserial
        constraint authority_pk
            primary key,
    name varchar(32) not null
        constraint authority_name_uk2
            unique
);

create table login_user_authority_rel
(
    id            bigserial
        constraint login_user_authority_rel_pk
            primary key,
    login_user_id bigint not null
        constraint login_user_authority_rel_login_user_id_fk
            references login_user,
    authority_id  bigint not null
        constraint login_user_authority_rel_authority_id_fk
            references authority,
    constraint login_user_authority_rel_pk2
        unique (login_user_id, authority_id)
);

create table bug
(
    id          bigserial
        constraint bug_pk
            primary key,
    content     varchar(4000) default ''::character varying not null,
    priority    char(4)       default '一般'::bpchar        not null,
    email       varchar(40),
    phone       varchar(20),
    create_time timestamp                                   not null
);


create function add_alert_row() returns trigger
    language plpgsql
as
$$
BEGIN
    IF NEW.inventory_count_alert is not null and NEW.inventory_count_alert >= NEW.count THEN
        INSERT INTO Alert (content) VALUES ('物料 ' || NEW.name || ' 库存不足，请添加库存');
    END IF;
    RETURN NEW;
END
$$;

drop trigger material_inventory_alert_trigger on material;

CREATE TRIGGER material_inventory_alert_trigger
    AFTER UPDATE OF inventory_count_alert,count
    ON material
    FOR EACH ROW
EXECUTE PROCEDURE add_alert_row();

CREATE OR REPLACE FUNCTION update_material_timestamp()
    RETURNS TRIGGER AS
$$
BEGIN
    NEW.update_timestamp := NOW();
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER material_update_trigger
    BEFORE INSERT OR UPDATE
    ON material
    FOR EACH ROW
EXECUTE procedure update_material_timestamp();

create table suggestion
(
    id          bigserial
        constraint suggestion_pk
            primary key,
    content     varchar(4000) default ''::character varying not null,
    email       char(40),
    phone       char(20),
    create_time timestamp     default now()                 not null
);

create table company
(
    id          bigserial primary key,
    name        varchar(100) unique not null,
    update_time timestamp
);

alter table material
    ADD CONSTRAINT material_name_category_uk UNIQUE (name, category, company_id)
        DEFERRABLE;