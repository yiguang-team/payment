package com.yiguang.payment.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/*
 * 时间处理工具类
 */
public class DateTimeHelper
{

	// 定义默认时间格式
	private static final String defaultFormat = "yyyyMMddHHmmss";

	// 定义默认时间格式
	private static final String datetimeFormatOne = "yyyy-MM-dd HH:mm:ss";

	// 字符串格式转换时间格式
	public static Date getDateTimeByString(String datetime, String format)
	{
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		Date date = null;
		try
		{
			date = sdf.parse(datetime);
		}
		catch (ParseException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return date;

	}

	// 获得当月第一天
	public static String getCurrentMonthFirstDay()
	{
		Calendar c = Calendar.getInstance();
		c.set(Calendar.DAY_OF_MONTH, 1);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		String firstDay = getStringByDateTime(c.getTime(), datetimeFormatOne);
		return firstDay;
	}

	// 获得当月最后一天
	public static String getCurrentMonthLastDay()
	{
		Calendar c = Calendar.getInstance();
		c.set(Calendar.HOUR_OF_DAY, 23);
		c.set(Calendar.MINUTE, 59);
		c.set(Calendar.SECOND, 59);
		c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
		String lastDay = getStringByDateTime(c.getTime(), datetimeFormatOne);
		return lastDay;
	}

	public static void main(String[] args)
	{
		System.out.println("First Day: " + getCurrentMonthFirstDay());

		System.out.println("Last Day: " + getCurrentMonthLastDay());
	}

	// 获得当前时间字符串
	public static String getNowDateTimeString()
	{
		return getStringByDateTime(new Date(), datetimeFormatOne);
	}

	// 时间格式转字符串
	public static String getStringByDateTime(Date datetime, String format)
	{
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		String dateString = "";
		dateString = sdf.format(datetime);
		return dateString;

	}

	// 时间加减分钟
	public static Date getPlusDateByMins(Date date, int mins)
	{
		return new Date(date.getTime() + mins * 60 * 1000);
	}

	// 默认个好似转换时间
	public static Date getDateTimeByString(String datetime)

	{
		return getDateTimeByString(datetime, defaultFormat);

	}

	// 是否与当前时间小差在n分钟之内
	public static Boolean IsNowBetweenMins(Date date1, int n)
	{
		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();
		Calendar cnow = Calendar.getInstance();
		Date dateNow = new Date();
		cnow.setTime(dateNow);

		c1.setTime(date1);
		c1.add(Calendar.MINUTE, n);

		c2.setTime(date1);
		c2.add(Calendar.MINUTE, -n);

		if (date1.after(dateNow) && c2.before(cnow))
		{
			return true;
		}
		if (date1.before(dateNow) && c1.after(cnow))
		{
			return true;

		}

		return false;

	}
}
