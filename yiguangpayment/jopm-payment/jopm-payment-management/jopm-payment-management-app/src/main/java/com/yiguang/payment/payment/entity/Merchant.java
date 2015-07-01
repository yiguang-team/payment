package com.yiguang.payment.payment.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 商户实体
 * 
 * @author Shinalon
 * 
 */
@Entity
@Table(name = "t_agent_merchant")
@SequenceGenerator(name = "seq_agent_merchant", sequenceName = "seq_agent_merchant", initialValue = 2000, allocationSize = 1)
public class Merchant implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String toString()
	{
		return "Merchant [id=" + id + ", name=" + name + ", remark=" + remark + ",status=" + status + "]";
	}

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_agent_merchant")
	@Column(name = "id")
	private long id;

	@Column(name = "name")
	private String name;

	@Column(name = "remark")
	private String remark;

	@Column(name = "status")
	private int status;

	@Column(name = "key")
	private String key;

	@Column(name = "notify_url")
	private String notifyUrl;

	@Column(name = "admin_user")
	private long adminUser;
	
	public long getId()
	{
		return id;
	}

	public void setId(long id)
	{
		this.id = id;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getRemark()
	{
		return remark;
	}

	public void setRemark(String remark)
	{
		this.remark = remark;
	}

	public int getStatus()
	{
		return status;
	}

	public void setStatus(int status)
	{
		this.status = status;
	}

	public String getKey()
	{
		return key;
	}

	public void setKey(String key)
	{
		this.key = key;
	}

	public String getNotifyUrl()
	{
		return notifyUrl;
	}

	public void setNotifyUrl(String notifyUrl)
	{
		this.notifyUrl = notifyUrl;
	}

	public long getAdminUser()
	{
		return adminUser;
	}

	public void setAdminUser(long adminUser)
	{
		this.adminUser = adminUser;
	}

}
