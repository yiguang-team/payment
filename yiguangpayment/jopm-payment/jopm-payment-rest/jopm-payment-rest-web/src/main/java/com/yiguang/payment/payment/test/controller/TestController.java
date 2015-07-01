package com.yiguang.payment.payment.test.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletRequest;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yiguang.payment.business.product.entity.Point;
import com.yiguang.payment.business.product.service.PointService;
import com.yiguang.payment.common.CommonConstant;
import com.yiguang.payment.common.RestConst;
import com.yiguang.payment.common.datasource.service.DataSourceService;
import com.yiguang.payment.common.datasource.vo.OptionVO;
import com.yiguang.payment.common.security.MD5Util;
import com.yiguang.payment.payment.entity.ChannelChargingCode;
import com.yiguang.payment.payment.entity.Merchant;
import com.yiguang.payment.payment.order.entity.MerchantOrder;
import com.yiguang.payment.payment.order.service.MerchantOrderService;
import com.yiguang.payment.payment.order.service.ParameterValidateService;

/*
 * 接口控制类
 */
@Controller
@RequestMapping(value = "/test")
public class TestController
{
	private static Logger logger = LoggerFactory.getLogger(TestController.class);
	@Autowired
	private ParameterValidateService ParameterValidateService;
	@Autowired
	private MerchantOrderService merchantOrderService;
	@Autowired
	private DataSourceService dataSourceService;
	@Autowired
	private PointService pointService;
	/**
	 * 测试页面初始化
	 */
	@RequestMapping(value = "/init")
	public String initTest(Model model, ServletRequest request)
	{
		List<OptionVO> merchantList = dataSourceService.findOpenOptions(CommonConstant.DataSourceName.MERCHANT);

		model.addAttribute("merchantList", merchantList);
		return "test/init";
	}

	/**
	 * 获取短信
	 */
	@RequestMapping(value = "/testFee")
	public @ResponseBody
	String testFee(@RequestParam(value = "feecpid", defaultValue = "") String cpid,
			@RequestParam(value = "feepointid", defaultValue = "") String serviceid, Model model, ServletRequest request)
	{
		String sms = null;
		Merchant merchant = ParameterValidateService.checkMerchant(cpid);
		String datetime = RestConst.SDF_14.format(new Date());

		String url = "http://localhost:17200/rest/payment/api/1.0/fee";

		HttpClient httpClient = new DefaultHttpClient();

		HttpResponse response = null;
		try
		{
			Point point = pointService.queryPoint(Long.parseLong(serviceid));
			String originalString = cpid.concat(point.getChargingCode()).concat(datetime).concat(merchant.getKey());
			String sign = MD5Util.getMD5Sign(originalString);
			url = url.concat("?").concat("cpid=").concat(cpid).concat("&serviceid=").concat(point.getChargingCode())
					.concat("&datetime=").concat(datetime).concat("&sign=").concat(sign);
			HttpGet get = new HttpGet(url);
			response = httpClient.execute(get);
			HttpEntity entity = response.getEntity();
			InputStream instream = null;

			try
			{
				instream = entity.getContent();

				BufferedReader reader = new BufferedReader(new InputStreamReader(instream, "utf-8"));
				String linet = reader.readLine();
				while (null != linet)
				{
					if (linet.contains("response"))
					{
						sms = linet.substring(linet.indexOf("<sms>") + 5, linet.indexOf("</sms>"));

					}
					linet = reader.readLine();
				}

			}
			catch (IllegalStateException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		catch (Exception e)
		{

		}
		return sms;
	}

	/**
	 * 短代通知
	 */
	@RequestMapping(value = "/testOrder", method = RequestMethod.POST)
	public @ResponseBody
	String testOrder(@RequestParam(value = "orderappid", defaultValue = "") String appid,
			@RequestParam(value = "orderpointid", defaultValue = "") String serviceid,
			@RequestParam(value = "phonenum", defaultValue = "") String phonenum,
			@RequestParam(value = "hRet", defaultValue = "") String hRet, Model model, ServletRequest request)
	{

		String url = "http://localhost:17200/rest/notify/iread/order";

		HttpClient httpClient = new DefaultHttpClient();
		HttpPost post = new HttpPost(url);

		HttpResponse response = null;
		try
		{

			String datetime = RestConst.SDF_14.format(new Date());
			Point point = pointService.queryPoint(Long.parseLong(serviceid));
			ChannelChargingCode channelChargingCode = ParameterValidateService.checkChannelChargingCode(point.getChargingCode(), "2");
			Date date = new Date();
			String timestamp = RestConst.SDF_14.format(date);
			Double num = Math.random() * 9000 + 1000;
			String random = String.valueOf(num.intValue());
			String orderid = point.getId() + "00" + timestamp + random;

			// 真正的短代通知
			StringBuffer signStr = new StringBuffer("");

			signStr.append("orderid=").append(orderid).append("&ordertime=");
			signStr.append(datetime).append("&cpid=").append(RestConst.CU_CPID);
			signStr.append("&appid=").append(point.getId()).append("&fid=");
			signStr.append("12345678").append("&consumeCode=").append(channelChargingCode.getChargingCode())
					.append("&payfee=");
			signStr.append(channelChargingCode.getChargingAmount()).append("&payType=").append("12").append("&myid=");
			signStr.append("0001").append("&phonenum=").append(phonenum).append("&hRet=");
			signStr.append(hRet).append("&status=").append("0").append("&Key=").append(RestConst.CU_KEY);

			StringBuffer notifyStr = new StringBuffer("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			notifyStr.append("<callbackReq><orderid>").append(orderid).append("</orderid><ordertime>").append(datetime)
					.append("</ordertime><cpid>100008</cpid><appid>").append(point.getId())
					.append("</appid><fid>12345678</fid><consumeCode>").append(channelChargingCode.getChargingCode())
					.append("</consumeCode><payfee>").append(channelChargingCode.getChargingAmount())
					.append("</payfee><payType>12</payType><myid>0001</myid><phonenum>").append(phonenum)
					.append("</phonenum><hRet>").append(hRet).append("</hRet><status>").append("0")
					.append("</status><signMsg>").append(MD5Util.getMD5Sign(signStr.toString()))
					.append("</signMsg></callbackReq>");

			StringEntity se = new StringEntity(notifyStr.toString());
			post.setEntity(se);
			long startTime = 0L;
			long endTime = 0L;
			post.addHeader("Content-Type", "text/xml");
			post.addHeader("User-Agent", "imgfornote");
			response = httpClient.execute(post);

			int statusCode = response.getStatusLine().getStatusCode();
			logger.info("statusCode:" + statusCode);
			logger.info("调用API 花费时间(单位：毫秒)：" + (endTime - startTime));
			if (statusCode != HttpStatus.SC_OK)
			{
				logger.error("Method failed:" + response.getStatusLine());
			}
			else
			{

			}
		}
		catch (Exception e)
		{

		}
		return null;
	}

	/**
	 * 短验通知
	 */
	@RequestMapping(value = "/testSmsCallBack", method = RequestMethod.POST)
	public @ResponseBody
	String testSmsCallBack(@RequestParam(value = "orderid", defaultValue = "") String orderid,
			@RequestParam(value = "resultcode", defaultValue = "") String resultcode, Model model,
			ServletRequest request)
	{

		String url = "http://localhost:17200/rest/notify/iread/smsCallBack";

		HttpPost post = new HttpPost(url);

		HttpResponse response = null;
		try
		{

			String clientid = RestConst.CU_CPID;
			String keyversion = RestConst.KEYVERSION;

			String timestamp = RestConst.SDF_14.format(new Date());
			StringBuffer signStr = new StringBuffer("");
			signStr.append(timestamp).append(clientid).append(keyversion).append(RestConst.CU_KEY);

			// BasicHttpParams params = new BasicHttpParams();
			// params.setParameter("clientid", clientid);
			// params.setParameter("keyversion", keyversion);
			// params.setParameter("timestamp", timestamp);
			// params.setParameter("orderid", orderid);
			// params.setParameter("resultcode", resultcode);
			// params.setParameter("passcode",
			// MD5Util.getMD5Sign(signStr.toString()));
			// post.setParams(params);

			ArrayList<BasicNameValuePair> pairs = new ArrayList<BasicNameValuePair>();

			pairs.add(new BasicNameValuePair("clientid", clientid));
			pairs.add(new BasicNameValuePair("keyversion", keyversion));
			pairs.add(new BasicNameValuePair("timestamp", timestamp));
			pairs.add(new BasicNameValuePair("orderid", orderid));
			pairs.add(new BasicNameValuePair("resultcode", resultcode));
			pairs.add(new BasicNameValuePair("resultmsg", resultcode));
			pairs.add(new BasicNameValuePair("passcode", MD5Util.getMD5Sign(signStr.toString())));

			HttpParams httpParameters = new BasicHttpParams();

			HttpClient httpclient = new DefaultHttpClient(httpParameters);
			post.setEntity(new UrlEncodedFormEntity(pairs, "UTF-8"));

			long startTime = 0L;
			long endTime = 0L;
			response = httpclient.execute(post);

			int statusCode = response.getStatusLine().getStatusCode();
			logger.info("statusCode:" + statusCode);
			logger.info("调用API 花费时间(单位：毫秒)：" + (endTime - startTime));
			if (statusCode != HttpStatus.SC_OK)
			{
				logger.error("Method failed:" + response.getStatusLine());
			}
			else
			{

			}
		}
		catch (Exception e)
		{

		}
		return null;
	}

	/**
	 * 短验通知
	 */
	@RequestMapping(value = "/testTelecomCallBack", method = RequestMethod.POST)
	public @ResponseBody
	String testTelecomCallBack(@RequestParam(value = "orderid", defaultValue = "") String orderid,
			@RequestParam(value = "resultcode", defaultValue = "") String resultcode,
			@RequestParam(value = "resultinfo", defaultValue = "") String resultinfo, Model model,
			ServletRequest request)
	{

		String url = "http://localhost:17200/rest/notify/telecom/smsCallBack";

		HttpPost post = new HttpPost(url);

		HttpResponse response = null;
		try
		{
			Double num = Math.random() * 9000 + 1000;
			String random = String.valueOf(num.intValue());
			MerchantOrder porder = merchantOrderService.queryMerchantOrderByOrderId(orderid);

			String timestamp = RestConst.SDF_14.format(new Date());

			String UPTRANSEQ = orderid + random;

			// BasicHttpParams params = new BasicHttpParams();
			// params.setParameter("clientid", clientid);
			// params.setParameter("keyversion", keyversion);
			// params.setParameter("timestamp", timestamp);
			// params.setParameter("orderid", orderid);
			// params.setParameter("resultcode", resultcode);
			// params.setParameter("passcode",
			// MD5Util.getMD5Sign(signStr.toString()));
			// post.setParams(params);

			ArrayList<BasicNameValuePair> pairs = new ArrayList<BasicNameValuePair>();

			StringBuffer signStr = new StringBuffer("");
			signStr.append("UPTRANSEQ=").append(UPTRANSEQ).append("&MERCHANTID=").append(porder.getMerchantId());
			signStr.append("&ORDERID=").append(orderid).append("&PAYMENT=").append(porder.getPayAmount());
			signStr.append("&RETNCODE=").append("resultcode").append("&RETNINFO=").append(resultinfo);
			signStr.append("&PAYDATE=").append(timestamp).append("&KEY=").append(RestConst.CT_KEY);

			pairs.add(new BasicNameValuePair("RETNCODE", resultcode));
			pairs.add(new BasicNameValuePair("RETNINFO", resultinfo));
			pairs.add(new BasicNameValuePair("TRANDATE", timestamp));
			pairs.add(new BasicNameValuePair("ORDERSEQ", orderid));
			pairs.add(new BasicNameValuePair("UPTRANSEQ", UPTRANSEQ));
			pairs.add(new BasicNameValuePair("MERCHANTID", RestConst.CT_CPID));
			pairs.add(new BasicNameValuePair("SIGN", MD5Util.getMD5Sign(signStr.toString())));

			HttpParams httpParameters = new BasicHttpParams();

			HttpClient httpclient = new DefaultHttpClient(httpParameters);
			post.setEntity(new UrlEncodedFormEntity(pairs, "UTF-8"));

			long startTime = 0L;
			long endTime = 0L;
			response = httpclient.execute(post);

			int statusCode = response.getStatusLine().getStatusCode();
			logger.info("statusCode:" + statusCode);
			logger.info("调用API 花费时间(单位：毫秒)：" + (endTime - startTime));
			if (statusCode != HttpStatus.SC_OK)
			{
				logger.error("Method failed:" + response.getStatusLine());
			}
			else
			{

			}
		}
		catch (Exception e)
		{

		}
		return null;
	}
}
