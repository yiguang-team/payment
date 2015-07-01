ALTER TABLE t_merchant_order drop column order_status  ;

delete from T_DATASOURCE t where t.code = 'ORDER_STATUS';
delete from T_DATASOURCE_OPTION t where t.data_source_code = 'ORDER_STATUS';

UPDATE T_DATASOURCE_OPTION SET option_id = '5' WHERE id = '32' ;
UPDATE T_DATASOURCE_OPTION SET option_id = '1' WHERE id = '33' ;
UPDATE T_DATASOURCE_OPTION SET option_id = '2' WHERE id = '34' ;
UPDATE T_DATASOURCE_OPTION SET option_id = '3' WHERE id = '35' ;
UPDATE T_DATASOURCE_OPTION SET option_id = '4', option_label = '渠道' WHERE id = '36' ;

UPDATE T_DATASOURCE_OPTION SET option_id = '8' WHERE id = '37' ;
UPDATE T_DATASOURCE_OPTION SET option_id = '9' WHERE id = '38' ;
UPDATE T_DATASOURCE_OPTION SET option_id = '10' WHERE id = '39' ;
insert into T_DATASOURCE_OPTION (id, option_id, option_label, status, remark, data_source_code, parent_option_id, parent_datasource_code)
values (82, 6, '城市', 1, null, 'PRODUCT_REPORT_MODEL', null, null);
insert into T_DATASOURCE_OPTION (id, option_id, option_label, status, remark, data_source_code, parent_option_id, parent_datasource_code)
values (83, 7, '商户', 1, null, 'PRODUCT_REPORT_MODEL', null, null);
commit;

insert into T_CITY (city_id, city_name, status, province_id, remark)
values ('2001', '资阳', 1, 'SC_', null);

insert into T_CITY (city_id, city_name, status, province_id, remark)
values ('2002', '三亚', 1, 'HIN', null);

insert into T_CITY (city_id, city_name, status, province_id, remark)
values ('2003', '三沙', 1, 'HIN', null);

insert into T_CITY (city_id, city_name, status, province_id, remark)
values ('2004', '嘉峪关', 1, 'GS_', null);

insert into T_CITY (city_id, city_name, status, province_id, remark)
values ('2005', '武威', 1, 'GS_', null);

insert into T_CITY (city_id, city_name, status, province_id, remark)
values ('2006', '贵港', 1, 'GX_', null);

insert into T_CITY (city_id, city_name, status, province_id, remark)
values ('2007', '贺州', 1, 'GX_', null);

insert into T_CITY (city_id, city_name, status, province_id, remark)
values ('2008', '来宾', 1, 'GX_', null);

insert into T_CITY (city_id, city_name, status, province_id, remark)
values ('2009', '崇左', 1, 'GX_', null);

commit;

