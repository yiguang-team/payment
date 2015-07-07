package com.yiguang.payment.common.logging.service;

import com.yiguang.payment.common.logging.entity.OperationLog;
import com.yiguang.payment.common.logging.vo.OperationLogVO;
import com.yiguang.payment.common.query.YcPage;

public interface OperationLogService
{
	void recordOperationLog(OperationLog log);
	
	YcPage<OperationLogVO> showLogList(OperationLogVO conditionVO, int pageNumber, int pageSize,
			String sortType);
}
