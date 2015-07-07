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
import com.yiguang.payment.payment.entity.WhiteList;
import com.yiguang.payment.payment.repository.WhiteListDao;
import com.yiguang.payment.payment.service.WhiteListService;
import com.yiguang.payment.payment.vo.WhiteListVO;

@Service("whiteListService")
@Transactional
public class WhiteListServiceImpl implements WhiteListService
{
	private static Logger logger = LoggerFactory.getLogger(WhiteListServiceImpl.class);

	@Autowired
	private WhiteListDao whiteListDao;

	@Autowired
	private DataSourceService dataSourceService;

	private Specification<WhiteList> getPageQuerySpec(final WhiteListVO vo)
	{
		Specification<WhiteList> spec = new Specification<WhiteList>(){
			@Override
			public Predicate toPredicate(Root<WhiteList> root,  
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
	@Cacheable(value="whiteListCache")
	public YcPage<WhiteListVO> queryWhiteList(WhiteListVO conditionVO, int pageNumber, int pageSize,
			String sortType)
	{
		logger.debug("queryWhiteList start");
		try
		{
			Specification<WhiteList> spec = getPageQuerySpec(conditionVO);
			YcPage<WhiteList> ycPage = PageUtil.queryYcPage(whiteListDao, spec, pageNumber, pageSize, new Sort(
					Direction.DESC, "id"), WhiteList.class);

			YcPage<WhiteListVO> result = new YcPage<WhiteListVO>();
			result.setPageTotal(ycPage.getPageTotal());
			result.setCountTotal(ycPage.getCountTotal());
			List<WhiteList> list = ycPage.getList();
			List<WhiteListVO> voList = new ArrayList<WhiteListVO>();
			WhiteListVO vo = null;
			for (WhiteList temp : list)
			{
				vo = copyPropertiesToVO(temp);
				voList.add(vo);
			}
			result.setList(voList);
			logger.debug("queryWhiteList end");
			return result;
		}
		catch (RpcException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			logger.error("queryWhiteList failed");
			logger.error(e.getLocalizedMessage(), e);
			throw new RpcException(ErrorCodeConst.ErrorCode99998);
		}
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Override
	@CacheEvict(value = "whiteListCache",allEntries=true,beforeInvocation=true)
	public WhiteList updateWhiteListStatus(WhiteList whiteList)
	{
		try
		{
			logger.debug("updateWhiteListStatus start, whiteList [" + whiteList.toString() + "]");
			WhiteList list = whiteListDao.findOne(whiteList.getId());
			if (list != null)
			{
				list.setStatus(whiteList.getStatus());
				list = whiteListDao.save(list);
			}
			else
			{
				// 不存在
				logger.error("updateWhiteListStatus failed, whiteList [" + whiteList.toString() + "]");
				throw new RpcException(ErrorCodeConst.ErrorCode10521);
			}
			logger.debug("updateWhiteListStatus end, blackList [" + whiteList.toString() + "]");
			return list;
		}
		catch (RpcException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			logger.error("updateWhiteListStatus failed, whiteList [" + whiteList.toString() + "]");
			logger.error(e.getLocalizedMessage(), e);
			throw new RpcException(ErrorCodeConst.ErrorCode99998);
		}
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Override
	@CacheEvict(value = "whiteListCache",allEntries=true,beforeInvocation=true)
	public WhiteList saveWhiteList(WhiteList whiteList)
	{
		logger.debug("saveWhiteList start, whiteList [" + whiteList.toString() + "]");
		try
		{

			WhiteList list = whiteListDao.queryWhiteListByALL(whiteList.getType(), whiteList.getValue());
			// 唯一性
			if (BeanUtils.isNotNull(list) && whiteList.getId() == 0)
			{

				logger.error("saveWhiteList fail, whiteList [" + whiteList.toString() + "]");
				throw new RpcException(ErrorCodeConst.ErrorCode10522);
			}
			else
			{
				whiteList = whiteListDao.save(whiteList);
			}

			logger.debug("saveWhiteList end, whiteList [" + whiteList.toString() + "]");
			return whiteList;
		}
		catch (RpcException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			logger.error("saveWhiteList failed, whiteList [" + whiteList.toString() + "]");
			logger.error(e.getLocalizedMessage(), e);
			throw new RpcException(ErrorCodeConst.ErrorCode99998);
		}
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Override
	@CacheEvict(value = "whiteListCache",allEntries=true,beforeInvocation=true)
	public String deleteWhiteList(WhiteList whiteList) {
		logger.debug("deleteWhiteList start, whiteList [" + whiteList.toString() + "]");
		try
		{
			WhiteList queryProduct = whiteListDao.findOne(whiteList.getId());
			if (queryProduct != null)
			{
				whiteListDao.delete(queryProduct);
			}
			else
			{
				// 准备删除的产品不存在！
				logger.error("deleteWhiteList failed, whiteList [" + whiteList.toString() + "]");
				throw new RpcException(19409);
			}
			logger.debug("deleteWhiteList end, whiteList [" + whiteList.toString() + "]");
			return Constant.Common.SUCCESS;
		}
		catch (RpcException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			logger.error("deleteWhiteList failed, whiteList [" + whiteList.toString() + "]");
			logger.error(e.getLocalizedMessage(), e);
			throw new RpcException(ErrorCodeConst.ErrorCode99998);
		}
	}
	
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Override
	@Cacheable(value = "whiteListCache",key="#root.methodName+#id")
	public WhiteList queryWhiteList(long id)
	{
		logger.debug("queryWhiteList start, id [" + id + "]");
		try
		{
			WhiteList whiteList = whiteListDao.findOne(id);
			logger.debug("queryWhiteList end, id [" + id + "]");
			return whiteList;
		}
		catch (Exception e)
		{
			logger.error("queryWhiteList failed, id [" + id + "]");
			logger.error(e.getLocalizedMessage(), e);
			throw new RpcException(ErrorCodeConst.ErrorCode99998);
		}
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Override
	@Cacheable(value = "whiteListCache",key="#root.methodName+#type+#value")
	public WhiteList queryWhiteListByAll(int type, String value)
	{
		logger.debug("queryWhiteListByAll start, type,value [" + type + "," + value + "]");
		try
		{
			WhiteList whiteList = whiteListDao.queryWhiteListByALL(type, value);
			logger.debug("queryWhiteListByAll end, type,value [" + type + "," + value + "]");
			return whiteList;
		}
		catch (Exception e)
		{
			logger.error("queryWhiteListByAll failed, type,value [" + type + "," + value + "]");
			logger.error(e.getLocalizedMessage(), e);
			throw new RpcException(ErrorCodeConst.ErrorCode99998);
		}
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Override
	public WhiteListVO copyPropertiesToVO(WhiteList temp)
	{
		logger.debug("copyPropertiesToVO start");
		try
		{
			WhiteListVO vo = new WhiteListVO();
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
