package com.yiguang.payment.payment.service;

import java.util.List;

import com.yiguang.payment.payment.entity.ChannelMerchantRelation;
import com.yiguang.payment.payment.vo.ChannelMerchantRelationVO;

public interface ChannelMerchantRelationService
{

	public ChannelMerchantRelationVO copyPropertiesToVO(ChannelMerchantRelation temp);

	public List<ChannelMerchantRelation> queryChannelMerchantRelationByMerchantId(long merchantId);

	public List<ChannelMerchantRelationVO> queryChannelMerchantRelationVOByMerchantId(long merchantId);
	
	public ChannelMerchantRelation saveChannelMerchantRelation(ChannelMerchantRelation relation);

	public String delete(List<ChannelMerchantRelation> list);

	public String save(List<ChannelMerchantRelation> list);
	
	public List<ChannelMerchantRelation> queryChannelMerchantRelationByChannelId(long channelId);
}
