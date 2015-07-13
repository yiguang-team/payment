package com.yiguang.payment.merchantOperate.serviceImpl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.yiguang.payment.business.product.entity.Point;
import com.yiguang.payment.business.product.service.PointService;
import com.yiguang.payment.common.RestConst;
import com.yiguang.payment.common.security.MD5Util;
import com.yiguang.payment.common.utils.StringUtil;
import com.yiguang.payment.merchantOperate.entity.MobileAndTotal;
import com.yiguang.payment.merchantOperate.service.MerchantOperateService;
import com.yiguang.payment.payment.entity.Merchant;
import com.yiguang.payment.payment.order.service.ParameterValidateService;
import com.yiguang.payment.payment.order.vo.MerchantOrderVO;

@Service(value = "merchantOperateService")
public class MerchantOperateServiceImpl implements MerchantOperateService {

	private static Logger logger = LoggerFactory
			.getLogger(MerchantOperateServiceImpl.class);
	@PersistenceContext
	private EntityManager em;
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private PointService pointService;
	@Autowired
	private ParameterValidateService parameterValidateService;
	@Override
	public String getMessage(String merchantId, String serviceid) {
		
		String sms = null;
		
		String cpid = merchantId;
		Merchant merchant = parameterValidateService.checkMerchant(cpid);
		String datetime = RestConst.SDF_14.format(new Date());

//		String url = "http://192.168.1.41/rest/iread/fee";
		String url = "http://localhost:17200/rest/payment/api/1.0/fee";
		HttpClient httpClient = new DefaultHttpClient();

		HttpResponse response = null;
		try
		{
			Point point = pointService.queryPoint(Long.valueOf(serviceid));
			
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
			e.printStackTrace();
		}
		return sms;
	}

	@Override
	public String getTotal(String merchantId,String beginDate,String endDate) {
		logger.debug("[MerchantOperateServiceImpl: getTotal(" + merchantId
				+ " , " + beginDate + ", " + endDate + ")] start");

		String sql = "select sum(pay_amount)/100 as total from t_merchant_order where PAY_STATUS = '0'  and merchant_id ='"
				+ merchantId;
		sql = sql + "' and request_time >= to_date('" + beginDate +"','yyyy-mm-dd hh24:mi:ss')";
		sql = sql + "  and request_time <= to_date('" + endDate +"','yyyy-mm-dd hh24:mi:ss')";
		logger.debug("[MerchantOperateServiceImpl: getTotal sql= (" + sql
				+ ")]");
		Query query = em.createNativeQuery(sql);
		Object sum =  query.getSingleResult();// 执行得到结果
		String total = "";
		if(sum != null){
			BigDecimal to = (BigDecimal) sum;
			total = to.toString();

		logger.debug("[MerchantOperateServiceImpl: total =' " + total + "]'");
		logger.debug("[MerchantOperateServiceImpl: getTotal(" + merchantId
				+ " , " + beginDate + ", " + endDate + ")] end");
		}
		return total;
	}

	@Override
	public List<MerchantOrderVO> getOrderList(String orderId, String merchantOrderId, String mobile, String beginDate,
			String username, String provinceId, String endDate, String channelId, String carrierId, String merchantId,
			String cityId, String productId, String payStatus, String deliveryStatus, String chargingPointId,
			String chargingType, String channelType, String notifyStatus) {
		logger.debug("[MerchantOperateServiceImpl: getOrderList(" + orderId
				+ " , " + mobile + "," + username + ","+beginDate+","+endDate+","+provinceId+")] start");
		String condition = StringUtil.initString();
		if (StringUtil.isNotBlank(orderId))
		{
			condition = condition + " and order_id like '%" + orderId + "%'";
		}
		if (StringUtil.isNotBlank(merchantOrderId))
		{
			condition = condition + " and merchant_order_id like '%" + merchantOrderId + "%'";
		}
		if (StringUtil.isNotBlank(username))
		{
			condition = condition + " and username like '%" + username + "%'";
		}
		if (StringUtil.isNotBlank(mobile))
		{
			condition = condition + " and mobile like '%" + mobile + "%'";
		}
		if (StringUtil.isNotBlank(provinceId) && !"-1".equals(provinceId))
		{
			condition = condition + " and province_id = '" + provinceId + "'";
		}
		if (StringUtil.isNotBlank(beginDate))
		{
			condition = condition + " and request_time >= to_date('" + beginDate + "','yyyy-mm-dd hh24:mi:ss')";
		}
		if (StringUtil.isNotBlank(endDate))
		{
			condition = condition + " and request_time <= to_date('" + endDate + "','yyyy-mm-dd hh24:mi:ss')";
		}
		if (StringUtil.isNotBlank(channelId) && !"-1".equals(channelId))
		{
			condition = condition + " and channel_id = '" + channelId + "'";
		}
		if (StringUtil.isNotBlank(carrierId) && !"-1".equals(carrierId))
		{
			condition = condition + " and carrier_id = '" + carrierId + "'";
		}
		if (StringUtil.isNotBlank(merchantId) && !"-1".equals(merchantId))
		{
			condition = condition + " and merchant_id = '" + merchantId + "'";
		}
		if (StringUtil.isNotBlank(cityId) && !"-1".equals(cityId))
		{
			condition = condition + " and city_id = '" + cityId + "'";
		}
		if (StringUtil.isNotBlank(productId) && !"-1".equals(productId))
		{
			condition = condition + " and product_id = '" + productId + "'";
		}
		if (StringUtil.isNotBlank(payStatus) && !"-1".equals(payStatus))
		{
			condition = condition + " and pay_status = '" + payStatus + "'";
		}
		if (StringUtil.isNotBlank(deliveryStatus) && !"-1".equals(deliveryStatus))
		{
			condition = condition + " and delivery_status = '" + deliveryStatus + "'";
		}
		if (StringUtil.isNotBlank(chargingPointId) && !"-1".equals(chargingPointId))
		{
			condition = condition + " and charging_point_id = '" + chargingPointId + "'";
		}
		if (StringUtil.isNotBlank(chargingType) && !"-1".equals(chargingType))
		{
			condition = condition + " and charging_type = '" + chargingType + "'";
		}
		if (StringUtil.isNotBlank(channelType) && !"-1".equals(channelType))
		{
			condition = condition + " and channel_type = '" + channelType + "'";
		}
		if (StringUtil.isNotBlank(notifyStatus) && !"-1".equals(notifyStatus))
		{
			condition = condition + " and notify_status = '" + notifyStatus + "'";
		}
		
		String sql = "select request_time,order_id,mobile,pay_amount/100,username from t_merchant_order where 1=1"+condition;
	
		logger.debug("[MerchantOperateServiceImpl: getListMAT sql= (" + sql
				+ ")]");
		List<Map<String,Object>> resultList = jdbcTemplate.queryForList(sql);
		logger.debug("[MerchantOperateServiceImpl: getListMAT sql= (" + resultList.toString()
				+ ")]");
		List<MerchantOrderVO> list = new ArrayList<MerchantOrderVO>();
		if(resultList != null){
			for (Map<String,Object> map : resultList)
			{
				MerchantOrderVO vo = new MerchantOrderVO();
				vo.setRequestTime(String.valueOf(map.get("request_time")));
				vo.setOrderId(String.valueOf(map.get("order_id")));
				vo.setMobile(String.valueOf(map.get("mobile")));
				vo.setPayAmount((BigDecimal) map.get("pay_amount/100"));
				vo.setUsername(String.valueOf(map.get("username")));
				list.add(vo);
			}
			logger.debug("[MerchantOperateServiceImpl: getOrderList(" + orderId
				+ " , " + mobile + ", " + username + ","+beginDate+","+endDate+","+provinceId+")] end");
		}
		return list;
	}
	
	@Override
	public List<MobileAndTotal> getListMAT(String merchantId,String beginDate,String endDate) {
		logger.debug("[MerchantOperateServiceImpl: getListMAT(" + merchantId
				+ " , " + beginDate + ", " + endDate + ")] start");
		
		String sql = "select mobile,sum(pay_amount)/100 as total from t_merchant_order where PAY_STATUS = '0' and merchant_id ='"
				+ merchantId;
		sql = sql + "' and request_time >= to_date('" + beginDate +"','yyyy-mm-dd hh24:mi:ss')";
		sql = sql + "  and request_time <= to_date('" + endDate +"','yyyy-mm-dd hh24:mi:ss')  group by mobile";
		logger.debug("[MerchantOperateServiceImpl: getListMAT sql= (" + sql
				+ ")]");
		List<Map<String,Object>> resultList = jdbcTemplate.queryForList(sql);
		logger.debug("[MerchantOperateServiceImpl: getListMAT sql= (" + resultList.toString()
				+ ")]");
		List<MobileAndTotal> list = new ArrayList<MobileAndTotal>();
		if(resultList != null){
			for (Map<String,Object> map : resultList)
			{
				MobileAndTotal mobileAndTotal = new MobileAndTotal();
				mobileAndTotal.setMobile(String.valueOf(map.get("mobile")));
				mobileAndTotal.setTotal(String.valueOf(map.get("total")));
				list.add(mobileAndTotal);
			}
			logger.debug("[MerchantOperateServiceImpl: getListMAT(" + merchantId
					+ " , " + beginDate + ", " + endDate + ")] end");
		}
		return list;
	}
}
