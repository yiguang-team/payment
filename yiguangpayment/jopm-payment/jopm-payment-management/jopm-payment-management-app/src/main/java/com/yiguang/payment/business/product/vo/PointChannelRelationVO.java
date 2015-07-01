package com.yiguang.payment.business.product.vo;

import java.io.Serializable;

public class PointChannelRelationVO implements Serializable
{

	private static final long serialVersionUID = 1L;

	private long id;

	private long pointId;

	private long channelId;

	private int status;

	private String remark;

	private String pointLabel;

	private String channelLabel;

	public long getId()
	{
		return id;
	}

	public void setId(long id)
	{
		this.id = id;
	}

	public long getPointId()
	{
		return pointId;
	}

	public void setPointId(long pointId)
	{
		this.pointId = pointId;
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

	public String getPointLabel()
	{
		return pointLabel;
	}

	public void setPointLabel(String pointLabel)
	{
		this.pointLabel = pointLabel;
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
