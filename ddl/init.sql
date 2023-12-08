create table product
(
    id    bigserial
        constraint product_pk
            primary key,
    name  varchar(64)
        constraint product_name_uk2
            unique,
    count bigint default 0 not null
);

alter table product
    owner to myuser;

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

alter table material
    owner to myuser;

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

alter table alert
    owner to myuser;

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

alter table product_material_rel
    owner to myuser;

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

alter table login_user
    owner to myuser;

create table authority
(
    id   bigserial
        constraint authority_pk
            primary key,
    name varchar(32) not null
        constraint authority_name_uk2
            unique
);

alter table authority
    owner to myuser;

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

alter table login_user_authority_rel
    owner to myuser;

create function add_alert_row() returns trigger
    language plpgsql
as
$$
BEGIN
    IF NEW.inventory_count_alert is not null and NEW.inventory_count_alert >= NEW.count THEN
        INSERT INTO Alert (content) VALUES ('物料 '||NEW.name||' 库存不足，请添加库存');
    END IF;
    RETURN NEW;
END
$$;

alter function add_alert_row() owner to myuser;

create trigger material_inventory_alert_trigger
    after update
        of inventory_count_alert
    on material
    for each row
execute procedure add_alert_row();

