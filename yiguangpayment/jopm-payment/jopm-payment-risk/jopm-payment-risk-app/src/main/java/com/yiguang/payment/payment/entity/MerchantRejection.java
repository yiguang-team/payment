package com.yiguang.payment.payment.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 商户互斥
 * 
 * @author ediosn
 * 
 */
@Entity
@Table(name = "t_rule_merchant_rejection")
@SequenceGenerator(name = "seq_rule_merchant_rejection", sequenceName = "seq_rule_merchant_rejection", initialValue = 2000, allocationSize = 1)
public class MerchantRejection implements Serializable
{

	private static final long serialVersionUID = 1L;

	@Override
	public String toString()
	{
		return "MerchantRejection [id=" + id + ", merchantA=" + merchantA + "merchantB=" + merchantB + "]";
	}

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_rule_merchant_rejection")
	@Column(name = "id")
	private long id;

	@Column(name = "merchant_a")
	private long merchantA;

	@Column(name = "merchant_b")
	private long merchantB;

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

	public long getMerchantA()
	{
		return merchantA;
	}

	public void setMerchantA(long merchantA)
	{
		this.merchantA = merchantA;
	}

	public long getMerchantB()
	{
		return merchantB;
	}

	public void setMerchantB(long merchantB)
	{
		this.merchantB = merchantB;
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

}
