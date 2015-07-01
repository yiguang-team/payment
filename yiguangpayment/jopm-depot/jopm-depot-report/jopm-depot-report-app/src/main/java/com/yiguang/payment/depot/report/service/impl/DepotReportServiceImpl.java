package com.yiguang.payment.depot.report.service.impl;

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

import com.yiguang.payment.common.CommonConstant.DataSourceName;
import com.yiguang.payment.common.CommonConstant.ModelColumns;
import com.yiguang.payment.common.datasource.service.DataSourceService;
import com.yiguang.payment.common.utils.BigDecimalUtil;
import com.yiguang.payment.common.utils.StringUtil;
import com.yiguang.payment.depot.report.service.DepotReportService;
import com.yiguang.payment.depot.report.vo.DepotReportVO;

@Service("depotReportService")
@Transactional
public class DepotReportServiceImpl implements DepotReportService
{
	private static Logger logger = LoggerFactory.getLogger(DepotReportServiceImpl.class);
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private DataSourceService dataSourceService;

	@Override
	public List<DepotReportVO> queryDepotReportList(Map<String, Object> searchParams)
	{
		List<DepotReportVO> rstList = new ArrayList<DepotReportVO>();
		try
		{
			logger.debug("-----------------searchParams: " + searchParams + "-------");
			String modelTitle = "";
			String dataSourceCode = "";
			int modelId = (Integer) searchParams.get("modelId");
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
			else if (modelId == ModelColumns.CHARGING_POINT_ID)
			{
				// 按计费点统计
				modelTitle = "CHARGING_POINT_ID";
				dataSourceCode = DataSourceName.POINT;
			}
			else if (modelId == ModelColumns.PICKUP_USER)
			{
				// 按提卡人统计
				modelTitle = "EXTRACT_USER";
				dataSourceCode = DataSourceName.MERCHANT;
			}

			// 组装查询条件
			String condition = "";

			if (StringUtil.isNotBlank(String.valueOf(searchParams.get("extract_type"))) && !"-1".equals(String.valueOf(searchParams.get("extract_type"))))
			{
				condition += "and extract_type='" + String.valueOf(searchParams.get("extract_type")) + "' ";
			}

			if (StringUtil.isNotBlank(String.valueOf(searchParams.get("charging_point_id"))) && !"-1".equals(String.valueOf(searchParams.get("charging_point_id"))))
			{
				condition += "and charging_point_id='" + String.valueOf(searchParams.get("charging_point_id")) + "' ";
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
					+ " as model_title, count(1) as count, nvl(sum(PAY_AMOUNT),0) as amount from T_EXTRACT_RECORD t where 1=1 "
					+ condition + " group by " + modelTitle + " order by model_title desc";
			logger.debug("-----------------totalSql: " + totalSql + "-------");

			List<Map<String, Object>> list = jdbcTemplate.queryForList(totalSql);
			logger.debug("[queryDepotReportList]list: " + list);
			BigDecimal calTotalAmount = BigDecimal.ZERO;
			int calTotalCount = 0;
			// 根据查询结果组装页面显示列表
			for (Map<String, Object> map : list)
			{

				String modelValue = String.valueOf(map.get("model_title"));

				int totalCount = ((BigDecimal) map.get("count")).intValue();

				BigDecimal totalAmount = BigDecimalUtil.divide((BigDecimal) map.get("amount"), new BigDecimal("100"),
						2, BigDecimal.ROUND_HALF_UP);

				DepotReportVO DepotReportVO = new DepotReportVO();

				// 如果存储的是id，需要取其对应的名称
				if (StringUtil.isNotBlank(dataSourceCode))
				{
					String tempValue = dataSourceService.findOptionVOById(dataSourceCode, modelValue).getText();
					modelValue = StringUtil.isNotBlank(tempValue) ? tempValue : modelValue;
				}

				DepotReportVO.setModelValue(modelValue);

				// 如果模式列为金额，需要除以100显示(数据库中以分为单位，显示以元为单位)
				if (modelId == ModelColumns.PAY_AMOUNT)
				{
					BigDecimal faceAmout = BigDecimalUtil.divide(new BigDecimal(modelValue), new BigDecimal("100"), 2,
							BigDecimal.ROUND_HALF_UP);
					DepotReportVO.setModelValue(faceAmout.toString());
				}
				calTotalAmount = BigDecimalUtil.add(calTotalAmount, totalAmount, 2, BigDecimal.ROUND_HALF_UP);
				calTotalCount += totalCount;

				DepotReportVO.setTotalCount(totalCount);
				DepotReportVO.setTotalAmount(totalAmount);
				DepotReportVO.setSuccessAmount(totalAmount);
				DepotReportVO.setSuccessCount(totalCount);
				DepotReportVO.setFailedAmount(BigDecimal.ZERO);
				DepotReportVO.setFailedCount(0);

				rstList.add(DepotReportVO);

			}
			logger.debug("[queryDepotReportList]rstList: " + rstList);
			if (rstList.size() > 0)
			{
				//由于卡库只记录成功记录，所以成功数据即为总共数据,失败数据为0
				rstList.add(new DepotReportVO(calTotalCount, calTotalAmount, calTotalCount, calTotalAmount, 0,
						BigDecimal.ZERO, "合计"));
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
