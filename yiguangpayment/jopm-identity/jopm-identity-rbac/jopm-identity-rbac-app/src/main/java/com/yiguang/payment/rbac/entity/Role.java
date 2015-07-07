package com.yiguang.payment.rbac.entity;

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
@Table(name = "t_role")
@SequenceGenerator(name = "seq_role", sequenceName = "seq_role", initialValue = 2000, allocationSize = 1)
public class Role implements Serializable
{
	private static final long serialVersionUID = -4004400990498493054L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_role")
	@Column(name = "id")
	private long id;

	@Column(name = "role_name")
	private String roleName;

	@Column(name = "create_time")
	private Date createTime;

	@Column(name = "status")
	private int status;

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

	public String getRoleName()
	{
		return roleName;
	}

	public void setRoleName(String roleName)
	{
		this.roleName = roleName;
	}

	public Date getCreateTime()
	{
		return createTime;
	}

	public void setCreateTime(Date createTime)
	{
		this.createTime = createTime;
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
}
