package com.yiguang.payment.payment.report.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yiguang.payment.common.CommonConstant;
import com.yiguang.payment.common.CommonConstant.DataSourceName;
import com.yiguang.payment.common.CommonConstant.ModelColumns;
import com.yiguang.payment.common.datasource.service.DataSourceService;
import com.yiguang.payment.common.utils.BigDecimalUtil;
import com.yiguang.payment.common.utils.StringUtil;
import com.yiguang.payment.payment.report.service.OrderReportService;
import com.yiguang.payment.payment.report.vo.OrderReportVO;

@Service("orderReportService")
@Transactional
public class OrderReportServiceImpl implements OrderReportService
{
	private static Logger logger = LoggerFactory.getLogger(OrderReportServiceImpl.class);
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private DataSourceService dataSourceService;

	@Override
	public List<OrderReportVO> queryOrderReportList(Map<String, Object> searchParams)
	{
		List<OrderReportVO> rstList = new ArrayList<OrderReportVO>();
		try
		{
			logger.debug("-----------------searchParams: " + searchParams + "-------");
			String modelTitle = "";
			String dataSourceCode = "";
			int modelId = (Integer) searchParams.get("modelId");
			if (modelId == ModelColumns.MERCHANT_ID)
			{

				modelTitle = "MERCHANT_ID";
				dataSourceCode = DataSourceName.MERCHANT;

			}
			else if (modelId == ModelColumns.PROVINCE_ID)
			{

				modelTitle = "PROVINCE_ID";
				dataSourceCode = DataSourceName.PROVINCE;

			}
			else if (modelId == ModelColumns.PAY_AMOUNT)
			{

				modelTitle = "PAY_AMOUNT";

			}
			else if (modelId == ModelColumns.CHANNEL_ID)
			{

				modelTitle = "CHANNEL_ID";
				dataSourceCode = DataSourceName.CHANNEL;

			}
			else if (modelId == ModelColumns.TIME_HOUR)
			{
				// 按小时统计
				modelTitle = "TO_CHAR(PAY_REQUEST_TIME, 'YYYY-MM-DD HH24')";

			}
			else if (modelId == ModelColumns.TIME_DAY)
			{
				// 按天统计
				modelTitle = "TO_CHAR(PAY_REQUEST_TIME, 'YYYY-MM-DD')";
			}
			else if (modelId == ModelColumns.TIME_MONTH)
			{
				// 按月统计
				modelTitle = "TO_CHAR(PAY_REQUEST_TIME, 'YYYY-MM')";
			}

			// 组装查询条件
			String condition = "";
			if (StringUtil.isNotBlank(String.valueOf(searchParams.get("channel_id"))))
			{
				condition += "and channel_id='" + String.valueOf(searchParams.get("channel_id")) + "' ";
			}
			if (StringUtil.isNotBlank(String.valueOf(searchParams.get("merchant_id"))))
			{
				condition += "and merchant_id='" + String.valueOf(searchParams.get("merchant_id")) + "' ";
			}
			if (StringUtil.isNotBlank(String.valueOf(searchParams.get("province_id"))))
			{
				condition += "and province_id='" + String.valueOf(searchParams.get("province_id")) + "' ";
			}
			if (StringUtil.isNotBlank(String.valueOf(searchParams.get("channel_type"))))
			{
				condition += "and channel_type='" + String.valueOf(searchParams.get("channel_type")) + "' ";
			}
			if (StringUtil.isNotBlank(String.valueOf(searchParams.get("pay_amount"))))
			{
				condition += "and pay_amount/100 ='" + searchParams.get("pay_amount") + "' ";
			}
			if (StringUtil.isNotBlank(String.valueOf(searchParams.get("beginDate"))))
			{
				condition = condition + " and pay_request_time >= to_date('"
						+ String.valueOf(searchParams.get("beginDate")) + "','yyyy-mm-dd hh24:mi:ss')";
			}
			if (StringUtil.isNotBlank(String.valueOf(searchParams.get("endDate"))))
			{
				condition = condition + " and pay_request_time <= to_date('"
						+ String.valueOf(searchParams.get("endDate")) + "','yyyy-mm-dd hh24:mi:ss')";
			}
			// 获得总条数和总金额
			String totalSql = "select "
					+ modelTitle
					+ " as model_title, count(1) as count, nvl(sum(PAY_AMOUNT),0) as amount from T_PAYMENT_ORDER t where (status = "
					+ CommonConstant.PayStatus.SUCCESS + " or status = " + CommonConstant.PayStatus.FAILED + ") "
					+ condition + " group by " + modelTitle + " order by model_title desc";
			logger.debug("-----------------totalSql: " + totalSql + "-------");
			// 获得成功条数和成功金额
			String successSql = "select count(1) as count, nvl(sum(PAY_AMOUNT),0) as amount from T_PAYMENT_ORDER a where a.status = "
					+ CommonConstant.PayStatus.SUCCESS + condition + " and ";

			// 获得失败条数和失败金额
			String failedSql = "select count(1) as count, nvl(sum(PAY_AMOUNT),0) as amount from T_PAYMENT_ORDER a where a.status = "
					+ CommonConstant.PayStatus.FAILED + condition + " and ";

			List<Map<String, Object>> list = jdbcTemplate.queryForList(totalSql);

			BigDecimal calTotalAmount = BigDecimal.ZERO;
			BigDecimal calSuccessAmount = BigDecimal.ZERO;
			BigDecimal calFailAmount = BigDecimal.ZERO;
			int calTotalCount = 0;
			int calSuccessCount = 0;
			int calFailCount = 0;

			for (Map<String, Object> map : list)
			{

				String modelValue = String.valueOf(map.get("model_title"));
				String subSql = modelTitle + "='" + modelValue + "'";
				if (modelId == ModelColumns.TIME_HOUR)
				{
					subSql = "pay_request_time >= to_date('" + modelValue
							+ "','YYYY-MM-DD hh24') and pay_request_time < to_date('" + modelValue
							+ "','YYYY-MM-DD hh24')+ 1/24";
				}
				else if (modelId == ModelColumns.TIME_DAY)
				{
					subSql = "pay_request_time >= to_date('" + modelValue
							+ "','YYYY-MM-DD') and pay_request_time < to_date('" + modelValue + "','YYYY-MM-DD')+ 1";
				}
				else if (modelId == ModelColumns.TIME_MONTH)
				{
					subSql = "pay_request_time >= to_date('" + modelValue
							+ "','YYYY-MM') and pay_request_time < add_months(to_date('" + modelValue
							+ "','YYYY-MM'),1)";
				}
				logger.debug("-----------------successSql: " + successSql + subSql + "-------");
				logger.debug("-----------------failedSql: " + failedSql + subSql + "-------");
				List<Map<String, Object>> successList = jdbcTemplate.queryForList(successSql + subSql);

				List<Map<String, Object>> failedList = jdbcTemplate.queryForList(failedSql + subSql);

				int successCount = ((BigDecimal) successList.get(0).get("count")).intValue();

				BigDecimal successAmount = BigDecimalUtil.divide((BigDecimal) successList.get(0).get("amount"),
						new BigDecimal("100"), 2, BigDecimal.ROUND_HALF_UP);

				int failedCount = ((BigDecimal) failedList.get(0).get("count")).intValue();

				BigDecimal failedAmount = BigDecimalUtil.divide((BigDecimal) failedList.get(0).get("amount"),
						new BigDecimal("100"), 2, BigDecimal.ROUND_HALF_UP);

				int totalCount = ((BigDecimal) map.get("count")).intValue();

				BigDecimal totalAmount = BigDecimalUtil.divide((BigDecimal) map.get("amount"), new BigDecimal("100"),
						2, BigDecimal.ROUND_HALF_UP);

				OrderReportVO orderReportVO = new OrderReportVO();

				if (StringUtil.isNotBlank(dataSourceCode))
				{
					modelValue = dataSourceService.findOptionVOById(dataSourceCode, modelValue).getText();
				}

				orderReportVO.setModelValue(modelValue);

				if (modelId == ModelColumns.PAY_AMOUNT)
				{
					BigDecimal faceAmout = BigDecimalUtil.divide(new BigDecimal(modelValue), new BigDecimal("100"), 2,
							BigDecimal.ROUND_HALF_UP);
					orderReportVO.setModelValue(faceAmout.toString());
				}

				calTotalAmount = BigDecimalUtil.add(calTotalAmount, totalAmount, 2, BigDecimal.ROUND_HALF_UP);
				calSuccessAmount = BigDecimalUtil.add(calSuccessAmount, successAmount, 2, BigDecimal.ROUND_HALF_UP);
				calFailAmount = BigDecimalUtil.add(calFailAmount, failedAmount, 2, BigDecimal.ROUND_HALF_UP);
				calTotalCount += totalCount;
				calSuccessCount += successCount;
				calFailCount += failedCount;
				orderReportVO.setTotalCount(totalCount);
				orderReportVO.setTotalAmount(totalAmount);
				orderReportVO.setSuccessAmount(successAmount);
				orderReportVO.setSuccessCount(successCount);
				orderReportVO.setFailedAmount(failedAmount);
				orderReportVO.setFailedCount(failedCount);

				rstList.add(orderReportVO);

			}
			if (rstList.size() > 0)
			{
				rstList.add(new OrderReportVO(calTotalCount, calTotalAmount, calSuccessCount, calSuccessAmount,
						calFailCount, calFailAmount, "合计"));
			}
			return rstList;
		}
		catch (Exception e)
		{
			logger.error("打印报表异常:" + e.getMessage());
			return rstList;
		}
	}
}
