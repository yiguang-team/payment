package com.yiguang.payment.payment.risk.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.rpc.RpcException;
import com.yiguang.payment.common.CommonConstant;
import com.yiguang.payment.common.exception.ErrorCodeConst;
import com.yiguang.payment.common.numsection.entity.NumSection;
import com.yiguang.payment.common.numsection.service.CheckNumSectionService;
import com.yiguang.payment.common.utils.StringUtil;
import com.yiguang.payment.payment.entity.BasicRule;
import com.yiguang.payment.payment.entity.BlackList;
import com.yiguang.payment.payment.entity.MerchantRejection;
import com.yiguang.payment.payment.entity.WhiteList;
import com.yiguang.payment.payment.risk.service.RiskService;
import com.yiguang.payment.payment.service.BasicRuleService;
import com.yiguang.payment.payment.service.BlackListService;
import com.yiguang.payment.payment.service.MerchantRejectionService;
import com.yiguang.payment.payment.service.WhiteListService;

@Service("riskService")
@Transactional
public class RiskServiceImpl implements RiskService
{
	private static Logger logger = LoggerFactory.getLogger(RiskServiceImpl.class);
	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@PersistenceContext
	private EntityManager em;
	@Autowired
	private CheckNumSectionService checkNumSectionService;
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private MerchantRejectionService merchantRejectionService;
	@Autowired
	private BasicRuleService basicRuleService;
	@Autowired
	private BlackListService blackListService;
	@Autowired
	private WhiteListService whiteListService;

	@Override
	public void checkMerchantRejection(String mobile, String merchantB)
	{
		try
		{
			logger.debug("[RiskServiceImpl: checkMerchantRejection(" + mobile + " , " + merchantB + ")] start");
			// 根据当前商户B查
			List<MerchantRejection> list = merchantRejectionService.queryMerchantRejectionByMerchantB(Long
					.parseLong(merchantB));
			if (list != null)
			{
				for (MerchantRejection rejection : list)
				{
					Long merchantAId = rejection.getMerchantA();
					String sql = "select count(1) from t_merchant_order where mobile = '" + mobile
							+ "' and merchant_id = '" + merchantAId + "' and pay_status = "
							+ CommonConstant.PayStatus.SUCCESS;
					logger.debug("[RiskServiceImpl: checkMerchantRejection sql= (" + sql + ")]");
					Query query = em.createNativeQuery(sql);
					BigDecimal count = (BigDecimal) query.getSingleResult();// 执行得到结果
					int cont = count.intValue();
					// 有消费过A商户
					if (cont > 0)
					{
						logger.error("[RiskServiceImpl: checkMerchantRejection(" + mobile + " , " + merchantB
								+ ")] failed");
						throw new RpcException(ErrorCodeConst.ErrorCode10514);
					}
				}
			}
			logger.debug("[RiskServiceImpl: checkMerchantRejection(" + mobile + " , " + merchantB + ")] end");
		}
		catch (RpcException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			logger.error("RiskServiceImpl: checkMerchantRejection(" + mobile + ")] failed");
			logger.error(e.getLocalizedMessage(), e);
			throw new RpcException(ErrorCodeConst.ErrorCode99998);
		}
	}

	@Override
	public long checkBasicRule(long channelId, String provinceId, String cityId, long merchantId, long productId,
			long pointId, String mobile, String ip, String username, long amount)
	{
		long brID = 0;
		List<BasicRule> list = basicRuleService.queryListByParms(channelId, provinceId, cityId, merchantId, productId,
				pointId, mobile, ip, username);
		int NOT_LIMIT = CommonConstant.RiskSelectType.NON_LIMIT;
		int CURRENT = CommonConstant.RiskSelectType.CURRENT;
		if(list != null){
			for (BasicRule br : list)
			{
				// 拼查询订单条件
				String where = " where 1=1 ";

				// 渠道
				if (br.getChannelId() == CURRENT)
				{
					where = where + " and channel_id = " + channelId;
				}
				else if (br.getChannelId() == NOT_LIMIT)
				{

				}
				else
				{
					where = where + " and channel_id = " + br.getChannelId();
				}

				// 省份
				if (String.valueOf(CURRENT).equals(br.getProvinceId()))
				{
					where = where + " and province_id = '" + provinceId + "'";
				}
				else if (String.valueOf(NOT_LIMIT).equals(br.getProvinceId()))
				{

				}
				else
				{
					where = where + " and province_id = '" + br.getProvinceId() + "'";
				}

				// 城市
				if (String.valueOf(CURRENT).equals(br.getCityId()))
				{
					where = where + " and city_id = '" + cityId + "'";
				}
				else if (String.valueOf(NOT_LIMIT).equals(br.getCityId()))
				{

				}
				else
				{
					where = where + " and city_id = '" + br.getCityId() + "'";
				}

				// 商户
				if (br.getMerchantId() == CURRENT)
				{
					where = where + " and merchant_id = " + merchantId;
				}
				else if (br.getMerchantId() == NOT_LIMIT)
				{

				}
				else
				{
					where = where + " and merchant_id = " + br.getMerchantId();
				}

				// 产品
				if (br.getProductId() == CURRENT)
				{
					where = where + " and product_id = " + productId;
				}
				else if (br.getProductId() == NOT_LIMIT)
				{

				}
				else
				{
					where = where + " and product_id = " + br.getProductId();
				}

				// 计费点
				if (br.getPointId() == CURRENT)
				{
					where = where + " and charging_point_id = " + pointId;
				}
				else if (br.getPointId() == NOT_LIMIT)
				{

				}
				else
				{
					where = where + " and charging_point_id = " + br.getPointId();
				}

				// mobile 号码
				if (String.valueOf(CURRENT).equals(br.getMobile()))
				{
					where = where + " and mobile = '" + mobile + "'";
				}
				else if (String.valueOf(NOT_LIMIT).equals(br.getMobile()))
				{

				}
				else
				{
					where = where + " and mobile = '" + br.getMobile() + "'";
				}

				// IP
				if (String.valueOf(CURRENT).equals(br.getIp()))
				{
					where = where + " and request_ip = '" + ip + "'";
				}
				else if (String.valueOf(NOT_LIMIT).equals(br.getIp()))
				{

				}
				else
				{
					where = where + " and request_ip = '" + br.getIp() + "'";
				}

				// username 充值账号
				if (String.valueOf(CURRENT).equals(br.getUsername()))
				{
					where = where + " and username = '" + username + "'";
				}
				else if (String.valueOf(NOT_LIMIT).equals(br.getUsername()))
				{

				}
				else
				{
					where = where + " and username = '" + br.getUsername() + "'";
				}

				where = where + " and pay_status <> " + CommonConstant.PayStatus.FAILED;

				// 拼查询时间条件
				int TIME_RELATIVE = CommonConstant.TimeLimitType.TIME_RELATIVE;
				int TIME_UNIT = CommonConstant.TimeLimitType.TIME_UNIT;
				int TIME_PERIOD = CommonConstant.TimeLimitType.TIME_PERIOD;
				int MONTH = CommonConstant.TimeUnit.MONTH;
				int DAY = CommonConstant.TimeUnit.DAY;
				int HOUR = CommonConstant.TimeUnit.HOUR;
				int MINUTE = CommonConstant.TimeUnit.MINUTE;
				Calendar calendar = Calendar.getInstance();

				int yyyy = calendar.get(Calendar.YEAR);
				int MM = calendar.get(Calendar.MONTH);
				int dd = calendar.get(Calendar.DAY_OF_MONTH);
				int HH = calendar.get(Calendar.HOUR_OF_DAY);

				if (TIME_UNIT == br.getTimeType())
				{
					int temp = 0;
					String startDate = null;
					String endDate = null;
					if (br.getTimeUnit() == MINUTE)
					{
						temp = HH;
						calendar.set(yyyy, MM, dd, HH, 0, 0);
						startDate = DATE_FORMAT.format(calendar.getTime());
						calendar.add(Calendar.MINUTE, 1);
						endDate = DATE_FORMAT.format(calendar.getTime());
					}
					if (br.getTimeUnit() == HOUR)
					{
						temp = HH;
						calendar.set(yyyy, MM, dd, HH, 0, 0);
						startDate = DATE_FORMAT.format(calendar.getTime());
						calendar.add(Calendar.HOUR_OF_DAY, 1);
						endDate = DATE_FORMAT.format(calendar.getTime());
					}
					else if (br.getTimeUnit() == DAY)
					{
						temp = dd;
						calendar.set(yyyy, MM, dd, 0, 0, 0);
						startDate = DATE_FORMAT.format(calendar.getTime());
						calendar.add(Calendar.DAY_OF_MONTH, 1);
						endDate = DATE_FORMAT.format(calendar.getTime());
					}
					else if (br.getTimeUnit() == MONTH)
					{
						temp = MM;
						calendar.set(yyyy, MM, 1, 0, 0, 0);
						startDate = DATE_FORMAT.format(calendar.getTime());
						calendar.add(Calendar.MONTH, 1);
						endDate = DATE_FORMAT.format(calendar.getTime());
					}

					if (br.getStartUnit() != br.getEndUnit())
					{
						if (temp >= br.getStartUnit() && temp <= br.getEndUnit())
						{
							where = where + " and request_time >= to_date('" + startDate
									+ "','yyyy-mm-dd hh24:mi:ss') and request_time < to_date('" + endDate
									+ "','yyyy-mm-dd hh24:mi:ss')";
						}
					}
					else
					{
						where = where + " and request_time >= to_date('" + startDate
								+ "','yyyy-mm-dd hh24:mi:ss') and request_time < to_date('" + endDate
								+ "','yyyy-mm-dd hh24:mi:ss')";
					}
				}
				else if (TIME_PERIOD == br.getTimeType())
				{
					String startDate = DATE_FORMAT.format(br.getStartTime());
					String endDate = DATE_FORMAT.format(br.getEndTime());
					where = where + " and request_time >= to_date('" + startDate
							+ "','yyyy-mm-dd hh24:mi:ss') and request_time < to_date('" + endDate
							+ "','yyyy-mm-dd hh24:mi:ss')";
				}
				else if (TIME_RELATIVE == br.getTimeType())
				{
					int relativeValue = Integer.parseInt(String.valueOf(br.getRelativeValue()));
					int relativeUnit = br.getRelativeUnit();
					if (relativeUnit == MINUTE)
					{
						calendar.add(Calendar.MINUTE, (0 - relativeValue));
					}
					if (relativeUnit == HOUR)
					{
						calendar.add(Calendar.HOUR_OF_DAY, (0 - relativeValue));
					}
					else if (relativeUnit == DAY)
					{
						calendar.add(Calendar.DAY_OF_MONTH, (0 - relativeValue));
					}
					else if (relativeUnit == MONTH)
					{
						calendar.add(Calendar.MONTH, (0 - relativeValue));
					}

					where = where + " and request_time >= to_date('" + DATE_FORMAT.format(calendar.getTime())
							+ "','yyyy-mm-dd hh24:mi:ss') ";
				}

				// 拼金额 还是 笔数
				String select = "";
				if (br.getLimitType() == CommonConstant.LimitType.LIMIT_NUM)
				{
					select = select + "select count(1) as result from t_merchant_order r ";
				}
				else
				{
					select = select + "select sum(pay_amount) as result from t_merchant_order r ";
				}

				String sql = select + where;

				logger.info("check risk sql : [" + sql + "]");

				Map<String, Object> map = jdbcTemplate.queryForMap(sql);

				Object temp = map.get("result");

				if (temp == null )
				{
					temp = new BigDecimal("0");
				}

				long result =((BigDecimal)temp).longValue();

				if (br.getLimitType() == CommonConstant.LimitType.LIMIT_NUM)
				{
					result = result + 1;
					
					if (result > br.getVolume()/100)
					{
						if (br.getAction() == CommonConstant.ACTION.FORBID)
						{
							brID = br.getId();
							break;
						}
					}
				}
				else
				{
					result = result + amount;
					
					if (result > br.getVolume())
					{
						if (br.getAction() == CommonConstant.ACTION.FORBID)
						{
							brID = br.getId();
							break;
						}
					}
				}
			}
		}
		return brID;
	}

	@Override
	public void checkBlackList(String mobile, String IP, String user)
	{
		try
		{
			logger.debug("[RiskServiceImpl: checkBlackList(" + mobile + "," + IP + "," + user + ")] start");
			if (StringUtil.isNotBlank(mobile))
			{
				BlackList mobileList = blackListService.queryBlackListByAll(CommonConstant.ListType.MOBILE, mobile);
				if (mobileList != null)
				{
					logger.error("[RiskServiceImpl: checkBlackList(" + CommonConstant.ListType.MOBILE + "," + mobile
							+ ")] failed");
					throw new RpcException(ErrorCodeConst.ErrorCode10531);
				}
				NumSection section = checkNumSectionService.checkNum(mobile);

				BlackList sectionList = blackListService.queryBlackListByAll(CommonConstant.ListType.SECTION,
						section.getSectionId());
				if (sectionList != null)
				{
					logger.error("[RiskServiceImpl: checkBlackList(" + CommonConstant.ListType.SECTION + ","
							+ section.getSectionId() + ")] failed");
					throw new RpcException(ErrorCodeConst.ErrorCode10532);
				}
			}

			if (StringUtil.isNotBlank(IP))
			{
				BlackList ipList = blackListService.queryBlackListByAll(CommonConstant.ListType.IP, IP);
				if (ipList != null)
				{
					logger.error("[RiskServiceImpl: checkBlackList(" + CommonConstant.ListType.IP + "," + IP
							+ ")] failed");
					throw new RpcException(ErrorCodeConst.ErrorCode10533);
				}
			}
			if (StringUtil.isNotBlank(user))
			{
				BlackList userList = blackListService.queryBlackListByAll(CommonConstant.ListType.USER, user);
				if (userList != null)
				{
					logger.error("[RiskServiceImpl: checkBlackList(" + CommonConstant.ListType.USER + "," + user
							+ ")] failed");
					throw new RpcException(ErrorCodeConst.ErrorCode10534);
				}
			}
			logger.debug("[RiskServiceImpl: checkBlackList(" + mobile + "," + IP + "," + user + ")] end");
		}
		catch (RpcException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			logger.error("[RiskServiceImpl: checkBlackList(" + mobile + "," + IP + "," + user + ")] fail");
			logger.error(e.getLocalizedMessage(), e);
			throw new RpcException(ErrorCodeConst.ErrorCode99998);
		}
	}

	@Override
	public boolean checkWhiteList(String mobile, String IP, String user)
	{
		try
		{
			logger.debug("[RiskServiceImpl: checkWhiteList(" + mobile + "," + IP + "," + user + ")] start");

			boolean result = false;
			if (StringUtil.isNotBlank(mobile))
			{
				NumSection section = checkNumSectionService.checkNum(mobile);
				String sectionId = section.getSectionId();

				WhiteList mobileList = whiteListService.queryWhiteListByAll(CommonConstant.ListType.MOBILE, mobile);
				if (mobileList != null)
				{
					logger.info("[RiskServiceImpl: checkWhiteList(" + CommonConstant.ListType.MOBILE + "," + mobile
							+ ")] success");
					result = true;
				}
				WhiteList sectionList = whiteListService
						.queryWhiteListByAll(CommonConstant.ListType.SECTION, sectionId);
				if (sectionList != null)
				{
					logger.info("[RiskServiceImpl: checkWhiteList(" + CommonConstant.ListType.SECTION + "," + sectionId
							+ ")] success");
					result = true;
				}
			}
			if (StringUtil.isNotBlank(IP))
			{
				WhiteList ipList = whiteListService.queryWhiteListByAll(CommonConstant.ListType.IP, IP);
				if (ipList != null)
				{
					logger.info("[RiskServiceImpl: checkWhiteList(" + CommonConstant.ListType.IP + "," + IP
							+ ")] success");
					result = true;
				}
			}
			if (StringUtil.isNotBlank(user))
			{
				WhiteList userList = whiteListService.queryWhiteListByAll(CommonConstant.ListType.USER, user);
				if (userList != null)
				{
					logger.info("[RiskServiceImpl: checkWhiteList(" + CommonConstant.ListType.USER + "," + user
							+ ")] success");
					result = true;
				}
			}
			logger.debug("[RiskServiceImpl: checkWhiteList(" + mobile + "," + IP + "," + user + ")] end");
			return result;
		}
		catch (RpcException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			logger.error("[RiskServiceImpl: checkWhiteList(" + mobile + "," + IP + "," + user + ")] fail");
			logger.error(e.getLocalizedMessage(), e);
			throw new RpcException(ErrorCodeConst.ErrorCode99998);
		}
	}
	
}
