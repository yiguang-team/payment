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
import org.springside.modules.persistence.SearchFilter.Operator;
import org.springside.modules.web.Servlets;

import com.alibaba.dubbo.rpc.RpcException;
import com.yiguang.payment.common.CommonConstant;
import com.yiguang.payment.common.JsonTool;
import com.yiguang.payment.common.datasource.service.DataSourceService;
import com.yiguang.payment.common.datasource.vo.OptionVO;
import com.yiguang.payment.common.message.MessageResolver;
import com.yiguang.payment.common.query.YcPage;
import com.yiguang.payment.common.utils.StringUtil;
import com.yiguang.payment.payment.entity.ChannelParms;
import com.yiguang.payment.payment.service.ChannelParmsService;
import com.yiguang.payment.payment.vo.ChannelParmsVO;
/**
 * LoginController负责打开登录页面(GET请求)和登录出错页面(POST请求)， 真正登录的POST请求由Filter完成,
 * 
 * @author Shinalon
 */
@Controller
@RequestMapping(value = "/payment/management/channelParms")
public class ChannelParmsController {
	@Autowired
	HttpSession session;

	@Autowired
	private ChannelParmsService channelParmsService;
	
	@Autowired
	private DataSourceService dataSourceService;

	private static final String PAGE_SIZE = "10";

	private static Logger logger = LoggerFactory.getLogger(ChannelParmsController.class);
	
	@RequestMapping(value = "/channelParmsList")
	public String channelParmsList(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "pageSize", defaultValue = PAGE_SIZE) int pageSize,
			@RequestParam(value = "sortType", defaultValue = "auto") String sortType,
			@RequestParam(value = "channelId", defaultValue = "-1") long channelId,
			@RequestParam(value = "key", defaultValue = "") String key,
			@RequestParam(value = "value", defaultValue = "-1") String value,
			@RequestParam(value = "status", defaultValue = "-1") int status, Model model, ServletRequest request) {
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		try {
			if (StringUtil.isNotBlank(String.valueOf(status)) && status != -1) {
				searchParams.put(Operator.EQ + "_" + "status", String.valueOf(status));
			}
			if (StringUtil.isNotBlank(String.valueOf(channelId)) && channelId != -1) {
				searchParams.put(Operator.EQ + "_" + "channelId", String.valueOf(channelId));
			}
			if (StringUtil.isNotBlank(key)) {
				key = key.trim();
				searchParams.put(Operator.LIKE + "_" + "key", key);
			}
			if (StringUtil.isNotBlank(value)) {
				value = value.trim();
				searchParams.put(Operator.LIKE + "_" + "value", value);
			}
			YcPage<ChannelParmsVO> page_list = channelParmsService.queryChannelParmsList(searchParams, pageNumber, pageSize, "");

			List<OptionVO> statusList = dataSourceService.findOpenOptions(CommonConstant.DataSourceName.COMMON_STATUS);
			List<OptionVO> channelList = dataSourceService.findOpenOptions(CommonConstant.DataSourceName.CHANNEL);
			model.addAttribute("status", status);
			model.addAttribute("statusList", statusList);
			model.addAttribute("key", key);
			model.addAttribute("value", value);
			model.addAttribute("channelList", channelList);
			model.addAttribute("mlist", page_list.getList());
			model.addAttribute("page", pageNumber);
			model.addAttribute("pageSize", pageSize);
			model.addAttribute("counttotal", page_list.getCountTotal() + "");
			model.addAttribute("pagetotal", page_list.getPageTotal() + "");

			return "payment/management/channel/channelParmsList";
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
			return "error/500";
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = "/updateChannelParmsStatus")
	public @ResponseBody
	String updateChannelParmsStatus(ChannelParms channelParms, ModelMap model) {

		Map<String, String> result = new HashMap<String, String>();
		try {
			channelParmsService.updateChannelParmsStatus(channelParms);

			result.put("result", "success");
		} catch (RpcException e) {
			result.put("result", "error");
			result.put("message", "操作失败[" + MessageResolver.getMessage(e.getCode()) + "]");
		}

		String json = JsonTool.beanToJson(result);

		return json;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/deleteChannel")
	public @ResponseBody
	String deleteChannelParms(ChannelParms channelParms, ModelMap model) {

		Map<String, String> result = new HashMap<String, String>();
		try {
			channelParmsService.deleteChannelParms(channelParms);

			result.put("result", "success");
		} catch (RpcException e) {
			result.put("result", "error");
			result.put("message", "操作失败[" + MessageResolver.getMessage(e.getCode()) + "]");
		}

		String json = JsonTool.beanToJson(result);

		return json;
	}

	@RequestMapping(value = "/editChannelParms")
	public String editChannelParms(ChannelParms channelParms, ModelMap model) {
		try {
			channelParms = channelParmsService.queryChannelParms(channelParms.getId());
			List<OptionVO> statusList = dataSourceService.findOpenOptions(CommonConstant.DataSourceName.COMMON_STATUS);
			List<OptionVO> channelList = dataSourceService.findOpenOptions(CommonConstant.DataSourceName.CHANNEL);
			model.addAttribute("channelParms", channelParms);
			model.addAttribute("channelList", channelList);
			model.addAttribute("statusList", statusList);
			return "payment/management/channel/editChannelParms";
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
			return "error/500";
		}

	}

	@RequestMapping(value = "/createChannelParms")
	public String createChannelParms(Model model) {
		try {
			List<OptionVO> statusList = dataSourceService.findOpenOptions(CommonConstant.DataSourceName.COMMON_STATUS);
		
			List<OptionVO> channelList = dataSourceService.findOpenOptions(CommonConstant.DataSourceName.CHANNEL);
		
			model.addAttribute("channelList", channelList);
			model.addAttribute("statusList", statusList);
			return "payment/management/channel/editChannelParms";
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
			return "error/500";
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = "/saveChannelParms")
	public @ResponseBody
	String saveChannelParms(ChannelParmsVO channelParmsVO, ModelMap model) {

		Map<String, String> result = new HashMap<String, String>();
		try {
			String regex_a = "[a-zA-Z]+";
			if (StringUtil.isBlank(channelParmsVO.getKey())) {
				result.put("result", "error");
				result.put("message", "操作失败[" + "请填写key" + "]");

			} else if (! Pattern.matches(regex_a, channelParmsVO.getKey())) {
				result.put("result", "error");
				result.put("message", "操作失败[" + "输入值只能是字母，区分大小写" + "]");

			} else if (StringUtil.isBlank(String.valueOf(channelParmsVO.getStatus()))) {
				result.put("result", "error");
				result.put("message", "操作失败[" + "请选择状态" + "]");

			} else if (StringUtil.isBlank(String.valueOf(channelParmsVO.getChannelId()))) {
				result.put("result", "error");
				result.put("message", "操作失败[" + "请选择渠道" + "]");

			} else if (StringUtil.isBlank(channelParmsVO.getValue())) {
				result.put("result", "error");
				result.put("message", "操作失败[" + "请输入值" + "]");

			} else {
				ChannelParms channelParms = new ChannelParms();
				channelParms.setId(channelParmsVO.getId());
				channelParms.setChannelId(channelParmsVO.getChannelId());
				channelParms.setKey(channelParmsVO.getKey());
				channelParms.setValue(channelParmsVO.getValue());
				channelParms.setStatus(channelParmsVO.getStatus());
				channelParms.setRemark(channelParmsVO.getRemark());
				channelParmsService.saveChannelParms(channelParms);
				result.put("result", "success");
			}
		} catch (RpcException e) {
			result.put("result", "error");
			result.put("message", "操作失败[" + MessageResolver.getMessage(e.getCode()) + "]");
		}
		String json = JsonTool.beanToJson(result);
		return json;
	}
}
