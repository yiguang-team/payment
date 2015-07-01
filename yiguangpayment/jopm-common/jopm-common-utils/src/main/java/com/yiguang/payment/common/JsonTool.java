package com.yiguang.payment.common;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonTool
{

	private static ObjectMapper mapper;

	/**
	 * 获取ObjectMapper实例
	 * 
	 * @param createNew
	 *            方式：true，新实例；false,存在的mapper实例
	 * @return
	 */
	public static synchronized ObjectMapper getMapperInstance(boolean createNew)
	{
		if (createNew)
		{
			return new ObjectMapper();
		}
		else if (mapper == null)
		{
			mapper = new ObjectMapper();
		}
		return mapper;
	}

	/**
	 * 将java对象转换成json字符串
	 * 
	 * @param obj
	 *            准备转换的对象
	 * @return json字符串
	 * @throws Exception
	 */
	public static String beanToJson(Object obj)
	{
		try
		{
			ObjectMapper objectMapper = getMapperInstance(false);
			String json = objectMapper.writeValueAsString(obj);
			return json;
		}
		catch (Exception e)
		{
			return "";
		}
	}

	/**
	 * 将java对象转换成json字符串
	 * 
	 * @param obj
	 *            准备转换的对象
	 * @param createNew
	 *            ObjectMapper实例方式:true，新实例;false,存在的mapper实例
	 * @return json字符串
	 * @throws Exception
	 */
	public static String beanToJson(Object obj, Boolean createNew)
	{
		try
		{
			ObjectMapper objectMapper = getMapperInstance(createNew);
			String json = objectMapper.writeValueAsString(obj);
			return json;
		}
		catch (Exception e)
		{
			return "";
		}
	}

	/**
	 * 将json字符串转换成java对象
	 * 
	 * @param json
	 *            准备转换的json字符串
	 * @param cls
	 *            准备转换的类
	 * @return
	 * @throws Exception
	 */
	public static Object jsonToBean(String json, Class<?> cls)
	{
		try
		{
			ObjectMapper objectMapper = getMapperInstance(false);
			Object vo = objectMapper.readValue(json, cls);
			return vo;
		}
		catch (Exception e)
		{
			return null;
		}
	}

	/**
	 * 将json字符串转换成java对象
	 * 
	 * @param json
	 *            准备转换的json字符串
	 * @param cls
	 *            准备转换的类
	 * @param createNew
	 *            ObjectMapper实例方式:true，新实例;false,存在的mapper实例
	 * @return
	 * @throws Exception
	 */
	public static Object jsonToBean(String json, Class<?> cls, Boolean createNew)
	{
		try
		{
			ObjectMapper objectMapper = getMapperInstance(createNew);
			Object vo = objectMapper.readValue(json, cls);
			return vo;
		}
		catch (Exception e)
		{
			return null;
		}
	}

	public static List<Map<String, Object>> jsonToList(String json)
	{

		try
		{
			ObjectMapper objectMapper = getMapperInstance(false);
			@SuppressWarnings("unchecked")
			List<Map<String, Object>> list = objectMapper.readValue(json, List.class);
			return list;
		}
		catch (Exception e)
		{
			return null;
		}
	}

	public static Map<String, Object> jsonToMap(String json)
	{

		try
		{
			ObjectMapper objectMapper = getMapperInstance(false);
			@SuppressWarnings("unchecked")
			Map<String, Object> map = objectMapper.readValue(json, Map.class);
			return map;
		}
		catch (Exception e)
		{
			return null;
		}
	}
}
