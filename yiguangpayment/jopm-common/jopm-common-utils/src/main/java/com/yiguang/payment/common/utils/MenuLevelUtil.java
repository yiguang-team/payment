package com.yiguang.payment.common.utils;

import com.yiguang.payment.common.Constant;

public class MenuLevelUtil
{

	public static String getChildMenuLevel(String ml)
	{
		if (ml.equals(Constant.MenuLevel.ZERO))
		{
			ml = Constant.MenuLevel.ONE;
		}
		else if (ml.equals(Constant.MenuLevel.ONE))
		{
			ml = Constant.MenuLevel.TWO;
		}
		else
		{
			ml = Constant.MenuLevel.THREE;
		}
		return ml;
	}

	public MenuLevelUtil()
	{
		super();
	}

}