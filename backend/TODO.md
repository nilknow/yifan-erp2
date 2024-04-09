1. use React to refactor some html code
2. common html component

```sql

alter table bug add column feedback varchar(36);
alter table suggestion add column feedback varchar(36);

alter table material add column serial_num varchar(36);
update material set serial_num=id;
alter table material add constraint uk_material_serial_num unique (serial_num);

alter table product add column description varchar(400);
alter table product add column serial_num varchar(36);

alter table product add column serial_num varchar(36);
update product set serial_num=id;
alter table product add constraint uk_product_serial_num unique (serial_num);

alter table material add column create_user bigint;
alter table material add column update_user bigint;

alter table material
    add constraint material_create_user_fk foreign key (create_user) references login_user(id);
alter table material
    add constraint material_update_user_fk foreign key (update_user) references login_user(id);

create table action_log (
    id bigserial primary key,
    batch_id uuid,
    event_type VARCHAR(36) NOT NULL,
    timestamp TIMESTAMP NOT NULL,
    user_id bigint references login_user(id),
    table_name VARCHAR(36) NOT NULL,
    source varchar(36) not null,
    description varchar(256) not null,
    additional_info JSONB
);

alter table alert_email add column company_id bigint references company(id);
```