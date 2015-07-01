package com.yiguang.payment.payment.controller;

import java.text.SimpleDateFormat;
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
import com.yiguang.payment.payment.entity.BasicRule;
import com.yiguang.payment.payment.service.BasicRuleService;
import com.yiguang.payment.payment.vo.BasicRuleVO;

/**
 * LoginController负责打开登录页面(GET请求)和登录出错页面(POST请求)， 真正登录的POST请求由Filter完成
 * 
 * @author ediosn
 * 
 */
@Controller
@RequestMapping(value = "/payment/risk/basicRule")
public class BasicRuleController
{
	@Autowired
	HttpSession session;

	@Autowired
	private BasicRuleService basicRuleService;

	@Autowired
	private DataSourceService dataSourceService;

	private static final String PAGE_SIZE = "10";

	private static Logger logger = LoggerFactory.getLogger(BasicRuleController.class);

	@RequestMapping(value = "/basicRuleList")
	public String basicRuleList(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "pageSize", defaultValue = PAGE_SIZE) int pageSize,
			@RequestParam(value = "sortType", defaultValue = "auto") String sortType,
			@RequestParam(value = "timeType", defaultValue = "-1") int timeType,
			@RequestParam(value = "timeUnit", defaultValue = "-1") int timeUnit,
			@RequestParam(value = "startTime", defaultValue = "") String startTime,
			@RequestParam(value = "endTime", defaultValue = "") String endTime,
			@RequestParam(value = "limitType", defaultValue = "-1") int limitType,
			@RequestParam(value = "volume", defaultValue = "") String volume,
			@RequestParam(value = "mobile", defaultValue = "") String mobile,
			@RequestParam(value = "channelId", defaultValue = "-1") long channelId,
			@RequestParam(value = "provinceId", defaultValue = "") String provinceId,
			@RequestParam(value = "cityId", defaultValue = "") String cityId,
			@RequestParam(value = "merchantId", defaultValue = "-1") long merchantId,
			@RequestParam(value = "productId", defaultValue = "-1") long productId,
			@RequestParam(value = "pointId", defaultValue = "-1") long pointId,
			@RequestParam(value = "status", defaultValue = "-1") int status,
			@RequestParam(value = "action", defaultValue = "-1") int action,
			@RequestParam(value = "ip", defaultValue = "") String ip,
			@RequestParam(value = "username", defaultValue = "") String username,
			@RequestParam(value = "remark", defaultValue = "") String remark, Model model, ServletRequest request)
	{
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		try
		{
			if (StringUtil.isNotBlank(String.valueOf(timeType)) && timeType != -1)
			{
				searchParams.put(Operator.EQ + "_" + "timeType", String.valueOf(timeType));
			}
			if (StringUtil.isNotBlank(String.valueOf(timeUnit)) && timeUnit != -1)
			{
				searchParams.put(Operator.EQ + "_" + "timeUnit", String.valueOf(timeUnit));
			}
			if (StringUtil.isNotBlank(String.valueOf(limitType)) && limitType != -1)
			{
				searchParams.put(Operator.EQ + "_" + "limitType", String.valueOf(limitType));
			}
			if (StringUtil.isNotBlank(mobile))
			{
				mobile=  mobile.trim();
				searchParams.put(Operator.LIKE + "_" + "mobile", mobile);
			}
			if (StringUtil.isNotBlank(String.valueOf(channelId)) && channelId != -1)
			{
				searchParams.put(Operator.EQ + "_" + "channelId", String.valueOf(channelId));
			}
			if (StringUtil.isNotBlank(provinceId) && !provinceId.equals("-1"))
			{
				searchParams.put(Operator.EQ + "_" + "provinceId", provinceId);
			}
			if (StringUtil.isNotBlank(cityId) && !cityId.equals("-1"))
			{
				searchParams.put(Operator.EQ + "_" + "cityId", cityId);
			}
			if (StringUtil.isNotBlank(volume))
			{
				volume=  volume.trim();
				searchParams.put(Operator.EQ + "_" + "volume", volume);
			}
			if (StringUtil.isNotBlank(String.valueOf(action)) && action != -1)
			{
				searchParams.put(Operator.EQ + "_" + "action", String.valueOf(action));
			}
			if (StringUtil.isNotBlank(ip))
			{
				ip=  ip.trim();
				searchParams.put(Operator.LIKE + "_" + "ip", ip);
			}
			if (StringUtil.isNotBlank(username))
			{
				username=  username.trim();
				searchParams.put(Operator.LIKE + "_" + "username", username);
			}
			if (StringUtil.isNotBlank(String.valueOf(merchantId)) && merchantId != -1)
			{
				searchParams.put(Operator.EQ + "_" + "merchantId", String.valueOf(merchantId));
			}
			if (StringUtil.isNotBlank(String.valueOf(productId)) && productId != -1)
			{
				searchParams.put(Operator.EQ + "_" + "productId", String.valueOf(productId));
			}
			if (StringUtil.isNotBlank(String.valueOf(pointId)) && pointId != -1)
			{
				searchParams.put(Operator.EQ + "_" + "pointId", String.valueOf(pointId));
			}
			if (StringUtil.isNotBlank(String.valueOf(status)) && status != -1)
			{
				searchParams.put(Operator.EQ + "_" + "status", String.valueOf(status));
			}

			YcPage<BasicRuleVO> page_list = basicRuleService.queryBasicRuleList(searchParams, pageNumber,
					pageSize, "");

			List<OptionVO> timeTypeList = dataSourceService.findOpenOptions(CommonConstant.DataSourceName.RISK_TIME_TYPE);
			List<OptionVO> limitTypeList = dataSourceService.findOpenOptions(CommonConstant.DataSourceName.RISK_LIMIT_TYPE);
			List<OptionVO> statusList = dataSourceService.findOpenOptions(CommonConstant.DataSourceName.COMMON_STATUS);
			List<OptionVO> timeUnitList = dataSourceService.findOpenOptions(CommonConstant.DataSourceName.TIME_UNIT);
			List<OptionVO> actionList = dataSourceService.findOpenOptions(CommonConstant.DataSourceName.RISK_ACTION);
			
			
			List<OptionVO> merchantList = dataSourceService.findOpenOptions(CommonConstant.DataSourceName.MERCHANT);
			List<OptionVO> productList = dataSourceService.findOpenOptions(CommonConstant.DataSourceName.PRODUCT);
			List<OptionVO> pointList = dataSourceService.findOpenOptions(CommonConstant.DataSourceName.POINT);
			List<OptionVO> channelList = dataSourceService.findOpenOptions(CommonConstant.DataSourceName.CHANNEL);
			List<OptionVO> provinceList = dataSourceService.findOpenOptions(CommonConstant.DataSourceName.PROVINCE);
			List<OptionVO> cityList = dataSourceService.findOpenOptionsByParent(CommonConstant.DataSourceName.CITY,
					provinceId);
			
			OptionVO vo1 = new OptionVO();
			vo1.setValue("-9");
			vo1.setText("不限");
			
			OptionVO vo2 = new OptionVO();
			vo2.setValue("-8");
			vo2.setText("当前");
			
			merchantList.add(1, vo2);
			merchantList.add(1, vo1);
			productList.add(1, vo2);
			productList.add(1, vo1);
			pointList.add(1, vo2);
			pointList.add(1, vo1);
			channelList.add(1, vo2);
			channelList.add(1, vo1);
			provinceList.add(1, vo2);
			provinceList.add(1, vo1);
			cityList.add(1, vo2);
			cityList.add(1, vo1);
			
			model.addAttribute("timeType", timeType);
			model.addAttribute("timeTypeList", timeTypeList);
			
			model.addAttribute("timeUnit", timeUnit);
			model.addAttribute("timeUnitList", timeUnitList);
			
			model.addAttribute("limitType", limitType);
			model.addAttribute("limitTypeList", limitTypeList);
			
			model.addAttribute("mobile", mobile);
			model.addAttribute("channelId", channelId);
			model.addAttribute("channelList", channelList);
			model.addAttribute("provinceId", provinceId);
			model.addAttribute("provinceList", provinceList);
			model.addAttribute("cityId", cityId);
			model.addAttribute("cityList", cityList);
			
			model.addAttribute("volume", volume);
			model.addAttribute("action", action);		
			model.addAttribute("actionList", actionList);		
			model.addAttribute("ip", ip);
			model.addAttribute("username", username);
			
			model.addAttribute("merchantId", merchantId);
			model.addAttribute("merchantList", merchantList);
			model.addAttribute("productId",productId);
			model.addAttribute("productList", productList);
			model.addAttribute("pointId", pointId);
			model.addAttribute("pointList", pointList);
			
			model.addAttribute("status", status);
			model.addAttribute("statusList", statusList);
			
			model.addAttribute("mlist", page_list.getList());
			model.addAttribute("page", pageNumber);
			model.addAttribute("pageSize", pageSize);
			model.addAttribute("counttotal", page_list.getCountTotal() + "");
			model.addAttribute("pagetotal", page_list.getPageTotal() + "");

			return "payment/risk/basicRule/basicRuleList";
		}
		catch (Exception e)
		{
			logger.error(e.getLocalizedMessage(), e);
			return "error/500";
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = "/updateBasicRuleStatus")
	public @ResponseBody
	String updateBasicRuleStatus(BasicRule basicRule, ModelMap model)
	{

		Map<String, String> result = new HashMap<String, String>();
		try
		{
			basicRuleService.updateBasicRuleStatus(basicRule);

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
	
	@RequestMapping(method = RequestMethod.POST, value = "/deleteBasicRule")
	public @ResponseBody
	String deleteBasicRule(BasicRule cardProduct, ModelMap model)
	{

		Map<String, String> result = new HashMap<String, String>();
		try
		{
			basicRuleService.deleteBasicRule(cardProduct);
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
	
	@RequestMapping(value = "/editBasicRule")
	public String editBasicRule(BasicRule basicRule, ModelMap model)
	{
		try
		{
			basicRule = basicRuleService.queryBasicRule(basicRule.getId());
			List<OptionVO> timeTypeList = dataSourceService.findOpenOptionsWithoutPlease(CommonConstant.DataSourceName.RISK_TIME_TYPE);
			List<OptionVO> limitTypeList = dataSourceService.findOpenOptionsWithoutPlease(CommonConstant.DataSourceName.RISK_LIMIT_TYPE);
			List<OptionVO> selectTypeList = dataSourceService.findOpenOptionsWithoutPlease(CommonConstant.DataSourceName.RISK_SELECT_TYPE);
			List<OptionVO> merchantList = dataSourceService.findOpenOptionsWithoutPlease(CommonConstant.DataSourceName.MERCHANT);
			List<OptionVO> productList = dataSourceService.findOpenOptionsWithoutPlease(CommonConstant.DataSourceName.PRODUCT);
			List<OptionVO> pointList = dataSourceService.findOpenOptionsWithoutPlease(CommonConstant.DataSourceName.POINT);
			List<OptionVO> statusList = dataSourceService.findOpenOptionsWithoutPlease(CommonConstant.DataSourceName.COMMON_STATUS);
			List<OptionVO> timeUnitList = dataSourceService.findOpenOptionsWithoutPlease(CommonConstant.DataSourceName.TIME_UNIT);
			List<OptionVO> channelList = dataSourceService.findOpenOptionsWithoutPlease(CommonConstant.DataSourceName.CHANNEL);
			List<OptionVO> provinceList = dataSourceService.findOpenOptionsWithoutPlease(CommonConstant.DataSourceName.PROVINCE);
			List<OptionVO> cityList = dataSourceService.findOpenOptionsByParent(CommonConstant.DataSourceName.CITY,
					basicRule.getProvinceId());
			cityList.remove(0);
			List<OptionVO> actionList = dataSourceService.findOpenOptionsWithoutPlease(CommonConstant.DataSourceName.RISK_ACTION);
			
			model.addAttribute("timeTypeList", timeTypeList);
			model.addAttribute("limitTypeList", limitTypeList);
			model.addAttribute("selectTypeList", selectTypeList);
			model.addAttribute("merchantList", merchantList);
			model.addAttribute("productList", productList);
			model.addAttribute("pointList", pointList);
			model.addAttribute("statusList", statusList);
			model.addAttribute("timeUnitList", timeUnitList);
			model.addAttribute("channelList", channelList);
			model.addAttribute("provinceList", provinceList);
			model.addAttribute("cityList", cityList);		
			model.addAttribute("actionList", actionList);		
			model.addAttribute("basicRule", basicRuleService.copyPropertiesToVO(basicRule));		
			return "payment/risk/basicRule/editBasicRule";
		}
		catch (Exception e)
		{
			logger.error(e.getLocalizedMessage(), e);
			return "error/500";
		}
	}

	@RequestMapping(value = "/createBasicRule")
	public String createBasicRule(Model model)
	{
		try
		{
			List<OptionVO> timeTypeList = dataSourceService.findOpenOptionsWithoutPlease(CommonConstant.DataSourceName.RISK_TIME_TYPE);
			List<OptionVO> limitTypeList = dataSourceService.findOpenOptionsWithoutPlease(CommonConstant.DataSourceName.RISK_LIMIT_TYPE);
			List<OptionVO> selectTypeList = dataSourceService.findOpenOptionsWithoutPlease(CommonConstant.DataSourceName.RISK_SELECT_TYPE);
			List<OptionVO> merchantList = dataSourceService.findOpenOptionsWithoutPlease(CommonConstant.DataSourceName.MERCHANT);
			List<OptionVO> productList = dataSourceService.findOpenOptionsWithoutPlease(CommonConstant.DataSourceName.PRODUCT);
			List<OptionVO> pointList = dataSourceService.findOpenOptionsWithoutPlease(CommonConstant.DataSourceName.POINT);
			List<OptionVO> statusList = dataSourceService.findOpenOptionsWithoutPlease(CommonConstant.DataSourceName.COMMON_STATUS);
			List<OptionVO> timeUnitList = dataSourceService.findOpenOptionsWithoutPlease(CommonConstant.DataSourceName.TIME_UNIT);
			List<OptionVO> channelList = dataSourceService.findOpenOptionsWithoutPlease(CommonConstant.DataSourceName.CHANNEL);
			List<OptionVO> provinceList = dataSourceService.findOpenOptionsWithoutPlease(CommonConstant.DataSourceName.PROVINCE);
			List<OptionVO> cityList = dataSourceService.findOpenOptionsByParent(CommonConstant.DataSourceName.CITY,
					provinceList.get(0).getValue());
			cityList.remove(0);
			List<OptionVO> actionList = dataSourceService.findOpenOptionsWithoutPlease(CommonConstant.DataSourceName.RISK_ACTION);
			
			model.addAttribute("timeTypeList", timeTypeList);
			model.addAttribute("limitTypeList", limitTypeList);
			model.addAttribute("selectTypeList", selectTypeList);
			model.addAttribute("merchantList", merchantList);
			model.addAttribute("productList", productList);
			model.addAttribute("pointList", pointList);
			model.addAttribute("statusList", statusList);
			model.addAttribute("timeUnitList", timeUnitList);
			model.addAttribute("channelList", channelList);
			model.addAttribute("provinceList", provinceList);
			model.addAttribute("cityList", cityList);		
			model.addAttribute("actionList", actionList);	
			return "payment/risk/basicRule/editBasicRule";
		}
		catch (Exception e)
		{
			logger.error(e.getLocalizedMessage(), e);
			return "error/500";
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = "/saveBasicRule")
	public @ResponseBody
	String saveBasicRule(BasicRuleVO basicRule, ModelMap model)
	{
		final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Map<String, String> result = new HashMap<String, String>();
		try
		{
			BasicRule br = new BasicRule();
			br.setId(basicRule.getId());
			br.setTimeType(basicRule.getTimeType());
			br.setTimeUnit(basicRule.getTimeUnit());
			br.setStartUnit(basicRule.getStartUnit());
			br.setEndUnit(basicRule.getEndUnit());
			br.setRelativeUnit(basicRule.getRelativeUnit());
			br.setRelativeValue(basicRule.getRelativeValue());
			if (StringUtil.isNotEmpty(basicRule.getStartTime()))
			{
				br.setStartTime(DATE_FORMAT.parse(basicRule.getStartTime()));
			}
			if (StringUtil.isNotEmpty(basicRule.getEndTime()))
			{
				br.setEndTime(DATE_FORMAT.parse(basicRule.getEndTime()));
			}
			br.setLimitType(basicRule.getLimitType());
			br.setVolume(basicRule.getVolume() * 100);
			br.setChannelId(basicRule.getChannelId());
			br.setProvinceId(basicRule.getProvinceId());
			br.setCityId(basicRule.getCityId());
			br.setMerchantId(basicRule.getMerchantId());
			br.setProductId(basicRule.getProductId());
			br.setPointId(basicRule.getPointId());
			br.setAction(basicRule.getAction());
			br.setMobile(basicRule.getMobile());
			br.setStatus(basicRule.getStatus());
			br.setIp(basicRule.getIp());
			br.setUsername(basicRule.getUsername());
			basicRuleService.saveBasicRule(br);
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
}
