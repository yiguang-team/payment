package com.yiguang.payment.merchantOperate.service;

import java.util.List;

import com.yiguang.payment.merchantOperate.entity.MobileAndTotal;
import com.yiguang.payment.payment.order.vo.MerchantOrderVO;


public interface MerchantOperateService {
	String getMessage(String userName, String serviceid);

	String getTotal(String userName,String beginDate,String endDate);

	List<MobileAndTotal> getListMAT(String userName,String beginDate,String endDate);

	List<MerchantOrderVO> getOrderList(String orderId, String mobile,
			String payAmount, String provinceId, String username,
			String requestTime, String endDate);
	
}
