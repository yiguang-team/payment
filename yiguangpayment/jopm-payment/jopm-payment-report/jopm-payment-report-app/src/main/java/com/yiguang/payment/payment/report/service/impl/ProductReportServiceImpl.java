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
import com.yiguang.payment.payment.report.service.ProductReportService;
import com.yiguang.payment.payment.report.vo.ProductReportVO;

@Service("productReportService")
@Transactional
public class ProductReportServiceImpl implements ProductReportService
{
	private static Logger logger = LoggerFactory.getLogger(ProductReportServiceImpl.class);
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private DataSourceService dataSourceService;

	@Override
	public List<ProductReportVO> queryProductReportList(Map<String, Object> searchParams)
	{
		List<ProductReportVO> rstList = new ArrayList<ProductReportVO>();
		try
		{
			logger.debug("-----------------searchParams: " + searchParams + "-------");
			String modelTitle = "";
			String dataSourceCode = "";
			String object = (String) searchParams.get("modelId");
			long modelId = Long.valueOf(object);
			if (modelId == ModelColumns.TIME_HOUR)
			{
				// 按小时统计
				modelTitle = "TO_CHAR(REQUEST_TIME, 'YYYY-MM-DD HH24')";

			}
			else if (modelId == ModelColumns.TIME_DAY)
			{
				// 按天统计
				modelTitle = "TO_CHAR(REQUEST_TIME, 'YYYY-MM-DD')";
			}
			else if (modelId == ModelColumns.TIME_MONTH)
			{
				// 按月统计
				modelTitle = "TO_CHAR(REQUEST_TIME, 'YYYY-MM')";

			}
			else if (modelId == ModelColumns.CHANNEL_ID)
			{
				//渠道
				modelTitle = "CHANNEL_ID";
				dataSourceCode = DataSourceName.CHANNEL;

			}
			else if (modelId == ModelColumns.PROVINCE_ID)
			{
				//省
				modelTitle = "PROVINCE_ID";
				dataSourceCode = DataSourceName.PROVINCE;

			}
			else if (modelId == ModelColumns.CITY_ID)
			{

				modelTitle = "CITY_ID";
				dataSourceCode = DataSourceName.CITY;

			}
			else if (modelId == ModelColumns.MERCHANT_ID)
			{
				// 按商户统计
				modelTitle = "MERCHANT_ID";
				dataSourceCode = DataSourceName.MERCHANT;

			}
			else if (modelId == ModelColumns.PRODUCT_ID)
			{
				// 按产品统计
				modelTitle = "PRODUCT_ID";
				dataSourceCode = DataSourceName.PRODUCT;
			}
			else if (modelId == ModelColumns.CHARGING_POINT_ID)
			{
				// 按计费点统计
				modelTitle = "CHARGING_POINT_ID";
				dataSourceCode = DataSourceName.POINT;
			}
			else if (modelId == ModelColumns.CHARGING_TYPE)
			{
				// 按充值类型统计
				modelTitle = "CHARGING_TYPE";
				dataSourceCode = DataSourceName.CHARGING_TYPE;
			}
			else if (modelId == ModelColumns.PAY_AMOUNT)
			{

				modelTitle = "PAY_AMOUNT";

			}
			String condition = "";

			if (StringUtil.isNotBlank(String.valueOf(searchParams.get("charging_type"))) && !"-1".equals(String.valueOf(searchParams.get("charging_type"))))
			{
				condition += "and charging_type='" + String.valueOf(searchParams.get("charging_type")) + "' ";
			}

			if (StringUtil.isNotBlank(String.valueOf(searchParams.get("city_id"))) && !"-1".equals(String.valueOf(searchParams.get("city_id"))))
			{
				condition += "and city_id='" + String.valueOf(searchParams.get("city_id")) + "' ";
			}

			if (StringUtil.isNotBlank(String.valueOf(searchParams.get("product_id"))) && !"-1".equals(String.valueOf(searchParams.get("product_id"))))
			{
				condition += "and product_id='" + String.valueOf(searchParams.get("product_id")) + "' ";
			}
			if (StringUtil.isNotBlank(String.valueOf(searchParams.get("channel_id"))) && !"-1".equals(String.valueOf(searchParams.get("channel_id"))))
			{
				condition += "and channel_id='" + String.valueOf(searchParams.get("channel_id")) + "' ";
			}
			if (StringUtil.isNotBlank(String.valueOf(searchParams.get("merchant_id"))) && !"-1".equals(String.valueOf(searchParams.get("merchant_id"))))
			{
				condition += "and merchant_id='" + String.valueOf(searchParams.get("merchant_id")) + "' ";
			}
			if (StringUtil.isNotBlank(String.valueOf(searchParams.get("charging_point_id"))) && !"-1".equals(String.valueOf(searchParams.get("charging_point_id"))))
			{
				condition += "and charging_point_id='" + String.valueOf(searchParams.get("charging_point_id")) + "' ";
			}

			if (StringUtil.isNotBlank(String.valueOf(searchParams.get("province_id"))) && !"-1".equals(String.valueOf(searchParams.get("province_id"))))
			{
				condition += "and province_id='" + String.valueOf(searchParams.get("province_id")) + "' ";
			}
			if (StringUtil.isNotBlank(String.valueOf(searchParams.get("beginDate"))))
			{
				condition = condition + " and REQUEST_TIME >= to_date('"
						+ String.valueOf(searchParams.get("beginDate")) + "','yyyy-mm-dd hh24:mi:ss')";
			}
			if (StringUtil.isNotBlank(String.valueOf(searchParams.get("endDate"))))
			{
				condition = condition + " and REQUEST_TIME <= to_date('" + String.valueOf(searchParams.get("endDate"))
						+ "','yyyy-mm-dd hh24:mi:ss')";
			}
			// 获得总条数和总金额
			String totalSql = "select "
					+ modelTitle
					+ " as model_title, count(1) as count, nvl(sum(PAY_AMOUNT),0) as amount from T_MERCHANT_ORDER t where (PAY_STATUS = "
					+ CommonConstant.PayStatus.SUCCESS + " or PAY_STATUS = " + CommonConstant.PayStatus.FAILED + ") "
					+ condition + " group by " + modelTitle + " order by model_title desc";
			logger.debug("-----------------totalSql: " + totalSql + "-------");
			// 获得成功条数和成功金额
			String successSql = "select count(1) as count, nvl(sum(PAY_AMOUNT),0) as amount from T_MERCHANT_ORDER a where a.PAY_STATUS = "
					+ CommonConstant.PayStatus.SUCCESS + condition + " and ";

			// 获得失败条数和失败金额
			String failedSql = "select count(1) as count, nvl(sum(PAY_AMOUNT),0) as amount from T_MERCHANT_ORDER a where a.PAY_STATUS =  "
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
					subSql = "REQUEST_TIME >= to_date('" + modelValue
							+ "','YYYY-MM-DD hh24') and REQUEST_TIME < to_date('" + modelValue
							+ "','YYYY-MM-DD hh24')+ 1/24";
				}
				else if (modelId == ModelColumns.TIME_DAY)
				{
					subSql = "REQUEST_TIME >= to_date('" + modelValue + "','YYYY-MM-DD') and REQUEST_TIME < to_date('"
							+ modelValue + "','YYYY-MM-DD')+ 1";
				}
				else if (modelId == ModelColumns.TIME_MONTH)
				{
					subSql = "REQUEST_TIME >= to_date('" + modelValue
							+ "','YYYY-MM') and REQUEST_TIME < add_months(to_date('" + modelValue + "','YYYY-MM'),1)";
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

				ProductReportVO productReportVO = new ProductReportVO();

				if (StringUtil.isNotBlank(dataSourceCode))
				{
					modelValue = dataSourceService.findOptionVOById(dataSourceCode, modelValue).getText();
				}

				productReportVO.setModelValue(modelValue);

				if (modelId == ModelColumns.PAY_AMOUNT)
				{
					BigDecimal faceAmout = BigDecimalUtil.divide(new BigDecimal(modelValue), new BigDecimal("100"), 2,
							BigDecimal.ROUND_HALF_UP);
					productReportVO.setModelValue(faceAmout.toString());
				}
				calTotalAmount = BigDecimalUtil.add(calTotalAmount, totalAmount, 2, BigDecimal.ROUND_HALF_UP);
				calSuccessAmount = BigDecimalUtil.add(calSuccessAmount, successAmount, 2, BigDecimal.ROUND_HALF_UP);
				calFailAmount = BigDecimalUtil.add(calFailAmount, failedAmount, 2, BigDecimal.ROUND_HALF_UP);
				calTotalCount += totalCount;
				calSuccessCount += successCount;
				calFailCount += failedCount;
				productReportVO.setTotalCount(totalCount);
				productReportVO.setTotalAmount(totalAmount);
				productReportVO.setSuccessAmount(successAmount);
				productReportVO.setSuccessCount(successCount);
				productReportVO.setFailedAmount(failedAmount);
				productReportVO.setFailedCount(failedCount);

				rstList.add(productReportVO);

			}
			if (rstList.size() > 0)
			{
				rstList.add(new ProductReportVO(calTotalCount, calTotalAmount, calSuccessCount, calSuccessAmount,
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
