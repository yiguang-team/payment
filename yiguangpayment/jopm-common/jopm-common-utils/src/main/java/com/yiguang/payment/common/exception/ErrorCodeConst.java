package com.yiguang.payment.common.exception;

public interface ErrorCodeConst
{
	public static final int ErrorCode90000 = 90000;
	public static final int ErrorCode90001 = 90001;
	public static final int ErrorCode90002 = 90002;
	public static final int ErrorCode90003 = 90003;
	public static final int ErrorCode90004 = 90004;
	public static final int ErrorCode90005 = 90005;
	public static final int ErrorCode90006 = 90006;
	public static final int ErrorCode90007 = 90007;
	public static final int ErrorCode90008 = 90008;
	public static final int ErrorCode90009 = 90009;
	public static final int ErrorCode90010 = 90010;
	public static final int ErrorCode90011 = 90011;
	public static final int ErrorCode90012 = 90012;
	public static final int ErrorCode90013 = 90013;
	public static final int ErrorCode90014 = 90014;
	public static final int ErrorCode90015 = 90015;
	public static final int ErrorCode90016 = 90016;
	public static final int ErrorCode90017 = 90017;
	public static final int ErrorCode90018 = 90018;
	public static final int ErrorCode90019 = 90019;
	public static final int ErrorCode90020 = 90020;
	public static final int ErrorCode90021 = 90021;
	public static final int ErrorCode90022 = 90022;
	public static final int ErrorCode90023 = 90023;
	public static final int ErrorCode99999 = 99999;
	/**
	 * 一天一个骏网直充账户只能用5个号码充值
	 */
	public static final int ErrorCode11005 = 11005;
	/**
	 * 系统异常
	 */
	public static final int ErrorCode99998 = 99998;

	/**
	 * numsection
	 */
	public static final int ErrorCode10201 = 10201;
	public static final int ErrorCode10202 = 10202;

	/**
	 * PAYMENT
	 */
	public static final int ErrorCode10301 = 10301;
	public static final int ErrorCode10302 = 10302;
	public static final int ErrorCode10303 = 10303;
	public static final int ErrorCode10304 = 10304;
	public static final int ErrorCode10305 = 10305;
	public static final int ErrorCode10306 = 10306;
	public static final int ErrorCode10307 = 10307;
	public static final int ErrorCode10308 = 10308;
	public static final int ErrorCode10309 = 10309;
	public static final int ErrorCode10310 = 10310;
	public static final int ErrorCode10311 = 10311;
	public static final int ErrorCode10312 = 10312;
	public static final int ErrorCode10313 = 10313;
	public static final int ErrorCode10314 = 10314;
	public static final int ErrorCode10315 = 10315;
	/**
	 * product
	 */
	public static final int ErrorCode10401 = 10401;
	public static final int ErrorCode10402 = 10402;
	public static final int ErrorCode10403 = 10403;
	public static final int ErrorCode10404 = 10404;
	public static final int ErrorCode10405 = 10405;
	public static final int ErrorCode10406 = 10406;
	
	public static final int ErrorCode10408 = 10408;
	
	/**
	 * risk
	 */
	public static final int ErrorCode10501 = 10501;
	public static final int ErrorCode10502 = 10502;
	public static final int ErrorCode10503 = 10503;
	public static final int ErrorCode10504 = 10504;
	public static final int ErrorCode10505 = 10505;
	public static final int ErrorCode10506 = 10506;
	public static final int ErrorCode10507 = 10507;
	public static final int ErrorCode10508 = 10508;
	public static final int ErrorCode10509 = 10509;
	public static final int ErrorCode10510 = 10510;
	public static final int ErrorCode10511 = 10511;
	public static final int ErrorCode10512 = 10512;
	public static final int ErrorCode10513 = 10513;
	public static final int ErrorCode10514 = 10514;
	public static final int ErrorCode10515 = 10515;
	public static final int ErrorCode10516 = 10516;
	public static final int ErrorCode10517 = 10517;
	public static final int ErrorCode10518 = 10518;
	public static final int ErrorCode10519 = 10519;
	public static final int ErrorCode10520 = 10520;
	public static final int ErrorCode10521 = 10521;
	public static final int ErrorCode10522 = 10522;

	public static final int ErrorCode10531 = 10531;
	public static final int ErrorCode10532 = 10532;
	public static final int ErrorCode10533 = 10533;
	public static final int ErrorCode10534 = 10534;

	public static final int ErrorCode10541 = 10541;
	public static final int ErrorCode10542 = 10542;
	public static final int ErrorCode10543 = 10543;
	public static final int ErrorCode10544 = 10544;
	/**
	 * depot
	 */
	public static final int ErrorCode10601 = 10601;
	public static final int ErrorCode10602 = 10602;
	public static final int ErrorCode10603 = 10603;

	// 成功
	public static final String CODE_SUCCESS = "0000";

	// 产品ID格式不正确
	public static final String CODE_PROID_FORMATE_ERR = "9005";

	// 订单号格式不正确
	public static final String CODE_ORDERID_FORMATE_ERR = "9000";

	// 代理商ID格式不正确
	public static final String CODE_DEALERID_FORMATE_ERR = "9004";

	// 请求时间格式不正确
	public static final String CODE_DATETIME_FORMATE_ERR = "9006";

	// 签名格式不正确
	public static final String CODE_SIGN_FORMATE_ERR = "9007";

	// 成交金额格式不正确
	public static final String CODE_PAYAMOUNT_FORMATE_ERR = "9001";

	// 请求卡密数量格式不正确
	public static final String CODE_COUNT_FORMATE_ERR = "9002";

	// 请求超时
	public static final String CODE_REQUEST_TIMEOUT = "9008";

	// 签名不匹配
	public static final String CODE_SIGN_MATCH_FAILED = "9011";

	// 提卡失败
	public static final String CODE_PICKUP_CARD_ERR = "9999";

	// 请求产品数据量不足
	public static final String CODE_CARD_NOTENOUGH = "9009";

	// 计费点未启用
	public static final String CODE_PROD_UNUSABLE = "8003";

	// 代理商未启用
	public static final String CODE_MERCHANT_UNUSABLE = "8002";

	// 运营商不存在
	public static final int CODE_CARRIER_NOT_EXISTS = 91024;

	// 产品不存在
	public static final int CODE_PRODUCT_NOT_EXISTS = 91025;

	// 产品运营商 关联不存在
	public static final int CODE_PRODUCT_CARRIER_NOT_EXISTS = 91026;

	// 面值格式不正确
	public static final int CODE_FACE_AMOUNT_FORMATE_ERR = 91027;

	// 面值计费点不存在
	public static final int CODE_FACE_AMOUNT_POINT_NOT_EXISTS = 91028;

	// 计费点充值类型不正确
	public static final int CODE_POINT_CHARGING_TYPE_ERR = 91030;

	// 卡号格式不正确
	public static final int CODE_CARD_NO_FORMATE_ERR = 91013;

	// 卡号存在
	public static final int CODE_CARD_NO_EXISTS = 91029;

	// 密码格式不正确
	public static final int CODE_PASSWORD_FORMATE_ERR = 91017;

	// 批次ID格式不正确
	public static final int CODE_BATCHID_FORMATE_ERR = 91018;

	// 卡密入库异常
	public static final int CODE_IMPORT_CARD_ERR = 91019;

	// 卡密不存在
	public static final int CODE_CARD_NOT_EXISTS = 91001;

}
