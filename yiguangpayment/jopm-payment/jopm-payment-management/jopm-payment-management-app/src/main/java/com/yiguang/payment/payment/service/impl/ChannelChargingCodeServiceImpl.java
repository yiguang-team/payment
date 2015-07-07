package com.yiguang.payment.payment.service.impl;

import java.math.BigDecimal;
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

import com.alibaba.dubbo.rpc.RpcException;
import com.yiguang.payment.common.CommonConstant;
import com.yiguang.payment.common.Constant;
import com.yiguang.payment.common.datasource.service.DataSourceService;
import com.yiguang.payment.common.exception.ErrorCodeConst;
import com.yiguang.payment.common.query.PageUtil;
import com.yiguang.payment.common.query.YcPage;
import com.yiguang.payment.common.utils.BeanUtils;
import com.yiguang.payment.common.utils.BigDecimalUtil;
import com.yiguang.payment.common.utils.StringUtil;
import com.yiguang.payment.payment.entity.Channel;
import com.yiguang.payment.payment.entity.ChannelChargingCode;
import com.yiguang.payment.payment.repository.ChannelChargingCodeDao;
import com.yiguang.payment.payment.service.ChannelChargingCodeService;
import com.yiguang.payment.payment.service.ChannelService;
import com.yiguang.payment.payment.vo.ChannelChargingCodeVO;

@Service("channelChargingCodeService")
@Transactional
public class ChannelChargingCodeServiceImpl implements ChannelChargingCodeService
{

	private static Logger logger = LoggerFactory.getLogger(ChannelChargingCodeServiceImpl.class);

	@Autowired
	private ChannelChargingCodeDao chargingCodeDao;

	@Autowired
	private ChannelService channelService;

	@Autowired
	private DataSourceService dataSourceService;

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Override
	@CacheEvict(value={"channelChargingCodeCache","dataSourceCache"},allEntries=true,beforeInvocation=true)
	public ChannelChargingCode updateChargingCodeStatus(ChannelChargingCode chargingCode)
	{
		try
		{
			logger.debug("updateChargingCodeStatus start, chargingCode[" + chargingCode.toString() + "]");
			ChannelChargingCode channelChargingCode = chargingCodeDao.findOne(chargingCode.getId());

			if (channelChargingCode == null)
			{
				logger.error("updateChargingCodeStatus failed, chargingCode[" + chargingCode.toString() + "]");
				throw new RpcException(ErrorCodeConst.ErrorCode10301);
			}

			long channelId = channelChargingCode.getChannelId();
			Channel channel = channelService.queryChannel(channelId);

			if (channel == null)
			{
				logger.error("updateChargingCodeStatus failed, chargingCode[" + chargingCode.toString() + "]");
				throw new RpcException(ErrorCodeConst.ErrorCode10302);
			}

			if (chargingCode.getStatus() == CommonConstant.CommonStatus.CLOSE)
			{
				channelChargingCode.setStatus(chargingCode.getStatus());
				channelChargingCode = chargingCodeDao.save(channelChargingCode);
			}
			else
			{
				int cStatus = channel.getStatus();
				if (cStatus == CommonConstant.CommonStatus.OPEN)
				{
					channelChargingCode.setStatus(chargingCode.getStatus());
					channelChargingCode = chargingCodeDao.save(channelChargingCode);
				}
				else
				{
					logger.error("updateChargingCodeStatus failed, chargingCode[" + chargingCode.toString() + "]");
					throw new RpcException(ErrorCodeConst.ErrorCode10303);
				}
			}
			

			logger.debug("updateChargingCodeStatus end, chargingCode[" + chargingCode.toString() + "]");
			return channelChargingCode;
		}
		catch (RpcException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			logger.error("updateChargingCodeStatus failed, chargingCode[" + chargingCode.toString() + "]");
			logger.error(e.getLocalizedMessage(), e);
			throw new RpcException(ErrorCodeConst.ErrorCode99998);
		}
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Override
	@CacheEvict(value={"channelChargingCodeCache","dataSourceCache"},allEntries=true,beforeInvocation=true)
	public String deleteChargingCode(ChannelChargingCode chargingCode)
	{
		try
		{
			logger.debug("deleteChargingCode start, chargingCode[" + chargingCode.toString() + "]");
			ChannelChargingCode channelChargingCode = chargingCodeDao.findOne(chargingCode.getId());
			if (channelChargingCode != null)
			{
				chargingCodeDao.delete(channelChargingCode);
			}
			else
			{
				// 准备删除的编码不存在！
				logger.error("updateChargingCodeStatus failed, chargingCode[" + chargingCode.toString() + "]");
				throw new RpcException(ErrorCodeConst.ErrorCode10301);
			}
			logger.debug("deleteChargingCode end, chargingCode[" + chargingCode.toString() + "]");
			return Constant.Common.SUCCESS;
		}
		catch (RpcException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			logger.error("deleteChargingCode failed, chargingCode[" + chargingCode.toString() + "]");
			logger.error(e.getLocalizedMessage(), e);
			throw new RpcException(ErrorCodeConst.ErrorCode99998);
		}
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Override
	@CacheEvict(value={"channelChargingCodeCache","dataSourceCache"},allEntries=true,beforeInvocation=true)
	public ChannelChargingCode saveChargingCode(ChannelChargingCode chargingCode)
	{
		try
		{
			logger.debug("saveChargingCode start, chargingCode[" + chargingCode.toString() + "]");
			ChannelChargingCode new_channel = chargingCodeDao.queryChargingCodeByChannelIdAndAmount(
					chargingCode.getChannelId(), chargingCode.getChargingAmount());
			// 唯一性
			if (BeanUtils.isNotNull(new_channel) && chargingCode.getId() == 0)
			{
				logger.debug("saveChargingCode failed, chargingCode[" + chargingCode.toString() + "]");
				throw new RpcException(ErrorCodeConst.ErrorCode10304);
			}
			else
			{
				chargingCode = chargingCodeDao.save(chargingCode);
			}

			logger.debug("saveChargingCode end, chargingCode[" + chargingCode.toString() + "]");
			return chargingCode;
		}
		catch (RpcException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			logger.error("saveChargingCode failed, chargingCode[" + chargingCode.toString() + "]");
			logger.error(e.getLocalizedMessage(), e);
			throw new RpcException(ErrorCodeConst.ErrorCode99998);
		}
	}

	@Override
	@Cacheable(value="channelChargingCodeCache",key="#root.methodName+#id")
	public ChannelChargingCode queryChargingCode(long id)
	{
		logger.debug("queryChargingCode start, id [" + id + "]");
		try
		{
			ChannelChargingCode code = chargingCodeDao.findOne(id);
			logger.debug("queryChargingCode end, id [" + id + "]");
			return code;
		}
		catch (Exception e)
		{
			logger.error("queryChargingCode failed, id [" + id + "]");
			logger.error(e.getLocalizedMessage(), e);
			throw new RpcException(ErrorCodeConst.ErrorCode99998);
		}

	}

	@Override
	@Cacheable(value="channelChargingCodeCache",key="#root.methodName+#channelId+#chargingAmount")
	public ChannelChargingCode queryChargingCodeByChannelIdAndAmount(long channelId, BigDecimal chargingAmount)
	{
		logger.debug("queryChargingCodeByChannelIdAndAmount start, channelId [" + channelId + "] chargingAmount ["
				+ chargingAmount + "]");
		try
		{
			ChannelChargingCode code = chargingCodeDao.queryChargingCodeByChannelIdAndAmount(channelId, chargingAmount);
			logger.debug("queryChargingCodeByChannelIdAndAmount end, channelId [" + channelId + "] chargingAmount ["
					+ chargingAmount + "]");
			return code;
		}
		catch (Exception e)
		{
			logger.error("queryChargingCodeByChannelIdAndAmount failed, channelId [" + channelId + "] chargingAmount ["
					+ chargingAmount + "]");
			logger.error(e.getLocalizedMessage(), e);
			throw new RpcException(ErrorCodeConst.ErrorCode99998);
		}
	}

	@Override
	@Cacheable(value="channelChargingCodeCache",key="#root.methodName+#chargingCode")
	public ChannelChargingCode queryChargingCodeByCode(String chargingCode)
	{
		logger.debug("queryChargingCodeByCode start, chargingCode [" + chargingCode + "]");
		try
		{
			ChannelChargingCode code = chargingCodeDao.queryChargingCodeByCode(chargingCode);
			logger.debug("queryChargingCodeByCode end, chargingCode [" + chargingCode + "]");
			return code;
		}
		catch (Exception e)
		{
			logger.error("queryChargingCodeByCode failed, chargingCode [" + chargingCode + "]");
			logger.error(e.getLocalizedMessage(), e);
			throw new RpcException(ErrorCodeConst.ErrorCode99998);
		}
	}


	@Override
	@Cacheable(value="channelChargingCodeCache",key="#root.methodName+#channelId")
	public List<ChannelChargingCode> queryChargingCodeByChannelId(long channelId) {
		logger.debug("queryChargingCodeByChannelId start, channelId [" + channelId + "]");
		try
		{
			List<ChannelChargingCode> code = chargingCodeDao.queryChargingCodeByChannelId(channelId);
			logger.debug("queryChargingCodeByChannelId end, channelId [" + channelId + "]");
			return code;
		}
		catch (Exception e)
		{
			logger.error("queryChargingCodeByChannelId failed, channelId [" + channelId + "]");
			logger.error(e.getLocalizedMessage(), e);
			throw new RpcException(ErrorCodeConst.ErrorCode99998);
		}
	}
	
	private Specification<ChannelChargingCode> getPageQuerySpec(final ChannelChargingCodeVO vo)
	{
		Specification<ChannelChargingCode> spec = new Specification<ChannelChargingCode>(){
			@Override
			public Predicate toPredicate(Root<ChannelChargingCode> root,  
		            CriteriaQuery<?> query, CriteriaBuilder cb) {  
		        
				List<Predicate> predicateList = new ArrayList<Predicate>();
				if (vo.getChannelId() != -1)
				{
					predicateList.add(cb.equal(root.get("channelId").as(Long.class), vo.getChannelId()));  
				}
				
				if (vo.getStatus() != -1)
				{
					predicateList.add(cb.equal(root.get("status").as(Integer.class), vo.getStatus()));  
				}
				
				if (StringUtil.isNotEmpty(vo.getChargingCode()))
				{
					predicateList.add(cb.equal(root.get("chargingCode").as(String.class), vo.getChargingCode().trim()));  
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
	@Cacheable(value="channelChargingCodeCache")
	public YcPage<ChannelChargingCodeVO> queryChargingCodeList(ChannelChargingCodeVO conditionVO, int pageNumber,
			int pageSize, String sortType)
	{
		logger.debug("queryChargingCodeList start");
		try
		{
			Specification<ChannelChargingCode> spec = getPageQuerySpec(conditionVO);
			YcPage<ChannelChargingCode> ycPage = PageUtil.queryYcPage(chargingCodeDao, spec, pageNumber, pageSize,
					new Sort(Direction.DESC, "id"), ChannelChargingCode.class);

			YcPage<ChannelChargingCodeVO> result = new YcPage<ChannelChargingCodeVO>();
			result.setPageTotal(ycPage.getPageTotal());
			result.setCountTotal(ycPage.getCountTotal());
			List<ChannelChargingCode> list = ycPage.getList();
			List<ChannelChargingCodeVO> voList = new ArrayList<ChannelChargingCodeVO>();
			ChannelChargingCodeVO vo = null;
			for (ChannelChargingCode temp : list)
			{
				vo = copyPropertiesToVO(temp);
				voList.add(vo);
			}
			result.setList(voList);
			logger.debug("queryChargingCodeList end");
			return result;
		}
		catch (RpcException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			logger.error("queryChargingCodeList failed");
			logger.error(e.getLocalizedMessage(), e);
			throw new RpcException(ErrorCodeConst.ErrorCode99998);
		}
	}

	@Override
	public ChannelChargingCodeVO copyPropertiesToVO(ChannelChargingCode temp)
	{
		logger.debug("copyPropertiesToVO start");
		ChannelChargingCodeVO vo = new ChannelChargingCodeVO();
		try
		{
			vo.setId(temp.getId());
			vo.setChannelId(temp.getChannelId());
			vo.setChargingAmount(BigDecimalUtil.divide(temp.getChargingAmount(), new BigDecimal("100"), 2,
					BigDecimal.ROUND_HALF_UP));
			vo.setChargingCode(temp.getChargingCode());
			vo.setRemark(temp.getRemark());
			vo.setStatus(temp.getStatus());
			vo.setChannelLabel(dataSourceService.findOptionVOById(CommonConstant.DataSourceName.CHANNEL,
					String.valueOf(temp.getChannelId())).getText());
			vo.setStatusLabel(dataSourceService.findOptionVOById(CommonConstant.DataSourceName.COMMON_STATUS,
					String.valueOf(temp.getStatus())).getText());
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
