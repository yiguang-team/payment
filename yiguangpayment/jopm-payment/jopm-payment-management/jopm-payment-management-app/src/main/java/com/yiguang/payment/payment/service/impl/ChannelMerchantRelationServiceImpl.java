package com.yiguang.payment.payment.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.rpc.RpcException;
import com.yiguang.payment.common.CommonConstant;
import com.yiguang.payment.common.Constant;
import com.yiguang.payment.common.datasource.service.DataSourceService;
import com.yiguang.payment.common.exception.ErrorCodeConst;
import com.yiguang.payment.common.utils.BeanUtils;
import com.yiguang.payment.payment.entity.ChannelMerchantRelation;
import com.yiguang.payment.payment.repository.ChannelMerchantRelationDao;
import com.yiguang.payment.payment.service.ChannelMerchantRelationService;
import com.yiguang.payment.payment.vo.ChannelMerchantRelationVO;

@Service("channelMerchantRelationService")
@Transactional
public class ChannelMerchantRelationServiceImpl implements ChannelMerchantRelationService
{

	private static Logger logger = LoggerFactory.getLogger(ChannelMerchantRelationServiceImpl.class);

	@Autowired
	private DataSourceService dataSourceService;

	@Autowired
	private ChannelMerchantRelationDao relationDao;

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Override
	public ChannelMerchantRelationVO copyPropertiesToVO(ChannelMerchantRelation temp)
	{
		logger.debug("copyPropertiesToVO start");
		try
		{
			ChannelMerchantRelationVO vo = new ChannelMerchantRelationVO();
			vo.setId(temp.getId());
			vo.setChannelId(temp.getChannelId());
			vo.setChannelLabel(dataSourceService.findOptionVOById(CommonConstant.DataSourceName.CHANNEL,
					String.valueOf(temp.getChannelId())).getText());
			vo.setMerchantId(temp.getMerchantId());
			vo.setMerchantLabel(dataSourceService.findOptionVOById(CommonConstant.DataSourceName.MERCHANT,
					String.valueOf(temp.getMerchantId())).getText());
			vo.setStatus(temp.getStatus());
			vo.setRemark(vo.getRemark());
			logger.debug("copyPropertiesToVO end");
			return vo;
		}
		catch (RpcException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			logger.error("copyPropertiesToVO failed");
			logger.error(e.getLocalizedMessage(), e);
			throw new RpcException(ErrorCodeConst.ErrorCode99998);
		}
	}

	@Override
	public List<ChannelMerchantRelation> queryChannelMerchantRelationByMerchantId(long merchantId)
	{
		logger.debug("queryChannelMerchantRelationByMerchantId start, merchantId [" + merchantId + "]");
		try
		{
			List<ChannelMerchantRelation> list = relationDao.queryChannelMerchantRelationByMerchantId(merchantId);
			logger.debug("queryChannelMerchantRelationByMerchantId end, merchantId [" + merchantId + "]");
			return list;
		}
		catch (Exception e)
		{
			logger.error("queryChannelMerchantRelationByMerchantId failed, merchantId [" + merchantId + "]");
			logger.error(e.getLocalizedMessage(), e);
			throw new RpcException(ErrorCodeConst.ErrorCode99998);
		}
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Override
	public ChannelMerchantRelation saveChannelMerchantRelation(
			ChannelMerchantRelation relation) {
		try
		{
			logger.debug("saveChannelMerchantRelation start, relation [" + relation.toString() + "]");
			ChannelMerchantRelation channelMerchantRelation = relationDao.queryChannelMerchantRelationByCidAndMerId(relation.getChannelId(), relation.getMerchantId());
			// 唯一性
			if (BeanUtils.isNotNull(channelMerchantRelation) && relation.getId() == 0)
			{

				logger.error("saveChannelMerchantRelation failed, relation [" + relation.toString() + "]");
				throw new RpcException(ErrorCodeConst.ErrorCode10311);
			}
			else
			{
				relation = relationDao.save(relation);
			}

			logger.debug("saveChannelMerchantRelation end, relation [" + relation.toString() + "]");
			return relation;
		}
		catch (RpcException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			logger.error("saveChannelMerchantRelation failed, merchant [" + relation.toString() + "]");
			logger.error(e.getLocalizedMessage(), e);
			throw new RpcException(ErrorCodeConst.ErrorCode99998);
		}
	}
	
	
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Override
	public String delete(List<ChannelMerchantRelation> list) {
		try
		{
			logger.debug("delete start, list [" + list.toString() + "]");
			relationDao.delete(list);
			logger.debug("delete end, list [" + list.toString() + "]");
			return Constant.Common.SUCCESS;
		}
		catch (Exception e)
		{
			logger.error("deletePoint failed, point [" + list.toString() + "]");
			logger.error(e.getLocalizedMessage(), e);
			throw new RpcException(ErrorCodeConst.ErrorCode99998);
		}
	}
	
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Override
	public String save(List<ChannelMerchantRelation> list) {
		try
		{
			logger.debug("save start, list [" + list.toString() + "]");
			relationDao.save(list);
			logger.debug("save end, list [" + list.toString() + "]");
			return Constant.Common.SUCCESS;
		}
		catch (Exception e)
		{
			logger.error("save failed, list [" + list.toString() + "]");
			logger.error(e.getLocalizedMessage(), e);
			throw new RpcException(ErrorCodeConst.ErrorCode99998);
		}
	}
	
	@Override
	public List<ChannelMerchantRelation> queryChannelMerchantRelationByChannelId(
			long channelId) {
		logger.debug("queryChannelMerchantRelationByChannelId start, channelId [" + channelId + "]");
		try
		{
			List<ChannelMerchantRelation> list = relationDao.queryChannelMerchantRelationByChannelId(channelId);
			logger.debug("queryChannelMerchantRelationByChannelId end, channelId [" + channelId + "]");
			return list;
		}
		catch (Exception e)
		{
			logger.error("queryChannelMerchantRelationByChannelId failed, channelId [" + channelId + "]");
			logger.error(e.getLocalizedMessage(), e);
			throw new RpcException(ErrorCodeConst.ErrorCode99998);
		}
	}
	
	@Override
	public List<ChannelMerchantRelationVO> queryChannelMerchantRelationVOByMerchantId(long merchantId)
	{
		logger.debug("queryChannelMerchantRelationVOByMerchantId start, merchantId [" + merchantId + "]");
		try
		{
			List<ChannelMerchantRelationVO> result = new ArrayList<ChannelMerchantRelationVO>();
			List<ChannelMerchantRelation> list = queryChannelMerchantRelationByMerchantId(merchantId);
			ChannelMerchantRelationVO vo = null;
			for (ChannelMerchantRelation temp : list)
			{
				vo = copyPropertiesToVO(temp);
				result.add(vo);
			}
			logger.debug("queryChannelMerchantRelationVOByMerchantId end, merchantId [" + merchantId + "]");
			return result;
		}
		catch (RpcException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			logger.error("queryChannelMerchantRelationVOByMerchantId failed, merchantId [" + merchantId + "]");
			logger.error(e.getLocalizedMessage(), e);
			throw new RpcException(ErrorCodeConst.ErrorCode99998);
		}
	}

}
