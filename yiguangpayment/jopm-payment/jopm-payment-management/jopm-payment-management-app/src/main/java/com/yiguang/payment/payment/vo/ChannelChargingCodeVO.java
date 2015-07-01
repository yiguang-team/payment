package com.yiguang.payment.payment.vo;

import java.io.Serializable;
import java.math.BigDecimal;

public class ChannelChargingCodeVO implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private long id;

	private long channelId;
	//
	private BigDecimal chargingAmount;
	// 计费编码
	private String chargingCode;

	private int status;

	private String remark;

	private String statusLabel;

	private String channelLabel;

	public long getId()
	{
		return id;
	}

	public void setId(long id)
	{
		this.id = id;
	}

	public long getChannelId()
	{
		return channelId;
	}

	public void setChannelId(long channelId)
	{
		this.channelId = channelId;
	}

	public BigDecimal getChargingAmount()
	{
		return chargingAmount;
	}

	public void setChargingAmount(BigDecimal chargingAmount)
	{
		this.chargingAmount = chargingAmount;
	}

	public String getChargingCode()
	{
		return chargingCode;
	}

	public void setChargingCode(String chargingCode)
	{
		this.chargingCode = chargingCode;
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

	public String getChannelLabel()
	{
		return channelLabel;
	}

	public void setChannelLabel(String channelLabel)
	{
		this.channelLabel = channelLabel;
	}

}
