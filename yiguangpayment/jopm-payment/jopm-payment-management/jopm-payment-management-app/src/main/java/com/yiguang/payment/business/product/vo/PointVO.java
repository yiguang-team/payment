package com.yiguang.payment.business.product.vo;

import java.io.Serializable;
import java.math.BigDecimal;

public class PointVO implements Serializable
{

	private static final long serialVersionUID = 1L;

	private long id;

	private String name;

	private long merchantId;

	private long productId;

	private String provinceId;

	private String cityId;

	private int status;

	private String statusLabel;

	private String merchantLabel;

	private String productLabel;

	private String provinceLabel;

	private String cityLabel;

	private String remark;

	private BigDecimal payAmount;

	private BigDecimal deliveryAmount;

	private BigDecimal faceAmount;

	private int chargingType;

	private String chargingTypeLabel;

	private String inStock;

	private String pointChannelRelationIDs;

	private String channelLabel;

	private String chargingCode;
	
	public String getInStock()
	{
		return inStock;
	}

	public void setInStock(String inStock)
	{
		this.inStock = inStock;
	}

	public long getId()
	{
		return id;
	}

	public void setId(long id)
	{
		this.id = id;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public long getMerchantId()
	{
		return merchantId;
	}

	public void setMerchantId(long merchantId)
	{
		this.merchantId = merchantId;
	}

	public long getProductId()
	{
		return productId;
	}

	public void setProductId(long productId)
	{
		this.productId = productId;
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

	public int getStatus()
	{
		return status;
	}

	public void setStatus(int status)
	{
		this.status = status;
	}

	public String getMerchantLabel()
	{
		return merchantLabel;
	}

	public void setMerchantLabel(String merchantLabel)
	{
		this.merchantLabel = merchantLabel;
	}

	public String getProductLabel()
	{
		return productLabel;
	}

	public void setProductLabel(String productLabel)
	{
		this.productLabel = productLabel;
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

	public BigDecimal getFaceAmount()
	{
		return faceAmount;
	}

	public void setFaceAmount(BigDecimal faceAmount)
	{
		this.faceAmount = faceAmount;
	}

	public String getStatusLabel()
	{
		return statusLabel;
	}

	public void setStatusLabel(String statusLabel)
	{
		this.statusLabel = statusLabel;
	}

	public String getRemark()
	{
		return remark;
	}

	public void setRemark(String remark)
	{
		this.remark = remark;
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

	public int getChargingType()
	{
		return chargingType;
	}

	public void setChargingType(int chargingType)
	{
		this.chargingType = chargingType;
	}

	public String getChargingTypeLabel()
	{
		return chargingTypeLabel;
	}

	public void setChargingTypeLabel(String chargingTypeLabel)
	{
		this.chargingTypeLabel = chargingTypeLabel;
	}

	public String getPointChannelRelationIDs()
	{
		return pointChannelRelationIDs;
	}

	public void setPointChannelRelationIDs(String pointChannelRelationIDs)
	{
		this.pointChannelRelationIDs = pointChannelRelationIDs;
	}

	public String getChannelLabel()
	{
		return channelLabel;
	}

	public void setChannelLabel(String channelLabel)
	{
		this.channelLabel = channelLabel;
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
