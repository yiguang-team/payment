package com.yiguang.payment.payment.report.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.yiguang.payment.common.CommonConstant;
import com.yiguang.payment.common.DateTimeHelper;
import com.yiguang.payment.common.datasource.service.DataSourceService;
import com.yiguang.payment.common.datasource.vo.OptionVO;
import com.yiguang.payment.payment.report.service.ProductReportService;
import com.yiguang.payment.payment.report.vo.ProductReportVO;

@Controller
@RequestMapping(value = "/mall/report")
public class ProductReportController
{
	@Autowired
	private ProductReportService productReportService;
	@Autowired
	private DataSourceService dataSourceService;
				
	@RequestMapping(value = "/productReportList")
	public String productReportList(@RequestParam(value = "cityId", defaultValue = "") String cityId,
			@RequestParam(value = "chargingType", defaultValue = "") String chargingType,
			@RequestParam(value = "prodcutId", defaultValue = "") String prodcutId,
			@RequestParam(value = "merchantId", defaultValue = "") String merchantId,
			@RequestParam(value = "chargingPointId", defaultValue = "") String chargingPointId,
			@RequestParam(value = "channelId", defaultValue = "") String channelId,
			@RequestParam(value = "provinceId", defaultValue = "") String provinceId,
			@RequestParam(value = "modelId", defaultValue = "5") String modelId,
			@RequestParam(value = "beginDate", defaultValue = "") String beginDate,
			@RequestParam(value = "endDate", defaultValue = "") String endDate, Model model, ServletRequest request)
	{

		if ("".equals(beginDate))
		{
			beginDate = DateTimeHelper.getCurrentMonthFirstDay();
		}
		if ("".equals(endDate))
		{
			endDate = DateTimeHelper.getCurrentMonthLastDay();
		}
		Map<String, Object> searchParams = new HashMap<String, Object>();
		searchParams.put("charging_type", chargingType);
		searchParams.put("city_id", cityId);
		searchParams.put("product_id", prodcutId);
		searchParams.put("channel_id", channelId);
		searchParams.put("merchant_id", merchantId);
		searchParams.put("charging_point_id", chargingPointId);
		searchParams.put("province_id", provinceId);
		searchParams.put("modelId", modelId);
		searchParams.put("beginDate", beginDate);
		searchParams.put("endDate", endDate);

		List<ProductReportVO> productReports = productReportService.queryProductReportList(searchParams);

		List<OptionVO> provinceList = dataSourceService.findOpenOptions(CommonConstant.DataSourceName.PROVINCE);
		List<OptionVO> cityList = dataSourceService.findOpenOptions(CommonConstant.DataSourceName.CITY);
		List<OptionVO> merchantList = dataSourceService.findOpenOptions(CommonConstant.DataSourceName.MERCHANT);
		List<OptionVO> channelList = dataSourceService.findOpenOptions(CommonConstant.DataSourceName.CHANNEL);
		List<OptionVO> prodcutList = dataSourceService.findOpenOptions(CommonConstant.DataSourceName.PRODUCT);

		List<OptionVO> chargingPointList = dataSourceService.findOpenOptions(CommonConstant.DataSourceName.POINT);
		List<OptionVO> modelList = dataSourceService
				.findOpenOptions(CommonConstant.DataSourceName.PRODUCT_REPORT_MODEL);

		List<OptionVO> chargingTypeList = dataSourceService
				.findOpenOptions(CommonConstant.DataSourceName.CHARGING_TYPE);

		String modelTitle = dataSourceService.findOptionVOById(CommonConstant.DataSourceName.PRODUCT_REPORT_MODEL,
				String.valueOf(modelId)).getText();

		model.addAttribute("prodcutId", prodcutId);
		model.addAttribute("modelTitle", modelTitle);
		model.addAttribute("chargingPointId", chargingPointId);
		model.addAttribute("beginDate", beginDate);
		model.addAttribute("endDate", endDate);
		model.addAttribute("mlist", productReports);
		model.addAttribute("modelId", modelId);
		model.addAttribute("chargingType", chargingType);
		model.addAttribute("merchantId", merchantId);
		model.addAttribute("channelId", channelId);
		model.addAttribute("cityId", cityId);
		model.addAttribute("provinceId", provinceId);
		model.addAttribute("provinceList", provinceList);
		model.addAttribute("cityList", cityList);
		model.addAttribute("channelList", channelList);
		model.addAttribute("merchantList", merchantList);
		model.addAttribute("prodcutList", prodcutList);
		model.addAttribute("chargingPointList", chargingPointList);
		model.addAttribute("chargingTypeList", chargingTypeList);
		model.addAttribute("modelList", modelList);

		return "/mall/report/productReportList";
	}
}
