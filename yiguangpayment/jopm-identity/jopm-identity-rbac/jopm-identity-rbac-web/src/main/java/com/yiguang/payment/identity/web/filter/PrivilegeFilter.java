package com.yiguang.payment.identity.web.filter;

/**
 * 权限模块过滤器
 * 
 * @author：Jinger
 * @date：2013-11-01
 */
import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.dubbo.rpc.RpcContext;
import com.yiguang.payment.rbac.entity.Menu;
import com.yiguang.payment.rbac.entity.User;
import com.yiguang.payment.rbac.service.MenuService;

public class PrivilegeFilter implements Filter
{
	private MenuService menuService;

	public void setMenuService(MenuService menuService)
	{
		this.menuService = menuService;
	}

	public void destroy()
	{

	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
			ServletException
	{

		HttpServletRequest httpRequest = (HttpServletRequest) request;
		String url = httpRequest.getRequestURI();
		Object object = httpRequest.getSession().getAttribute("loginUser");

		if ((url.contains("/login") || url.contains("/template") || url.contains("/rest")) && object == null)
		{
			chain.doFilter(request, response);
			return;
		}
		if (object != null)
		{
			User operator = (User) object;
			RpcContext.getContext().set("operator_id", operator.getId());
			url = url.substring(1, url.length());
			String menu_key = url.substring(url.indexOf("/") + 1, url.length());

			Menu menu = menuService.queryMenuByUrl(menu_key);
			request.setAttribute("menu", menu);
		}
		else
		{
			HttpServletRequest httpServletRequest = (HttpServletRequest) request;
			HttpServletResponse httpresponse = (HttpServletResponse) response;
			httpresponse.sendRedirect(httpServletRequest.getContextPath()+"/login");
			return;
		}
		chain.doFilter(request, response);
	}

	public void init(FilterConfig filterConfig) throws ServletException
	{

	}

}