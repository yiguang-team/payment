package com.yiguang.payment.payment.order.vo;

import java.io.Serializable;
import java.math.BigDecimal;

public class MerchantOrderVO implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private long id;

	private String orderId;

	private String merchantOrderId;
	
	private String requestTime;

	private String completeTime;

	private String deliveryRequestTime;

	private String deliveryCompleteTime;

	private String mobile;

	private long merchantId;

	private long channelId;

	private long carrierId;
	
	private String provinceId;

	private String cityId;

	private long productId;

	private BigDecimal faceAmount;

	private BigDecimal payAmount;

	private BigDecimal deliveryAmount;

	private int payStatus;

	private int deliveryStatus;

	private String deliveryNo;

	private String username;

	private String requestIp;

	private long chargingPointId;

	private long chargingType;

	private String pointLabel;

	private String payStatusLabel;

	private String deliveryStatusLabel;

	private String orderStatusLabel;

	private String merchantLabel;
	
	private String channelLabel;
	
	private String carrierLabel;
	
	private String provinceLabel;

	private String cityLabel;

	private String productLabel;

	private String chargingTypeLabel;

	private String ext1;

	private String ext2;

	private String ext3;

	private String ext4;

	private String ext5;

	private String returnCode;

	private String returnMessage;

	private String deliveryReturnCode;

	private String deliveryReturnMessage;
	
	private int channelType;
	private String channelTypeLabel;
	private String subject;
	
	private String description;
	
	private int notifyStatus;
	private String notifyStatusLabel;
	private String notifyUrl;
	private String showUrl;
	private String channelTradeNo;
	private String smscode;
	private String orderTimestamp;
	private String payTimestamp;

	public String getOrderTimestamp()
	{
		return orderTimestamp;
	}

	public void setOrderTimestamp(String orderTimestamp)
	{
		this.orderTimestamp = orderTimestamp;
	}

	public String getPayTimestamp()
	{
		return payTimestamp;
	}

	public void setPayTimestamp(String payTimestamp)
	{
		this.payTimestamp = payTimestamp;
	}

	public String getMerchantOrderId()
	{
		return merchantOrderId;
	}

	public void setMerchantOrderId(String merchantOrderId)
	{
		this.merchantOrderId = merchantOrderId;
	}

	public String getPointLabel()
	{
		return pointLabel;
	}

	public void setPointLabel(String pointLabel)
	{
		this.pointLabel = pointLabel;
	}

	public long getChargingPointId()
	{
		return chargingPointId;
	}

	public void setChargingPointId(long chargingPointId)
	{
		this.chargingPointId = chargingPointId;
	}

	public String getChannelLabel()
	{
		return channelLabel;
	}

	public void setChannelLabel(String channelLabel)
	{
		this.channelLabel = channelLabel;
	}

	public String getProvinceLabel()
	{
		return provinceLabel;
	}

	public void setProvinceLabel(String provinceLabel)
	{
		this.provinceLabel = provinceLabel;
	}

	public String getCityLabel()
	{
		return cityLabel;
	}

	public void setCityLabel(String cityLabel)
	{
		this.cityLabel = cityLabel;
	}

	public String getProductLabel()
	{
		return productLabel;
	}

	public void setProductLabel(String productLabel)
	{
		this.productLabel = productLabel;
	}

	public String getChargingTypeLabel()
	{
		return chargingTypeLabel;
	}

	public void setChargingTypeLabel(String chargingTypeLabel)
	{
		this.chargingTypeLabel = chargingTypeLabel;
	}

	public String getPayStatusLabel()
	{
		return payStatusLabel;
	}

	public void setPayStatusLabel(String payStatusLabel)
	{
		this.payStatusLabel = payStatusLabel;
	}

	public String getDeliveryStatusLabel()
	{
		return deliveryStatusLabel;
	}

	public void setDeliveryStatusLabel(String deliveryStatusLabel)
	{
		this.deliveryStatusLabel = deliveryStatusLabel;
	}

	public String getOrderStatusLabel()
	{
		return orderStatusLabel;
	}

	public void setOrderStatusLabel(String orderStatusLabel)
	{
		this.orderStatusLabel = orderStatusLabel;
	}

	public long getId()
	{
		return id;
	}

	public void setId(long id)
	{
		this.id = id;
	}

	public String getOrderId()
	{
		return orderId;
	}

	public void setOrderId(String orderId)
	{
		this.orderId = orderId;
	}

	public String getRequestTime()
	{
		return requestTime;
	}

	public void setRequestTime(String requestTime)
	{
		this.requestTime = requestTime;
	}

	public String getCompleteTime()
	{
		return completeTime;
	}

	public void setCompleteTime(String completeTime)
	{
		this.completeTime = completeTime;
	}

	public String getDeliveryRequestTime()
	{
		return deliveryRequestTime;
	}

	public void setDeliveryRequestTime(String deliveryRequestTime)
	{
		this.deliveryRequestTime = deliveryRequestTime;
	}

	public String getDeliveryCompleteTime()
	{
		return deliveryCompleteTime;
	}

	public void setDeliveryCompleteTime(String deliveryCompleteTime)
	{
		this.deliveryCompleteTime = deliveryCompleteTime;
	}

	public String getMobile()
	{
		return mobile;
	}

	public void setMobile(String mobile)
	{
		this.mobile = mobile;
	}

	public long getMerchantId()
	{
		return merchantId;
	}

	public void setMerchantId(long merchantId)
	{
		this.merchantId = merchantId;
	}

	public String getMerchantLabel()
	{
		return merchantLabel;
	}

	public void setMerchantLabel(String merchantLabel)
	{
		this.merchantLabel = merchantLabel;
	}

	public long getChannelId()
	{
		return channelId;
	}

	public void setChannelId(long channelId)
	{
		this.channelId = channelId;
	}

	public String getProvinceId()
	{
		return provinceId;
	}

	public void setProvinceId(String provinceId)
	{
		this.provinceId = provinceId;
	}

	public String getCityId()
	{
		return cityId;
	}

	public void setCityId(String cityId)
	{
		this.cityId = cityId;
	}

	public long getProductId()
	{
		return productId;
	}

	public void setProductId(long productId)
	{
		this.productId = productId;
	}

	public BigDecimal getFaceAmount()
	{
		return faceAmount;
	}

	public void setFaceAmount(BigDecimal faceAmount)
	{
		this.faceAmount = faceAmount;
	}

	public BigDecimal getPayAmount()
	{
		return payAmount;
	}

	public void setPayAmount(BigDecimal payAmount)
	{
		this.payAmount = payAmount;
	}

	public BigDecimal getDeliveryAmount()
	{
		return deliveryAmount;
	}

	public void setDeliveryAmount(BigDecimal deliveryAmount)
	{
		this.deliveryAmount = deliveryAmount;
	}

	public int getPayStatus()
	{
		return payStatus;
	}

	public void setPayStatus(int payStatus)
	{
		this.payStatus = payStatus;
	}

	public int getDeliveryStatus()
	{
		return deliveryStatus;
	}

	public void setDeliveryStatus(int deliveryStatus)
	{
		this.deliveryStatus = deliveryStatus;
	}

	public String getDeliveryNo()
	{
		return deliveryNo;
	}

	public void setDeliveryNo(String deliveryNo)
	{
		this.deliveryNo = deliveryNo;
	}

	public String getUsername()
	{
		return username;
	}

	public void setUsername(String username)
	{
		this.username = username;
	}

	public String getRequestIp()
	{
		return requestIp;
	}

	public void setRequestIp(String requestIp)
	{
		this.requestIp = requestIp;
	}

	public long getChargingType()
	{
		return chargingType;
	}

	public void setChargingType(long chargingType)
	{
		this.chargingType = chargingType;
	}

	public String getExt1()
	{
		return ext1;
	}

	public void setExt1(String ext1)
	{
		this.ext1 = ext1;
	}

	public String getExt2()
	{
		return ext2;
	}

	public void setExt2(String ext2)
	{
		this.ext2 = ext2;
	}

	public String getExt3()
	{
		return ext3;
	}

	public void setExt3(String ext3)
	{
		this.ext3 = ext3;
	}

	public String getExt4()
	{
		return ext4;
	}

	public void setExt4(String ext4)
	{
		this.ext4 = ext4;
	}

	public String getExt5()
	{
		return ext5;
	}

	public void setExt5(String ext5)
	{
		this.ext5 = ext5;
	}

	public String getReturnCode()
	{
		return returnCode;
	}

	public void setReturnCode(String returnCode)
	{
		this.returnCode = returnCode;
	}

	public String getReturnMessage()
	{
		return returnMessage;
	}

	public void setReturnMessage(String returnMessage)
	{
		this.returnMessage = returnMessage;
	}

	public String getDeliveryReturnCode()
	{
		return deliveryReturnCode;
	}

	public void setDeliveryReturnCode(String deliveryReturnCode)
	{
		this.deliveryReturnCode = deliveryReturnCode;
	}

	public String getDeliveryReturnMessage()
	{
		return deliveryReturnMessage;
	}

	public void setDeliveryReturnMessage(String deliveryReturnMessage)
	{
		this.deliveryReturnMessage = deliveryReturnMessage;
	}

	public long getCarrierId()
	{
		return carrierId;
	}

	public void setCarrierId(long carrierId)
	{
		this.carrierId = carrierId;
	}

	public String getCarrierLabel()
	{
		return carrierLabel;
	}

	public void setCarrierLabel(String carrierLabel)
	{
		this.carrierLabel = carrierLabel;
	}

	public int getChannelType()
	{
		return channelType;
	}

	public void setChannelType(int channelType)
	{
		this.channelType = channelType;
	}

	public String getChannelTypeLabel()
	{
		return channelTypeLabel;
	}

	public void setChannelTypeLabel(String channelTypeLabel)
	{
		this.channelTypeLabel = channelTypeLabel;
	}

	public String getSubject()
	{
		return subject;
	}

	public void setSubject(String subject)
	{
		this.subject = subject;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public int getNotifyStatus()
	{
		return notifyStatus;
	}

	public void setNotifyStatus(int notifyStatus)
	{
		this.notifyStatus = notifyStatus;
	}

	public String getNotifyStatusLabel()
	{
		return notifyStatusLabel;
	}

	public void setNotifyStatusLabel(String notifyStatusLabel)
	{
		this.notifyStatusLabel = notifyStatusLabel;
	}

	public String getNotifyUrl()
	{
		return notifyUrl;
	}

	public void setNotifyUrl(String notifyUrl)
	{
		this.notifyUrl = notifyUrl;
	}

	public String getChannelTradeNo()
	{
		return channelTradeNo;
	}

	public void setChannelTradeNo(String channelTradeNo)
	{
		this.channelTradeNo = channelTradeNo;
	}

	public String getShowUrl()
	{
		return showUrl;
	}

	public void setShowUrl(String showUrl)
	{
		this.showUrl = showUrl;
	}

	public String getSmscode()
	{
		return smscode;
	}

	public void setSmscode(String smscode)
	{
		this.smscode = smscode;
	}

}
