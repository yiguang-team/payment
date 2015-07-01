package com.yiguang.payment.merchantOperate.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesUtil
{

	// 根据key读取value
	public static String readValue(String key)
	{

		Properties config = null;
		InputStream in = PropertiesUtil.class.getClassLoader().getResourceAsStream("config.properties");
		config = new Properties();
		try
		{
			config.load(in);
			in.close();
		}
		catch (IOException e)
		{
			System.out.println("No AreaPhone.properties defined error");
		}
		try
		{

			String value = config.getProperty(key);
			return value;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.err.println("ConfigInfoError" + e.toString());
			return null;
		}
	}

	public static void main(String args[])
	{
		// String LaSaPhone=config.getProperty("LaSaPhone");
		// System.out.println(LaSaPhone);
		// System.out.println(getPhone.readValue("LaSaPhone"));
		System.out.println(PropertiesUtil.readValue("maxValue"));

		if (Long.parseLong("1798.0000".split("\\.")[0]) > Long.parseLong("20000"))
		{
			System.out.print("today's total number is more than ");
			System.out.print("1798.0000".split("\\.")[0]);
		}
	}
}
