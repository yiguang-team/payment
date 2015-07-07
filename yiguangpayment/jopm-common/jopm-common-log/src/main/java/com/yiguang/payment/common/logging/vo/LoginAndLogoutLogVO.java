package com.yiguang.payment.common.logging.vo;

import java.io.Serializable;
import java.util.Date;

public class LoginAndLogoutLogVO implements Serializable
{

	private static final long serialVersionUID = 1L;

	private long id;

	private String username;

	private String operationTypeLabel;

	private int operationType;
	
	private Date operationTime;

	private String operationIp;

	private String remark;

	public long getId()
	{
		return id;
	}

	public void setId(long id)
	{
		this.id = id;
	}

	public String getUsername()
	{
		return username;
	}

	public void setUsername(String username)
	{
		this.username = username;
	}

	public String getOperationTypeLabel()
	{
		return operationTypeLabel;
	}

	public void setOperationTypeLabel(String operationTypeLabel)
	{
		this.operationTypeLabel = operationTypeLabel;
	}

	public Date getOperationTime()
	{
		return operationTime;
	}

	public void setOperationTime(Date operationTime)
	{
		this.operationTime = operationTime;
	}

	public String getOperationIp()
	{
		return operationIp;
	}

	public void setOperationIp(String operationIp)
	{
		this.operationIp = operationIp;
	}

	public String getRemark()
	{
		return remark;
	}

	public void setRemark(String remark)
	{
		this.remark = remark;
	}

	public int getOperationType() {
		return operationType;
	}

	public void setOperationType(int operationType) {
		this.operationType = operationType;
	}

}
