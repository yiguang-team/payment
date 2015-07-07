package com.yiguang.payment.payment.order.service;

import com.yiguang.payment.common.query.YcPage;
import com.yiguang.payment.payment.order.entity.MerchantOrder;
import com.yiguang.payment.payment.order.vo.MerchantOrderVO;

public interface MerchantOrderService
{
	// public YcPage<MerchantOrderVO> queryMerchantOrderList(Map<String, Object>
	// searchParams, int pageNumber,
	// int pageSize, String sortType);

	public MerchantOrderVO copyPropertiesToVO(MerchantOrder temp);

	public MerchantOrder createMerchantOrder(MerchantOrder temp);

	public MerchantOrder updateMerchantOrder(MerchantOrder merchantOrder);

	public MerchantOrder queryMerchantOrderByOrderId(String orderId);

	public MerchantOrder queryMerchantOrderByMerchantOrderId(String merchantOrderId, long merchantId);

	public void updateProductOrderStatus();

	YcPage<MerchantOrderVO> queryMerchantOrderList( int pageNumber, int pageSize,
			String sortType, String orderId, String merchantOrderId, String requestIp, String username, String mobile,
			long carrierId, long channelId, long supplierId, String provinceId, String cityId, long productId,
			int payStatus, int deliveryStatus, String deliveryNo, long chargingPointId,
			int chargingType,int channelType, int notifyStatus, String beginDate, String endDate);

}
