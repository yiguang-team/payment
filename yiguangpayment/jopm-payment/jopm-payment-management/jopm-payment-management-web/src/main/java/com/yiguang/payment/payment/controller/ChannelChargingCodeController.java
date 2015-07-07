package com.yiguang.payment.payment.controller;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

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
import com.yiguang.payment.common.utils.BigDecimalUtil;
import com.yiguang.payment.common.utils.StringUtil;
import com.yiguang.payment.payment.entity.ChannelChargingCode;
import com.yiguang.payment.payment.service.ChannelChargingCodeService;
import com.yiguang.payment.payment.vo.ChannelChargingCodeVO;

/**
 * LoginController负责打开登录页面(GET请求)和登录出错页面(POST请求)， 真正登录的POST请求由Filter完成,
 * 
 * @author Shinalon
 */
@Controller
@RequestMapping(value = "/payment/management/channel")
public class ChannelChargingCodeController
{

	@Autowired
	HttpSession session;

	@Autowired
	private ChannelChargingCodeService chargingCodeService;

	@Autowired
	private DataSourceService dataSourceService;

	private static final String PAGE_SIZE = "10";

	private static Logger logger = LoggerFactory.getLogger(ChannelChargingCodeController.class);

	@RequestMapping(value = "/chargingCodeList")
	public String chargingCodeList(
			@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "pageSize", defaultValue = PAGE_SIZE) int pageSize,
			@RequestParam(value = "sortType", defaultValue = "auto") String sortType,
			@RequestParam(value = "channelId", defaultValue = "-1") long channelId,
			// @RequestParam(value = "chargingAmount", defaultValue = "-1") long
			// chargingAmount,
			@RequestParam(value = "chargingCode", defaultValue = "") String chargingCode,
			@RequestParam(value = "status", defaultValue = "-1") int status, Model model, ServletRequest request)
	{
		try
		{
			ChannelChargingCodeVO vo = new ChannelChargingCodeVO();
			vo.setChannelId(channelId);
			vo.setStatus(status);
			vo.setChargingCode(chargingCode);
			
			YcPage<ChannelChargingCodeVO> page_list = chargingCodeService.queryChargingCodeList(vo,
					pageNumber, pageSize, "");

			List<OptionVO> statusList = dataSourceService.findOpenOptions(CommonConstant.DataSourceName.COMMON_STATUS);
			List<OptionVO> channelList = dataSourceService.findOpenOptions(CommonConstant.DataSourceName.CHANNEL);
			model.addAttribute("status", status);
			model.addAttribute("channelid", channelId);
			// model.addAttribute("chargingAmount", chargingAmount);
			model.addAttribute("chargingCode", chargingCode);
			model.addAttribute("channelList", channelList);
			model.addAttribute("statusList", statusList);
			model.addAttribute("mlist", page_list.getList());
			model.addAttribute("page", pageNumber);
			model.addAttribute("pageSize", pageSize);
			model.addAttribute("counttotal", page_list.getCountTotal() + "");
			model.addAttribute("pagetotal", page_list.getPageTotal() + "");

			return "/payment/management/channel/chargingCodeList";
		}
		catch (Exception e)
		{
			logger.error(e.getLocalizedMessage());
			return "error/500";
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = "/updateChargingCodeStatus")
	public @ResponseBody
	String updateChargingCodeStatus(ChannelChargingCode chargingCode, ModelMap model)
	{

		Map<String, String> result = new HashMap<String, String>();
		try
		{
			chargingCodeService.updateChargingCodeStatus(chargingCode);

			result.put("result", "success");
		}
		catch (RpcException e)
		{
			result.put("result", "error");
			result.put("message", "操作失败[" + MessageResolver.getMessage(e.getCode()) + "]");
		}
		String json = JsonTool.beanToJson(result);
		return json;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/deleteChargingCode")
	public @ResponseBody
	String deleteChargingCode(ChannelChargingCode chargingCode, ModelMap model)
	{

		Map<String, String> result = new HashMap<String, String>();
		try
		{
			chargingCodeService.deleteChargingCode(chargingCode);

			result.put("result", "success");
		}
		catch (RpcException e)
		{
			result.put("result", "error");
			result.put("message", "操作失败[" + MessageResolver.getMessage(e.getCode()) + "]");
		}
		String json = JsonTool.beanToJson(result);
		return json;
	}

	@RequestMapping(value = "/editChargingCode")
	public String editChargingCode(ChannelChargingCode chargingCode, ModelMap model)
	{
		try
		{
			chargingCode = chargingCodeService.queryChargingCode(chargingCode.getId());
			List<OptionVO> statusList = dataSourceService.findOpenOptions(CommonConstant.DataSourceName.COMMON_STATUS);
			List<OptionVO> channelList = dataSourceService.findOpenOptions(CommonConstant.DataSourceName.CHANNEL);
			model.addAttribute("chargingCode", chargingCodeService.copyPropertiesToVO(chargingCode));
			model.addAttribute("statusList", statusList);
			model.addAttribute("channelList", channelList);
			return "/payment/management/channel/editChargingCode";
		}
		catch (Exception e)
		{
			logger.error(e.getLocalizedMessage());
			return "error/500";
		}

	}

	@RequestMapping(value = "/createChargingCode")
	public String createChargingCode(Model model)
	{
		try
		{
			List<OptionVO> statusList = dataSourceService.findOpenOptions(CommonConstant.DataSourceName.COMMON_STATUS);
			List<OptionVO> channelList = dataSourceService.findOpenOptions(CommonConstant.DataSourceName.CHANNEL);
			model.addAttribute("channelList", channelList);
			model.addAttribute("statusList", statusList);
			return "/payment/management/channel/editChargingCode";
		}
		catch (Exception e)
		{
			logger.error(e.getLocalizedMessage());
			return "error/500";
		}

	}

	@RequestMapping(method = RequestMethod.POST, value = "/saveChargingCode")
	public @ResponseBody
	String saveChargingCode(ChannelChargingCode chargingCode, ModelMap model)
	{

		Map<String, String> result = new HashMap<String, String>();
		try
		{
			String regex_a = "[`~!@#$%^&*(){}'?/-=\\.<>￥%]";
			String regex_b = "^\\d+(\\.\\d+)?";
			if (StringUtil.isBlank(String.valueOf(chargingCode.getChannelId())))
			{
				result.put("result", "error");
				result.put("message", "操作失败[" + "请选择渠道" + "]");

			}
			else if (StringUtil.isBlank(String.valueOf(chargingCode.getStatus())))
			{
				result.put("result", "error");
				result.put("message", "操作失败[" + "请选择状态" + "]");

			}
			else if (StringUtil.isBlank(String.valueOf(chargingCode.getChargingAmount())))
			{
				result.put("result", "error");
				result.put("message", "操作失败[" + "请输入计费值" + "]");
			}
			else if (!Pattern.matches(regex_b, (CharSequence) chargingCode.getChargingAmount().toString()))
			{
				result.put("result", "error");
				result.put("message", "操作失败[" + "请输入数字，并确保格式正确" + "]");
			}
			else if (StringUtil.isBlank(String.valueOf(chargingCode.getChargingCode())))
			{
				result.put("result", "error");
				result.put("message", "操作失败[" + "请输入计费编码" + "]");
			}
			else if (Pattern.matches(regex_a, chargingCode.getChargingCode()))
			{
				result.put("result", "error");
				result.put("message", "操作失败[" + "输入值不能为空和包含其他非法字符" + "]");
			}
			else
			{
				chargingCode.setChargingAmount(BigDecimalUtil.multiply(chargingCode.getChargingAmount(),
						new BigDecimal("100"), 0, BigDecimal.ROUND_HALF_UP));
				chargingCodeService.saveChargingCode(chargingCode);
				result.put("result", "success");
			}
		}
		catch (RpcException e)
		{
			result.put("result", "error");
			result.put("message", "操作失败[" + MessageResolver.getMessage(e.getCode()) + "]");
		}
		String json = JsonTool.beanToJson(result);
		return json;
	}
}
