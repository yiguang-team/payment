package com.yiguang.payment.common;

public class Constant
{
	public class RebateTimeType
	{

		public static final long MONTH = 1; // 按月返

		public static final long QUARTER = 2; // 按季返

		public static final long YEAR = 3; // 按年返

		public static final long DAY = 4; // 按天返
	}

	public class RebateRuleMerchantType
	{

		public static final String MERCHANT = "0"; // 发生商戶

		public static final String MERCHANT_AND_REBATEMERCHANT = "1"; // 发生商户和返佣商户
	}

	public class RebateType
	{

		public static final long PROPORTION = 0; // 比例

		public static final long FIXED_VALUE = 1; // 固定值
	}

	public class RebateStatus
	{

		public static final String ENABLE = "0";

		public static final String DISABLE = "1";

		public static final String DEL_STATUS = "2";

	}

	public class RebateTransactionVolume
	{
		public static final String ONE_HUNDRED_PERCENT = "0.01";

		public static final long MAX_TRADING_VOLUME = 999999999999999999l;
	}

	public class MerchantRebateStatus
	{

		public static final long ENABLE = 0l;

		public static final long DISABLE = 1l;
	}

	public class RebateRecordStatus
	{
		public static final String WAIT_REBATE = "1";

		public static final String REBATE_SUCCESS = "0";
	}

	public class CacheKey
	{
		public static final String URI_TRANSACTION_MAPPING = "UriTransactionMapping";

		public static final String RESPONSE_CODE_TRANSLATION = "ResponseCodeTranslation";

		public static final String INTERFACE_PACKET_DEFINITION = "interfacePacketsDefinition";

		public static final String INTERFACE_PACKET_TYPE_CONF = "InterfacePacketTypeConf";

		public static final String INTERFACE_PARAM = "interfaceParam";

		public static final String INTERFACE_CONSTANT = "interfaceConstant";

		public static final String ERROR_CODE = "errorCode";

		public static final String MERCHANT_INFO = "merchantInfo";

		public static final String PRODUCT = "Product";

		public static final String PRODUCT_LIST = "ProductList";

		public static final String AGENT_PRODUCT_RELATION = "AgentProductRelation";

		public static final String SUPPLY_PRODUCT_RELATION = "SupplyProductRelation";

		public static final String FAKE_RULE = "fakeRule";

		public static final String NUM_SECTION = "NumSection";

		public static final String TMALL_TSC = "TmallTSC";

		public static final String SECURITY_CREDENTIAL = "SecurityCredential";

		public static final String SECURITY_CREDENTIAL_TYPE = "SecurityCredentialType";

		public static final String SECURITY_CREDENTIAL_RULE = "SecurityCredentialRule";

		public static final String SP = "sp";

		public static final String ACCOUNT = "Account";

		public static final String ACCOUNT_TYPE = "AccountType";

		public static final String MERCHANT_RESPONSE = "merchantResponse";
	}

	public class Common
	{

		public static final String CACHE = "cache";

		public static final String SYSTEM = "system";

		public static final String TRANSACTION_CACHE = "TransactionCache";

		public static final String GETWAY_CACHE = "GetwayCache";

		public static final String NUM_SECTION_CACHE = "NumSectionCache";

		public static final String TMALL_TSC_CACHE = "TmallTSCCache";

		// hops-identity
		public static final String IDENTITY_CACHE = "IdentityCache";

		public static final String LOGIN_NAME_CACHE = "LoginName";

		public static final String SECURITY_CREDENTIAL_CACHE = "SecurityCredential";

		public static final String SECURITY_CREDENTIAL_TYPE_CACHE = "SecurityCredentialType";

		public static final String SECURITY_CREDENTIAL_RULE_CACHE = "SecurityCredentialRule";

		// chcache chacheName
		public static final String BUSINESS_CACHE = "BusinessCache";

		public static final String KEY = "key";

		public static final String VALUE = "value";

		public static final String LIST = "list";

		public static final String SEARCH = "search";

		public static final String FAKE_ORDER_HEAD = "fake";

		public static final String MERCHANT_ORDER_HEAD = "mo";

		public static final String MERCHANTTYPE_AGENT = "AGENT";

		public static final String MERCHANTTYPE_SUPPLY = "SUPPLY";

		public static final String MERCHANTTYPE_ALL = "ALL";

		public static final String RSA = "RSA";

		public static final String MD5 = "MD5";

		public static final String PACKET_TYPE_TAOBAO = "taobao";

		public static final String PACKET_TYPE_YUECHENG = "yuecheng";

		public static final String DATE_FORMAT_TYPE = "yyyyMMddHHmmss";

		public static final String DATE_FORMAT_TYPE_SQL = "yyyy-MM-dd HH:mm:ss";

		public static final String MONEY_FORMAT_TYPE = "###.###";

		public static final String MONEY_FORMAT_TYPE_CENT = "fen";

		public static final String MONEY_FORMAT_TYPE_YUAN = "yuan";

		public static final String DEFAULT_ENCODING = "GBK";

		public static final int PAGE_SIZE = 100;

		public static final String SUCCESS = "success";

		public static final String FAIL = "fail";

		public static final String ERROR = "error";

		public static final String CHILD = "child";

		public static final String ZERO = "0";

		public static final String SEPARATOR = ",";

		public static final long DEFAULT_LIMIT_BIND_TIMES = 10;
	}

	public class Sort
	{

		public static final String ID = "id";

		public static final String ASC = "ASC";

		public static final String DESC = "DESC";
	}

	public class Merchant
	{

		public static final String MERCHANT_STATUS = "1";

		public static final String MERCHANT_REMARK = "注册商户自动生成";

		public static final long IS_REBATE_YES = 0l;// 商户返佣,是

		public static final long IS_REBATE_NO = 1l;// 否

		public static final long TOP_PARENT_ID = 0l;// 顶级商户父级编号默认0
	}

	public class TrueOrFalse
	{
		public static final String TRUE = "true";

		public static final String FALSE = "false";
	}

	public class BusinessNo
	{
		public static final String HU_FEI = "HF";
	}

	public class BusinessType
	{
		public static final String BUSINESS_TYPE_HF = "0";

		public static final String BUSINESS_AIRTIME = "airtime";
	}

	public class MerchantProductLevelName
	{
		public static final String HIGH_PRODUCT = "优质产品";

		public static final String MEDIUM_PRODUCT = "中等产品";

		public static final String COMMON_PRODUCT = "普通产品";
	}

	/**
	 * 返回错误码常量
	 * 
	 * @author：Jinger
	 * @date：2014-04-18
	 */
	public class ErrorCode
	{
		// 操作成功
		public static final String SUCCESS = "100";

		// 操作成功，充值中
		public static final String WAITING_RECHARGE = "101";

		// 操作成功，充值失败
		public static final String RECHARGE_FAIL = "102";

		// 操作成功，充值成功
		public static final String RECHARGE_SUCCESS = "103";

		// 下单未知错误，请查询获取结果
		public static final String ORDER_UNKNOW = "104";

		// 操作失败
		public static final String FAIL = "200";

		// 必须参数为空
		public static final String PARAM_IS_ERROR = "250";

		// 签名错误
		public static final String SIGN_ERROR = "251";

		// 订单已存在
		public static final String IS_EXIST = "252";

		// 商家不存在或未启用
		public static final String PARTNER_ERROR = "253";

		// 金额错误
		public static final String AMOUNT_ERROR = "254";

		// 购买数量非法
		public static final String CARD_ERROR = "255";

		// 渠道产品未开通
		public static final String NOT_SUPPORT = "256";

		// 加解密错误
		public static final String DECRYPTY_ERROR = "257";

		// 订单不存在
		public static final String IS_NOT_EXIST = "258";

		// 产品状态关闭
		public static final String RULE_NOT_SUPPORT = "259";

		// 系统不支持该产品
		public static final String PRODUCT_DISABLED = "260";

		// 充值产品和帐号不匹配
		public static final String PDU_ACT_NOT_SUPPORT = "261";

		// 发送订单频率过快
		public static final String FAIL_FREQUENCY = "271";

		// 提交订单总金额超过限制金额
		public static final String FAIL_TRADING_MONEY = "272";

		// 商家账户未开启
		public static final String ACCOUNT_LOCKED = "600";

		// 商家扣款失败
		public static final String BALANCE_NOT_ENOUGH = "601";

		// 充值异常，需要人工审核
		public static final String MANUAL = "999";

		// 供货商资金不足
		public static final String SUPPLY_MERCHANT_FUNDS_NOT_ENOUGH = "300";

		// 供货商返回失败
		public static final String SUPPLY_MERCHANT_RESPONSE_ORDER_FAIL = "301";

		// 订单绑定失败
		public static final String BIND_ORDER_FAIL = "302";
	}

	/**
	 * 淘宝返回错误码常量
	 * 
	 * @author：Jinger
	 * @date：2014-04-18
	 */
	public class TBErrorCode
	{
		// / <summary>
		// / 成功（自定义）
		// / </summary>
		public static final String TB_SUCCESS = "0000";

		// / <summary>
		// / 缺少必须参数
		// / </summary>
		public static final String TB_PARAM_NULL_ERROR = "0101";

		// / <summary>
		// / 签名错误
		// / </summary>
		public static final String TB_SIGN_ERROR = "0102";

		// / <summary>
		// / 参数有误，参数格式或值非法
		// / </summary>
		public static final String TB_PARAM_IS_ERROR = "0103";

		// / <summary>
		// / 找不到对应订单
		// / </summary>
		public static final String TB_IS_NOT_EXIST = "0104";

		// / <summary>
		// / 订单重复
		// / </summary>
		public static final String TB_IS_EXIST = "0301";

		// / <summary>
		// / 购买数量非法
		// / </summary>
		public static final String TB_NO_NUMBER = "0302";

		// / <summary>
		// / 商品库存不足
		// / </summary>
		public static final String TB_NO_MORE_PRODUCT = "0303";

		// / <summary>
		// / 商品库存不足
		// / </summary>
		public static final String TB_PRODUCT_MAINTAIN = "0304";

		// / <summary>
		// / 找不到对应的商品
		// / </summary>
		public static final String TB_NO_PRODUCT = "0305";

		// / <summary>
		// / 参数错误，被充值帐户有误，比如联通充值时输入了移动的号码，QQ充值时输入字母
		// / </summary>
		public static final String TB_PARAM_CUSTOMER_ERROR = "0306";

		// / <summary>
		// / 运营商例行维护，无法充值
		// / </summary>
		public static final String TB_PARTNER_MAINTAIN = "0501";

		// / <summary>
		// / 被充值帐户有误，比如联通充值时输入了移动的号码，QQ充值时输入字母
		// / </summary>
		public static final String TB_CUSTOMER_ERROR = "0502";

		// / <summary>
		// / 充值失败
		// / </summary>
		public static final String TB_FAIL = "0503";

		// / <summary>
		// / 非法的订单状态转换
		// / </summary>
		public static final String TB_ORDER_STATUS_CHANGE = "0701";

		// / <summary>
		// / 充值超时，订单被取消
		// / </summary>
		public static final String TB_TIMEOUT = "0901";

		// / <summary>
		// / 未知错误
		// / </summary>
		public static final String TB_ERROR = "9999";
	}

	public class Channel
	{
		public static final String CHANNEL_API = "API";

		public static final String CHANNEL_MPROTAL = "MPORTAL";
	}

	/**
	 * 权限模块常量
	 * 
	 * @author：Jinger
	 * @date：2013-09-23
	 */
	public class PrivilegeConstants
	{
		public static final String SUCCESS = "success";

		public static final String FAIL = "fail";

		public static final String FAILPWD = "failpwd";
	}

	/**
	 * 账户类常量参数
	 * 
	 * @author Administrator
	 */
	public class Account
	{
		public static final String ACCOUNT_STATUS_LOCKED = "-1";

		public static final String ACCOUNT_STATUS_UNLOCK = "1";

		public static final String TRANSACTION_TYPE_CALLS = "1";

		public static final String TRANSACTION_TYPE_CALAMOUNT = "2";

		public static final String ZERO = "0";

		public static final String STATUS = "status";

		public static final String ACCOUNT_RELATION_TYPE_OWN = "own"; // 用户与账户的关系--所属

		public static final String ACCOUNT_RELATION_TYPE_USE = "use"; // 用户与账户的关系--使用

		public static final int VERSION = 1;

		public static final int DEFAULT_BALANCE = 0;
	}

	public class RefundConfiguration
	{
		public static final long NO_REFUND = 0;

		public static final long REFUNDED = 1;
	}

	public class TransferType
	{
		public static final String TRANSFER_AGENT_ORDERED = "1"; // 下游下单

		public static final String TRANSFER_SUPPLY_ORDER_FINISH = "2";// 上游订单成功

		public static final String TRANSFER_SYSTEM_AGENT_PROFIT = "3"; // 订单成功、系统借记账户往系统利润账户转款

		public static final String TRANSFER_PROFIT_IMPUTATION = "4";// 利润归集

		public static final String TRANSFER_REBATE_AMT_LIQUIDATIOR = "5"; // 返佣清算

		public static final String TRANSFER_REBATE_AMT_ROLLBACK = "6"; // 返佣资金回滚（删除时使用）

		public static final String TRANSFER_CONFIRM_PROFIT = "7";// 确认利润

		public static final String TRANSFER_REBATE_AMT_CONFIRM = "8"; // 确认返佣

		public static final String TRANSFER_REBATE_BALANCE_RECOVER = "9"; // 余款收回

		public static final String TRANSFER_UN_AGENT_ORDERED = "10";// 退款

		public static final String TRANSFER_REBATE_ACTULAMOUNT_RECOVER = "13"; // 佣金收回
	}

	public class AccountBalanceOperationType
	{
		public static final String ACT_BAL_OPR_CREDIT = "1"; // 贷记

		public static final String ACT_BAL_OPR_DEBIT = "2"; // 借记

		public static final String ACT_BAL_OPR_FORZEN = "3"; // 冻结

		public static final String ACT_BAL_OPR_UNFORZEN = "4"; // 解冻

		public static final String ACT_BAL_OPR_ADD_CREDIT = "5"; // 授信加款

		public static final String ACT_BAL_OPR_SUB_CREDIT = "6"; // 授信减款

		public static final String ACT_BAL_OPR_ADD_CASH = "7"; // 加款

		public static final String ACT_BAL_OPR_SUB_CASH = "8"; // 减款

	}

	public class AddCashStatus
	{
		public static final int WAIT_VERIFY = 1; // 1-待审核

		public static final int VERIFY_SUCCESS = 2; // 2-审核成功

		public static final int VERIFY_FAIL = 3; // 3-审核失败
	}

	public class BatchOrderStatus
	{
		public static final long WAITING_CHECK = 0l;

		public static final long WAITING_RECHARGE = 1l;

		public static final long RECHARGE_SUCCESS = 2l;

		public static final long RECHARGE_FAIL = 4l;

		public static final long DELETE = 3l;
	}

	/**
	 * 订单状态常量
	 * 
	 * @author Jinger 2014-03-07
	 */
	public class OrderStatus
	{
		public static final int WAIT_PAY = 0; // 待付款

		public static final int WAIT_RECHARGE = 1; // 待处理

		public static final int RECHARGING = 2; // 处理中

		public static final int FAILURE_ALL = 4; // 失败

		public static final int FAILURE_PART = 91; // 部分失败

		public static final int SUCCESS = 3; // 成功

		public static final int SUCCESS_PART = 5; // 部分成功

		public static final int SUCCESS_PART_RECHARGING = 6; // 部分成功,发货中

		public static final int SUCCESS_PART_RECHARGED = 7; // 部分成功,已经发货,还未全部成功

		public static final String ORDER_MANUAL_SUCCESS = "1000"; // 手工处理成功

		public static final String ORDER_MANUAL_FAIL = "1001"; // 手工处理失败

		public static final String ORDER_QUERY_ERROR = "1002";// 订单查询次数过多

		public static final int PRE_SUCCESS_STATUS_NO_NEED = 0;// 无需预处理成功

		public static final int PRE_SUCCESS_STATUS_WAIT = 1;// 待处理为预成功

		public static final int PRE_SUCCESS_STATUS_DONE = 2;// 预处理为成功
	}

	/**
	 * 订单手工处理标示
	 * 
	 * @ClassName: OrderManualFlag @Description: TODO @author
	 *             Comsys-Administrator @date 2014年4月2日 下午2:00:32
	 */
	public class OrderManualFlag
	{
		public static final int ORDER_MANUAL_FLAG_NO_NEED = 0;// 0:无需手工处理

		public static final int ORDER_MANUAL_FLAG_ING = 1;// 1:已转手工处理

		public static final int ORDER_MANUAL_FLAG_DONE = 2;// 2:手工处理完成
	}

	/**
	 * 通知状态常量
	 * 
	 * @author Jinger 2014-03-07
	 */
	public class NotifyStatus
	{
		public static final int NO_NEED_NOTIFY = 0; // 无需通知

		public static final int WAIT_NOTIFY = 1; // 等待通知

		public static final int NOTIFYING = 2; // 正在通知

		public static final int NOTIFY_SUCCESS = 3; // 通知成功

		public static final int NOTIFY_FAIL = 4; // 通知失败
	}

	/**
	 * 账户类型常量
	 * 
	 * @author Jinger 2014-03-07
	 */
	public class AccountType
	{
		public static final long SYSTEM_DEBIT = 149001l; // 系统借记账户

		public static final long SYSTEM_PROFIT = 148001l; // 商户系统利润账户

		public static final long MERCHANT_DEBIT = 148000l; // 商户借记账户

		public static final long MERCHANT_CREDIT = 149000l; // 商户贷记账户

		public static final long SYSTEM_PROFIT_OWN = 204001l; // 系统利润户

		public static final long SYSTEM_MIDDLE_PROFIT = 204000l; // 系统中间利润户

		public static final long REBATE_DEAL = 45000l; // 返用应付账户

		public static final long SUPPLY_PROFIT = 45001l; // 供货商利润账户

		public static final long ENABLE = 1;

		public static final long DISABLE = 0;

		public static final int SYSTEM_DEBIT_NUM = 10; // 系统借记账户分表数
	}

	/**
	 * @author Administrator
	 */
	public class OrderResponse
	{
		public static final String ORDER_RESPONSE_TYPE_QUERY = "QUERY";

		public static final String ORDER_RESPONSE_TYPE_REQUEST = "REQUEST";
	}

	/**
	 * 发送状态
	 * 
	 * @author Administrator
	 */
	public class Delivery
	{
		public static final int DELIVERY_STATUS_WAIT = 0; // 等待发货中

		public static final int DELIVERY_STATUS_SENDING = 1; // 发货采购中

		public static final int DELIVERY_STATUS_SENDED = 2; // 已经发货

		public static final int DELIVERY_STATUS_FAIL = 3; // 发货失败

		public static final int DELIVERY_STATUS_SUCCESS = 4; // 发货成功

		public static final int QUERY_FLAG_NO_NEED = 0; // 0-无需查询

		public static final int QUERY_FLAG_WAIT_QUERY = 1; // 1-待查询

		public static final int QUERY_FLAG_QUERYING = 2; // 2-查询中

		public static final int QUERY_FLAG_QUERY_END = 3; // 3-查询结束
	}

	/**
	 * 淘宝订单状态
	 * 
	 * @author Jinger 2014-03-24
	 */
	public class TBOrderStatus
	{
		public static final String ORDER_FAILED = "ORDER_FAILED";

		public static final String CANCEL = "CANCEL";

		public static final String SUCCESS = "SUCCESS";

		public static final String UNDERWAY = "UNDERWAY";

		public static final String FAILED = "FAILED";

		public static final String REQUEST_FAILED = "REQUEST_FAILED";
	}

	/**
	 * 商户状态常量
	 * 
	 * @author Jinger 2014-03-17
	 */
	public class MerchantStatus
	{
		// 初始化状态
		public static final String MERCHANT_INIT = "2";

		public static final String ENABLE = "0";

		public static final String DISABLE = "1";
	}

	/**
	 * 下游商户产品状态
	 * 
	 * @author Administrator
	 */
	public class AgentProductStatus
	{
		public static final String CLOSE_STATUS = "0";

		public static final String OPEN_STATUS = "1";
	}

	/**
	 * 上游商户产品状态
	 * 
	 * @author Administrator
	 */
	public class SupplyProductStatus
	{
		public static final String CLOSE_STATUS = "0";

		public static final String OPEN_STATUS = "1";
	}

	/**
	 * 产品常量
	 * 
	 * @author Administrator
	 */
	public class Product
	{
		public static final String DEFAULT_SELECT_VAL = "AH_";

		public static final String INIT_UP_PRODUCT_REALTION_QUALITY = "100";

		// public static final String INIT_UP_PRODUCT_REALTION_DISCOUNT = "80";

		public static final String PRODUCT_OPEN = "1";

		public static final String PRODUCT_CLOSE = "0";

		public static final String PRODUCT_DELETE = "9";

		public static final String NO_PRODUCT_NAME = "暂无";
	}

	public class MerchantResponseStatus
	{
		// 1.重试 2.重绑 3.强制关闭 4.正常成功 5.正常失败
		public static final int RETRY_STATUS = 1;

		public static final int RE_BIND_STATUS = 2;

		public static final int CLOSE_STATUS = 3;

		public static final int SUCCESS_STATUS = 4;

		public static final int FAIL_STATUS = 5;
	}

	public class DateUnit
	{
		public static final String TIME_UNIT_SECOND = "s";

		public static final String TIME_UNIT_MINUTE = "m";

		public static final String TIME_UNIT_HOUR = "h";

		public static final String TIME_UNIT_DAY = "d";

		public static final long DEFAULT_INTERVAL_TIME = 10l;
	}

	/**
	 * 用户状态
	 * 
	 * @author Administrator
	 */
	public class CustomerStatus
	{
		public static final String CLOSE_STATUS = "1";

		public static final String OPEN_STATUS = "0";

		public static final String LOGIN_PWD = "87654321";

		public static final String PAY_PWD = "98765432";

		public static final String BEGIN_PWD = "365";

		public static final String END_PWD = "yc";

	}

	/**
	 * Identity状态
	 * 
	 * @author Administrator
	 */
	public class IdentityStatus
	{
		public static final String CLOSE_STATUS = "1";

		public static final String OPEN_STATUS = "0";

		public static final String DELETE_STATUS = "-1";
	}

	/**
	 * 菜单状态
	 * 
	 * @author Administrator
	 */
	public class MenuStatus
	{
		public static final String CLOSE_STATUS = "1";

		public static final String OPEN_STATUS = "0";
	}

	/**
	 * 页面资源状态
	 * 
	 * @author Administrator
	 */
	public class PageResourceStatus
	{
		public static final String CLOSE_STATUS = "1";

		public static final String OPEN_STATUS = "0";
	}

	/**
	 * 角色状态
	 * 
	 * @author Administrator
	 */
	public class RoleStatus
	{
		public static final String CLOSE_STATUS = "1";

		public static final String OPEN_STATUS = "0";
	}

	/**
	 * 角色状态
	 * 
	 * @author Administrator
	 */
	public class RoleType
	{
		public static final String ROLE_TYPE_SP = "SP";

		public static final String ROLE_TYPE_MERCHANT = "MERCHANT";

		public static final String ROLE_TYPE_CUSTOMER = "CUSTOMER";
	}

	public class SecurityCredentialStatus
	{
		public static final String ENABLE_STATUS = "0";// 启用

		public static final String DISABLE_STATUS = "1";// 禁用

		public static final String DELETE_STATUS = "2"; // 删除

		public static final String EXPIRATION_STATUS = "3"; // 已过期

	}

	// public class OrderReason{
	// public static final String ORDER_MANUAL_SUCCESS = "1000";
	// public static final String ORDER_QUERY_ERROR = "1001";
	// public static final String ORDER_MANUAL_FAIL = "1002";
	// public static final String ORDER_SUCCESS = "1003";
	// public static final String ORDER_FAIL = "1004";
	// public static final String ORDER_WAIT_SEND = "1005";
	// public static final String ORDER_SENDING = "1006";
	// }

	public class AssignExclude
	{
		public static final String RULE_TYPE_ASSIGN = "1";

		public static final String RULE_TYPE_EXCLUDE = "2";
	}

	/**
	 * 参数配置
	 */
	public class ParameterConfiguration
	{
		public static final String BIND_INTERVAL_TIME = "bind_interval_time";// 绑定间隔时间(分钟)

		public static final String DELIVERY_INTERVAL_TIME = "delivery_interval_time";// 发货间隔时间(分钟)

		public static final String RANDOM_BINDING_TIMES_CONSTANT = "random_binding_times";// "绑定随机次数";

		public static final String NOTIFY_MAX_TIMES = "notify_max_times";// 通知最大次数

		public static final String DELIVERY_TIMES_CONSTANT = "delivery_times";// "发货次数";

		public static final String SCANNINGORDERS_CONSTANT = "scanning_orders";// "扫描订单量";

		public static final String TIMEOUT_CONSTANT = "time_out_order";// "超时订单";

		public static final String MANUALAUDIT_CONSTANT = "manual_audit_order";// "人工审核";

		public static final String FAKE_SUPPLY_MERCHANT_NAME = "fakeSupplyMerchant";// 假上游名称

		public static final String FAKE_AGENT_MERCHANT_NAME = "fakeAgentMerchant";// 假下游名称

		public static final String POLLING_INTERVAL_TIME = "polling_interval_time";// 10分钟轮询发货状态

		public static final String DEFAULT_INTERVAL_TIME = "default_interval_time";// 默认查询间隔数

		public static final String SCANNER_BIND_NUM = "scanner_bind_num";// 定时任务，轮循绑定记录条数

		public static final String SCANNER_SEND_NUM = "scanner_send_num";// 定时任务，轮循发货记录条数

		public static final String SCANNER_QUERY_NUM = "scanner_query_num";// 定时任务，轮循查询记录条数

		public static final String SCANNER_NOTIFY_NUM = "scanner_notify_num";// 定时任务，轮循通知记录条数
	}

	// public class Pa
	public class OrderInitParames
	{
		public static final int ORDER_INIT_VERSION = 1;

		public static final int ORDER_INIT_SUCCESS_FEE = 0;

		public static final long ORDER_INIT_BIND_TIMES = 0;
	}

	public class RSACacheKey
	{
		public static final String RSA_PUBLICKEY = "RSApublicKey";// 公钥

		public static final String RSA_PRIVATEKEY = "RSAprivateKey";// 私钥

		public static final String RSA_KEY = "serverca";// Rsa密钥

		public static final String RSA_PUBLICKEY_OBJECT = "RSApublicKeyObject";// 公钥对象

		public static final String RSA_PRIVATEKEY_OBJECT = "RSAprivateKeyObject";// 私钥对象
	}

	public class SecurityCredential
	{
		public static final String LOGIN_MD5 = "LOGIN_MD5";

		public static final String PAY_MD5 = "PAY_MD5";

		public static final String MD5 = "MD5";

		public static final String RSA = "RSA";

		public static final String NO_NEED = "NO_NEED";

		public static final String SSL_TRUST_STORE = "";

		public static final String SSL_TRUST_STORE_PASSWORD = "";

		public static final String KEY_MD5 = "880101";

		public static final String KEY_3DES = "Key3Des";// 3des密钥

		public static final String KEY_LOGIN = "3311202";

		public static final String SECURITYNAME_LONGIN = "_登录密码";

		public static final String SECURITYNAME_PAY = "_支付密码";

		public static final String SECURITYNAME_MD5KEY = "_MD5Key";

		// 系统的md5
		public static final String SYSTEM_MD5_KEY_STRING = "yuecheng";

		public static final String SYSTEM_MD5_KEY = "4cd76129fef41c7fa1b7c4141dc0c2ca";

		// 系统md5key1
		public static final String SYSTEM_MD5_KEY_1 = "系统MD5Key1";

		// 系统md5key2
		public static final String SYSTEM_MD5_KEY_2 = "系统MD5Key2"; // 禁用

		public static final String UPLOAD_CERTFILE_NAME = "certfile";

		public static final String RSA_PUBLICKEY = "RSApublicKey";// 公钥

		public static final String RSA_PRIVATEKEY = "RSAprivateKey";// 私钥

		public static final String RSA_KEY = "serverca";// Rsa密钥

		public static final String DEFULT_PWD = "123456";// 默认密码

		public static final String DEFULT_VALIDITY_DATE = "9999-12-31 23:59:59";// 默认永久到期时间

		public static final String SYSTEM = "system";// 系统
	}

	public class Interface
	{
		public static final String INTERFACE_TYPE_SEND_ORDER = "send_order";

		public static final String INTERFACE_TYPE_AGENT_NOTIFY_ORDER = "agent_notify_order";

		public static final String INTERFACE_TYPE_SUPPLY_NOTIFY_ORDER = "supply_notify_order";

		public static final String INTERFACE_TYPE_SUPPLY_NOTIFY_ORDER_SUCCESS = "supply_notify_order_success";

		public static final String INTERFACE_TYPE_SUPPLY_NOTIFY_ORDER_FAIL = "supply_notify_order_fail";

		public static final String INTERFACE_TYPE_QUERY_ORDER = "query_order";

		public static final String INTERFACE_TYPE_RECIEVER_ORDER = "reciever_order";

		public static final String INTERFACE_TYPE_RECIEVER_TBORDER = "reciever_TBorder";

		public static final String INTERFACE_TYPE_AGENT_QUERY_ORDER = "agent_query_order";

		public static final String INTERFACE_TYPE_AGENT_QUERY_TBORDER = "agent_query_TBorder";

		public static final String INTERFACE_TYPE_AGENT_QUERY_ACCOUNT = "agent_query_account";

		public static final String PACKET_TYPE_XML = "text/xml";

		public static final String PACKET_TYPE_TEXT = "text/plain";

		public static final String INTERFACE_TYPE_IN = "in";

		public static final String INTERFACE_TYPE_OUT = "out";

		public static final String INTERFACE_INBODY_TRUE = "true";

		public static final String INTERFACE_INBODY_FALSE = "false";

		public static final String INTERFACE_PARAM_DATATYPE_STRING = "string";

		public static final String INTERFACE_PARAM_DATATYPE_DATE = "date";

		public static final String INTERFACE_PARAM_PARAMTYPE_PASSWORD = "password";

		public static final String INTERFACE_PARAM_PARAMTYPE_CONSTANT = "constant";

		public static final String INTERFACE_PARAM_PARAMTYPE_TRANSPARAM = "transParam";

		public static final String INTERFACE_PARAM_PARAMTYPE_AMOUNT = "amount";

		public static final String INTERFACE_PARAM_PARAMTYPE_DATE = "date";

		public static final String INTERFACE_PARAM_PARAMTYPE_RETURN_SUCCESS = "returnSuccess";

		public static final String INTERFACE_PARAM_PARAMTYPE_RETURN_FAIL = "returnFail";

		public static final String INTERFACE_CONNECTION_MODULE_REQUEST = "request";

		public static final String INTERFACE_CONNECTION_MODULE_RESPONSE = "response";

		public static final String INTERFACE_IS_CAPITAL_UP = "up";

		public static final String INTERFACE_IS_CAPITAL_DOWN = "down";

		public static final String INTERFACE_IS_CAPITAL_UNCHANGED = "unchanged";

		public static final String INTERFACE_CONNECTION_TYPE_HTTP = "http";

		public static final String INTERFACE_CONNECTION_TYPE_HTTPS = "https";

		public static final String INTERFACE_CONNECTION_TYPE_SOCKET = "socket";

		public static final String INTERFACE_CONNECTION_TYPE_SOCKETS = "ssh_socket";

		public static final long IS_CONF_OPEN = 1l;

		public static final long IS_CONF_CLOSE = 0l;

		public static final String OPEN = "open";

		public static final String CLOSE = "close";

		public static final String DEFAULT_ERROR_MSG = "<response><result>false</result><code>999</code><msg>系统异常</msg></response>";
	}

	public class ReportType
	{
		public static final String ACCOUNT_REPORTS = "Account_Reports"; // "账户统计";

		public static final String PROFIT_REPORTS = "Profit_Reports"; // "利润统计";

		public static final String TRANSACTION_REPORTS = "Transaction_Reports"; // "交易量";

		public static final String AGENT_TRANSACTION_REPORTS = "Agent_Transaction_Reports"; // "交易量";

		public static final String SUPPLY_TRANSACTION_REPORTS = "Supply_Transaction_Reports"; // "交易量";

		public static final String CCY_ACCOUNT_BALANCE_HISTORY_REPORTS = "ccy_account_balance_history_reports";// 虚拟账户资金变动

		public static final String CARD_ACCOUNT_BALANCE_HISTORY_REPORTS = "card_account_balance_history_reports";// 实体卡账户资金变动

		public static final String ORDER_REPORTS = "order_reports"; // 订单报表

		public static final String MERCHANTTYPE_AGENT = "AGENT";

		public static final String MERCHANTTYPE_SUPPLY = "SUPPLY";

		public static final String MERCHANTTYPE_AGENT_ZH_CN = "代理商";

		public static final String MERCHANTTYPE_SUPPLY_ZH_CN = "供货商";

		public static final String SPTYPE_ZH_CN = "系统";

		public static final String CUSTOMERTYPE_ZH_CN = "用户";

		public static final String TRANSACTION_STATUS_SUCCESS = "3"; // 已成交

		public static final String TRANSACTION_STATUS_ALL = "0";// 订单的全部状态

		public static final String REPORT_TERM_STATUS_CLOSE = "0";

		public static final String REPORT_TERM_STATUS_OPEN = "1";

		public static final String MERCHANT = "MERCHANT";

		public static final String REPORT_PROPERTY_DATE = "java.util.Date";

		public static final String REPORT_PROPERTY_STRING = "String";

		public static final String REPORT_PROPERTY_NUMBER = "Number";

		public static final String PROPERTY_ALL = "all";

		public static final String LOG_TYPE_PLUS = "plus";

		public static final String LOG_TYPE_SUBTRACT = "subtract";
	}

	public class ProfitImputation
	{
		public static final long NOT_IMPUTATION = 0;// 不用归集

		public static final long WAIT_IMPUTATION = 1;// 等待归集

		public static final long IS_IMPUTATION = 2;// 已经归集

		public static final long CONFIRM_IMPUTATION = 3;// 确认利润
	}

	public class ProductOperationHistory
	{
		public static final String CLOSE_STATUS = "0";

		public static final String OPEN_STATUS = "1";

		public static final String STATUS_INIT = "init";

		public static final String STATUS_DONE = "done";

		public static final String STATUS_CLOSE = "close";
	}

	public class MenuLevel
	{

		public static final String ZERO = "Zero"; // 一级菜单

		public static final String ONE = "One"; // 二级菜单

		public static final String TWO = "Two"; // 三级菜单

		public static final String THREE = "Three"; // 四级菜单
	}

	public class IdentityConstants
	{
		// 商户状态
		public static final String MERCHANT_ENABLE = "0"; // 启用

		public static final String MERCHANT_DISABLE = "1"; // 禁用

		public static final String MERCHANT_INIT = "2"; // 初始化

		public static final String MERCHANT_DELETE = "3"; // 删除

		// 操作员状态
		public static final String OPERATOR_ENABLE = "0"; // 启用

		public static final String OPERATOR_DISABLE = "1"; // 禁用

		public static final String OPERATOR_DELETE = "3"; // 删除

		// 密钥状态
		public static final String SECURITY_CREDENTIAL_ENABLE = "0"; // 启用

		public static final String SECURITY_CREDENTIAL_DISABLE = "1"; // 禁用

		public static final String SECURITY_CREDENTIAL_ACTIVE = "2";// 待激活

		// 密钥前缀
		public static final String SECURITY_CREDENTIAL_PREFIX = "365"; // 启用

		public static final String SECURITY_CREDENTIAL_LASTFIX = "yc"; // 禁用

	}

	/**
	 * @ClassName: BatchOrderRequestHandlerStatus
	 * @Description: 批量手工补单状态
	 * @author 肖进
	 * @date 2014年8月14日 下午2:26:53
	 */
	public class BatchOrderRequestHandlerStatus
	{
		// 上传初始状态：待审核
		public static final long WAIT_AUDIT = 0;

		// 审核：待充值
		public static final long WAIT_RECHARGE = 1;

		// 批量提交：下单成功
		public static final long ORDER_OK = 2;

	}

	public class EncryptType
	{
		// 加密类型：MD5、3DES、RSA
		public static final String ENCRYPT_TYPE_MD5 = "MD5";

		public static final String ENCRYPT_TYPE_3DES = "3DES";

		public static final String ENCRYPT_TYPE_RSA = "RSA";

		public static final String ENCRYPT_TYPE_JSRSA = "JSRSA";
	}

	public class SecurityType
	{
		public static final long OPEN_STATUS = 0; // 启用

		public static final long CLOSE_STATUS = 1; // 禁用

		public static final long LOGIN_PWD_TYPE = 50;// 登录密码类型

		public static final long AGENT_MERCHANT_MD5KEY = 51;// 商户的md5key

	}

	public class SecurityCredentialType
	{
		// PASSWORD("01"), SPMD5KEY("02"), AGENTMD5KEY("03"),
		// SUPPLYMD5KEY("04"), SUPPLYPUBLICKEY(
		// "05"), DESKEY("06");
		public static final String PASSWORD = "PASSWORD";

		public static final String SPMD5KEY = "SPMD5KEY";

		public static final String AGENTMD5KEY = "AGENTMD5KEY";

		public static final String SUPPLYMD5KEY = "SUPPLYMD5KEY";

		public static final String SUPPLYPUBLICKEY = "SUPPLYPUBLICKEY";

		public static final String DESKEY = "DESKEY";

		public static final String SUPPLYMERCHANT = "SUPPLYMERCHANT";

	}

	public class SecurityRule
	{
		public static final String NEED = "0";

		public static final String NOTNEED = "1";
	}

	public class TransactionCode
	{
		public static final String ERROR_CODE = "errorCode";

		public static final String RESULT = "result";

		public static final String MSG = "msg";
	}

	public class StringSplitUtil
	{
		public static final String DECODE = "\\|";

		public static final String ENCODE = "|";
	}

	public class RecordStatus
	{
		public static final String INITIALIZATION = "0";

		public static final String PENDING = "1";

		public static final String SUCCESS = "2";

		public static final String FAILURE = "3";

	}

	public class TransactionReportRecord
	{
		public static final String TYPE_TRANSACTION = "0";

		public static final String TYPE_AGENT = "1";

		public static final String TYPE_SUPPLY = "2";
	}

	public class StatusEventType
	{
		public static final String ORDER = "orderStatus";

		public static final String DELIVERY = "deliveryStatus";

		public static final String DELIVERY_QUERY = "delvieryQueryStatus";

		public static final String NOTIFY = "notifyStatus";
	}

	public class AccountFundChange
	{
		public static final String LOGTYPE_TRANSACTION = "1";// 交易

		public static final String LOGTYPE_ACCOUNT = "2";// 账户

	}
}
