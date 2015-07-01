package com.yiguang.payment.rbac.vo;

import java.io.Serializable;

public class RoleUserVO implements Serializable{


	private static final long serialVersionUID = 1L;

	private long id;
	
	private long roleId;
	
	private long userId;
	
	private int status;
	
	private String statusLabel;
	
	private String roleUserIds;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getRoleId() {
		return roleId;
	}

	public void setRoleId(long roleId) {
		this.roleId = roleId;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getStatusLabel() {
		return statusLabel;
	}

	public void setStatusLabel(String statusLabel) {
		this.statusLabel = statusLabel;
	}

	public String getRoleUserIds() {
		return roleUserIds;
	}

	public void setRoleUserIds(String roleUserIds) {
		this.roleUserIds = roleUserIds;
	}
	
}
