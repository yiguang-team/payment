package com.yiguang.payment.payment.rest.service;

import com.yiguang.payment.payment.order.entity.MerchantOrder;

/**
 * 通知商户支付结果
 * 
 * @author Administrator
 * 
 */
public interface NotifyService
{
	/**
	 * 通知商户支付结果
	 * 
	 * @param cpid
	 * @param serviceid
	 * @param datetime
	 * @param mobile
	 * @param operator
	 * @return
	 */
	public MerchantOrder notify(MerchantOrder merchantOrder);
}
