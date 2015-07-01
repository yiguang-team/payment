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
 * 支付渠道
 * 
 * @author Shinalon
 * 
 */
@Entity
@Table(name = "t_channel")
@SequenceGenerator(name = "seq_channel", sequenceName = "seq_channel",initialValue=1000,allocationSize = 1)
public class Channel implements Serializable {
	/**
	 * 
	 */
	@Override
	public String toString() {
		return "Channel [id=" + id + ", name=" + name + ", status=" + status + ", remark=" + remark + "]";
	}

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_channel")
	@Column(name = "id")
	private long id;

	@Column(name = "name")
	private String name;

	@Column(name = "status", length = 2)
	private int status;

	@Column(name = "remark")
	private String remark;
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}
