package com.yiguang.payment.payment.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 渠道计费编码
 * 
 * @author Shinalon
 */
@Entity
@Table(name = "t_channel_charging_code")
@SequenceGenerator(name = "seq_channel_charging_code", sequenceName = "seq_channel_charging_code", initialValue = 2000, allocationSize = 1)
public class ChannelChargingCode implements Serializable
{

	private static final long serialVersionUID = 1L;

	@Override
	public String toString()
	{
		return "Channel [id=" + id + ", channelId=" + channelId + ", chargingAmount=" + chargingAmount
				+ ", chargingCode=" + chargingCode + ", status=" + status + ", remark=" + remark + "]";
	}

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_channel_charging_code")
	@Column(name = "id")
	private long id;

	@Column(name = "channel_id")
	private long channelId;

	@Column(name = "charging_amount")
	private BigDecimal chargingAmount;

	@Column(name = "charging_code")
	private String chargingCode;

	@Column(name = "status", length = 2)
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

}
