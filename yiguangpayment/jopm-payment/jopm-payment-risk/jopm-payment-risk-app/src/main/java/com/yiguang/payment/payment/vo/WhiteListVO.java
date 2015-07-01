package com.yiguang.payment.payment.vo;

import java.io.Serializable;

public class WhiteListVO implements Serializable
{
	private static final long serialVersionUID = 1L;

	private long id;

	private int type;// 类型(黑白名单公用)

	private String value;// 值

	private int status;// 状态

	private String typeLabel;

	private String statusLabel;

	public long getId()
	{
		return id;
	}

	public void setId(long id)
	{
		this.id = id;
	}

	public int getType()
	{
		return type;
	}

	public void setType(int type)
	{
		this.type = type;
	}

	public String getValue()
	{
		return value;
	}

	public void setValue(String value)
	{
		this.value = value;
	}

	public int getStatus()
	{
		return status;
	}

	public void setStatus(int status)
	{
		this.status = status;
	}

	public String getTypeLabel()
	{
		return typeLabel;
	}

	public void setTypeLabel(String typeLabel)
	{
		this.typeLabel = typeLabel;
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
