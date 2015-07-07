package com.yiguang.payment.rbac.vo;

import java.io.Serializable;


public class UserVO implements Serializable
{
	private static final long serialVersionUID = 4545974984697335703L;

	private long id;
	
	private String username;
	
	private String password;
	
	private int status;

	private int isLock;
	
	private String createTime;
	
	private String remark;
	
	private String statusLabel;
	
	private String isLockLabel;

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

	public String getPassword()
	{
		return password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

	public int getStatus()
	{
		return status;
	}

	public void setStatus(int status)
	{
		this.status = status;
	}

	public int getIsLock()
	{
		return isLock;
	}

	public void setIsLock(int isLock)
	{
		this.isLock = isLock;
	}

	public String getCreateTime()
	{
		return createTime;
	}

	public void setCreateTime(String createTime)
	{
		this.createTime = createTime;
	}

	public String getRemark()
	{
		return remark;
	}

	public void setRemark(String remark)
	{
		this.remark = remark;
	}

	public String getStatusLabel() {
		return statusLabel;
	}

	public void setStatusLabel(String statusLabel) {
		this.statusLabel = statusLabel;
	}

	public String getIsLockLabel() {
		return isLockLabel;
	}

	public void setIsLockLabel(String isLockLabel) {
		this.isLockLabel = isLockLabel;
	}
	
}
