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
import com.yiguang.payment.common.logging.entity.LoginAndLogoutLog;
import com.yiguang.payment.common.logging.repository.LoginAndLogoutLogDAO;
import com.yiguang.payment.common.logging.service.LoginAndLogoutLogService;
import com.yiguang.payment.common.logging.vo.LoginAndLogoutLogVO;
import com.yiguang.payment.common.query.PageUtil;
import com.yiguang.payment.common.query.YcPage;

@Service("loginAndLogoutLogService")
@Transactional
public class LoginAndLogoutLogServiceImpl implements LoginAndLogoutLogService
{

	private static Logger logger = LoggerFactory.getLogger(LoginAndLogoutLogServiceImpl.class);
	@Autowired
	private LoginAndLogoutLogDAO loginAndLogoutLogDAO;

	@Autowired
	private DataSourceService dataSourceService;

	@Override
	public void recordLoginAndLogoutLog(LoginAndLogoutLog log)
	{
		logger.debug("recordLoginAndLogoutLog start, log: [" + log + "]");
		try
		{
			loginAndLogoutLogDAO.save(log);

		}
		catch (Exception e)
		{
			logger.error("recordLoginAndLogoutLog failed");
			logger.error(e.getLocalizedMessage(), e);
			throw new RpcException(ErrorCodeConst.ErrorCode99998);
		}
	}

	@Override
	public YcPage<LoginAndLogoutLogVO> showLogList(Map<String, Object> searchParams, int pageNumber, int pageSize,
			String sortType)
	{

		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);

		// 调用查询API获得登陆日志记录集
		YcPage<LoginAndLogoutLog> ycPage = PageUtil.queryYcPage(loginAndLogoutLogDAO, filters, pageNumber, pageSize,
				new Sort(Direction.DESC, "id"), LoginAndLogoutLog.class);

		YcPage<LoginAndLogoutLogVO> result = new YcPage<LoginAndLogoutLogVO>();
		result.setPageTotal(ycPage.getPageTotal());
		result.setCountTotal(ycPage.getCountTotal());
		List<LoginAndLogoutLog> list = ycPage.getList();
		List<LoginAndLogoutLogVO> voList = new ArrayList<LoginAndLogoutLogVO>();
		LoginAndLogoutLogVO vo = null;

		// 迭代查询结果 转换为页面显示对象
		for (LoginAndLogoutLog temp : list)
		{
			vo = copyRecordEntityToVO(temp);
			voList.add(vo);
		}

		result.setList(voList);

		return result;
	}

	// 数据库对象转换为页面显示对象
	private LoginAndLogoutLogVO copyRecordEntityToVO(LoginAndLogoutLog temp)
	{
		LoginAndLogoutLogVO vo = new LoginAndLogoutLogVO();
		vo.setOperationTypeLabel(dataSourceService.findOptionVOById(CommonConstant.DataSourceName.LOGIN_LOGOUT_TYPE,
				String.valueOf(temp.getOperationType())).getText());
		vo.setOperationIp(temp.getOperationIp());
		vo.setOperationTime(temp.getOperationTime());
		vo.setUsername(temp.getUsername());
		vo.setRemark(temp.getRemark());
		return vo;
	}

}
