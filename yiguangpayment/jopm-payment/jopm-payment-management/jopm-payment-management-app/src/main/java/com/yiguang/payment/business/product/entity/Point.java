package com.yiguang.payment.business.product.entity;

/**
 * 计费点实体
 * @author ediosn
 */
import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "t_product_charging_point")
@SequenceGenerator(name = "seq_product_charging_point", sequenceName = "seq_product_charging_point", initialValue = 2000, allocationSize = 1)
public class Point implements Serializable
{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_product_charging_point")
	@Column(name = "id")
	private long id;

	@Column(name = "name")
	private String name;

	@Column(name = "merchant_id")
	private long merchantId;

	@Column(name = "product_id")
	private long productId;

	@Column(name = "province_id")
	private String provinceId;

	@Column(name = "city_id")
	private String cityId;

	@Column(name = "face_amount")
	private BigDecimal faceAmount;

	@Column(name = "status")
	private int status;

	@Column(name = "remark")
	private String remark;

	@Column(name = "pay_amount")
	private BigDecimal payAmount;

	@Column(name = "delivery_amount")
	private BigDecimal deliveryAmount;

	@Column(name = "charging_type")
	private int chargingType;

	@Column(name = "charging_code")
	private String chargingCode;
	
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

	public BigDecimal getFaceAmount()
	{
		return faceAmount;
	}

	public void setFaceAmount(BigDecimal faceAmount)
	{
		this.faceAmount = faceAmount;
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

	public String getChargingCode()
	{
		return chargingCode;
	}

	public void setChargingCode(String chargingCode)
	{
		this.chargingCode = chargingCode;
	}
}
