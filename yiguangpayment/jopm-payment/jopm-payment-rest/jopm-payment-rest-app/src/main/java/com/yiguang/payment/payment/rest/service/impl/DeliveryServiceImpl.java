package com.yiguang.payment.payment.rest.service.impl;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.rpc.RpcException;
import com.yiguang.payment.common.CommonConstant;
import com.yiguang.payment.common.RestConst;
import com.yiguang.payment.common.message.MessageResolver;
import com.yiguang.payment.common.security.MD5Util;
import com.yiguang.payment.common.utils.BigDecimalUtil;
import com.yiguang.payment.depot.order.entity.DepotOrder;
import com.yiguang.payment.depot.order.service.DepotOrderService;
import com.yiguang.payment.payment.order.entity.MerchantOrder;
import com.yiguang.payment.payment.order.service.MerchantOrderService;
import com.yiguang.payment.payment.rest.service.DeliveryService;

@Service("deliveryService")
@Transactional
public class DeliveryServiceImpl implements DeliveryService
{
	private static Logger logger = LoggerFactory.getLogger(DeliveryServiceImpl.class);
	
	@Autowired
	private DepotOrderService depotOrderService;
	@Autowired
	private MerchantOrderService merchantOrderService;
	
	@Override
	public MerchantOrder delivery(MerchantOrder merchantOrder)
	{
		merchantOrder.setDeliveryRequestTime(new Date());
		
		// 骏卡发货
		if (merchantOrder.getChargingType() == CommonConstant.CHARGING_TYPE.DERICT)
		{
			if (merchantOrder.getProductId() == RestConst.JUNWANG_YIKATONG_ID)
			{
				String url = RestConst.JNET_CHARGE;
				String dateTime = RestConst.SDF_14.format(merchantOrder.getRequestTime());
				String amount = String.valueOf(BigDecimalUtil.divide(merchantOrder.getDeliveryAmount(),
						new BigDecimal("100"), 2, BigDecimal.ROUND_HALF_UP));
				StringBuffer md5 = new StringBuffer("");
				md5.append("agent_id=").append(RestConst.APP_AGENT_ID);
				md5.append("&bill_id=").append(merchantOrder.getOrderId());
				md5.append("&bill_time=").append(dateTime);
				md5.append("&user_account=").append(merchantOrder.getUsername());
				md5.append("&charge_amt=").append(amount);
				md5.append("&time_stamp=").append(dateTime);

				String md5Key = MD5Util.getMD5Sign(md5.toString() + "|||" + RestConst.APP_AGENT_KEY);

				url = url.concat("?").concat(md5.toString()).concat("&sign=").concat(md5Key);
				url = url.concat("&client_ip=").concat(RestConst.CLIENT_IP).concat("&phone=")
						.concat(merchantOrder.getMobile());
				HttpGet get = new HttpGet(url);
				HttpClient httpClient = new DefaultHttpClient();

				HttpResponse response = null;

				try
				{
					logger.info("MALL JUNWANG_YIKATONG_ID delivery start");
					logger.info("MALL JUNWANG_YIKATONG_ID delivery url [" + url + "]");

					response = httpClient.execute(get);
					HttpEntity entity = response.getEntity();
					InputStream instream = null;
					String deliveryReturn = "";
					instream = entity.getContent();

					BufferedReader reader = new BufferedReader(new InputStreamReader(instream, "GB2312"));
					String linet = reader.readLine();
					while (null != linet)
					{
						deliveryReturn = deliveryReturn + linet;
						linet = reader.readLine();
					}

					logger.info("MALL JUNWANG_YIKATONG_ID delivery response [" + deliveryReturn + "]");
					Map<String, String> paraMap = new HashMap<String, String>();
					String[] keyAndValues = deliveryReturn.split("&");
					for (String keyAndValue : keyAndValues)
					{
						String[] s = keyAndValue.split("=");
						if (s.length > 1)
						{
							paraMap.put(s[0], s[1]);
						}
						else
						{
							paraMap.put(s[0], "");
						}
					}

					String ret_code = paraMap.get("ret_code");
					String ret_msg = paraMap.get("ret_msg");
					String jnet_bill_no = paraMap.get("jnet_bill_no");

					merchantOrder.setDeliveryReturnCode(ret_code);
					merchantOrder.setDeliveryReturnMessage(ret_msg);
					merchantOrder.setDeliveryNo(jnet_bill_no);
					if ("0".equals(ret_code))
					{
						merchantOrder.setDeliveryStatus(CommonConstant.DeliveryStatus.SUCCESS);
					}
					else
					{
						merchantOrder.setDeliveryStatus(CommonConstant.DeliveryStatus.FAILED);
					}
					merchantOrder.setDeliveryCompleteTime(new Date());
					logger.info("MALL JUNWANG_YIKATONG_ID delivery end");
				}
				catch (Exception e)
				{
					// 发货失败
					logger.error("MALL JUNWANG_YIKATONG_ID delivery failed");
					logger.error(e.getLocalizedMessage(), e);

				}
			}
		}
		else
		{
			logger.info("MALL CARD delivery start");

			DepotOrder depotOrder = new DepotOrder();
			depotOrder.setRequestIp(merchantOrder.getRequestIp());

			// 外部产品id对应内部计费点id
			depotOrder.setChargingPointId(merchantOrder.getChargingPointId());

			depotOrder.setExtractUser(RestConst.YIGUANG_MERCHANT_ID);

			// 设置日志生成渠道为接口提卡
			depotOrder.setExtractType(0);
			depotOrder.setExtractCount(1);
			depotOrder.setOrderId(merchantOrder.getOrderId());
			depotOrder.setPayAmount(Long.parseLong(String.valueOf(merchantOrder.getPayAmount())));
			depotOrder.setRequestTime(new Date());
			depotOrder.setReturnCode("0000");
			depotOrder.setReturnMessage("0000");
			depotOrder.setMerchantId(merchantOrder.getMerchantId());
			// 提卡
			try
			{
				long deliveryId = depotOrderService.pickUpCardWithoutList(depotOrder);
				merchantOrder.setDeliveryReturnCode("0000");
				merchantOrder.setDeliveryReturnMessage("提卡成功");
				merchantOrder.setDeliveryNo(String.valueOf(deliveryId));
				merchantOrder.setDeliveryStatus(CommonConstant.DeliveryStatus.SUCCESS);

				merchantOrder.setDeliveryCompleteTime(new Date());
			}
			catch (RpcException e)
			{
				logger.info("MALL CARD delivery failed");
				merchantOrder.setDeliveryReturnCode(String.valueOf(e.getCode()));
				merchantOrder.setDeliveryReturnMessage(MessageResolver.getMessage(e.getCode()));
				merchantOrder.setDeliveryStatus(CommonConstant.DeliveryStatus.FAILED);
			}
			logger.info("MALL CARD delivery end");
		}
		
		merchantOrder = merchantOrderService.updateMerchantOrder(merchantOrder);
		return merchantOrder;
	}

}
