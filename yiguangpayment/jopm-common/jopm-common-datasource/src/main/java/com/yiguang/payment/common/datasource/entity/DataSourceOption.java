package com.yiguang.payment.common.datasource.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "t_datasource_option")
@SequenceGenerator(name = "seq_datasource_option", sequenceName = "seq_datasource_option")
public class DataSourceOption implements Serializable
{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_datasource_option")
	@Column(name = "id", length = 20)
	private long id;

	@Column(name = "option_id", length = 20)
	private String optionId;

	@Column(name = "option_label")
	private String optionLabel;

	@Column(name = "status")
	private int status;

	@Column(name = "remark")
	private String remark;

	@Column(name = "data_source_code")
	private String dataSourceCode;

	public long getId()
	{
		return id;
	}

	public void setId(long id)
	{
		this.id = id;
	}

	public String getOptionId()
	{
		return optionId;
	}

	public void setOptionId(String optionId)
	{
		this.optionId = optionId;
	}

	public String getOptionLabel()
	{
		return optionLabel;
	}

	public void setOptionLabel(String optionLabel)
	{
		this.optionLabel = optionLabel;
	}

	public int getStatus()
	{
		return status;
	}

	public void setStatus(int status)
	{
		this.status = status;
	}

	public String getRemark()
	{
		return remark;
	}

	public void setRemark(String remark)
	{
		this.remark = remark;
	}

	public String getDataSourceId()
	{
		return dataSourceCode;
	}

	public void setDatasourceId(String dataSourceCode)
	{
		this.dataSourceCode = dataSourceCode;
	}

}
