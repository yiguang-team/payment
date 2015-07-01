package com.yiguang.payment.common.logging.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springside.modules.persistence.SearchFilter.Operator;
import org.springside.modules.web.Servlets;

import com.alibaba.dubbo.rpc.RpcException;
import com.yiguang.payment.common.CommonConstant;
import com.yiguang.payment.common.datasource.service.DataSourceService;
import com.yiguang.payment.common.datasource.vo.OptionVO;
import com.yiguang.payment.common.logging.service.LoginAndLogoutLogService;
import com.yiguang.payment.common.logging.vo.LoginAndLogoutLogVO;
import com.yiguang.payment.common.query.YcPage;
import com.yiguang.payment.common.utils.StringUtil;

/**
 * LoginController负责打开登录页面(GET请求)和登录出错页面(POST请求)， 真正登录的POST请求由Filter完成,
 * 
 * @author calvin
 */
@Controller
@RequestMapping(value = "/logging/loginAndLogoutLog")
public class LoginAndLogoutLogController
{
	@Autowired
	HttpSession session;

	@Autowired
	private LoginAndLogoutLogService loginAndLogoutLogService;

	@Autowired
	private DataSourceService dataSourceService;

	private static final String PAGE_SIZE = "10";

	private static Logger logger = LoggerFactory.getLogger(LoginAndLogoutLogController.class);

	@RequestMapping(value = "/showLogList")
	public String showLogList(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "pageSize", defaultValue = PAGE_SIZE) int pageSize,
			@RequestParam(value = "sortType", defaultValue = "auto") String sortType,
			@RequestParam(value = "operationType", defaultValue = "-1") int operationType,
			@RequestParam(value = "username", defaultValue = "") String username, Model model, ServletRequest request)
	{
		logger.debug("showLogList start");
		try
		{
			logger.debug("[查询登陆日志请求开始][url:/showLogList]--------------------------------------------");
			// 组装查询条件
			Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");

			if (StringUtil.isNotBlank(String.valueOf(operationType)) && operationType != -1)
			{
				searchParams.put(Operator.EQ + "_" + "operationType", String.valueOf(operationType));
			}

	
			if (StringUtil.isNotBlank(username))
			{
				searchParams.put(Operator.EQ + "_" + "username", username);
			}

			// 获取操作类型名称，ID键值对
			List<OptionVO> operationTypeList = dataSourceService
					.findOpenOptions(CommonConstant.DataSourceName.LOGIN_LOGOUT_TYPE);

			logger.debug("[查询登陆日志数据库开始][url:/showLogList]--------------------------------------------");
			// 查询登陆日志信息
			YcPage<LoginAndLogoutLogVO> page_list = loginAndLogoutLogService.showLogList(searchParams, pageNumber,
					pageSize, "");
			logger.debug("[查询登陆日志数据库结束][url:/showLogList]--------------------------------------------");

			// 将数据传入页面中
			model.addAttribute("operationType", operationType);
			model.addAttribute("username", username);
			model.addAttribute("mlist", page_list.getList());
			model.addAttribute("operationTypeList", operationTypeList);
			model.addAttribute("page", pageNumber);
			model.addAttribute("pageSize", pageSize);
			model.addAttribute("counttotal", page_list.getCountTotal() + "");
			model.addAttribute("pagetotal", page_list.getPageTotal() + "");
			logger.debug("[查询登陆日志请求结束][url:/showLogList]--------------------------------------------");
			return "logging/loginAndLogoutLog/showLogList";
		}
		catch (RpcException e)
		{
			logger.error("showLogList failed");
			return "error/500";
		}
		catch (Exception e)
		{
			logger.error("showLogList failed");
			logger.error(e.getLocalizedMessage(), e);
			return "error/500";
		}

	}

}
