package com.yiguang.payment.common.logging.service;

import java.util.Map;

import com.yiguang.payment.common.logging.entity.LoginAndLogoutLog;
import com.yiguang.payment.common.logging.vo.LoginAndLogoutLogVO;
import com.yiguang.payment.common.query.YcPage;

public interface LoginAndLogoutLogService
{

	void recordLoginAndLogoutLog(LoginAndLogoutLog log);

	YcPage<LoginAndLogoutLogVO> showLogList(Map<String, Object> searchParams, int pageNumber, int pageSize,
			String sortType);
}
