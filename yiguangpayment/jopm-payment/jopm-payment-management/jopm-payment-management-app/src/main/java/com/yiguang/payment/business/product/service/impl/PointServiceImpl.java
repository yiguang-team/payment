package com.yiguang.payment.business.product.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.rpc.RpcException;
import com.yiguang.payment.business.product.entity.Point;
import com.yiguang.payment.business.product.entity.PointChannelRelation;
import com.yiguang.payment.business.product.entity.Product;
import com.yiguang.payment.business.product.repository.PointDao;
import com.yiguang.payment.business.product.service.PointChannelRelationService;
import com.yiguang.payment.business.product.service.PointService;
import com.yiguang.payment.business.product.service.ProductService;
import com.yiguang.payment.business.product.vo.PointVO;
import com.yiguang.payment.common.CommonConstant;
import com.yiguang.payment.common.Constant;
import com.yiguang.payment.common.datasource.service.DataSourceService;
import com.yiguang.payment.common.enump.DecimalPlaces;
import com.yiguang.payment.common.exception.ErrorCodeConst;
import com.yiguang.payment.common.query.YcPage;
import com.yiguang.payment.common.utils.BeanUtils;
import com.yiguang.payment.common.utils.BigDecimalUtil;
import com.yiguang.payment.common.utils.StringUtil;
import com.yiguang.payment.payment.entity.Merchant;
import com.yiguang.payment.payment.service.MerchantService;

@Service("pointService")
@Transactional
public class PointServiceImpl implements PointService
{

	private static Logger logger = LoggerFactory.getLogger(PointServiceImpl.class);
	@PersistenceContext
	private EntityManager em;
	@Autowired
	private PointDao pointDao;
	@Autowired
	private ProductService productService;
	@Autowired
	private PointChannelRelationService pointChannelRelationService;
	@Autowired
	private DataSourceService dataSourceService;

	@Autowired
	private MerchantService merchantService;

	@Override
	@Cacheable(value="pointCache")
	public YcPage<PointVO> queryPointList(int pageNumber, int pageSize, String sortType, long channelId, String name,
			long merchantId, long productId, String provinceId, String cityId, int chargingType, int status)
	{
		try
		{
			int startIndex = pageNumber * pageSize - pageSize;
			int endIndex = startIndex + pageSize;
			if (StringUtil.isBlank(sortType) || sortType.equalsIgnoreCase("auto"))
			{
				sortType = "id";
			}
			String insidesql = "select p.*,rownum rn from (select distinct(p.id),p.name,p.merchant_id,p.status,p.product_id,p.province_id,p.city_id,p.remark,p.face_amount,p.pay_amount,p.delivery_amount ,p.charging_type,p.charging_code, r.channel_id from t_product_charging_point p,T_POINT_CHANNEL_RELATION r where r.point_id (+)= p.id order by "
					+ sortType + " desc) p where 1=1  ";
			if (endIndex > 0)
			{
				insidesql = insidesql + " and rownum <= " + endIndex;
			}
			String condition = StringUtil.initString();
			if (StringUtil.isNotBlank(name))
			{
				condition = condition + " and p.name = '" + name + "'";
			}
			if (StringUtil.isNotBlank(provinceId) && !"-1".equals(provinceId))
			{
				condition = condition + " and p.province_id = '" + provinceId + "'";
			}
			if (StringUtil.isNotBlank(cityId) && !"-1".equals(cityId))
			{
				condition = condition + " and p.city_id = '" + cityId + "'";
			}
			if (StringUtil.isNotBlank(String.valueOf(channelId)) && channelId != -1)
			{									
				condition = condition + " and channel_id = " + channelId;
			}
			if (StringUtil.isNotBlank(String.valueOf(merchantId)) && merchantId != -1)
			{
				condition = condition + " and p.merchant_id = " + merchantId;
			}
			if (StringUtil.isNotBlank(String.valueOf(productId)) && productId != -1)
			{
				condition = condition + " and p.product_id = " + productId;
			}
			if (StringUtil.isNotBlank(String.valueOf(chargingType)) && chargingType != -1)
			{
				condition = condition + " and p.charging_type = " + chargingType;
			}
			if (StringUtil.isNotBlank(String.valueOf(status)) && status != -1)
			{
				condition = condition + " and p.status = " + status;
			}
			String pageTotal_sql = "select count(*) from (select distinct(id) from (select distinct(p.id),p.name,p.merchant_id,p.status,p.product_id,p.province_id,p.city_id,p.remark,p.face_amount,p.pay_amount,p.delivery_amount ,p.charging_type,p.charging_code, r.channel_id from t_product_charging_point p,T_POINT_CHANNEL_RELATION r where r.point_id (+)= p.id " + condition+") ) "
					;
			Query query = em.createNativeQuery(pageTotal_sql);
			BigDecimal total = (BigDecimal) query.getSingleResult();

			Double pageTotal = BigDecimalUtil.divide(total, new BigDecimal(pageSize), DecimalPlaces.ZERO.value(),
					BigDecimal.ROUND_CEILING).doubleValue();

			insidesql = insidesql + condition;
			String sql = "select distinct(id),name,merchant_id,status,product_id,province_id,city_id,remark,face_amount,pay_amount,delivery_amount ,charging_type,charging_code from (" + insidesql + ") where rn>" + startIndex;
			logger.debug("SQL:"+sql);
			query = em.createNativeQuery(sql, Point.class);
			@SuppressWarnings("unchecked")
			List<Point> list = query.getResultList();

			YcPage<PointVO> ycPage = new YcPage<PointVO>();
			ycPage.setCountTotal(total.intValue());
			ycPage.setPageTotal(pageTotal.intValue());

			List<PointVO> voList = new ArrayList<PointVO>();
			PointVO vo = null;
			Point p = null;
			for (Point temp : list)
			{
				p = this.queryPoint(temp.getId());
				vo = copyPropertiesToVO(p);
				voList.add(vo);
			}
			ycPage.setList(voList);
			logger.debug("queryPointList end");
			return ycPage;
		}
		catch (Exception e)
		{
			logger.error("queryPointList failed");
			logger.error(e.getLocalizedMessage(), e);
			throw new RpcException(ErrorCodeConst.ErrorCode99998);
		}
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Override
	public PointVO copyPropertiesToVO(Point temp)
	{

		logger.debug("copyPropertiesToVO start");
		try
		{
			PointVO vo = new PointVO();
			vo.setId(temp.getId());
			vo.setName(temp.getName());
			vo.setMerchantId(temp.getMerchantId());
			vo.setProductId(temp.getProductId());
			vo.setChargingCode(temp.getChargingCode());
			vo.setProvinceId(temp.getProvinceId());
			vo.setCityId(temp.getCityId());
			vo.setStatus(temp.getStatus());
			List<PointChannelRelation> relations = pointChannelRelationService.queryPointChannelRelationByPointId(temp
					.getId());
			StringBuffer channelLabel = new StringBuffer("");
			for (PointChannelRelation pcr : relations)
			{
				channelLabel.append(dataSourceService.findOptionVOById(CommonConstant.DataSourceName.COMMON_STATUS,
						String.valueOf(pcr.getChannelId())).getText());
			}
			vo.setStatusLabel(dataSourceService.findOptionVOById(CommonConstant.DataSourceName.COMMON_STATUS,
					String.valueOf(temp.getStatus())).getText());
			vo.setMerchantLabel(dataSourceService.findOptionVOById(CommonConstant.DataSourceName.MERCHANT,
					String.valueOf(temp.getMerchantId())).getText());
			vo.setProductLabel(dataSourceService.findOptionVOById(CommonConstant.DataSourceName.PRODUCT,
					String.valueOf(temp.getProductId())).getText());
			vo.setProvinceLabel(dataSourceService.findOptionVOById(CommonConstant.DataSourceName.PROVINCE,
					String.valueOf(temp.getProvinceId())).getText());
			vo.setCityLabel(dataSourceService.findOptionVOById(CommonConstant.DataSourceName.CITY,
					String.valueOf(temp.getCityId())).getText());
			vo.setChargingType(temp.getChargingType());
			vo.setChargingTypeLabel(dataSourceService.findOptionVOById(CommonConstant.DataSourceName.CHARGING_TYPE,
					String.valueOf(temp.getChargingType())).getText());
			vo.setFaceAmount(BigDecimalUtil.divide(temp.getFaceAmount(), new BigDecimal("100"), 2,
					BigDecimal.ROUND_HALF_UP));
			vo.setPayAmount(BigDecimalUtil.divide(temp.getPayAmount(), new BigDecimal("100"), 2,
					BigDecimal.ROUND_HALF_UP));
			vo.setDeliveryAmount(BigDecimalUtil.divide(temp.getDeliveryAmount(), new BigDecimal("100"), 2,
					BigDecimal.ROUND_HALF_UP));
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
	@CacheEvict(value={"pointCache","dataSourceCache"},allEntries=true,beforeInvocation=true)
	public Point updatePointStatus(Point point)
	{
		logger.debug("updatePointStatus start, point [" + point.toString() + "]");
		try
		{
			Point queryPoint = pointDao.findOne(point.getId());

			if (queryPoint == null)
			{
				// 计费点不存在！
				logger.error("updatePointStatus failed, point [" + point.toString() + "]");
				throw new RpcException(ErrorCodeConst.ErrorCode10402);
			}

			long merchantId = queryPoint.getMerchantId();
			long productId = queryPoint.getProductId();
			Merchant merchant = merchantService.queryMerchant(merchantId);
			if (merchant == null)
			{
				// 供货商不存在！
				logger.error("updatePointStatus failed, point [" + point.toString() + "]");
				throw new RpcException(ErrorCodeConst.ErrorCode10314);
			}
			int carStatus = merchant.getStatus();
			if (carStatus == CommonConstant.CommonStatus.CLOSE)
			{
				// 供货商关闭！
				logger.error("updatePointStatus failed, point [" + point.toString() + "]");
				throw new RpcException(ErrorCodeConst.ErrorCode10313);
			}
			
			Product product = productService.queryProduct(productId);
			if (product == null)
			{
				// 产品不存在！
				logger.error("updatePointStatus failed, point [" + point.toString() + "]");
				throw new RpcException(ErrorCodeConst.ErrorCode10403);
			}
			
			int proStatus = product.getStatus();
			if (proStatus == CommonConstant.CommonStatus.CLOSE)
			{
				// 产品关闭！
				logger.error("updatePointStatus failed, point [" + point.toString() + "]");
				throw new RpcException(ErrorCodeConst.ErrorCode10408);
			}

			queryPoint.setStatus(point.getStatus());
			queryPoint = pointDao.save(queryPoint);

			long pointId = queryPoint.getId();
			List<PointChannelRelation> relations = pointChannelRelationService
					.queryPointChannelRelationByPointId(pointId);
			for (PointChannelRelation relation : relations)
			{
				relation.setStatus(point.getStatus());
				pointChannelRelationService.savePointChannelRelation(relation);
			}

			logger.debug("updatePointStatus end, point [" + point.toString() + "]");
			return queryPoint;
		}
		catch (RpcException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			logger.error("updatePointStatus failed, point [" + point.toString() + "]");
			logger.error(e.getLocalizedMessage(), e);
			throw new RpcException(ErrorCodeConst.ErrorCode99998);
		}
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Override
	@CacheEvict(value={"pointCache","dataSourceCache"},allEntries=true,beforeInvocation=true)
	public String deletePoint(Point point)
	{
		try
		{
			logger.debug("deletePoint start, point [" + point.toString() + "]");
			Point Product = pointDao.findOne(point.getId());
			if (Product != null)
			{
				pointDao.delete(Product);
				long pointId = Product.getId();
				List<PointChannelRelation> list = pointChannelRelationService.queryPointChannelRelationByPointId(pointId);
				if(list != null)
				{
					pointChannelRelationService.delete(list);
				}
			}
			else
			{
				// 准备删除的产品不存在！
				logger.error("deletePoint failed, point [" + point.toString() + "]");
				throw new RpcException(ErrorCodeConst.ErrorCode10402);
			}
			logger.debug("deletePoint end, point [" + point.toString() + "]");
			return Constant.Common.SUCCESS;
		}
		catch (RpcException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			logger.error("deletePoint failed, point [" + point.toString() + "]");
			logger.error(e.getLocalizedMessage(), e);
			throw new RpcException(ErrorCodeConst.ErrorCode99998);
		}
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Override
	@CacheEvict(value={"pointCache","dataSourceCache"},allEntries=true,beforeInvocation=true)
	public Point savePoint(Point point)
	{
		logger.debug("savePoint start, point [" + point.toString() + "]");
		try
		{

			Point queryPoint = pointDao.queryPointByName(point.getName());
			// 唯一性
			if (BeanUtils.isNotNull(queryPoint) && point.getId() == 0)
			{

				logger.error("savePoint failed, point [" + point.toString() + "]");
				throw new RpcException(ErrorCodeConst.ErrorCode10401);
			}
			else
			{
				point = pointDao.save(point);
				// 方便联通传入16位产品编码 生成16位编码用于传参
				long codeId = point.getId() + 10000000;
				long merchantId = point.getMerchantId() + 10000000;
				String codeStr = merchantId + "" + codeId;
				point.setChargingCode(codeStr);
				point = pointDao.save(point);
			}

			queryPoint = pointDao.queryPointByProps(point.getMerchantId(), point.getProductId(), point.getProvinceId(),
					point.getCityId(), point.getFaceAmount(),point.getChargingType());
			// 唯一性
			if (BeanUtils.isNotNull(queryPoint) && point.getId() == 0)
			{

				logger.error("savePoint failed, point [" + point.toString() + "]");
				throw new RpcException(ErrorCodeConst.ErrorCode10401);
			}
			else
			{
				point = pointDao.save(point);
			}

			logger.debug("savePoint end, point [" + point.toString() + "]");
			return point;
		}
		catch (RpcException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			logger.error("savePoint failed, point [" + point.toString() + "]");
			logger.error(e.getLocalizedMessage(), e);
			throw new RpcException(ErrorCodeConst.ErrorCode99998);
		}
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Override
	@CacheEvict(value={"pointCache","dataSourceCache"},allEntries=true,beforeInvocation=true)
	public Point savePoint(Point point, String pointChannelRelationIDs)
	{
		logger.debug("savePoint start, point [" + point.toString() + "] pointChannelRelationIDs ["
				+ pointChannelRelationIDs + "]");
		try
		{
			point = savePoint(point);
			if (StringUtils.isNotEmpty(pointChannelRelationIDs))
			{
				String[] channelIds = pointChannelRelationIDs.split("[,]");
				List<PointChannelRelation> list = new ArrayList<PointChannelRelation>();
				PointChannelRelation pcr = null;
				for (String channelId : channelIds)
				{
					if (StringUtil.isNotBlank(channelId))
					{
						pcr = new PointChannelRelation();
						pcr.setChannelId(Long.parseLong(channelId));
						pcr.setPointId(point.getId());
						pcr.setStatus(point.getStatus());
						list.add(pcr);
					}
				}

				List<PointChannelRelation> oldList = pointChannelRelationService.queryPointChannelRelationByPointId(point
						.getId());
				pointChannelRelationService.delete(oldList);
				pointChannelRelationService.save(list);
				
			}
			logger.debug("savePoint end, point [" + point.toString() + "] pointChannelRelationIDs ["
					+ pointChannelRelationIDs + "]");
			return point;
		}
		catch (RpcException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			logger.error("savePoint failed, point [" + point.toString() + "] pointChannelRelationIDs ["
					+ pointChannelRelationIDs + "]");
			logger.error(e.getLocalizedMessage(), e);
			throw new RpcException(ErrorCodeConst.ErrorCode99998);
		}
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Override
	@Cacheable(value="pointCache",key="#root.methodName+#id")
	public Point queryPoint(long id)
	{
		logger.debug("queryPoint start, id [" + id + "]");
		try
		{
			Point point = pointDao.findOne(id);
			logger.debug("queryPoint end, id [" + id + "]");
			return point;
		}
		catch (Exception e)
		{
			logger.error("queryPoint failed, id [" + id + "]");
			logger.error(e.getLocalizedMessage(), e);
			throw new RpcException(ErrorCodeConst.ErrorCode99998);
		}
	}

	@Override
	@Cacheable(value="pointCache",key="#root.methodName+#productId+#faceAmt")
	public Point queryPointByProductIdAndFaceAmt(long productId, BigDecimal faceAmt)
	{
		logger.debug("queryPointByProductIdAndFaceAmt start, productId [" + productId + "] faceAmt [" + faceAmt + "]");
		try
		{
			Point point = pointDao.queryPointByProductIdAndFaceAmt(productId, faceAmt);
			logger.debug("queryPointByProductIdAndFaceAmt end, productId [" + productId + "] faceAmt [" + faceAmt + "]");
			return point;
		}
		catch (Exception e)
		{
			logger.error("queryPointByProductIdAndFaceAmt failed, productId [" + productId + "] faceAmt [" + faceAmt
					+ "]");
			logger.error(e.getLocalizedMessage(), e);
			throw new RpcException(ErrorCodeConst.ErrorCode99998);
		}
	}
	
	@Override
	@Cacheable(value="pointCache",key="#root.methodName+#productId")
	public List<Point> queryPointByProductId(long productId) {
		logger.debug("queryPointByProductId start, productId [" + productId + " ]");
		try
		{
			List<Point> list = pointDao.queryPointByProductId(productId);
			logger.debug("queryPointByProductId end, productId [" + productId + "]");
			return list;
		}
		catch (Exception e)
		{
			logger.error("queryPointByProductId failed, productId [" + productId + "]");
			logger.error(e.getLocalizedMessage(), e);
			throw new RpcException(ErrorCodeConst.ErrorCode99998);
		}
	}
	
	@Override
	@Cacheable(value="pointCache",key="#root.methodName+#supplierId")
	public List<Point> queryPointBySupplierId(long supplierId) {
		logger.debug("queryPointBySupplierId start, supplierId [" + supplierId + " ]");
		try
		{
			List<Point> list = pointDao.queryPointBySupplierId(supplierId);
			logger.debug("queryPointBySupplierId end, supplierId [" + supplierId + "]");
			return list;
		}
		catch (Exception e)
		{
			logger.error("queryPointBySupplierId failed, supplierId [" + supplierId + "]");
			logger.error(e.getLocalizedMessage(), e);
			throw new RpcException(ErrorCodeConst.ErrorCode99998);
		}
	}

	@Override
	@Cacheable(value="pointCache",key="#root.methodName+#supplierId+#channelId")
	public List<Point> queryPointByMerAndCh(long supplierId, long channelId) {
		logger.debug("queryPointByMerAndCh start, supplierId,channelId [" + supplierId + channelId + " ]");
		try
		{
			List<Point> points = pointDao.queryPointBySupplierId(supplierId);
			List<Point> list = new ArrayList<Point>();
			for(Point point : points)
			{
				List<PointChannelRelation> relations = pointChannelRelationService.queryPointChannelRelationByPointId(point.getId());
				for(PointChannelRelation pointChannelRelation : relations)
				{
					if(pointChannelRelation.getChannelId() == channelId && pointChannelRelation.getStatus() == CommonConstant.CommonStatus.OPEN)
					{
						list.add(point);
						break;
					}
				}
			}
			return list;
		}
		catch (Exception e)
		{
			logger.error("queryPointByMerAndCh failed, supplierId,channelId[" + supplierId + channelId + " ]");
			logger.error(e.getLocalizedMessage(), e);
			throw new RpcException(ErrorCodeConst.ErrorCode99998);
		}
	}
	
	@Override
	@Cacheable(value="pointCache",key="#root.methodName+#chargingCode")
	public Point queryPointByChargingCode(String chargingCode)
	{
		logger.debug("queryPointByChargingCode start, chargingCode [" + chargingCode + " ]");
		try
		{
			Point point = pointDao.queryPointByChargingCode(chargingCode);
			logger.debug("queryPointByChargingCode end, chargingCode [" + chargingCode + "]");
			return point;
		}
		catch (Exception e)
		{
			logger.error("queryPointByChargingCode failed, chargingCode [" + chargingCode + "]");
			logger.error(e.getLocalizedMessage(), e);
			throw new RpcException(ErrorCodeConst.ErrorCode99998);
		}
	}
}
