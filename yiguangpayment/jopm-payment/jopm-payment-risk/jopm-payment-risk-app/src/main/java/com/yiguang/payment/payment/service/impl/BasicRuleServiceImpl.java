package com.yiguang.payment.payment.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.persistence.SearchFilter;

import com.alibaba.dubbo.rpc.RpcException;
import com.yiguang.payment.common.CommonConstant;
import com.yiguang.payment.common.Constant;
import com.yiguang.payment.common.datasource.service.DataSourceService;
import com.yiguang.payment.common.exception.ErrorCodeConst;
import com.yiguang.payment.common.query.PageUtil;
import com.yiguang.payment.common.query.YcPage;
import com.yiguang.payment.common.utils.BeanUtils;
import com.yiguang.payment.common.utils.StringUtil;
import com.yiguang.payment.payment.entity.BasicRule;
import com.yiguang.payment.payment.repository.BasicRuleDao;
import com.yiguang.payment.payment.service.BasicRuleService;
import com.yiguang.payment.payment.vo.BasicRuleVO;

@Service("basicRuleService")
@Transactional
public class BasicRuleServiceImpl implements BasicRuleService
{
	private static Logger logger = LoggerFactory.getLogger(BasicRuleServiceImpl.class);
	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	@PersistenceContext
	private EntityManager em;

	@Autowired
	private BasicRuleDao basicRuleDao;

	@Autowired
	private DataSourceService dataSourceService;

	@Override
	@Cacheable(value = "basicRuleCache")
	public YcPage<BasicRuleVO> queryBasicRuleList(Map<String, Object> searchParams, int pageNumber, int pageSize,
			String sortType)
	{
		logger.debug("queryBasicRuleList start");
		try
		{
			Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
			YcPage<BasicRule> ycPage = PageUtil.queryYcPage(basicRuleDao, filters, pageNumber, pageSize, new Sort(
					Direction.DESC, "id"), BasicRule.class);

			YcPage<BasicRuleVO> result = new YcPage<BasicRuleVO>();
			result.setPageTotal(ycPage.getPageTotal());
			result.setCountTotal(ycPage.getCountTotal());
			List<BasicRule> list = ycPage.getList();
			List<BasicRuleVO> voList = new ArrayList<BasicRuleVO>();
			BasicRuleVO vo = null;
			for (BasicRule temp : list)
			{
				vo = copyPropertiesToVO(temp);
				voList.add(vo);
			}
			result.setList(voList);
			logger.debug("queryBasicRuleList end");
			return result;
		}
		catch (RpcException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			logger.error("queryBasicRuleList failed");
			logger.error(e.getLocalizedMessage(), e);
			throw new RpcException(ErrorCodeConst.ErrorCode99998);
		}
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Override
	@CacheEvict(value = "basicRuleCache", allEntries = true, beforeInvocation = true)
	public BasicRule saveBasicRule(BasicRule basicRule)
	{
		logger.debug("saveBasicRule start, basicRule [" + basicRule.toString() + "]");
		try
		{

			BasicRule queryBasicRule = null;
			// 唯一性
			if (BeanUtils.isNotNull(queryBasicRule) && basicRule.getId() == 0)
			{

				logger.error("saveBasicRule failed, basicRule [" + basicRule.toString() + "]");
				throw new RpcException(ErrorCodeConst.ErrorCode10501);
			}
			else
			{
				basicRule = basicRuleDao.save(basicRule);
			}

			logger.debug("saveBasicRule end, basicRule [" + basicRule.toString() + "]");
			return basicRule;
		}
		catch (RpcException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			logger.error("saveBasicRule failed, basicRule [" + basicRule.toString() + "]");
			logger.error(e.getLocalizedMessage(), e);
			throw new RpcException(ErrorCodeConst.ErrorCode99998);
		}
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Override
	@Cacheable(value = "basicRuleCache", key = "#root.methodName+#id")
	public BasicRule queryBasicRule(long id)
	{
		logger.debug("queryBasicRule start, id [" + id + "]");
		try
		{
			BasicRule basicRule = basicRuleDao.findOne(id);
			logger.debug("queryBasicRule end, id [" + id + "]");
			return basicRule;
		}
		catch (Exception e)
		{
			logger.error("queryBasicRule faild, id [" + id + "]");
			logger.error(e.getLocalizedMessage(), e);
			throw new RpcException(ErrorCodeConst.ErrorCode99998);
		}
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Override
	public BasicRuleVO copyPropertiesToVO(BasicRule temp)
	{
		logger.debug("copyPropertiesToVO start");
		try
		{
			BasicRuleVO vo = new BasicRuleVO();
			vo.setId(temp.getId());

			vo.setTimeType(temp.getTimeType());
			vo.setTimeTypeLabel(dataSourceService.findOptionVOById(CommonConstant.DataSourceName.RISK_TIME_TYPE,
					String.valueOf(temp.getTimeType())).getText());
			vo.setTimeUnit(temp.getTimeUnit());
			vo.setStartUnit(temp.getStartUnit());
			vo.setEndUnit(temp.getEndUnit());
			vo.setTimeUnitLabel(dataSourceService.findOptionVOById(CommonConstant.DataSourceName.TIME_UNIT,
					String.valueOf(temp.getTimeUnit())).getText());
			vo.setRelativeValue(temp.getRelativeValue());
			vo.setRelativeUnit(temp.getRelativeUnit());
			vo.setRelativeUnitLabel(dataSourceService.findOptionVOById(CommonConstant.DataSourceName.TIME_UNIT,
					String.valueOf(temp.getRelativeUnit())).getText());
			if (null != temp.getStartTime())
			{
				vo.setStartTime(DATE_FORMAT.format(temp.getStartTime()));
			}
			if (null != temp.getEndTime())
			{
				vo.setEndTime(DATE_FORMAT.format(temp.getEndTime()));
			}
			vo.setLimitType(temp.getLimitType());
			vo.setLimitTypeLabel(dataSourceService.findOptionVOById(CommonConstant.DataSourceName.RISK_LIMIT_TYPE,
					String.valueOf(temp.getLimitType())).getText());
			vo.setVolume(temp.getVolume() / 100);
			vo.setChannelId(temp.getChannelId());
			if (temp.getChannelId() != CommonConstant.RiskSelectType.NON_LIMIT
					&& temp.getChannelId() != CommonConstant.RiskSelectType.CURRENT)
			{
				vo.setChannelLabel(dataSourceService.findOptionVOById(CommonConstant.DataSourceName.CHANNEL,
						String.valueOf(temp.getChannelId())).getText());
			}
			vo.setProvinceId(temp.getProvinceId());
			if (!String.valueOf(CommonConstant.RiskSelectType.NON_LIMIT).equals(temp.getProvinceId())
					&& !String.valueOf(CommonConstant.RiskSelectType.CURRENT).equals(temp.getProvinceId()))
			{
				vo.setProvinceLabel(dataSourceService.findOptionVOById(CommonConstant.DataSourceName.PROVINCE,
						String.valueOf(temp.getProvinceId())).getText());
			}
			vo.setCityId(temp.getCityId());
			if (!String.valueOf(CommonConstant.RiskSelectType.NON_LIMIT).equals(temp.getCityId())
					&& !String.valueOf(CommonConstant.RiskSelectType.CURRENT).equals(temp.getCityId()))
			{
				vo.setCityLabel(dataSourceService.findOptionVOById(CommonConstant.DataSourceName.CITY,
						String.valueOf(temp.getCityId())).getText());
			}
			vo.setMerchantId(temp.getMerchantId());
			if (temp.getMerchantId() != CommonConstant.RiskSelectType.NON_LIMIT
					&& temp.getMerchantId() != CommonConstant.RiskSelectType.CURRENT)
			{
				vo.setMerchantLabel(dataSourceService.findOptionVOById(CommonConstant.DataSourceName.MERCHANT,
						String.valueOf(temp.getMerchantId())).getText());
			}
			vo.setProductId(temp.getProductId());
			if (temp.getProductId() != CommonConstant.RiskSelectType.NON_LIMIT
					&& temp.getProductId() != CommonConstant.RiskSelectType.CURRENT)
			{
				vo.setProductLabel(dataSourceService.findOptionVOById(CommonConstant.DataSourceName.PRODUCT,
						String.valueOf(temp.getProductId())).getText());
			}
			vo.setPointId(temp.getPointId());
			if (temp.getPointId() != CommonConstant.RiskSelectType.NON_LIMIT
					&& temp.getPointId() != CommonConstant.RiskSelectType.CURRENT)
			{
				vo.setPointLabel(dataSourceService.findOptionVOById(CommonConstant.DataSourceName.POINT,
						String.valueOf(temp.getPointId())).getText());
			}
			vo.setAction(temp.getAction());
			vo.setActionLabel(dataSourceService.findOptionVOById(CommonConstant.DataSourceName.RISK_ACTION,
					String.valueOf(temp.getAction())).getText());
			
			vo.setMobile(temp.getMobile());
			vo.setIp(temp.getIp());
			vo.setUsername(temp.getUsername());

			vo.setStatus(temp.getStatus());
			vo.setStatusLabel(dataSourceService.findOptionVOById(CommonConstant.DataSourceName.COMMON_STATUS,
					String.valueOf(temp.getStatus())).getText());

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

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Override
	@CacheEvict(value = "basicRuleCache", allEntries = true, beforeInvocation = true)
	public BasicRule updateBasicRuleStatus(BasicRule basicRule)
	{
		try
		{
			logger.debug("updateBasicRuleStatus start, basicRule [" + basicRule.toString() + "]");
			BasicRule rule = basicRuleDao.findOne(basicRule.getId());
			if (rule != null)
			{
				rule.setStatus(basicRule.getStatus());
				rule = basicRuleDao.save(rule);
			}
			else
			{
				// 不存在
				logger.error("updateBasicRuleStatus failed, basicRule [" + basicRule.toString() + "]");
				throw new RpcException(ErrorCodeConst.ErrorCode10507);
			}
			logger.debug("updateBasicRuleStatus end, basicRule [" + basicRule.toString() + "]");
			return rule;
		}
		catch (RpcException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			logger.error("updateSectionBanStatus failed, basicRule [" + basicRule.toString() + "]");
			logger.error(e.getLocalizedMessage(), e);
			throw new RpcException(ErrorCodeConst.ErrorCode99998);
		}
	}
	
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Override
	@CacheEvict(value = "basicRuleCache", allEntries = true, beforeInvocation = true)
	public String deleteBasicRule(BasicRule basicRule) {
		logger.debug("deleteBasicRule start, basicRule [" + basicRule.toString() + "]");
		try
		{
			BasicRule queryProduct = basicRuleDao.findOne(basicRule.getId());
			if (queryProduct != null)
			{
				basicRuleDao.delete(queryProduct);
			}
			else
			{
				// 准备删除的产品不存在！
				logger.error("deleteBasicRule failed, basicRule [" + basicRule.toString() + "]");
				throw new RpcException(19409);
			}
			logger.debug("deleteBasicRule end, basicRule [" + basicRule.toString() + "]");
			return Constant.Common.SUCCESS;
		}
		catch (RpcException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			logger.error("deleteBasicRule failed, basicRule [" + basicRule.toString() + "]");
			logger.error(e.getLocalizedMessage(), e);
			throw new RpcException(ErrorCodeConst.ErrorCode99998);
		}
	}
	
	
	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Override
	@Cacheable(value = "basicRuleCache", key = "#root.methodName+#channelId+#provinceId+#cityId+#merchantId+#productId+#pointId+#mobile+#ip+#username")
	public List<BasicRule> queryListByParms(long channelId, String provinceId, String cityId, long merchantId,
			long productId, long pointId, String mobile, String ip, String username)
	{
		String insidesql = "select * from t_basic_rule r where 1=1";

		String condition = StringUtil.initString();
		int NOT_LIMIT=CommonConstant.RiskSelectType.NON_LIMIT;
		int CURRENT=CommonConstant.RiskSelectType.CURRENT;
		if (channelId != 0)
		{
			condition = condition + " and ( channel_id = " + channelId + " or channel_id = "+CURRENT+" or channel_id = " + NOT_LIMIT + ")";
		}
		else
		{
			condition = condition + " and ( channel_id = " + NOT_LIMIT + ")";
		}
		if (StringUtil.isNotBlank(provinceId))
		{
			condition = condition + " and ( province_id = '" + provinceId + "' or province_id = '"+CURRENT+"' or province_id = '" + NOT_LIMIT + "')";
		}
		else
		{
			condition = condition + " and ( province_id = '" + NOT_LIMIT + "')";
		}
		
		if (StringUtil.isNotBlank(cityId))
		{
			condition = condition + " and ( city_id = '" + cityId + "' or city_id = '"+CURRENT+"' or city_id = '" + NOT_LIMIT + "')";
		}
		else
		{
			condition = condition + " and ( city_id = '" + NOT_LIMIT + "')";
		}
		
		if (merchantId != 0)
		{
			condition = condition + " and ( merchant_id = " + merchantId + " or merchant_id = "+CURRENT+" or merchant_id = " + NOT_LIMIT + ")";
		}
		else
		{
			condition = condition + " and ( merchant_id = " + NOT_LIMIT + ")";
		}
		
		if (productId != 0)
		{
			condition = condition + " and ( product_id = " + productId + " or product_id = "+CURRENT+" or product_id = " + NOT_LIMIT + ")";
		}
		else
		{
			condition = condition + " and ( product_id = " + NOT_LIMIT + ")";
		}
		
		if (pointId != 0)
		{
			condition = condition + " and ( point_id = " + pointId + " or point_id = "+CURRENT+" or point_id = " + NOT_LIMIT + ")";
		}
		else
		{
			condition = condition + " and ( point_id = " + NOT_LIMIT + ")";
		}
		
		if (StringUtil.isNotBlank(mobile))
		{
			condition = condition + " and ( mobile = '" + mobile + "' or mobile = '"+CURRENT+"' or mobile = '" + NOT_LIMIT + "')";
		}
		else
		{
			condition = condition + " and ( mobile = '" + NOT_LIMIT + "')";
		}
		
		if (StringUtil.isNotBlank(ip))
		{
			condition = condition + " and ( ip = '" + ip + "' or ip = '"+CURRENT+"' or ip = '" + NOT_LIMIT + "')";
		}
		else
		{
			condition = condition + " and ( ip = '" + NOT_LIMIT + "')";
		}
		
		if (StringUtil.isNotBlank(username))
		{
			condition = condition + " and ( username = '" + username + "' or username = '"+CURRENT+"' or username = '" + NOT_LIMIT + "')";
		}
		else
		{
			condition = condition + " and ( username = '" + NOT_LIMIT + "')";
		}
		
		condition = condition + " and status = " + CommonConstant.CommonStatus.OPEN;
		insidesql = insidesql + condition;
		logger.info("select risk rule sql:[" + insidesql + "]");
		Query query = em.createNativeQuery(insidesql, BasicRule.class);
		List<BasicRule> list = query.getResultList();
		return list;
	}
}
