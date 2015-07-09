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
import com.yiguang.payment.common.logging.entity.OperationLog;
import com.yiguang.payment.common.logging.repository.OperationLogDAO;
import com.yiguang.payment.common.logging.service.OperationLogService;
import com.yiguang.payment.common.logging.vo.OperationLogVO;
import com.yiguang.payment.common.query.PageUtil;
import com.yiguang.payment.common.query.YcPage;
import com.yiguang.payment.common.utils.StringUtil;

@Service("operationLogService")
@Transactional
public class OperationLogServiceImpl implements OperationLogService
{
	@Autowired
	private DataSourceService dataSourceService;

	private static Logger logger = LoggerFactory.getLogger(OperationLogServiceImpl.class);

	@Autowired
	private OperationLogDAO operationLogDAO;

	private Specification<OperationLog> getPageQuerySpec(final OperationLogVO vo)
	{
		Specification<OperationLog> spec = new Specification<OperationLog>(){
			@Override
			public Predicate toPredicate(Root<OperationLog> root,  
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
				
				if (StringUtil.isNotEmpty(vo.getOperationObj()))
				{
					predicateList.add(cb.equal(root.get("operationObj").as(String.class), vo.getOperationObj().trim()));  
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
	public YcPage<OperationLogVO> showLogList(OperationLogVO conditionVO, int pageNumber, int pageSize,
			String sortType)
	{

		Specification<OperationLog> spec = getPageQuerySpec(conditionVO);

		// 调用查询API获得操作日志记录集
		YcPage<OperationLog> ycPage = PageUtil.queryYcPage(operationLogDAO, spec, pageNumber, pageSize, new Sort(
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
