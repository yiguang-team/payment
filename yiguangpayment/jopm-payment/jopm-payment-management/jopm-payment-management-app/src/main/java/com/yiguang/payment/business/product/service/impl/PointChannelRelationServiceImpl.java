package com.yiguang.payment.business.product.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.rpc.RpcException;
import com.yiguang.payment.business.product.entity.PointChannelRelation;
import com.yiguang.payment.business.product.repository.PointChannelRelationDao;
import com.yiguang.payment.business.product.service.PointChannelRelationService;
import com.yiguang.payment.business.product.vo.PointChannelRelationVO;
import com.yiguang.payment.common.CommonConstant;
import com.yiguang.payment.common.Constant;
import com.yiguang.payment.common.datasource.service.DataSourceService;
import com.yiguang.payment.common.exception.ErrorCodeConst;
import com.yiguang.payment.common.utils.BeanUtils;

@Service("pointChannelRelationService")
@Transactional
public class PointChannelRelationServiceImpl implements PointChannelRelationService
{

	private static Logger logger = LoggerFactory.getLogger(PointChannelRelationServiceImpl.class);

	@Autowired
	private DataSourceService dataSourceService;

	@Autowired
	private PointChannelRelationDao pointChannelRelationDao;

	@Override
	public List<PointChannelRelation> queryPointChannelRelationByPointId(long pointId)
	{
		logger.debug("queryPointChannelRelationByPointId start, pointId [" + pointId + "]");
		try
		{
			List<PointChannelRelation> list = pointChannelRelationDao.queryPointChannelRelationByPointId(pointId);
			logger.debug("queryPointChannelRelationByPointId end, pointId [" + pointId + "]");
			return list;
		}
		catch (Exception e)
		{
			logger.error("queryPointChannelRelationByPointId failed");
			logger.error(e.getLocalizedMessage(), e);
			throw new RpcException(ErrorCodeConst.ErrorCode99998);
		}
	}

	@Override
	public List<PointChannelRelation> queryPointChannelRelationByChannelId(long channelId)
	{
		logger.debug("queryPointChannelRelationByChannelId start, channelId [" + channelId + "]");
		try
		{
			List<PointChannelRelation> list = pointChannelRelationDao.queryPointChannelRelationByChannelId(channelId);
			logger.debug("queryPointChannelRelationByChannelId end, channelId [" + channelId + "]");
			return list;
		}
		catch (Exception e)
		{
			logger.error("queryPointChannelRelationByChannelId failed, channelId [" + channelId + "]");
			logger.error(e.getLocalizedMessage(), e);
			throw new RpcException(ErrorCodeConst.ErrorCode99998);
		}
	}

	@Override
	public PointChannelRelation queryPointChannelRelationByPointIdAndChannelId(long pointId, long channelId)
	{
		logger.debug("queryPointChannelRelationByPointIdAndChannelId start, pointId [" + pointId + "] channelId ["
				+ channelId + "]");
		try
		{
			PointChannelRelation list = pointChannelRelationDao.queryPointChannelRelationByPointIdAndChannelId(
					pointId, channelId);
			logger.debug("queryPointChannelRelationByPointIdAndChannelId end, pointId [" + pointId + "] channelId ["
					+ channelId + "]");
			return list;
		}
		catch (Exception e)
		{
			logger.error("queryPointChannelRelationByPointIdAndChannelId failed, pointId [" + pointId + "] channelId ["
					+ channelId + "]");
			logger.error(e.getLocalizedMessage(), e);
			throw new RpcException(ErrorCodeConst.ErrorCode99998);
		}
	}

	@Override
	public List<PointChannelRelationVO> queryPointChannelRelationVOByPointId(long pointId)
	{
		try
		{
			logger.debug("queryPointChannelRelationVOByPointId start, pointId [" + pointId + "]");
			List<PointChannelRelationVO> result = new ArrayList<PointChannelRelationVO>();
			List<PointChannelRelation> list = queryPointChannelRelationByPointId(pointId);
			PointChannelRelationVO vo = null;
			for (PointChannelRelation temp : list)
			{
				vo = copyPropertiesToVO(temp);
				result.add(vo);
			}
			logger.debug("queryPointChannelRelationVOByPointId end, pointId [" + pointId + "]");
			return result;
		}
		catch (RpcException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			logger.error("queryPointChannelRelationVOByPointId failed, pointId [" + pointId + "]");
			logger.error(e.getLocalizedMessage(), e);
			throw new RpcException(ErrorCodeConst.ErrorCode99998);
		}
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Override
	public PointChannelRelation savePointChannelRelation(
			PointChannelRelation pointChannelRelation) {
		logger.debug("savePointChannelRelation start, pointChannelRelation [" + pointChannelRelation.toString() + "]");
		try
		{

			PointChannelRelation queryPoint = pointChannelRelationDao.queryPointChannelRelationByPointIdAndChannelId(
					pointChannelRelation.getPointId(), pointChannelRelation.getChannelId());
			// 唯一性
			if (BeanUtils.isNotNull(queryPoint) && pointChannelRelation.getId() == 0)
			{

				logger.error("savePointChannelRelation failed, pointChannelRelation [" + pointChannelRelation.toString() + "]");
				throw new RpcException(ErrorCodeConst.ErrorCode10405);
			}
			else
			{
				pointChannelRelation = pointChannelRelationDao.save(pointChannelRelation);
			}
			return pointChannelRelation;
		}
		catch (RpcException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			logger.error("savePointChannelRelation failed, pointChannelRelation [" + pointChannelRelation.toString() + "]");
			logger.error(e.getLocalizedMessage(), e);
			throw new RpcException(ErrorCodeConst.ErrorCode99998);
		}
	}
	
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Override
	public String delete(List<PointChannelRelation> list) {
		try
		{
			logger.debug("delete start, list [" + list.toString() + "]");
			pointChannelRelationDao.delete(list);
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
	public String save(List<PointChannelRelation> list) {
		try
		{
			logger.debug("delete start, list [" + list.toString() + "]");
			pointChannelRelationDao.save(list);
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
	public PointChannelRelationVO copyPropertiesToVO(PointChannelRelation temp)
	{
		try
		{
			logger.debug("copyPropertiesToVO start");
			PointChannelRelationVO vo = new PointChannelRelationVO();
			vo.setId(temp.getId());
			vo.setChannelId(temp.getChannelId());
			vo.setChannelLabel(dataSourceService.findOptionVOById(CommonConstant.DataSourceName.CHANNEL,
					String.valueOf(temp.getChannelId())).getText());
			vo.setPointId(temp.getPointId());
			vo.setPointLabel(dataSourceService.findOptionVOById(CommonConstant.DataSourceName.POINT,
					String.valueOf(temp.getPointId())).getText());
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
}
