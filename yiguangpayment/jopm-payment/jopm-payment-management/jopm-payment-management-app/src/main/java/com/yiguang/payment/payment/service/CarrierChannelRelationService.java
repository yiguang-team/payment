package com.yiguang.payment.payment.service;

import java.util.List;

import com.yiguang.payment.payment.entity.CarrierChannelRelation;
import com.yiguang.payment.payment.vo.CarrierChannelRelationVO;

public interface CarrierChannelRelationService {
	public CarrierChannelRelationVO copyPropertiesToVO(CarrierChannelRelation temp);

	public List<CarrierChannelRelation> queryCarrierChannelRelationByChannelId(long channelId);

	public List<CarrierChannelRelationVO> queryCarrierChannelRelationVOByChannelId(long channelId);
	
	public CarrierChannelRelation saveCarrierChannelRelation(CarrierChannelRelation relation);
	
	public String deleteCarrierChannelRelation(CarrierChannelRelation relation);

//	public String delete(List<CarrierChannelRelation> list);
//
//	public String save(List<CarrierChannelRelation> list);
	
	public List<CarrierChannelRelation> queryCarrierChannelRelationByCarrierId(long carrierId);

	public String update(List<CarrierChannelRelation> oldList,
			List<CarrierChannelRelation> newList);
	
}
