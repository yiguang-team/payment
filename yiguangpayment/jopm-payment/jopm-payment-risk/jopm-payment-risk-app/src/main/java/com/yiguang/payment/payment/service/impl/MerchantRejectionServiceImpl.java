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
import com.yiguang.payment.payment.entity.MerchantRejection;
import com.yiguang.payment.payment.repository.MerchantRejectionDao;
import com.yiguang.payment.payment.service.MerchantRejectionService;
import com.yiguang.payment.payment.vo.MerchantRejectionVO;

@Service("merchantRejectionService")
@Transactional
public class MerchantRejectionServiceImpl implements MerchantRejectionService
{

	private static Logger logger = LoggerFactory.getLogger(MerchantRejectionServiceImpl.class);

	@Autowired
	private MerchantRejectionDao merchantRejectionDao;

	@Autowired
	private DataSourceService dataSourceService;

	private Specification<MerchantRejection> getPageQuerySpec(final MerchantRejectionVO vo)
	{
		Specification<MerchantRejection> spec = new Specification<MerchantRejection>(){
			@Override
			public Predicate toPredicate(Root<MerchantRejection> root,  
		            CriteriaQuery<?> query, CriteriaBuilder cb) {  
		        
				List<Predicate> predicateList = new ArrayList<Predicate>();
				if (vo.getMerchantA() != -1)
				{
					predicateList.add(cb.equal(root.get("merchantA").as(Long.class), vo.getMerchantA()));  
				}
				
				if (vo.getStatus() != -1)
				{
					predicateList.add(cb.equal(root.get("status").as(Integer.class), vo.getStatus()));  
				}
				
				if (vo.getMerchantB() != -1)
				{
					predicateList.add(cb.equal(root.get("merchantB").as(Long.class), vo.getMerchantB()));  
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
	@Cacheable(value="merchantRejectionCache")
	public YcPage<MerchantRejectionVO> queryMerchantRejectionList(MerchantRejectionVO conditionVO, int pageNumber,
			int pageSize, String sortType)
	{
		logger.debug("queryMerchantRejectionList start");
		try
		{
			Specification<MerchantRejection> spec = getPageQuerySpec(conditionVO);
			YcPage<MerchantRejection> ycPage = PageUtil.queryYcPage(merchantRejectionDao, spec, pageNumber,
					pageSize, new Sort(Direction.DESC, "id"), MerchantRejection.class);

			YcPage<MerchantRejectionVO> result = new YcPage<MerchantRejectionVO>();
			result.setPageTotal(ycPage.getPageTotal());
			result.setCountTotal(ycPage.getCountTotal());
			List<MerchantRejection> list = ycPage.getList();
			List<MerchantRejectionVO> voList = new ArrayList<MerchantRejectionVO>();
			MerchantRejectionVO vo = null;
			for (MerchantRejection temp : list)
			{
				vo = copyPropertiesToVO(temp);
				voList.add(vo);
			}
			result.setList(voList);
			logger.debug("queryMerchantRejectionList end");
			return result;
		}
		catch (RpcException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			logger.error("queryMerchantRejectionList failed");
			logger.error(e.getLocalizedMessage(), e);
			throw new RpcException(ErrorCodeConst.ErrorCode99998);
		}
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Override
	@CacheEvict(value = "merchantRejectionCache",allEntries=true,beforeInvocation=true)
	public MerchantRejection saveMerchantRejection(MerchantRejection merchantRejection)
	{
		logger.debug("saveMerchantRejection start, merchantRejection [" + merchantRejection.toString() + "]");
		try
		{

			MerchantRejection queryMerchantRejection = merchantRejectionDao.queryMerchantRejectionByMerchant(
					merchantRejection.getMerchantA(), merchantRejection.getMerchantB());
			// 唯一性
			if (BeanUtils.isNotNull(queryMerchantRejection) && merchantRejection.getId() == 0)
			{

				logger.error("saveMerchantRejection failed, merchantRejection [" + merchantRejection.toString() + "]");
				throw new RpcException(ErrorCodeConst.ErrorCode10506);
			}
			else
			{
				merchantRejection = merchantRejectionDao.save(merchantRejection);
			}

			logger.debug("saveMerchantRejection end, merchantRejection [" + merchantRejection.toString() + "]");
			return merchantRejection;
		}
		catch (RpcException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			logger.error("saveMerchantRejection failed, merchantRejection [" + merchantRejection.toString() + "]");
			logger.error(e.getLocalizedMessage(), e);
			throw new RpcException(ErrorCodeConst.ErrorCode99998);
		}
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Override
	@CacheEvict(value = "merchantRejectionCache",allEntries=true,beforeInvocation=true)
	public String deleteMerchantRejection(MerchantRejection merchantRejection) {
		logger.debug("deleteMerchantRejection start, merchantRejection [" + merchantRejection.toString() + "]");
		try
		{
			MerchantRejection queryProduct = merchantRejectionDao.findOne(merchantRejection.getId());
			if (queryProduct != null)
			{
				merchantRejectionDao.delete(queryProduct);
			}
			else
			{
				// 准备删除的产品不存在！
				logger.error("deleteMerchantRejection failed, merchantRejection [" + merchantRejection.toString() + "]");
				throw new RpcException(19409);
			}
			logger.debug("deleteMerchantRejection end, merchantRejection [" + merchantRejection.toString() + "]");
			return Constant.Common.SUCCESS;
		}
		catch (RpcException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			logger.error("deleteMerchantRejection failed, merchantRejection [" + merchantRejection.toString() + "]");
			logger.error(e.getLocalizedMessage(), e);
			throw new RpcException(ErrorCodeConst.ErrorCode99998);
		}
	}
	
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Override
	@CacheEvict(value = "merchantRejectionCache",allEntries=true,beforeInvocation=true)
	public MerchantRejection queryMerchantRejection(long id)
	{
		logger.debug("queryMerchantRejection start, id [" + id + "]");
		try
		{
			MerchantRejection merchantRejection = merchantRejectionDao.findOne(id);
			logger.debug("queryProduct end, id [" + id + "]");
			return merchantRejection;
		}
		catch (Exception e)
		{
			logger.error("queryMerchantRejection failed, id [" + id + "]");
			logger.error(e.getLocalizedMessage(), e);
			throw new RpcException(ErrorCodeConst.ErrorCode99998);
		}
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Override
	@Cacheable(value="merchantRejectionCache",key="#root.methodName+#merchantA+#merchantB")
	public MerchantRejection queryMerchantRejectionByMerchant(long merchantA, long merchantB)
	{
		logger.debug("queryMerchantRejectionByMerchant start, merchantA [" + merchantA + "]  merchantB[" + merchantB
				+ "]");
		try
		{
			MerchantRejection merchantRejection = merchantRejectionDao.queryMerchantRejectionByMerchant(merchantA,
					merchantB);
			logger.debug("queryMerchantRejectionByMerchant end, merchantA [" + merchantA + "]  merchantB[" + merchantB
					+ "]");
			return merchantRejection;
		}
		catch (Exception e)
		{
			logger.error("queryMerchantRejectionByMerchant failed, merchantA [" + merchantA + "]  merchantB["
					+ merchantB + "]");
			logger.error(e.getLocalizedMessage(), e);
			throw new RpcException(ErrorCodeConst.ErrorCode99998);
		}
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Override
	@Cacheable(value="merchantRejectionCache",key="#root.methodName+#merchantB")
	public List<MerchantRejection> queryMerchantRejectionByMerchantB(long merchantB)
	{
		logger.debug("queryMerchantRejectionByMerchant start, merchantA [merchantB[" + merchantB + "]");
		try
		{
			List<MerchantRejection> list = merchantRejectionDao.queryMerchantRejectionByMerchantB(merchantB);
			logger.debug("queryMerchantRejectionByMerchant end, merchantA [merchantB[" + merchantB + "]");
			return list;
		}
		catch (Exception e)
		{
			logger.error("queryMerchantRejectionByMerchant failed, merchantA [merchantB[" + merchantB + "]");
			logger.error(e.getLocalizedMessage(), e);
			throw new RpcException(ErrorCodeConst.ErrorCode99998);
		}
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Override
	public MerchantRejectionVO copyPropertiesToVO(MerchantRejection temp)
	{
		logger.debug("copyPropertiesToVO start");
		try
		{
			MerchantRejectionVO vo = new MerchantRejectionVO();
			vo.setId(temp.getId());
			vo.setMerchantA(temp.getMerchantA());
			vo.setMerchantB(temp.getMerchantB());
			vo.setStatus(temp.getStatus());
			vo.setRemark(temp.getRemark());
			vo.setStatusLabel(dataSourceService.findOptionVOById(CommonConstant.DataSourceName.COMMON_STATUS,
					String.valueOf(temp.getStatus())).getText());
			vo.setMerchantALabel(dataSourceService.findOptionVOById(CommonConstant.DataSourceName.MERCHANT,
					String.valueOf(temp.getMerchantA())).getText());
			vo.setMerchantBLabel(dataSourceService.findOptionVOById(CommonConstant.DataSourceName.MERCHANT,
					String.valueOf(temp.getMerchantB())).getText());

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

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Override
	@CacheEvict(value = "merchantRejectionCache",allEntries=true,beforeInvocation=true)
	public MerchantRejection updateMerchantRejection(MerchantRejection merchantRejection)
	{
		try
		{
			logger.error("updateMerchantRejection start, merchantRejection:[" + merchantRejection.toString() + "]");
			MerchantRejection rejection = merchantRejectionDao.findOne(merchantRejection.getId());
			if (rejection != null)
			{
				rejection.setStatus(merchantRejection.getStatus());
				rejection = merchantRejectionDao.save(rejection);

			}
			else
			{
				logger.error("updateMerchantRejection failed, merchantRejection:[" + merchantRejection.toString() + "]");
				throw new RpcException(ErrorCodeConst.ErrorCode10505);
			}
			logger.debug("updateMerchantRejection end, merchantRejection:[" + merchantRejection.toString() + "]");
			return rejection;
		}
		catch (RpcException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			logger.error("updateMerchantRejection failed, merchantRejection:[" + merchantRejection.toString() + "]");
			logger.error(e.getLocalizedMessage(), e);
			throw new RpcException(ErrorCodeConst.ErrorCode99998);
		}
	}

}
