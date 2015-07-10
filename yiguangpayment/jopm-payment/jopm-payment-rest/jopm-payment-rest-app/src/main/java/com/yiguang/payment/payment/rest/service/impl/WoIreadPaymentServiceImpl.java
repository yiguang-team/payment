package com.yiguang.payment.payment.rest.service.impl;

import java.util.Date;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.rpc.RpcException;
import com.alibaba.fastjson.JSONObject;
import com.yiguang.payment.business.product.entity.Point;
import com.yiguang.payment.common.RestConst;
import com.yiguang.payment.common.exception.ErrorCodeConst;
import com.yiguang.payment.common.security.MD5Util;
import com.yiguang.payment.payment.entity.ChannelChargingCode;
import com.yiguang.payment.payment.entity.Merchant;
import com.yiguang.payment.payment.order.entity.MerchantOrder;
import com.yiguang.payment.payment.order.service.ParameterValidateService;
import com.yiguang.payment.payment.rest.entity.RestResult;
import com.yiguang.payment.payment.rest.service.SmsAgentPaymentService;
import com.yiguang.payment.payment.rest.service.SmsVerifyPaymentService;

@Service("woIreadPaymentService")
@Transactional
public class WoIreadPaymentServiceImpl implements SmsVerifyPaymentService,SmsAgentPaymentService
{

	private static Logger logger = LoggerFactory.getLogger(WoIreadPaymentServiceImpl.class);

	@Autowired
	private ParameterValidateService parameterValidateService;

	@Override
	public RestResult sendsmscode(MerchantOrder mo)
	{

		String orderid = mo.getOrderId();
		String cpid = String.valueOf(mo.getMerchantId());
		String serviceid = mo.getChargingCode();
		String datetime = mo.getOrderTimestamp();
		String mobile = mo.getMobile();
		String operator = String.valueOf(mo.getCarrierId());

		String subject = mo.getSubject();
		String description = mo.getDescription();
		String channelId = String.valueOf(mo.getChannelId());
		RestResult restResult = null;
		logger.info("UNICOM SENDSMSCODE START----------------------------------------------------------------------");
		logger.info("UNICOM SENDSMSCODE [orderid][" + orderid + "]");
		logger.info("UNICOM SENDSMSCODE [cpid][" + cpid + "]");
		logger.info("UNICOM SENDSMSCODE [serviceid][" + serviceid + "]");
		logger.info("UNICOM SENDSMSCODE [datetime][" + datetime + "]");
		logger.info("UNICOM SENDSMSCODE [mobile][" + mobile + "]");
		logger.info("UNICOM SENDSMSCODE [operator][" + operator + "]");
		logger.info("UNICOM SENDSMSCODE [subject][" + subject + "]");
		logger.info("UNICOM SENDSMSCODE [description][" + description + "]");

		try
		{
			ChannelChargingCode channelChargingCode = parameterValidateService.checkChannelChargingCode(serviceid,
					channelId);
			String channelCode = channelChargingCode.getChargingCode();
			// 下面是下行短信发送
			String clientid = RestConst.CU_CPID;
			String keyversion = RestConst.KEYVERSION;
			String passcode = MD5Util
					.getMD5Sign(datetime + RestConst.CU_CPID + RestConst.KEYVERSION + RestConst.CU_KEY);
			String usercode = mobile;
			String paycode = channelCode;
			String url = "http://open.iread.wo.com.cn/smscharge/rest/openread/thirdfee/sendsmscode/11/" + datetime
					+ "/" + clientid + "/" + keyversion + "/" + passcode + "/?usercode=" + usercode + "&paycode="
					+ paycode + "&producttype=2&chargeunittype=1&optype=order&monthlytype=1";

			logger.info("UNICOM SENDSMSCODE [url][" + url + "]");

			HttpClient httpClient = new DefaultHttpClient();
			HttpGet get = new HttpGet(url);

			HttpResponse response = null;

			long startTime = 0L;
			long endTime = 0L;
			response = httpClient.execute(get);

			int statusCode = response.getStatusLine().getStatusCode();
			logger.info("statusCode:" + statusCode);
			logger.info("调用API 花费时间(单位：毫秒)：" + (endTime - startTime));
			if (statusCode != HttpStatus.SC_OK)
			{
				logger.error("Method failed:" + response.getStatusLine());
				throw new RpcException(ErrorCodeConst.ErrorCode99999);
			}
			else
			{
				org.apache.http.HttpEntity entity = response.getEntity();
				if (entity != null)
				{
					String body = EntityUtils.toString(response.getEntity(), HTTP.UTF_8);

					JSONObject jo = JSONObject.parseObject(body);
					String returnInnerCode = String.valueOf(jo.get("innercode"));
					String returnMessage = String.valueOf(jo.get("message"));

					restResult = new RestResult(returnInnerCode, returnMessage);
				}
			}
		}
		catch (RpcException e)
		{
			throw e;
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
		String serviceid = String.valueOf(mo.getChargingCode());
		String datetime = mo.getOrderTimestamp();
		String mobile = mo.getMobile();
		String orderid = mo.getOrderId();
		String smscode = mo.getSmscode();
		String username = mo.getUsername();
		String operator = String.valueOf(mo.getCarrierId());
		String channelId = String.valueOf(mo.getChannelId());
		RestResult restResult = null;
		logger.info("UNICOM SMSCHARGE START----------------------------------------------------------------------");
		logger.info("UNICOM SMSCHARGE [cpid][" + cpid + "]");
		logger.info("UNICOM SMSCHARGE [serviceid][" + serviceid + "]");
		logger.info("UNICOM SMSCHARGE [datetime][" + datetime + "]");
		logger.info("UNICOM SMSCHARGE [mobile][" + mobile + "]");
		logger.info("UNICOM SMSCHARGE [orderid][" + orderid + "]");
		logger.info("UNICOM SMSCHARGE [smscode][" + smscode + "]");
		logger.info("UNICOM SMSCHARGE [username][" + username + "]");
		logger.info("UNICOM SMSCHARGE [operator][" + operator + "]");

		try
		{
			ChannelChargingCode channelChargingCode = parameterValidateService.checkChannelChargingCode(serviceid,
					channelId);
			String channelCode = channelChargingCode.getChargingCode();

			Date date = new Date();
			String timestamp = RestConst.SDF_14.format(date);

			// 再提交支付请求
			String clientid = RestConst.CU_CPID;
			String keyversion = RestConst.KEYVERSION;
			String passcode = MD5Util.getMD5Sign(timestamp + RestConst.CU_CPID + RestConst.KEYVERSION
					+ RestConst.CU_KEY);
			String usercode = mobile;
			String ordertype = "2";
			String paytype = "1";
			String paycode = channelCode;

			String url = "http://open.iread.wo.com.cn/smscharge/rest/openread/thirdfee/ordersms/11/" + usercode + "/"
					+ timestamp + "/" + clientid + "/" + keyversion + "/" + passcode + "/";

			logger.info("UNICOM SMSCHARGE [url][" + url + "]");
			logger.info("UNICOM SMSCHARGE [orderid][" + orderid + "]");
			logger.info("UNICOM SMSCHARGE [ordertype][" + ordertype + "]");
			logger.info("UNICOM SMSCHARGE [paytype][" + paytype + "]");
			logger.info("UNICOM SMSCHARGE [paycode][" + paycode + "]");
			logger.info("UNICOM SMSCHARGE [username][" + username + "]");
			logger.info("UNICOM SMSCHARGE [productid][" + mo.getProductId() + "]");
			logger.info("UNICOM SMSCHARGE [productdesc][" + mo.getDescription() + "]");
			logger.info("UNICOM SMSCHARGE [smscode][" + smscode + "]");

			JSONObject object = new JSONObject();
			object.put("orderid", orderid);
			object.put("ordertype", ordertype);
			object.put("paytype", paytype);
			object.put("paycode", paycode);
			object.put("username", username);
			object.put("productid", mo.getProductId());
			object.put("productdesc", mo.getDescription());
			object.put("smscode", smscode);

			HttpClient httpClient = new DefaultHttpClient();
			HttpPost post = new HttpPost(url);

			HttpResponse response = null;

			StringEntity se = new StringEntity(object.toJSONString());
			post.setEntity(se);
			long startTime = 0L;
			long endTime = 0L;
			post.addHeader("Content-Type", "application/json");
			post.addHeader("User-Agent", "imgfornote");
			response = httpClient.execute(post);

			int statusCode = response.getStatusLine().getStatusCode();
			logger.info("statusCode:" + statusCode);
			logger.info("调用API 花费时间(单位：毫秒)：" + (endTime - startTime));
			if (statusCode != HttpStatus.SC_OK)
			{
				logger.error("Method failed:" + response.getStatusLine());
				throw new RpcException(ErrorCodeConst.ErrorCode99999);
			}
			else
			{
				HttpEntity entity = response.getEntity();
				if (entity != null)
				{

					String body = EntityUtils.toString(response.getEntity(), HTTP.UTF_8);

					JSONObject jo = JSONObject.parseObject(body);
					String returnInnerCode = String.valueOf(jo.get("innercode"));
					String returnMessage = String.valueOf(jo.get("message"));
					restResult = new RestResult(returnInnerCode, returnMessage);
				}
			}

			logger.info("UNICOM SMSCHARGE END------------------------------------------------------------------------");
		}
		catch (RpcException e)
		{
			throw e;
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
		logger.info("UNICOM GET FEE START----------------------------------------------------------------------");
		logger.info("UNICOM GET FEE [mcpid][" + mcpid + "]");
		logger.info("UNICOM GET FEE [mserviceid][" + mserviceid + "]");
		logger.info("UNICOM GET FEE [datetime][" + datetime + "]");
		logger.info("UNICOM GET FEE [sign][" + sign + "]");

		StringBuffer sms = new StringBuffer("");
		try
		{
			parameterValidateService.checkCpidMatches(mcpid);
			parameterValidateService.checkServiceIdMatches(mserviceid);
			parameterValidateService.checkDatetimeMatches(datetime);
			parameterValidateService.checkSignMatches(sign);

			Merchant merchant = parameterValidateService.checkMerchant(mcpid);
			String signNoMd5 = mcpid + mserviceid + datetime + merchant.getKey();
			parameterValidateService.checkSign(signNoMd5, sign);
			Point point = parameterValidateService.checkMerchantChargingCode(mserviceid, mcpid);
			ChannelChargingCode channelChargingCode = parameterValidateService.checkChannelChargingCode(mserviceid,
					String.valueOf(RestConst.UNICOM_CHANNELID));
			String channelCode = channelChargingCode.getChargingCode();

			String command = "02";
			String feetype = "12";
			String cpid = RestConst.CU_CPID;
			String serviceid = channelCode;
			String channelid = "12345678";
			String version = "003";
			String appid = String.valueOf(point.getId());
			String myid = "0001";
			String cpprama = "0000000";
			String time = RestConst.SDF_14.format(new Date());

			Double num = Math.random() * 9000 + 1000;
			String random = String.valueOf(num.intValue());
			String cporderid = mcpid + "00" + time + random;

//			String cporderid = time + random + "123456";
			String productId = point.getChargingCode();
			String str = cpid + serviceid + appid + time + cporderid + RestConst.CU_KEY;

			sms.append("<orderid>").append(cporderid).append("</orderid>");
			sms.append("<port>").append("10656666").append("</port>").append("<sms>");
			sms.append(command).append(feetype).append(cpid);
			sms.append(serviceid).append(channelid).append(version);
			sms.append(appid).append(myid).append(cpprama).append(time);
			sms.append(cporderid).append(productId).append(MD5Util.getMD5Sign(str)).append("</sms>");

		}
		catch (RpcException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			throw new RpcException(ErrorCodeConst.ErrorCode99999);
		}
		logger.info("UNICOM GET FEE[sms][" + sms.toString() + "]");
		logger.info("UNICOM GET FEE END------------------------------------------------------------------------");
		return sms.toString();
	}
}
