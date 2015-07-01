package com.yiguang.payment.business.product.service;

import java.util.List;

import com.yiguang.payment.business.product.entity.PointChannelRelation;
import com.yiguang.payment.business.product.vo.PointChannelRelationVO;

public interface PointChannelRelationService
{

	public PointChannelRelationVO copyPropertiesToVO(PointChannelRelation temp);

	public List<PointChannelRelation> queryPointChannelRelationByPointId(long pointId);

	public List<PointChannelRelationVO> queryPointChannelRelationVOByPointId(long pointId);

	public PointChannelRelation queryPointChannelRelationByPointIdAndChannelId(long pointId, long channelId);

	public List<PointChannelRelation> queryPointChannelRelationByChannelId(long channelId);
	
	public PointChannelRelation savePointChannelRelation(PointChannelRelation pointChannelRelation);
	
	public String delete(List<PointChannelRelation> list);
	
	public String save(List<PointChannelRelation> list);
}
