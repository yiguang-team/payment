package com.yiguang.payment.rbac.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
@Entity
@Table(name = "t_role_privilege")
@SequenceGenerator(name = "seq_role_privilege", sequenceName = "seq_role_privilege", initialValue = 2000, allocationSize = 1)
public class RolePrivilege implements Serializable{

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_role_privilege")
	@Column(name = "id")
	private long id;
	
	@Column(name = "privilege_id")
	private long privilegeId;
	
	@Column(name = "role_id")
	private long roleId;
	
	@Column(name = "status")
	private int status;

	@Column(name = "remark")
	private String  remark;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getPrivilegeId() {
		return privilegeId;
	}

	public void setPrivilegeId(long privilegeId) {
		this.privilegeId = privilegeId;
	}

	public long getRoleId() {
		return roleId;
	}

	public void setRoleId(long roleId) {
		this.roleId = roleId;
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
