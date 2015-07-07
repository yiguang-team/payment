package com.yiguang.payment.payment.order.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.rpc.RpcException;
import com.yiguang.payment.common.CommonConstant;
import com.yiguang.payment.common.DateTimeHelper;
import com.yiguang.payment.common.datasource.service.DataSourceService;
import com.yiguang.payment.common.enump.DecimalPlaces;
import com.yiguang.payment.common.exception.ErrorCodeConst;
import com.yiguang.payment.common.query.YcPage;
import com.yiguang.payment.common.utils.BigDecimalUtil;
import com.yiguang.payment.common.utils.StringUtil;
import com.yiguang.payment.payment.order.entity.MerchantOrder;
import com.yiguang.payment.payment.order.repository.MerchantOrderDao;
import com.yiguang.payment.payment.order.service.MerchantOrderService;
import com.yiguang.payment.payment.order.vo.MerchantOrderVO;

@Service("merchantOrderService")
@Transactional
public class MerchantOrderServiceImpl implements MerchantOrderService
{

	private static Logger logger = LoggerFactory.getLogger(MerchantOrderServiceImpl.class);

	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@PersistenceContext
	private EntityManager em;

	@Autowired
	private MerchantOrderDao orderDao;

	@Autowired
	private DataSourceService dataSourceService;

	@SuppressWarnings("unchecked")
	@Override
	public YcPage<MerchantOrderVO> queryMerchantOrderList(int pageNumber,
			int pageSize, String sortType, String orderId, String merchantOrderId, String requestIp, String username,
			String mobile, long carrierId, long channelId, long merchantId, String provinceId, String cityId,
			long productId, int payStatus, int deliveryStatus, String deliveryNo,
			long chargingPointId, int chargingType,int channelType, int notifyStatus, String beginDate, String endDate)
	{
		logger.debug("queryMerchantOrderList start");
		try
		{
			int startIndex = pageNumber * pageSize - pageSize;
			int endIndex = startIndex + pageSize;
			if (StringUtil.isBlank(sortType) || sortType.equalsIgnoreCase("auto"))
			{
				sortType = "request_time";
			}
			String insidesql = "select o.*,rownum rn from (select * from t_merchant_order order by " + sortType
					+ " desc) o where 1=1";
			if (endIndex > 0)
			{
				insidesql = insidesql + " and rownum <= " + endIndex;
			}
			String condition = StringUtil.initString();
			if (StringUtil.isNotBlank(orderId))
			{
				condition = condition + " and order_id like '%" + orderId + "%'";
			}
			if (StringUtil.isNotBlank(merchantOrderId))
			{
				condition = condition + " and merchant_order_id like '%" + merchantOrderId + "%'";
			}
			if (StringUtil.isNotBlank(requestIp))
			{
				condition = condition + " and request_ip like '%" + requestIp + "%'";
			}
			if (StringUtil.isNotBlank(username))
			{
				condition = condition + " and username like '%" + username + "%'";
			}
			if (StringUtil.isNotBlank(String.valueOf(mobile)))
			{
				condition = condition + " and mobile like '%" + mobile + "%'";
			}
			
			if (StringUtil.isNotBlank(String.valueOf(carrierId)) && carrierId != -1)
			{
				condition = condition + " and carrier_id = '" + carrierId + "'";
			}
			if (StringUtil.isNotBlank(String.valueOf(channelId)) && channelId != -1)
			{
				condition = condition + " and channel_id = '" + channelId + "'";
			}
			if (StringUtil.isNotBlank(String.valueOf(merchantId)) && merchantId != -1)
			{
				condition = condition + " and merchant_id = '" + merchantId + "'";
			}
			if (StringUtil.isNotBlank(provinceId) && !"-1".equals(provinceId))
			{
				condition = condition + " and province_id = '" + provinceId + "'";
			}
			if (StringUtil.isNotBlank(deliveryNo))
			{
				condition = condition + " and delivery_no like '%" + deliveryNo + "%'";
			}
			if (StringUtil.isNotBlank(cityId) && !"-1".equals(cityId))
			{
				condition = condition + " and city_id = '" + cityId + "'";
			}
			if (StringUtil.isNotBlank(String.valueOf(productId)) && productId != -1)
			{
				condition = condition + " and product_id = '" + productId + "'";
			}
			if (StringUtil.isNotBlank(String.valueOf(chargingPointId)) && chargingPointId != -1)
			{
				condition = condition + " and charging_point_id = '" + chargingPointId + "'";
			}
			if (StringUtil.isNotBlank(String.valueOf(payStatus)) && payStatus != -1)
			{
				condition = condition + " and pay_status = '" + payStatus + "'";
			}
			if (StringUtil.isNotBlank(String.valueOf(deliveryStatus)) && deliveryStatus != -1)
			{
				condition = condition + " and delivery_status = '" + deliveryStatus + "'";
			}
			if (StringUtil.isNotBlank(String.valueOf(chargingType)) && chargingType != -1)
			{
				condition = condition + " and charging_type = '" + chargingType + "'";
			}
			if (StringUtil.isNotBlank(String.valueOf(channelType)) && channelType != -1)
			{
				condition = condition + " and channel_type = '" + channelType + "'";
			}
			if (StringUtil.isNotBlank(String.valueOf(notifyStatus)) && notifyStatus != -1)
			{
				condition = condition + " and notify_status = '" + notifyStatus + "'";
			}
			if (StringUtil.isNotBlank(beginDate))
			{
				condition = condition + " and request_time >= to_date('" + beginDate + "','yyyy-mm-dd hh24:mi:ss')";
			}
			if (StringUtil.isNotBlank(endDate))
			{
				condition = condition + " and request_time <= to_date('" + endDate + "','yyyy-mm-dd hh24:mi:ss')";
			}
			String pageTotal_sql = "select count(*) from t_merchant_order where 1=1 " + condition;
			Query query = em.createNativeQuery(pageTotal_sql);
			BigDecimal total = (BigDecimal) query.getSingleResult();

			Double pageTotal = BigDecimalUtil.divide(total, new BigDecimal(pageSize), DecimalPlaces.ZERO.value(),
					BigDecimal.ROUND_CEILING).doubleValue();

			insidesql = insidesql + condition;
			String sql = "select * from (" + insidesql + ") where rn>" + startIndex + "order by request_time desc";
			query = em.createNativeQuery(sql, MerchantOrder.class);
			List<MerchantOrder> list = query.getResultList();

			YcPage<MerchantOrderVO> ycPage = new YcPage<MerchantOrderVO>();
			ycPage.setCountTotal(total.intValue());
			ycPage.setPageTotal(pageTotal.intValue());

			List<MerchantOrderVO> voList = new ArrayList<MerchantOrderVO>();
			MerchantOrderVO vo = null;
			for (MerchantOrder temp : list)
			{
				vo = copyPropertiesToVO(temp);
				voList.add(vo);
			}
			ycPage.setList(voList);
			logger.debug("queryMerchantOrderList end");
			return ycPage;
		}
		catch (Exception e)
		{
			logger.error("queryMerchantOrderList failed");
			logger.error(e.getLocalizedMessage(), e);
			throw new RpcException(ErrorCodeConst.ErrorCode99998);
		}
	}

	@Override
	public MerchantOrderVO copyPropertiesToVO(MerchantOrder temp)
	{
		logger.debug("copyPropertiesToVO start");
		try
		{
			MerchantOrderVO vo = new MerchantOrderVO();

			vo.setId(temp.getId());
			vo.setOrderId(temp.getOrderId());
			vo.setMerchantOrderId(temp.getMerchantOrderId());
			if (null != temp.getRequestTime())
			{
				vo.setRequestTime(DATE_FORMAT.format(temp.getRequestTime()));
			}
			if (null != temp.getCompleteTime())
			{
				vo.setCompleteTime(DATE_FORMAT.format(temp.getCompleteTime()));
			}
			if (null != temp.getDeliveryRequestTime())
			{
				vo.setDeliveryRequestTime(DATE_FORMAT.format(temp.getDeliveryRequestTime()));
			}
			if (null != temp.getDeliveryCompleteTime())
			{
				vo.setDeliveryCompleteTime(DATE_FORMAT.format(temp.getDeliveryCompleteTime()));
			}
			vo.setMobile(temp.getMobile());
			vo.setMerchantId(temp.getMerchantId());
			vo.setProvinceId(temp.getProvinceId());
			vo.setCityId(temp.getCityId());
			vo.setProductId(temp.getProductId());
			vo.setFaceAmount(BigDecimalUtil.divide(temp.getFaceAmount(), new BigDecimal("100"), 2,
					BigDecimal.ROUND_HALF_UP));
			vo.setPayAmount(BigDecimalUtil.divide(temp.getPayAmount(), new BigDecimal("100"), 2,
					BigDecimal.ROUND_HALF_UP));
			vo.setDeliveryAmount(BigDecimalUtil.divide(temp.getDeliveryAmount(), new BigDecimal("100"), 2,
					BigDecimal.ROUND_HALF_UP));
			vo.setPayStatus(temp.getPayStatus());
			vo.setDeliveryStatus(temp.getDeliveryStatus());
			vo.setDeliveryNo(temp.getDeliveryNo());
			vo.setUsername(temp.getUsername());
			vo.setRequestIp(temp.getRequestIp());
			vo.setChargingType(temp.getChargingType());
			vo.setChannelId(temp.getChannelId());
			vo.setExt1(temp.getExt1());
			vo.setExt2(temp.getExt2());
			vo.setExt3(temp.getExt3());
			vo.setExt4(temp.getExt4());
			vo.setExt5(temp.getExt5());
			vo.setOrderTimestamp(temp.getOrderTimestamp());
			vo.setPayTimestamp(temp.getPayTimestamp());
			vo.setCarrierId(temp.getCarrierId());
			vo.setSmscode(temp.getSmscode());
			vo.setCarrierLabel(dataSourceService.findOptionVOById(CommonConstant.DataSourceName.CARRIER,
					String.valueOf(temp.getCarrierId())).getText());
			vo.setPointLabel(dataSourceService.findOptionVOById(CommonConstant.DataSourceName.POINT,
					String.valueOf(temp.getChargingPointId())).getText());
			vo.setPayStatusLabel(dataSourceService.findOptionVOById(CommonConstant.DataSourceName.PAY_STATUS,
					String.valueOf(temp.getPayStatus())).getText());
			vo.setDeliveryStatusLabel(dataSourceService.findOptionVOById(CommonConstant.DataSourceName.DELIVERY_STATUS,
					String.valueOf(temp.getDeliveryStatus())).getText());
			vo.setMerchantLabel(dataSourceService.findOptionVOById(CommonConstant.DataSourceName.MERCHANT,
					String.valueOf(temp.getMerchantId())).getText());
			vo.setChannelLabel(dataSourceService.findOptionVOById(CommonConstant.DataSourceName.CHANNEL,
					String.valueOf(temp.getChannelId())).getText());
			vo.setProvinceLabel(dataSourceService.findOptionVOById(CommonConstant.DataSourceName.PROVINCE,
					String.valueOf(temp.getProvinceId())).getText());
			vo.setCityLabel(dataSourceService.findOptionVOById(CommonConstant.DataSourceName.CITY,
					String.valueOf(temp.getCityId())).getText());
			vo.setProductLabel(dataSourceService.findOptionVOById(CommonConstant.DataSourceName.PRODUCT,
					String.valueOf(temp.getProductId())).getText());
			vo.setChargingTypeLabel(dataSourceService.findOptionVOById(CommonConstant.DataSourceName.CHARGING_TYPE,
					String.valueOf(temp.getChargingType())).getText());
			vo.setReturnCode(temp.getReturnCode());
			vo.setReturnMessage(temp.getReturnMessage());
			vo.setDeliveryReturnCode(temp.getDeliveryReturnCode());
			vo.setDeliveryReturnMessage(temp.getDeliveryReturnMessage());
			
			vo.setChannelType(temp.getChannelType());
			vo.setChannelTypeLabel(dataSourceService.findOptionVOById(CommonConstant.DataSourceName.CHANNEL_TYPE,
					String.valueOf(temp.getChannelType())).getText());
			
			vo.setNotifyStatus(temp.getNotifyStatus());
			vo.setNotifyStatusLabel(dataSourceService.findOptionVOById(CommonConstant.DataSourceName.NOTIFY_STATUS,
					String.valueOf(temp.getNotifyStatus())).getText());
			
			vo.setShowUrl(temp.getShowUrl());
			vo.setNotifyUrl(temp.getNotifyUrl());
			vo.setSubject(temp.getSubject());
			vo.setDescription(temp.getDescription());
			vo.setChannelTradeNo(temp.getChannelTradeNo());
			logger.debug("copyPropertiesToVO end");
			return vo;
		}
		catch (Exception e)
		{
			logger.error("copyPropertiesToVO failed");
			logger.error(e.getLocalizedMessage(), e);
			throw new RpcException(ErrorCodeConst.ErrorCode99998);
		}
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Override
	public MerchantOrder createMerchantOrder(MerchantOrder merchantOrder)
	{
		logger.debug("createMerchantOrder start, merchantOrder [" + merchantOrder.toString() + "]");
		try
		{
			merchantOrder = orderDao.save(merchantOrder);
			logger.debug("createMerchantOrder end, merchantOrder [" + merchantOrder.toString() + "]");
			return merchantOrder;
		}
		catch (Exception e)
		{
			logger.error("createMerchantOrder failed, merchantOrder [" + merchantOrder.toString() + "]");
			logger.error(e.getLocalizedMessage(), e);
			throw new RpcException(ErrorCodeConst.ErrorCode99998);
		}
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Override
	public MerchantOrder updateMerchantOrder(MerchantOrder merchantOrder)
	{
		logger.debug("updateMerchantOrder start, merchantOrder [" + merchantOrder.toString() + "]");
		try
		{
			merchantOrder = orderDao.save(merchantOrder);
			logger.debug("updateMerchantOrder end, merchantOrder [" + merchantOrder.toString() + "]");
			return merchantOrder;
		}
		catch (Exception e)
		{
			logger.error("updateMerchantOrder failed, merchantOrder [" + merchantOrder.toString() + "]");
			logger.error(e.getLocalizedMessage(), e);
			throw new RpcException(ErrorCodeConst.ErrorCode99998);
		}
	}
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Override
	public MerchantOrder queryMerchantOrderByOrderId(String orderId)
	{
		logger.debug("queryMerchantOrderByOrderId start, orderId [" + orderId + "]");
		try
		{
			MerchantOrder order = orderDao.queryMerchantOrderByOrderId(orderId);
			logger.debug("queryMerchantOrderByOrderId end, orderId [" + orderId + "]");
			return order;
		}
		catch (RpcException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			logger.error("queryMerchantOrderByOrderId failed, orderId [" + orderId + "]");
			logger.error(e.getLocalizedMessage(), e);
			throw new RpcException(ErrorCodeConst.ErrorCode99998);
		}

	}
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Override
	public MerchantOrder queryMerchantOrderByMerchantOrderId(String merchantOrderId, long merchantId)
	{
		logger.debug("queryMerchantOrderByMerchantOrderId start, merchantOrderId [" + merchantOrderId + "] merchantId [" + merchantId + "]");
		try
		{
			MerchantOrder order = orderDao.queryMerchantOrderByMerchantOrderId(merchantOrderId,merchantId);
			logger.debug("queryMerchantOrderByMerchantOrderId end, merchantOrderId [" + merchantOrderId + "] merchantId [" + merchantId + "]");
			return order;
		}
		catch (RpcException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			logger.error("queryMerchantOrderByMerchantOrderId failed, merchantOrderId [" + merchantOrderId + "] merchantId [" + merchantId + "]");
			logger.error(e.getLocalizedMessage(), e);
			throw new RpcException(ErrorCodeConst.ErrorCode99998);
		}

	}

	// 清理超时未支付的商品订单
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Override
	public void updateProductOrderStatus()
	{
		logger.debug("updateProductOrderStatus start");
		try
		{
			// 获得符合条件的订单
			Date nowDate = new Date();
			List<MerchantOrder> orders = orderDao.queryMerchantOrderListByStatus(
					DateTimeHelper.getPlusDateByMins(nowDate, -30), CommonConstant.PayStatus.NOT_PAY);
			logger.debug("updateProductOrderStatus 获得符合条件的订单orders: " + orders);

			// 修改订单状态
			for (MerchantOrder merchantOrder : orders)
			{
				merchantOrder.setPayStatus(CommonConstant.PayStatus.FAILED);
				merchantOrder.setReturnCode("1111");
				merchantOrder.setReturnMessage("超时未支付");
				orderDao.save(merchantOrder);
			}

			logger.debug("updateProductOrderStatus end");
		}
		catch (RpcException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			logger.error("updateProductOrderStatus failed");
			logger.error(e.getLocalizedMessage(), e);
			throw new RpcException(ErrorCodeConst.ErrorCode99998);
		}

	}

}
