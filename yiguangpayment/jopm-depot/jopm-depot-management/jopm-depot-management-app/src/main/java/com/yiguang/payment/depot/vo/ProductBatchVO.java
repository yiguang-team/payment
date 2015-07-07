package com.yiguang.payment.depot.vo;

import java.io.Serializable;
import java.math.BigDecimal;

public class ProductBatchVO implements Serializable
{

	private static final long serialVersionUID = 1L;

	private long id;

	private String batchId;

	private long carrierId;

	private long merchantId;
	
	private String merchantLabel;

	private long productId;

	private String productLabel;

	private BigDecimal totalAmt;

	private long totalCount;

	private int status;

	private String statusLabel;

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

	public long getCarrierId()
	{
		return carrierId;
	}

	public void setCarrierId(long carrierId)
	{
		this.carrierId = carrierId;
	}

	public long getProductId()
	{
		return productId;
	}

	public void setProductId(long productId)
	{
		this.productId = productId;
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

	public String getStatusLabel()
	{
		return statusLabel;
	}

	public void setStatusLabel(String statusLabel)
	{
		this.statusLabel = statusLabel;
	}

	public long getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(long merchantId) {
		this.merchantId = merchantId;
	}

}
