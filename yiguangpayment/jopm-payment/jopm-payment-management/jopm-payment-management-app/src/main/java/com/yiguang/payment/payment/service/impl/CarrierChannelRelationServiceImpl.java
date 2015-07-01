package com.yiguang.payment.payment.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.persistence.DynamicSpecifications;
import org.springside.modules.persistence.SearchFilter;
import org.springside.modules.persistence.SearchFilter.Operator;

import com.alibaba.dubbo.rpc.RpcException;
import com.yiguang.payment.common.CommonConstant;
import com.yiguang.payment.common.Constant;
import com.yiguang.payment.common.datasource.service.DataSourceService;
import com.yiguang.payment.common.exception.ErrorCodeConst;
import com.yiguang.payment.common.utils.BeanUtils;
import com.yiguang.payment.payment.entity.CarrierChannelRelation;
import com.yiguang.payment.payment.repository.CarrierChannelRelationDao;
import com.yiguang.payment.payment.service.CarrierChannelRelationService;
import com.yiguang.payment.payment.vo.CarrierChannelRelationVO;

@Service("carrierChannelRelationService")
@Transactional
public class CarrierChannelRelationServiceImpl implements CarrierChannelRelationService{

	private static Logger logger = LoggerFactory.getLogger(CarrierChannelRelationServiceImpl.class);

	@Autowired
	private DataSourceService dataSourceService;

	@Autowired
	private CarrierChannelRelationDao relationDao;
	
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Override
	public CarrierChannelRelationVO copyPropertiesToVO(CarrierChannelRelation temp) {
		logger.debug("copyPropertiesToVO start");
		try
		{
			CarrierChannelRelationVO vo = new CarrierChannelRelationVO();
			vo.setId(temp.getId());
			vo.setChannelId(temp.getChannelId());
			vo.setSort(temp.getSort());
			vo.setChannelLabel(dataSourceService.findOptionVOById(CommonConstant.DataSourceName.CHANNEL,
					String.valueOf(temp.getChannelId())).getText());
			vo.setCarrierId(temp.getCarrierId());
			vo.setCarrierLabel(dataSourceService.findOptionVOById(CommonConstant.DataSourceName.CARRIER,
					String.valueOf(temp.getCarrierId())).getText());
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
	public List<CarrierChannelRelation> queryCarrierChannelRelationByChannelId(
			long channelId) {
		logger.debug("queryCarrierChannelRelationByChannelId start, channelId [" + channelId + "]");
		try
		{
			List<CarrierChannelRelation> list = relationDao.queryCarrierChannelRelationByChannelId(channelId);
			logger.debug("queryCarrierChannelRelationByChannelId end, channelId [" + channelId + "]");
			return list;
		}
		catch (Exception e)
		{
			logger.error("queryCarrierChannelRelationByChannelId failed, channelId [" + channelId + "]");
			logger.error(e.getLocalizedMessage(), e);
			throw new RpcException(ErrorCodeConst.ErrorCode99998);
		}
	}

	@Override
	public List<CarrierChannelRelationVO> queryCarrierChannelRelationVOByChannelId(
			long channelId) {
		logger.debug("queryCarrierChannelRelationVOByChannelId start, channelId [" + channelId + "]");
		try
		{
			List<CarrierChannelRelationVO> result = new ArrayList<CarrierChannelRelationVO>();
			List<CarrierChannelRelation> list = queryCarrierChannelRelationByChannelId(channelId);
			CarrierChannelRelationVO vo = null;
			for (CarrierChannelRelation temp : list)
			{
				vo = copyPropertiesToVO(temp);
				result.add(vo);
			}
			logger.debug("queryCarrierChannelRelationVOByChannelId end, channelId [" + channelId + "]");
			return result;
		}
		catch (RpcException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			logger.error("queryCarrierChannelRelationVOByChannelId failed, channelId [" + channelId + "]");
			logger.error(e.getLocalizedMessage(), e);
			throw new RpcException(ErrorCodeConst.ErrorCode99998);
		}
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Override
	public CarrierChannelRelation saveCarrierChannelRelation(
			CarrierChannelRelation relation) {
		try
		{
			logger.debug("saveCarrierChannelRelation start, relation [" + relation.toString() + "]");
			CarrierChannelRelation carrierChannelRelation = relationDao.queryCarrierChannelRelationByAll(relation.getCarrierId(), relation.getChannelId());
			// 唯一性
			if (BeanUtils.isNotNull(carrierChannelRelation) && relation.getId() == 0)
			{

				logger.error("saveCarrierChannelRelation failed, relation [" + relation.toString() + "]");
				throw new RpcException(ErrorCodeConst.ErrorCode10311);
			}
			else
			{
				relation = relationDao.save(relation);
			}

			logger.debug("saveCarrierChannelRelation end, relation [" + relation.toString() + "]");
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
	public String deleteCarrierChannelRelation(CarrierChannelRelation relation) {
		// TODO 自动生成的方法存根
		return null;
	}
	
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Override
	public String update(List<CarrierChannelRelation> oldList,
			List<CarrierChannelRelation> newList) {
		try
		{
			logger.debug("update start, oldList,newList: [" + oldList.toString() + newList.toString()+ "]");
			relationDao.delete(oldList);
			relationDao.save(newList);
			logger.debug("update end, oldList,newList: [" + oldList.toString() + newList.toString()+ "]");
			return Constant.Common.SUCCESS;
		}
		catch (Exception e)
		{
			logger.error("update failed, oldList,newList: [" + oldList.toString() + newList.toString()+ "]");
			logger.error(e.getLocalizedMessage(), e);
			throw new RpcException(ErrorCodeConst.ErrorCode99998);
		}
	}
	
	@Override
	public List<CarrierChannelRelation> queryCarrierChannelRelationByCarrierId(
			long carrierId) {
		logger.debug("queryCarrierChannelRelationByCarrierId start, carrierId [" + carrierId + "]");
		try
		{
			Map<String, SearchFilter> filters = new HashMap<String, SearchFilter>();
			filters.put("status", new SearchFilter("status", Operator.EQ, CommonConstant.CommonStatus.OPEN));
			filters.put("carrierId", new SearchFilter("carrierId", Operator.EQ, carrierId));
			Specification<CarrierChannelRelation> spec = DynamicSpecifications.bySearchFilter(filters.values(), CarrierChannelRelation.class);
			List<CarrierChannelRelation> list = relationDao.findAll(spec, new Sort(Direction.DESC, "sort"));
			logger.debug("queryCarrierChannelRelationByCarrierId end, carrierId [" + carrierId + "]");
			return list;
		}
		catch (Exception e)
		{
			logger.error("queryCarrierChannelRelationByCarrierId failed, carrierId [" + carrierId + "]");
			logger.error(e.getLocalizedMessage(), e);
			throw new RpcException(ErrorCodeConst.ErrorCode99998);
		}
	}

}
