package com.yiguang.payment.payment.vo;

import java.io.Serializable;

public class MerchantRejectionVO implements Serializable
{

	private static final long serialVersionUID = 1L;

	private long id;

	private long merchantA;

	private long merchantB;

	private int status;

	private String remark;

	private String statusLabel;

	private String merchantALabel;

	private String merchantBLabel;

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

	public String getMerchantALabel()
	{
		return merchantALabel;
	}

	public void setMerchantALabel(String merchantALabel)
	{
		this.merchantALabel = merchantALabel;
	}

	public String getMerchantBLabel()
	{
		return merchantBLabel;
	}

	public void setMerchantBLabel(String merchantBLabel)
	{
		this.merchantBLabel = merchantBLabel;
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

	public String getStatusLabel()
	{
		return statusLabel;
	}

	public void setStatusLabel(String statusLabel)
	{
		this.statusLabel = statusLabel;
	}

}
