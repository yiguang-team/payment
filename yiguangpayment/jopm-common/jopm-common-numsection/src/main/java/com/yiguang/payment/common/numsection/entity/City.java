package com.yiguang.payment.common.numsection.entity;

/**
 * 城市实体
 * @author ediosn
 * 
 */
import java.io.Serializable;

/**
 * 城市市表实体
 * 
 * @author Jinger
 * @date：2013-10-15
 */
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "t_city")
public class City implements Serializable
{
	@Override
	public String toString()
	{
		return "City [id=" + cityId + ", cityName=" + cityName + ", status=" + status + ", province=" + province + "]";
	}

	private static final long serialVersionUID = -5703418279857089776L;

	@Id
	@Column(name = "city_id")
	private String cityId;

	@Column(name = "city_name")
	private String cityName;

	@Column(name = "status", length = 1)
	private int status;

	@ManyToOne
	@JoinColumn(name = "province_id", nullable = false)
	private Province province;

	public String getCityId()
	{
		return cityId;
	}

	public void setCityId(String cityId)
	{
		this.cityId = cityId;
	}

	public String getCityName()
	{
		return cityName;
	}

	public void setCityName(String cityName)
	{
		this.cityName = cityName;
	}

	public int getStatus()
	{
		return status;
	}

	public void setStatus(int status)
	{
		this.status = status;
	}

	public Province getProvince()
	{
		return province;
	}

	public void setProvince(Province province)
	{
		this.province = province;
	}

}
