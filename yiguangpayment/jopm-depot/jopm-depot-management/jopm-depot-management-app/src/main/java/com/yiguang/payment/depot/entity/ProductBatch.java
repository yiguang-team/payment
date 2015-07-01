package com.yiguang.payment.depot.entity;

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
@Table(name = "t_product_batch")
@SequenceGenerator(name = "seq_product_batch", sequenceName = "seq_product_batch")
public class ProductBatch implements Serializable
{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_product_batch")
	@Column(name = "id")
	private long id;

	@Column(name = "batch_id")
	private String batchId;

	@Column(name = "merchant_id")
	private long merchantId;

	@Column(name = "product_id")
	private long productId;

	@Column(name = "total_amt")
	private BigDecimal totalAmt;

	@Column(name = "total_count")
	private long totalCount;

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

	public String getBatchId()
	{
		return batchId;
	}

	public void setBatchId(String batchId)
	{
		this.batchId = batchId;
	}

	public BigDecimal getTotalAmt()
	{
		return totalAmt;
	}

	public void setTotalAmt(BigDecimal totalAmt)
	{
		this.totalAmt = totalAmt;
	}

	public long getTotalCount()
	{
		return totalCount;
	}

	public void setTotalCount(long totalCount)
	{
		this.totalCount = totalCount;
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

	public long getProductId()
	{
		return productId;
	}

	public void setProductId(long productId)
	{
		this.productId = productId;
	}

}
