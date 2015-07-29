package com.yiguang.payment.payment.rest.service.impl;

import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yiguang.payment.common.CommonConstant;
import com.yiguang.payment.common.RestConst;
import com.yiguang.payment.common.security.MD5Util;
import com.yiguang.payment.payment.entity.Merchant;
import com.yiguang.payment.payment.order.entity.MerchantOrder;
import com.yiguang.payment.payment.rest.service.NotifyService;
import com.yiguang.payment.payment.service.MerchantService;

@Service("notifyService")
@Transactional
public class NotifyServiceImpl implements NotifyService {

	private static Logger logger = LoggerFactory.getLogger(NotifyServiceImpl.class);

	@Autowired
	private MerchantService merchantService;

	@Override
	public MerchantOrder notify(MerchantOrder merchantOrder) {

		try {
			long merchantId = merchantOrder.getMerchantId();
			Merchant merchant = merchantService.queryMerchant(merchantId);
			
			if (StringUtils.isNotEmpty(merchant.getNotifyUrl())) {
				
				StringBuffer notifyUrl = new StringBuffer();
				String key = merchant.getKey();
				String mobile = merchantOrder.getMobile();
				String merchantid = String.valueOf(merchantId);
				String merchantOrderid = merchantOrder.getMerchantOrderId();
				String payorderid = merchantOrder.getOrderId();
				String payfee = String.valueOf(merchantOrder.getPayAmount());
				Date date = new Date();
				String datetime = RestConst.SDF_14.format(date);
				String errorcode = "0000";
				String result = String.valueOf(merchantOrder.getPayStatus());
				String sign = MD5Util.getMD5Sign(
						merchantid + merchantOrderid + payorderid + datetime + payfee + mobile + result + key);
				notifyUrl.append(merchant.getNotifyUrl());
				notifyUrl.append("?merchantid=").append(merchantid);
				notifyUrl.append("&orderid=").append(merchantOrderid);
				notifyUrl.append("&payorderid=").append(payorderid);
				notifyUrl.append("&datetime=").append(datetime);
				notifyUrl.append("&mobile=").append(mobile);
				notifyUrl.append("&errorcode=").append(errorcode);
				notifyUrl.append("&payfee=").append(payfee);
				notifyUrl.append("&result=").append(result);
				notifyUrl.append("&returnMessage=").append(merchantOrder.getReturnMessage());
				notifyUrl.append("&returnCode=").append(merchantOrder.getReturnCode());
				notifyUrl.append("&sign=").append(sign);

				HttpClient httpClient = new DefaultHttpClient();
				HttpGet get = new HttpGet(notifyUrl.toString());

				HttpResponse response = null;

				long startTime = 0L;
				long endTime = 0L;
				response = httpClient.execute(get);

				int statusCode = response.getStatusLine().getStatusCode();
				logger.info("调用API 花费时间(单位：毫秒)：" + (endTime - startTime));
				if (statusCode == HttpStatus.SC_OK) {
					logger.info("payorderid : " + payorderid + " notify success . result:" + statusCode);
					merchantOrder.setNotifyStatus(CommonConstant.NotifyStatus.SUCCESS);
					merchantOrder.setNotifyUrl(notifyUrl.toString());
				} else {
					merchantOrder.setNotifyStatus(CommonConstant.NotifyStatus.FAILED);
					merchantOrder.setNotifyUrl(notifyUrl.toString());
					logger.info("payorderid : " + payorderid + " notify failed . result:" + statusCode);
				}
			}

		} catch (Exception e) {
			logger.error(e.getLocalizedMessage(), e);
			logger.error("notify failed .");
		}
		return merchantOrder;

	}
}
