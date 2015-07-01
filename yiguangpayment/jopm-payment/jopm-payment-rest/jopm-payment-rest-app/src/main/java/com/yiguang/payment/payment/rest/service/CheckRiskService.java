package com.yiguang.payment.payment.rest.service;

import com.yiguang.payment.payment.order.entity.MerchantOrder;

public interface CheckRiskService
{
	MerchantOrder checkRiskAndChannel(MerchantOrder merchantOrder);
	
	void checkRisk(MerchantOrder mo, long channelId);
}
