package com.yiguang.payment.common.numsection.entity;

/**
 * 运营商类型
 * 
 * @author Jinger
 * @date：2013-10-15
 */
import java.io.Serializable;

import com.yiguang.payment.common.CodedEnum;

public enum CarrierType implements CodedEnum<CarrierType>, Serializable
{

	HuaFei("01"), Game("02");

	public String code;

	private CarrierType(String code)
	{
		this.code = code;
	}

	@Override
	public String getCode(CarrierType t)
	{
		if (null != t)
		{
			for (CarrierType carrierType : CarrierType.values())
			{
				if (carrierType.equals(t))
				{
					return carrierType.code;
				}
			}
		}
		return null;
	}

	@Override
	public CarrierType getEnum(String code)
	{
		if (null != code)
		{
			for (CarrierType carrierType : CarrierType.values())
			{
				if (carrierType.code.equals(code))
				{
					return carrierType;
				}
			}
		}
		return null;
	}
}
