package com.yiguang.payment.payment.report.service;

import java.util.List;
import java.util.Map;

import com.yiguang.payment.payment.report.vo.ProductReportVO;

public interface ProductReportService
{

	public List<ProductReportVO> queryProductReportList(Map<String, Object> searchParams);
}
