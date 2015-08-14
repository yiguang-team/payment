package com.yiguang.payment.payment.rest.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yiguang.payment.business.product.entity.Point;
import com.yiguang.payment.business.product.entity.Product;
import com.yiguang.payment.business.product.service.PointService;
import com.yiguang.payment.business.product.service.ProductService;
import com.yiguang.payment.common.CommonConstant;
import com.yiguang.payment.common.JsonTool;
import com.yiguang.payment.common.RestConst;
import com.yiguang.payment.common.errorcode.service.ErrorCodeService;
import com.yiguang.payment.common.numsection.entity.NumSection;
import com.yiguang.payment.payment.entity.ChannelChargingCode;
import com.yiguang.payment.payment.order.entity.MerchantOrder;
import com.yiguang.payment.payment.order.service.MerchantOrderService;
import com.yiguang.payment.payment.order.service.ParameterValidateService;
import com.yiguang.payment.payment.rest.service.DeliveryService;
import com.yiguang.payment.payment.rest.service.NotifyService;
import com.yiguang.payment.payment.risk.service.RiskService;

@Controller
@RequestMapping(value = "/notify")
public class NotifyController {
	private static Logger logger = LoggerFactory.getLogger(NotifyController.class);
	@Autowired
	private DeliveryService deliveryService;
	@Autowired
	private NotifyService notifyService;
	@Autowired
	private MerchantOrderService merchantOrderService;
	@Autowired
	private ParameterValidateService parameterValidateService;
	@Autowired
	private ErrorCodeService errorCodeService;
	@Autowired
	private PointService pointService;
	@Autowired
	private ProductService productService;
	@Autowired
	private RiskService riskService;

	/**
	 * 电信翼支付短验通知接口
	 */
	@SuppressWarnings({ "rawtypes" })
	@RequestMapping(value = "/telecom/smsCallBack", method = RequestMethod.POST)
	public @ResponseBody String telecom_smsCallBack(Model model, ServletRequest request) {

		Map map = request.getParameterMap();
		String result = "";
		try {
			@SuppressWarnings("unchecked")
			Set<Entry> set = map.entrySet();

			logger.info(
					"TELECOM SMS NOTIFY START----------------------------------------------------------------------");
			for (Entry e : set) {
				logger.info("TELECOM SMS NOTIFY[" + String.valueOf(e.getKey()) + "]:[" + ((String[]) e.getValue())[0]
						+ "]");
			}

			String resultcode = ((String[]) map.get("RETNCODE"))[0];
			String resultmessage = ((String[]) map.get("RETNINFO"))[0];
			String orderid = ((String[]) map.get("ORDERSEQ"))[0];
			String merchantId = RestConst.CT_CPID;
			String timestamp = ((String[]) map.get("TRANDATE"))[0];
			String amount = ((String[]) map.get("ORDERAMOUNT"))[0];

			String UPTRANSEQ = ((String[]) map.get("UPTRANSEQ"))[0];
			String BANKACCID = ((String[]) map.get("BANKACCID"))[0];
			String sign = ((String[]) map.get("SIGN"))[0];

			StringBuffer signStr = new StringBuffer("");
			signStr.append("UPTRANSEQ=").append(UPTRANSEQ).append("&MERCHANTID=").append(merchantId);
			signStr.append("&ORDERID=").append(orderid).append("&PAYMENT=").append(amount);
			signStr.append("&RETNCODE=").append(resultcode).append("&RETNINFO=").append(resultmessage);
			signStr.append("&PAYDATE=").append(timestamp).append("&KEY=").append(RestConst.CT_KEY);
			parameterValidateService.checkSign(signStr.toString(), sign);

			MerchantOrder merchantOrder = merchantOrderService.queryMerchantOrderByOrderId(orderid);

			// 电信要求保留字段
			merchantOrder.setChannelTradeNo(UPTRANSEQ);
			merchantOrder.setExt1(timestamp);

			// 防止用户篡改号码，以电信通知回来的号码为订单号码
			NumSection section = parameterValidateService.checkMobileMatches(BANKACCID);
			merchantOrder.setMobile(BANKACCID);
			merchantOrder.setChannelId(section.getCarrierInfo().getId());
			merchantOrder.setProvinceId(section.getProvince().getProvinceId());
			merchantOrder.setCityId(section.getCity().getCityId());

			// 订单完成时间 返回结果 返回消息
			merchantOrder.setCompleteTime(new Date());
			merchantOrder.setReturnCode(resultcode);
			merchantOrder.setReturnMessage(resultmessage);

			if (StringUtils.equals(resultcode, "0000")) {
				merchantOrder.setPayStatus(CommonConstant.PayStatus.SUCCESS);
			} else {
				merchantOrder.setPayStatus(CommonConstant.PayStatus.FAILED);
			}
			merchantOrder = merchantOrderService.updateMerchantOrder(merchantOrder);

			if (merchantOrder.getPayStatus() == CommonConstant.PayStatus.SUCCESS) {
				merchantOrder.setDeliveryStatus(CommonConstant.DeliveryStatus.DELIVERYING);
				merchantOrder = merchantOrderService.updateMerchantOrder(merchantOrder);

				deliveryService.delivery(merchantOrder);
			}
			logger.info(
					"TELECOM SMS NOTIFY END------------------------------------------------------------------------");
			result = "UPTRANSEQ_" + UPTRANSEQ;
		} catch (Exception e) {
			logger.error("telecom callback error!");
			logger.error(e.getLocalizedMessage(), e);
		}

		return result;
	}

	/**
	 * 联通短验通知接口
	 */
	@SuppressWarnings({ "rawtypes" })
	@RequestMapping(value = "/iread/smsCallBack", method = RequestMethod.POST)
	public @ResponseBody String iread_smsCallBack(Model model, ServletRequest request) {

		Map<String, String> result = new HashMap<String, String>();
		try {
			HttpServletRequest httpRequest = (HttpServletRequest) request;
			ServletInputStream is = httpRequest.getInputStream();
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			int i = -1;
			while ((i = is.read()) != -1) {
				baos.write(i);
			}

			String json = baos.toString();
			Map map = JsonTool.jsonToMap(json);

			@SuppressWarnings("unchecked")
			Set<Entry> set = map.entrySet();
			logger.info(
					"UNICOM SMSCHARGE NOTIFY START----------------------------------------------------------------------");
			for (Entry e : set) {
				logger.info("UNICOM SMSCHARGE NOTIFY[" + String.valueOf(e.getKey()) + "]:["
						+ (String.valueOf(e.getValue())) + "]");
			}

			String resultcode = String.valueOf(map.get("resultcode"));
			String resultmsg = String.valueOf(map.get("restltmsg"));
			String orderid = String.valueOf(map.get("orderid"));
			String clientid = String.valueOf(map.get("clientid"));
			String keyversion = String.valueOf(map.get("keyversion"));
			String passcode = String.valueOf(map.get("passcode"));
			String timestamp = String.valueOf(map.get("timestamp"));

			StringBuffer signStr = new StringBuffer("");
			signStr.append(timestamp).append(clientid).append(keyversion).append(RestConst.CU_KEY);
			parameterValidateService.checkSign(signStr.toString(), passcode);

			MerchantOrder merchantOrder = merchantOrderService.queryMerchantOrderByOrderId(orderid);
			merchantOrder.setCompleteTime(new Date());
			merchantOrder.setReturnCode(resultcode);
			merchantOrder.setReturnMessage(resultmsg);

			if (StringUtils.equals(resultcode, "0000")) {
				merchantOrder.setPayStatus(CommonConstant.PayStatus.SUCCESS);
			} else {
				merchantOrder.setPayStatus(CommonConstant.PayStatus.FAILED);
			}
			merchantOrder = merchantOrderService.updateMerchantOrder(merchantOrder);

			// 通知
			merchantOrder = notifyService.notify(merchantOrder);
			merchantOrderService.updateMerchantOrder(merchantOrder);

			if (merchantOrder.getPayStatus() == CommonConstant.PayStatus.SUCCESS) {

				merchantOrder.setDeliveryStatus(CommonConstant.DeliveryStatus.DELIVERYING);
				merchantOrder = merchantOrderService.updateMerchantOrder(merchantOrder);

				deliveryService.delivery(merchantOrder);
			}

			logger.info(
					"UNICOM SMSCHARGE NOTIFY END------------------------------------------------------------------------");

			result.put("code", "0000");
			result.put("innercode", "0000");
			result.put("message", "成功");
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage(), e);
			result.put("code", "9999");
			result.put("innercode", "9999");
			result.put("message", "失败");
		}

		String json = JsonTool.beanToJson(result);
		return json;
	}

	// /**
	// * 联通短代通知接口
	// *
	// * @throws IOException
	// */
	// @RequestMapping(value = "/iread/order", method = RequestMethod.POST)
	// public @ResponseBody void iread_order(Model model, ServletRequest
	// request, ServletResponse response)
	// throws IOException
	// {
	// String result = null;
	// HttpServletResponse httpResponse = (HttpServletResponse) response;
	// try
	// {
	// ServletInputStream is = request.getInputStream();
	// ByteArrayOutputStream baos = new ByteArrayOutputStream();
	// int i = -1;
	// while ((i = is.read()) != -1)
	// {
	// baos.write(i);
	// }
	//
	// String sms = baos.toString();
	// logger.info("UNICOM FEE NOTIFY
	// START----------------------------------------------------------------------");
	// logger.info("UNICOM FEE NOTIFY[sms][" + sms + "]");
	// Document doc = (Document) DocumentHelper.parseText(sms);
	//
	// String orderid = "";
	// String ordertime = "";
	// String cpid = "";
	// String appid = "";
	// String fid = "";
	// String consumeCode = "";
	// String payfee = "";
	// String payType = "";
	// String myid = "";
	// String phonenum = "";
	// String hRet = "";
	// String status = "";
	// String signMsg = "";
	//
	// // 根节点
	// Element root = doc.getRootElement();
	//
	// @SuppressWarnings("rawtypes")
	// Iterator Elements = root.elementIterator();
	// while (Elements.hasNext())
	// {
	// Element el = (Element) Elements.next();
	//
	// if (el.getName().equals("orderid"))
	// {
	// orderid = el.getText();
	// }
	// else if (el.getName().equals("ordertime"))
	// {
	// ordertime = el.getText();
	// }
	// else if (el.getName().equals("cpid"))
	// {
	// cpid = el.getText();
	// }
	// else if (el.getName().equals("appid"))
	// {
	// appid = el.getText();
	// }
	// else if (el.getName().equals("fid"))
	// {
	// fid = el.getText();
	// }
	// else if (el.getName().equals("consumeCode"))
	// {
	// consumeCode = el.getText();
	// }
	// else if (el.getName().equals("payfee"))
	// {
	// payfee = el.getText();
	// }
	// else if (el.getName().equals("payType"))
	// {
	// payType = el.getText();
	// }
	// else if (el.getName().equals("myid"))
	// {
	// myid = el.getText();
	// }
	// else if (el.getName().equals("phonenum"))
	// {
	// phonenum = el.getText();
	// }
	// else if (el.getName().equals("hRet"))
	// {
	// hRet = el.getText();
	// }
	// else if (el.getName().equals("status"))
	// {
	// status = el.getText();
	// }
	// else if (el.getName().equals("signMsg"))
	// {
	// signMsg = el.getText();
	// }
	// }
	//
	// // 校验订单
	// if (StringUtil.isBlank(cpid))
	// {
	// // 短代风控限量
	// cpid = orderid.substring(0, 4);
	//
	// try
	// {
	// long not_limit = CommonConstant.RiskSelectType.NON_LIMIT;
	// riskService.checkBasicRule(not_limit, String.valueOf(not_limit),
	// String.valueOf(not_limit),
	// Integer.parseInt(cpid), not_limit, not_limit, String.valueOf(not_limit),
	// String.valueOf(not_limit), String.valueOf(not_limit), 0);
	// result = "0";
	// }
	// catch (Exception e)
	// {
	// result = "1";
	// }
	// }
	// else
	// {
	// // 真正的短代通知
	// StringBuffer signStr = new StringBuffer("");
	//
	// signStr.append("orderid=").append(orderid).append("&ordertime=");
	// signStr.append(ordertime).append("&cpid=").append(cpid);
	// signStr.append("&appid=").append(appid).append("&fid=");
	// signStr.append(fid).append("&consumeCode=").append(consumeCode).append("&payfee=");
	// signStr.append(payfee).append("&payType=").append(payType).append("&myid=");
	// signStr.append(myid).append("&phonenum=").append(phonenum).append("&hRet=");
	// signStr.append(hRet).append("&status=").append(status).append("&Key=").append(RestConst.CU_KEY);
	//
	// parameterValidateService.checkSign(signStr.toString(), signMsg);
	//
	// // 根据通知结果添加支付订单
	// String channelId = String.valueOf(RestConst.CHANNEL_ID_UNICOM);
	// NumSection section =
	// parameterValidateService.checkMobileMatchCarrier(phonenum, channelId);
	// Point point = pointService.queryPoint(Long.parseLong(appid));
	//
	// // 下订单
	// MerchantOrder merchantOrder = new MerchantOrder();
	// Date date = new Date();
	// String timestamp = RestConst.SDF_14.format(date);
	// Double num = Math.random() * 9000 + 1000;
	// String random = String.valueOf(num.intValue());
	// String payOrderId = appid + "00" + timestamp + random;
	//
	// merchantOrder.setOrderTimestamp(timestamp);
	// merchantOrder.setOrderId(payOrderId);
	// merchantOrder.setMerchantOrderId(orderid);
	// merchantOrder.setProductId(point.getProductId());
	// merchantOrder.setChannelId(2);
	// merchantOrder.setMobile(phonenum);
	// merchantOrder.setUsername("");
	// merchantOrder.setChargingType(point.getChargingType());
	// merchantOrder.setChargingPointId(point.getId());
	// merchantOrder.setRequestIp("");
	// merchantOrder.setCarrierId(section.getCarrierInfo().getId());
	// merchantOrder.setRequestTime(RestConst.SDF_14.parse(ordertime));
	// merchantOrder.setCompleteTime(new Date());
	// merchantOrder.setChannelType(CommonConstant.ChannelType.DUANDAI);
	// merchantOrder.setSubject("");
	// merchantOrder.setDescription("");
	// merchantOrder.setNotifyStatus(CommonConstant.NotifyStatus.NOT_NOTIFY);
	// merchantOrder.setProvinceId(section.getProvince().getProvinceId());
	// merchantOrder.setCityId(section.getCity().getCityId());
	// merchantOrder.setFaceAmount(point.getFaceAmount());
	// merchantOrder.setDeliveryAmount(point.getDeliveryAmount());
	// merchantOrder.setPayAmount(point.getPayAmount());
	// merchantOrder.setMerchantId(point.getMerchantId());
	// merchantOrder.setDeliveryStatus(CommonConstant.DeliveryStatus.NOT_DELIVERY);
	// merchantOrder.setPayTimestamp(ordertime);
	// merchantOrder.setReturnCode(hRet);
	// merchantOrder.setReturnMessage(status);
	// merchantOrder = merchantOrderService.createMerchantOrder(merchantOrder);
	// if ("0".equals(hRet))
	// {
	// merchantOrder.setPayStatus(CommonConstant.PayStatus.SUCCESS);
	// merchantOrder = notifyService.notify(merchantOrder);
	//
	// }
	// else
	// {
	// merchantOrder.setPayStatus(CommonConstant.PayStatus.FAILED);
	// }
	// merchantOrderService.updateMerchantOrder(merchantOrder);
	// result = "0";
	// }
	// logger.info("UNICOM FEE NOTIFY
	// END------------------------------------------------------------------------");
	//
	// }
	// catch (Exception e)
	// {
	// logger.error(e.getLocalizedMessage(), e);
	// result = "1";
	// }
	//
	// httpResponse.setHeader("Content-type", "text/xml");
	// httpResponse.getWriter().write(
	// "<?xml version=\"1.0\" encoding=\"utf-8\"?><checkOrderIdRsp>" + result +
	// "</checkOrderIdRsp>");
	// }

	/**
	 * 联通短代通知接口
	 * 
	 * @throws IOException
	 */
	@RequestMapping(value = "/iread/order", method = RequestMethod.POST)
	public @ResponseBody void iread_order(Model model, ServletRequest request, ServletResponse response)
			throws IOException {
		String result = null;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		try {
			ServletInputStream is = request.getInputStream();
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			int i = -1;
			while ((i = is.read()) != -1) {
				baos.write(i);
			}

			String sms = baos.toString();
			logger.info(
					"UNICOM FEE NOTIFY START----------------------------------------------------------------------");
			logger.info("UNICOM FEE NOTIFY[sms][" + sms + "]");
			Document doc = (Document) DocumentHelper.parseText(sms);

			// 根节点
			Element root = doc.getRootElement();

			if (root.getName().equals("checkOrderIdReq")) {
				try {
					String ItfType = "";
					String Command = "";
					String FeeType = "";
					String CPID = "";
					String ServiceID = "";
					String ChannelID = "";
					String appid = "";
					String MYID = "";
					String TIME = "";
					String orderid = "";
					String Cpcustom = "";
					String Phonenum = "";
					String signMsg = "";

					@SuppressWarnings("rawtypes")
					Iterator Elements = root.elementIterator();
					while (Elements.hasNext()) {
						Element el = (Element) Elements.next();

						if (el.getName().equals("ItfType")) {
							ItfType = el.getText();
						} else if (el.getName().equals("Command")) {
							Command = el.getText();
						} else if (el.getName().equals("FeeType")) {
							FeeType = el.getText();
						} else if (el.getName().equals("CPID")) {
							CPID = el.getText();
						} else if (el.getName().equals("ServiceID")) {
							ServiceID = el.getText();
						} else if (el.getName().equals("ChannelID")) {
							ChannelID = el.getText();
						} else if (el.getName().equals("appid")) {
							appid = el.getText();
						} else if (el.getName().equals("MYID")) {
							MYID = el.getText();
						} else if (el.getName().equals("TIME")) {
							TIME = el.getText();
						} else if (el.getName().equals("orderid")) {
							orderid = el.getText();
						} else if (el.getName().equals("Cpcustom")) {
							Cpcustom = el.getText();
						} else if (el.getName().equals("Phonenum")) {
							Phonenum = el.getText();
						} else if (el.getName().equals("signMsg")) {
							signMsg = el.getText();
						}
					}

					// 短代风控限量
					long longCpcustom = Long.parseLong(Cpcustom);
					longCpcustom = 1000 + longCpcustom;

					long not_limit = CommonConstant.RiskSelectType.NON_LIMIT;
					riskService.checkBasicRule(not_limit, String.valueOf(not_limit), String.valueOf(not_limit),
							Integer.parseInt(String.valueOf(longCpcustom)), not_limit, not_limit,
							String.valueOf(not_limit), String.valueOf(not_limit), String.valueOf(not_limit), 0);
					result = "0";
				} catch (Exception e) {
					logger.error(e.getLocalizedMessage(), e);
					result = "1";
				}

				httpResponse.setHeader("Content-type", "text/xml");
				httpResponse.getWriter().write(
						"<?xml version=\"1.0\" encoding=\"utf-8\"?><checkOrderIdRsp>" + result + "</checkOrderIdRsp>");

			} else if (root.getName().equals("callbackAckReq")) {
				try {
					String orderid = "";
					String ordertime = "";
					String cpid = "";
					String appid = "";
					String fid = "";
					String consumeCode = "";
					String payfee = "";
					String payType = "";
					String myid = "";
					String phonenum = "";
					String hRet = "";
					String status = "";
					String signMsg = "";
					String cpcustom = "";

					@SuppressWarnings("rawtypes")
					Iterator Elements = root.elementIterator();
					while (Elements.hasNext()) {
						Element el = (Element) Elements.next();

						if (el.getName().equals("orderid")) {
							orderid = el.getText();
						} else if (el.getName().equals("ordertime")) {
							ordertime = el.getText();
						} else if (el.getName().equals("cpid")) {
							cpid = el.getText();
						} else if (el.getName().equals("appid")) {
							appid = el.getText();
						} else if (el.getName().equals("fid")) {
							fid = el.getText();
						} else if (el.getName().equals("consumeCode")) {
							consumeCode = el.getText();
						} else if (el.getName().equals("payfee")) {
							payfee = el.getText();
						} else if (el.getName().equals("payType")) {
							payType = el.getText();
						} else if (el.getName().equals("myid")) {
							myid = el.getText();
						} else if (el.getName().equals("phonenum")) {
							phonenum = el.getText();
						} else if (el.getName().equals("cpcustom")) {
							cpcustom = el.getText();
						} else if (el.getName().equals("hRet")) {
							hRet = el.getText();
						} else if (el.getName().equals("status")) {
							status = el.getText();
						} else if (el.getName().equals("signMsg")) {
							signMsg = el.getText();
						}
					}

					// 真正的短代通知
					StringBuffer signStr = new StringBuffer("");

					signStr.append("orderid=").append(orderid).append("&ordertime=");
					signStr.append(ordertime).append("&cpid=").append(cpid);
					signStr.append("&appid=").append(appid).append("&fid=");
					signStr.append(fid).append("&consumeCode=").append(consumeCode).append("&payfee=");
					signStr.append(payfee).append("&payType=").append(payType).append("&myid=");
					signStr.append(myid).append("&phonenum=").append(phonenum).append("&cpcustom=");
					signStr.append(cpcustom).append("&hRet=");
					signStr.append(hRet).append("&status=").append(status).append("&Key=").append(RestConst.CU_KEY);

					parameterValidateService.checkSign(signStr.toString(), signMsg);

					long longCpcustom = Long.parseLong(cpcustom);
					longCpcustom = 1000 + longCpcustom;

					// 根据通知结果添加支付订单
					String channelId = String.valueOf(RestConst.CHANNEL_ID_UNICOM);
					NumSection section = parameterValidateService.checkMobileMatchCarrier(phonenum, channelId);

					ChannelChargingCode ccc = parameterValidateService.checkChannelChargingCode(consumeCode, channelId);

					List<Product> productList = productService.queryProductBySupplierId(longCpcustom);
					Point point = pointService.queryPointByProductIdAndFaceAmt(productList.get(0).getId(),
							ccc.getChargingAmount());

					// 下订单
					MerchantOrder merchantOrder = new MerchantOrder();
					Date date = new Date();
					String timestamp = RestConst.SDF_14.format(date);
					Double num = Math.random() * 9000 + 1000;
					String random = String.valueOf(num.intValue());
					String payOrderId = longCpcustom + "00" + timestamp + random;

					merchantOrder.setOrderTimestamp(timestamp);
					merchantOrder.setOrderId(payOrderId);
					merchantOrder.setMerchantOrderId(orderid);
					merchantOrder.setProductId(point.getProductId());
					merchantOrder.setChannelId(2);
					merchantOrder.setMobile(phonenum);
					merchantOrder.setUsername("");
					merchantOrder.setChargingType(point.getChargingType());
					merchantOrder.setChargingPointId(point.getId());
					merchantOrder.setRequestIp("");
					merchantOrder.setCarrierId(section.getCarrierInfo().getId());
					merchantOrder.setRequestTime(RestConst.SDF_14.parse(ordertime));
					merchantOrder.setCompleteTime(new Date());
					merchantOrder.setChannelType(CommonConstant.ChannelType.DUANDAI);
					merchantOrder.setSubject("");
					merchantOrder.setDescription("");
					merchantOrder.setNotifyStatus(CommonConstant.NotifyStatus.NOT_NOTIFY);
					merchantOrder.setProvinceId(section.getProvince().getProvinceId());
					merchantOrder.setCityId(section.getCity().getCityId());
					merchantOrder.setFaceAmount(point.getFaceAmount());
					merchantOrder.setDeliveryAmount(point.getDeliveryAmount());
					merchantOrder.setPayAmount(point.getPayAmount());
					merchantOrder.setMerchantId(point.getMerchantId());
					merchantOrder.setDeliveryStatus(CommonConstant.DeliveryStatus.NOT_DELIVERY);
					merchantOrder.setPayTimestamp(ordertime);
					merchantOrder.setReturnCode(hRet);
					merchantOrder.setReturnMessage(status);
					merchantOrder = merchantOrderService.createMerchantOrder(merchantOrder);
					if ("0".equals(hRet)) {
						merchantOrder.setPayStatus(CommonConstant.PayStatus.SUCCESS);
						merchantOrder = notifyService.notify(merchantOrder);

					} else {
						merchantOrder.setPayStatus(CommonConstant.PayStatus.FAILED);
					}
					merchantOrderService.updateMerchantOrder(merchantOrder);

					// 通知
					merchantOrder = notifyService.notify(merchantOrder);
					merchantOrderService.updateMerchantOrder(merchantOrder);

					result = "0";
				} catch (Exception e) {
					logger.error(e.getLocalizedMessage(), e);
					result = "1";
				}

				httpResponse.setHeader("Content-type", "text/xml");
				httpResponse.getWriter().write(
						"<?xml version=\"1.0\" encoding=\"utf-8\"?><callbackAckRsp>" + result + "</callbackAckRsp>");
			}

			logger.info(
					"UNICOM FEE NOTIFY END------------------------------------------------------------------------");

		} catch (Exception e) {
			logger.error(e.getLocalizedMessage(), e);
			result = "1";
		}

	}
}
