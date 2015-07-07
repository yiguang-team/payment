package com.yiguang.payment.business.product.controller;

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
import com.yiguang.payment.business.product.entity.Point;
import com.yiguang.payment.business.product.service.PointChannelRelationService;
import com.yiguang.payment.business.product.service.PointService;
import com.yiguang.payment.business.product.vo.PointChannelRelationVO;
import com.yiguang.payment.business.product.vo.PointVO;
import com.yiguang.payment.common.CommonConstant;
import com.yiguang.payment.common.JsonTool;
import com.yiguang.payment.common.datasource.service.DataSourceService;
import com.yiguang.payment.common.datasource.vo.OptionVO;
import com.yiguang.payment.common.message.MessageResolver;
import com.yiguang.payment.common.query.YcPage;
import com.yiguang.payment.common.utils.BigDecimalUtil;
import com.yiguang.payment.common.utils.StringUtil;
import com.yiguang.payment.depot.service.ProductDepotService;

/**
 * LoginController负责打开登录页面(GET请求)和登录出错页面(POST请求)， 真正登录的POST请求由Filter完成,
 * 
 * @author calvin
 */
@Controller
@RequestMapping(value = "/mall/management/point")
public class PointController
{

	@Autowired
	HttpSession session;

	@Autowired
	private PointService pointService;

	@Autowired
	private ProductDepotService productDepotService;

	@Autowired
	private DataSourceService dataSourceService;
	@Autowired
	private PointChannelRelationService pointChannelRelationService;
	private static final String PAGE_SIZE = "10";

	private static Logger logger = LoggerFactory.getLogger(PointController.class);

	@RequestMapping(value = "/pointList")
	public String pointList(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "pageSize", defaultValue = PAGE_SIZE) int pageSize,
			@RequestParam(value = "sortType", defaultValue = "auto") String sortType,
			@RequestParam(value = "name", defaultValue = "") String name,
			@RequestParam(value = "merchantId", defaultValue = "-1") long merchantId,
			@RequestParam(value = "productId", defaultValue = "-1") long productId,
			@RequestParam(value = "channelId", defaultValue = "-1") long channelId,
			@RequestParam(value = "provinceId", defaultValue = "-1") String provinceId,
			@RequestParam(value = "cityId", defaultValue = "-1") String cityId,
			@RequestParam(value = "chargingType", defaultValue = "-1") int chargingType,
			@RequestParam(value = "status", defaultValue = "-1") int status, Model model, ServletRequest request)
	{
		
		try
		{

			YcPage<PointVO> page_list = pointService.queryPointList(pageNumber, pageSize, sortType, channelId, name,
					merchantId, productId, provinceId, cityId, chargingType, status);
			List<PointVO> voList = page_list.getList();

			for (PointVO vo : voList)
			{
				// 如果计费点类型为卡密取得卡密库存
				if (vo.getChargingType() == CommonConstant.CHARGING_TYPE.CARD)
				{
					vo.setInStock(String.valueOf(productDepotService.getUsableCount(vo.getId())));
				}
				else
				{
					vo.setInStock("-");
				}
			}

			List<OptionVO> statusList = dataSourceService.findOpenOptions(CommonConstant.DataSourceName.COMMON_STATUS);
			List<OptionVO> chargingTypeList = dataSourceService
					.findOpenOptions(CommonConstant.DataSourceName.CHARGING_TYPE);
			List<OptionVO> merchantList = dataSourceService.findOpenOptions(CommonConstant.DataSourceName.MERCHANT);
			List<OptionVO> channelList = dataSourceService.findOpenOptions(CommonConstant.DataSourceName.CHANNEL);
			List<OptionVO> productList = dataSourceService.findOpenOptionsByParent(
					CommonConstant.DataSourceName.PRODUCT,String.valueOf(merchantId));

			List<OptionVO> provinceList = dataSourceService.findOpenOptions(CommonConstant.DataSourceName.PROVINCE);

			List<OptionVO> cityList = dataSourceService.findOpenOptionsByParent(CommonConstant.DataSourceName.CITY,
					provinceId);

			model.addAttribute("name", name);
			model.addAttribute("channelId", channelId);
			model.addAttribute("channelList", channelList);
			model.addAttribute("status", status);
			model.addAttribute("statusList", statusList);
			model.addAttribute("merchantId", merchantId);
			model.addAttribute("merchantList", merchantList);
			model.addAttribute("productId", productId);
			model.addAttribute("productList", productList);
			model.addAttribute("provinceId", provinceId);
			model.addAttribute("provinceList", provinceList);
			model.addAttribute("cityId", cityId);
			model.addAttribute("cityList", cityList);
			model.addAttribute("chargingTypeList", chargingTypeList);
			model.addAttribute("chargingType", chargingType);
			model.addAttribute("mlist", page_list.getList());
			model.addAttribute("page", pageNumber);
			model.addAttribute("pageSize", pageSize);
			model.addAttribute("counttotal", page_list.getCountTotal() + "");
			model.addAttribute("pagetotal", page_list.getPageTotal() + "");

			return "mall/management/point/pointList";
		}
		catch (Exception e)
		{
			logger.error(e.getLocalizedMessage(), e);
			return "error/500";
		}

	}

	@RequestMapping(method = RequestMethod.POST, value = "/updatePointStatus")
	public @ResponseBody
	String updatePointStatus(Point cardProduct, ModelMap model)
	{

		Map<String, String> result = new HashMap<String, String>();
		try
		{
			pointService.updatePointStatus(cardProduct);

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

	@RequestMapping(method = RequestMethod.POST, value = "/deletePoint")
	public @ResponseBody
	String deletePoint(Point cardProduct, ModelMap model)
	{

		Map<String, String> result = new HashMap<String, String>();
		try
		{
			pointService.deletePoint(cardProduct);

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

	@RequestMapping(value = "/editPoint")
	public String editPoint(Point point, ModelMap model)
	{
		try
		{
			point = pointService.queryPoint(point.getId());
			String provinceId = point.getProvinceId();
			long merchantId = point.getMerchantId();
			List<OptionVO> statusList = dataSourceService.findOpenOptions(CommonConstant.DataSourceName.COMMON_STATUS);
			List<OptionVO> merchantList = dataSourceService.findOpenOptions(CommonConstant.DataSourceName.MERCHANT);

			List<OptionVO> productList = dataSourceService.findOpenOptionsByParent(
					CommonConstant.DataSourceName.PRODUCT, String.valueOf(merchantId));

			List<OptionVO> provinceList = dataSourceService.findOpenOptions(CommonConstant.DataSourceName.PROVINCE);

			List<OptionVO> cityList = dataSourceService.findOpenOptionsByParent(CommonConstant.DataSourceName.CITY,
					provinceId);

			List<OptionVO> faceAmountList = dataSourceService
					.findOpenOptions(CommonConstant.DataSourceName.FACE_AMOUNT);
			List<OptionVO> chargingTypeList = dataSourceService
					.findOpenOptions(CommonConstant.DataSourceName.CHARGING_TYPE);
			List<OptionVO> pointChannelRelationList = dataSourceService
					.findOpenOptionsWithoutPlease(CommonConstant.DataSourceName.CHANNEL);
			List<PointChannelRelationVO> checkedlist = pointChannelRelationService
					.queryPointChannelRelationVOByPointId(point.getId());

			model.addAttribute("point", pointService.copyPropertiesToVO(point));
			model.addAttribute("statusList", statusList);
			model.addAttribute("merchantList", merchantList);
			model.addAttribute("productList", productList);
			model.addAttribute("provinceList", provinceList);
			model.addAttribute("cityList", cityList);
			model.addAttribute("faceAmountList", faceAmountList);
			model.addAttribute("chargingTypeList", chargingTypeList);
			model.addAttribute("checkedlist", checkedlist);
			model.addAttribute("pointChannelRelationList", pointChannelRelationList);
			return "mall/management/point/editPoint";
		}
		catch (Exception e)
		{
			logger.error(e.getLocalizedMessage(), e);
			return "error/500";
		}

	}

	@RequestMapping(value = "/createPoint")
	public String createPoint(Model model)
	{
		try
		{
			List<OptionVO> statusList = dataSourceService.findOpenOptions(CommonConstant.DataSourceName.COMMON_STATUS);
			List<OptionVO> merchantList = dataSourceService.findOpenOptions(CommonConstant.DataSourceName.MERCHANT);
			List<OptionVO> productList = dataSourceService.findOpenOptions(CommonConstant.DataSourceName.PRODUCT);
			List<OptionVO> provinceList = dataSourceService.findOpenOptions(CommonConstant.DataSourceName.PROVINCE);
			List<OptionVO> cityList = dataSourceService.findOpenOptions(CommonConstant.DataSourceName.CITY);
			List<OptionVO> faceAmountList = dataSourceService
					.findOpenOptions(CommonConstant.DataSourceName.FACE_AMOUNT);
			List<OptionVO> chargingTypeList = dataSourceService
					.findOpenOptions(CommonConstant.DataSourceName.CHARGING_TYPE);
			List<OptionVO> pointChannelRelationList = dataSourceService
					.findOpenOptionsWithoutPlease(CommonConstant.DataSourceName.CHANNEL);
			model.addAttribute("statusList", statusList);
			model.addAttribute("merchantList", merchantList);
			model.addAttribute("productList", productList);
			model.addAttribute("provinceList", provinceList);
			model.addAttribute("cityList", cityList);
			model.addAttribute("chargingTypeList", chargingTypeList);
			model.addAttribute("faceAmountList", faceAmountList);

			model.addAttribute("pointChannelRelationList", pointChannelRelationList);

			return "mall/management/point/editPoint";
		}
		catch (Exception e)
		{
			logger.error(e.getLocalizedMessage(), e);
			return "error/500";
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = "/savePoint")
	public @ResponseBody
	String savePoint(PointVO pointVO, Model model)
	{

		Map<String, String> result = new HashMap<String, String>();
		try
		{
			String regex_a = "[`~!@#$%^&*(){}'?/-=\\.<>￥%]";
			String regex_b = "^\\d+(\\.\\d+)?";

			if (StringUtil.isBlank(String.valueOf(pointVO.getMerchantId())))
			{
				result.put("result", "error");
				result.put("message", "操作失败[" + "请选择运营商" + "]");
			}
			else if (StringUtil.isBlank(String.valueOf(pointVO.getProductId())))
			{
				result.put("result", "error");
				result.put("message", "操作失败[" + "请选择产品" + "]");
			}
			else if (StringUtil.isBlank(String.valueOf(pointVO.getStatus())))
			{
				result.put("result", "error");
				result.put("message", "操作失败[" + "请选择状态" + "]");
			}
			else if (StringUtil.isBlank(String.valueOf(pointVO.getChargingType())))
			{
				result.put("result", "error");
				result.put("message", "操作失败[" + "请选择计费类型" + "]");
			}
			else if (StringUtil.isBlank(String.valueOf(pointVO.getFaceAmount())))
			{
				result.put("result", "error");
				result.put("message", "操作失败[" + "请填写面值" + "]");
			}
			else if (!Pattern.matches(regex_b, (CharSequence) pointVO.getFaceAmount().toString()))
			{
				result.put("result", "error");
				result.put("message", "操作失败[" + "请输入数字，并确保格式正确" + "]");
			}
			else if (Pattern.matches(regex_a, (CharSequence) pointVO.getName()))
			{
				result.put("result", "error");
				result.put("message", "操作失败[" + "名字中不能包含特殊字符" + "]");
			}
			else if (StringUtil.isBlank(String.valueOf(pointVO.getPayAmount())))
			{
				result.put("result", "error");
				result.put("message", "操作失败[" + "请填写支付价格" + "]");
			}
			else if (!Pattern.matches(regex_b, (CharSequence) pointVO.getPayAmount().toString()))
			{
				result.put("result", "error");
				result.put("message", "操作失败[" + "请输入数字，并确保格式正确" + "]");
			}
			else if (StringUtil.isBlank(String.valueOf(pointVO.getDeliveryAmount())))
			{
				result.put("result", "error");
				result.put("message", "操作失败[" + "请填写发货价格" + "]");
			}
			else if (!Pattern.matches(regex_b, (CharSequence) pointVO.getDeliveryAmount().toString()))
			{
				result.put("result", "error");
				result.put("message", "操作失败[" + "请输入数字，并确保格式正确" + "]");
			}
			else
			{

				Point point = new Point();
				point.setFaceAmount(BigDecimalUtil.multiply(pointVO.getFaceAmount(), new BigDecimal("100"), 0,
						BigDecimal.ROUND_HALF_UP));
				point.setPayAmount(BigDecimalUtil.multiply(pointVO.getPayAmount(), new BigDecimal("100"), 0,
						BigDecimal.ROUND_HALF_UP));
				point.setDeliveryAmount(BigDecimalUtil.multiply(pointVO.getDeliveryAmount(), new BigDecimal("100"), 0,
						BigDecimal.ROUND_HALF_UP));

				point.setName(pointVO.getName());
				point.setId(pointVO.getId());
				point.setMerchantId(pointVO.getMerchantId());
				point.setProductId(pointVO.getProductId());
				point.setStatus(pointVO.getStatus());
				point.setChargingType(pointVO.getChargingType());
				point.setProvinceId(pointVO.getProvinceId());
				point.setCityId(pointVO.getCityId());
				point.setRemark(pointVO.getRemark());
				point = pointService.savePoint(point, pointVO.getPointChannelRelationIDs());
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
