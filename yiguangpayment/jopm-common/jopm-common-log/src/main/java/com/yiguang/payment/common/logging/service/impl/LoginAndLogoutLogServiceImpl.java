package com.yiguang.payment.common.logging.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
import com.yiguang.payment.common.utils.StringUtil;

@Service("loginAndLogoutLogService")
@Transactional
public class LoginAndLogoutLogServiceImpl implements LoginAndLogoutLogService
{

	private static Logger logger = LoggerFactory.getLogger(LoginAndLogoutLogServiceImpl.class);
	@Autowired
	private LoginAndLogoutLogDAO loginAndLogoutLogDAO;

	@Autowired
	private DataSourceService dataSourceService;

	private Specification<LoginAndLogoutLog> getPageQuerySpec(final LoginAndLogoutLogVO vo)
	{
		Specification<LoginAndLogoutLog> spec = new Specification<LoginAndLogoutLog>(){
			@Override
			public Predicate toPredicate(Root<LoginAndLogoutLog> root,  
		            CriteriaQuery<?> query, CriteriaBuilder cb) {  
		        
				List<Predicate> predicateList = new ArrayList<Predicate>();
				if (vo.getOperationType() != -1)
				{
					predicateList.add(cb.equal(root.get("operationType").as(Integer.class), vo.getOperationType())); 
				}
				
				if (StringUtil.isNotEmpty(vo.getUsername()))
				{
					predicateList.add(cb.equal(root.get("username").as(String.class), vo.getUsername().trim()));  
				}
				
				if (StringUtil.isNotEmpty(vo.getRemark()))
				{
					predicateList.add(cb.equal(root.get("remark").as(String.class), vo.getRemark().trim()));  
				}
				
				
				Predicate[] p = new Predicate[predicateList.size()];  
		        query.where(cb.and(predicateList.toArray(p)));  
		        //添加排序的功能  
		        query.orderBy(cb.asc(root.get("id").as(Integer.class)));  
		          
		        return query.getRestriction();  
			}
		};
		
		return spec;
	}
	
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
	public YcPage<LoginAndLogoutLogVO> showLogList(LoginAndLogoutLogVO conditionVO, int pageNumber, int pageSize,
			String sortType)
	{
		Specification<LoginAndLogoutLog> spec = getPageQuerySpec(conditionVO);
		
		// 调用查询API获得登陆日志记录集 JpaSpecificationExecutor 
		YcPage<LoginAndLogoutLog> ycPage = PageUtil.queryYcPage(loginAndLogoutLogDAO, spec, pageNumber, pageSize,
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
