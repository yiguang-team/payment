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
 * 白名单
 * 
 * @author ediosn
 * 
 */
@Entity
@Table(name = "t_rule_white_list")
@SequenceGenerator(name = "seq_rule_white_list", sequenceName = "seq_rule_white_list", initialValue = 2000, allocationSize = 1)
public class WhiteList implements Serializable
{

	private static final long serialVersionUID = 1L;

	@Override
	public String toString()
	{
		return "WhiteList [id=" + id + ", type=" + type + ", value=" + value + ", status=" + status + "]";
	}

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_rule_white_list")
	@Column(name = "id")
	private long id;

	@Column(name = "type")
	private int type;// 类型(黑白名单公用)

	@Column(name = "value")
	private String value;// 值

	@Column(name = "status")
	private int status;// 状态

	public long getId()
	{
		return id;
	}

	public void setId(long id)
	{
		this.id = id;
	}

	public int getType()
	{
		return type;
	}

	public void setType(int type)
	{
		this.type = type;
	}

	public String getValue()
	{
		return value;
	}

	public void setValue(String value)
	{
		this.value = value;
	}

	public int getStatus()
	{
		return status;
	}

	public void setStatus(int status)
	{
		this.status = status;
	}

}
