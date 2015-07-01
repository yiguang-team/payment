package com.yiguang.payment.common;

import javax.servlet.http.HttpServletRequest;

import com.yiguang.payment.common.utils.StringUtil;

/*
 * 客户端IP地址获得处理类
 */
public class IpTool
{

	/*
	 * 获取客户端真实IP地址
	 */
	public static String getIpAddr(HttpServletRequest request)
	{
		String ip = request.getHeader("X-Real-IP");
		if (!StringUtil.isBlank(ip) && !"unknown".equalsIgnoreCase(ip))
		{
			return ip;
		}
		ip = request.getHeader("X-Forwarded-For");
		if (!StringUtil.isBlank(ip) && !"unknown".equalsIgnoreCase(ip))
		{
			// 多次反向代理后会有多个IP值，第一个为真实IP。
			int index = ip.indexOf(',');
			if (index != -1)
			{
				return ip.substring(0, index);
			}
			else
			{
				return ip;
			}
		}
		else
		{
			return request.getRemoteAddr();
		}
	}

}
