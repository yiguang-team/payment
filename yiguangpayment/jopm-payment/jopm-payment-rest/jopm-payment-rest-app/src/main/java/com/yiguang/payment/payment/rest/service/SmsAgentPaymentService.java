package com.yiguang.payment.payment.rest.service;


/**
 * 短代接口服务实现类
 * 
 * @author Administrator
 * 
 */
public interface SmsAgentPaymentService
{
	/**
	 * 短代获取短信接口
	 * 
	 * @param cpid
	 * @param serviceid
	 * @param datetime
	 * @param sign
	 * @return
	 * @throws PaymentApplicationException
	 */
	String fee(String cpid, String serviceid, String datetime, String sign);
}
