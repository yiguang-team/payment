package com.yiguang.payment.payment.rest.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.rpc.RpcException;
import com.yiguang.payment.business.product.entity.Point;
import com.yiguang.payment.common.CommonConstant;
import com.yiguang.payment.common.IpTool;
import com.yiguang.payment.common.JsonTool;
import com.yiguang.payment.common.RestConst;
import com.yiguang.payment.common.exception.ErrorCodeConst;
import com.yiguang.payment.common.message.MessageResolver;
import com.yiguang.payment.common.numsection.entity.NumSection;
import com.yiguang.payment.common.utils.SpringUtils;
import com.yiguang.payment.common.utils.StringUtil;
import com.yiguang.payment.payment.entity.Merchant;
import com.yiguang.payment.payment.order.entity.MerchantOrder;
import com.yiguang.payment.payment.order.service.MerchantOrderService;
import com.yiguang.payment.payment.order.service.ParameterValidateService;
import com.yiguang.payment.payment.rest.entity.RestResult;
import com.yiguang.payment.payment.rest.service.CheckRiskService;
import com.yiguang.payment.payment.rest.service.SmsAgentPaymentService;
import com.yiguang.payment.payment.rest.service.SmsVerifyPaymentService;

@Controller
@RequestMapping(value = "/payment/api/1.0/")
public class PaymentAPIControllerV1 {
	private static final long CHANNEL_WO_PLUS = 1;
	private static final long CHANNEL_WO_IREAD = 2;
	private static final long CHANNEL_YI_BEST_PAY = 3;

	@Autowired
	private CheckRiskService checkRiskService;
	@Autowired
	private ParameterValidateService parameterValidateService;
	@Autowired
	private MerchantOrderService merchantOrderService;
	@PersistenceContext
	private EntityManager em;
	private static Logger logger = LoggerFactory.getLogger(PaymentAPIControllerV1.class);

	private static Map<String, String> payingMap = new ConcurrentHashMap<String, String>();

	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

	/**
	 * 联通发送短信接口
	 */
	@RequestMapping(value = "/sendsmscode", method = RequestMethod.POST)
	public @ResponseBody String sendsmscode(@RequestParam(value = "cpid", defaultValue = "") String cpid,
			@RequestParam(value = "orderid", defaultValue = "") String orderid,
			@RequestParam(value = "serviceid", defaultValue = "") String serviceid,
			@RequestParam(value = "datetime", defaultValue = "") String datetime,
			@RequestParam(value = "sign", defaultValue = "") String sign,
			@RequestParam(value = "mobile", defaultValue = "") String mobile,
			@RequestParam(value = "operator", defaultValue = "") String operator,
			@RequestParam(value = "subject", defaultValue = "") String subject,
			@RequestParam(value = "description", defaultValue = "") String description,
			@RequestParam(value = "username", defaultValue = "") String username, Model model, ServletRequest request) {
		RestResult restResult = null;
		try {
			HttpServletRequest httpRequest = (HttpServletRequest) request;
			String requestIp = IpTool.getIpAddr(httpRequest);

			parameterValidateService.checkOrderIdMatches(orderid);
			parameterValidateService.checkMobileMatches(mobile);
			parameterValidateService.checkCpidMatches(cpid);
			parameterValidateService.checkOperatorIdMatches(operator);
			parameterValidateService.checkServiceIdMatches(serviceid);
			parameterValidateService.checkDatetimeMatches(datetime);
			parameterValidateService.checkSignMatches(sign);
			parameterValidateService.checkMobileMatchCarrier(mobile, operator);

			Merchant merchant = parameterValidateService.checkMerchant(cpid);

			String signNoMd5 = orderid + cpid + serviceid + mobile + operator + datetime + merchant.getKey();
			parameterValidateService.checkSign(signNoMd5, sign);
			parameterValidateService.checkCarrier(operator);
			Point point = parameterValidateService.checkMerchantChargingCode(serviceid, cpid);

			NumSection section = parameterValidateService.checkMobileMatchCarrier(mobile, operator);

			Date date = new Date();
			String timestamp = RestConst.SDF_14.format(date);

			MerchantOrder merchantOrder = merchantOrderService.queryMerchantOrderByMerchantOrderId(orderid,
					Long.parseLong(cpid));

			if (merchantOrder == null) {
				// 下订单
				merchantOrder = new MerchantOrder();

				Double num = Math.random() * 9000 + 1000;
				String random = String.valueOf(num.intValue());
				String selfOrderId = cpid + "00" + timestamp + random;
				merchantOrder.setOrderTimestamp(datetime);
				merchantOrder.setOrderId(selfOrderId);
				merchantOrder.setMerchantOrderId(orderid);
				merchantOrder.setProductId(point.getProductId());

				merchantOrder.setMobile(mobile);
				merchantOrder.setUsername(username);
				merchantOrder.setChargingType(point.getChargingType());
				merchantOrder.setChargingPointId(point.getId());
				merchantOrder.setChargingCode(serviceid);
				merchantOrder.setRequestIp(requestIp);
				merchantOrder.setCarrierId(section.getCarrierInfo().getId());
				merchantOrder.setRequestTime(new Date());
				merchantOrder.setChannelType(CommonConstant.ChannelType.DUANYAN);
				merchantOrder.setSubject(subject);
				merchantOrder.setDescription(description);
				merchantOrder.setNotifyStatus(CommonConstant.NotifyStatus.NOT_NOTIFY);
				merchantOrder.setProvinceId(section.getProvince().getProvinceId());
				merchantOrder.setCityId(section.getCity().getCityId());

				merchantOrder.setFaceAmount(point.getFaceAmount());
				merchantOrder.setDeliveryAmount(point.getDeliveryAmount());
				merchantOrder.setPayAmount(point.getPayAmount());
				merchantOrder.setMerchantId(point.getMerchantId());
				merchantOrder.setPayStatus(CommonConstant.PayStatus.NOT_PAY);
				merchantOrder.setDeliveryStatus(CommonConstant.DeliveryStatus.NOT_DELIVERY);

				// 校验风控 并返回通过风控的第一个渠道

				merchantOrder = checkRiskService.checkRiskAndChannel(merchantOrder);
				if (!merchantOrder.getProvinceId().equals("HUN") && !merchantOrder.getProvinceId().equals("GD_")
						&& !merchantOrder.getProvinceId().equals("GX_")) {
					if (merchantOrder.getChargingType() == CommonConstant.CHARGING_TYPE.DERICT
							&& merchantOrder.getMerchantId() == 1000) {
						String account = merchantOrder.getUsername();
						Date requestTime = merchantOrder.getRequestTime();
						String time = DATE_FORMAT.format(requestTime);
						String pageTotal_sql = "select count(1) from (select mobile from t_merchant_order t where t.username = '"
								+ account + "' and t.request_time >= to_date('" + time
								+ "','yyyy-mm-dd') and t.pay_status = 0 and t.province_id = '"
								+ merchantOrder.getProvinceId() + "' group by t.mobile)";
						Query query = em.createNativeQuery(pageTotal_sql);
						BigDecimal total = (BigDecimal) query.getSingleResult();
						if (total.intValue() >= 5) {
							logger.error("sendsmscode failed");
							throw new RpcException(ErrorCodeConst.ErrorCode11005);
						}
					}
				}

				// 保存订单
				merchantOrder = merchantOrderService.createMerchantOrder(merchantOrder);
			}

			SmsVerifyPaymentService smsVerifyPaymentService = getService(merchantOrder.getChannelId());

			restResult = smsVerifyPaymentService.sendsmscode(merchantOrder);
		} catch (RpcException e) {
			restResult = new RestResult(e.getCode(), e.getMessage());
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage(), e);
			RpcException ae = new RpcException(ErrorCodeConst.ErrorCode99999);
			restResult = new RestResult(ae.getCode(), ae.getMessage());
		}

		if (restResult.getMessage() == null) {
			String message = MessageResolver.getMessage(restResult.getCode());
			restResult.setMessage(message);
		}

		String json = JsonTool.beanToJson(restResult);

		return json;
	}

	/**
	 * 联通计费接口
	 */
	@RequestMapping(value = "/smscharge", method = RequestMethod.POST)
	public @ResponseBody String smscharge(@RequestParam(value = "cpid", defaultValue = "") String cpid,
			@RequestParam(value = "serviceid", defaultValue = "") String serviceid,
			@RequestParam(value = "datetime", defaultValue = "") String datetime,
			@RequestParam(value = "sign", defaultValue = "") String sign,
			@RequestParam(value = "mobile", defaultValue = "") String mobile,
			@RequestParam(value = "orderid", defaultValue = "") String orderid,
			@RequestParam(value = "smscode", defaultValue = "") String smscode,
			@RequestParam(value = "username", defaultValue = "") String username,
			@RequestParam(value = "operator", defaultValue = "") String operator,
			@RequestParam(value = "subject", defaultValue = "") String subject,
			@RequestParam(value = "description", defaultValue = "") String description,
			@RequestParam(value = "showUrl", defaultValue = "") String showUrl,
			@RequestParam(value = "notifyUrl", defaultValue = "") String notifyUrl) {
		RestResult restResult = null;
		try {
			parameterValidateService.checkMobileMatches(mobile);
			parameterValidateService.checkCpidMatches(cpid);
			parameterValidateService.checkOperatorIdMatches(operator);
			parameterValidateService.checkServiceIdMatches(serviceid);
			parameterValidateService.checkDatetimeMatches(datetime);
			parameterValidateService.checkSignMatches(sign);
			parameterValidateService.checkOrderIdMatches(orderid);
			parameterValidateService.checkSmscodeMatches(smscode);

			String isPaying = payingMap.get(cpid + orderid);

			if (StringUtil.isNotBlank(isPaying)) {
				// 订单已经支付 重复提交
				logger.error("merchantOrder is payed! orderid[" + orderid + "] cpid [" + cpid + "]");
				throw new RpcException(ErrorCodeConst.ErrorCode90021);
			} else {
				payingMap.put(cpid + orderid, cpid + orderid);

				parameterValidateService.checkMobileMatchCarrier(mobile, operator);

				Merchant merchant = parameterValidateService.checkMerchant(cpid);

				String signNoMd5 = orderid + cpid + serviceid + mobile + operator + datetime + merchant.getKey();
				parameterValidateService.checkSign(signNoMd5, sign);
				parameterValidateService.checkCarrier(operator);
				Point point = parameterValidateService.checkMerchantChargingCode(serviceid, cpid);

				MerchantOrder merchantOrder = merchantOrderService.queryMerchantOrderByMerchantOrderId(orderid,
						Long.parseLong(cpid));

				if (merchantOrder == null) {
					logger.error("merchantOrder not exsit! orderid[" + orderid + "] cpid [" + cpid + "]");
					throw new RpcException(ErrorCodeConst.ErrorCode90021);
				} else if (merchantOrder.getPayStatus() == CommonConstant.PayStatus.SUCCESS) {
					// 订单已经支付 重复提交
					logger.error("merchantOrder is payed! orderid[" + orderid + "] cpid [" + cpid + "]");
					throw new RpcException(ErrorCodeConst.ErrorCode90021);
				} else if (merchantOrder.getPayStatus() != CommonConstant.PayStatus.NOT_PAY) {
					// 订单已经支付 重复提交
					logger.error("merchantOrder is Timeout! orderid[" + orderid + "] cpid [" + cpid + "]");
					throw new RpcException(ErrorCodeConst.ErrorCode90021);
				}

				if (point.getId() != merchantOrder.getChargingPointId()) {
					throw new RpcException(ErrorCodeConst.ErrorCode90021);
				}

				if (!StringUtil.equals(merchantOrder.getMobile(), mobile)) {
					throw new RpcException(ErrorCodeConst.ErrorCode90021);
				}

				merchantOrder.setUsername(username);
				merchantOrder.setPayTimestamp(datetime);
				merchantOrder.setSmscode(smscode);
				merchantOrder.setShowUrl(showUrl);
				merchantOrder.setNotifyUrl(notifyUrl);
				merchantOrder.setPayStatus(CommonConstant.PayStatus.PAYING);

				SmsVerifyPaymentService smsVerifyPaymentService = getService(merchantOrder.getChannelId());

				merchantOrder = merchantOrderService.updateMerchantOrder(merchantOrder);
				payingMap.put(cpid + orderid, "");
				restResult = smsVerifyPaymentService.smscharge(merchantOrder);
			}
		} catch (RpcException e) {
			restResult = new RestResult(e.getCode(), e.getMessage());
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage(), e);
			restResult = new RestResult(ErrorCodeConst.ErrorCode99999, "");
		}

		if (restResult.getMessage() == null) {
			String message = MessageResolver.getMessage(restResult.getCode());
			restResult.setMessage(message);
		}

		String json = JsonTool.beanToJson(restResult);
		return json;
	}

	/**
	 * 联通短代获取短信接口
	 * 
	 * @throws IOException
	 */
	@RequestMapping(value = "/fee")
	public @ResponseBody void fee(@RequestParam(value = "cpid", defaultValue = "") String cpid,
			@RequestParam(value = "serviceid", defaultValue = "") String serviceid,
			@RequestParam(value = "datetime", defaultValue = "") String datetime,
			@RequestParam(value = "sign", defaultValue = "") String sign, ServletResponse response) throws IOException {
		String sms = null;
		String error = "0";
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		try {
			parameterValidateService.checkCpidMatches(cpid);
			parameterValidateService.checkServiceIdMatches(serviceid);
			parameterValidateService.checkDatetimeMatches(datetime);
			parameterValidateService.checkSignMatches(sign);

			Merchant merchant = parameterValidateService.checkMerchant(cpid);

			String signNoMd5 = cpid + serviceid + datetime + merchant.getKey();
			parameterValidateService.checkSign(signNoMd5, sign);
			Point point = parameterValidateService.checkMerchantChargingCode(serviceid, cpid);
			// 下订单
			MerchantOrder merchantOrder = new MerchantOrder();

			merchantOrder.setOrderTimestamp(datetime);
			merchantOrder.setProductId(point.getProductId());
			merchantOrder.setChargingType(point.getChargingType());
			merchantOrder.setChargingPointId(point.getId());
			merchantOrder.setChargingCode(serviceid);
			merchantOrder.setRequestTime(new Date());
			merchantOrder.setChannelType(CommonConstant.ChannelType.DUANDAI);
			merchantOrder.setNotifyStatus(CommonConstant.NotifyStatus.NOT_NOTIFY);
			merchantOrder.setFaceAmount(point.getFaceAmount());
			merchantOrder.setDeliveryAmount(point.getDeliveryAmount());
			merchantOrder.setPayAmount(point.getPayAmount());
			merchantOrder.setMerchantId(point.getMerchantId());
			merchantOrder.setPayStatus(CommonConstant.PayStatus.NOT_PAY);
			merchantOrder.setDeliveryStatus(CommonConstant.DeliveryStatus.NOT_DELIVERY);

			// 校验风控 并返回通过风控的第一个渠道
			checkRiskService.checkRisk(merchantOrder, CHANNEL_WO_IREAD);
			SmsAgentPaymentService woIreadPaymentService = (SmsAgentPaymentService) SpringUtils
					.getBean("woIreadPaymentService");
			sms = woIreadPaymentService.fee(cpid, serviceid, datetime, sign);

		} catch (RpcException e) {
			error = String.valueOf(e.getCode());
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage(), e);
			error = String.valueOf(ErrorCodeConst.ErrorCode99999);
		}

		httpResponse.setHeader("Content-type", "text/xml");
		httpResponse.getWriter().write("<?xml version=\"1.0\" encoding=\"utf-8\"?><response><error>" + error
				+ "</error>" + sms + "</response>");
	}

	private SmsVerifyPaymentService getService(long channelId) {

		String serviceName = null;
		if (channelId == CHANNEL_WO_PLUS) {
			serviceName = "woPlusPaymentService";
		} else if (channelId == CHANNEL_WO_IREAD) {
			serviceName = "woIreadPaymentService";
		} else if (channelId == CHANNEL_YI_BEST_PAY) {
			serviceName = "yiBestPaymentService";
		} else {
			serviceName = "";
		}

		Object service = SpringUtils.getBean(serviceName);
		SmsVerifyPaymentService smsVerifyPaymentService = null;
		if (null == service) {
			RpcException ae = new RpcException(ErrorCodeConst.ErrorCode99999);
			throw ae;
		} else {
			if (!(service instanceof SmsVerifyPaymentService)) {
				RpcException ae = new RpcException(ErrorCodeConst.ErrorCode99999);
				throw ae;
			} else {
				smsVerifyPaymentService = (SmsVerifyPaymentService) service;
			}
		}

		return smsVerifyPaymentService;
	}
}
