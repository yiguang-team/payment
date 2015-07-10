package com.yiguang.payment.payment.rest.service.impl;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.rpc.RpcException;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.yiguang.payment.common.CommonConstant;
import com.yiguang.payment.common.RestConst;
import com.yiguang.payment.common.exception.ErrorCodeConst;
import com.yiguang.payment.common.utils.Encrypt;
import com.yiguang.payment.common.utils.HttpRequest;
import com.yiguang.payment.payment.order.entity.MerchantOrder;
import com.yiguang.payment.payment.order.service.MerchantOrderService;
import com.yiguang.payment.payment.rest.entity.RestResult;
import com.yiguang.payment.payment.rest.service.DeliveryService;
import com.yiguang.payment.payment.rest.service.NotifyService;
import com.yiguang.payment.payment.rest.service.SmsAgentPaymentService;
import com.yiguang.payment.payment.rest.service.SmsVerifyPaymentService;

@Service("woPlusPaymentService")
@Transactional
public class WoPlusPaymentServiceImpl implements SmsVerifyPaymentService, SmsAgentPaymentService
{

	private static Logger logger = LoggerFactory.getLogger(WoPlusPaymentServiceImpl.class);

	// 应用APPKEY
	private static final String APPKEY = "9e1fdedff1bf38669f92320cfa0c9dec568b0396";

	// appSecret
	private static final String APPSECRET = "b26c3e58de644cdc96689b1c82add465f4cf67e6";

	private static final Map<String, String> tokenMap = new ConcurrentHashMap<String, String>();
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");

	private String getToken()
	{
		try
		{
			String datetime = tokenMap.get("datetime");
			String token = null;
			boolean getNewToken = false;
			Calendar calendarNow = Calendar.getInstance();
			if (null != datetime)
			{
				Calendar calendarToken = Calendar.getInstance();
				calendarToken.setTime(dateFormat.parse(datetime));
				long s = calendarNow.getTimeInMillis() - calendarToken.getTimeInMillis();
				if (s >= 1000 * 3600 * 24 * 20)
				{
					getNewToken = true;
				}
			}
			else
			{
				getNewToken = true;

			}

			if (getNewToken)
			{
				logger.info("getNewToken start!");
				datetime = dateFormat.format(calendarNow.getTime());
				tokenMap.put("datetime",datetime);
				
				String s = HttpRequest.sendGet("http://open.wo.com.cn/openapi/authenticate/v1.0",
						"appKey=9e1fdedff1bf38669f92320cfa0c9dec568b0396&appSecret=b26c3e58de644cdc96689b1c82add465f4cf67e6");

				JSONObject js = this.parseSmsBody(s);

				token = js.getString("token");
				tokenMap.put("token",token);
				logger.info("getNewToken end!");
			}
			else
			{
				token = tokenMap.get("token");
			}
			
			return token ;
		}
		catch (Exception e)
		{
			logger.error("get token error!");
			throw new RpcException(ErrorCodeConst.ErrorCode99999);
		}
	}

	
	@Autowired
	private DeliveryService deliveryService;
	@Autowired
	private NotifyService notifyService;
	
	@Autowired
	private MerchantOrderService merchantOrderService;

	@Override
	public RestResult sendsmscode(MerchantOrder mo)
	{

		String orderid = mo.getOrderId();
		long cpid = mo.getMerchantId();
		long serviceid = mo.getChargingPointId();
		String datetime = mo.getOrderTimestamp();
		String mobile = mo.getMobile();
		long operator = mo.getCarrierId();
		BigDecimal totalAmount = mo.getPayAmount();
		String subject = mo.getSubject();
		String description = mo.getDescription();

		RestResult restResult = null;
		logger.info("WoPlus SENDSMSCODE START----------------------------------------------------------------------");
		logger.info("WoPlus SENDSMSCODE [orderid][" + orderid + "]");
		logger.info("WoPlus SENDSMSCODE [cpid][" + cpid + "]");
		logger.info("WoPlus SENDSMSCODE [serviceid][" + serviceid + "]");
		logger.info("WoPlus SENDSMSCODE [datetime][" + datetime + "]");
		logger.info("WoPlus SENDSMSCODE [mobile][" + mobile + "]");
		logger.info("WoPlus SENDSMSCODE [operator][" + operator + "]");
		logger.info("WoPlus SENDSMSCODE [subject][" + subject + "]");
		logger.info("WoPlus SENDSMSCODE [description][" + description + "]");

		try
		{
			String POST_URL = "http://open.wo.com.cn/openapi/rpc/paymentcodesms/v2.0";
			URL postUrl = new URL(POST_URL);

			HttpURLConnection connection = (HttpURLConnection) postUrl.openConnection();

			// 打开读写属性，默认均为false
			connection.setDoOutput(true);
			connection.setDoInput(true);

			// 设置请求方式，默认为GET
			connection.setRequestMethod("POST");

			// Post 请求不能使用缓存
			connection.setUseCaches(false);

			connection.setInstanceFollowRedirects(true);

			// 时间建议长点
			connection.setConnectTimeout(60000);
			connection.setReadTimeout(60000);

			// 配置连接的Content-type，配置为application/json;charset=UTF-8
			connection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");

			connection.setRequestProperty("Accept", "application/json");

			String head = "appKey=" + "\"" + APPKEY + "\"" + ", token=" + "\"" + getToken() + "\"" + "";

			connection.setRequestProperty("Authorization", head);

			DataOutputStream out = new DataOutputStream(connection.getOutputStream());
			// 正文内容其实跟get的URL中'?'后的参数字符串一致
			HashMap<String, Object> cnts = new HashMap<String, Object>();

			// 联通手机号
			cnts.put("paymentUser", mobile);

			// 操作类型：0 按次扣费 1 周期性订购
			cnts.put("paymentType", 0);

			// 外部订单号
			cnts.put("outTradeNo", orderid);

			// 支付账户类型
			cnts.put("paymentAcount", "001");

			// 商品名称
			cnts.put("subject", subject);

			// 商品描述
			cnts.put("description", "001");

			// 交易金额
			cnts.put("totalFee", totalAmount.floatValue() / 100);

			String content = JSON.toJSONString(cnts);

			// DataOutputStream.writeBytes将字符串中的16位的 unicode字符以8位的字符形式写道流里面
			out.write(content.getBytes());
			out.flush();
			out.close();
			int responseCode = connection.getResponseCode();
			System.out.println(responseCode);
			String resultData = null;
			if (200 == responseCode || 201 == responseCode)
			{
				InputStream in = connection.getInputStream();
				resultData = getResponseResult(new InputStreamReader(in, "UTF-8"));
				JSONObject jo = parseSmsBody(resultData);
				connection.disconnect();

				String resultCode = String.valueOf(jo.get("resultCode"));
				String resultDescription = String.valueOf(jo.get("resultDescription"));
				if ("0".equals(resultCode))
				{
					resultCode = "0000";
				}

				restResult = new RestResult(resultCode, resultDescription);
			}
			else if (0 == connection.getContentLength())
			{
				resultData = "get response error,content is empty";
				connection.disconnect();
				restResult = new RestResult(ErrorCodeConst.ErrorCode99999, resultData);
			}
			else
			{
				BufferedInputStream err = new BufferedInputStream(connection.getErrorStream());
				resultData = getResponseResult(new InputStreamReader(err));
				connection.disconnect();

				restResult = new RestResult(ErrorCodeConst.ErrorCode99999, resultData);
			}
		}
		catch (Exception e)
		{
			throw new RpcException(ErrorCodeConst.ErrorCode99999);
		}
		return restResult;
	}

	@Override
	public RestResult smscharge(MerchantOrder mo)
	{

		String cpid = String.valueOf(mo.getMerchantId());
		String serviceid = String.valueOf(mo.getChargingPointId());
		String datetime = mo.getOrderTimestamp();
		String mobile = mo.getMobile();
		String orderid = mo.getOrderId();
		String smscode = mo.getSmscode();
		String username = mo.getUsername();
		String operator = String.valueOf(mo.getCarrierId());
		BigDecimal totalAmount = mo.getPayAmount();
		String subject = mo.getSubject();
		String description = mo.getDescription();
		RestResult restResult = null;
		logger.info("WoPlus SMSCHARGE START----------------------------------------------------------------------");
		logger.info("WoPlus SMSCHARGE [cpid][" + cpid + "]");
		logger.info("WoPlus SMSCHARGE [serviceid][" + serviceid + "]");
		logger.info("WoPlus SMSCHARGE [datetime][" + datetime + "]");
		logger.info("WoPlus SMSCHARGE [mobile][" + mobile + "]");
		logger.info("WoPlus SMSCHARGE [orderid][" + orderid + "]");
		logger.info("WoPlus SMSCHARGE [smscode][" + smscode + "]");
		logger.info("WoPlus SMSCHARGE [username][" + username + "]");
		logger.info("WoPlus SMSCHARGE [operator][" + operator + "]");
		logger.info("WoPlus SMSCHARGE [subject][" + subject + "]");
		logger.info("WoPlus SMSCHARGE [description][" + description + "]");
		try
		{
			// 再提交支付请求
			String POST_URL = "http://open.wo.com.cn/openapi/rpc/apppayment/v2.0";
			// Post请求的url，与get不同的是不需要带参数
			URL postUrl = new URL(POST_URL);

			HttpURLConnection connection = (HttpURLConnection) postUrl.openConnection();

			// 打开读写属性，默认均为false
			connection.setDoOutput(true);
			connection.setDoInput(true);

			// 设置请求方式，默认为GET
			connection.setRequestMethod("POST");

			// Post 请求不能使用缓存
			connection.setUseCaches(false);

			connection.setInstanceFollowRedirects(true);

			// 时间建议长点
			connection.setConnectTimeout(60000);
			connection.setReadTimeout(60000);

			// 配置连接的Content-type，配置为application/json;charset=UTF-8
			connection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
			connection.setRequestProperty("Accept", "application/json");
			String head = "appKey=" + "\"" + APPKEY + "\"" + ", token=" + "\"" + getToken() + "\"" + "";

			// 设置Authorization
			connection.setRequestProperty("Authorization", head);

			DataOutputStream out = new DataOutputStream(connection.getOutputStream());

			// 正文内容其实跟get的URL中'?'后的参数字符串一致
			HashMap<String, Object> cnts = new HashMap<String, Object>();

			// 联通手机号
			cnts.put("paymentUser", mobile);

			// 支付账户类型
			cnts.put("paymentAcount", "001");

			// 外部订单号
			cnts.put("outTradeNo", orderid);

			// 商品名称
			cnts.put("subject", subject);

			// 描述
			cnts.put("description", description);

			// 交易金额
			cnts.put("totalFee", totalAmount.floatValue() / 100);

			// 短信验证码
			cnts.put("paymentcodesms", Long.parseLong(smscode));

			// 时间戳
			cnts.put("timeStamp", dateFormat.format(System.currentTimeMillis()));
			String secretKey = APPKEY + "&" + APPSECRET;
			String signature = Encrypt.encryptHmacSha1(cnts, secretKey);
			cnts.put("signType", "HMAC-SHA1");
			cnts.put("signature", signature);
			String content = JSON.toJSONString(cnts);

			// DataOutputStream.writeBytes将字符串中的16位的 unicode字符以8位的字符形式写道流里面
			out.write(content.getBytes());
			out.flush();
			out.close();
			int responseCode = connection.getResponseCode();

			String resultData = null;
			if (200 == responseCode || 201 == responseCode)
			{
				InputStream in = connection.getInputStream();
				resultData = getResponseResult(new InputStreamReader(in, "UTF-8"));
				JSONObject jo = parseSmsBody(resultData);
				connection.disconnect();

				String resultCode = String.valueOf(jo.get("resultCode"));
				String resultDescription = String.valueOf(jo.get("resultDescription"));
				String transactionId = String.valueOf(jo.get("transactionId"));

				if ("0".equals(resultCode))
				{
					resultCode = "0000";
				}

				restResult = new RestResult(resultCode, resultDescription);

				mo.setCompleteTime(new Date());
				mo.setReturnCode(resultCode);
				mo.setReturnMessage(resultDescription);
				mo.setChannelTradeNo(transactionId);
				if (StringUtils.equals(resultCode, "0000"))
				{
					mo.setPayStatus(CommonConstant.PayStatus.SUCCESS);
					mo = merchantOrderService.updateMerchantOrder(mo);
					
					if (RestConst.YIGUANG_MERCHANT_ID.equals(String.valueOf(mo.getMerchantId())))
					{
						mo.setDeliveryStatus(CommonConstant.DeliveryStatus.DELIVERYING);
						mo = merchantOrderService.updateMerchantOrder(mo);

						deliveryService.delivery(mo);
					}
					else
					{
						mo = notifyService.notify(mo);
						merchantOrderService.updateMerchantOrder(mo);
					}
				}
				else
				{
					mo.setPayStatus(CommonConstant.PayStatus.FAILED);
					mo = merchantOrderService.updateMerchantOrder(mo);
				}

			}
			else if (0 == connection.getContentLength())
			{
				resultData = "get response error,content is empty";
				connection.disconnect();
				
				mo.setPayStatus(CommonConstant.PayStatus.FAILED);
				mo = merchantOrderService.updateMerchantOrder(mo);
				
				restResult = new RestResult(ErrorCodeConst.ErrorCode99999, resultData);
			}
			else
			{
				BufferedInputStream err = new BufferedInputStream(connection.getErrorStream());
				resultData = getResponseResult(new InputStreamReader(err));
				connection.disconnect();
				
				mo.setPayStatus(CommonConstant.PayStatus.FAILED);
				mo = merchantOrderService.updateMerchantOrder(mo);
				
				restResult = new RestResult(ErrorCodeConst.ErrorCode99999, resultData);
			}
			
		}
		catch (Exception e)
		{
			throw new RpcException(ErrorCodeConst.ErrorCode99999);
		}
		return restResult;
	}

	@Override
	public String fee(String mcpid, String mserviceid, String datetime, String sign)
	{
		// 渠道不支持短代。
		return null;
	}

	private String getResponseResult(InputStreamReader inputReader)
	{
		String dataTemp = "";
		BufferedReader buffer = null;
		try
		{
			buffer = new BufferedReader(inputReader);
			String inputLine = null;
			dataTemp = "";
			while ((inputLine = buffer.readLine()) != null)
			{
				dataTemp += inputLine + "";
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			if (null != buffer)
			{
				try
				{
					buffer.close();
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}

		}

		return dataTemp;
	}

	private JSONObject parseSmsBody(String resultData)
	{
		resultData = resultData.replace("\t", "");
		byte[] data = resultData.toString().getBytes();
		JSONObject jso = null;
		try
		{
			jso = getJsonModelFromByte(data);

		}
		catch (JSONException e)
		{
			e.printStackTrace();
		}
		return jso;
	}

	private JSONObject getJsonModelFromByte(byte[] data)
	{
		if (null == data)
		{
			return null;
		}

		JSONObject jsonSrcObj = null;

		try
		{
			Feature[] fArray = new Feature[] {};
			jsonSrcObj = (JSONObject) JSON.parse(data, fArray);
		}
		catch (Exception e)
		{
			return null;
		}

		return jsonSrcObj;
	}
}
