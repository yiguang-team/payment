package com.yiguang.payment.depot.order.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "t_extract_record")
@SequenceGenerator(name = "seq_extract_record", sequenceName = "seq_extract_record")
public class DepotOrder implements Serializable
{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_extract_record")
	@Column(name = "id")
	private long id;

	@Column(name = "charging_point_id")
	private long chargingPointId;

	@Column(name = "product_id")
	private long productId;

	@Column(name = "province_id")
	private long provinceId;

	@Column(name = "city_id")
	private long cityId;

	@Column(name = "face_amount")
	private long faceAmount;

	@Column(name = "status")
	private int status;

	@Column(name = "remark")
	private String remark;

	@Column(name = "request_time")
	private Date requestTime;

	@Column(name = "request_ip")
	private String requestIp;

	@Column(name = "order_id")
	private String orderId;

	@Column(name = "return_code")
	private String returnCode;

	@Column(name = "return_message")
	private String returnMessage;

	@Column(name = "extract_type")
	private int extractType;

	@Column(name = "extract_user")
	private String extractUser;

	@Column(name = "merchant_id")
	private long merchantId;

	@Column(name = "pay_amount")
	private long payAmount;

	@Column(name = "extract_count")
	private long extractCount;

	public long getId()
	{
		return id;
	}

	public void setId(long id)
	{
		this.id = id;
	}

	public long getChargingPointId()
	{
		return chargingPointId;
	}

	public void setChargingPointId(long chargingPointId)
	{
		this.chargingPointId = chargingPointId;
	}

	public long getProductId()
	{
		return productId;
	}

	public void setProductId(long productId)
	{
		this.productId = productId;
	}

	public long getProvinceId()
	{
		return provinceId;
	}

	public void setProvinceId(long provinceId)
	{
		this.provinceId = provinceId;
	}

	public long getCityId()
	{
		return cityId;
	}

	public void setCityId(long cityId)
	{
		this.cityId = cityId;
	}

	public long getFaceAmount()
	{
		return faceAmount;
	}

	public void setFaceAmount(long faceAmount)
	{
		this.faceAmount = faceAmount;
	}

	public int getStatus()
	{
		return status;
	}

	public void setStatus(int status)
	{
		this.status = status;
	}

	public String getRemark()
	{
		return remark;
	}

	public void setRemark(String remark)
	{
		this.remark = remark;
	}

	public Date getRequestTime()
	{
		return requestTime;
	}

	public void setRequestTime(Date requestTime)
	{
		this.requestTime = requestTime;
	}

	public String getRequestIp()
	{
		return requestIp;
	}

	public void setRequestIp(String requestIp)
	{
		this.requestIp = requestIp;
	}

	public String getOrderId()
	{
		return orderId;
	}

	public void setOrderId(String orderId)
	{
		this.orderId = orderId;
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

	public int getExtractType()
	{
		return extractType;
	}

	public void setExtractType(int extractType)
	{
		this.extractType = extractType;
	}

	public String getExtractUser()
	{
		return extractUser;
	}

	public void setExtractUser(String extractUser)
	{
		this.extractUser = extractUser;
	}

	public long getMerchantId()
	{
		return merchantId;
	}

	public void setMerchantId(long merchantId)
	{
		this.merchantId = merchantId;
	}

	public long getPayAmount()
	{
		return payAmount;
	}

	public void setPayAmount(long payAmount)
	{
		this.payAmount = payAmount;
	}

	public long getExtractCount()
	{
		return extractCount;
	}

	public void setExtractCount(long extractCount)
	{
		this.extractCount = extractCount;
	}

}
