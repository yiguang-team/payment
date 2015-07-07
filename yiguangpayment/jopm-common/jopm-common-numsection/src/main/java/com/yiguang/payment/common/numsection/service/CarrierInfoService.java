package com.yiguang.payment.common.numsection.service;

import com.yiguang.payment.common.numsection.entity.CarrierInfo;
import com.yiguang.payment.common.numsection.vo.CarrierInfoVO;
import com.yiguang.payment.common.query.YcPage;

public interface CarrierInfoService
{
	public CarrierInfo saveCarrierInfo(CarrierInfo carrierInfo);

	public String deleteCarrier(CarrierInfo temp);

	public CarrierInfo queryCarrierInfo(long id);

	YcPage<CarrierInfoVO> queryCarrierInfoList(CarrierInfoVO conditionVO, int pageNumber, int pageSize,
			String sortType);

	public CarrierInfoVO copyPropertiesToVO(CarrierInfo temp);

	public CarrierInfo updateCarrierStatus(CarrierInfo carrierInfo);
}
