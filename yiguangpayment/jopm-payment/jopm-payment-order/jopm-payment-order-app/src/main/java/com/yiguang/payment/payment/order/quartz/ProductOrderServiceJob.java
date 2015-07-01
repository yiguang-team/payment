package com.yiguang.payment.payment.order.quartz;

import com.yiguang.payment.payment.order.service.scanner.ProductOrderPendingQueryScanner;

public class ProductOrderServiceJob
{
	ProductOrderPendingQueryScanner productOrderPendingQueryScanner;

	public ProductOrderPendingQueryScanner getProductOrderPendingQueryScanner()
	{
		return productOrderPendingQueryScanner;
	}

	public void setProductOrderPendingQueryScanner(ProductOrderPendingQueryScanner productOrderPendingQueryScanner)
	{
		this.productOrderPendingQueryScanner = productOrderPendingQueryScanner;
	}

	public void execute()
	{
		productOrderPendingQueryScanner.updateProductOrderStatus();
	}

}
