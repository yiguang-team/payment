package com.yiguang.payment.common.numsection.service.impl;

/**
 * 运营商逻辑层
 * @author Jinger
 * @date：2013-10-18
 *
 */
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
import com.yiguang.payment.common.numsection.entity.CarrierInfo;
import com.yiguang.payment.common.numsection.repository.CarrierInfoDao;
import com.yiguang.payment.common.numsection.service.CarrierInfoService;
import com.yiguang.payment.common.numsection.vo.CarrierInfoVO;
import com.yiguang.payment.common.query.PageUtil;
import com.yiguang.payment.common.query.YcPage;
import com.yiguang.payment.common.utils.BeanUtils;
import com.yiguang.payment.common.utils.StringUtil;

@Service("carrierInfoService")
public class CarrierInfoServiceImpl implements CarrierInfoService
{
	@Autowired
	private CarrierInfoDao carrierInfoDao;

	@Autowired
	private DataSourceService dataSourceService;

	private static Logger logger = LoggerFactory.getLogger(CarrierInfoServiceImpl.class);

	private Specification<CarrierInfo> getPageQuerySpec(final CarrierInfoVO vo)
	{
		Specification<CarrierInfo> spec = new Specification<CarrierInfo>(){
			@Override
			public Predicate toPredicate(Root<CarrierInfo> root,  
		            CriteriaQuery<?> query, CriteriaBuilder cb) {  
		        
				List<Predicate> predicateList = new ArrayList<Predicate>();
				if (vo.getCarrierType() != -1)
				{
					predicateList.add(cb.equal(root.get("carrierType").as(Integer.class), vo.getCarrierType()));  
				}
				
				if (vo.getStatus() != -1)
				{
					predicateList.add(cb.equal(root.get("status").as(Integer.class), vo.getStatus()));  
				}
				
				if (StringUtil.isNotEmpty(vo.getCarrierName()))
				{
					predicateList.add(cb.equal(root.get("carrierName").as(String.class), vo.getCarrierName().trim()));  
				}
				if (StringUtil.isNotEmpty(vo.getCarrierNo()))
				{
					predicateList.add(cb.equal(root.get("carrierNo").as(String.class), vo.getCarrierNo().trim()));  
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
	@Cacheable(value="carrierCache")
	public YcPage<CarrierInfoVO> queryCarrierInfoList(CarrierInfoVO conditionVO, int pageNumber, int pageSize,
			String sortType)
	{
		logger.debug("queryCarrierInfoList start");
		try
		{
			Specification<CarrierInfo> spec = getPageQuerySpec(conditionVO);
			YcPage<CarrierInfo> ycPage = PageUtil.queryYcPage(carrierInfoDao, spec, pageNumber, pageSize, new Sort(
					Direction.DESC, "id"), CarrierInfo.class);
			YcPage<CarrierInfoVO> result = new YcPage<CarrierInfoVO>();
			result.setPageTotal(ycPage.getPageTotal());
			result.setCountTotal(ycPage.getCountTotal());
			List<CarrierInfo> list = ycPage.getList();
			List<CarrierInfoVO> voList = new ArrayList<CarrierInfoVO>();
			CarrierInfoVO vo = null;
			for (CarrierInfo temp : list)
			{
				vo = copyPropertiesToVO(temp);
				voList.add(vo);
			}
			result.setList(voList);
			logger.debug("queryCarrierInfoList end");
			return result;
		}
		catch (Exception e)
		{
			logger.error("queryCarrierInfoList failed");
			logger.error(e.getLocalizedMessage(), e);
			throw new RpcException(ErrorCodeConst.ErrorCode99998);
		}
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Override
	public CarrierInfoVO copyPropertiesToVO(CarrierInfo temp)
	{
		logger.debug("copyPropertiesToVO start");
		try
		{
			CarrierInfoVO vo = new CarrierInfoVO();

			vo.setId(temp.getId());
			vo.setCarrierNo(temp.getCarrierNo());
			vo.setCarrierName(temp.getCarrierName());
			vo.setCarrierType(temp.getCarrierType());
			vo.setStatus(temp.getStatus());

			vo.setStatusLabel(dataSourceService.findOptionVOById(CommonConstant.DataSourceName.COMMON_STATUS,
					String.valueOf(temp.getStatus())).getText());
			vo.setCarrierTypeLabel(dataSourceService.findOptionVOById(CommonConstant.DataSourceName.CARRIER_TYPE,
					String.valueOf(temp.getCarrierType())).getText());
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
	@CacheEvict(value={"carrierCache","dataSourceCache"},allEntries=true,beforeInvocation=true)
	public CarrierInfo saveCarrierInfo(CarrierInfo carrierInfo)
	{
		try
		{
			logger.debug("saveCarrierInfo start, carrierInfo:[" + carrierInfo.toString() + "]");
			String carrierName = carrierInfo.getCarrierName();
			CarrierInfo carrier = carrierInfoDao.getByName(carrierName);
			// 判断唯一性
			if (BeanUtils.isNotNull(carrier) && carrier.getId() == 0)
			{
				logger.error("saveCarrierInfo failed, carrierInfo:[" + carrierInfo.toString() + "]");
				throw new RpcException(ErrorCodeConst.ErrorCode10201);
			}
			else
			{
				carrierInfo = carrierInfoDao.save(carrierInfo);
			}
			logger.debug("saveCarrierInfo end, carrierInfo:[" + carrierInfo.toString() + "]");
			return carrierInfo;
		}
		catch (RpcException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			logger.error("saveCarrierInfo failed, carrierInfo:[" + carrierInfo.toString() + "]");
			logger.error(e.getLocalizedMessage(), e);
			throw new RpcException(ErrorCodeConst.ErrorCode99998);
		}
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Override
	@CacheEvict(value={"carrierCache","dataSourceCache"},allEntries=true,beforeInvocation=true)
	public CarrierInfo updateCarrierStatus(CarrierInfo carrierInfo)
	{

		try
		{
			logger.error("updateCarrierStatus start, carrierInfo:[" + carrierInfo.toString() + "]");
			CarrierInfo newcarrierInfo = carrierInfoDao.findOne(carrierInfo.getId());
			if (newcarrierInfo != null)
			{
				newcarrierInfo.setStatus(carrierInfo.getStatus());
				newcarrierInfo = carrierInfoDao.save(newcarrierInfo);
				long carrierId = newcarrierInfo.getId();

			}
			else
			{
				logger.error("updateCarrierStatus failed, carrierInfo:[" + carrierInfo.toString() + "]");
				throw new RpcException(ErrorCodeConst.ErrorCode10202);
			}
			logger.debug("updateCarrierStatus end, carrierInfo:[" + carrierInfo.toString() + "]");
			return newcarrierInfo;
		}
		catch (RpcException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			logger.error("updateCarrierStatus failed, carrierInfo:[" + carrierInfo.toString() + "]");
			logger.error(e.getLocalizedMessage(), e);
			throw new RpcException(ErrorCodeConst.ErrorCode99998);
		}
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Override
	@CacheEvict(value={"carrierCache","dataSourceCache"},allEntries=true,beforeInvocation=true)
	public String deleteCarrier(CarrierInfo carrierInfo)
	{
		try
		{
			logger.debug("deleteCarrier start, carrierInfo:[" + carrierInfo.toString() + "]");
			CarrierInfo carrier = carrierInfoDao.findOne(carrierInfo.getId());
			if (carrier != null)
			{
				carrierInfoDao.delete(carrier);
			}
			else
			{
				// 准备删除的产品不存在！
				logger.error("deleteCarrier failed, carrierInfo:[" + carrierInfo.toString() + "]");
				throw new RpcException(ErrorCodeConst.ErrorCode10202);
			}
			logger.debug("deleteCarrier end, carrierInfo:[" + carrierInfo.toString() + "]");
			return Constant.Common.SUCCESS;
		}
		catch (RpcException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			logger.error("updateCarrierStatus failed, carrierInfo:[" + carrierInfo.toString() + "]");
			logger.error(e.getLocalizedMessage(), e);
			throw new RpcException(ErrorCodeConst.ErrorCode99998);
		}
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Override
	@Cacheable(value="carrierCache",key="#root.methodName+#id")
	public CarrierInfo queryCarrierInfo(long id)
	{
		logger.debug("queryCarrierInfo start, id:[" + id + "]");
		try
		{
			CarrierInfo carrierInfo = carrierInfoDao.findOne(id);
			logger.debug("queryCarrierInfo end, id:[" + id + "]");
			return carrierInfo;
		}
		catch (RpcException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			logger.error("queryCarrierInfo failed, id:[" + id + "]");
			logger.error(e.getLocalizedMessage(), e);
			throw new RpcException(ErrorCodeConst.ErrorCode99998);
		}

	}
}
