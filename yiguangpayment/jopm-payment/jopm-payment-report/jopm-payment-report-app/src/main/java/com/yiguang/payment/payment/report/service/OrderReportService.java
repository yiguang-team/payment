package com.yiguang.payment.payment.report.service;

import java.util.List;
import java.util.Map;

import com.yiguang.payment.payment.report.vo.OrderReportVO;

public interface OrderReportService
{

	public List<OrderReportVO> queryOrderReportList(Map<String, Object> searchParams);
}
