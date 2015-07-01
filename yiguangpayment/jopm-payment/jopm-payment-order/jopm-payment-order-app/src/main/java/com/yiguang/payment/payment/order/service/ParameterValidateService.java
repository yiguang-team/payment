package com.yiguang.payment.payment.order.service;

import com.yiguang.payment.business.product.entity.Point;
import com.yiguang.payment.common.numsection.entity.CarrierInfo;
import com.yiguang.payment.common.numsection.entity.NumSection;
import com.yiguang.payment.payment.entity.ChannelChargingCode;
import com.yiguang.payment.payment.entity.Merchant;

public interface ParameterValidateService
{

	/**
	 * 手机号码格式校验
	 * 
	 * @param mobile
	 * @return
	 */
	NumSection checkMobileMatches(String mobile);

	/**
	 * 商户ID格式校验
	 * 
	 * @param cpid
	 * @return
	 */
	void checkCpidMatches(String cpid);

	/**
	 * 渠道ID格式校验
	 * 
	 * @param cpid
	 * @return
	 */
	void checkOperatorIdMatches(String channelId);

	/**
	 * 计费ID格式校验
	 * 
	 * @param serviceid
	 * @return
	 */
	void checkServiceIdMatches(String serviceid);

	/**
	 * 日期格式,超时校验
	 * 
	 * @param serviceid
	 * @return
	 */
	void checkDatetimeMatches(String datetime);

	/**
	 * 签名格式校验
	 * 
	 * @param serviceid
	 * @return
	 */
	void checkSignMatches(String sign);

	/**
	 * 订单号格式校验
	 * 
	 * @param serviceid
	 * @return
	 */
	void checkOrderIdMatches(String orderid);

	/**
	 * 短信验证码格式校验
	 * 
	 * @param smscode
	 * @return
	 */
	void checkSmscodeMatches(String smscode);

	/**
	 * 号码和运营商匹配性校验
	 * 
	 * @param merchantId
	 * @return
	 */
	NumSection checkMobileMatchCarrier(String mobile, String channelId);

	/**
	 * 商户激活和存在性校验
	 * 
	 * @param merchantId
	 * @return
	 */
	Merchant checkMerchant(String cpid);

	/**
	 * 签名校验
	 * 
	 * @param merchantId
	 * @return
	 */
	void checkSign(String sign, String md5sign);

	/**
	 * 渠道存在和启用性校验
	 * 
	 * @param channelId
	 * @return
	 */
	CarrierInfo checkCarrier(String channelId);

	/**
	 * 计费编码存在、启用、与商户匹配校验
	 * 
	 * @param serviceid
	 * @param cpid
	 * @param channelId
	 * @return
	 */
	Point checkMerchantChargingCode(String serviceid, String cpid);

	/**
	 * 计费编码存在、启用、与商户匹配、与渠道匹配校验
	 * 
	 * @param serviceid
	 * @param cpid
	 * @param channelId
	 * @return
	 */
	ChannelChargingCode checkChannelChargingCode(String serviceid, String channelId);

}
