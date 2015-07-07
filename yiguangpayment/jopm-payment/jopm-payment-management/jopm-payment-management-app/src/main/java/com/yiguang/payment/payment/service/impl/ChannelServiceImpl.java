package com.yiguang.payment.payment.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.rpc.RpcException;
import com.yiguang.payment.common.CommonConstant;
import com.yiguang.payment.common.Constant;
import com.yiguang.payment.common.datasource.service.DataSourceService;
import com.yiguang.payment.common.exception.ErrorCodeConst;
import com.yiguang.payment.common.query.PageUtil;
import com.yiguang.payment.common.query.YcPage;
import com.yiguang.payment.common.utils.BeanUtils;
import com.yiguang.payment.common.utils.StringUtil;
import com.yiguang.payment.payment.entity.CarrierChannelRelation;
import com.yiguang.payment.payment.entity.Channel;
import com.yiguang.payment.payment.repository.ChannelDao;
import com.yiguang.payment.payment.service.CarrierChannelRelationService;
import com.yiguang.payment.payment.service.ChannelService;
import com.yiguang.payment.payment.vo.ChannelVO;

@Service("channelService")
@Transactional
public class ChannelServiceImpl implements ChannelService {

	private static Logger logger = LoggerFactory.getLogger(ChannelServiceImpl.class);
	@Autowired
	private ChannelDao channelDao;
	@Autowired
	private CarrierChannelRelationService carrierChannelRelationService;
	@Autowired
	private DataSourceService dataSourceService;
	@Autowired
	private CarrierChannelRelationService relationService;
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Override
	@CacheEvict(value={"channelCache","dataSourceCache"},allEntries=true,beforeInvocation=true)
	public Channel updateChannelStatus(Channel channel) {
		try {
			logger.debug("updateChannelStatus start, channel [" + channel.toString() + "]");
			Channel new_channel = channelDao.findOne(channel.getId());
			if (new_channel == null) {
				
				// 渠道不存在
				logger.error("updateChannelStatus failed, channel [" + channel.toString() + "]");
				throw new RpcException(ErrorCodeConst.ErrorCode10302);
			}
			
			new_channel.setStatus(channel.getStatus());
			new_channel = channelDao.save(new_channel);
			
			long channelId = new_channel.getId();
			List<CarrierChannelRelation> Crelations = relationService
					.queryCarrierChannelRelationByChannelId(channelId);

			for (CarrierChannelRelation relation : Crelations)
			{
				// 关联关系与渠道保持一致
				relation.setStatus(new_channel.getStatus());
				relationService.saveCarrierChannelRelation(relation);
			}
			//因渠道的计费码过多，所以不关联关闭
//			// 渠道状态的改变的连带反应
//			if (new_channel.getStatus() == CommonConstant.CommonStatus.CLOSE) {
//				
//				List<ChannelChargingCode> chargingCodes = chargingCodeService.queryChargingCodeByChannelId(channelId);
//				for (ChannelChargingCode chargingCode : chargingCodes) {
//					if (chargingCode.getStatus() != CommonConstant.CommonStatus.CLOSE) {
//						chargingCode.setStatus(CommonConstant.CommonStatus.CLOSE);
//						chargingCodeService.updateChargingCodeStatus(chargingCode);
//					}
//				}
//			}
			logger.debug("updateChannelStatus end, channel [" + channel.toString() + "]");
			return new_channel;
		} catch (RpcException e) {
			throw e;
		} catch (Exception e) {
			logger.error("updateChannelStatus failed, channel [" + channel.toString() + "]");
			logger.error(e.getLocalizedMessage(), e);
			throw new RpcException(ErrorCodeConst.ErrorCode99998);
		}
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Override
	@CacheEvict(value={"channelCache","dataSourceCache"},allEntries=true,beforeInvocation=true)
	public String deleteChannel(Channel channel) {
		try {
			logger.debug("deleteChannel start, channel [" + channel.toString() + "]");
			Channel new_channel = channelDao.findOne(channel.getId());
			if (new_channel != null) {
				channelDao.delete(new_channel);
				
			} else {
				// 准备删除的渠道不存在！
				logger.error("deleteChannel failed, channel [" + channel.toString() + "]");
				throw new RpcException(ErrorCodeConst.ErrorCode10302);
			}
			logger.debug("deleteChannel end, channel [" + channel.toString() + "]");
			return Constant.Common.SUCCESS;
		} catch (RpcException e) {
			throw e;
		} catch (Exception e) {
			logger.error("deleteChannel failed, channel [" + channel.toString() + "]");
			logger.error(e.getLocalizedMessage(), e);
			throw new RpcException(ErrorCodeConst.ErrorCode99998);
		}
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Override
	@CacheEvict(value={"channelCache","dataSourceCache"},allEntries=true,beforeInvocation=true)
	public Channel saveChannel(Channel channel) {
		try {
			logger.debug("saveChannel start, channel [" + channel.toString() + "]");
			Channel new_channel = channelDao.queryChannelByName(channel.getName());
			// 唯一性
			if (BeanUtils.isNotNull(new_channel) && channel.getId() == 0) {

				logger.error("saveChannel failed, channel [" + channel.toString() + "]");
				throw new RpcException(ErrorCodeConst.ErrorCode10305);
			} else {
				channel = channelDao.save(channel);
			}

			logger.debug("saveChannel end, channel [" + channel.toString() + "]");
			return channel;
		} catch (RpcException e) {
			throw e;
		} catch (Exception e) {
			logger.error("saveChannel failed, channel [" + channel.toString() + "]");
			logger.error(e.getLocalizedMessage(), e);
			throw new RpcException(ErrorCodeConst.ErrorCode99998);
		}
	}
	/**
	 * 通过运营商查渠道，并按照sort进行升序排序
	 * @author ediosn
	 */
	@Override
	@Cacheable(value="channelCache",key="#root.methodName+#carrierId")
	public List<Channel> queryChannelbyCarrier(long carrierId) {
		logger.debug("queryChannelbyCarrier start, carrier [" + carrierId + "]");
		try {
			
			List<CarrierChannelRelation> carrierChannels = carrierChannelRelationService.queryCarrierChannelRelationByCarrierId(carrierId);
			List<Channel> channels = new ArrayList<Channel>();
			for(CarrierChannelRelation relation : carrierChannels){
				long channelId = relation.getChannelId();
				Channel channel = channelDao.findOne(channelId);
				channels.add(channel);
			}
			logger.debug("queryChannelbyCarrier end, carrier [" + carrierId + "]");
			return channels;
		} catch (Exception e) {
			logger.error("queryChannelbyCarrier failed, carrier [" + carrierId + "]");
			logger.error(e.getLocalizedMessage(), e);
			throw new RpcException(ErrorCodeConst.ErrorCode99998);
		}
	}
	
	@Override
	@Cacheable(value="channelCache",key="#root.methodName+#id")
	public Channel queryChannel(long id) {
		logger.debug("queryChannel start, id [" + id + "]");
		try {

			Channel channel = channelDao.findOne(id);
			logger.debug("queryChannel end, id [" + id + "]");
			return channel;
		} catch (Exception e) {
			logger.error("queryChannel failed, id [" + id + "]");
			logger.error(e.getLocalizedMessage(), e);
			throw new RpcException(ErrorCodeConst.ErrorCode99998);
		}
	}

	private Specification<Channel> getPageQuerySpec(final ChannelVO vo)
	{
		Specification<Channel> spec = new Specification<Channel>(){
			@Override
			public Predicate toPredicate(Root<Channel> root,  
		            CriteriaQuery<?> query, CriteriaBuilder cb) {  
		        
				List<Predicate> predicateList = new ArrayList<Predicate>();
			
				if (vo.getStatus() != -1)
				{
					predicateList.add(cb.equal(root.get("status").as(Integer.class), vo.getStatus()));  
				}
				
				if (StringUtil.isNotEmpty(vo.getName()))
				{
					predicateList.add(cb.equal(root.get("name").as(String.class), vo.getName().trim()));  
				}
				
				Predicate[] p = new Predicate[predicateList.size()];  
		        query.where(cb.and(predicateList.toArray(p)));  
		        //添加排序的功能  
		        query.orderBy(cb.asc(root.get("id").as(Integer.class)));  
		          
		        return query.getRestriction();  
			}
		};
		
		return spec;
	}
	
	
	@Override
	@Cacheable(value="channelCache")
	public YcPage<ChannelVO> queryChannelList(ChannelVO conditionVO, int pageNumber, int pageSize,
			String sortType) {
		logger.debug("queryChannelList start");
		try {
			Specification<Channel> spec = getPageQuerySpec(conditionVO);
			YcPage<Channel> ycPage = PageUtil.queryYcPage(channelDao, spec, pageNumber, pageSize, new Sort(
					Direction.DESC, "id"), Channel.class);

			YcPage<ChannelVO> result = new YcPage<ChannelVO>();
			result.setPageTotal(ycPage.getPageTotal());
			result.setCountTotal(ycPage.getCountTotal());
			List<Channel> list = ycPage.getList();
			List<ChannelVO> voList = new ArrayList<ChannelVO>();
			ChannelVO vo = null;
			for (Channel temp : list) {
				vo = copyPropertiesToVO(temp);
				voList.add(vo);
			}
			logger.debug("queryChannelList end");
			result.setList(voList);

			return result;
		} catch (RpcException e) {
			throw e;
		} catch (Exception e) {
			logger.error("queryChannelList failed");
			logger.error(e.getLocalizedMessage(), e);
			throw new RpcException(ErrorCodeConst.ErrorCode99998);
		}
	}

	@Override
	public ChannelVO copyPropertiesToVO(Channel temp) {
		logger.debug("copyPropertiesToVO start");
		try {
			ChannelVO vo = new ChannelVO();

			vo.setId(temp.getId());
			vo.setName(temp.getName());
			vo.setRemark(temp.getRemark());
			vo.setStatus(temp.getStatus());
			vo.setStatusLabel(dataSourceService.findOptionVOById(CommonConstant.DataSourceName.COMMON_STATUS,
					String.valueOf(temp.getStatus())).getText());
			logger.debug("copyPropertiesToVO end");
			return vo;
		} catch (RpcException e) {
			throw e;
		} catch (Exception e) {
			logger.error("copyPropertiesToVO failed");
			logger.error(e.getLocalizedMessage(), e);
			throw new RpcException(ErrorCodeConst.ErrorCode99998);
		}

	}
	
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Override
	@CacheEvict(value = {"channelCache","dataSourceCache"}, allEntries = true, beforeInvocation = true)
	public Channel saveChannel(Channel channel, String carrierChannelRelationIDs)
	{
		logger.debug("saveChannel start, Channel [" + channel.toString() + "] carrierChannelRelationIDs ["
				+ carrierChannelRelationIDs + "]");
		try
		{
			channel = saveChannel(channel);
			if (StringUtils.isNotEmpty(carrierChannelRelationIDs))
			{
				List<CarrierChannelRelation> list = new ArrayList<CarrierChannelRelation>();
				CarrierChannelRelation relation = null;
				String[] carrierIdAndSorts = carrierChannelRelationIDs.split("[,]");
				for(String carrierIdAndSort : carrierIdAndSorts) {
					String[]  carrAndSor= carrierIdAndSort.split("\\|");
					if(carrAndSor.length == 2){
						String carrierId = carrAndSor[0];
						String sort = carrAndSor[1];
						relation = new CarrierChannelRelation();
						relation.setCarrierId(Long.parseLong(carrierId));
						relation.setChannelId(channel.getId());
						relation.setStatus(channel.getStatus());
						relation.setSort(Integer.valueOf(sort));
						list.add(relation);
					}
				}
				List<CarrierChannelRelation> oldList = relationService
						.queryCarrierChannelRelationByChannelId(channel.getId());
				relationService.update(oldList, list);
				logger.debug("saveChannel end, Channel [" + channel.toString() + "] carrierChannelRelationIDs ["
						+ carrierChannelRelationIDs + "]");
			}
			return channel;
		}
		catch (RpcException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			logger.error("saveChannel failed, Channel [" + channel.toString() + "] carrierChannelRelationIDs ["
					+ carrierChannelRelationIDs + "]");
			logger.error(e.getLocalizedMessage(), e);
			throw new RpcException(ErrorCodeConst.ErrorCode99998);
		}

	}
	
}
