package com.yiguang.payment.payment.vo;

import java.io.Serializable;


public class BasicRuleVO implements Serializable
{
	private static final long serialVersionUID = 1L;

	private long id;

	private int timeType;// 时间限制方式  单位限制  时段限制
	private String timeTypeLabel;
	private int timeUnit;// 单位限制的时间单位有  每月 每天 每时
	private String timeUnitLabel;
	private String startTime;// 时段限制起始时间
	
	private String endTime;// 时段限制结束时间

	private int limitType;//限制方式  笔数  金额
	private String limitTypeLabel;
	private String volume;// 限制值
	
	private int startUnit;//
	private int endUnit;//

	private long relativeValue;
	private int relativeUnit;
	private String relativeUnitLabel;
	/**
	 * 以下字段   -1代表当前值，0代表不限制  其他值对应数据源条件、
	 */

	private long channelId;// 渠道
	private String channelLabel;
	private String provinceId;// 省
	private String provinceLabel;
	private String cityId;// 市
	private String cityLabel;
	private long merchantId;
	private String merchantLabel;
	private long productId;
	private String productLabel;
	private long pointId;
	private String pointLabel;
	
	/**
	 * 是否单号码限制
	 */
	private String mobile;//号码
	private String ip;//IP
	private String username;//
	
	private int action;//规则动作 
	private String actionLabel;
	private int status;//状态
	private String statusLabel;
	private String remark;//说明

	public long getId()
	{
		return id;
	}

	public void setId(long id)
	{
		this.id = id;
	}

	public int getTimeType()
	{
		return timeType;
	}

	public void setTimeType(int timeType)
	{
		this.timeType = timeType;
	}

	public int getTimeUnit()
	{
		return timeUnit;
	}

	public void setTimeUnit(int timeUnit)
	{
		this.timeUnit = timeUnit;
	}

	public String getStartTime()
	{
		return startTime;
	}

	public void setStartTime(String startTime)
	{
		this.startTime = startTime;
	}

	public String getEndTime()
	{
		return endTime;
	}

	public void setEndTime(String endTime)
	{
		this.endTime = endTime;
	}

	public int getLimitType()
	{
		return limitType;
	}

	public void setLimitType(int limitType)
	{
		this.limitType = limitType;
	}

	public String getVolume()
	{
		return volume;
	}

	public void setVolume(String volume)
	{
		this.volume = volume;
	}

	public long getChannelId()
	{
		return channelId;
	}

	public void setChannelId(long channelId)
	{
		this.channelId = channelId;
	}

	public String getProvinceId()
	{
		return provinceId;
	}

	public void setProvinceId(String provinceId)
	{
		this.provinceId = provinceId;
	}

	public String getCityId()
	{
		return cityId;
	}

	public void setCityId(String cityId)
	{
		this.cityId = cityId;
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

	public long getPointId()
	{
		return pointId;
	}

	public void setPointId(long pointId)
	{
		this.pointId = pointId;
	}

	public String getMobile()
	{
		return mobile;
	}

	public void setMobile(String mobile)
	{
		this.mobile = mobile;
	}

	public String getIp()
	{
		return ip;
	}

	public void setIp(String ip)
	{
		this.ip = ip;
	}

	public String getUsername()
	{
		return username;
	}

	public void setUsername(String username)
	{
		this.username = username;
	}

	public int getAction()
	{
		return action;
	}

	public void setAction(int action)
	{
		this.action = action;
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

	public String getTimeTypeLabel()
	{
		return timeTypeLabel;
	}

	public void setTimeTypeLabel(String timeTypeLabel)
	{
		this.timeTypeLabel = timeTypeLabel;
	}

	public String getTimeUnitLabel()
	{
		return timeUnitLabel;
	}

	public void setTimeUnitLabel(String timeUnitLabel)
	{
		this.timeUnitLabel = timeUnitLabel;
	}

	public String getLimitTypeLabel()
	{
		return limitTypeLabel;
	}

	public void setLimitTypeLabel(String limitTypeLabel)
	{
		this.limitTypeLabel = limitTypeLabel;
	}

	public String getChannelLabel()
	{
		return channelLabel;
	}

	public void setChannelLabel(String channelLabel)
	{
		this.channelLabel = channelLabel;
	}

	public String getProvinceLabel()
	{
		return provinceLabel;
	}

	public void setProvinceLabel(String provinceLabel)
	{
		this.provinceLabel = provinceLabel;
	}

	public String getCityLabel()
	{
		return cityLabel;
	}

	public void setCityLabel(String cityLabel)
	{
		this.cityLabel = cityLabel;
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

	public String getPointLabel()
	{
		return pointLabel;
	}

	public void setPointLabel(String pointLabel)
	{
		this.pointLabel = pointLabel;
	}

	public String getActionLabel()
	{
		return actionLabel;
	}

	public void setActionLabel(String actionLabel)
	{
		this.actionLabel = actionLabel;
	}

	public String getStatusLabel()
	{
		return statusLabel;
	}

	public void setStatusLabel(String statusLabel)
	{
		this.statusLabel = statusLabel;
	}

	public int getStartUnit()
	{
		return startUnit;
	}

	public void setStartUnit(int startUnit)
	{
		this.startUnit = startUnit;
	}

	public int getEndUnit()
	{
		return endUnit;
	}

	public void setEndUnit(int endUnit)
	{
		this.endUnit = endUnit;
	}

	public long getRelativeValue()
	{
		return relativeValue;
	}

	public void setRelativeValue(long relativeValue)
	{
		this.relativeValue = relativeValue;
	}

	public int getRelativeUnit()
	{
		return relativeUnit;
	}

	public void setRelativeUnit(int relativeUnit)
	{
		this.relativeUnit = relativeUnit;
	}

	public String getRelativeUnitLabel()
	{
		return relativeUnitLabel;
	}

	public void setRelativeUnitLabel(String relativeUnitLabel)
	{
		this.relativeUnitLabel = relativeUnitLabel;
	}
}
