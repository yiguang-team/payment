package com.yiguang.payment.merchantOperate.entity;

import java.io.Serializable;


public class MobileAndTotal implements Serializable 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -199933729347416622L;
	private String mobile;
	private String total;

	public MobileAndTotal()
	{
		// TODO Auto-generated constructor stub
	}

	public MobileAndTotal(String mobile, String total)
	{
		this.mobile = mobile;
		this.total = total;
	}

	public String getMobile()
	{
		return mobile;
	}

	public void setMobile(String mobile)
	{
		this.mobile = mobile;
	}

	public String getTotal()
	{
		return total;
	}

	public void setTotal(String total)
	{
		this.total = total;
	}

}
