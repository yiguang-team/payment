package com.yiguang.payment.common;

public class CommonConstant
{

	public class DataSourceName
	{

		public static final String PRODUCT = "PRODUCT";

		public static final String CARD_STATUS = "CARD_STATUS";

		public static final String FACE_AMOUNT = "FACE_AMOUNT";

		public static final String NOTIFY_STATUS = "NOTIFY_STATUS";

		public static final String COMMON_STATUS = "COMMON_STATUS";
		
		public static final String USER = "USER";
		
		public static final String ROLE = "ROLE";
		
		public static final String SOLD_TYPE = "SOLD_TYPE";

		public static final String PRODUCT_TYPE = "PRODUCT_TYPE";

		public static final String CARRIER_TYPE = "CARRIER_TYPE";

		public static final String DATA_SOURCE = "DATA_SOURCE";

		public static final String PROVINCE = "PROVINCE";

		public static final String CHARGING_TYPE = "CHARGING_TYPE";

		public static final String CITY = "CITY";

		public static final String CHANNEL = "CHANNEL";
		
		public static final String CARRIER = "CARRIER";

		public static final String POINT = "POINT";

		public static final String POINT_CARD_PWD = "POINT_CARD_PWD";

		public static final String MERCHANT = "MERCHANT";

		public static final String RISK_LIMIT_TYPE = "RISK_LIMIT_TYPE";
		
		public static final String RISK_SELECT_TYPE = "RISK_SELECT_TYPE";
		
		public static final String RISK_TIME_TYPE = "RISK_TIME_TYPE";

		public static final String RISK_ACTION = "RISK_ACTION";
		
		public static final String CHANNEL_TYPE = "CHANNEL_TYPE";

		public static final String MERCAHNT_TYPE = "MERCAHNT_TYPE";

		public static final String PAY_STATUS = "PAY_STATUS";

		public static final String DELIVERY_STATUS = "DELIVERY_STATUS";

		public static final String PICKUP_TYPE = "PICKUP_TYPE";

		public static final String MERCHANT_CHARGING_CODE = "MERCHANT_CHARGING_CODE";

		public static final String CHANNEL_CHARGING_CODE = "CHANNEL_CHARGING_CODE";

		public static final String ORDER_REPORT_MODEL = "ORDER_REPORT_MODEL";

		public static final String PRODUCT_REPORT_MODEL = "PRODUCT_REPORT_MODEL";

		public static final String DEPOT_REPORT_MODEL = "DEPOT_REPORT_MODEL";

		public static final String RISK_RULE = "RISK_RULE";

		public static final String TIME_UNIT = "TIME_UNIT";

		public static final String LOGIN_LOGOUT_TYPE = "LOGIN_LOGOUT_TYPE";

		public static final String LOG_ORERATION_TYPE = "LOG_ORERATION_TYPE";

		public static final String LIST_TYPE = "LIST_TYPE";// 名单类型
		
		public static final String IS_LOCK = "IS_LOCK";
		
	}
	public class ACTION
	{
		public static final int REMINDER = 1;

		public static final int FORBID = 0;
	}
	public class TimeLimitType
	{
		public static final int TIME_RELATIVE = 2;
		
		public static final int TIME_PERIOD = 1;

		public static final int TIME_UNIT = 0;
	}
	
	public class TimeUnit
	{
		public static final int MONTH = 3;

		public static final int DAY = 2;
		
		public static final int HOUR = 1;
		
		public static final int MINUTE = 0;
	}
	
	public class LimitType
	{
		public static final int LIMIT_AMOUNT = 0;

		public static final int LIMIT_NUM = 1;
	}
	
	public class RiskSelectType
	{
		public static final int NON_LIMIT = -9;

		public static final int CURRENT = -8;

		public static final int SELECTED = -7;
	}

	public class ListType
	{

		public static final int MOBILE = 1;

		public static final int SECTION = 2;

		public static final int IP = 3;

		public static final int USER = 4;
	}

	public class TimeType
	{
		public static final int HOUR = 1;

		public static final int DAY = 2;

		public static final int MONTH = 3;
	}

	public class PayStatus
	{
		public static final int NOT_PAY = 3;

		public static final int PAYING = 2;

		public static final int SUCCESS = 0;

		public static final int FAILED = 1;
	}

	public class DeliveryStatus
	{

		public static final int NOT_DELIVERY = 3;

		public static final int DELIVERYING = 2;

		public static final int SUCCESS = 0;

		public static final int FAILED = 1;
	}

	public class NotifyStatus
	{

		public static final int NOT_NOTIFY = 3;

		public static final int NOTIFYING = 2;

		public static final int SUCCESS = 0;

		public static final int FAILED = 1;
	}

	public class ChannelType
	{

		public static final int DUANYAN = 1;

		public static final int DUANDAI = 2;
	}

	public class LoginAndLogoutType
	{
		public static final int LOGIN = 0;

		public static final int LOGOUT = 1;
	}

	public class LogOperationType
	{
		public static final int ADD = 0;

		public static final int MODIFY = 1;

		public static final int DEL = 2;
	}

	public class DataSourceType
	{

		public static final int COMMON = 1;

		public static final int SQL = 2;
	}

	public class Result
	{

		public static final int SUCCESS = 1;

		public static final int FAILED = 0;

	}

	public class CHARGING_TYPE
	{

		public static final int CARD = 2;

		public static final int DERICT = 1;

	}

	public class CommonStatus
	{
		public static final int OPEN = 1;

		public static final int CLOSE = 0;
	}

	public class CardStatus
	{
		public static final int NOT_SALES = 1;

		public static final int SALES = 2;

		public static final int SOLD = 3;

	}

	public class ModelColumns
	{
		public static final int TIME_HOUR = 1;

		public static final int TIME_DAY = 2;
		
		public static final int TIME_MONTH = 3;
		
		public static final int CHANNEL_ID = 4;

		public static final int PROVINCE_ID = 5;

		public static final int CITY_ID = 6;

		public static final int MERCHANT_ID = 7;

		public static final int PRODUCT_ID = 8;

		public static final int CHARGING_POINT_ID = 9;

		public static final int CHARGING_TYPE = 10;

		public static final int PAY_AMOUNT = 11;

		public static final int PICKUP_USER = 12;

	}

	public class CardColumns
	{

		public static final String CARD_ID = "CARD_ID";
		public static final String CARD_PWD = "CARD_PWD";
		public static final String FACE_AMOUNT = "FACE_AMOUNT";
		public static final String USEFUL_START_DATE = "USEFUL_START_DATE";
		public static final String USEFUL_END_DATE = "USEFUL_END_DATE";
		public static final String REMARK = "REMARK";
	}
}
