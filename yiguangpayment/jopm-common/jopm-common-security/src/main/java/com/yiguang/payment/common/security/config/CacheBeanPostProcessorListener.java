package com.yiguang.payment.common.security.config;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yiguang.payment.common.utils.SpringUtils;

public class CacheBeanPostProcessorListener implements ServletContextListener
{
	private static final Logger LOGGER = LoggerFactory.getLogger(CacheBeanPostProcessorListener.class);

	public void startup()
	{

	}

	public void contextInitialized(ServletContextEvent event)
	{
		LOGGER.info("CacheBeanPostProcessorListener start");
		CacheBeanPostProcessor processer = SpringUtils.getBean("cacheBeanPostProcessor");
		processer.init();
		LOGGER.info("CacheBeanPostProcessorListener end");
	}

	public void contextDestroyed(ServletContextEvent event)
	{

	}
}
