package com.yiguang.payment.business.product.entity;

/**
 * 计费点渠道关系实体表
 * @author ediosn
 * 
 */
import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "t_point_channel_relation")
@SequenceGenerator(name = "seq_point_channel_relation", sequenceName = "seq_point_channel_relation", initialValue = 2000, allocationSize = 1)
public class PointChannelRelation implements Serializable
{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_point_channel_relation")
	@Column(name = "id")
	private long id;

	@Column(name = "point_id")
	private long pointId;

	@Column(name = "channel_id")
	private long channelId;

	@Column(name = "status")
	private int status;

	@Column(name = "remark")
	private String remark;

	public long getId()
	{
		return id;
	}

	public void setId(long id)
	{
		this.id = id;
	}

	public long getPointId()
	{
		return pointId;
	}

	public void setPointId(long pointId)
	{
		this.pointId = pointId;
	}

	public long getChannelId()
	{
		return channelId;
	}

	public void setChannelId(long channelId)
	{
		this.channelId = channelId;
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

}
