package com.yiguang.payment.payment.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
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

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.rpc.RpcException;
import com.yiguang.payment.business.product.entity.Product;
import com.yiguang.payment.business.product.service.ProductService;
import com.yiguang.payment.common.CommonConstant;
import com.yiguang.payment.common.Constant;
import com.yiguang.payment.common.datasource.service.DataSourceService;
import com.yiguang.payment.common.exception.ErrorCodeConst;
import com.yiguang.payment.common.query.PageUtil;
import com.yiguang.payment.common.query.YcPage;
import com.yiguang.payment.common.utils.BeanUtils;
import com.yiguang.payment.common.utils.StringUtil;
import com.yiguang.payment.payment.entity.ChannelMerchantRelation;
import com.yiguang.payment.payment.entity.Merchant;
import com.yiguang.payment.payment.repository.MerchantDao;
import com.yiguang.payment.payment.service.ChannelMerchantRelationService;
import com.yiguang.payment.payment.service.MerchantService;
import com.yiguang.payment.payment.vo.MerchantVO;

@Service("merchantService")
@Transactional
public class MerchantServiceImpl implements MerchantService
{

	private static Logger logger = LoggerFactory.getLogger(MerchantServiceImpl.class);

	@Autowired
	private MerchantDao merchantDao;
	@Autowired
	private ProductService productService;
	@Autowired
	private ChannelMerchantRelationService relationService;
	@Autowired
	private DataSourceService dataSourceService;
	
	private Specification<Merchant> getPageQuerySpec(final MerchantVO vo)
	{
		Specification<Merchant> spec = new Specification<Merchant>(){
			@Override
			public Predicate toPredicate(Root<Merchant> root,  
		            CriteriaQuery<?> query, CriteriaBuilder cb) {  
		        
				List<Predicate> predicateList = new ArrayList<Predicate>();
				
				if (vo.getStatus() != -1)
				{
					predicateList.add(cb.equal(root.get("status").as(Integer.class), vo.getStatus()));  
				}
				
				if (StringUtil.isNotEmpty(vo.getName()))
				{
					predicateList.add(cb.equal(root.get("name").as(String.class), vo.getName().trim()));  
				}
				if (vo.getAdminUser() != -1)
				{
					predicateList.add(cb.equal(root.get("adminUser").as(Integer.class), vo.getAdminUser()));  
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
	@Cacheable(value = "merchantCache")
	public YcPage<MerchantVO> queryMerchantList(MerchantVO conditionVO, int pageNumber, int pageSize,
			String sortType)
	{
		logger.debug("queryMerchantList start");
		try
		{
			Specification<Merchant> spec = getPageQuerySpec(conditionVO);
			YcPage<Merchant> ycPage = PageUtil.queryYcPage(merchantDao, spec, pageNumber, pageSize, new Sort(
					Direction.DESC, "id"), Merchant.class);

			YcPage<MerchantVO> result = new YcPage<MerchantVO>();
			result.setPageTotal(ycPage.getPageTotal());
			result.setCountTotal(ycPage.getCountTotal());
			List<Merchant> list = ycPage.getList();
			List<MerchantVO> voList = new ArrayList<MerchantVO>();
			MerchantVO vo = null;
			for (Merchant temp : list)
			{
				vo = copyPropertiesToVO(temp);
				voList.add(vo);
			}
			result.setList(voList);
			logger.debug("queryMerchantList end");
			return result;
		}
		catch (RpcException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			logger.error("queryMerchantList failed");
			logger.error(e.getLocalizedMessage(), e);
			throw new RpcException(ErrorCodeConst.ErrorCode99998);
		}
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Override
	@CacheEvict(value = { "merchantCache", "dataSourceCache" }, allEntries = true, beforeInvocation = true)
	public Merchant updateMerchantStatus(Merchant merchant)
	{
		try
		{
			logger.debug("updateMerchantStatus start, merchant [" + merchant.toString() + "]");
			Merchant new_merchant = merchantDao.findOne(merchant.getId());
			if (new_merchant != null)
			{
				new_merchant.setStatus(merchant.getStatus());
				new_merchant = merchantDao.save(new_merchant);

				long merchantId = new_merchant.getId();
				List<ChannelMerchantRelation> Crelations = relationService
						.queryChannelMerchantRelationByMerchantId(merchantId);

				for (ChannelMerchantRelation relation : Crelations)
				{
					// 关联关系与商户保持一致
					relation.setStatus(new_merchant.getStatus());
					relationService.saveChannelMerchantRelation(relation);
				}
				// 如果是关闭操作，需要级联关闭下级对象 商户计费产品
				if (new_merchant.getStatus() == CommonConstant.CommonStatus.CLOSE)
				{
					List<Product> products = productService.queryProductBySupplierId(new_merchant.getId());

					for (Product product : products)
					{
						if (product.getStatus() != CommonConstant.CommonStatus.CLOSE)
						{
							product.setStatus(CommonConstant.CommonStatus.CLOSE);
							productService.updateProductStatus(product);
						}
					}
				}
			}
			else
			{
				// 不存在
				logger.error("updateMerchantStatus failed, merchant [" + merchant.toString() + "]");
				throw new RpcException(ErrorCodeConst.ErrorCode10307);
			}
			logger.debug("updateMerchantStatus end, merchant [" + merchant.toString() + "]");
			return new_merchant;
		}
		catch (RpcException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			logger.error("updateMerchantStatus failed, merchant [" + merchant.toString() + "]");
			logger.error(e.getLocalizedMessage(), e);
			throw new RpcException(ErrorCodeConst.ErrorCode99998);
		}
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Override
	@CacheEvict(value = { "merchantCache", "dataSourceCache" }, allEntries = true, beforeInvocation = true)
	public String deleteMerchant(Merchant merchant)
	{
		try
		{
			logger.debug("deleteMerchant start, merchant [" + merchant.toString() + "]");
			Merchant new_merchant = merchantDao.findOne(merchant.getId());
			if (new_merchant != null)
			{
				merchantDao.delete(new_merchant);
			}
			else
			{
				// 准备删除的产品不存在！
				logger.error("deleteMerchant failed, merchant [" + merchant.toString() + "]");
				throw new RpcException(ErrorCodeConst.ErrorCode10307);
			}
			logger.debug("deleteMerchant end, merchant [" + merchant.toString() + "]");
			return Constant.Common.SUCCESS;
		}
		catch (RpcException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			logger.error("deleteMerchant failed, merchant [" + merchant.toString() + "]");
			logger.error(e.getLocalizedMessage(), e);
			throw new RpcException(ErrorCodeConst.ErrorCode99998);
		}
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Override
	@CacheEvict(value = { "merchantCache", "dataSourceCache" }, allEntries = true, beforeInvocation = true)
	public Merchant saveMerchant(Merchant merchant)
	{
		try
		{
			logger.debug("saveMerchant start, merchant [" + merchant.toString() + "]");
			Merchant new_merchant = merchantDao.queryMerchantByName(merchant.getName());
			// 唯一性
			if (BeanUtils.isNotNull(new_merchant) && merchant.getId() == 0)
			{

				logger.error("saveMerchant failed, merchant [" + merchant.toString() + "]");
				throw new RpcException(ErrorCodeConst.ErrorCode10310);
			}
			else
			{
				merchant = merchantDao.save(merchant);
			}

			logger.debug("saveMerchant end, merchant [" + merchant.toString() + "]");
			return merchant;
		}
		catch (RpcException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			logger.error("saveMerchant failed, merchant [" + merchant.toString() + "]");
			logger.error(e.getLocalizedMessage(), e);
			throw new RpcException(ErrorCodeConst.ErrorCode99998);
		}
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Override
	@Cacheable(value = "merchantCache", key = "#root.methodName+#id")
	public Merchant queryMerchant(long id)
	{
		logger.debug("queryMerchant start, id [" + id + "]");
		try
		{
			Merchant merchant = merchantDao.findOne(id);
			logger.debug("queryMerchant end, id [" + id + "]");
			return merchant;
		}
		catch (Exception e)
		{
			logger.error("queryMerchant failed, id [" + id + "]");
			logger.error(e.getLocalizedMessage(), e);
			throw new RpcException(ErrorCodeConst.ErrorCode99998);
		}
	}

	
	public List<Merchant> findAll() {
		logger.debug("queryMerchant findAll start");
		try
		{
			 Iterable<Merchant> merchants  = merchantDao.findAll();
			 Iterator<Merchant> merchantss =  merchants.iterator();
			 List<Merchant> list = new ArrayList<Merchant>();
			 while(merchantss.hasNext())
			 {
				 list.add(merchantss.next());
			 }
			logger.debug("queryMerchant findAll end");
			return list;
		}
		catch (Exception e)
		{
			logger.error("queryMerchant findAll failed");
			logger.error(e.getLocalizedMessage(), e);
			throw new RpcException(ErrorCodeConst.ErrorCode99998);
		}
	}
	
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Override
	@Cacheable(value = "merchantCache", key = "#root.methodName+#adminUser")
	public Merchant queryMerchantByUserId(long adminUser) {
		logger.debug("queryMerchantByUserId start, adminUser [" + adminUser + "]");
		try
		{
			Merchant merchant = merchantDao.queryMerchantByUserId(adminUser);
			logger.debug("queryMerchantByUserId end, adminUser [" + adminUser + "]");
			return merchant;
		}
		catch (Exception e)
		{
			logger.error("queryMerchantByUserId failed, adminUser [" + adminUser + "]");
			logger.error(e.getLocalizedMessage(), e);
			throw new RpcException(ErrorCodeConst.ErrorCode99998);
		}
	}
	
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Override
	@Cacheable(value = "merchantCache", key = "#root.methodName+#channelId")
	public List<Merchant> queryMerchantByChannelId(long channelId) {
		logger.debug("queryMerchantByChannelId start, channelId [" + channelId + "]");
		try
		{
			List<ChannelMerchantRelation> channelMerchantRelations = relationService.queryChannelMerchantRelationByChannelId(channelId);
			List<Merchant> merchants = new ArrayList<Merchant>();
			for(ChannelMerchantRelation channelMerchantRelation : channelMerchantRelations)
			{
				Merchant merchant = merchantDao.findOne(channelMerchantRelation.getMerchantId());
				merchants.add(merchant);
			}
			logger.debug("queryMerchantByChannelId end, channelId [" + channelId + "]");
			return merchants;
		}
		catch (Exception e)
		{
			logger.error("queryMerchantByChannelId failed, channelId [" + channelId + "]");
			logger.error(e.getLocalizedMessage(), e);
			throw new RpcException(ErrorCodeConst.ErrorCode99998);
		}
	}
	
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Override
	public MerchantVO copyPropertiesToVO(Merchant temp)
	{
		logger.debug("copyPropertiesToVO start");
		try
		{
			MerchantVO vo = new MerchantVO();
			vo.setId(temp.getId());
			vo.setName(temp.getName());
			vo.setRemark(temp.getRemark());
			vo.setStatus(temp.getStatus());
			vo.setKey(temp.getKey());
			vo.setAdminUser(temp.getAdminUser());
			vo.setAdminUserName(dataSourceService.findOptionVOById(CommonConstant.DataSourceName.USER,
					String.valueOf(temp.getAdminUser())).getText());
			vo.setNotifyUrl(temp.getNotifyUrl());
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

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Override
	@CacheEvict(value = { "merchantCache", "dataSourceCache" }, allEntries = true, beforeInvocation = true)
	public Merchant saveMerchant(Merchant merchant, String channelMerchantRelationIDs)
	{
		logger.debug("saveMerchant start, merchant [" + merchant.toString() + "] channelMerchantRelationIDs ["
				+ channelMerchantRelationIDs + "]");
		try
		{
			merchant = saveMerchant(merchant);
			if (StringUtils.isNotEmpty(channelMerchantRelationIDs))
			{
				String[] channelIds = channelMerchantRelationIDs.split("[,]");
				List<ChannelMerchantRelation> list = new ArrayList<ChannelMerchantRelation>();
				ChannelMerchantRelation relation = null;
				for (String channelId : channelIds)
				{
					relation = new ChannelMerchantRelation();
					relation.setChannelId(Long.parseLong(channelId));
					relation.setMerchantId(merchant.getId());
					relation.setStatus(merchant.getStatus());
					list.add(relation);
				}

				List<ChannelMerchantRelation> oldList = relationService
						.queryChannelMerchantRelationByMerchantId(merchant.getId());
				relationService.delete(oldList);
				relationService.save(list);
				logger.debug("saveMerchant end, merchant [" + merchant.toString() + "] channelMerchantRelationIDs ["
						+ channelMerchantRelationIDs + "]");
			}
			return merchant;
		}
		catch (RpcException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			logger.error("saveMerchant failed, merchant [" + merchant.toString() + "] channelMerchantRelationIDs ["
					+ channelMerchantRelationIDs + "]");
			logger.error(e.getLocalizedMessage(), e);
			throw new RpcException(ErrorCodeConst.ErrorCode99998);
		}

	}
}
