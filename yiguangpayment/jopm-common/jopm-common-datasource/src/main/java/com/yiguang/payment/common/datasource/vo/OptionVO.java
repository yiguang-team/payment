package com.yiguang.payment.common.datasource.vo;

import java.io.Serializable;

public class OptionVO implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String text;
	private String value;

	public String getText()
	{
		return text;
	}

	public void setText(String text)
	{
		this.text = text;
	}

	public String getValue()
	{
		return value;
	}

	public void setValue(String value)
	{
		this.value = value;
	}
}
