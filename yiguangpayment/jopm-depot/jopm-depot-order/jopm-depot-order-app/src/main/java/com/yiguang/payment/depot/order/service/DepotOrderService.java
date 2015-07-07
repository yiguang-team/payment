package com.yiguang.payment.depot.order.service;

import java.util.List;

import com.yiguang.payment.common.query.YcPage;
import com.yiguang.payment.depot.order.entity.DepotOrder;
import com.yiguang.payment.depot.order.vo.DepotOrderVO;
import com.yiguang.payment.depot.vo.CardAndPwdVO;

public interface DepotOrderService
{

	// 接口内申明提卡函数
	List<CardAndPwdVO> pickUpCard(DepotOrder pickUpCardRecord, int count);

	// 接口内申明提卡函数,该函数不返回卡密列表，只返回对应的卡密提取记录ID
	long pickUpCardWithoutList(DepotOrder pickUpCardRecord);

	// 接口内声明提卡记录函数
	YcPage<DepotOrderVO> queryPickUpRecordList(DepotOrderVO searchParams, int pageNumber, int pageSize,
			String sortType);

	// 接口内声明查询历史卡密信息函数
	List<CardAndPwdVO> getHisCardAndPwdByCond(String orderId, String merchantId);
}
