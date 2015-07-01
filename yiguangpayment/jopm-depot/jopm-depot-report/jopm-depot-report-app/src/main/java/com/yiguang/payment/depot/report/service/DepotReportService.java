package com.yiguang.payment.depot.report.service;

import java.util.List;
import java.util.Map;

import com.yiguang.payment.depot.report.vo.DepotReportVO;

public interface DepotReportService
{

	public List<DepotReportVO> queryDepotReportList(Map<String, Object> searchParams);
}
