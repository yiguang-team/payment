package com.yiguang.payment.common.numsection.vo;

import java.io.Serializable;
import java.util.Date;

public class CarrierInfoVO implements Serializable
{

	private static final long serialVersionUID = 1L;

	private long id;

	private String carrierNo;

	private String carrierName;

	private Date createTime;

	private String updateName;

	private Date updateTime;

	private int status;

	private int carrierType;

	private String statusLabel;

	private String typeLabel;

	private String carrierTypeLabel;

	public long getId()
	{
		return id;
	}

	public void setId(long id)
	{
		this.id = id;
	}

	public String getStatusLabel()
	{
		return statusLabel;
	}

	public void setStatusLabel(String statusLabel)
	{
		this.statusLabel = statusLabel;
	}

	public String getTypeLabel()
	{
		return typeLabel;
	}

	public void setTypeLabel(String typeLabel)
	{
		this.typeLabel = typeLabel;
	}

	public String getCarrierNo()
	{
		return carrierNo;
	}

	public void setCarrierNo(String carrierNo)
	{
		this.carrierNo = carrierNo;
	}

	public String getCarrierName()
	{
		return carrierName;
	}

	public void setCarrierName(String carrierName)
	{
		this.carrierName = carrierName;
	}

	public Date getCreateTime()
	{
		return createTime;
	}

	public void setCreateTime(Date createTime)
	{
		this.createTime = createTime;
	}

	public String getUpdateName()
	{
		return updateName;
	}

	public void setUpdateName(String updateName)
	{
		this.updateName = updateName;
	}

	public Date getUpdateTime()
	{
		return updateTime;
	}

	public void setUpdateTime(Date updateTime)
	{
		this.updateTime = updateTime;
	}

	public int getStatus()
	{
		return status;
	}

	public void setStatus(int status)
	{
		this.status = status;
	}

	public int getCarrierType()
	{
		return carrierType;
	}

	public void setCarrierType(int carrierType)
	{
		this.carrierType = carrierType;
	}

	public String getCarrierTypeLabel()
	{
		return carrierTypeLabel;
	}

	public void setCarrierTypeLabel(String carrierTypeLabel)
	{
		this.carrierTypeLabel = carrierTypeLabel;
	}

}
