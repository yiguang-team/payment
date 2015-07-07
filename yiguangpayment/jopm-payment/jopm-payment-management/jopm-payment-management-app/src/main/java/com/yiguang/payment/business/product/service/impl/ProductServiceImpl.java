package com.yiguang.payment.business.product.service.impl;

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
import com.yiguang.payment.business.product.entity.Point;
import com.yiguang.payment.business.product.entity.Product;
import com.yiguang.payment.business.product.repository.ProductDao;
import com.yiguang.payment.business.product.service.PointService;
import com.yiguang.payment.business.product.service.ProductService;
import com.yiguang.payment.business.product.vo.ProductVO;
import com.yiguang.payment.common.CommonConstant;
import com.yiguang.payment.common.Constant;
import com.yiguang.payment.common.datasource.service.DataSourceService;
import com.yiguang.payment.common.exception.ErrorCodeConst;
import com.yiguang.payment.common.query.PageUtil;
import com.yiguang.payment.common.query.YcPage;
import com.yiguang.payment.common.utils.BeanUtils;
import com.yiguang.payment.common.utils.StringUtil;
import com.yiguang.payment.payment.entity.Merchant;
import com.yiguang.payment.payment.service.MerchantService;

@Service("productService")
@Transactional
public class ProductServiceImpl implements ProductService
{

	private static Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

	@Autowired
	private ProductDao productDao;

	@Autowired
	private PointService pointService;

	@Autowired
	private MerchantService merchantService;

	@Autowired
	private DataSourceService dataSourceService;

	private Specification<Product> getPageQuerySpec(final ProductVO vo)
	{
		Specification<Product> spec = new Specification<Product>(){
			@Override
			public Predicate toPredicate(Root<Product> root,  
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
				
				if (StringUtil.isNotEmpty(vo.getName()))
				{
					predicateList.add(cb.equal(root.get("name").as(String.class), vo.getName().trim()));  
				}
				
				if (vo.getMerchantId() != -1)
				{
					predicateList.add(cb.equal(root.get("merchantId").as(Integer.class), vo.getMerchantId()));  
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
	@Cacheable(value = "productCache")
	public YcPage<ProductVO> queryProductList(ProductVO conditionVO, int pageNumber, int pageSize,
			String sortType)
	{
		logger.debug("queryProductList start");
		try
		{
			Specification<Product> spec = getPageQuerySpec(conditionVO);
			
			YcPage<Product> ycPage = PageUtil.queryYcPage(productDao, spec, pageNumber, pageSize, new Sort(
					Direction.DESC, "id"), Product.class);

			YcPage<ProductVO> result = new YcPage<ProductVO>();
			result.setPageTotal(ycPage.getPageTotal());
			result.setCountTotal(ycPage.getCountTotal());
			List<Product> list = ycPage.getList();
			List<ProductVO> voList = new ArrayList<ProductVO>();
			ProductVO vo = null;
			for (Product temp : list)
			{
				vo = copyPropertiesToVO(temp);
				voList.add(vo);
			}

			result.setList(voList);
			logger.debug("queryProductList end");
			return result;
		}
		catch (RpcException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			logger.error("queryProductList failed");
			logger.error(e.getLocalizedMessage(), e);
			throw new RpcException(ErrorCodeConst.ErrorCode99998);
		}
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Override
	public ProductVO copyPropertiesToVO(Product temp)
	{
		logger.debug("copyPropertiesToVO start");
		try
		{
			ProductVO vo = new ProductVO();
			vo.setId(temp.getId());
			vo.setName(temp.getName());
			vo.setRemark(temp.getRemark());
			vo.setStatus(temp.getStatus());
			vo.setType(temp.getType());

			vo.setStatusLabel(dataSourceService.findOptionVOById(CommonConstant.DataSourceName.COMMON_STATUS,
					String.valueOf(temp.getStatus())).getText());
			vo.setTypeLabel(dataSourceService.findOptionVOById(CommonConstant.DataSourceName.PRODUCT_TYPE,
					String.valueOf(temp.getType())).getText());
			vo.setMerchantId(temp.getMerchantId());
			vo.setMerchantLabel(dataSourceService.findOptionVOById(CommonConstant.DataSourceName.MERCHANT,
					String.valueOf(temp.getMerchantId())).getText());
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
	@CacheEvict(value = { "productCache", "dataSourceCache" }, allEntries = true, beforeInvocation = true)
	public String deleteProduct(Product product)
	{
		logger.debug("deleteProduct start, product [" + product.toString() + "]");
		try
		{
			Product queryProduct = productDao.findOne(product.getId());
			if (queryProduct != null)
			{
				productDao.delete(queryProduct);
			}
			else
			{
				// 准备删除的产品不存在！
				logger.error("deleteProduct failed, product [" + product.toString() + "]");
				throw new RpcException(19409);
			}
			logger.debug("deleteProduct end, product [" + product.toString() + "]");
			return Constant.Common.SUCCESS;
		}
		catch (RpcException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			logger.error("deleteProduct failed, product [" + product.toString() + "]");
			logger.error(e.getLocalizedMessage(), e);
			throw new RpcException(ErrorCodeConst.ErrorCode99998);
		}
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Override
	@CacheEvict(value = { "productCache", "dataSourceCache" }, allEntries = true, beforeInvocation = true)
	public Product saveProduct(Product product)
	{
		logger.debug("saveProduct start, product [" + product.toString() + "]");
		try
		{

			Product queryProduct = productDao.queryProductByName(product.getName());
			// 唯一性
			if (BeanUtils.isNotNull(queryProduct) && product.getId() == 0)
			{

				logger.error("updateProductStatus failed, product [" + product.toString() + "]");
				throw new RpcException(ErrorCodeConst.ErrorCode10404);
			}
			else
			{
				product = productDao.save(product);
			}

			logger.debug("saveProduct end, product [" + product.toString() + "]");
			return product;
		}
		catch (RpcException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			logger.error("saveProduct failed, product [" + product.toString() + "]");
			logger.error(e.getLocalizedMessage(), e);
			throw new RpcException(ErrorCodeConst.ErrorCode99998);
		}
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Override
	@Cacheable(value = "productCache", key = "#root.methodName+#id")
	public Product queryProduct(long id)
	{
		logger.debug("queryProduct start, id [" + id + "]");
		try
		{
			Product product = productDao.findOne(id);
			logger.debug("queryProduct end, id [" + id + "]");
			return product;
		}
		catch (Exception e)
		{
			logger.error("queryProduct failed, id [" + id + "]");
			logger.error(e.getLocalizedMessage(), e);
			throw new RpcException(ErrorCodeConst.ErrorCode99998);
		}

	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Override
	@CacheEvict(value = { "productCache", "dataSourceCache" }, allEntries = true, beforeInvocation = true)
	public Product updateProductStatus(Product product)
	{
		try
		{
			logger.debug("updateProductStatus start, product [" + product.toString() + "]");
			Product queryProduct = productDao.findOne(product.getId());
			if (queryProduct == null)
			{
				// 产品不存在
				logger.error("updateProductStatus failed, product [" + product.toString() + "]");
				throw new RpcException(ErrorCodeConst.ErrorCode10403);
			}
			Merchant merchant = merchantService.queryMerchant(queryProduct.getMerchantId());
			if (merchant == null)
			{
				// 商户不存在！
				logger.error("updateProductStatus failed, product [" + product.toString() + "]");
				throw new RpcException(ErrorCodeConst.ErrorCode10314);
			}
			int carStatus = merchant.getStatus();
			if (carStatus == CommonConstant.CommonStatus.CLOSE)
			{
				// 商户关闭！
				logger.error("updateProductStatus failed, product [" + product.toString() + "]");
				throw new RpcException(ErrorCodeConst.ErrorCode10313);
			}

			queryProduct.setStatus(product.getStatus());
			queryProduct = productDao.save(queryProduct);
			long productId = queryProduct.getId();
			// 关联 当产品状态关闭计费点也关闭
			if (queryProduct.getStatus() == CommonConstant.CommonStatus.CLOSE)
			{
				List<Point> pointList = pointService.queryPointByProductId(productId);
				for (Point point : pointList)
				{
					if (point.getStatus() != CommonConstant.CommonStatus.CLOSE)
					{
						point.setStatus(CommonConstant.CommonStatus.CLOSE);
						pointService.updatePointStatus(point);
					}
				}
			}

			logger.debug("updateProductStatus end, product [" + product.toString() + "]");
			return queryProduct;
		}
		catch (RpcException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			logger.error("updateProductStatus failed, product [" + product.toString() + "]");
			logger.error(e.getLocalizedMessage(), e);
			throw new RpcException(ErrorCodeConst.ErrorCode99998);
		}
	}

	@Override
	@Cacheable(value = "productCache", key = "#root.methodName+#supplierId+#productId")
	public Product queryProductBySupplierIdAndId(long supplierId, long productId)
	{
		logger.debug("queryProductBySupplierIdAndId start, supplierId [" + supplierId + "] productId [" + productId
				+ "]");
		try
		{
			Product product = productDao.queryProductBySupplierIdAndId(supplierId, productId);
			logger.debug("queryProductBySupplierIdAndId end, supplierId [" + supplierId + "] productId [" + productId
					+ "]");
			return product;
		}
		catch (Exception e)
		{
			logger.error("queryProductBySupplierIdAndId failed, supplierId [" + supplierId + "] productId ["
					+ productId + "]");
			logger.error(e.getLocalizedMessage(), e);
			throw new RpcException(ErrorCodeConst.ErrorCode99998);
		}
	}

	@Override
	@Cacheable(value = "productCache", key = "#root.methodName+#supplierId")
	public List<Product> queryProductBySupplierId(long supplierId)
	{
		logger.debug("queryProductBySupplierId start, supplierId [" + supplierId + "]");
		try
		{
			List<Product> product = productDao.queryProductBySupplierId(supplierId);
			logger.debug("queryProductBySupplierId end, supplierId [" + supplierId + "]");
			return product;
		}
		catch (Exception e)
		{
			logger.error("queryProductBySupplierId failed, supplierId [" + supplierId + "]");
			logger.error(e.getLocalizedMessage(), e);
			throw new RpcException(ErrorCodeConst.ErrorCode99998);
		}
	}
}
