package com.yiguang.payment.depot.order.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class DepotOrderVO implements Serializable
{

	private static final long serialVersionUID = 1L;

	private long chargingPointId;

	private String chargingPointLabel;

	private Date requestTime;

	private String requestIp;

	private String orderId;

	private String extractType;

	private String extractUser;

	private BigDecimal payAmount;

	private long extractCount;

	public String getChargingPointLabel()
	{
		return chargingPointLabel;
	}

	public void setChargingPointLabel(String chargingPointLabel)
	{
		this.chargingPointLabel = chargingPointLabel;
	}

	public long getChargingPointId()
	{
		return chargingPointId;
	}

	public void setChargingPointId(long chargingPointId)
	{
		this.chargingPointId = chargingPointId;
	}

	public Date getRequestTime()
	{
		return requestTime;
	}

	public void setRequestTime(Date requestTime)
	{
		this.requestTime = requestTime;
	}

	public String getRequestIp()
	{
		return requestIp;
	}

	public void setRequestIp(String requestIp)
	{
		this.requestIp = requestIp;
	}

	public String getOrderId()
	{
		return orderId;
	}

	public void setOrderId(String orderId)
	{
		this.orderId = orderId;
	}

	public String getExtractType()
	{
		return extractType;
	}

	public void setExtractType(String extractType)
	{
		this.extractType = extractType;
	}

	public String getExtractUser()
	{
		return extractUser;
	}

	public void setExtractUser(String extractUser)
	{
		this.extractUser = extractUser;
	}

	public BigDecimal getPayAmount()
	{
		return payAmount;
	}

	public void setPayAmount(BigDecimal payAmount)
	{
		this.payAmount = payAmount;
	}

	public long getExtractCount()
	{
		return extractCount;
	}

	public void setExtractCount(long extractCount)
	{
		this.extractCount = extractCount;
	}

}
