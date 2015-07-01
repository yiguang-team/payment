package com.yiguang.payment.payment.order.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 商品订单实体
 * 
 * @author Shinalon
 * 
 */
@Entity
@Table(name = "t_merchant_order")
@SequenceGenerator(name = "seq_merchant_order", sequenceName = "seq_merchant_order", initialValue = 2000, allocationSize = 1)
public class MerchantOrder implements Serializable
{

	@Override
	public String toString()
	{
		return "MerchantOrder [id=" + id + ", orderId=" + orderId + ", username=" + username + ", requestIp="
				+ requestIp + ", chargingType=" + chargingType + "" + ", mobile=" + mobile + ", channelId=" + channelId
				+ ", provinceId=" + provinceId + "" + ", cityId=" + cityId + ", productId=" + productId
				+ ", faceAmount=" + faceAmount + ", payAmount=" + payAmount + ", deliveryAmount=" + deliveryAmount + ""
				+ ", payStatus=" + payStatus + ", deliveryStatus=" + deliveryStatus + ", deliveryNo=" + deliveryNo
				+ "]";
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_merchant_order")
	@Column(name = "id")
	private long id;

	@Column(name = "order_id")
	private String orderId;

	@Column(name = "merchant_order_id")
	private String merchantOrderId;
	
	@Column(name = "request_time")
	private Date requestTime;

	@Column(name = "complete_time")
	private Date completeTime;

	@Column(name = "delivery_request_time")
	private Date deliveryRequestTime;

	@Column(name = "delivery_complete_time")
	private Date deliveryCompleteTime;

	@Column(name = "mobile")
	private String mobile;

	@Column(name = "carrier_id")
	private long carrierId;
	
	@Column(name = "channel_id")
	private long channelId;

	@Column(name = "merchant_id")
	private long merchantId;

	@Column(name = "province_id")
	private String provinceId;

	@Column(name = "city_id")
	private String cityId;

	@Column(name = "product_id")
	private long productId;

	@Column(name = "face_amount")
	private BigDecimal faceAmount;

	@Column(name = "pay_amount")
	private BigDecimal payAmount;

	@Column(name = "delivery_amount")
	private BigDecimal deliveryAmount;

	@Column(name = "pay_status")
	private int payStatus;

	@Column(name = "delivery_status")
	private int deliveryStatus;

	@Column(name = "delivery_no")
	private String deliveryNo;

	@Column(name = "username")
	private String username;

	@Column(name = "request_ip")
	private String requestIp;

	@Column(name = "charging_point_id")
	private long chargingPointId;

	@Column(name = "charging_type")
	private int chargingType;

	@Column(name = "return_code")
	private String returnCode;

	@Column(name = "return_message")
	private String returnMessage;

	@Column(name = "delivery_return_code")
	private String deliveryReturnCode;

	@Column(name = "delivery_return_message")
	private String deliveryReturnMessage;

	@Column(name = "ext1")
	private String ext1;

	@Column(name = "ext2")
	private String ext2;

	@Column(name = "ext3")
	private String ext3;

	@Column(name = "ext4")
	private String ext4;

	@Column(name = "ext5")
	private String ext5;

	@Column(name = "channel_type")
	private int channelType;
	
	@Column(name = "subject")
	private String subject;
	
	@Column(name = "description")
	private String description;
	
	@Column(name = "notify_status")
	private int notifyStatus;

	@Column(name = "notify_url")
	private String notifyUrl;
	
	@Column(name = "show_url")
	private String showUrl;
	
	@Column(name = "channel_trade_no")
	private String channelTradeNo;
	
	@Column(name = "smscode")
	private String smscode;
	
	@Column(name = "order_timestamp")
	private String orderTimestamp;
	
	@Column(name = "pay_timestamp")
	private String payTimestamp;
	
	@Column(name = "charging_code")
	private String chargingCode;
	
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

	public long getCarrierId()
	{
		return carrierId;
	}

	public void setCarrierId(long carrierId)
	{
		this.carrierId = carrierId;
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

	public String getMerchantOrderId()
	{
		return merchantOrderId;
	}

	public void setMerchantOrderId(String merchantOrderId)
	{
		this.merchantOrderId = merchantOrderId;
	}

	public Date getRequestTime()
	{
		return requestTime;
	}

	public void setRequestTime(Date requestTime)
	{
		this.requestTime = requestTime;
	}

	public Date getCompleteTime()
	{
		return completeTime;
	}

	public void setCompleteTime(Date completeTime)
	{
		this.completeTime = completeTime;
	}

	public Date getDeliveryRequestTime()
	{
		return deliveryRequestTime;
	}

	public void setDeliveryRequestTime(Date deliveryRequestTime)
	{
		this.deliveryRequestTime = deliveryRequestTime;
	}

	public Date getDeliveryCompleteTime()
	{
		return deliveryCompleteTime;
	}

	public void setDeliveryCompleteTime(Date deliveryCompleteTime)
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

	public long getChannelId()
	{
		return channelId;
	}

	public void setChannelId(long channelId)
	{
		this.channelId = channelId;
	}

	public long getMerchantId()
	{
		return merchantId;
	}

	public void setMerchantId(long merchantId)
	{
		this.merchantId = merchantId;
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

	public long getChargingPointId()
	{
		return chargingPointId;
	}

	public void setChargingPointId(long chargingPointId)
	{
		this.chargingPointId = chargingPointId;
	}

	public int getChargingType()
	{
		return chargingType;
	}

	public void setChargingType(int chargingType)
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

	public int getChannelType()
	{
		return channelType;
	}

	public void setChannelType(int channelType)
	{
		this.channelType = channelType;
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

	public String getChargingCode()
	{
		return chargingCode;
	}

	public void setChargingCode(String chargingCode)
	{
		this.chargingCode = chargingCode;
	}
	
}
