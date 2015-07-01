package com.yiguang.payment.payment.rest.entity;

public class RestResult
{
	private String code;

	private String message;

	public RestResult(int code, String message)
	{
		this.code = String.valueOf(code);
		this.message = message;
	}

	public RestResult(String code, String message)
	{
		this.code = code;
		this.message = message;
	}

	public String getCode()
	{
		return code;
	}

	public void setCode(String code)
	{
		this.code = code;
	}

	public String getMessage()
	{
		return message;
	}

	public void setMessage(String message)
	{
		this.message = message;
	}

}
