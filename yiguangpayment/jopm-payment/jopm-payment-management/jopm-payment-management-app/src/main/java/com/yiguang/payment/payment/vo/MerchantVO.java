package com.yiguang.payment.payment.vo;

import java.io.Serializable;
import java.math.BigDecimal;

public class MerchantVO implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private long id;

	private String name;

	private long adminUser;

	private String remark;

	private int status;

	private BigDecimal discount;

	private String key;

	private String notifyUrl;

	private String statusLabel;

	private String adminUserName;

	private String channelMerchantRelationIDs;

	public String getChannelMerchantRelationIDs()
	{
		return channelMerchantRelationIDs;
	}

	public void setChannelMerchantRelationIDs(String channelMerchantRelationIDs)
	{
		this.channelMerchantRelationIDs = channelMerchantRelationIDs;
	}

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

	public String getRemark()
	{
		return remark;
	}

	public void setRemark(String remark)
	{
		this.remark = remark;
	}

	public int getStatus()
	{
		return status;
	}

	public void setStatus(int status)
	{
		this.status = status;
	}

	public BigDecimal getDiscount()
	{
		return discount;
	}

	public void setDiscount(BigDecimal discount)
	{
		this.discount = discount;
	}

	public String getStatusLabel()
	{
		return statusLabel;
	}

	public void setStatusLabel(String statusLabel)
	{
		this.statusLabel = statusLabel;
	}

	public String getKey()
	{
		return key;
	}

	public void setKey(String key)
	{
		this.key = key;
	}

	public String getNotifyUrl()
	{
		return notifyUrl;
	}

	public void setNotifyUrl(String notifyUrl)
	{
		this.notifyUrl = notifyUrl;
	}

	public long getAdminUser()
	{
		return adminUser;
	}

	public void setAdminUser(long adminUser)
	{
		this.adminUser = adminUser;
	}

	public String getAdminUserName()
	{
		return adminUserName;
	}

	public void setAdminUserName(String adminUserName)
	{
		this.adminUserName = adminUserName;
	}

}
