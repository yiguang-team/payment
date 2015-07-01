package com.yiguang.payment.payment.vo;

import java.io.Serializable;

/**
 * 渠道VO
 * 
 * @author Shinalon
 * 
 */
public class ChannelVO implements Serializable {

	private static final long serialVersionUID = 1L;

	private long id;

	private String name;
	
	private int status;

	private String remark;
	
	private String statusLabel;
	
	private String sortLabel;
	
	public String getSortLabel() {
		return sortLabel;
	}

	public void setSortLabel(String sortLabel) {
		this.sortLabel = sortLabel;
	}

	private String carrierChannelRelationIDs;

	public String getCarrierChannelRelationIDs() {
		return carrierChannelRelationIDs;
	}

	public void setCarrierChannelRelationIDs(String carrierChannelRelationIDs) {
		this.carrierChannelRelationIDs = carrierChannelRelationIDs;
	}

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

	public String getStatusLabel() {
		return statusLabel;
	}

	public void setStatusLabel(String statusLabel) {
		this.statusLabel = statusLabel;
	}

}
