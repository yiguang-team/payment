package com.yiguang.payment.business.product.entity;

/**
 * 产品实体
 * @author ediosn
 * 
 */
import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "t_product")
@SequenceGenerator(name = "seq_product", sequenceName = "seq_product", initialValue = 2000, allocationSize = 1)
public class Product implements Serializable
{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_product")
	@Column(name = "id")
	private long id;

	@Column(name = "name")
	private String name;

	@Column(name = "merchant_id")
	private long merchantId;

	@Column(name = "type")
	private int type;

	@Column(name = "status")
	private int status;

	@Column(name = "remark")
	private String remark;

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

	public int getType()
	{
		return type;
	}

	public void setType(int type)
	{
		this.type = type;
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

	public long getMerchantId()
	{
		return merchantId;
	}

	public void setMerchantId(long merchantId)
	{
		this.merchantId = merchantId;
	}

}
