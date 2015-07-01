package com.yiguang.payment.payment.service;

import java.util.Map;

import com.yiguang.payment.common.query.YcPage;
import com.yiguang.payment.payment.entity.ChannelParms;
import com.yiguang.payment.payment.vo.ChannelParmsVO;

public interface ChannelParmsService {
	public ChannelParms updateChannelParmsStatus(ChannelParms channelParms);

	public String deleteChannelParms(ChannelParms channelParms);

	public ChannelParms saveChannelParms(ChannelParms channelParms);

	public ChannelParms queryChannelParms(long id);
	
	public String queryChannelParms(long channelId, String key);
	
	public YcPage<ChannelParmsVO> queryChannelParmsList(Map<String, Object> searchParams, int pageNumber, int pageSize,
			String sortType);

	public ChannelParmsVO copyPropertiesToVO(ChannelParms temp);

}
