package com.yiguang.payment.payment.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "t_basic_rule")
@SequenceGenerator(name = "seq_basic_rule", sequenceName = "seq_basic_rule", initialValue = 2000, allocationSize = 1)
public class BasicRule implements Serializable
{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_basic_rule")
	@Column(name = "id")
	private long id;

	@Column(name = "time_type")
	private int timeType;// 时间限制方式  单位限制  时段限制
	
	@Column(name = "timeUnit")
	private int timeUnit;// 单位限制的时间单位有  每月 每天 每时
	
	@Column(name = "start_unit")
	private int startUnit;//
	
	@Column(name = "end_unit")
	private int endUnit;//
	
	@Column(name = "relative_value")
	private long relativeValue;//
	
	@Column(name = "relative_unit")
	private int relativeUnit;
	
	@Column(name = "start_time")
	private Date startTime;// 时段限制起始时间
	
	@Column(name = "end_time")
	private Date endTime;// 时段限制结束时间

	@Column(name = "limit_type")
	private int limitType;//限制方式  笔数  金额
	
	@Column(name = "volume")
	private long volume;// 限制值
	
	/**
	 * 以下字段   -1代表当前值，0代表不限制  其他值对应数据源条件、
	 */

	@Column(name = "channel_id")
	private long channelId;// 渠道
	
	@Column(name = "province_id")
	private String provinceId;// 省

	@Column(name = "city_id")
	private String cityId;// 市

	@Column(name = "merchant_id")
	private long merchantId;
	
	@Column(name = "product_id")
	private long productId;
	
	@Column(name = "point_id")
	private long pointId;

	
	@Column(name = "mobile")
	private String mobile;//号码
	
	@Column(name = "ip")
	private String ip;//IP
	
	@Column(name = "username")
	private String username;//账号
	
	@Column(name = "action")
	private int action;//规则动作 
	
	@Column(name = "status")
	private int status;//状态

	@Column(name = "remark")
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

	public Date getStartTime()
	{
		return startTime;
	}

	public void setStartTime(Date startTime)
	{
		this.startTime = startTime;
	}

	public Date getEndTime()
	{
		return endTime;
	}

	public void setEndTime(Date endTime)
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

	public long getVolume()
	{
		return volume;
	}

	public void setVolume(long volume)
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
	
}
