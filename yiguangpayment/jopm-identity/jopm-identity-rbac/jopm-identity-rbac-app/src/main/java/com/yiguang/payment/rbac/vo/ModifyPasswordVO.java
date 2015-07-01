package com.yiguang.payment.rbac.vo;

import java.io.Serializable;


public class ModifyPasswordVO implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String id;
	
	private String rsaOldpwd;
	
	private String rsaNewpwd;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getRsaOldpwd() {
		return rsaOldpwd;
	}

	public void setRsaOldpwd(String rsaOldpwd) {
		this.rsaOldpwd = rsaOldpwd;
	}

	public String getRsaNewpwd() {
		return rsaNewpwd;
	}

	public void setRsaNewpwd(String rsaNewpwd) {
		this.rsaNewpwd = rsaNewpwd;
	}
	
	
}
