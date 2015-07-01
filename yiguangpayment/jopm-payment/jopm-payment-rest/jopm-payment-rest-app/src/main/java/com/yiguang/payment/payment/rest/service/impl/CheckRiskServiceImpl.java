package com.yiguang.payment.payment.rest.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.rpc.RpcException;
import com.yiguang.payment.business.product.entity.PointChannelRelation;
import com.yiguang.payment.business.product.service.PointChannelRelationService;
import com.yiguang.payment.common.exception.ErrorCodeConst;
import com.yiguang.payment.payment.entity.Channel;
import com.yiguang.payment.payment.entity.ChannelMerchantRelation;
import com.yiguang.payment.payment.order.entity.MerchantOrder;
import com.yiguang.payment.payment.rest.service.CheckRiskService;
import com.yiguang.payment.payment.risk.service.RiskService;
import com.yiguang.payment.payment.service.ChannelMerchantRelationService;
import com.yiguang.payment.payment.service.ChannelService;

@Service("checkRiskService")
@Transactional
public class CheckRiskServiceImpl implements CheckRiskService
{
	@Autowired
	private ChannelService channelService;
	@Autowired
	private ChannelMerchantRelationService ChannelMerchantRelationService;
	@Autowired
	private PointChannelRelationService pointChannelRelationService;
	@Autowired
	private RiskService riskService;
	private static Logger logger = LoggerFactory.getLogger(CheckRiskServiceImpl.class);
	@Override
	public MerchantOrder checkRiskAndChannel(MerchantOrder mo)
	{
		List<Channel> channelList = channelService.queryChannelbyCarrier(mo.getCarrierId());
		List<Channel> merchantChannelList = new ArrayList<Channel>();
		List<Channel> pointChannelList = new ArrayList<Channel>();
		
		for (Channel channel : channelList)
		{
			boolean isMerchantChannelOK = false;

			List <ChannelMerchantRelation> cmrList = ChannelMerchantRelationService.queryChannelMerchantRelationByMerchantId(mo.getMerchantId());
			for (ChannelMerchantRelation cmr : cmrList)
			{
				if (cmr.getChannelId() == channel.getId())
				{
					isMerchantChannelOK = true;
					break;
				}
			}

			if (isMerchantChannelOK)
			{
				merchantChannelList.add(channel);
			}
		}
		
		if (merchantChannelList.size() == 0)
		{
			throw new RpcException(90022);
		}
		
		for (Channel channel : merchantChannelList)
		{
			boolean isPointChannelOK = false;

			List <PointChannelRelation> pcrList = pointChannelRelationService.queryPointChannelRelationByPointId(mo.getChargingPointId());
			for (PointChannelRelation pcr : pcrList)
			{
				if (pcr.getChannelId() == channel.getId())
				{
					isPointChannelOK = true;
					break;
				}
			}
			
			if (isPointChannelOK)
			{
				pointChannelList.add(channel);
			}
		}
		
		if (pointChannelList.size() == 0)
		{
			throw new RpcException(90023);
		}
		
		riskService.checkBlackList(mo.getMobile(), mo.getRequestIp(), mo.getUsername());
		boolean isWhite = riskService.checkWhiteList(mo.getMobile(), mo.getRequestIp(), mo.getUsername());

		for (Channel channel : pointChannelList)
		{
			long riskId = 0;

			if (!isWhite)
			{
				riskService.checkMerchantRejection(mo.getMobile(), String.valueOf(mo.getMerchantId()));
				
				riskId = riskService.checkBasicRule(channel.getId(), mo.getProvinceId(), mo.getCityId(),
						mo.getMerchantId(), mo.getProductId(), mo.getChargingPointId(), mo.getMobile(),
						mo.getRequestIp(), mo.getUsername(), mo.getPayAmount().longValue());
			}

			if (riskId != 0)
			{
				logger.error("[checkRiskAndChannel(" + riskId + ")] failed");
				throw new RpcException(ErrorCodeConst.ErrorCode10502);
			}
			else
			{
				mo.setChannelId(channel.getId());
			}
		}

		return mo;
	}

	@Override
	public void checkRisk(MerchantOrder mo, long channelId)
	{
		Channel channel = channelService.queryChannel(channelId);
		long riskId = 0;
		riskService.checkBlackList(mo.getMobile(), mo.getRequestIp(), mo.getUsername());
		boolean isWhite = riskService.checkWhiteList(mo.getMobile(), mo.getRequestIp(), mo.getUsername());
		if (!isWhite)
		{
			riskId = riskService.checkBasicRule(channel.getId(), mo.getProvinceId(), mo.getCityId(),
					mo.getMerchantId(), mo.getProductId(), mo.getChargingPointId(), mo.getMobile(),
					mo.getRequestIp(), mo.getUsername(), mo.getPayAmount().longValue());
		}

		if (riskId != 0)
		{
			logger.error("[checkRiskAndChannel(" + riskId + ")] failed");
			throw new RpcException(ErrorCodeConst.ErrorCode10502);
		}
	}
	
}
