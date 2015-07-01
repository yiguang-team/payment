create sequence SEQ_USER
minvalue 1
maxvalue 9999999999999999999999999999
start with 2000
increment by 1
cache 20;

create sequence SEQ_USER_INFO
minvalue 1
maxvalue 9999999999999999999999999999
start with 2000
increment by 1
cache 20;

create sequence SEQ_ROLE
minvalue 1
maxvalue 9999999999999999999999999999
start with 2000
increment by 1
cache 20;

create sequence SEQ_MENU
minvalue 1
maxvalue 9999999999999999999999999999
start with 2000
increment by 1
cache 20;

create sequence SEQ_ROLE_MENU
minvalue 1
maxvalue 9999999999999999999999999999
start with 2000
increment by 1
cache 20;

create sequence SEQ_ROLE_USER
minvalue 1
maxvalue 9999999999999999999999999999
start with 2000
increment by 1
cache 20;

create table T_USER
(
  ID            NUMBER(20) not null,
  USERNAME      VARCHAR2(200) not null,
  PASSWORD      VARCHAR2(200) ,
  STATUS        NUMBER(1) not null,
  ISLOCK        NUMBER(1) not null,
  REMARK        VARCHAR2(200),
  CREATE_TIME   DATE ,
  constraint PK_USER primary key (ID)
);
create table T_USER_INFO
(
  ID            NUMBER(20) not null,
  USER_ID       NUMBER(20) not null,
  NAME          VARCHAR2(200) not null,
  EMAIL         VARCHAR2(200) not null,
  MOBILE        VARCHAR2(200) not null,
  constraint PK_USER_INFO primary key (ID)
);
create table T_ROLE
(
  ID            NUMBER(20) not null,
  ROLE_NAME     VARCHAR2(200) not null,
  STATUS        NUMBER(1) not null,
  REMARK        VARCHAR2(200),
  CREATE_TIME   DATE ,
  constraint PK_ROLE primary key (ID)
);

create table T_MENU
(
  ID            NUMBER(20) not null,
  MENU_NAME     VARCHAR2(200) not null,
  DISPLAY_ORDER NUMBER(2) not null,
  PARENT_ID     NUMBER(20) not null,
  MENU_LEVEL    NUMBER(2) not null,
  SUB_SYSTEM    VARCHAR2(200),
  SUB_MODULE    VARCHAR2(200),
  URL           VARCHAR2(200),
  STATUS        NUMBER(1) not null,
  REMARK        VARCHAR2(200),
  CREATE_TIME   DATE ,
  constraint PK_MENU primary key (ID)
);

create table T_ROLE_MENU
(
  ID            NUMBER(20) not null,
  ROLE_ID       NUMBER(20) not null,
  MENU_ID       NUMBER(20) not null,
  STATUS        NUMBER(1) not null,
  constraint PK_ROLE_MENU primary key (ID)
);


create table T_ROLE_USER
(
  ID            NUMBER(20) not null,
  ROLE_ID       NUMBER(20) not null,
  USER_ID       NUMBER(20) not null,
  STATUS        NUMBER(1) not null,
  constraint PK_ROLE_USER primary key (ID)
);

insert into T_USER (id, username, password, status, islock, remark, create_time)
values (1581, 'admin', '8286fa77574a3d62acd3f764559d2ba1', 1, 1, null, to_date('18-05-2015', 'dd-mm-yyyy'));
commit;

insert into T_MENU (id, menu_name, display_order, parent_id, menu_level, sub_system, sub_module, url, status, remark, create_time)
values (1, '订单查询', 1, 0, 1, null, null, null, 1, null, to_date('20-05-2015', 'dd-mm-yyyy'));
insert into T_MENU (id, menu_name, display_order, parent_id, menu_level, sub_system, sub_module, url, status, remark, create_time)
values (2, '商品订单', 1, 1, 2, null, null, 'mall/order/merchantOrderList', 1, null, to_date('20-05-2015', 'dd-mm-yyyy'));
insert into T_MENU (id, menu_name, display_order, parent_id, menu_level, sub_system, sub_module, url, status, remark, create_time)
values (3, '卡库管理', 2, 0, 1, null, null, null, 1, null, to_date('20-05-2015', 'dd-mm-yyyy'));
insert into T_MENU (id, menu_name, display_order, parent_id, menu_level, sub_system, sub_module, url, status, remark, create_time)
values (4, '商品导入', 1, 3, 2, null, null, 'depot/depot/toImportProduct', 1, null, to_date('20-05-2015', 'dd-mm-yyyy'));
insert into T_MENU (id, menu_name, display_order, parent_id, menu_level, sub_system, sub_module, url, status, remark, create_time)
values (5, '商品列表', 2, 3, 2, null, null, 'depot/depot/depotList', 1, null, to_date('20-05-2015', 'dd-mm-yyyy'));
insert into T_MENU (id, menu_name, display_order, parent_id, menu_level, sub_system, sub_module, url, status, remark, create_time)
values (6, '商品批次', 3, 3, 2, null, null, 'depot/batch/batchList', 1, null, to_date('20-05-2015', 'dd-mm-yyyy'));
insert into T_MENU (id, menu_name, display_order, parent_id, menu_level, sub_system, sub_module, url, status, remark, create_time)
values (7, '出库记录', 4, 3, 2, null, null, 'depot/depot/pickUpRecordList', 1, null, to_date('20-05-2015', 'dd-mm-yyyy'));
insert into T_MENU (id, menu_name, display_order, parent_id, menu_level, sub_system, sub_module, url, status, remark, create_time)
values (8, '业务管理', 3, 0, 1, null, null, null, 1, null, to_date('20-05-2015', 'dd-mm-yyyy'));
insert into T_MENU (id, menu_name, display_order, parent_id, menu_level, sub_system, sub_module, url, status, remark, create_time)
values (9, '支付渠道', 1, 8, 2, null, null, 'payment/management/channel/channelList', 1, null, to_date('20-05-2015', 'dd-mm-yyyy'));
insert into T_MENU (id, menu_name, display_order, parent_id, menu_level, sub_system, sub_module, url, status, remark, create_time)
values (10, '渠道计费编码', 2, 8, 2, null, null, 'payment/management/channel/chargingCodeList', 1, null, to_date('20-05-2015', 'dd-mm-yyyy'));
insert into T_MENU (id, menu_name, display_order, parent_id, menu_level, sub_system, sub_module, url, status, remark, create_time)
values (11, '商户', 3, 8, 2, null, null, 'payment/management/merchant/merchantList', 1, null, to_date('20-05-2015', 'dd-mm-yyyy'));
insert into T_MENU (id, menu_name, display_order, parent_id, menu_level, sub_system, sub_module, url, status, remark, create_time)
values (12, '计费点', 4, 8, 2, null, null, 'mall/management/point/pointList', 1, null, to_date('20-05-2015', 'dd-mm-yyyy'));
insert into T_MENU (id, menu_name, display_order, parent_id, menu_level, sub_system, sub_module, url, status, remark, create_time)
values (13, '产品', 5, 8, 2, null, null, 'mall/management/product/productList', 1, null, to_date('20-05-2015', 'dd-mm-yyyy'));
insert into T_MENU (id, menu_name, display_order, parent_id, menu_level, sub_system, sub_module, url, status, remark, create_time)
values (14, '认证管理', 4, 0, 1, null, null, null, 1, null, to_date('20-05-2015', 'dd-mm-yyyy'));
insert into T_MENU (id, menu_name, display_order, parent_id, menu_level, sub_system, sub_module, url, status, remark, create_time)
values (15, '用户管理', 1, 14, 2, null, null, 'identity/user/userList', 1, null, to_date('20-05-2015', 'dd-mm-yyyy'));
insert into T_MENU (id, menu_name, display_order, parent_id, menu_level, sub_system, sub_module, url, status, remark, create_time)
values (16, '角色管理', 2, 14, 2, null, null, 'identity/role/roleList', 1, null, to_date('20-05-2015', 'dd-mm-yyyy'));
insert into T_MENU (id, menu_name, display_order, parent_id, menu_level, sub_system, sub_module, url, status, remark, create_time)
values (17, '报表管理', 5, 0, 1, null, null, null, 1, null, to_date('20-05-2015', 'dd-mm-yyyy'));
insert into T_MENU (id, menu_name, display_order, parent_id, menu_level, sub_system, sub_module, url, status, remark, create_time)
values (18, '卡库报表', 1, 17, 2, null, null, 'depot/report/depotReportList', 1, null, to_date('20-05-2015', 'dd-mm-yyyy'));
insert into T_MENU (id, menu_name, display_order, parent_id, menu_level, sub_system, sub_module, url, status, remark, create_time)
values (19, '商品报表', 2, 17, 2, null, null, 'mall/report/productReportList', 1, null, to_date('20-05-2015', 'dd-mm-yyyy'));
insert into T_MENU (id, menu_name, display_order, parent_id, menu_level, sub_system, sub_module, url, status, remark, create_time)
values (20, '人工提卡', 3, 3, 2, null, null, 'depot/depot/pickUpCarkManually', 1, null, to_date('20-05-2015', 'dd-mm-yyyy'));
insert into T_MENU (id, menu_name, display_order, parent_id, menu_level, sub_system, sub_module, url, status, remark, create_time)
values (21, '风控管理', 6, 0, 1, null, null, null, 1, null, to_date('20-05-2015', 'dd-mm-yyyy'));
insert into T_MENU (id, menu_name, display_order, parent_id, menu_level, sub_system, sub_module, url, status, remark, create_time)
values (22, '基本限制', 1, 21, 2, null, null, 'payment/risk/basicRule/basicRuleList', 1, null, to_date('20-05-2015', 'dd-mm-yyyy'));
insert into T_MENU (id, menu_name, display_order, parent_id, menu_level, sub_system, sub_module, url, status, remark, create_time)
values (25, '商户互斥', 4, 21, 2, null, null, 'payment/risk/merchantRejection/merchantRejectionList', 1, null, to_date('20-05-2015', 'dd-mm-yyyy'));
insert into T_MENU (id, menu_name, display_order, parent_id, menu_level, sub_system, sub_module, url, status, remark, create_time)
values (26, '黑名单', 5, 21, 2, null, null, 'payment/risk/blackList/blackList', 1, null, to_date('20-05-2015', 'dd-mm-yyyy'));
insert into T_MENU (id, menu_name, display_order, parent_id, menu_level, sub_system, sub_module, url, status, remark, create_time)
values (27, '白名单', 6, 21, 2, null, null, 'payment/risk/whiteList/whiteList', 1, null, to_date('20-05-2015', 'dd-mm-yyyy'));
insert into T_MENU (id, menu_name, display_order, parent_id, menu_level, sub_system, sub_module, url, status, remark, create_time)
values (28, '日志管理', 7, 0, 1, null, null, null, 1, null, to_date('20-05-2015', 'dd-mm-yyyy'));
insert into T_MENU (id, menu_name, display_order, parent_id, menu_level, sub_system, sub_module, url, status, remark, create_time)
values (29, '登录日志', 1, 28, 2, null, null, 'logging/loginAndLogoutLog/showLogList', 1, null, to_date('20-05-2015', 'dd-mm-yyyy'));
insert into T_MENU (id, menu_name, display_order, parent_id, menu_level, sub_system, sub_module, url, status, remark, create_time)
values (30, '操作日志', 2, 28, 2, null, null, 'logging/operationLog/showLogList', 1, null, to_date('20-05-2015', 'dd-mm-yyyy'));
insert into T_MENU (id, menu_name, display_order, parent_id, menu_level, sub_system, sub_module, url, status, remark, create_time)
values (31, '商户操作', 8, 0, 1, null, null, null, 1, null, to_date('20-05-2015', 'dd-mm-yyyy'));
insert into T_MENU (id, menu_name, display_order, parent_id, menu_level, sub_system, sub_module, url, status, remark, create_time)
values (32, '提取短信', 1, 31, 2, null, null, 'merchantOperate/getMessageList', 1, null, to_date('20-05-2015', 'dd-mm-yyyy'));
insert into T_MENU (id, menu_name, display_order, parent_id, menu_level, sub_system, sub_module, url, status, remark, create_time)
values (33, '查看订单', 2, 31, 2, null, null, 'merchantOperate/viewOrder', 1, null, to_date('20-05-2015', 'dd-mm-yyyy'));
insert into T_MENU (id, menu_name, display_order, parent_id, menu_level, sub_system, sub_module, url, status, remark, create_time)
values (34, '渠道参数', 6, 8, 2, null, null, 'payment/management/channelParms/channelParmsList', 1, null, to_date('02-06-2015', 'dd-mm-yyyy'));
commit;

insert into T_ROLE (id, role_name, status, remark, create_time)
values (1, '超级管理员', 1, null, to_date('20-05-2015', 'dd-mm-yyyy'));
insert into T_ROLE (id, role_name, status, remark, create_time)
values (2, '商户管理员', 1, null, to_date('26-05-2015', 'dd-mm-yyyy'));
commit;

insert into T_ROLE_MENU (id, role_id, menu_id, status)
values (1, 1, 1, 1);
insert into T_ROLE_MENU (id, role_id, menu_id, status)
values (2, 1, 2, 1);
insert into T_ROLE_MENU (id, role_id, menu_id, status)
values (3, 1, 3, 1);
insert into T_ROLE_MENU (id, role_id, menu_id, status)
values (4, 1, 4, 1);
insert into T_ROLE_MENU (id, role_id, menu_id, status)
values (5, 1, 5, 1);
insert into T_ROLE_MENU (id, role_id, menu_id, status)
values (6, 1, 6, 1);
insert into T_ROLE_MENU (id, role_id, menu_id, status)
values (7, 1, 7, 1);
insert into T_ROLE_MENU (id, role_id, menu_id, status)
values (8, 1, 8, 1);
insert into T_ROLE_MENU (id, role_id, menu_id, status)
values (9, 1, 9, 1);
insert into T_ROLE_MENU (id, role_id, menu_id, status)
values (10, 1, 10, 1);
insert into T_ROLE_MENU (id, role_id, menu_id, status)
values (11, 1, 11, 1);
insert into T_ROLE_MENU (id, role_id, menu_id, status)
values (12, 1, 12, 1);
insert into T_ROLE_MENU (id, role_id, menu_id, status)
values (13, 1, 13, 1);
insert into T_ROLE_MENU (id, role_id, menu_id, status)
values (14, 1, 14, 1);
insert into T_ROLE_MENU (id, role_id, menu_id, status)
values (15, 1, 15, 1);
insert into T_ROLE_MENU (id, role_id, menu_id, status)
values (16, 1, 16, 1);
insert into T_ROLE_MENU (id, role_id, menu_id, status)
values (17, 1, 17, 1);
insert into T_ROLE_MENU (id, role_id, menu_id, status)
values (18, 1, 18, 1);
insert into T_ROLE_MENU (id, role_id, menu_id, status)
values (19, 1, 19, 1);
insert into T_ROLE_MENU (id, role_id, menu_id, status)
values (20, 1, 20, 1);
insert into T_ROLE_MENU (id, role_id, menu_id, status)
values (21, 1, 21, 1);
insert into T_ROLE_MENU (id, role_id, menu_id, status)
values (22, 1, 22, 1);
insert into T_ROLE_MENU (id, role_id, menu_id, status)
values (25, 1, 25, 1);
insert into T_ROLE_MENU (id, role_id, menu_id, status)
values (26, 1, 26, 1);
insert into T_ROLE_MENU (id, role_id, menu_id, status)
values (27, 1, 27, 1);
insert into T_ROLE_MENU (id, role_id, menu_id, status)
values (28, 1, 28, 1);
insert into T_ROLE_MENU (id, role_id, menu_id, status)
values (29, 1, 29, 1);
insert into T_ROLE_MENU (id, role_id, menu_id, status)
values (30, 1, 30, 1);
insert into T_ROLE_MENU (id, role_id, menu_id, status)
values (31, 1, 31, 1);
insert into T_ROLE_MENU (id, role_id, menu_id, status)
values (32, 1, 32, 1);
insert into T_ROLE_MENU (id, role_id, menu_id, status)
values (33, 1, 33, 1);
commit;

insert into T_ROLE_USER (id, role_id, user_id, status)
values (1000, 1, 1581, 1);
commit;

alter table t_agent_merchant add(admin_user number(20) default 1581 not null);

insert into T_DATASOURCE (id, name, type, status, remark, option_sql, parent_code, code)
values (34, '角色', 2, 1, null, 'select t.id as optionId,t.role_name as optionLabel from t_role t where 1=1', null, 'ROLE');
insert into T_DATASOURCE (id, name, type, status, remark, option_sql, parent_code, code)
values (35, '锁定状态', 1, 1, null, null, null, 'IS_LOCK');
insert into T_DATASOURCE_OPTION (id, option_id, option_label, status, remark, data_source_code, parent_option_id, parent_datasource_code)
values (65, 1, '未锁定', 1, null, 'IS_LOCK', null, null);
insert into T_DATASOURCE_OPTION (id, option_id, option_label, status, remark, data_source_code, parent_option_id, parent_datasource_code)
values (66, 0, '已锁定', 1, null, 'IS_LOCK', null, null);
commit;
insert into T_DATASOURCE (id, name, type, status, remark, option_sql, parent_code, code)
values (36, '时间限制方式', 1, 1, null, null, null, 'RISK_TIME_TYPE');
insert into T_DATASOURCE_OPTION (id, option_id, option_label, status, remark, data_source_code, parent_option_id, parent_datasource_code)
values (67, 1, '时段限制', 1, null, 'RISK_TIME_TYPE', null, null);
insert into T_DATASOURCE_OPTION (id, option_id, option_label, status, remark, data_source_code, parent_option_id, parent_datasource_code)
values (68, 0, '单位限制', 1, null, 'RISK_TIME_TYPE', null, null);
insert into T_DATASOURCE_OPTION (id, option_id, option_label, status, remark, data_source_code, parent_option_id, parent_datasource_code)
values (56, 2, '相对限制', 1, null, 'RISK_TIME_TYPE', null, null);
commit;
insert into T_DATASOURCE (id, name, type, status, remark, option_sql, parent_code, code)
values (37, '风控限制方式', 1, 1, null, null, null, 'RISK_LIMIT_TYPE');
insert into T_DATASOURCE_OPTION (id, option_id, option_label, status, remark, data_source_code, parent_option_id, parent_datasource_code)
values (69, 0, '成交金额限制', 1, null, 'RISK_LIMIT_TYPE', null, null);
insert into T_DATASOURCE_OPTION (id, option_id, option_label, status, remark, data_source_code, parent_option_id, parent_datasource_code)
values (70, 1, '成交笔数限制', 1, null, 'RISK_LIMIT_TYPE', null, null);
commit;
insert into T_DATASOURCE (id, name, type, status, remark, option_sql, parent_code, code)
values (38, '风控条件选择方式', 1, 1, null, null, null, 'RISK_SELECT_TYPE');
insert into T_DATASOURCE_OPTION (id, option_id, option_label, status, remark, data_source_code, parent_option_id, parent_datasource_code)
values (71, -9, '不限', 1, null, 'RISK_SELECT_TYPE', null, null);
insert into T_DATASOURCE_OPTION (id, option_id, option_label, status, remark, data_source_code, parent_option_id, parent_datasource_code)
values (72, -8, '当前', 1, null, 'RISK_SELECT_TYPE', null, null);
insert into T_DATASOURCE_OPTION (id, option_id, option_label, status, remark, data_source_code, parent_option_id, parent_datasource_code)
values (73, -7, '指定', 1, null, 'RISK_SELECT_TYPE', null, null);
commit;
delete from T_DATASOURCE where code='TIME';
delete from T_DATASOURCE_OPTION where data_source_code='TIME';
commit;
insert into T_DATASOURCE (id, name, type, status, remark, option_sql, parent_code, code)
values (27, '时间单位', 1, 1, null, null, null, 'TIME_UNIT');
insert into T_DATASOURCE_OPTION (id, option_id, option_label, status, remark, data_source_code, parent_option_id, parent_datasource_code)
values (51, 1, '小时', 1, null, 'TIME_UNIT', null, null);
insert into T_DATASOURCE_OPTION (id, option_id, option_label, status, remark, data_source_code, parent_option_id, parent_datasource_code)
values (52, 2, '天', 1, null, 'TIME_UNIT', null, null);
insert into T_DATASOURCE_OPTION (id, option_id, option_label, status, remark, data_source_code, parent_option_id, parent_datasource_code)
values (53, 3, '月', 1, null, 'TIME_UNIT', null, null);
commit;

insert into T_DATASOURCE (id, name, type, status, remark, option_sql, parent_code, code)
values (40, '风控动作', 1, 1, null, null, null, 'RISK_ACTION');
insert into T_DATASOURCE_OPTION (id, option_id, option_label, status, remark, data_source_code, parent_option_id, parent_datasource_code)
values (76, 0, '禁止交易', 1, null, 'RISK_ACTION', null, null);
insert into T_DATASOURCE_OPTION (id, option_id, option_label, status, remark, data_source_code, parent_option_id, parent_datasource_code)
values (77, 1, '风险提示', 1, null, 'RISK_ACTION', null, null);

commit;
insert into T_DATASOURCE (id, name, type, status, remark, option_sql, parent_code, code)
values (41, '用户信息', 2, 1, null, 'select t.id as optionId,t.username as optionLabel from t_user t where 1=1', null, 'USER');

commit;
create table T_BASIC_RULE
(
  ID            NUMBER(20) not null,
  TIME_TYPE     NUMBER(1),
  TIME_UNIT     NUMBER(1),
  START_UNIT    NUMBER(4),
  END_UNIT      NUMBER(4),
  START_TIME    DATE ,
  END_TIME      DATE ,
  RELATIVE_UNIT NUMBER(4),
  RELATIVE_VALUE NUMBER(4),
  LIMIT_TYPE    NUMBER(1),
  VOLUME        NUMBER(20),
  CHANNEL_ID    NUMBER(20),
  PROVINCE_ID   VARCHAR2(20),
  CITY_ID       VARCHAR2(20),
  MERCHANT_ID   NUMBER(20),
  PRODUCT_ID    NUMBER(20),
  POINT_ID      NUMBER(20),
  STATUS        NUMBER(1) not null,
  MOBILE        VARCHAR2(100),
  IP            VARCHAR2(100),
  USERNAME      VARCHAR2(100),
  ACTION        NUMBER(1),
  REMARK        VARCHAR2(200),
  constraint PK_BASIC_RULE primary key (ID)
);

create sequence SEQ_BASIC_RULE
increment by 1
start with 2000
 minvalue 2000;
 
create sequence SEQ_PRIVILEGE
minvalue 1
maxvalue 9999999999999999999999999999
start with 2000
increment by 1
cache 20;

create sequence SEQ_ROLE_PRIVILEGE
minvalue 1
maxvalue 9999999999999999999999999999
start with 2000
increment by 1
cache 20;

create sequence SEQ_CHANNEL_PARMS
minvalue 1
maxvalue 9999999999999999999999999999
start with 2000
increment by 1
cache 20;

create table T_PRIVILEGE
(
  id              NUMBER(20) not null,
  name            VARCHAR2(200) not null,
  parent_id       NUMBER(20) not null,
  url             VARCHAR2(200),
  privilege_level NUMBER(2) not null,
  display_order   NUMBER(2) not null,
  status          NUMBER(1) not null,
  remark          VARCHAR2(200)
)
;
alter table T_PRIVILEGE
  add constraint PK_PRIVILEGE primary key (ID);

prompt Creating T_ROLE_PRIVILEGE...
create table T_ROLE_PRIVILEGE
(
  id           NUMBER(20) not null,
  role_id      NUMBER(20) not null,
  privilege_id NUMBER(20) not null,
  status       NUMBER(1) not null,
  remark       VARCHAR2(200)
)
;
alter table T_ROLE_PRIVILEGE
  add constraint PK_ROLE_PRIVILEGE primary key (ID)
  disable;
  
create table T_CHANNEL_PARMS
(
  id         NUMBER(20) not null,
  channel_id NUMBER(20) not null,
  key        VARCHAR2(200) not null,
  value      VARCHAR2(200) not null,
  status     NUMBER(1) not null,
  remark     VARCHAR2(4000)
)
;
alter table T_CHANNEL_PARMS
  add constraint PK_T_CHANNEL_PARMS primary key (ID);
  
  
ALTER TABLE t_merchant_order add (charging_code VARCHAR2(200));
update t_merchant_order t set t.charging_code = (select p.charging_code from t_product_charging_point p where p.id = t.charging_point_id);
commit;

insert into T_AGENT_MERCHANT (id, name, status, key, remark, notify_url)
values (1010, '翼光商户8', 1, 'ca217f6038f0db63dce4552f0819ef48', null, 'http://192.168.1.22:8080/unipay/payCallbackFalseKJKASDKJQBDA20150602.json');
commit;
insert into T_PRODUCT (id, name, type, merchant_id, status, remark)
values (1010, '翼光商户8支付产品', 2, 1010, 1, null);
commit;
update T_AGENT_MERCHANT t set t.notify_url = 'http://192.168.1.22:8080/unipay/payCallbackFalseKJKASDKJQBDA20150602.json'
where t.id in (1007,1006,1005,1004,1003,1002,1001);
commit;


insert into T_AGENT_MERCHANT (id, name, status, key, remark, notify_url)
values (1011, '世坤远大', 1, '43c802069b46c70504f631306a2b9e5b', null, '');
commit;
insert into T_PRODUCT (id, name, type, merchant_id, status, remark)
values (1011, '世坤远大产品', 2, 1011, 1, null);
commit;

ALTER TABLE T_CARRIER_CHANNEL_RELATION add (SORT NUMBER(2));

insert into T_DATASOURCE (id, name, type, status, remark, option_sql, parent_code, code)
values (7, '名单类型', 1, 1, null, null, null, 'LIST_TYPE');
insert into T_DATASOURCE_OPTION (id, option_id, option_label, status, remark, data_source_code, parent_option_id, parent_datasource_code)
values (78, 1, '号码', 1, null, 'LIST_TYPE', null, null);
insert into T_DATASOURCE_OPTION (id, option_id, option_label, status, remark, data_source_code, parent_option_id, parent_datasource_code)
values (79, 2, '号段', 1, null, 'LIST_TYPE', null, null);
insert into T_DATASOURCE_OPTION (id, option_id, option_label, status, remark, data_source_code, parent_option_id, parent_datasource_code)
values (80, 3, 'IP', 1, null, 'LIST_TYPE', null, null);
insert into T_DATASOURCE_OPTION (id, option_id, option_label, status, remark, data_source_code, parent_option_id, parent_datasource_code)
values (81, 4, '账号', 1, null, 'LIST_TYPE', null, null);

insert into T_PRODUCT_CHARGING_POINT (id, name, merchant_id, status, product_id, province_id, city_id, remark, face_amount, pay_amount, delivery_amount, charging_type)
values (1100, '短代商户8-0.1元', 1010, 1, 1010, null, null, null, 10, 10, 10, 1);
insert into T_PRODUCT_CHARGING_POINT (id, name, merchant_id, status, product_id, province_id, city_id, remark, face_amount, pay_amount, delivery_amount, charging_type)
values (1101, '短代商户8-2元', 1010, 1, 1010, null, null, null, 200, 200, 200, 1);
insert into T_PRODUCT_CHARGING_POINT (id, name, merchant_id, status, product_id, province_id, city_id, remark, face_amount, pay_amount, delivery_amount, charging_type)
values (1102, '短代商户8-5元', 1010, 1, 1010, null, null, null, 500, 500, 500, 1);
insert into T_PRODUCT_CHARGING_POINT (id, name, merchant_id, status, product_id, province_id, city_id, remark, face_amount, pay_amount, delivery_amount, charging_type)
values (1103, '短代商户8-10元', 1010, 1, 1010, null, null, null, 1000, 1000, 1000, 1);
insert into T_PRODUCT_CHARGING_POINT (id, name, merchant_id, status, product_id, province_id, city_id, remark, face_amount, pay_amount, delivery_amount, charging_type)
values (1104, '短代商户8-20元', 1010, 1, 1010, null, null, null, 2000, 2000, 2000, 1);
insert into T_PRODUCT_CHARGING_POINT (id, name, merchant_id, status, product_id, province_id, city_id, remark, face_amount, pay_amount, delivery_amount, charging_type)
values (1105, '短代商户8-30元', 1010, 1, 1010, null, null, null, 3000, 3000, 3000, 1);
insert into T_PRODUCT_CHARGING_POINT (id, name, merchant_id, status, product_id, province_id, city_id, remark, face_amount, pay_amount, delivery_amount, charging_type)
values (1106, '短代商户8-40元', 1010, 1, 1010, null, null, null, 4000, 4000, 4000, 1);
insert into T_PRODUCT_CHARGING_POINT (id, name, merchant_id, status, product_id, province_id, city_id, remark, face_amount, pay_amount, delivery_amount, charging_type)
values (1107, '短代商户8-50元', 1010, 1, 1010, null, null, null, 5000, 5000, 5000, 1);
insert into T_PRODUCT_CHARGING_POINT (id, name, merchant_id, status, product_id, province_id, city_id, remark, face_amount, pay_amount, delivery_amount, charging_type)
values (1108, '短代商户8-60元', 1010, 1, 1010, null, null, null, 6000, 6000, 6000, 1);

insert into T_PRODUCT_CHARGING_POINT (id, name, merchant_id, status, product_id, province_id, city_id, remark, face_amount, pay_amount, delivery_amount, charging_type)
values (1109, '天下通30元', 1011, 1, 1011, null, null, null, 3000, 3000, 3000, 1);
insert into T_PRODUCT_CHARGING_POINT (id, name, merchant_id, status, product_id, province_id, city_id, remark, face_amount, pay_amount, delivery_amount, charging_type)
values (1110, '天下通15元', 1011, 1, 1011, null, null, null, 1500, 1500, 1500, 1);
insert into T_PRODUCT_CHARGING_POINT (id, name, merchant_id, status, product_id, province_id, city_id, remark, face_amount, pay_amount, delivery_amount, charging_type)
values (1111, '天宏30元', 1011, 1, 1011, null, null, null, 3000, 3000, 3000, 1);
insert into T_PRODUCT_CHARGING_POINT (id, name, merchant_id, status, product_id, province_id, city_id, remark, face_amount, pay_amount, delivery_amount, charging_type)
values (1112, '天宏15元', 1011, 1, 1011, null, null, null, 1500, 1500, 1500, 1);
insert into T_PRODUCT_CHARGING_POINT (id, name, merchant_id, status, product_id, province_id, city_id, remark, face_amount, pay_amount, delivery_amount, charging_type)
values (1113, '空中15元', 1011, 1, 1011, null, null, null, 1500, 1500, 1500, 1);
insert into T_PRODUCT_CHARGING_POINT (id, name, merchant_id, status, product_id, province_id, city_id, remark, face_amount, pay_amount, delivery_amount, charging_type)
values (1114, '盛大30元', 1011, 1, 1011, null, null, null, 3000, 3000, 3000, 1);
insert into T_PRODUCT_CHARGING_POINT (id, name, merchant_id, status, product_id, province_id, city_id, remark, face_amount, pay_amount, delivery_amount, charging_type)
values (1115, '盛大15元', 1011, 1, 1011, null, null, null, 1500, 1500, 1500, 1);
insert into T_PRODUCT_CHARGING_POINT (id, name, merchant_id, status, product_id, province_id, city_id, remark, face_amount, pay_amount, delivery_amount, charging_type)
values (1116, '盛大10元', 1011, 1, 1011, null, null, null, 1000, 1000, 1000, 1);
insert into T_PRODUCT_CHARGING_POINT (id, name, merchant_id, status, product_id, province_id, city_id, remark, face_amount, pay_amount, delivery_amount, charging_type)
values (1117, '骏网30元', 1011, 1, 1011, null, null, null, 3000, 3000, 3000, 1);
insert into T_PRODUCT_CHARGING_POINT (id, name, merchant_id, status, product_id, province_id, city_id, remark, face_amount, pay_amount, delivery_amount, charging_type)
values (1118, '骏网15元', 1011, 1, 1011, null, null, null, 1500, 1500, 1500, 1);
insert into T_PRODUCT_CHARGING_POINT (id, name, merchant_id, status, product_id, province_id, city_id, remark, face_amount, pay_amount, delivery_amount, charging_type)
values (1119, '骏网10元', 1011, 1, 1011, null, null, null, 1000, 1000, 1000, 1);
insert into T_PRODUCT_CHARGING_POINT (id, name, merchant_id, status, product_id, province_id, city_id, remark, face_amount, pay_amount, delivery_amount, charging_type)
values (1120, '世坤远大测试0.1元', 1011, 1, 1011, null, null, null, 10, 10, 10, 1);
insert into T_POINT_CHANNEL_RELATION (id, point_id, channel_id, status, remark)
values (1100, 1100, 2, 1, null);
insert into T_POINT_CHANNEL_RELATION (id, point_id, channel_id, status, remark)
values (1101, 1101, 2, 1, null);
insert into T_POINT_CHANNEL_RELATION (id, point_id, channel_id, status, remark)
values (1102, 1102, 2, 1, null);
insert into T_POINT_CHANNEL_RELATION (id, point_id, channel_id, status, remark)
values (1103, 1103, 2, 1, null);
insert into T_POINT_CHANNEL_RELATION (id, point_id, channel_id, status, remark)
values (1104, 1104, 2, 1, null);
insert into T_POINT_CHANNEL_RELATION (id, point_id, channel_id, status, remark)
values (1105, 1105, 2, 1, null);
insert into T_POINT_CHANNEL_RELATION (id, point_id, channel_id, status, remark)
values (1106, 1106, 2, 1, null);
insert into T_POINT_CHANNEL_RELATION (id, point_id, channel_id, status, remark)
values (1107, 1107, 2, 1, null);
insert into T_POINT_CHANNEL_RELATION (id, point_id, channel_id, status, remark)
values (1108, 1108, 2, 1, null);
insert into T_POINT_CHANNEL_RELATION (id, point_id, channel_id, status, remark)
values (1109, 1109, 2, 1, null);
insert into T_POINT_CHANNEL_RELATION (id, point_id, channel_id, status, remark)
values (1110, 1110, 2, 1, null);
insert into T_POINT_CHANNEL_RELATION (id, point_id, channel_id, status, remark)
values (1111, 1111, 2, 1, null);
insert into T_POINT_CHANNEL_RELATION (id, point_id, channel_id, status, remark)
values (1112, 1112, 2, 1, null);
insert into T_POINT_CHANNEL_RELATION (id, point_id, channel_id, status, remark)
values (1113, 1113, 2, 1, null);
insert into T_POINT_CHANNEL_RELATION (id, point_id, channel_id, status, remark)
values (1114, 1114, 2, 1, null);
insert into T_POINT_CHANNEL_RELATION (id, point_id, channel_id, status, remark)
values (1115, 1115, 2, 1, null);
insert into T_POINT_CHANNEL_RELATION (id, point_id, channel_id, status, remark)
values (1116, 1116, 2, 1, null);
insert into T_POINT_CHANNEL_RELATION (id, point_id, channel_id, status, remark)
values (1117, 1117, 2, 1, null);
insert into T_POINT_CHANNEL_RELATION (id, point_id, channel_id, status, remark)
values (1118, 1118, 2, 1, null);
insert into T_POINT_CHANNEL_RELATION (id, point_id, channel_id, status, remark)
values (1119, 1119, 2, 1, null);
insert into T_POINT_CHANNEL_RELATION (id, point_id, channel_id, status, remark)
values (1120, 1120, 2, 1, null);

update t_product_charging_point t set t.charging_code = '1000'||t.merchant_id||'1000'||t.id;

commit;