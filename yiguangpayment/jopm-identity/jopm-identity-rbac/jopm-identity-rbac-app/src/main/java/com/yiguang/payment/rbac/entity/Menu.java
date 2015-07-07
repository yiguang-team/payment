package com.yiguang.payment.rbac.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "t_menu")
@SequenceGenerator(name = "seq_menu", sequenceName = "seq_menu", initialValue = 2000, allocationSize = 1)
public class Menu implements Serializable
{
	private static final long serialVersionUID = -7779982121215638469L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_menu")
	@Column(name = "id")
	private long id;

	@Column(name = "menu_name")
	private String menuName;

	@Column(name = "display_order")
	private int displayOrder;

	@Column(name = "parent_id")
	private Long parentId;

	@Column(name = "create_time")
	private Date createTime;

	@Column(name = "status")
	private int status;

	@Column(name = "menu_level")
	private int menulevel; // 菜单级别

	@Column(name = "remark")
	private String remark;

	@Column(name = "sub_module")
	private String subModule; 
	
	@Column(name = "sub_system")
	private String subSystem; 
	
	@Column(name = "url")
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
}
