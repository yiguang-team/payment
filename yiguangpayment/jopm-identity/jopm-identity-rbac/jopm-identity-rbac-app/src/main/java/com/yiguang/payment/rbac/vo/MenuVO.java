package com.yiguang.payment.rbac.vo;

import java.io.Serializable;
import java.util.Date;


public class MenuVO implements Serializable
{
	private static final long serialVersionUID = -7779982121215638469L;

	private long id;

	private String menuName;

	private int displayOrder;

	private Long parentId;

	private Date createTime;

	private int status;

	private int menulevel; // 菜单级别

	private String remark;

	private String subModule; 
	
	private String subSystem; 
	
	private String statusLabel;
	
	private String url;

	public long getId()
	{
		return id;
	}

	public void setId(long id)
	{
		this.id = id;
	}

	public String getMenuName()
	{
		return menuName;
	}

	public void setMenuName(String menuName)
	{
		this.menuName = menuName;
	}

	public int getDisplayOrder()
	{
		return displayOrder;
	}

	public void setDisplayOrder(int displayOrder)
	{
		this.displayOrder = displayOrder;
	}

	public Long getParentId()
	{
		return parentId;
	}

	public void setParentId(Long parentId)
	{
		this.parentId = parentId;
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

	public int getMenulevel()
	{
		return menulevel;
	}

	public void setMenulevel(int menulevel)
	{
		this.menulevel = menulevel;
	}

	public String getRemark()
	{
		return remark;
	}

	public void setRemark(String remark)
	{
		this.remark = remark;
	}

	public String getSubModule()
	{
		return subModule;
	}

	public void setSubModule(String subModule)
	{
		this.subModule = subModule;
	}

	public String getSubSystem()
	{
		return subSystem;
	}

	public void setSubSystem(String subSystem)
	{
		this.subSystem = subSystem;
	}

	public String getUrl()
	{
		return url;
	}

	public void setUrl(String url)
	{
		this.url = url;
	}

	public String getStatusLabel() {
		return statusLabel;
	}

	public void setStatusLabel(String statusLabel) {
		this.statusLabel = statusLabel;
	}
	
}
