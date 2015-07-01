package com.yiguang.payment.payment.order.service.impl;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.rpc.RpcException;
import com.yiguang.payment.business.product.entity.Point;
import com.yiguang.payment.business.product.service.PointService;
import com.yiguang.payment.common.CommonConstant;
import com.yiguang.payment.common.exception.ErrorCodeConst;
import com.yiguang.payment.common.numsection.entity.CarrierInfo;
import com.yiguang.payment.common.numsection.entity.NumSection;
import com.yiguang.payment.common.numsection.service.CarrierInfoService;
import com.yiguang.payment.common.numsection.service.CheckNumSectionService;
import com.yiguang.payment.common.security.MD5Util;
import com.yiguang.payment.common.utils.StringUtil;
import com.yiguang.payment.payment.entity.ChannelChargingCode;
import com.yiguang.payment.payment.entity.Merchant;
import com.yiguang.payment.payment.order.service.ParameterValidateService;
import com.yiguang.payment.payment.service.ChannelChargingCodeService;
import com.yiguang.payment.payment.service.MerchantService;

@Service("parameterValidateService")
@Transactional
public class ParameterValidateServiceImpl implements ParameterValidateService
{

	private static Logger logger = LoggerFactory.getLogger(ParameterValidateServiceImpl.class);
	@Autowired
	private MerchantService merchantService;
	@Autowired
	private CarrierInfoService carrierInfoService;
	@Autowired
	private PointService pointService;
	@Autowired
	private ChannelChargingCodeService channelChargingCodeService;
	@Autowired
	private CheckNumSectionService checkNumSectionService;

	private final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");

	String regex_mobile = "^\\d{11}$";
	String regex_number_4 = "^\\d{4}$";
	String regex_number_16 = "^\\d{16}$";
	String regex_number_2 = "^\\d{1,2}$";
	String regex_number_6 = "^\\d{6}$";
	String regex_number_date = "^20\\d\\d[01]\\d[0123]\\d[012]\\d[012345]\\d[012345]\\d$";
	String regex_32 = "^\\w{32}$";

	@Override
	public NumSection checkMobileMatches(String mobile)
	{
		logger.debug("[ParameterValidateServiceImpl:checkMobileMatches(" + mobile + ")] start");
		if (!Pattern.matches(regex_mobile, mobile))
		{

			logger.error("[ParameterValidateServiceImpl:checkMobileMatches(" + mobile + ")] failed");
			throw new RpcException(ErrorCodeConst.ErrorCode90001);
		}

		NumSection section = checkNumSectionService.checkNum(mobile);
		if (section.getCarrierInfo() == null)
		{
			logger.error("[ParameterValidateServiceImpl:checkMobileMatches(" + mobile + ")] failed");
			throw new RpcException(ErrorCodeConst.ErrorCode90001);
		}
		
		return section;
	}

	@Override
	public void checkCpidMatches(String cpid)
	{
		logger.debug("[ParameterValidateServiceImpl:checkCpidMatches(" + cpid + ")] start");
		if (!Pattern.matches(regex_number_4, cpid))
		{
			logger.error("[ParameterValidateServiceImpl:checkCpidMatches(" + cpid + ")] failed");
			throw new RpcException(ErrorCodeConst.ErrorCode90002);
		}
	}

	@Override
	public void checkOperatorIdMatches(String operatorId)
	{
		logger.debug("[ParameterValidateServiceImpl:checkChannelIdMatches(" + operatorId + ")] start ");
		if (!Pattern.matches(regex_number_2, operatorId))
		{
			logger.error("[ParameterValidateServiceImpl:checkChannelIdMatches(" + operatorId + ")] failed");
			throw new RpcException(ErrorCodeConst.ErrorCode90003);
		}
	}

	@Override
	public void checkServiceIdMatches(String serviceid)
	{
		logger.debug("[ParameterValidateServiceImpl:checkServiceIdMatches(" + serviceid + ")] start");
		if (!Pattern.matches(regex_number_16, serviceid))
		{
			logger.error("[ParameterValidateServiceImpl:checkServiceIdMatches(" + serviceid + ")] failed");
			throw new RpcException(ErrorCodeConst.ErrorCode90004);
		}
	}

	@Override
	public void checkDatetimeMatches(String datetime)
	{
		logger.debug("[ParameterValidateServiceImpl:checkDatetimeMatches(" + datetime + ")] start");
		if (!Pattern.matches(regex_number_date, datetime))
		{
			logger.error("[ParameterValidateServiceImpl:checkDatetimeMatches(" + datetime + ")] failed");
			throw new RpcException(ErrorCodeConst.ErrorCode90005);
		}
		else
		{
			try
			{
				Date date = new Date();
				String xxx = sdf.format(date);
				if (((sdf.parse(xxx).getTime()) - sdf.parse(datetime).getTime()) > 5 * 60 * 1000)
				{
					logger.error("[ParameterValidateServiceImpl:checkDatetimeMatches(" + datetime + ")] failed");
					throw new RpcException(ErrorCodeConst.ErrorCode90006);
				}
			}
			catch (ParseException e)
			{
				logger.error("[ParameterValidateServiceImpl:checkDatetimeMatches(" + datetime + ")] failed");
				throw new RpcException(ErrorCodeConst.ErrorCode90005);
			}
		}
	}

	@Override
	public void checkSignMatches(String sign)
	{
		logger.debug("[ParameterValidateServiceImpl:checkSignMatches(" + sign + ")] start");
		if (!Pattern.matches(regex_32, sign))
		{
			logger.error("[ParameterValidateServiceImpl:checkSignMatches(" + sign + ")] failed");
			throw new RpcException(ErrorCodeConst.ErrorCode90007);
		}
	}

	@Override
	public void checkOrderIdMatches(String orderid)
	{
		logger.debug("[ParameterValidateServiceImpl:checkOrderIdMatches(" + orderid + ")] start");
		if (StringUtils.isEmpty(orderid))
		{
			logger.error("[ParameterValidateServiceImpl:checkOrderIdMatches(" + orderid + ")] failed");
			throw new RpcException(ErrorCodeConst.ErrorCode90008);
		}
	}

	@Override
	public void checkSmscodeMatches(String smscode)
	{
		logger.debug("[ParameterValidateServiceImpl:checkSmscodeMatches(" + smscode + ")] start");
		if (!Pattern.matches(regex_number_6, smscode))
		{
			logger.error("[ParameterValidateServiceImpl:checkSmscodeMatches(" + smscode + ")] failed");
			throw new RpcException(ErrorCodeConst.ErrorCode90009);
		}
	}

	@Override
	public NumSection checkMobileMatchCarrier(String mobile, String operatorId)
	{
		logger.debug("[ParameterValidateServiceImpl:checkMobileMatchCarrier(" + mobile + "," + operatorId + ")] start");
		NumSection section = checkNumSectionService.checkNum(mobile);
		if (!StringUtil.equals(String.valueOf(section.getCarrierInfo().getId()), operatorId))
		{
			logger.error("[ParameterValidateServiceImpl:checkMobileMatchCarrier(" + mobile + "," + operatorId
					+ ")] failed");
			throw new RpcException(ErrorCodeConst.ErrorCode90010);
		}

		return section;
	}

	@Override
	public Merchant checkMerchant(String cpid)
	{
		logger.debug("[ParameterValidateServiceImpl:checkMerchant(" + cpid + ")] start");
		Merchant merchant = merchantService.queryMerchant(Long.parseLong(cpid));
		if (merchant == null)
		{
			logger.error("[ParameterValidateServiceImpl:checkMerchant(" + cpid + ")] failed");
			throw new RpcException(ErrorCodeConst.ErrorCode90011);
		}
		else if (merchant.getStatus() == CommonConstant.CommonStatus.CLOSE)
		{
			logger.error("[ParameterValidateServiceImpl:checkMerchant(" + cpid + ")] failed");
			throw new RpcException(ErrorCodeConst.ErrorCode90012);
		}
		return merchant;
	}

	@Override
	public void checkSign(String sign, String md5sign)
	{
		logger.debug("[ParameterValidateServiceImpl:checkSign(" + sign + "," + md5sign + ")] start");
		if (!StringUtil.equals(MD5Util.getMD5Sign(sign).toLowerCase(), md5sign.toLowerCase()))
		{
			logger.error("[ParameterValidateServiceImpl:checkSign(" + sign + "," + md5sign + ")] failed");
			throw new RpcException(ErrorCodeConst.ErrorCode90013);
		}
	}

	@Override
	public CarrierInfo checkCarrier(String operatorId)
	{
		logger.debug("[ParameterValidateServiceImpl:checkCarrier(" + operatorId + ")] start");
		CarrierInfo channel = carrierInfoService.queryCarrierInfo(Long.parseLong(operatorId));
		if (channel == null)
		{
			logger.error("[ParameterValidateServiceImpl:checkCarrier(" + operatorId + ")] failed");
			throw new RpcException(ErrorCodeConst.ErrorCode90014);
		}
		else if (channel.getStatus() == CommonConstant.CommonStatus.CLOSE)
		{
			logger.error("[ParameterValidateServiceImpl:checkCarrier(" + operatorId + ")] failed");
			throw new RpcException(ErrorCodeConst.ErrorCode90015);
		}
		return channel;
	}

	@Override
	public Point checkMerchantChargingCode(String serviceid, String cpid)
	{
		logger.debug("[ParameterValidateServiceImpl:checkChargingCode(" + serviceid + "," + cpid + ")] start");

		Point point = pointService.queryPointByChargingCode(serviceid);

		if (point == null)
		{
			logger.error("[ParameterValidateServiceImpl:checkChargingCode(" + serviceid + "," + cpid + ")] failed");
			throw new RpcException(ErrorCodeConst.ErrorCode90016);
		}
		else if (point.getStatus() == CommonConstant.CommonStatus.CLOSE)
		{
			logger.error("[ParameterValidateServiceImpl:checkChargingCode(" + serviceid + "," + cpid + ")] failed");
			throw new RpcException(ErrorCodeConst.ErrorCode90017);
		}
		else if (point.getMerchantId() != Long.parseLong(cpid))
		{
			logger.error("[ParameterValidateServiceImpl:checkChargingCode(" + serviceid + "," + cpid + ")] failed");
			throw new RpcException(ErrorCodeConst.ErrorCode90018);
		}

		return point;
	}

	@Override
	public ChannelChargingCode checkChannelChargingCode(String serviceid, String operatorId)
	{
		logger.debug("[ParameterValidateServiceImpl:checkChargingCode(" + serviceid + "," + operatorId + ")] start");

		Point point = pointService.queryPointByChargingCode(serviceid);

		BigDecimal MerchantAmount = point.getPayAmount();
		ChannelChargingCode channelChargingCode = channelChargingCodeService.queryChargingCodeByChannelIdAndAmount(
				Long.parseLong(operatorId), MerchantAmount);

		if (channelChargingCode == null)
		{
			logger.error("[ParameterValidateServiceImpl:checkChargingCode(" + serviceid + "," + operatorId + ")] failed");
			throw new RpcException(ErrorCodeConst.ErrorCode90019);
		}
		else if (channelChargingCode.getStatus() == CommonConstant.CommonStatus.CLOSE)
		{
			logger.error("[ParameterValidateServiceImpl:checkChargingCode(" + serviceid + "," + operatorId + ")] failed");
			throw new RpcException(ErrorCodeConst.ErrorCode90020);
		}
		return channelChargingCode;
	}
}
