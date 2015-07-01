package com.yiguang.payment.payment.order.service.scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yiguang.payment.payment.order.service.MerchantOrderService;

@Service("productOrderPendingQueryScanner")
public class ProductOrderPendingQueryScanner
{
	private static Logger logger = LoggerFactory.getLogger(ProductOrderPendingQueryScanner.class);

	@Autowired
	private MerchantOrderService merchantOrderService;

	// 清理超时未支付的商品订单
	public void updateProductOrderStatus()
	{
		logger.debug("清理超时未支付的商品订单[开始]!");
		merchantOrderService.updateProductOrderStatus();
		logger.debug("清理超时未支付的商品订单[结束]!");
	}

}
