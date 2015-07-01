package com.yiguang.payment.common.datasource.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.rpc.RpcException;
import com.yiguang.payment.common.datasource.service.DataSourceService;
import com.yiguang.payment.common.datasource.vo.OptionVO;

/**
 * LoginController负责打开登录页面(GET请求)和登录出错页面(POST请求)， 真正登录的POST请求由Filter完成,
 * 
 * @author calvin
 */
@Controller
@RequestMapping(value = "/business/dataSource")
public class DataSourceController
{
	@Autowired
	HttpSession session;

	@Autowired
	private DataSourceService dataSourceService;

	private static Logger logger = LoggerFactory.getLogger(DataSourceController.class);

	@RequestMapping(value = "/changeSonString", method = RequestMethod.POST)
	public @ResponseBody
	String changeSonString(@RequestParam(value = "parentId", defaultValue = "") String parentId,
			@RequestParam(value = "dataSourceCode", defaultValue = "") String dataSourceCode, Model model)
	{
		logger.debug("changeSonString start");
		try
		{
			List<OptionVO> list = dataSourceService.findOpenOptionsByParent(dataSourceCode, parentId);
			String html = "";
			for (OptionVO op : list)
			{
				html = html + "<option value=\"" + op.getValue() + "\">" + op.getText() + "</option>";
			}
			logger.debug("changeSonString end, html is [" + html + "]");
			return html;
		}
		catch (RpcException e)
		{
			logger.error("changeSonString failed");
			return "";
		}
		catch (Exception e)
		{
			logger.error("changeSonString failed");
			logger.error(e.getLocalizedMessage(), e);
			return "";
		}

	}

	@RequestMapping(value = "/changeSonLong", method = RequestMethod.POST)
	public @ResponseBody
	String changeSonLong(@RequestParam(value = "parentId", defaultValue = "-1") long parentId,
			@RequestParam(value = "dataSourceCode", defaultValue = "") String dataSourceCode, Model model)
	{

		logger.debug("changeSonLong start");
		try
		{
			List<OptionVO> list = dataSourceService.findOpenOptionsByParent(dataSourceCode, String.valueOf(parentId));
			String html = "";
			for (OptionVO op : list)
			{
				html = html + "<option value=\"" + op.getValue() + "\">" + op.getText() + "</option>";
			}
			logger.debug("changeSonLong end, html is [" + html + "]");
			return html;
		}
		catch (RpcException e)
		{
			logger.error("changeSonString failed");
			return "";
		}
		catch (Exception e)
		{
			logger.error("changeSonString failed");
			logger.error(e.getLocalizedMessage(), e);
			return "";
		}

	}
	
	@RequestMapping(value = "/changeSonStringWithoutPlease", method = RequestMethod.POST)
	public @ResponseBody
	String changeSonStringWithoutPlease(@RequestParam(value = "parentId", defaultValue = "") String parentId,
			@RequestParam(value = "dataSourceCode", defaultValue = "") String dataSourceCode, Model model)
	{
		logger.debug("changeSonString start");
		try
		{
			List<OptionVO> list = dataSourceService.findOpenOptionsByParentWithoutPlease(dataSourceCode, parentId);
			String html = "";
			for (OptionVO op : list)
			{
				html = html + "<option value=\"" + op.getValue() + "\">" + op.getText() + "</option>";
			}
			logger.debug("changeSonString end, html is [" + html + "]");
			return html;
		}
		catch (RpcException e)
		{
			logger.error("changeSonString failed");
			return "";
		}
		catch (Exception e)
		{
			logger.error("changeSonString failed");
			logger.error(e.getLocalizedMessage(), e);
			return "";
		}

	}

	@RequestMapping(value = "/changeSonLongWithoutPlease", method = RequestMethod.POST)
	public @ResponseBody
	String changeSonLongWithoutPlease(@RequestParam(value = "parentId", defaultValue = "-1") long parentId,
			@RequestParam(value = "dataSourceCode", defaultValue = "") String dataSourceCode, Model model)
	{

		logger.debug("changeSonLong start");
		try
		{
			List<OptionVO> list = dataSourceService.findOpenOptionsByParentWithoutPlease(dataSourceCode, String.valueOf(parentId));
			String html = "";
			for (OptionVO op : list)
			{
				html = html + "<option value=\"" + op.getValue() + "\">" + op.getText() + "</option>";
			}
			logger.debug("changeSonLong end, html is [" + html + "]");
			return html;
		}
		catch (RpcException e)
		{
			logger.error("changeSonString failed");
			return "";
		}
		catch (Exception e)
		{
			logger.error("changeSonString failed");
			logger.error(e.getLocalizedMessage(), e);
			return "";
		}

	}
}
