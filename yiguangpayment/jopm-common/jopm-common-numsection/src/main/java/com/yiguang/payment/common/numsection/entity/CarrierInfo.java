package com.yiguang.payment.common.numsection.entity;

/**
 * 运营商实体
 * 
 * @author Jinger
 * @date：2013-10-15
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
@Table(name = "t_carrier_info")
@SequenceGenerator(name = "seq_carrier_info", sequenceName = "seq_carrier_info", initialValue = 2000, allocationSize = 1)
public class CarrierInfo implements Serializable
{
	@Override
	public String toString()
	{
		return "CarrierInfo [id=" + id + ", carrierNo=" + carrierNo + "carrierName=" + carrierName + ", status="
				+ status + "]";
	}

	private static final long serialVersionUID = -7713317049589822028L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_carrier_info")
	@Column(name = "id")
	private long id;

	@Column(name = "carrier_no")
	private String carrierNo;

	@Column(name = "carrier_name")
	private String carrierName;

	@Column(name = "status", length = 2)
	private int status;

	@Column(name = "carrier_type")
	private int carrierType;

	public long getId()
	{
		return id;
	}

	public void setId(long id)
	{
		this.id = id;
	}

	public String getCarrierNo()
	{
		return carrierNo;
	}

	public void setCarrierNo(String carrierNo)
	{
		this.carrierNo = carrierNo;
	}

	public String getCarrierName()
	{
		return carrierName;
	}

	public void setCarrierName(String carrierName)
	{
		this.carrierName = carrierName;
	}

	public int getStatus()
	{
		return status;
	}

	public void setStatus(int status)
	{
		this.status = status;
	}

	public int getCarrierType()
	{
		return carrierType;
	}

	public void setCarrierType(int carrierType)
	{
		this.carrierType = carrierType;
	}

}
