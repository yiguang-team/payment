package com.yiguang.payment.payment.rest.service;

import com.yiguang.payment.payment.order.entity.MerchantOrder;
import com.yiguang.payment.payment.rest.entity.RestResult;

/**
 * 短验接口服务实现类
 * 
 * @author Administrator
 * 
 */
public interface SmsVerifyPaymentService
{
	/**
	 * 短信验证码下发接口
	 * 
	 * @param cpid
	 * @param serviceid
	 * @param datetime
	 * @param mobile
	 * @param operator
	 * @return
	 */
	RestResult sendsmscode(MerchantOrder mo);

	/**
	 * 短验支付接口
	 * 
	 * @param cpid
	 * @param serviceid
	 * @param datetime
	 * @param mobile
	 * @param orderid
	 * @param smscode
	 * @param username
	 * @param operator
	 * @return
	 */
	RestResult smscharge(MerchantOrder mo);
}
