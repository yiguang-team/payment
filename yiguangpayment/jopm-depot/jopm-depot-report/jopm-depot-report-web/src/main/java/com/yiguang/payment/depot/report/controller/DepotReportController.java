package com.yiguang.payment.depot.report.controller;

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
import com.yiguang.payment.depot.report.service.DepotReportService;
import com.yiguang.payment.depot.report.vo.DepotReportVO;

@Controller
@RequestMapping(value = "/depot/report")
public class DepotReportController
{
	@Autowired
	private DepotReportService depotReportService;
	@Autowired
	private DataSourceService dataSourceService;

	// 卡库报表请求入口
	@RequestMapping(value = "/depotReportList")
	public String depotReportList(@RequestParam(value = "extractType", defaultValue = "") String extractType,
			@RequestParam(value = "chargingPointId", defaultValue = "") String chargingPointId,
			@RequestParam(value = "modelId", defaultValue = "2") int modelId,
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
		searchParams.put("extract_type", extractType);
		searchParams.put("charging_point_id", chargingPointId);
		searchParams.put("modelId", modelId);
		searchParams.put("beginDate", beginDate);
		searchParams.put("endDate", endDate);

		// 获得卡库报表
		List<DepotReportVO> depotReports = depotReportService.queryDepotReportList(searchParams);

		// 获得字典列表
		List<OptionVO> chargingPointList = dataSourceService
				.findOpenOptions(CommonConstant.DataSourceName.POINT_CARD_PWD);
		List<OptionVO> modelList = dataSourceService
				.findOpenOptions(CommonConstant.DataSourceName.DEPOT_REPORT_MODEL);
		List<OptionVO> extractTypeList = dataSourceService.findOpenOptions(CommonConstant.DataSourceName.PICKUP_TYPE);
		String modelTitle = dataSourceService.findOptionVOById(CommonConstant.DataSourceName.DEPOT_REPORT_MODEL,
				String.valueOf(modelId)).getText();
		
		// 传值到页面
		model.addAttribute("modelTitle", modelTitle);
		model.addAttribute("chargingPointId", chargingPointId);
		model.addAttribute("beginDate", beginDate);
		model.addAttribute("endDate", endDate);
		model.addAttribute("mlist", depotReports);
		model.addAttribute("modelId", modelId);
		model.addAttribute("extractType", extractType);

		model.addAttribute("chargingPointList", chargingPointList);
		model.addAttribute("extractTypeList", extractTypeList);
		model.addAttribute("modelList", modelList);

		return "/depot/report/depotReportList";
	}
}
