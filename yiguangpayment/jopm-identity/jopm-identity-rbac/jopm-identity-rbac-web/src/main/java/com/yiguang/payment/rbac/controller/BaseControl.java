package com.yiguang.payment.rbac.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.ModelMap;



import com.yiguang.payment.rbac.entity.User;

import freemarker.ext.beans.BeansWrapper;
import freemarker.template.TemplateModelException;

public class BaseControl
{
	public static final String PAGE_SIZE = "10";

	private static final Logger LOGGER = LoggerFactory.getLogger(BaseControl.class);

	protected static void setDefaultStaticModel(ModelMap modelMap, Class[] staticClasses)
	{
		for (Class clz : staticClasses)
		{
			String name = clz.getSimpleName();
			try
			{
				BeansWrapper wrapper = BeansWrapper.getDefaultInstance();

				modelMap.addAttribute(name, wrapper.getStaticModels().get(clz.getName()));
			}
			catch (TemplateModelException ex)
			{
				LOGGER.info("setDefaultStatic[" + name + "] fail");
			}

		}

	}

	public static void setDefaultEnumModel(ModelMap modelMap, Class[] enumClasses)
	{
		for (Class clz : enumClasses)
		{
			String name = clz.getSimpleName();
			try
			{
				BeansWrapper wrapper = BeansWrapper.getDefaultInstance();

				modelMap.addAttribute(name, wrapper.getEnumModels().get(clz.getName()));
			}
			catch (TemplateModelException ex)
			{
				LOGGER.info("setDefaultEnumModel[" + name + "] fail");
			}

		}
	}

	public static User getLoginUser()
	{
		try
		{
			User user = (User) SecurityUtils.getSubject().getPrincipals().getPrimaryPrincipal();
			if (user != null)
			{
				return user;
			}
			else
			{
				throw new AuthenticationException("授权未通过，认证用户信息不存在！");
			}
		}
		catch (AuthenticationException ae)
		{
			throw new AuthenticationException("获取认证信息失败！");
		}
	}
}
