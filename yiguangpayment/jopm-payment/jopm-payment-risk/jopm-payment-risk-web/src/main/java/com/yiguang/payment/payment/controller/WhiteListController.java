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
import com.yiguang.payment.payment.entity.WhiteList;
import com.yiguang.payment.payment.service.WhiteListService;
import com.yiguang.payment.payment.vo.WhiteListVO;

/**
 * LoginController负责打开登录页面(GET请求)和登录出错页面(POST请求)， 真正登录的POST请求由Filter完成
 * 
 * @author ediosn
 * 
 */
@Controller
@RequestMapping(value = "/payment/risk/whiteList")
public class WhiteListController
{
	@Autowired
	HttpSession session;

	@Autowired
	private WhiteListService whiteListService;

	@Autowired
	private DataSourceService dataSourceService;

	private static final String PAGE_SIZE = "10";

	private static Logger logger = LoggerFactory.getLogger(WhiteListController.class);

	@RequestMapping(value = "/whiteList")
	public String merchantLimitList(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "pageSize", defaultValue = PAGE_SIZE) int pageSize,
			@RequestParam(value = "sortType", defaultValue = "auto") String sortType,
			@RequestParam(value = "type", defaultValue = "-1") int type,
			@RequestParam(value = "value", defaultValue = "") String value,
			@RequestParam(value = "status", defaultValue = "-1") int status, Model model, ServletRequest request)
	{
		try
		{
			WhiteListVO vo = new WhiteListVO();
			vo.setType(type);
			vo.setStatus(status);
			vo.setValue(value);
			
			YcPage<WhiteListVO> page_list = whiteListService.queryWhiteList(vo, pageNumber, pageSize, "");
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

			return "payment/risk/whiteList/whiteList";
		}
		catch (Exception e)
		{
			logger.error(e.getLocalizedMessage(), e);
			return "error/500";
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = "/deleteWhiteList")
	public @ResponseBody
	String deleteWhiteList(WhiteList cardProduct, ModelMap model)
	{

		Map<String, String> result = new HashMap<String, String>();
		try
		{
			whiteListService.deleteWhiteList(cardProduct);
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

	@RequestMapping(method = RequestMethod.POST, value = "/updateWhiteListStatus")
	public @ResponseBody
	String updateWhiteListStatus(WhiteList whiteList, ModelMap model)
	{

		Map<String, String> result = new HashMap<String, String>();
		try
		{
			whiteListService.updateWhiteListStatus(whiteList);

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

	@RequestMapping(value = "/editWhiteList")
	public String editWhiteList(WhiteList whiteList, ModelMap model)
	{
		try
		{
			whiteList = whiteListService.queryWhiteList(whiteList.getId());
			List<OptionVO> statusList = dataSourceService.findOpenOptions(CommonConstant.DataSourceName.COMMON_STATUS);
			List<OptionVO> typeList = dataSourceService.findOpenOptions(CommonConstant.DataSourceName.LIST_TYPE);

			model.addAttribute("typeList", typeList);
			model.addAttribute("statusList", statusList);
			model.addAttribute("whiteList", whiteList);
			return "payment/risk/whiteList/editWhiteList";
		}
		catch (Exception e)
		{
			logger.error(e.getLocalizedMessage(), e);
			return "error/500";
		}
	}

	@RequestMapping(value = "/createWhiteList")
	public String createWhiteList(Model model)
	{
		try
		{
			List<OptionVO> statusList = dataSourceService.findOpenOptions(CommonConstant.DataSourceName.COMMON_STATUS);
			List<OptionVO> typeList = dataSourceService.findOpenOptions(CommonConstant.DataSourceName.LIST_TYPE);
			model.addAttribute("statusList", statusList);
			model.addAttribute("typeList", typeList);
			return "payment/risk/whiteList/editWhiteList";
		}
		catch (Exception e)
		{
			logger.error(e.getLocalizedMessage(), e);
			return "error/500";
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = "/saveWhiteList")
	public @ResponseBody
	String saveWhiteList(WhiteList whiteList, ModelMap model)
	{

		Map<String, String> result = new HashMap<String, String>();
		try
		{
			if (StringUtil.isBlank(String.valueOf(whiteList.getType())))
			{
				result.put("result", "error");
				result.put("message", "操作失败[" + "请选择类型" + "]");
			}
			else if (StringUtil.isBlank(String.valueOf(whiteList.getStatus())))
			{
				result.put("result", "error");
				result.put("message", "操作失败[" + "请选择状态" + "]");
			}
			else if (StringUtil.isBlank(whiteList.getValue()))
			{
				result.put("result", "error");
				result.put("message", "操作失败[" + "请输入值" + "]");
			}
			else
			{
				whiteListService.saveWhiteList(whiteList);
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
