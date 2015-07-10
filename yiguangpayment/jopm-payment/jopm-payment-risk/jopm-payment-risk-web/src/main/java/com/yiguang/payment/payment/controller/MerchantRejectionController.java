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
import com.yiguang.payment.payment.entity.MerchantRejection;
import com.yiguang.payment.payment.service.MerchantRejectionService;
import com.yiguang.payment.payment.vo.MerchantRejectionVO;

/**
 * LoginController负责打开登录页面(GET请求)和登录出错页面(POST请求)， 真正登录的POST请求由Filter完成
 * 
 * @author ediosn
 * 
 */
@Controller
@RequestMapping(value = "/payment/risk/merchantRejection")
public class MerchantRejectionController
{
	@Autowired
	HttpSession session;

	@Autowired
	private MerchantRejectionService merchantRejectionService;

	@Autowired
	private DataSourceService dataSourceService;

	private static final String PAGE_SIZE = "10";

	private static Logger logger = LoggerFactory.getLogger(MerchantRejectionController.class);

	@RequestMapping(value = "/merchantRejectionList")
	public String merchantRejectionList(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "pageSize", defaultValue = PAGE_SIZE) int pageSize,
			@RequestParam(value = "sortType", defaultValue = "auto") String sortType,
			@RequestParam(value = "merchantA", defaultValue = "-1") long merchantA,
			@RequestParam(value = "merchantB", defaultValue = "-1") long merchantB,
			@RequestParam(value = "status", defaultValue = "-1") int status,
			@RequestParam(value = "remark", defaultValue = "") String remark, Model model, ServletRequest request)
	{
		try
		{
			MerchantRejectionVO vo = new MerchantRejectionVO();
			vo.setMerchantA(merchantA);
			vo.setStatus(status);
			vo.setMerchantB(merchantB);
			vo.setRemark(remark);
			
			YcPage<MerchantRejectionVO> page_list = merchantRejectionService.queryMerchantRejectionList(vo,
					pageNumber, pageSize, "");

			List<OptionVO> merchantList = dataSourceService.findOpenOptions(CommonConstant.DataSourceName.MERCHANT);
			List<OptionVO> statusList = dataSourceService.findOpenOptions(CommonConstant.DataSourceName.COMMON_STATUS);
			model.addAttribute("merchantA", merchantA);
			model.addAttribute("merchantB", merchantB);
			model.addAttribute("status", status);
			model.addAttribute("statusList", statusList);
			model.addAttribute("merchantList", merchantList);
			model.addAttribute("mlist", page_list.getList());
			model.addAttribute("page", pageNumber);
			model.addAttribute("pageSize", pageSize);
			model.addAttribute("counttotal", page_list.getCountTotal() + "");
			model.addAttribute("pagetotal", page_list.getPageTotal() + "");

			return "payment/risk/merchantRejection/merchantRejectionList";
		}
		catch (Exception e)
		{
			logger.error(e.getLocalizedMessage(), e);
			return "error/500";
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = "/deleteMerchantRejection")
	public @ResponseBody
	String deleteMerchantRejection(MerchantRejection cardProduct, ModelMap model)
	{

		Map<String, String> result = new HashMap<String, String>();
		try
		{
			merchantRejectionService.deleteMerchantRejection(cardProduct);
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
	
	@RequestMapping(method = RequestMethod.POST, value = "/updateMerchantRejection")
	public @ResponseBody
	String updateMerchantRejection(MerchantRejection merchantRejection, ModelMap model)
	{

		Map<String, String> result = new HashMap<String, String>();
		try
		{
			merchantRejectionService.updateMerchantRejection(merchantRejection);

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

	@RequestMapping(value = "/editMerchantRejection")
	public String editMerchantRejection(MerchantRejection merchantRejection, ModelMap model)
	{
		try
		{
			MerchantRejection rejection = merchantRejectionService.queryMerchantRejection(merchantRejection.getId());
			long merchantAId = rejection.getMerchantA();
			long merchantBId = rejection.getMerchantB();
			merchantRejection = merchantRejectionService.queryMerchantRejectionByMerchant(merchantAId, merchantBId);
			List<OptionVO> merchantList = dataSourceService.findOpenOptions(CommonConstant.DataSourceName.MERCHANT);
			List<OptionVO> statusList = dataSourceService.findOpenOptions(CommonConstant.DataSourceName.COMMON_STATUS);
			model.addAttribute("merchantRejection", merchantRejection);
			model.addAttribute("merchantList", merchantList);
			model.addAttribute("statusList", statusList);
			return "payment/risk/merchantRejection/editMerchantRejection";
		}
		catch (Exception e)
		{
			logger.error(e.getLocalizedMessage(), e);
			return "error/500";
		}
	}

	@RequestMapping(value = "/createMerchantRejection")
	public String createMerchantRejection(Model model)
	{
		try
		{
			List<OptionVO> merchantList = dataSourceService.findOpenOptions(CommonConstant.DataSourceName.MERCHANT);
			List<OptionVO> statusList = dataSourceService.findOpenOptions(CommonConstant.DataSourceName.COMMON_STATUS);
			model.addAttribute("merchantList", merchantList);
			model.addAttribute("statusList", statusList);
			return "payment/risk/merchantRejection/editMerchantRejection";
		}
		catch (Exception e)
		{
			logger.error(e.getLocalizedMessage(), e);
			return "error/500";
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = "/saveMerchantRejection")
	public @ResponseBody
	String saveMerchantRejection(MerchantRejection merchantRejection, ModelMap model)
	{

		Map<String, String> result = new HashMap<String, String>();
		try
		{
			if (StringUtil.isBlank(String.valueOf(merchantRejection.getMerchantA())))
			{
				result.put("result", "error");
				result.put("message", "操作失败[" + "请选择商户" + "]");

			}
			else if (StringUtil.isBlank(String.valueOf(merchantRejection.getMerchantB()))
					&& merchantRejection.getMerchantA() != merchantRejection.getMerchantB())
			{
				result.put("result", "error");
				result.put("message", "操作失败[" + "请选择商户" + "]");

			}
			else if (StringUtil.isBlank(String.valueOf(merchantRejection.getStatus())))
			{
				result.put("result", "error");
				result.put("message", "操作失败[" + "请选择状态" + "]");

			}
			else
			{
				merchantRejectionService.saveMerchantRejection(merchantRejection);
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
