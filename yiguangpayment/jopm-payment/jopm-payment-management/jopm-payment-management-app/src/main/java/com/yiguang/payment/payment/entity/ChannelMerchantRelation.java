package com.yiguang.payment.payment.entity;

/**
 * 渠道商户关系实体表
 * @author ediosn
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
@Table(name = "t_channel_merchant_relation")
@SequenceGenerator(name = "seq_channel_merchant_relation", sequenceName = "seq_channel_merchant_relation", initialValue = 1000, allocationSize = 1)
public class ChannelMerchantRelation implements Serializable
{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_channel_merchant_relation")
	@Column(name = "id")
	private long id;

	@Column(name = "merchant_id")
	private long merchantId;

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

	public long getMerchantId()
	{
		return merchantId;
	}

	public void setMerchantId(long merchantId)
	{
		this.merchantId = merchantId;
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
