package com.yiguang.payment.payment.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.rpc.RpcException;
import com.yiguang.payment.common.CommonConstant;
import com.yiguang.payment.common.JsonTool;
import com.yiguang.payment.common.datasource.service.DataSourceService;
import com.yiguang.payment.common.datasource.vo.OptionVO;
import com.yiguang.payment.common.message.MessageResolver;
import com.yiguang.payment.common.query.YcPage;
import com.yiguang.payment.common.utils.StringUtil;
import com.yiguang.payment.payment.entity.BlackList;
import com.yiguang.payment.payment.service.BlackListService;
import com.yiguang.payment.payment.vo.BlackListVO;

/**
 * LoginController负责打开登录页面(GET请求)和登录出错页面(POST请求)， 真正登录的POST请求由Filter完成
 * 
 * @author ediosn
 * 
 */
@Controller
@RequestMapping(value = "/payment/risk/blackList")
public class BlackListController
{
	@Autowired
	HttpSession session;

	@Autowired
	private BlackListService blackListService;

	@Autowired
	private DataSourceService dataSourceService;

	private static final String PAGE_SIZE = "10";

	private static Logger logger = LoggerFactory.getLogger(BlackListController.class);

	@RequestMapping(value = "/blackList")
	public String blackList(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "pageSize", defaultValue = PAGE_SIZE) int pageSize,
			@RequestParam(value = "sortType", defaultValue = "auto") String sortType,
			@RequestParam(value = "type", defaultValue = "-1") int type,
			@RequestParam(value = "value", defaultValue = "") String value,
			@RequestParam(value = "status", defaultValue = "-1") int status, Model model, ServletRequest request)
	{
		try
		{
			BlackListVO vo = new BlackListVO();
			vo.setType(type);
			vo.setStatus(status);
			vo.setValue(value);
			

			YcPage<BlackListVO> page_list = blackListService.queryBlackList(vo, pageNumber, pageSize, "");
			List<OptionVO> statusList = dataSourceService.findOpenOptions(CommonConstant.DataSourceName.COMMON_STATUS);
			List<OptionVO> typeList = dataSourceService.findOpenOptions(CommonConstant.DataSourceName.LIST_TYPE);

			model.addAttribute("type", type);
			model.addAttribute("value", value);
			model.addAttribute("status", status);
			model.addAttribute("statusList", statusList);
			model.addAttribute("typeList", typeList);
			model.addAttribute("mlist", page_list.getList());
			model.addAttribute("page", pageNumber);
			model.addAttribute("pageSize", pageSize);
			model.addAttribute("counttotal", page_list.getCountTotal() + "");
			model.addAttribute("pagetotal", page_list.getPageTotal() + "");

			return "payment/risk/blackList/blackList";
		}
		catch (Exception e)
		{
			logger.error(e.getLocalizedMessage(), e);
			return "error/500";
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = "/deleteBlackList")
	public @ResponseBody
	String deleteBlackList(BlackList cardProduct, ModelMap model)
	{

		Map<String, String> result = new HashMap<String, String>();
		try
		{
			blackListService.deleteBlackList(cardProduct);
			result.put("result", "success");
		}
		catch (RpcException e)
		{
			result.put("result", "error");
			result.put("message", "操作失败[" + MessageResolver.getMessage(e.getCode()) + "]");
		}
		catch (Exception e)
		{
			logger.error(e.getLocalizedMessage(), e);
		}
		String json = JsonTool.beanToJson(result);

		return json;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/updateBlackListStatus")
	public @ResponseBody
	String updateBlackListStatus(BlackList blackList, ModelMap model)
	{

		Map<String, String> result = new HashMap<String, String>();
		try
		{
			blackListService.updateBlackListStatus(blackList);

			result.put("result", "success");
		}
		catch (RpcException e)
		{
			result.put("result", "error");
			result.put("message", "操作失败[" + MessageResolver.getMessage(e.getCode()) + "]");
		}
		catch (Exception e)
		{
			logger.error(e.getLocalizedMessage(), e);
		}
		String json = JsonTool.beanToJson(result);
		return json;
	}

	@RequestMapping(value = "/editBlackList")
	public String editBlackList(BlackList blackList, ModelMap model)
	{
		try
		{
			blackList = blackListService.queryBlackListById(blackList.getId());
			List<OptionVO> statusList = dataSourceService.findOpenOptions(CommonConstant.DataSourceName.COMMON_STATUS);
			List<OptionVO> typeList = dataSourceService.findOpenOptions(CommonConstant.DataSourceName.LIST_TYPE);

			model.addAttribute("typeList", typeList);
			model.addAttribute("statusList", statusList);
			model.addAttribute("blackList", blackList);
			return "payment/risk/blackList/editBlackList";
		}
		catch (Exception e)
		{
			logger.error(e.getLocalizedMessage(), e);
			return "error/500";
		}
	}

	@RequestMapping(value = "/createBlackList")
	public String createBlackList(Model model)
	{
		try
		{
			List<OptionVO> statusList = dataSourceService.findOpenOptions(CommonConstant.DataSourceName.COMMON_STATUS);
			List<OptionVO> typeList = dataSourceService.findOpenOptions(CommonConstant.DataSourceName.LIST_TYPE);
			model.addAttribute("statusList", statusList);
			model.addAttribute("typeList", typeList);
			return "payment/risk/blackList/editBlackList";
		}
		catch (Exception e)
		{
			logger.error(e.getLocalizedMessage(), e);
			return "error/500";
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = "/saveBlackList")
	public @ResponseBody
	String saveBlackList(BlackList blackList, ModelMap model)
	{

		Map<String, String> result = new HashMap<String, String>();
		try
		{
			if (StringUtil.isBlank(String.valueOf(blackList.getType())))
			{
				result.put("result", "error");
				result.put("message", "操作失败[" + "请选择类型" + "]");
			}
			else if (StringUtil.isBlank(String.valueOf(blackList.getStatus())))
			{
				result.put("result", "error");
				result.put("message", "操作失败[" + "请选择状态" + "]");
			}
			else if (StringUtil.isBlank(blackList.getValue()))
			{
				result.put("result", "error");
				result.put("message", "操作失败[" + "请输入值" + "]");
			}
			else
			{
				blackListService.saveBlackList(blackList);
				result.put("result", "success");
			}

		}
		catch (RpcException e)
		{
			result.put("result", "error");
			result.put("message", "操作失败[" + MessageResolver.getMessage(e.getCode()) + "]");
		}
		catch (Exception e)
		{
			logger.error(e.getLocalizedMessage(), e);
		}
		String json = JsonTool.beanToJson(result);
		return json;
	}
}
