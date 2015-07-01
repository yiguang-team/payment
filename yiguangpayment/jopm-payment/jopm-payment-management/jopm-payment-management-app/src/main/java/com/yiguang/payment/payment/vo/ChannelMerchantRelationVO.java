package com.yiguang.payment.payment.vo;

import java.io.Serializable;
import java.math.BigDecimal;

public class ChannelMerchantRelationVO implements Serializable
{

	private static final long serialVersionUID = 1L;

	private long id;

	private long merchantId;

	private long channelId;

	private int status;

	private String remark;

	private String merchantLabel;

	private String channelLabel;

	public String getMerchantLabel()
	{
		return merchantLabel;
	}

	public void setMerchantLabel(String merchantLabel)
	{
		this.merchantLabel = merchantLabel;
	}

	public String getChannelLabel()
	{
		return channelLabel;
	}

	public void setChannelLabel(String channelLabel)
	{
		this.channelLabel = channelLabel;
	}

	public long getId()
	{
		return id;
	}

	public void setId(long id)
	{
		this.id = id;
	}

	public long getMerchantId()
	{
		return merchantId;
	}

	public void setMerchantId(long merchantId)
	{
		this.merchantId = merchantId;
	}

	public long getChannelId()
	{
		return channelId;
	}

	public void setChannelId(long channelId)
	{
		this.channelId = channelId;
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
