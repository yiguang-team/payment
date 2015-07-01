package com.yiguang.payment.payment.vo;

import java.io.Serializable;

public class CarrierChannelRelationVO implements Serializable{

	private static final long serialVersionUID = 1L;

	private long id;

	private long carrierId;

	private long channelId;

	private int status;
	
	private int sort;
	
	private String remark;

	private String carrierLabel;

	private String channelLabel;
	
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

	public String getCarrierLabel() {
		return carrierLabel;
	}

	public void setCarrierLabel(String carrierLabel) {
		this.carrierLabel = carrierLabel;
	}

	public String getChannelLabel() {
		return channelLabel;
	}

	public void setChannelLabel(String channelLabel) {
		this.channelLabel = channelLabel;
	}

	public int getSort() {
		return sort;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}

}
