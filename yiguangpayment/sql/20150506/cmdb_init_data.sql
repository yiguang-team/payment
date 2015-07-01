drop table T_CHANNEL cascade constraints;

drop sequence SEQ_CHANNEL;

create sequence SEQ_CHANNEL
start with 2000
increment by 1
 minvalue 2000;
 
 
 /*==============================================================*/
/* Table: T_CHANNEL                                             */
/*==============================================================*/
create table T_CHANNEL 
(
   ID                   NUMBER(20)           not null,
   NAME                 VARCHAR(200)         not null,
   STATUS               NUMBER(1)            not null,
   REMARK               VARCHAR(4000),
   constraint PK_T_CHANNEL primary key (ID)
);
----------------------------------------------------------------------------------------------------------------------------------------------
insert into T_CHANNEL (id, name, status, remark)
values (1, '联通wo+', 1, null);
insert into T_CHANNEL (id, name, status, remark)
values (2, '联通沃阅读', 1, null);
insert into T_CHANNEL (id, name, status, remark)
values (3, '电信翼支付', 1, null);

commit;
delete from T_DATASOURCE t where t.id = 12;
insert into T_DATASOURCE (id, name, type, status, remark, option_sql, parent_code, code)
values (12, '渠道', 2, 1, null, 'select t.id as optionId,t.name as optionLabel from t_channel t where 1=1', null, 'CHANNEL');

drop table T_CARRIER_CHANNEL_RELATION cascade constraints;


drop sequence SEQ_CARRIER_CHANNEL_RELATION;

create sequence SEQ_CARRIER_CHANNEL_RELATION
increment by 1
start with 2000
 minvalue 2000;
 

   /*==============================================================*/
/* Table:  T_CARRIER_CHANNEL_RELATION                                */
/*==============================================================*/
create table T_CARRIER_CHANNEL_RELATION
(
  id         NUMBER(20) not null,
  channel_id NUMBER(20) not null,
  carrier_id NUMBER(20),
  status     NUMBER(1) not null,
  remark     VARCHAR2(4000),
  constraint PK_T_CARRIER_CHANNEL_RELATION primary key (ID)
)
;
alter table T_PRODUCT_CHARGING_POINT add (charging_code VARCHAR2(200));

insert into T_PRODUCT (id, name, type, supplier_id, status, remark)
values (1008, '凯翼悌支付产品', 2, 1008, 1, null);
insert into T_PRODUCT (id, name, type, supplier_id, status, remark)
values (1009, '翼光天天跑得快支付产品', 2, 1009, 1, null);
insert into T_PRODUCT (id, name, type, supplier_id, status, remark)
values (1007, '短代商户7支付产品', 2, 1007, 1, null);
insert into T_PRODUCT (id, name, type, supplier_id, status, remark)
values (1006, '短代商户6支付产品', 2, 1006, 1, null);
insert into T_PRODUCT (id, name, type, supplier_id, status, remark)
values (1005, '短代商户5支付产品', 2, 1005, 1, null);
insert into T_PRODUCT (id, name, type, supplier_id, status, remark)
values (1004, '短代商户4支付产品', 2, 1004, 1, null);
insert into T_PRODUCT (id, name, type, supplier_id, status, remark)
values (1003, '短代商户3支付产品', 2, 1003, 1, null);
insert into T_PRODUCT (id, name, type, supplier_id, status, remark)
values (1002, '短代商户2支付产品', 2, 1002, 1, null);
insert into T_PRODUCT (id, name, type, supplier_id, status, remark)
values (1001, '短代商户1支付产品', 2, 1001, 1, null);

insert into T_PRODUCT_CHARGING_POINT (id, name, supplier_id, status, product_id, province_id, city_id, remark, face_amount, pay_amount, delivery_amount, charging_type)
values (1014, '凯翼悌0.1元', 1008, 1, 1008, null, null, null, 10, 10, 10, 1);
insert into T_PRODUCT_CHARGING_POINT (id, name, supplier_id, status, product_id, province_id, city_id, remark, face_amount, pay_amount, delivery_amount, charging_type)
values (1015, '凯翼悌2元', 1008, 1, 1008, null, null, null, 200, 200, 200, 1);
insert into T_PRODUCT_CHARGING_POINT (id, name, supplier_id, status, product_id, province_id, city_id, remark, face_amount, pay_amount, delivery_amount, charging_type)
values (1016, '凯翼悌5元', 1008, 1, 1008, null, null, null, 500, 500, 500, 1);
insert into T_PRODUCT_CHARGING_POINT (id, name, supplier_id, status, product_id, province_id, city_id, remark, face_amount, pay_amount, delivery_amount, charging_type)
values (1017, '凯翼悌10元', 1008, 1, 1008, null, null, null, 1000, 1000, 1000, 1);
insert into T_PRODUCT_CHARGING_POINT (id, name, supplier_id, status, product_id, province_id, city_id, remark, face_amount, pay_amount, delivery_amount, charging_type)
values (1018, '凯翼悌20元', 1008, 1, 1008, null, null, null, 2000, 2000, 2000, 1);
insert into T_PRODUCT_CHARGING_POINT (id, name, supplier_id, status, product_id, province_id, city_id, remark, face_amount, pay_amount, delivery_amount, charging_type)
values (1019, '凯翼悌30元', 1008, 1, 1008, null, null, null, 3000, 3000, 3000, 1);
insert into T_PRODUCT_CHARGING_POINT (id, name, supplier_id, status, product_id, province_id, city_id, remark, face_amount, pay_amount, delivery_amount, charging_type)
values (1020, '凯翼悌40元', 1008, 1, 1008, null, null, null, 4000, 4000, 4000, 1);
insert into T_PRODUCT_CHARGING_POINT (id, name, supplier_id, status, product_id, province_id, city_id, remark, face_amount, pay_amount, delivery_amount, charging_type)
values (1021, '凯翼悌50元', 1008, 1, 1008, null, null, null, 5000, 5000, 5000, 1);
insert into T_PRODUCT_CHARGING_POINT (id, name, supplier_id, status, product_id, province_id, city_id, remark, face_amount, pay_amount, delivery_amount, charging_type)
values (1022, '凯翼悌60元', 1008, 1, 1008, null, null, null, 6000, 6000, 6000, 1);

insert into T_PRODUCT_CHARGING_POINT (id, name, supplier_id, status, product_id, province_id, city_id, remark, face_amount, pay_amount, delivery_amount, charging_type)
values (1023, '短代商户1-0.1元', 1001, 1, 1001, null, null, null, 10, 10, 10, 1);
insert into T_PRODUCT_CHARGING_POINT (id, name, supplier_id, status, product_id, province_id, city_id, remark, face_amount, pay_amount, delivery_amount, charging_type)
values (1024, '短代商户1-2元', 1001, 1, 1001, null, null, null, 200, 200, 200, 1);
insert into T_PRODUCT_CHARGING_POINT (id, name, supplier_id, status, product_id, province_id, city_id, remark, face_amount, pay_amount, delivery_amount, charging_type)
values (1025, '短代商户1-5元', 1001, 1, 1001, null, null, null, 500, 500, 500, 1);
insert into T_PRODUCT_CHARGING_POINT (id, name, supplier_id, status, product_id, province_id, city_id, remark, face_amount, pay_amount, delivery_amount, charging_type)
values (1026, '短代商户1-10元', 1001, 1, 1001, null, null, null, 1000, 1000, 1000, 1);
insert into T_PRODUCT_CHARGING_POINT (id, name, supplier_id, status, product_id, province_id, city_id, remark, face_amount, pay_amount, delivery_amount, charging_type)
values (1027, '短代商户1-20元', 1001, 1, 1001, null, null, null, 2000, 2000, 2000, 1);
insert into T_PRODUCT_CHARGING_POINT (id, name, supplier_id, status, product_id, province_id, city_id, remark, face_amount, pay_amount, delivery_amount, charging_type)
values (1028, '短代商户1-30元', 1001, 1, 1001, null, null, null, 3000, 3000, 3000, 1);
insert into T_PRODUCT_CHARGING_POINT (id, name, supplier_id, status, product_id, province_id, city_id, remark, face_amount, pay_amount, delivery_amount, charging_type)
values (1029, '短代商户1-40元', 1001, 1, 1001, null, null, null, 4000, 4000, 4000, 1);
insert into T_PRODUCT_CHARGING_POINT (id, name, supplier_id, status, product_id, province_id, city_id, remark, face_amount, pay_amount, delivery_amount, charging_type)
values (1030, '短代商户1-50元', 1001, 1, 1001, null, null, null, 5000, 5000, 5000, 1);
insert into T_PRODUCT_CHARGING_POINT (id, name, supplier_id, status, product_id, province_id, city_id, remark, face_amount, pay_amount, delivery_amount, charging_type)
values (1031, '短代商户1-60元', 1001, 1, 1001, null, null, null, 6000, 6000, 6000, 1);

insert into T_PRODUCT_CHARGING_POINT (id, name, supplier_id, status, product_id, province_id, city_id, remark, face_amount, pay_amount, delivery_amount, charging_type)
values (1032, '短代商户2-0.1元', 1002, 1, 1002, null, null, null, 10, 10, 10, 1);
insert into T_PRODUCT_CHARGING_POINT (id, name, supplier_id, status, product_id, province_id, city_id, remark, face_amount, pay_amount, delivery_amount, charging_type)
values (1033, '短代商户2-2元', 1002, 1, 1002, null, null, null, 200, 200, 200, 1);
insert into T_PRODUCT_CHARGING_POINT (id, name, supplier_id, status, product_id, province_id, city_id, remark, face_amount, pay_amount, delivery_amount, charging_type)
values (1034, '短代商户2-5元', 1002, 1, 1002, null, null, null, 500, 500, 500, 1);
insert into T_PRODUCT_CHARGING_POINT (id, name, supplier_id, status, product_id, province_id, city_id, remark, face_amount, pay_amount, delivery_amount, charging_type)
values (1035, '短代商户2-10元', 1002, 1, 1002, null, null, null, 1000, 1000, 1000, 1);
insert into T_PRODUCT_CHARGING_POINT (id, name, supplier_id, status, product_id, province_id, city_id, remark, face_amount, pay_amount, delivery_amount, charging_type)
values (1036, '短代商户2-20元', 1002, 1, 1002, null, null, null, 2000, 2000, 2000, 1);
insert into T_PRODUCT_CHARGING_POINT (id, name, supplier_id, status, product_id, province_id, city_id, remark, face_amount, pay_amount, delivery_amount, charging_type)
values (1037, '短代商户2-30元', 1002, 1, 1002, null, null, null, 3000, 3000, 3000, 1);
insert into T_PRODUCT_CHARGING_POINT (id, name, supplier_id, status, product_id, province_id, city_id, remark, face_amount, pay_amount, delivery_amount, charging_type)
values (1038, '短代商户2-40元', 1002, 1, 1002, null, null, null, 4000, 4000, 4000, 1);
insert into T_PRODUCT_CHARGING_POINT (id, name, supplier_id, status, product_id, province_id, city_id, remark, face_amount, pay_amount, delivery_amount, charging_type)
values (1039, '短代商户2-50元', 1002, 1, 1002, null, null, null, 5000, 5000, 5000, 1);
insert into T_PRODUCT_CHARGING_POINT (id, name, supplier_id, status, product_id, province_id, city_id, remark, face_amount, pay_amount, delivery_amount, charging_type)
values (1040, '短代商户2-60元', 1002, 1, 1002, null, null, null, 6000, 6000, 6000, 1);

insert into T_PRODUCT_CHARGING_POINT (id, name, supplier_id, status, product_id, province_id, city_id, remark, face_amount, pay_amount, delivery_amount, charging_type)
values (1041, '短代商户3-0.1元', 1003, 1, 1003, null, null, null, 10, 10, 10, 1);
insert into T_PRODUCT_CHARGING_POINT (id, name, supplier_id, status, product_id, province_id, city_id, remark, face_amount, pay_amount, delivery_amount, charging_type)
values (1042, '短代商户3-2元', 1003, 1, 1003, null, null, null, 200, 200, 200, 1);
insert into T_PRODUCT_CHARGING_POINT (id, name, supplier_id, status, product_id, province_id, city_id, remark, face_amount, pay_amount, delivery_amount, charging_type)
values (1043, '短代商户3-5元', 1003, 1, 1003, null, null, null, 500, 500, 500, 1);
insert into T_PRODUCT_CHARGING_POINT (id, name, supplier_id, status, product_id, province_id, city_id, remark, face_amount, pay_amount, delivery_amount, charging_type)
values (1044, '短代商户3-10元', 1003, 1, 1003, null, null, null, 1000, 1000, 1000, 1);
insert into T_PRODUCT_CHARGING_POINT (id, name, supplier_id, status, product_id, province_id, city_id, remark, face_amount, pay_amount, delivery_amount, charging_type)
values (1045, '短代商户3-20元', 1003, 1, 1003, null, null, null, 2000, 2000, 2000, 1);
insert into T_PRODUCT_CHARGING_POINT (id, name, supplier_id, status, product_id, province_id, city_id, remark, face_amount, pay_amount, delivery_amount, charging_type)
values (1046, '短代商户3-30元', 1003, 1, 1003, null, null, null, 3000, 3000, 3000, 1);
insert into T_PRODUCT_CHARGING_POINT (id, name, supplier_id, status, product_id, province_id, city_id, remark, face_amount, pay_amount, delivery_amount, charging_type)
values (1047, '短代商户3-40元', 1003, 1, 1003, null, null, null, 4000, 4000, 4000, 1);
insert into T_PRODUCT_CHARGING_POINT (id, name, supplier_id, status, product_id, province_id, city_id, remark, face_amount, pay_amount, delivery_amount, charging_type)
values (1048, '短代商户3-50元', 1003, 1, 1003, null, null, null, 5000, 5000, 5000, 1);
insert into T_PRODUCT_CHARGING_POINT (id, name, supplier_id, status, product_id, province_id, city_id, remark, face_amount, pay_amount, delivery_amount, charging_type)
values (1049, '短代商户3-60元', 1003, 1, 1003, null, null, null, 6000, 6000, 6000, 1);

insert into T_PRODUCT_CHARGING_POINT (id, name, supplier_id, status, product_id, province_id, city_id, remark, face_amount, pay_amount, delivery_amount, charging_type)
values (1050, '短代商户4-0.1元', 1004, 1, 1004, null, null, null, 10, 10, 10, 1);
insert into T_PRODUCT_CHARGING_POINT (id, name, supplier_id, status, product_id, province_id, city_id, remark, face_amount, pay_amount, delivery_amount, charging_type)
values (1051, '短代商户4-2元', 1004, 1, 1004, null, null, null, 200, 200, 200, 1);
insert into T_PRODUCT_CHARGING_POINT (id, name, supplier_id, status, product_id, province_id, city_id, remark, face_amount, pay_amount, delivery_amount, charging_type)
values (1052, '短代商户4-5元', 1004, 1, 1004, null, null, null, 500, 500, 500, 1);
insert into T_PRODUCT_CHARGING_POINT (id, name, supplier_id, status, product_id, province_id, city_id, remark, face_amount, pay_amount, delivery_amount, charging_type)
values (1053, '短代商户4-10元', 1004, 1, 1004, null, null, null, 1000, 1000, 1000, 1);
insert into T_PRODUCT_CHARGING_POINT (id, name, supplier_id, status, product_id, province_id, city_id, remark, face_amount, pay_amount, delivery_amount, charging_type)
values (1054, '短代商户4-20元', 1004, 1, 1004, null, null, null, 2000, 2000, 2000, 1);
insert into T_PRODUCT_CHARGING_POINT (id, name, supplier_id, status, product_id, province_id, city_id, remark, face_amount, pay_amount, delivery_amount, charging_type)
values (1055, '短代商户4-30元', 1004, 1, 1004, null, null, null, 3000, 3000, 3000, 1);
insert into T_PRODUCT_CHARGING_POINT (id, name, supplier_id, status, product_id, province_id, city_id, remark, face_amount, pay_amount, delivery_amount, charging_type)
values (1056, '短代商户4-40元', 1004, 1, 1004, null, null, null, 4000, 4000, 4000, 1);
insert into T_PRODUCT_CHARGING_POINT (id, name, supplier_id, status, product_id, province_id, city_id, remark, face_amount, pay_amount, delivery_amount, charging_type)
values (1057, '短代商户4-50元', 1004, 1, 1004, null, null, null, 5000, 5000, 5000, 1);
insert into T_PRODUCT_CHARGING_POINT (id, name, supplier_id, status, product_id, province_id, city_id, remark, face_amount, pay_amount, delivery_amount, charging_type)
values (1058, '短代商户4-60元', 1004, 1, 1004, null, null, null, 6000, 6000, 6000, 1);

insert into T_PRODUCT_CHARGING_POINT (id, name, supplier_id, status, product_id, province_id, city_id, remark, face_amount, pay_amount, delivery_amount, charging_type)
values (1059, '短代商户5-0.1元', 1005, 1, 1005, null, null, null, 10, 10, 10, 1);
insert into T_PRODUCT_CHARGING_POINT (id, name, supplier_id, status, product_id, province_id, city_id, remark, face_amount, pay_amount, delivery_amount, charging_type)
values (1060, '短代商户5-2元', 1005, 1, 1005, null, null, null, 200, 200, 200, 1);
insert into T_PRODUCT_CHARGING_POINT (id, name, supplier_id, status, product_id, province_id, city_id, remark, face_amount, pay_amount, delivery_amount, charging_type)
values (1061, '短代商户5-5元', 1005, 1, 1005, null, null, null, 500, 500, 500, 1);
insert into T_PRODUCT_CHARGING_POINT (id, name, supplier_id, status, product_id, province_id, city_id, remark, face_amount, pay_amount, delivery_amount, charging_type)
values (1062, '短代商户5-10元', 1005, 1, 1005, null, null, null, 1000, 1000, 1000, 1);
insert into T_PRODUCT_CHARGING_POINT (id, name, supplier_id, status, product_id, province_id, city_id, remark, face_amount, pay_amount, delivery_amount, charging_type)
values (1063, '短代商户5-20元', 1005, 1, 1005, null, null, null, 2000, 2000, 2000, 1);
insert into T_PRODUCT_CHARGING_POINT (id, name, supplier_id, status, product_id, province_id, city_id, remark, face_amount, pay_amount, delivery_amount, charging_type)
values (1064, '短代商户5-30元', 1005, 1, 1005, null, null, null, 3000, 3000, 3000, 1);
insert into T_PRODUCT_CHARGING_POINT (id, name, supplier_id, status, product_id, province_id, city_id, remark, face_amount, pay_amount, delivery_amount, charging_type)
values (1065, '短代商户5-40元', 1005, 1, 1005, null, null, null, 4000, 4000, 4000, 1);
insert into T_PRODUCT_CHARGING_POINT (id, name, supplier_id, status, product_id, province_id, city_id, remark, face_amount, pay_amount, delivery_amount, charging_type)
values (1066, '短代商户5-50元', 1005, 1, 1005, null, null, null, 5000, 5000, 5000, 1);
insert into T_PRODUCT_CHARGING_POINT (id, name, supplier_id, status, product_id, province_id, city_id, remark, face_amount, pay_amount, delivery_amount, charging_type)
values (1067, '短代商户5-60元', 1005, 1, 1005, null, null, null, 6000, 6000, 6000, 1);

insert into T_PRODUCT_CHARGING_POINT (id, name, supplier_id, status, product_id, province_id, city_id, remark, face_amount, pay_amount, delivery_amount, charging_type)
values (1068, '短代商户6-0.1元', 1006, 1, 1006, null, null, null, 10, 10, 10, 1);
insert into T_PRODUCT_CHARGING_POINT (id, name, supplier_id, status, product_id, province_id, city_id, remark, face_amount, pay_amount, delivery_amount, charging_type)
values (1069, '短代商户6-2元', 1006, 1, 1006, null, null, null, 200, 200, 200, 1);
insert into T_PRODUCT_CHARGING_POINT (id, name, supplier_id, status, product_id, province_id, city_id, remark, face_amount, pay_amount, delivery_amount, charging_type)
values (1070, '短代商户6-5元', 1006, 1, 1006, null, null, null, 500, 500, 500, 1);
insert into T_PRODUCT_CHARGING_POINT (id, name, supplier_id, status, product_id, province_id, city_id, remark, face_amount, pay_amount, delivery_amount, charging_type)
values (1071, '短代商户6-10元', 1006, 1, 1006, null, null, null, 1000, 1000, 1000, 1);
insert into T_PRODUCT_CHARGING_POINT (id, name, supplier_id, status, product_id, province_id, city_id, remark, face_amount, pay_amount, delivery_amount, charging_type)
values (1072, '短代商户6-20元', 1006, 1, 1006, null, null, null, 2000, 2000, 2000, 1);
insert into T_PRODUCT_CHARGING_POINT (id, name, supplier_id, status, product_id, province_id, city_id, remark, face_amount, pay_amount, delivery_amount, charging_type)
values (1073, '短代商户6-30元', 1006, 1, 1006, null, null, null, 3000, 3000, 3000, 1);
insert into T_PRODUCT_CHARGING_POINT (id, name, supplier_id, status, product_id, province_id, city_id, remark, face_amount, pay_amount, delivery_amount, charging_type)
values (1074, '短代商户6-40元', 1006, 1, 1006, null, null, null, 4000, 4000, 4000, 1);
insert into T_PRODUCT_CHARGING_POINT (id, name, supplier_id, status, product_id, province_id, city_id, remark, face_amount, pay_amount, delivery_amount, charging_type)
values (1075, '短代商户6-50元', 1006, 1, 1006, null, null, null, 5000, 5000, 5000, 1);
insert into T_PRODUCT_CHARGING_POINT (id, name, supplier_id, status, product_id, province_id, city_id, remark, face_amount, pay_amount, delivery_amount, charging_type)
values (1076, '短代商户6-60元', 1006, 1, 1006, null, null, null, 6000, 6000, 6000, 1);

insert into T_PRODUCT_CHARGING_POINT (id, name, supplier_id, status, product_id, province_id, city_id, remark, face_amount, pay_amount, delivery_amount, charging_type)
values (1077, '短代商户7-0.1元', 1007, 1, 1007, null, null, null, 10, 10, 10, 1);
insert into T_PRODUCT_CHARGING_POINT (id, name, supplier_id, status, product_id, province_id, city_id, remark, face_amount, pay_amount, delivery_amount, charging_type)
values (1078, '短代商户7-2元', 1007, 1, 1007, null, null, null, 200, 200, 200, 1);
insert into T_PRODUCT_CHARGING_POINT (id, name, supplier_id, status, product_id, province_id, city_id, remark, face_amount, pay_amount, delivery_amount, charging_type)
values (1079, '短代商户7-5元', 1007, 1, 1007, null, null, null, 500, 500, 500, 1);
insert into T_PRODUCT_CHARGING_POINT (id, name, supplier_id, status, product_id, province_id, city_id, remark, face_amount, pay_amount, delivery_amount, charging_type)
values (1080, '短代商户7-10元', 1007, 1, 1007, null, null, null, 1000, 1000, 1000, 1);
insert into T_PRODUCT_CHARGING_POINT (id, name, supplier_id, status, product_id, province_id, city_id, remark, face_amount, pay_amount, delivery_amount, charging_type)
values (1081, '短代商户7-20元', 1007, 1, 1007, null, null, null, 2000, 2000, 2000, 1);
insert into T_PRODUCT_CHARGING_POINT (id, name, supplier_id, status, product_id, province_id, city_id, remark, face_amount, pay_amount, delivery_amount, charging_type)
values (1082, '短代商户7-30元', 1007, 1, 1007, null, null, null, 3000, 3000, 3000, 1);
insert into T_PRODUCT_CHARGING_POINT (id, name, supplier_id, status, product_id, province_id, city_id, remark, face_amount, pay_amount, delivery_amount, charging_type)
values (1083, '短代商户7-40元', 1007, 1, 1007, null, null, null, 4000, 4000, 4000, 1);
insert into T_PRODUCT_CHARGING_POINT (id, name, supplier_id, status, product_id, province_id, city_id, remark, face_amount, pay_amount, delivery_amount, charging_type)
values (1084, '短代商户7-50元', 1007, 1, 1007, null, null, null, 5000, 5000, 5000, 1);
insert into T_PRODUCT_CHARGING_POINT (id, name, supplier_id, status, product_id, province_id, city_id, remark, face_amount, pay_amount, delivery_amount, charging_type)
values (1085, '短代商户7-60元', 1007, 1, 1007, null, null, null, 6000, 6000, 6000, 1);

insert into T_PRODUCT_CHARGING_POINT (id, name, supplier_id, status, product_id, province_id, city_id, remark, face_amount, pay_amount, delivery_amount, charging_type)
values (1086, '翼光天天跑得快-0.1元', 1009, 1, 1009, null, null, null, 10, 10, 10, 1);
insert into T_PRODUCT_CHARGING_POINT (id, name, supplier_id, status, product_id, province_id, city_id, remark, face_amount, pay_amount, delivery_amount, charging_type)
values (1087, '翼光天天跑得快-2元', 1009, 1, 1009, null, null, null, 200, 200, 200, 1);
insert into T_PRODUCT_CHARGING_POINT (id, name, supplier_id, status, product_id, province_id, city_id, remark, face_amount, pay_amount, delivery_amount, charging_type)
values (1088, '翼光天天跑得快-5元', 1009, 1, 1009, null, null, null, 500, 500, 500, 1);
insert into T_PRODUCT_CHARGING_POINT (id, name, supplier_id, status, product_id, province_id, city_id, remark, face_amount, pay_amount, delivery_amount, charging_type)
values (1089, '翼光天天跑得快-10元', 1009, 1, 1009, null, null, null, 1000, 1000, 1000, 1);
insert into T_PRODUCT_CHARGING_POINT (id, name, supplier_id, status, product_id, province_id, city_id, remark, face_amount, pay_amount, delivery_amount, charging_type)
values (1090, '翼光天天跑得快-20元', 1009, 1, 1009, null, null, null, 2000, 2000, 2000, 1);
insert into T_PRODUCT_CHARGING_POINT (id, name, supplier_id, status, product_id, province_id, city_id, remark, face_amount, pay_amount, delivery_amount, charging_type)
values (1091, '翼光天天跑得快-30元', 1009, 1, 1009, null, null, null, 3000, 3000, 3000, 1);
insert into T_PRODUCT_CHARGING_POINT (id, name, supplier_id, status, product_id, province_id, city_id, remark, face_amount, pay_amount, delivery_amount, charging_type)
values (1092, '翼光天天跑得快-40元', 1009, 1, 1009, null, null, null, 4000, 4000, 4000, 1);
insert into T_PRODUCT_CHARGING_POINT (id, name, supplier_id, status, product_id, province_id, city_id, remark, face_amount, pay_amount, delivery_amount, charging_type)
values (1093, '翼光天天跑得快-50元', 1009, 1, 1009, null, null, null, 5000, 5000, 5000, 1);
insert into T_PRODUCT_CHARGING_POINT (id, name, supplier_id, status, product_id, province_id, city_id, remark, face_amount, pay_amount, delivery_amount, charging_type)
values (1094, '翼光天天跑得快-60元', 1009, 1, 1009, null, null, null, 6000, 6000, 6000, 1);
insert into T_PRODUCT_CHARGING_POINT (id, name, supplier_id, status, product_id, province_id, city_id, remark, face_amount, pay_amount, delivery_amount, charging_type)
values (1095, '翼光天天跑得快-VIP 15天', 1009, 1, 1009, null, null, null, 3000, 3000, 3000, 1);
insert into T_PRODUCT_CHARGING_POINT (id, name, supplier_id, status, product_id, province_id, city_id, remark, face_amount, pay_amount, delivery_amount, charging_type)
values (1096, '翼光天天跑得快-大喇叭10个', 1009, 1, 1009, null, null, null, 800, 800, 800, 1);
insert into T_PRODUCT_CHARGING_POINT (id, name, supplier_id, status, product_id, province_id, city_id, remark, face_amount, pay_amount, delivery_amount, charging_type)
values (1097, '翼光天天跑得快-大喇叭1个', 1009, 1, 1009, null, null, null, 100, 100, 100, 1);
insert into T_PRODUCT_CHARGING_POINT (id, name, supplier_id, status, product_id, province_id, city_id, remark, face_amount, pay_amount, delivery_amount, charging_type)
values (1098, '翼光天天跑得快-记牌器7天', 1009, 1, 1009, null, null, null, 600, 600, 600, 1);
insert into T_PRODUCT_CHARGING_POINT (id, name, supplier_id, status, product_id, province_id, city_id, remark, face_amount, pay_amount, delivery_amount, charging_type)
values (1099, '翼光天天跑得快-记牌器2天', 1009, 1, 1009, null, null, null, 200, 200, 200, 1);

insert into T_POINT_CHANNEL_RELATION (id, point_id, channel_id, status, remark)
values (1014, 1014, 2, 1, null);
insert into T_POINT_CHANNEL_RELATION (id, point_id, channel_id, status, remark)
values (1015, 1015, 2, 1, null);
insert into T_POINT_CHANNEL_RELATION (id, point_id, channel_id, status, remark)
values (1016, 1016, 2, 1, null);
insert into T_POINT_CHANNEL_RELATION (id, point_id, channel_id, status, remark)
values (1017, 1017, 2, 1, null);
insert into T_POINT_CHANNEL_RELATION (id, point_id, channel_id, status, remark)
values (1018, 1018, 2, 1, null);
insert into T_POINT_CHANNEL_RELATION (id, point_id, channel_id, status, remark)
values (1019, 1019, 2, 1, null);
insert into T_POINT_CHANNEL_RELATION (id, point_id, channel_id, status, remark)
values (1020, 1020, 2, 1, null);
insert into T_POINT_CHANNEL_RELATION (id, point_id, channel_id, status, remark)
values (1021, 1021, 2, 1, null);
insert into T_POINT_CHANNEL_RELATION (id, point_id, channel_id, status, remark)
values (1022, 1023, 2, 1, null);
insert into T_POINT_CHANNEL_RELATION (id, point_id, channel_id, status, remark)
values (1024, 1024, 2, 1, null);
insert into T_POINT_CHANNEL_RELATION (id, point_id, channel_id, status, remark)
values (1025, 1025, 2, 1, null);
insert into T_POINT_CHANNEL_RELATION (id, point_id, channel_id, status, remark)
values (1026, 1026, 2, 1, null);
insert into T_POINT_CHANNEL_RELATION (id, point_id, channel_id, status, remark)
values (1027, 1027, 2, 1, null);
insert into T_POINT_CHANNEL_RELATION (id, point_id, channel_id, status, remark)
values (1028, 1028, 2, 1, null);
insert into T_POINT_CHANNEL_RELATION (id, point_id, channel_id, status, remark)
values (1029, 1029, 2, 1, null);
insert into T_POINT_CHANNEL_RELATION (id, point_id, channel_id, status, remark)
values (1030, 1030, 2, 1, null);
insert into T_POINT_CHANNEL_RELATION (id, point_id, channel_id, status, remark)
values (1031, 1031, 2, 1, null);
insert into T_POINT_CHANNEL_RELATION (id, point_id, channel_id, status, remark)
values (1032, 1032, 2, 1, null);
insert into T_POINT_CHANNEL_RELATION (id, point_id, channel_id, status, remark)
values (1033, 1033, 2, 1, null);
insert into T_POINT_CHANNEL_RELATION (id, point_id, channel_id, status, remark)
values (1034, 1034, 2, 1, null);
insert into T_POINT_CHANNEL_RELATION (id, point_id, channel_id, status, remark)
values (1035, 1035, 2, 1, null);
insert into T_POINT_CHANNEL_RELATION (id, point_id, channel_id, status, remark)
values (1036, 1036, 2, 1, null);
insert into T_POINT_CHANNEL_RELATION (id, point_id, channel_id, status, remark)
values (1037, 1037, 2, 1, null);
insert into T_POINT_CHANNEL_RELATION (id, point_id, channel_id, status, remark)
values (1038, 1038, 2, 1, null);
insert into T_POINT_CHANNEL_RELATION (id, point_id, channel_id, status, remark)
values (1039, 1039, 2, 1, null);
insert into T_POINT_CHANNEL_RELATION (id, point_id, channel_id, status, remark)
values (1040, 1040, 2, 1, null);
insert into T_POINT_CHANNEL_RELATION (id, point_id, channel_id, status, remark)
values (1041, 1041, 2, 1, null);
insert into T_POINT_CHANNEL_RELATION (id, point_id, channel_id, status, remark)
values (1042, 1042, 2, 1, null);
insert into T_POINT_CHANNEL_RELATION (id, point_id, channel_id, status, remark)
values (1043, 1043, 2, 1, null);
insert into T_POINT_CHANNEL_RELATION (id, point_id, channel_id, status, remark)
values (1044, 1044, 2, 1, null);
insert into T_POINT_CHANNEL_RELATION (id, point_id, channel_id, status, remark)
values (1045, 1045, 2, 1, null);
insert into T_POINT_CHANNEL_RELATION (id, point_id, channel_id, status, remark)
values (1046, 1046, 2, 1, null);
insert into T_POINT_CHANNEL_RELATION (id, point_id, channel_id, status, remark)
values (1047, 1047, 2, 1, null);
insert into T_POINT_CHANNEL_RELATION (id, point_id, channel_id, status, remark)
values (1048, 1048, 2, 1, null);
insert into T_POINT_CHANNEL_RELATION (id, point_id, channel_id, status, remark)
values (1049, 1049, 2, 1, null);
insert into T_POINT_CHANNEL_RELATION (id, point_id, channel_id, status, remark)
values (1050, 1050, 2, 1, null);
insert into T_POINT_CHANNEL_RELATION (id, point_id, channel_id, status, remark)
values (1051, 1051, 2, 1, null);
insert into T_POINT_CHANNEL_RELATION (id, point_id, channel_id, status, remark)
values (1052, 1052, 2, 1, null);
insert into T_POINT_CHANNEL_RELATION (id, point_id, channel_id, status, remark)
values (1053, 1053, 2, 1, null);
insert into T_POINT_CHANNEL_RELATION (id, point_id, channel_id, status, remark)
values (1054, 1054, 2, 1, null);
insert into T_POINT_CHANNEL_RELATION (id, point_id, channel_id, status, remark)
values (1055, 1055, 2, 1, null);
insert into T_POINT_CHANNEL_RELATION (id, point_id, channel_id, status, remark)
values (1056, 1056, 2, 1, null);
insert into T_POINT_CHANNEL_RELATION (id, point_id, channel_id, status, remark)
values (1057, 1057, 2, 1, null);
insert into T_POINT_CHANNEL_RELATION (id, point_id, channel_id, status, remark)
values (1058, 1058, 2, 1, null);
insert into T_POINT_CHANNEL_RELATION (id, point_id, channel_id, status, remark)
values (1059, 1059, 2, 1, null);
insert into T_POINT_CHANNEL_RELATION (id, point_id, channel_id, status, remark)
values (1060, 1060, 2, 1, null);
insert into T_POINT_CHANNEL_RELATION (id, point_id, channel_id, status, remark)
values (1061, 1061, 2, 1, null);
insert into T_POINT_CHANNEL_RELATION (id, point_id, channel_id, status, remark)
values (1062, 1062, 2, 1, null);
insert into T_POINT_CHANNEL_RELATION (id, point_id, channel_id, status, remark)
values (1063, 1063, 2, 1, null);
insert into T_POINT_CHANNEL_RELATION (id, point_id, channel_id, status, remark)
values (1064, 1064, 2, 1, null);
insert into T_POINT_CHANNEL_RELATION (id, point_id, channel_id, status, remark)
values (1065, 1065, 2, 1, null);
insert into T_POINT_CHANNEL_RELATION (id, point_id, channel_id, status, remark)
values (1066, 1066, 2, 1, null);
insert into T_POINT_CHANNEL_RELATION (id, point_id, channel_id, status, remark)
values (1067, 1067, 2, 1, null);
insert into T_POINT_CHANNEL_RELATION (id, point_id, channel_id, status, remark)
values (1068, 1068, 2, 1, null);
insert into T_POINT_CHANNEL_RELATION (id, point_id, channel_id, status, remark)
values (1069, 1069, 2, 1, null);
insert into T_POINT_CHANNEL_RELATION (id, point_id, channel_id, status, remark)
values (1070, 1070, 2, 1, null);
insert into T_POINT_CHANNEL_RELATION (id, point_id, channel_id, status, remark)
values (1071, 1071, 2, 1, null);
insert into T_POINT_CHANNEL_RELATION (id, point_id, channel_id, status, remark)
values (1072, 1072, 2, 1, null);
insert into T_POINT_CHANNEL_RELATION (id, point_id, channel_id, status, remark)
values (1073, 1073, 2, 1, null);
insert into T_POINT_CHANNEL_RELATION (id, point_id, channel_id, status, remark)
values (1074, 1074, 2, 1, null);
insert into T_POINT_CHANNEL_RELATION (id, point_id, channel_id, status, remark)
values (1075, 1075, 2, 1, null);
insert into T_POINT_CHANNEL_RELATION (id, point_id, channel_id, status, remark)
values (1076, 1076, 2, 1, null);
insert into T_POINT_CHANNEL_RELATION (id, point_id, channel_id, status, remark)
values (1077, 1077, 2, 1, null);
insert into T_POINT_CHANNEL_RELATION (id, point_id, channel_id, status, remark)
values (1078, 1078, 2, 1, null);
insert into T_POINT_CHANNEL_RELATION (id, point_id, channel_id, status, remark)
values (1079, 1079, 2, 1, null);
insert into T_POINT_CHANNEL_RELATION (id, point_id, channel_id, status, remark)
values (1080, 1080, 2, 1, null);
insert into T_POINT_CHANNEL_RELATION (id, point_id, channel_id, status, remark)
values (1081, 1081, 2, 1, null);
insert into T_POINT_CHANNEL_RELATION (id, point_id, channel_id, status, remark)
values (1082, 1082, 2, 1, null);
insert into T_POINT_CHANNEL_RELATION (id, point_id, channel_id, status, remark)
values (1083, 1083, 2, 1, null);
insert into T_POINT_CHANNEL_RELATION (id, point_id, channel_id, status, remark)
values (1084, 1084, 2, 1, null);
insert into T_POINT_CHANNEL_RELATION (id, point_id, channel_id, status, remark)
values (1085, 1085, 2, 1, null);
insert into T_POINT_CHANNEL_RELATION (id, point_id, channel_id, status, remark)
values (1086, 1086, 2, 1, null);
insert into T_POINT_CHANNEL_RELATION (id, point_id, channel_id, status, remark)
values (1087, 1087, 2, 1, null);
insert into T_POINT_CHANNEL_RELATION (id, point_id, channel_id, status, remark)
values (1088, 1088, 2, 1, null);
insert into T_POINT_CHANNEL_RELATION (id, point_id, channel_id, status, remark)
values (1089, 1089, 2, 1, null);
insert into T_POINT_CHANNEL_RELATION (id, point_id, channel_id, status, remark)
values (1090, 1090, 2, 1, null);
insert into T_POINT_CHANNEL_RELATION (id, point_id, channel_id, status, remark)
values (1091, 1091, 2, 1, null);
insert into T_POINT_CHANNEL_RELATION (id, point_id, channel_id, status, remark)
values (1092, 1092, 2, 1, null);
insert into T_POINT_CHANNEL_RELATION (id, point_id, channel_id, status, remark)
values (1093, 1093, 2, 1, null);
insert into T_POINT_CHANNEL_RELATION (id, point_id, channel_id, status, remark)
values (1094, 1094, 2, 1, null);
insert into T_POINT_CHANNEL_RELATION (id, point_id, channel_id, status, remark)
values (1095, 1095, 2, 1, null);
insert into T_POINT_CHANNEL_RELATION (id, point_id, channel_id, status, remark)
values (1096, 1096, 2, 1, null);
insert into T_POINT_CHANNEL_RELATION (id, point_id, channel_id, status, remark)
values (1097, 1097, 2, 1, null);
insert into T_POINT_CHANNEL_RELATION (id, point_id, channel_id, status, remark)
values (1098, 1098, 2, 1, null);
insert into T_POINT_CHANNEL_RELATION (id, point_id, channel_id, status, remark)
values (1099, 1099, 2, 1, null);
commit;

update t_product_charging_point t set t.charging_code = '1000'||t.supplier_id||'1000'||t.id;
commit;
delete from T_DATASOURCE t where t.code = 'SUPPLIER';
delete from T_DATASOURCE t where t.code = 'PRODUCT';
insert into T_DATASOURCE (id, name, type, status, remark, option_sql, parent_code, code)
values (15, '产品', 2, 1, null, 'select t.id as optionId,t.name as optionLabel from t_product t where 1=1', 'merchant_id', 'PRODUCT');
insert into T_DATASOURCE (id, name, type, status, remark, option_sql, parent_code, code)
values (33, '运营商', 2, 1, null, 'select t.id as optionId,t.carrier_name as optionLabel from t_carrier_info t where 1=1', '', 'CARRIER');
commit;
ALTER TABLE t_product_batch RENAME COLUMN supplier_id TO merchant_id;
ALTER TABLE t_extract_record drop column supplier_id  ;
ALTER TABLE t_product_charging_point RENAME COLUMN supplier_id TO merchant_id;
ALTER TABLE t_product RENAME COLUMN supplier_id TO merchant_id;
ALTER TABLE t_merchant_order RENAME COLUMN supplier_id TO merchant_id;

ALTER TABLE t_merchant_order add (carrier_id NUMBER(20));
ALTER TABLE t_merchant_order add (merchant_order_id VARCHAR2(200));
update t_merchant_order t set t.carrier_id = t.channel_id, t.merchant_order_id = t.order_id;

ALTER TABLE t_merchant_order add (channel_type NUMBER(1));
ALTER TABLE t_merchant_order add (subject VARCHAR2(200));
ALTER TABLE t_merchant_order add (description VARCHAR2(200));
ALTER TABLE t_merchant_order add (notify_status NUMBER(1));
ALTER TABLE t_merchant_order add (notify_url VARCHAR2(200));
ALTER TABLE t_merchant_order add (show_url VARCHAR2(200));
ALTER TABLE t_merchant_order add (channel_trade_no VARCHAR2(200));
ALTER TABLE t_merchant_order add (smscode VARCHAR2(20));
ALTER TABLE t_merchant_order add (order_timestamp VARCHAR2(200));
ALTER TABLE t_merchant_order add (pay_timestamp VARCHAR2(200));
update t_merchant_order t set t.channel_type = (select channel_type from t_payment_order p where p.merchant_order_id = t.order_id);
update t_merchant_order t set t.notify_status = (select notify_status from t_payment_order p where p.merchant_order_id = t.order_id);


ALTER TABLE t_merchant_order drop column notify_url  ;
ALTER TABLE t_merchant_order drop column show_url  ;
ALTER TABLE t_merchant_order add (notify_url VARCHAR2(1000));
ALTER TABLE t_merchant_order add (show_url VARCHAR2(1000));
update t_merchant_order t set t.notify_url = (select notify_url from t_payment_order p where p.merchant_order_id = t.order_id);