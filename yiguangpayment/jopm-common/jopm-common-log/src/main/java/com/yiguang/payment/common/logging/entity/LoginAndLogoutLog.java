package com.yiguang.payment.common.logging.entity;

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
@Table(name = "t_login_logout_log")
@SequenceGenerator(name = "seq_login_logout_log", sequenceName = "seq_login_logout_log")
public class LoginAndLogoutLog implements Serializable
{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_login_logout_log")
	@Column(name = "id", length = 20)
	private long id;

	@Column(name = "username")
	private String username;

	@Column(name = "operation_type")
	private int operationType;

	@Column(name = "operation_time")
	private Date operationTime;

	@Column(name = "operation_ip")
	private String operationIp;

	@Column(name = "remark")
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

	public int getOperationType()
	{
		return operationType;
	}

	public void setOperationType(int operationType)
	{
		this.operationType = operationType;
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

}
