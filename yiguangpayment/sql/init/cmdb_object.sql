/*==============================================================*/
/* DBMS name:      ORACLE Version 11g                           */
/* Created on:     2015/4/6 星期一 下午 3:26:25                      */
/*==============================================================*/
drop table T_RULE_BLACK_LIST cascade constraints;

drop table T_RULE_WHITE_LIST cascade constraints;

drop table T_RULE_CHANNEL_LIMIT cascade constraints;

drop table T_RULE_MERCHANT_LIMIT cascade constraints;
   
drop table T_RULE_MERCHANT_REJECTION cascade constraints;

drop table T_RULE_MERCHANT_LIMIT_SPEED cascade constraints;
   
drop table T_MERCHANT_CHARGING_CODE cascade constraints;

drop table T_AGENT_MERCHANT cascade constraints;

drop table T_CARRIER_INFO cascade constraints;

drop table T_SUPPLIER cascade constraints;

drop table T_CHANNEL_CHARGING_CODE cascade constraints;

drop table T_CHANNEL_MERCHANT_RELATION cascade constraints;

drop table T_CITY cascade constraints;

drop table T_DATASOURCE cascade constraints;

drop table T_DATASOURCE_OPTION cascade constraints;

drop table T_EXTRACT_RECORD cascade constraints;

drop table T_ERROR_CODE cascade constraints;

drop table T_NUM_SECTION cascade constraints;

drop table T_PRODUCT cascade constraints;

drop table T_PRODUCT_BATCH cascade constraints;

drop table T_PRODUCT_CHARGING_POINT cascade constraints;

drop table T_PRODUCT_DEPOT cascade constraints;

drop table T_PROVINCE cascade constraints;

drop table T_POINT_CHANNEL_RELATION cascade constraints;

drop table T_LOGIN_LOGOUT_LOG cascade constraints;

drop table T_OPERATION_LOG cascade constraints;


drop sequence SEQ_MERCHANT_CHARGING_CODE;

drop sequence SEQ_AGENT_MERCHANT;

drop sequence SEQ_PRODUCT_BATCH;

drop sequence SEQ_CARRIER_INFO;

drop sequence SEQ_SUPPLIER;

drop sequence SEQ_CHANNEL_CHARGING_CODE;

drop sequence SEQ_CHANNEL_MERCHANT_RELATION;

drop sequence SEQ_CITY;

drop sequence SEQ_DATASOURCE;

drop sequence SEQ_DATESOURCE_OPTION;

drop sequence SEQ_EXTRACT_RECORD;

drop sequence SEQ_PRODUCT;

drop sequence SEQ_PRODUCT_CATEGORY;

drop sequence SEQ_PRODUCT_CHARGING_POINT;

drop sequence SEQ_PRODUCT_DEPOT;

drop sequence SEQ_PROVINCE;

drop sequence SEQ_POINT_CHANNEL_RELATION;

drop sequence SEQ_RULE_MERCHANT_LIMIT_SPEED;

drop sequence SEQ_RULE_MERCHANT_REJECTION;

drop sequence SEQ_RULE_MERCHANT_LIMIT;

drop sequence SEQ_RULE_CHANNEL_LIMIT;

drop sequence SEQ_RULE_BLACK_LIST;

drop sequence SEQ_RULE_WHITE_LIST;

drop sequence SEQ_OPERATION_LOG;

drop sequence SEQ_LOGIN_LOGOUT_LOG;

create sequence SEQ_MERCHANT_CHARGING_CODE
start with 2000
increment by 1
 minvalue 2000;

create sequence SEQ_AGENT_MERCHANT
increment by 1
start with 2000
 minvalue 2000;

create sequence SEQ_PRODUCT_BATCH
increment by 1
start with 2000
 minvalue 2000;

create sequence SEQ_CARRIER_INFO
increment by 1
start with 2000
 minvalue 2000;

create sequence SEQ_SUPPLIER
start with 2000
increment by 1
 minvalue 2000;

create sequence SEQ_CHANNEL_CHARGING_CODE
start with 2000
increment by 1
 minvalue 2000;

create sequence SEQ_CHANNEL_MERCHANT_RELATION
increment by 1
start with 2000
 minvalue 2000;

create sequence SEQ_CITY
start with 2000
increment by 1;

create sequence SEQ_DATASOURCE
increment by 1
start with 2000
 minvalue 2000;

create sequence SEQ_DATESOURCE_OPTION
increment by 1
start with 2000
 minvalue 2000;

create sequence SEQ_EXTRACT_RECORD
start with 2000
increment by 1
 minvalue 2000;

create sequence SEQ_PRODUCT
increment by 1
start with 2000
 minvalue 2000;

create sequence SEQ_PRODUCT_CATEGORY
start with 2000
increment by 1;

create sequence SEQ_PRODUCT_CHARGING_POINT
increment by 1
start with 2000
 minvalue 2000;

create sequence SEQ_PRODUCT_DEPOT
start with 2000
increment by 1
 minvalue 2000;

create sequence SEQ_PROVINCE
start with 2000
increment by 1;

create sequence SEQ_POINT_CHANNEL_RELATION
increment by 1
start with 2000
 minvalue 2000;

create sequence SEQ_RULE_MERCHANT_LIMIT_SPEED
increment by 1
start with 2000
 minvalue 2000;
 
create sequence SEQ_RULE_MERCHANT_REJECTION
increment by 1
start with 2000
 minvalue 2000;
 
create sequence SEQ_RULE_MERCHANT_LIMIT
increment by 1
start with 2000
 minvalue 2000;

create sequence SEQ_RULE_CHANNEL_LIMIT
increment by 1
start with 2000
 minvalue 2000;
 
create sequence SEQ_RULE_BLACK_LIST
increment by 1
start with 2000
 minvalue 2000;
 
create sequence SEQ_RULE_WHITE_LIST
increment by 1
start with 2000
 minvalue 2000;
 
create sequence SEQ_OPERATION_LOG
minvalue 2000
maxvalue 9999999999999999999999999999
start with 2000
increment by 1
cache 20;

create sequence SEQ_LOGIN_LOGOUT_LOG
minvalue 2000
maxvalue 9999999999999999999999999999
start with 2000
increment by 1
cache 20;
 
  /*==============================================================*/
/* Table: T_RULE_BLACK_LIST                                 */
/*==============================================================*/
create table T_RULE_BLACK_LIST
(
  id     NUMBER(20) not null,
  type   NUMBER(1) not null,
  value  VARCHAR2(200) not null,
  status NUMBER(1) not null,
  constraint PK_RULE_BLACK_LIST primary key (ID)
)
;

  /*==============================================================*/
/* Table: T_RULE_WHITE_LIST                                  */
/*==============================================================*/
create table T_RULE_WHITE_LIST
(
  id     NUMBER(20) not null,
  type   NUMBER(1) not null,
  value  VARCHAR2(200) not null,
  status NUMBER(1) not null,
  constraint PK_RULE_WHITE_LIST primary key (ID)
)
;

 
  /*==============================================================*/
/* Table: T_RULE_CHANNEL_LIMIT                                  */
/*==============================================================*/
create table T_RULE_CHANNEL_LIMIT
(
  id          NUMBER(20) not null,
  channel_id  NUMBER(20) not null,
  time 		  NUMBER(20) not null,
  volume      NUMBER(20) not null,
  status      NUMBER(1) not null,
  remark      VARCHAR2(4000),
  province_id VARCHAR2(4),
  city_id     VARCHAR2(4),
  constraint PK_RULE_CHANNEL_LIMIT primary key (ID)
)
;


 /*==============================================================*/
/* Table: T_RULE_MERCHANT_LIMIT                                  */
/*==============================================================*/
create table T_RULE_MERCHANT_LIMIT
(
  id          NUMBER(20) not null,
  merchant_id NUMBER(20) not null,
  time 		  NUMBER(20) not null,
  volume      NUMBER(20) not null,
  status      NUMBER(1) not null,
  remark      VARCHAR2(4000),
  province_id VARCHAR2(4),
  city_id     VARCHAR2(4),
  channel_id  NUMBER(20) not null,
  constraint PK_RULE_MERCHANT_LIMIT primary key (ID)
)
;
 
 /*==============================================================*/
/* Table: T_RULE_MERCHANT_REJECTION                                  */
/*==============================================================*/
create table T_RULE_MERCHANT_REJECTION
(
   id         NUMBER(20) not null,
  merchant_a NUMBER(20) not null,
  merchant_b NUMBER(20) not null,
  status     NUMBER(1) not null,
  remark     VARCHAR2(4000),
   constraint PK_RULE_MERCHANT_REJECTION primary key (ID)
);

/*==============================================================*/
/* Table: T_RULE_MERCHANT_LIMIT_SPEED                                 */
/*==============================================================*/
create table T_RULE_MERCHANT_LIMIT_SPEED
(
   id          NUMBER(20) not null,
  merchant_id NUMBER(20) not null,
  speed       NUMBER(20) not null,
  status      NUMBER(1) not null,
  remark      VARCHAR2(4000),
   constraint PK_RULE_MERCHANT_LIMIT_SPEED primary key (ID)
);


/*==============================================================*/
/* Table: T_MERCHANT_CHARGING_CODE                                 */
/*==============================================================*/
create table T_MERCHANT_CHARGING_CODE 
(
   ID                   NUMBER(20)           not null,
   NAME                 VARCHAR(200)         not null,
   MERCHANT_ID          NUMBER(20)           not null,
   CHARGING_AMOUNT      NUMBER(20)           not null,
   CHARGING_CODE        VARCHAR(200)         not null,
   STATUS               NUMBER(1)            not null,
   REMARK               VARCHAR(4000),
   constraint PK_T_MERCHANT_CHARGING_CODE primary key (ID)
);

/*==============================================================*/
/* Table: T_AGENT_MERCHANT                                      */
/*==============================================================*/
create table T_AGENT_MERCHANT 
(
   ID                   NUMBER(20)           not null,
   NAME                 VARCHAR(200)         not null,
   STATUS               NUMBER(1)            not null,
   KEY                  VARCHAR(200)         not null,
   REMARK               VARCHAR(4000),
   NOTIFY_URL           VARCHAR2(200),
   constraint PK_T_AGENT_MERCHANT primary key (ID)
);

/*==============================================================*/
/* Table: T_CARRIER_INFO                                        */
/*==============================================================*/
create table T_CARRIER_INFO 
(
   ID                   NUMBER(20)           not null,
   CARRIER_NO           VARCHAR2(32)         not null,
   CARRIER_NAME         VARCHAR2(32)         not null,
   STATUS               NUMBER(1)            not null,
   REMARK               VARCHAR2(4000),
   CARRIER_TYPE         NUMBER(1)            not null,
   constraint PK_T_CARRIER_INFO primary key (ID)
);

/*==============================================================*/
/* Table: T_CHANNEL                                             */
/*==============================================================*/
create table T_SUPPLIER 
(
   ID                   NUMBER(20)           not null,
   NAME                 VARCHAR(200)         not null,
   STATUS               NUMBER(1)            not null,
   REMARK               VARCHAR(4000),
   constraint PK_T_SUPPLIER primary key (ID)
);

/*==============================================================*/
/* Table: T_CHANNEL_CHARGING_CODE                               */
/*==============================================================*/
create table T_CHANNEL_CHARGING_CODE 
(
   ID                   NUMBER(20)           not null,
   CHANNEL_ID           NUMBER(20)           not null,
   CHARGING_AMOUNT      NUMBER(20)           not null,
   CHARGING_CODE        VARCHAR(200)         not null,
   STATUS               NUMBER(1)            not null,
   REMARK               VARCHAR(4000),
   constraint PK_T_CHANNEL_CHARGING_CODE primary key (ID)
);

/*==============================================================*/
/* Table: T_CHANNEL_MERCHANT_RELATION                           */
/*==============================================================*/
create table T_CHANNEL_MERCHANT_RELATION 
(
   ID                   NUMBER(20)           not null,
   MERCHANT_ID          NUMBER(20)           not null,
   CHANNEL_ID           NUMBER(20)           not null,
   STATUS               NUMBER(1)            not null,
   REMARK               VARCHAR(4000),
   constraint PK_T_CHANNEL_MERCHANT_RELATION primary key (ID)
);

/*==============================================================*/
/* Table: T_CITY                                                */
/*==============================================================*/
create table T_CITY 
(
   CITY_ID              VARCHAR(4)           not null,
   CITY_NAME            VARCHAR(200)         not null,
   STATUS               NUMBER(1)            not null,
   PROVINCE_ID          VARCHAR(4)           not null,
   REMARK               VARCHAR(4000),
   constraint PK_T_CITY primary key (CITY_ID)
);

/*==============================================================*/
/* Table: T_DATASOURCE                                          */
/*==============================================================*/
create table T_DATASOURCE 
(
   ID                   NUMBER(20)           not null,
   NAME                 VARCHAR(200)         not null,
   TYPE                 NUMBER(1)            not null,
   STATUS               NUMBER(1)            not null,
   REMARK               VARCHAR(4000),
   OPTION_SQL           VARCHAR(4000),
   PARENT_CODE          VARCHAR2(200),
   CODE                 VARCHAR2(200)        not null,
   constraint PK_T_DATASOURCE primary key (ID)
);

comment on table T_DATASOURCE is
'系统字典数据源表';

comment on column T_DATASOURCE.ID is
'订单状态ID';

comment on column T_DATASOURCE.NAME is
'订单状态值';

comment on column T_DATASOURCE.TYPE is
'1是普通数据源，2是sql数据源';

comment on column T_DATASOURCE.STATUS is
'状态，1激活，0非激活';

comment on column T_DATASOURCE.REMARK is
'说明';

/*==============================================================*/
/* Table: T_DATASOURCE_OPTION                                   */
/*==============================================================*/
create table T_DATASOURCE_OPTION 
(
   ID                   NUMBER(20)           not null,
   OPTION_ID            NUMBER(20)           not null,
   OPTION_LABEL         VARCHAR(200)         not null,
   STATUS               NUMBER(1)            not null,
   REMARK               VARCHAR(4000),
   DATA_SOURCE_CODE     VARCHAR(200)         not null,
   PARENT_OPTION_ID     VARCHAR2(200),
   PARENT_DATASOURCE_CODE VARCHAR2(200),
   constraint PK_T_DATASOURCE_OPTION primary key (ID)
);

comment on table T_DATASOURCE_OPTION is
'字典值表';

comment on column T_DATASOURCE_OPTION.ID is
'支付状态ID';

comment on column T_DATASOURCE_OPTION.OPTION_LABEL is
'支付状态值';

comment on column T_DATASOURCE_OPTION.STATUS is
'状态，1激活，0非激活';

comment on column T_DATASOURCE_OPTION.REMARK is
'说明';

/*==============================================================*/
/* Table: T_EXTRACT_RECORD                                      */
/*==============================================================*/
create table T_EXTRACT_RECORD 
(
   ID                   NUMBER(20)           not null,
   CHARGING_POINT_ID    NUMBER(20),
   SUPPLIER_ID          NUMBER(20),
   PRODUCT_ID           NUMBER(20),
   PROVINCE_ID          VARCHAR(200),
   CITY_ID              VARCHAR(200),
   FACE_AMOUNT          NUMBER(20),
   STATUS               NUMBER(1),
   REMARK               VARCHAR(4000),
   REQUEST_TIME         DATE                 not null,
   REQUEST_IP           VARCHAR(200),
   ORDER_ID             VARCHAR(200)         not null,
   RETURN_CODE          VARCHAR(200),
   RETURN_MESSAGE       VARCHAR(200),
   EXTRACT_TYPE         NUMBER(1),
   EXTRACT_USER         VARCHAR(200),
   MERCHANT_ID          NUMBER(20),
   PAY_AMOUNT           NUMBER(20),
   DELIVERY_AMOUNT      NUMBER(20),
   EXTRACT_COUNT        NUMBER(20),
   constraint PK_T_EXTRACT_RECORD primary key (ID)
);



/*==============================================================*/
/* Table: T_NUM_SECTION                                         */
/*==============================================================*/
create table T_NUM_SECTION 
(
   SECTION_ID           VARCHAR(20)          not null,
   CARRIER_NO           VARCHAR(20),
   PROVINCE_ID          VARCHAR(20),
   CITY_ID              VARCHAR(20),
   MOBILE_TYPE          VARCHAR(20),
   PRIORITY             NUMBER(20),
   USED_TIMES           NUMBER(20),
   CREATE_TIME          DATE,
   constraint PK_T_NUM_SECTION primary key (SECTION_ID)
);


/*==============================================================*/
/* Table: T_PRODUCT                                             */
/*==============================================================*/
create table T_PRODUCT 
(
   ID                   NUMBER(20)           not null,
   NAME                 VARCHAR2(200)        not null,
   TYPE                 NUMBER(1)            not null,
   SUPPLIER_ID           NUMBER(20)           not null,
   STATUS               NUMBER(1)            not null,
   REMARK               VARCHAR2(4000),
   constraint PK_T_PRODUCT primary key (ID)
);

/*==============================================================*/
/* Table: T_PRODUCT_BATCH                                       */
/*==============================================================*/
create table T_PRODUCT_BATCH 
(
   ID                   NUMBER(20)           not null,
   SUPPLIER_ID           NUMBER(20)           not null,
   PRODUCT_ID           NUMBER(20)           not null,
   BATCH_ID             VARCHAR(200)         not null,
   TOTAL_AMT            NUMBER(20)           not null,
   TOTAL_COUNT          NUMBER(20)           not null,
   STATUS               NUMBER(1)            not null,
   REMARK               VARCHAR(4000),
   constraint PK_T_PRODUCT_BATCH primary key (ID)
);

/*==============================================================*/
/* Table: T_PRODUCT_CHARGING_POINT                              */
/*==============================================================*/
create table T_PRODUCT_CHARGING_POINT 
(
   ID                   NUMBER(20)           not null,
   NAME                 VARCHAR(200)         not null,
   SUPPLIER_ID           NUMBER(20)           not null,
   STATUS               NUMBER(1)            not null,
   PRODUCT_ID           NUMBER(20)           not null,
   PROVINCE_ID          VARCHAR(200),
   CITY_ID              VARCHAR(200),
   REMARK               VARCHAR(4000),
   FACE_AMOUNT          NUMBER(20)           not null,
   PAY_AMOUNT           NUMBER(20)           not null,
   DELIVERY_AMOUNT      NUMBER(20)           not null,
   CHARGING_TYPE        NUMBER(1)            not null,
   constraint PK_T_PRODUCT_CHARGING_POINT primary key (ID)
);

/*==============================================================*/
/* Table: T_PRODUCT_DEPOT                                       */
/*==============================================================*/
create table T_PRODUCT_DEPOT 
(
   ID                   NUMBER(20)           not null,
   CHARGING_POINT_ID    NUMBER(20)           not null,
   CARD_ID              VARCHAR(200)         not null,
   CARD_PWD             VARCHAR(500)         not null,
   BATCH_ID             VARCHAR(200)         not null,
   USEFUL_START_DATE    DATE,
   USEFUL_END_DATE      DATE,
   STOCK_IN_DATE        DATE                 not null,
   STATUS               NUMBER(1)            not null,
   REMARK               VARCHAR(4000),
   EXTRACT_NO           VARCHAR(200),
   PRODUCT_ID           NUMBER(20)           not null,
   constraint PK_T_PRODUCT_DEPOT primary key (ID)
);

/*==============================================================*/
/* Table: T_PROVINCE                                            */
/*==============================================================*/
create table T_PROVINCE 
(
   PROVINCE_ID          VARCHAR2(4)          not null,
   PROVINCE_NAME        VARCHAR2(20)         not null,
   STATUS               NUMBER(1)            not null,
   SORT_ID              NUMBER(2)            not null,
   REMARK               VARCHAR2(4000),
   constraint PK_T_PROVINCE primary key (PROVINCE_ID)
);

/*==============================================================*/
/* Table: T_POINT_CHANNEL_RELATION                              */
/*==============================================================*/
create table T_POINT_CHANNEL_RELATION 
(
   ID                   NUMBER(20)           not null,
   POINT_ID             NUMBER(20),
   CHANNEL_ID           NUMBER(20)           not null,
   STATUS               NUMBER(1)            not null,
   REMARK               VARCHAR(4000),
   constraint PK_T_POINT_CHANNEL_RELATION primary key (ID)
);

/*==============================================================*/
/* Table: T_LOGIN_LOGOUT_LOG                              */
/*==============================================================*/
create table T_LOGIN_LOGOUT_LOG
(
  id                NUMBER(20) not null,
  username            VARCHAR2(200),
  operation_type    NUMBER(1),
  remark            VARCHAR2(4000),
  operation_time    DATE not null,
  operation_ip      VARCHAR2(200),
  constraint PK_T_LOGIN_LOGOUT_LOG primary key (ID)
);


/*==============================================================*/
/* Table: T_OPERATION_LOG                              */
/*==============================================================*/
create table T_OPERATION_LOG
(
  id                NUMBER(20) not null,
  username          VARCHAR2(200),
  operation_type    NUMBER(1),
  remark            VARCHAR2(4000),
  operation_time    DATE not null,
  operation_ip      VARCHAR2(200),
  operation_obj     VARCHAR2(200),
  constraint PK_T_OPERATION_LOG primary key (ID)
);

  
-- 错误码信息表
-- Create table
create table T_ERROR_CODE
(
  code VARCHAR2(10) not null,
  msg  VARCHAR2(100)
);

-- Add comments to the columns 
comment on column T_ERROR_CODE.code
  is '错误码';
comment on column T_ERROR_CODE.msg
  is '错误信息';
-- Create/Recreate primary, unique and foreign key constraints 
alter table T_ERROR_CODE
  add constraint PAY_ERROR_CODE_PK primary key (CODE);
  
/*==============================================================*/
/* Table: T_PAYMENT_ORDER                                       */
/*==============================================================*/
drop table T_PAYMENT_ORDER cascade constraints;

drop sequence SEQ_PAYMENT_ORDER;

create sequence SEQ_PAYMENT_ORDER
increment by 1
start with 2000
 minvalue 2000;

create table T_PAYMENT_ORDER 
(
   ID                   NUMBER(20)           not null,
   PAY_ORDER_ID    VARCHAR(200)         not null,
   MERCHANT_ORDER_ID    VARCHAR(200)         not null,
   PAY_REQUEST_TIME     DATE,
   PAY_COMPLETE_TIME    DATE,
   MOBILE               NUMBER(20),
   PROVINCE_ID          VARCHAR(200),
   CITY_ID              VARCHAR(200),
   CHANNEL_ID           NUMBER(20),
   CHANNEL_TYPE         NUMBER(1),
   MERCHANT_ID          NUMBER(20),
   PAY_AMOUNT           NUMBER(20),
   USERNAME             VARCHAR(200),
   PRODUCT_ID           VARCHAR(200),
   PRODUCT_DESC         VARCHAR(200),
   STATUS               NUMBER(1),
   RETURN_CODE          VARCHAR(200),
   RETURN_MESSAGE       VARCHAR(4000),
   NOTIFY_STATUS        NUMBER(1),
   NOTIFY_URL       	VARCHAR(1000),
   EXT1                 VARCHAR(200),
   EXT2                 VARCHAR(200),
   EXT3                 VARCHAR(200),
   EXT4                 VARCHAR(200),
   EXT5                 VARCHAR(200),
   constraint PK_T_PAYMENT_ORDER primary key (ID)
);

/*==============================================================*/
/* Table: T_MERCHANT_ORDER                                      */
/*==============================================================*/
drop table T_MERCHANT_ORDER cascade constraints;

drop sequence SEQ_MERCHANT_ORDER;

create sequence SEQ_MERCHANT_ORDER
start with 2000
increment by 1
 minvalue 2000;
 
create table T_MERCHANT_ORDER 
(
   ID                   NUMBER(20)           not null,
   ORDER_ID             VARCHAR(50)          not null,
   REQUEST_TIME         DATE,
   COMPLETE_TIME        DATE,
   DELIVERY_REQUEST_TIME DATE,
   DELIVERY_COMPLETE_TIME DATE,
   MOBILE               NUMBER(20),
   SUPPLIER_ID           NUMBER(20),
   PROVINCE_ID          VARCHAR(200),
   CITY_ID              VARCHAR(200),
   PRODUCT_ID           NUMBER(20),
   CHANNEL_ID           NUMBER(20),
   FACE_AMOUNT          NUMBER(20),
   PAY_AMOUNT           NUMBER(20),
   DELIVERY_AMOUNT      NUMBER(20),
   PAY_STATUS           NUMBER(1),
   DELIVERY_STATUS      NUMBER(1),
   DELIVERY_NO          VARCHAR(200),
   ORDER_STATUS         NUMBER(1),
   USERNAME             VARCHAR(200),
   REQUEST_IP           VARCHAR(200),
   CHARGING_POINT_ID    NUMBER(20),
   CHARGING_TYPE        NUMBER(1),
   RETURN_CODE          VARCHAR(200),
   RETURN_MESSAGE       VARCHAR(4000),
   DELIVERY_RETURN_CODE          VARCHAR(200),
   DELIVERY_RETURN_MESSAGE       VARCHAR(4000),
   EXT1                 VARCHAR(200),
   EXT2                 VARCHAR(200),
   EXT3                 VARCHAR(200),
   EXT4                 VARCHAR(200),
   EXT5                 VARCHAR(200),
   constraint PK_T_MERCHANT_ORDER primary key (ID)
);
-----------------------------------------------------------------------------
