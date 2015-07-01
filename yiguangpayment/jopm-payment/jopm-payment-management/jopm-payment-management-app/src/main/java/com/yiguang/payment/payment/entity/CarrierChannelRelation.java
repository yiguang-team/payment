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
 * 
 * 运营商渠道关系实体表
 * @author ediosn
 *
 */
@Entity
@Table(name = "t_carrier_channel_relation")
@SequenceGenerator(name = "seq_carrier_channel_relation", sequenceName = "seq_carrier_channel_relation", initialValue = 1000, allocationSize = 1)
public class CarrierChannelRelation implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_carrier_channel_relation")
	@Column(name = "id")
	private long id;

	@Column(name = "carrier_id")
	private long carrierId;

	@Column(name = "channel_id")
	private long channelId;

	@Column(name = "sort")
	private int sort;
	
	@Column(name = "status")
	private int status;

	@Column(name = "remark")
	private String remark;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getCarrierId() {
		return carrierId;
	}

	public void setCarrierId(long carrierId) {
		this.carrierId = carrierId;
	}

	public long getChannelId() {
		return channelId;
	}

	public void setChannelId(long channelId) {
		this.channelId = channelId;
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

	public int getSort() {
		return sort;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}

}
