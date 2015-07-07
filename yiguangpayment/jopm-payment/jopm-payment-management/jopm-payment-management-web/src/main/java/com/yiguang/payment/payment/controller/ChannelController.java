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
import com.yiguang.payment.payment.entity.Channel;
import com.yiguang.payment.payment.service.CarrierChannelRelationService;
import com.yiguang.payment.payment.service.ChannelService;
import com.yiguang.payment.payment.vo.CarrierChannelRelationVO;
import com.yiguang.payment.payment.vo.ChannelVO;

/**
 * LoginController负责打开登录页面(GET请求)和登录出错页面(POST请求)， 真正登录的POST请求由Filter完成,
 * 
 * @author Shinalon
 */
@Controller
@RequestMapping(value = "/payment/management/channel")
public class ChannelController {

	@Autowired
	HttpSession session;

	@Autowired
	private ChannelService channelService;
	
	@Autowired
	private CarrierChannelRelationService relationService;
	
	@Autowired
	private DataSourceService dataSourceService;

	private static final String PAGE_SIZE = "10";

	private static Logger logger = LoggerFactory.getLogger(ChannelController.class);

	@RequestMapping(value = "/channelList")
	public String channelList(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "pageSize", defaultValue = PAGE_SIZE) int pageSize,
			@RequestParam(value = "sortType", defaultValue = "auto") String sortType,
			@RequestParam(value = "name", defaultValue = "") String name,
			@RequestParam(value = "status", defaultValue = "-1") int status, Model model, ServletRequest request) {
		try {
			
			ChannelVO vo = new ChannelVO();
			vo.setName(name);
			vo.setStatus(status);
			YcPage<ChannelVO> page_list = channelService.queryChannelList(vo, pageNumber, pageSize, "");

			List<OptionVO> statusList = dataSourceService.findOpenOptions(CommonConstant.DataSourceName.COMMON_STATUS);
			
			model.addAttribute("status", status);
			model.addAttribute("statusList", statusList);
			model.addAttribute("name", name);
			model.addAttribute("mlist", page_list.getList());
			model.addAttribute("page", pageNumber);
			model.addAttribute("pageSize", pageSize);
			model.addAttribute("counttotal", page_list.getCountTotal() + "");
			model.addAttribute("pagetotal", page_list.getPageTotal() + "");

			return "payment/management/channel/channelList";
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
			return "error/500";
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = "/updateChannelStatus")
	public @ResponseBody
	String updateChannelStatus(Channel channel, ModelMap model) {

		Map<String, String> result = new HashMap<String, String>();
		try {
			channelService.updateChannelStatus(channel);

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
	String deleteChannel(Channel channel, ModelMap model) {

		Map<String, String> result = new HashMap<String, String>();
		try {
			channelService.deleteChannel(channel);

			result.put("result", "success");
		} catch (RpcException e) {
			result.put("result", "error");
			result.put("message", "操作失败[" + MessageResolver.getMessage(e.getCode()) + "]");
		}

		String json = JsonTool.beanToJson(result);

		return json;
	}

	@RequestMapping(value = "/editChannel")
	public String editChannel(Channel channel, ModelMap model) {
		try {
			channel = channelService.queryChannel(channel.getId());
			List<OptionVO> statusList = dataSourceService.findOpenOptions(CommonConstant.DataSourceName.COMMON_STATUS);
			List<OptionVO> carrierChannelRelationList = dataSourceService
					.findOpenOptionsWithoutPlease(CommonConstant.DataSourceName.CARRIER);
			List<CarrierChannelRelationVO> checkedlist = relationService
					.queryCarrierChannelRelationVOByChannelId(channel.getId());
			model.addAttribute("channel", channel);
		
			model.addAttribute("carrierChannelRelationList", carrierChannelRelationList);
			model.addAttribute("statusList", statusList);
			model.addAttribute("checkedlist", checkedlist);
			return "payment/management/channel/editChannel";
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
			return "error/500";
		}

	}

	@RequestMapping(value = "/createChannel")
	public String createChannel(Model model) {
		try {
			List<OptionVO> statusList = dataSourceService.findOpenOptions(CommonConstant.DataSourceName.COMMON_STATUS);
		
			List<OptionVO> carrierChannelRelationList = dataSourceService
					.findOpenOptionsWithoutPlease(CommonConstant.DataSourceName.CARRIER);
		
			model.addAttribute("carrierChannelRelationList", carrierChannelRelationList);
			model.addAttribute("statusList", statusList);
			return "payment/management/channel/editChannel";
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
			return "error/500";
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = "/saveChannel")
	public @ResponseBody
	String saveChannel(ChannelVO channelVO, ModelMap model) {

		Map<String, String> result = new HashMap<String, String>();
		try {
			String regex_a = "[`~!@#$%^&*(){}'?/-=\\.<>￥%]";
			if (StringUtil.isBlank(channelVO.getName())) {
				result.put("result", "error");
				result.put("message", "操作失败[" + "请填写名称" + "]");

			} else if (Pattern.matches(regex_a, channelVO.getName())) {
				result.put("result", "error");
				result.put("message", "操作失败[" + "输入值不能为空和包含其他非法字符" + "]");

			} else if (StringUtil.isBlank(String.valueOf(channelVO.getStatus()))) {
				result.put("result", "error");
				result.put("message", "操作失败[" + "请选择状态" + "]");

			} else {
				Channel channel = new Channel();
				channel.setId(channelVO.getId());
				channel.setName(channelVO.getName());
				channel.setStatus(channelVO.getStatus());
				channel.setRemark(channelVO.getRemark());
				channelService.saveChannel(channel, channelVO.getCarrierChannelRelationIDs());
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
