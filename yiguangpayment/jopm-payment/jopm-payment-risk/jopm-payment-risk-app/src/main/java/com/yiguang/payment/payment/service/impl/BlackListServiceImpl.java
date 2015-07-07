package com.yiguang.payment.payment.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.rpc.RpcException;
import com.yiguang.payment.common.CommonConstant;
import com.yiguang.payment.common.Constant;
import com.yiguang.payment.common.datasource.service.DataSourceService;
import com.yiguang.payment.common.exception.ErrorCodeConst;
import com.yiguang.payment.common.query.PageUtil;
import com.yiguang.payment.common.query.YcPage;
import com.yiguang.payment.common.utils.BeanUtils;
import com.yiguang.payment.common.utils.StringUtil;
import com.yiguang.payment.payment.entity.BlackList;
import com.yiguang.payment.payment.repository.BlackListDao;
import com.yiguang.payment.payment.service.BlackListService;
import com.yiguang.payment.payment.vo.BlackListVO;

@Service("blackListService")
@Transactional
public class BlackListServiceImpl implements BlackListService
{
	private static Logger logger = LoggerFactory.getLogger(BlackListServiceImpl.class);

	@Autowired
	private BlackListDao blackListDao;

	@Autowired
	private DataSourceService dataSourceService;

	private Specification<BlackList> getPageQuerySpec(final BlackListVO vo)
	{
		Specification<BlackList> spec = new Specification<BlackList>(){
			@Override
			public Predicate toPredicate(Root<BlackList> root,  
		            CriteriaQuery<?> query, CriteriaBuilder cb) {  
		        
				List<Predicate> predicateList = new ArrayList<Predicate>();
				if (vo.getType() != -1)
				{
					predicateList.add(cb.equal(root.get("type").as(Integer.class), vo.getType()));  
				}
				
				if (vo.getStatus() != -1)
				{
					predicateList.add(cb.equal(root.get("status").as(Integer.class), vo.getStatus()));  
				}
				
				if (StringUtil.isNotEmpty(vo.getValue()))
				{
					predicateList.add(cb.equal(root.get("value").as(String.class), vo.getValue().trim()));  
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
	@Cacheable(value="blackListCache")
	public YcPage<BlackListVO> queryBlackList(BlackListVO conditionVO, int pageNumber, int pageSize,
			String sortType)
	{
		logger.debug("queryBlackList start");
		try
		{
			Specification<BlackList> spec = getPageQuerySpec(conditionVO);
			YcPage<BlackList> ycPage = PageUtil.queryYcPage(blackListDao, spec, pageNumber, pageSize, new Sort(
					Direction.DESC, "id"), BlackList.class);

			YcPage<BlackListVO> result = new YcPage<BlackListVO>();
			result.setPageTotal(ycPage.getPageTotal());
			result.setCountTotal(ycPage.getCountTotal());
			List<BlackList> list = ycPage.getList();
			List<BlackListVO> voList = new ArrayList<BlackListVO>();
			BlackListVO vo = null;
			for (BlackList temp : list)
			{
				vo = copyPropertiesToVO(temp);
				voList.add(vo);
			}
			result.setList(voList);
			logger.debug("queryBlackList end");
			return result;
		}
		catch (RpcException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			logger.error("queryBlackList failed");
			logger.error(e.getLocalizedMessage(), e);
			throw new RpcException(ErrorCodeConst.ErrorCode99998);
		}
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Override
	@CacheEvict(value = "blackListCache",allEntries=true,beforeInvocation=true)
	public BlackList updateBlackListStatus(BlackList blackList)
	{
		try
		{
			logger.debug("updateBlackListStatus start, blackList [" + blackList.toString() + "]");
			BlackList list = blackListDao.findOne(blackList.getId());
			if (list != null)
			{
				list.setStatus(blackList.getStatus());
				list = blackListDao.save(list);
			}
			else
			{
				// 不存在
				logger.error("updateBlackListStatus failed, blackList [" + blackList.toString() + "]");
				throw new RpcException(ErrorCodeConst.ErrorCode10519);
			}
			logger.debug("updateBlackListStatus end, blackList [" + blackList.toString() + "]");
			return list;
		}
		catch (RpcException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			logger.error("updateBlackListStatus failed, blackList [" + blackList.toString() + "]");
			logger.error(e.getLocalizedMessage(), e);
			throw new RpcException(ErrorCodeConst.ErrorCode99998);
		}
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Override
	@CacheEvict(value = "blackListCache",allEntries=true,beforeInvocation=true)
	public String deleteBlackList(BlackList blackList) {
		logger.debug("deleteBlackList start, blackList [" + blackList.toString() + "]");
		try
		{
			BlackList queryProduct = blackListDao.findOne(blackList.getId());
			if (queryProduct != null)
			{
				blackListDao.delete(queryProduct);
			}
			else
			{
				// 准备删除的产品不存在！
				logger.error("deleteBlackList failed, blackList [" + blackList.toString() + "]");
				throw new RpcException(19409);
			}
			logger.debug("deleteBlackList end, blackList [" + blackList.toString() + "]");
			return Constant.Common.SUCCESS;
		}
		catch (RpcException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			logger.error("deleteBlackList failed, blackList [" + blackList.toString() + "]");
			logger.error(e.getLocalizedMessage(), e);
			throw new RpcException(ErrorCodeConst.ErrorCode99998);
		}
	}
	
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Override
	@CacheEvict(value = "blackListCache",allEntries=true,beforeInvocation=true)
	public BlackList saveBlackList(BlackList blackList)
	{
		logger.debug("saveBlackList start, blackList [" + blackList.toString() + "]");
		try
		{

			BlackList list = blackListDao.queryBlackListByALL(blackList.getType(), blackList.getValue());
			// 唯一性
			if (BeanUtils.isNotNull(list) && blackList.getId() == 0)
			{

				logger.error("saveMerchantLimit fail, blackList [" + blackList.toString() + "]");
				throw new RpcException(ErrorCodeConst.ErrorCode10520);
			}
			else
			{
				blackList = blackListDao.save(blackList);
			}

			logger.debug("saveBlackList end, blackList [" + blackList.toString() + "]");
			return blackList;
		}
		catch (RpcException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			logger.error("saveBlackList failed, blackList [" + blackList.toString() + "]");
			logger.error(e.getLocalizedMessage(), e);
			throw new RpcException(ErrorCodeConst.ErrorCode99998);
		}
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Override
	@Cacheable(value="blackListCache",key="#root.methodName+#id")
	public BlackList queryBlackListById(long id)
	{
		logger.debug("queryBlackList start, id [" + id + "]");
		try
		{
			BlackList blackList = blackListDao.findOne(id);
			logger.debug("queryBlackList end, id [" + id + "]");
			return blackList;
		}
		catch (Exception e)
		{
			logger.error("queryBlackList failed, id [" + id + "]");
			logger.error(e.getLocalizedMessage(), e);
			throw new RpcException(ErrorCodeConst.ErrorCode99998);
		}
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Override
	@Cacheable(value="blackListCache",key="#root.methodName+#type+#value")
	public BlackList queryBlackListByAll(int type, String value)
	{
		logger.debug("queryBlackListByAll start, type,value [" + type + "," + value + "]");
		try
		{
			BlackList blackList = blackListDao.queryBlackListByALL(type, value);
			logger.debug("queryBlackListByAll end, type,value [" + type + "," + value + "]");
			return blackList;
		}
		catch (Exception e)
		{
			logger.error("queryBlackListByAll failed, type,value [" + type + "," + value + "]");
			logger.error(e.getLocalizedMessage(), e);
			throw new RpcException(ErrorCodeConst.ErrorCode99998);
		}
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Override
	public BlackListVO copyPropertiesToVO(BlackList temp)
	{
		logger.debug("copyPropertiesToVO start");
		try
		{
			BlackListVO vo = new BlackListVO();
			vo.setId(temp.getId());
			vo.setType(temp.getType());
			vo.setValue(temp.getValue());
			vo.setStatus(temp.getStatus());

			vo.setTypeLabel(dataSourceService.findOptionVOById(CommonConstant.DataSourceName.LIST_TYPE,
					String.valueOf(temp.getType())).getText());
			vo.setStatusLabel(dataSourceService.findOptionVOById(CommonConstant.DataSourceName.COMMON_STATUS,
					String.valueOf(temp.getStatus())).getText());
			logger.debug("copyPropertiesToVO end");
			return vo;
		}
		catch (RpcException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			logger.error("copyPropertiesToVO failed");
			logger.error(e.getLocalizedMessage(), e);
			throw new RpcException(ErrorCodeConst.ErrorCode99998);
		}
	}

}
