package com.yiguang.payment.common.logging.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.persistence.SearchFilter;

import com.alibaba.dubbo.rpc.RpcException;
import com.yiguang.payment.common.CommonConstant;
import com.yiguang.payment.common.datasource.service.DataSourceService;
import com.yiguang.payment.common.exception.ErrorCodeConst;
import com.yiguang.payment.common.logging.entity.OperationLog;
import com.yiguang.payment.common.logging.repository.OperationLogDAO;
import com.yiguang.payment.common.logging.service.OperationLogService;
import com.yiguang.payment.common.logging.vo.OperationLogVO;
import com.yiguang.payment.common.query.PageUtil;
import com.yiguang.payment.common.query.YcPage;

@Service("operationLogService")
@Transactional
public class OperationLogServiceImpl implements OperationLogService
{
	@Autowired
	private DataSourceService dataSourceService;

	private static Logger logger = LoggerFactory.getLogger(OperationLogServiceImpl.class);

	@Autowired
	private OperationLogDAO operationLogDAO;

	@Override
	public void recordOperationLog(OperationLog log)
	{
		logger.debug("recordOperationLog start, log: [" + log + "]");
		try
		{
			operationLogDAO.save(log);

		}
		catch (Exception e)
		{
			logger.error("recordOperationLog failed");
			logger.error(e.getLocalizedMessage(), e);
			throw new RpcException(ErrorCodeConst.ErrorCode99998);
		}

	}

	@Override
	public YcPage<OperationLogVO> showLogList(Map<String, Object> searchParams, int pageNumber, int pageSize,
			String sortType)
	{

		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);

		// 调用查询API获得操作日志记录集
		YcPage<OperationLog> ycPage = PageUtil.queryYcPage(operationLogDAO, filters, pageNumber, pageSize, new Sort(
				Direction.DESC, "id"), OperationLog.class);

		YcPage<OperationLogVO> result = new YcPage<OperationLogVO>();
		result.setPageTotal(ycPage.getPageTotal());
		result.setCountTotal(ycPage.getCountTotal());
		List<OperationLog> list = ycPage.getList();
		List<OperationLogVO> voList = new ArrayList<OperationLogVO>();
		OperationLogVO vo = null;

		// 迭代查询结果 转换为页面显示对象
		for (OperationLog temp : list)
		{
			vo = copyRecordEntityToVO(temp);
			voList.add(vo);
		}

		result.setList(voList);

		return result;
	}

	// 数据库对象转换为页面显示对象
	private OperationLogVO copyRecordEntityToVO(OperationLog temp)
	{
		OperationLogVO vo = new OperationLogVO();
		vo.setOperationTypeLabel(dataSourceService.findOptionVOById(CommonConstant.DataSourceName.LOG_ORERATION_TYPE,
				String.valueOf(temp.getOperationType())).getText());
		vo.setOperationIp(temp.getOperationIp());
		vo.setOperationTime(temp.getOperationTime());
		vo.setOperationObj(temp.getOperationObj());
		vo.setUsername(temp.getUsername());
		vo.setRemark(temp.getRemark());
		return vo;
	}
}
