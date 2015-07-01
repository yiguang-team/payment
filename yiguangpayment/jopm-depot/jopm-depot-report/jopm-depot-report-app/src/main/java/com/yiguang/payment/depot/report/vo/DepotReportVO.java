package com.yiguang.payment.depot.report.vo;

import java.io.Serializable;
import java.math.BigDecimal;

public class DepotReportVO implements Serializable
{

	private static final long serialVersionUID = 1L;
	private int totalCount;
	private BigDecimal totalAmount;
	private int successCount;
	private BigDecimal successAmount;
	private int failedCount;
	private BigDecimal failedAmount;
	private String modelValue;

	public DepotReportVO()
	{
	}

	public DepotReportVO(int totalCount, BigDecimal totalAmount, int successCount, BigDecimal successAmount,
			int failedCount, BigDecimal failedAmount, String modelValue)
	{
		super();
		this.totalCount = totalCount;
		this.totalAmount = totalAmount;
		this.successCount = successCount;
		this.successAmount = successAmount;
		this.failedCount = failedCount;
		this.failedAmount = failedAmount;
		this.modelValue = modelValue;
	}

	public String getModelValue()
	{
		return modelValue;
	}

	public void setModelValue(String modelValue)
	{
		this.modelValue = modelValue;
	}

	public BigDecimal getFailedAmount()
	{
		return failedAmount;
	}

	public void setFailedAmount(BigDecimal failedAmount)
	{
		this.failedAmount = failedAmount;
	}

	public int getTotalCount()
	{
		return totalCount;
	}

	public void setTotalCount(int totalCount)
	{
		this.totalCount = totalCount;
	}

	public BigDecimal getTotalAmount()
	{
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount)
	{
		this.totalAmount = totalAmount;
	}

	public int getSuccessCount()
	{
		return successCount;
	}

	public void setSuccessCount(int successCount)
	{
		this.successCount = successCount;
	}

	public BigDecimal getSuccessAmount()
	{
		return successAmount;
	}

	public void setSuccessAmount(BigDecimal successAmount)
	{
		this.successAmount = successAmount;
	}

	public int getFailedCount()
	{
		return failedCount;
	}

	public void setFailedCount(int failedCount)
	{
		this.failedCount = failedCount;
	}

}
