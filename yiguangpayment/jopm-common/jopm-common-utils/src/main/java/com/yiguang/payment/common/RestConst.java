package com.yiguang.payment.common;

import java.text.SimpleDateFormat;

public interface RestConst
{
	public static final SimpleDateFormat SDF_14 = new SimpleDateFormat("yyyyMMddHHmmss");

	public static final long CHANNEL_ID_UNICOM = 2;
	public static final long CHANNEL_ID_TELECOM = 3;

	public static final String YIGUANG_MERCHANT_ID = "1000";

	/**
	 * Unicom
	 */
	public static final String CU_CPID = "100008";
	public static final String CU_KEY = "a24vt7dad954443vgfff15395b67549";
	public static final String KEYVERSION = "1";
	public static final long UNICOM_CHANNELID = 2;

	/**
	 * telecom
	 */
	public static final String TELECOM_SMS_PAY_URL = "https://webpaywg.bestpay.com.cn/payWebDirect.do";
	public static final String CT_KEY = "300EC08B686391AE46703DD56CCAACD1DD31265C9FCEE3DA";
	public static final String CT_CPID = "02430108043096000";
	public static final String TEL_SMS_NOTIFY_URL = "http://58.67.196.166:17200/rest/notify/telecom/smsCallBack";
	public static final String SMS_NOTIFY_URL = "http://58.67.196.166:17200/rest/mall/junwang/queryOrder";
	public static final String YI_NOTIFY_URL = "http://58.67.196.166:17100/mportal/rest/queryOrder";
	public static final String RONGHAN_QUERY_ORDER_URL = "http://58.67.196.166:9000/aportal/mall/ronghan/queryOrder";
	/**
	 * 骏网
	 */
	public static final String APP_AGENT_ID = "1884247";
	public static final String APP_AGENT_KEY = "yuecheng2014";
	public static final String JNET_VALIDATION = "http://211.154.166.211/Service/Personal/CheckAccount.aspx";
	public static final String JNET_CHARGE = "http://211.154.166.211/Service/Personal/Submit.aspx";
	public static final String JNET_QUERY = "http://211.154.166.211/Service/Personal/Query.aspx";
	public static final String CLIENT_IP = "58.67.196.164";

	public static final long JUNWANG_YIKATONG_ID = 1000;

}
