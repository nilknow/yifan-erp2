create table public.product
(
    id    bigserial
        constraint product_pk
            primary key,
    name  varchar(64)
        constraint product_name_uk2
            unique,
    count bigint default 0 not null
);

create table public.material
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

create trigger material_inventory_alert_trigger
    after update
        of inventory_count_alert
    on public.material
    for each row
execute procedure public.add_alert_row();

create table public.alert
(
    id         bigserial,
    content    varchar(2000)     not null,
    state      integer default 0 not null,
    email_sent integer default 0 not null
);

comment on table public.alert is '预警表，包括库存预警';

comment on column public.alert.state is '0: unread 1:working on it 2:solved';

comment on column public.alert.email_sent is '0: not sent 1: sent';


create table public.product_material_rel
(
    id             bigserial
        constraint product_material_rel_pk
            primary key,
    product_id     bigint not null
        constraint product_material_rel_product_id_fk
            references public.product,
    material_id    bigint not null
        constraint product_material_rel_material_id_fk
            references public.material,
    material_count bigint not null
);
alter table product_material_rel
    add constraint product_material_rel_pk2
        unique (product_id, material_id);

CREATE OR REPLACE FUNCTION add_alert_row()
    RETURNS TRIGGER AS $$
BEGIN
    IF NEW.inventory_count_alert is not null and NEW.inventory_count_alert <= NEW.count THEN
        INSERT INTO Alert (content) VALUES ('物料 '||NEW.name||' 库存不足，请添加库存');
    END IF;
    RETURN NEW;
END
$$ LANGUAGE plpgsql;

CREATE TRIGGER material_inventory_alert_trigger
    AFTER UPDATE OF inventory_count_alert ON Material
    FOR EACH ROW
EXECUTE FUNCTION add_alert_row();