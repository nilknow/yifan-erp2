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

```