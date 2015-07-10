package com.yiguang.payment.depot.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.rpc.RpcException;
import com.yiguang.payment.common.CommonConstant;
import com.yiguang.payment.common.Constant;
import com.yiguang.payment.common.datasource.service.DataSourceService;
import com.yiguang.payment.common.exception.ErrorCodeConst;
import com.yiguang.payment.common.exception.ExceptionUtil;
import com.yiguang.payment.common.query.PageUtil;
import com.yiguang.payment.common.query.YcPage;
import com.yiguang.payment.common.utils.BeanUtils;
import com.yiguang.payment.common.utils.BigDecimalUtil;
import com.yiguang.payment.common.utils.StringUtil;
import com.yiguang.payment.depot.entity.ProductBatch;
import com.yiguang.payment.depot.entity.ProductDepot;
import com.yiguang.payment.depot.repository.ProductBatchDao;
import com.yiguang.payment.depot.repository.ProductDepotDao;
import com.yiguang.payment.depot.service.ProductBatchService;
import com.yiguang.payment.depot.vo.ProductBatchVO;

@Service("productBatchService")
@Transactional
public class ProductBatchServiceImpl implements ProductBatchService
{

	private static Logger logger = LoggerFactory.getLogger(ProductBatchServiceImpl.class);

	@Autowired
	private ProductBatchDao productBatchDao;
	@Autowired
	private DataSourceService dataSourceService;
	@Autowired
	private ProductDepotDao productDepotDao;

	private Specification<ProductBatch> getPageQuerySpec(final ProductBatchVO vo)
	{
		Specification<ProductBatch> spec = new Specification<ProductBatch>(){
			@Override
			public Predicate toPredicate(Root<ProductBatch> root,  
		            CriteriaQuery<?> query, CriteriaBuilder cb) {  
		        
				List<Predicate> predicateList = new ArrayList<Predicate>();
				if (vo.getMerchantId() != -1)
				{
					predicateList.add(cb.equal(root.get("merchantId").as(Integer.class), vo.getMerchantId()));  
				}
				if (vo.getProductId() != -1)
				{
					predicateList.add(cb.equal(root.get("productId").as(Integer.class), vo.getProductId()));  
				}
				if (vo.getStatus() != -1)
				{
					predicateList.add(cb.equal(root.get("status").as(Integer.class), vo.getStatus()));  
				}
				if (StringUtil.isNotEmpty(vo.getBatchId()))
				{
					predicateList.add(cb.equal(root.get("batchId").as(String.class), vo.getBatchId().trim()));  
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
	public String generateBatchId()
	{
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyMMddHHmmssSS");
		Date date = new Date();
		return dateFormat.format(date);
	}

	@Override
	public YcPage<ProductBatchVO> queryProductBatchList(ProductBatchVO conditionVO, int pageNumber, int pageSize,
			String sortType)
	{
		Specification<ProductBatch> spec = getPageQuerySpec(conditionVO);
		YcPage<ProductBatch> ycPage = PageUtil.queryYcPage(productBatchDao, spec, pageNumber, pageSize, new Sort(
				Direction.DESC, "id"), ProductBatch.class);

		YcPage<ProductBatchVO> result = new YcPage<ProductBatchVO>();
		result.setPageTotal(ycPage.getPageTotal());
		result.setCountTotal(ycPage.getCountTotal());
		List<ProductBatch> list = ycPage.getList();
		List<ProductBatchVO> voList = new ArrayList<ProductBatchVO>();
		ProductBatchVO vo = null;
		for (ProductBatch temp : list)
		{
			vo = copyPropertiesToVO(temp);
			voList.add(vo);
		}

		result.setList(voList);

		return result;
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Override
	public ProductBatchVO copyPropertiesToVO(ProductBatch temp)
	{
		ProductBatchVO vo = new ProductBatchVO();
		vo.setId(temp.getId());
		vo.setBatchId(temp.getBatchId());
		vo.setCarrierId(temp.getMerchantId());
		vo.setProductId(temp.getProductId());
		vo.setStatus(temp.getStatus());
		vo.setRemark(temp.getRemark());
		vo.setTotalCount(temp.getTotalCount());
		vo.setTotalAmt(BigDecimalUtil.divide(temp.getTotalAmt(), new BigDecimal("100"), 2, BigDecimal.ROUND_HALF_UP));
		vo.setStatusLabel(dataSourceService.findOptionVOById(CommonConstant.DataSourceName.COMMON_STATUS,
				String.valueOf(temp.getStatus())).getText());
		vo.setMerchantLabel(dataSourceService.findOptionVOById(CommonConstant.DataSourceName.MERCHANT,
				String.valueOf(temp.getMerchantId())).getText());
		vo.setProductLabel(dataSourceService.findOptionVOById(CommonConstant.DataSourceName.PRODUCT,
				String.valueOf(temp.getProductId())).getText());
		return vo;
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Override
	public ProductBatch updateProductBatchStatus(ProductBatch batch)
	{
		try
		{
			logger.debug("[updateProductBatchStatus start : ProductBatch[" + batch.toString() + "]");
			ProductBatch productBatch = productBatchDao.findOne(batch.getId());
			if (productBatch != null)
			{
				productBatch.setStatus(batch.getStatus());
				productBatch = productBatchDao.save(productBatch);
			}
			else
			{
				// 面值不存在
				logger.error("[updateProductBatchStatus failed : ProductBatch[" + batch.toString() + "]");
				throw new RpcException(0);
			}
			logger.debug("[updateProductBatchStatus end : ProductBatch[" + batch.toString() + "]");
			return productBatch;
		}
		catch (RpcException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			logger.error("[updateProductBatchStatus failed : ProductBatch[" + batch.toString() + "]");
			throw new RpcException(ErrorCodeConst.ErrorCode99998);
		}
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Override
	public String deleteProductBatch(ProductBatch batch)
	{
		try
		{
			logger.debug("[deleteProductBatch start : ProductBatch[" + batch.toString() + "]");
			ProductBatch productBatch = productBatchDao.findOne(batch.getId());
			if (productBatch != null)
			{
				productBatchDao.delete(productBatch);
			}
			else
			{
				// 准备删除批次不存在！
				logger.error("[deleteProductBatch failed : ProductBatch[" + batch.toString() + "]");
				throw new RpcException(ErrorCodeConst.ErrorCode10601);
			}
			logger.debug("[deleteProductBatch failed : ProductBatch[" + batch.toString() + "]");
			return Constant.Common.SUCCESS;
		}
		catch (Exception e)
		{
			logger.error("[产品分类状态修改开始][ProductServiceImpl :deleteProductBatch failed]");
			throw new RpcException(ErrorCodeConst.ErrorCode99998);
		}
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Override
	public ProductBatch saveProductBatch(ProductBatch batch)
	{
		try
		{
			logger.debug("[saveProductBatch start : ProductBatch[" + batch.toString() + "]");
			ProductBatch productBatch = productBatchDao.queryProductBatchByOther(batch.getBatchId(),
					batch.getMerchantId(), batch.getProductId());
			// 唯一性
			if (BeanUtils.isNotNull(productBatch))
			{
				logger.error("[saveProductBatch failed : ProductBatch[" + batch.toString() + "]");
				throw new RpcException(ErrorCodeConst.ErrorCode10602);
			}
			else
			{
				batch = productBatchDao.save(batch);
			}

			logger.debug("[saveProductBatch end : ProductBatch[" + batch.toString() + "]");
			return batch;
		}
		catch (RpcException e)
		{
			throw ExceptionUtil.throwException(e);
		}
		catch (Exception e)
		{
			logger.error("[saveProductBatch failed : ProductBatch[" + batch.toString() + "]");
			logger.error(e.getLocalizedMessage(), e);
			throw new RpcException(ErrorCodeConst.ErrorCode99998);
		}
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Override
	public ProductBatch queryProductBatch(long id)
	{
		logger.debug("[queryProductBatch start : id[" + id + "]");
		try
		{
			ProductBatch batch = productBatchDao.findOne(id);
			logger.debug("[queryProductBatch end : id[" + id + "]");
			return batch;
		}
		catch (Exception e)
		{
			logger.error("[queryProductBatch failed : id[" + id + "]");
			logger.error(e.getLocalizedMessage(), e);
			throw new RpcException(ErrorCodeConst.ErrorCode99998);
		}
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Override
	public void updateBatchStatus(int tag, String batchId)
	{
		logger.debug("[updateBatchStatus start : tag[" + tag + "] batchId[" + batchId + "]");
		try
		{
			// xiu gai pici zhuangtai
			List<ProductBatch> pbs = productBatchDao.findBatchsByBatchId(batchId);
			for (ProductBatch productBatch : pbs)
			{
				if (productBatch.getStatus() != tag)
				{
					productBatch.setStatus(tag);
					productBatchDao.save(productBatch);
				}
			}

			// xiu gai ka mi biao zhuang tai
			int depotStatus = CommonConstant.CardStatus.NOT_SALES;

			if (tag == CommonConstant.CommonStatus.OPEN)
			{
				depotStatus = CommonConstant.CardStatus.SALES;
			}
			else
			{
				depotStatus = CommonConstant.CardStatus.NOT_SALES;
			}

			List<ProductDepot> pds = productDepotDao.findProductDepotByBatchId(batchId);
			for (ProductDepot productDepot : pds)
			{
				if (productDepot.getStatus() != CommonConstant.CardStatus.SOLD
						&& productDepot.getStatus() != depotStatus)
				{
					productDepot.setStatus(depotStatus);
					productDepotDao.save(productDepot);
				}
			}
			logger.debug("[updateBatchStatus end : tag[" + tag + "] batchId[" + batchId + "]");
		}
		catch (RpcException e)
		{
			throw ExceptionUtil.throwException(e);
		}
		catch (Exception e)
		{
			logger.error("[updateBatchStatus failed : tag[" + tag + "] batchId[" + batchId + "]");
			logger.error(e.getLocalizedMessage(), e);
			throw new RpcException(ErrorCodeConst.ErrorCode99998);
		}

	}

}
