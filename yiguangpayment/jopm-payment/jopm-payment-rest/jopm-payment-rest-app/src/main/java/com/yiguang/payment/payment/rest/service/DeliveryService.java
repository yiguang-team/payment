package com.yiguang.payment.payment.rest.service;

import com.yiguang.payment.payment.order.entity.MerchantOrder;

public interface DeliveryService
{
	MerchantOrder delivery(MerchantOrder merchantOrder);
}
