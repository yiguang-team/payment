package com.yiguang.payment.payment.controller;

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
import com.yiguang.payment.common.utils.StringUtil;
import com.yiguang.payment.payment.entity.Merchant;
import com.yiguang.payment.payment.service.ChannelMerchantRelationService;
import com.yiguang.payment.payment.service.MerchantService;
import com.yiguang.payment.payment.vo.ChannelMerchantRelationVO;
import com.yiguang.payment.payment.vo.MerchantVO;

/**
 * LoginController负责打开登录页面(GET请求)和登录出错页面(POST请求)， 真正登录的POST请求由Filter完成,
 * 
 * @author calvin
 */
@Controller
@RequestMapping(value = "/payment/management/merchant")
public class MerchantController
{
	@Autowired
	HttpSession session;

	@Autowired
	private MerchantService merchantService;

	@Autowired
	private ChannelMerchantRelationService relationService;

	@Autowired
	private DataSourceService dataSourceService;

	private static final String PAGE_SIZE = "10";

	private static Logger logger = LoggerFactory.getLogger(MerchantController.class);

	@RequestMapping(value = "/merchantList")
	public String merchantList(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "pageSize", defaultValue = PAGE_SIZE) int pageSize,
			@RequestParam(value = "sortType", defaultValue = "auto") String sortType,
			@RequestParam(value = "name", defaultValue = "") String name,
			@RequestParam(value = "adminUser", defaultValue = "-1") int adminUser,
			@RequestParam(value = "status", defaultValue = "-1") int status, Model model, ServletRequest request)
	{
		try
		{
			MerchantVO vo = new MerchantVO();
			vo.setName(name);
			vo.setStatus(status);
			vo.setAdminUser(adminUser);
			
			YcPage<MerchantVO> page_list = merchantService.queryMerchantList(vo, pageNumber, pageSize, "");

			// List<OptionVO> typeList =
			// dataSourceService.findOptions(CommonConstant.DataSourceName.MERCAHNT_TYPE);

			List<OptionVO> statusList = dataSourceService.findOpenOptions(CommonConstant.DataSourceName.COMMON_STATUS);
			List<OptionVO> userList = dataSourceService.findOpenOptions(CommonConstant.DataSourceName.USER);
			model.addAttribute("status", status);
			model.addAttribute("adminUser", adminUser);
			model.addAttribute("userList", userList);
			// model.addAttribute("typeList", typeList);
			model.addAttribute("statusList", statusList);
			model.addAttribute("name", name);
			model.addAttribute("mlist", page_list.getList());
			model.addAttribute("page", pageNumber);
			model.addAttribute("pageSize", pageSize);
			model.addAttribute("counttotal", page_list.getCountTotal() + "");
			model.addAttribute("pagetotal", page_list.getPageTotal() + "");

			return "/payment/management/merchant/merchantList";
		}
		catch (Exception e)
		{
			logger.error(e.getMessage());
			return "/error/500";
		}

	}

	@RequestMapping(method = RequestMethod.POST, value = "/updateMerchantStatus")
	public @ResponseBody
	String updateMerchantStatus(Merchant merchant, ModelMap model)
	{

		Map<String, String> result = new HashMap<String, String>();
		try
		{
			merchantService.updateMerchantStatus(merchant);

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

	@RequestMapping(method = RequestMethod.POST, value = "/deleteMerchant")
	public @ResponseBody
	String deleteMerchant(Merchant merchant, ModelMap model)
	{

		Map<String, String> result = new HashMap<String, String>();
		try
		{
			merchantService.deleteMerchant(merchant);

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

	@RequestMapping(value = "/editMerchant")
	public String editMerchant(Merchant merchant, ModelMap model)
	{
		try
		{
			merchant = merchantService.queryMerchant(merchant.getId());

			List<OptionVO> statusList = dataSourceService.findOpenOptions(CommonConstant.DataSourceName.COMMON_STATUS);
			List<OptionVO> userList = dataSourceService.findOpenOptions(CommonConstant.DataSourceName.USER);
			// List<OptionVO> typeList =
			// dataSourceService.findOptions(CommonConstant.DataSourceName.MERCAHNT_TYPE);
			List<OptionVO> channelMerchantRelationList = dataSourceService
					.findOpenOptionsWithoutPlease(CommonConstant.DataSourceName.CHANNEL);
			List<ChannelMerchantRelationVO> checkedlist = relationService
					.queryChannelMerchantRelationVOByMerchantId(merchant.getId());
			model.addAttribute("merchant", merchantService.copyPropertiesToVO(merchant));
			model.addAttribute("userList", userList);
			model.addAttribute("statusList", statusList);
			model.addAttribute("checkedlist", checkedlist);
			// model.addAttribute("typeList", typeList);
			model.addAttribute("channelMerchantRelationList", channelMerchantRelationList);
			return "/payment/management/merchant/editMerchant";
		}
		catch (Exception e)
		{
			return "/error/500";
		}

	}

	@RequestMapping(value = "/createMerchant")
	public String createMerchant(Model model)
	{
		// List<OptionVO> typeList =
		// dataSourceService.findOptions(CommonConstant.DataSourceName.MERCAHNT_TYPE);
		try
		{
			List<OptionVO> statusList = dataSourceService.findOpenOptions(CommonConstant.DataSourceName.COMMON_STATUS);
			List<OptionVO> userList = dataSourceService.findOpenOptions(CommonConstant.DataSourceName.USER);
			List<OptionVO> channelMerchantRelationList = dataSourceService
					.findOpenOptionsWithoutPlease(CommonConstant.DataSourceName.CHANNEL);
			model.addAttribute("channelMerchantRelationList", channelMerchantRelationList);
			model.addAttribute("userList", userList);
			model.addAttribute("statusList", statusList);
			// model.addAttribute("typeList", typeList);
			return "/payment/management/merchant/editMerchant";
		}
		catch (Exception e)
		{
			return "/error/500";
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = "/saveMerchant")
	public @ResponseBody
	String saveMerchant(MerchantVO merchantVO, Model model)
	{
		Map<String, String> result = new HashMap<String, String>();
		try
		{
			String regex_a = "[`~!@#$%^&*(){}'?/-=\\.<>￥%]";

			if (StringUtil.isBlank(merchantVO.getName()))
			{
				result.put("result", "error");
				result.put("message", "操作失败[" + "请填写名称" + "]");
			}
			else if (Pattern.matches(regex_a, merchantVO.getName()))
			{
				result.put("result", "error");
				result.put("message", "操作失败[" + "输入值不能为空和包含其他非法字符" + "]");
			}
			else if (StringUtil.isBlank(String.valueOf(merchantVO.getStatus())))
			{
				result.put("result", "error");
				result.put("message", "操作失败[" + "请选择状态" + "]");
			}
			else if (StringUtil.isBlank(String.valueOf(merchantVO.getAdminUser())))
			{
				result.put("result", "error");
				result.put("message", "操作失败[" + "请选择操作员" + "]");
			}
			else
			{
				// merchantService.saveMerchant(merchantVO);
				// result.put("result", "success");
				Merchant merchant = new Merchant();
				merchant.setId(merchantVO.getId());
				merchant.setName(merchantVO.getName());
				merchant.setStatus(merchantVO.getStatus());
				merchant.setRemark(merchantVO.getRemark());
				merchant.setKey(merchantVO.getKey());
				merchant.setNotifyUrl(merchantVO.getNotifyUrl());
				merchant.setAdminUser(merchantVO.getAdminUser());
				merchant = merchantService.saveMerchant(merchant, merchantVO.getChannelMerchantRelationIDs());

				// merchant = merchantService.saveMerchant(merchant);

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
