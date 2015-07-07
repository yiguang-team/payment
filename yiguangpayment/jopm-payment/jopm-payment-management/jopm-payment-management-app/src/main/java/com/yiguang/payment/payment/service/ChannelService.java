package com.yiguang.payment.payment.service;

import java.util.List;

import com.yiguang.payment.common.query.YcPage;
import com.yiguang.payment.payment.entity.Channel;
import com.yiguang.payment.payment.vo.ChannelVO;

public interface ChannelService {

	public Channel updateChannelStatus(Channel cf);

	public String deleteChannel(Channel cf);

	public Channel saveChannel(Channel cf);

	public Channel queryChannel(long id);
	
	public YcPage<ChannelVO> queryChannelList(ChannelVO conditionVO, int pageNumber, int pageSize,
			String sortType);

	public ChannelVO copyPropertiesToVO(Channel temp);

	public Channel saveChannel(Channel channel, String carrierChannelRelationIDs);
	
	public List<Channel> queryChannelbyCarrier(long carrierId);
}
