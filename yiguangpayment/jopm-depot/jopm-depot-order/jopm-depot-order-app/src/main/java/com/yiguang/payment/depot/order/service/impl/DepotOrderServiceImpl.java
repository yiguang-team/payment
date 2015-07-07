package com.yiguang.payment.depot.order.service.impl;

import java.math.BigDecimal;
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
import com.yiguang.payment.business.product.entity.Point;
import com.yiguang.payment.business.product.service.PointService;
import com.yiguang.payment.common.CommonConstant;
import com.yiguang.payment.common.CommonConstant.CommonStatus;
import com.yiguang.payment.common.datasource.service.DataSourceService;
import com.yiguang.payment.common.exception.ErrorCodeConst;
import com.yiguang.payment.common.exception.ExceptionUtil;
import com.yiguang.payment.common.query.PageUtil;
import com.yiguang.payment.common.query.YcPage;
import com.yiguang.payment.common.utils.BigDecimalUtil;
import com.yiguang.payment.common.utils.StringUtil;
import com.yiguang.payment.depot.entity.ProductDepot;
import com.yiguang.payment.depot.order.entity.DepotOrder;
import com.yiguang.payment.depot.order.repository.DepotOrderDAO;
import com.yiguang.payment.depot.order.service.DepotOrderService;
import com.yiguang.payment.depot.order.vo.DepotOrderVO;
import com.yiguang.payment.depot.service.ProductDepotService;
import com.yiguang.payment.depot.vo.CardAndPwdVO;
import com.yiguang.payment.payment.entity.Merchant;
import com.yiguang.payment.payment.service.MerchantService;

/*
 * 提卡接口服务类
 */
@Service("depotOrderService")
@Transactional
public class DepotOrderServiceImpl implements DepotOrderService
{

	private static Logger logger = LoggerFactory.getLogger(DepotOrderServiceImpl.class);

	@Autowired
	private ProductDepotService productDepotService;

	@Autowired
	private DepotOrderDAO pickUpCardRecordDAO;

	@Autowired
	PointService pointService;

	@Autowired
	private MerchantService merchantService;

	@Autowired
	private DataSourceService dataSourceService;

	/*
	 * 实现提卡函数功能
	 */
	@Override
	public synchronized List<CardAndPwdVO> pickUpCard(DepotOrder pickUpCardRecord, int count)
	{

		try
		{
			logger.debug("[提卡开始][ProductDepotRestServiceImpl :pickUpCard()]");
			long pointid = pickUpCardRecord.getChargingPointId();

			List<CardAndPwdVO> resultList = new ArrayList<CardAndPwdVO>();

			// 验证代理商操作合法性
			// 查询产品表,校验产品是否存在并开启
			// 由于系统内部同一个产品的有多种面值，这里对外部称的产品对应为计费点表对象
			Point point = pointService.queryPoint(pointid);
			if (point == null || point.getStatus() != CommonStatus.OPEN)
			{

				throw new RpcException(ErrorCodeConst.CODE_PROD_UNUSABLE);
			}

			// 查询代理商表，校验代理商是否开启
			// 获取代理商
			if (pickUpCardRecord.getExtractType() == 0)
			{
				Merchant merchant = merchantService.queryMerchant(Long.parseLong(pickUpCardRecord.getExtractUser()));

				// 代理商不存在或未开启
				if (merchant == null || merchant.getStatus() == CommonConstant.CommonStatus.CLOSE)
				{

					throw new RpcException(ErrorCodeConst.CODE_MERCHANT_UNUSABLE);

				}
			}
			// 查询代理商产品表,校验是否拥有提取该产品的权限

			List<ProductDepot> pds = productDepotService.queryCardPwdByPointid(pointid, count);
			// 如果返回结果集少于请求数量
			if (pds.size() < count)
			{

				throw new RpcException(ErrorCodeConst.CODE_CARD_NOTENOUGH);
			}
			// 记录提取日志
			pickUpCardRecordDAO.save(pickUpCardRecord);
			logger.debug("[记录提取日志成功][ProductDepotRestServiceImpl :pickUpCard()]");
			// 修改卡密状态并关联日志id
			for (ProductDepot productDepot : pds)
			{

				// 状态修改为已售
				// 1：未销售 2：待销售 3：已销售
				productDepot.setStatus(3);

				// 将日志id更新到卡密表extract_no字段
				productDepot.setExtractNo(String.valueOf(pickUpCardRecord.getId()));

				// 提交修改信息
				productDepotService.saveProductDepot(productDepot);

				// 增加到返回卡密列表
				resultList.add(new CardAndPwdVO(productDepot.getCardId(), productDepot.getCardPwd()));
			}
			logger.debug("[提卡成功][ProductDepotRestServiceImpl :pickUpCard()]");
			// 返回卡密列表
			return resultList;
		}
		catch (RpcException e)
		{

			// 抛出已定义错误码
			logger.debug("[业务校验未通过，提卡失败][ProductDepotRestServiceImpl :pickUpCard()]");
			throw ExceptionUtil.throwException(e);

		}
		catch (Exception e)
		{

			// 抛出未知异常
			logger.error("[提卡异常][ProductDepotRestServiceImpl :pickUpCard(): " + e.getMessage() + "]");
			throw ExceptionUtil.throwException(e);

		}
	}

	/*
	 * 该函数不返回卡密列表，只返回对应的卡密提取记录ID
	 */
	@Override
	public synchronized long pickUpCardWithoutList(DepotOrder pickUpCardRecord)
	{

		try
		{
			logger.debug("[提卡开始][ProductDepotRestServiceImpl :pickUpCardWithoutList()]");
			long pointid = pickUpCardRecord.getChargingPointId();
			long count = pickUpCardRecord.getExtractCount();

			// 验证代理商操作合法性
			// 查询产品表,校验产品是否存在并开启
			// 由于系统内部同一个产品的有多种面值，这里对外部称的产品对应为计费点表对象
			Point point = pointService.queryPoint(pointid);
			if (point == null || point.getStatus() != CommonStatus.OPEN)
			{

				throw new RpcException(ErrorCodeConst.CODE_PROD_UNUSABLE);
			}

			// 查询代理商表，校验代理商是否开启
			// 获取代理商
			if (pickUpCardRecord.getExtractType() == 0)
			{
				Merchant merchant = merchantService.queryMerchant(Long.parseLong(pickUpCardRecord.getExtractUser()));

				// 代理商不存在或未开启
				if (merchant == null || merchant.getStatus() == CommonConstant.CommonStatus.CLOSE)
				{

					throw new RpcException(ErrorCodeConst.CODE_MERCHANT_UNUSABLE);

				}
			}
			// 查询代理商产品表,校验是否拥有提取该产品的权限

			List<ProductDepot> pds = productDepotService.queryCardPwdByPointid(pointid, (int) count);

			// 如果返回结果集少于请求数量
			if (pds.size() < count)
			{
				throw new RpcException(ErrorCodeConst.CODE_CARD_NOTENOUGH);
			}
			// 记录提取日志
			pickUpCardRecordDAO.save(pickUpCardRecord);
			logger.debug("[记录提取日志成功][ProductDepotRestServiceImpl :pickUpCardWithoutList()]");
			// 修改卡密状态并关联日志id
			for (ProductDepot productDepot : pds)
			{

				// 状态修改为已售
				// 1：未销售 2：待销售 3：已销售
				productDepot.setStatus(3);

				// 将日志id更新到卡密表extract_no字段
				productDepot.setExtractNo(String.valueOf(pickUpCardRecord.getId()));

				// 提交修改信息
				productDepotService.saveProductDepot(productDepot);

			}
			logger.debug("[提卡成功][ProductDepotRestServiceImpl :pickUpCardWithoutList()]");
			// 返回提卡记录ID
			return pickUpCardRecord.getId();
		}
		catch (RpcException e)
		{

			// 抛出已定义错误码
			logger.debug("[业务校验未通过，提卡失败][ProductDepotRestServiceImpl :pickUpCardWithoutList()]");
			throw ExceptionUtil.throwException(e);

		}
		catch (Exception e)
		{

			// 抛出未知异常
			logger.error("[提卡异常][ProductDepotRestServiceImpl :pickUpCardWithoutList(): " + e.getMessage() + "]");
			throw ExceptionUtil.throwException(e);

		}
	}

	/*
	 * 通过代理商和订单号查询提过的卡密
	 */
	@Override
	public List<CardAndPwdVO> getHisCardAndPwdByCond(String orderId, String merchantId)
	{
		try
		{
			logger.debug("[getHisCardAndPwdByCond开始][orderId = " + orderId + "merchantId = " + merchantId + "]");

			List<CardAndPwdVO> resultList = new ArrayList<CardAndPwdVO>();

			// 查询历史提卡记录表 看是否有符合条件的记录
			List<DepotOrder> depotOrders = pickUpCardRecordDAO.queryDepotOrderByContion(orderId, merchantId);
			List<ProductDepot> pds = new ArrayList<ProductDepot>();

			// 迭代查询结果到 卡密表查询是否有相应的记录 ,如果有加入的卡密列列表中
			for (DepotOrder depotOrder : depotOrders)
			{
				pds.addAll(productDepotService.queryCardPwdByExtractNo(String.valueOf(depotOrder.getId())));
			}

			logger.debug("[getHisCardAndPwdByCond记录提取日志成功][pds: " + pds + "]");

			// 迭代卡密列表加入到结果集中
			for (ProductDepot productDepot : pds)
			{
				resultList.add(new CardAndPwdVO(productDepot.getCardId(), productDepot.getCardPwd()));
			}
			logger.debug("[getHisCardAndPwdByCond结束][resultList: " + resultList + "]");
			// 返回卡密列表
			return resultList;
		}
		catch (RpcException e)
		{

			// 抛出已定义错误码
			logger.debug("[查询失败][getHisCardAndPwdByCond]");
			throw ExceptionUtil.throwException(e);

		}
		catch (Exception e)
		{

			// 抛出未知异常
			logger.error("[查询异常][getHisCardAndPwdByCond: " + e.getMessage() + "]");
			throw ExceptionUtil.throwException(e);

		}
	}

	private Specification<DepotOrder> getPageQuerySpec(final DepotOrderVO vo)
	{
		Specification<DepotOrder> spec = new Specification<DepotOrder>(){
			@Override
			public Predicate toPredicate(Root<DepotOrder> root,  
		            CriteriaQuery<?> query, CriteriaBuilder cb) {  
		        
				List<Predicate> predicateList = new ArrayList<Predicate>();
				if (StringUtil.isNotEmpty(vo.getExtractUser()))
				{
					predicateList.add(cb.equal(root.get("extractUser").as(String.class), vo.getExtractUser()));  
				}
				
				if (vo.getChargingPointId() != -1)
				{
					predicateList.add(cb.equal(root.get("chargingPointId").as(Integer.class), vo.getChargingPointId()));  
				}
				
				if (StringUtil.isNotEmpty(vo.getOrderId()))
				{
					predicateList.add(cb.equal(root.get("name").as(String.class), vo.getOrderId().trim()));  
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
	
	
	// 查询提卡历史记录
	@Override
	public YcPage<DepotOrderVO> queryPickUpRecordList(DepotOrderVO conditionVO, int pageNumber, int pageSize,
			String sortType)
	{
		Specification<DepotOrder> spec = getPageQuerySpec(conditionVO);

		// 调用查询API获得提卡历史记录集
		YcPage<DepotOrder> ycPage = PageUtil.queryYcPage(pickUpCardRecordDAO, spec, pageNumber, pageSize, new Sort(
				Direction.DESC, "id"), DepotOrderVO.class);

		YcPage<DepotOrderVO> result = new YcPage<DepotOrderVO>();
		result.setPageTotal(ycPage.getPageTotal());
		result.setCountTotal(ycPage.getCountTotal());
		List<DepotOrder> list = ycPage.getList();
		List<DepotOrderVO> voList = new ArrayList<DepotOrderVO>();
		DepotOrderVO vo = null;

		// 迭代查询结果 转换为页面显示对象
		for (DepotOrder temp : list)
		{
			vo = copyRecordEntityToVO(temp);
			voList.add(vo);
		}

		result.setList(voList);

		return result;
	}

	// 数据库对象转换为页面显示对象
	private DepotOrderVO copyRecordEntityToVO(DepotOrder temp)
	{
		DepotOrderVO vo = new DepotOrderVO();

		vo.setChargingPointLabel(dataSourceService.findOptionVOById(CommonConstant.DataSourceName.POINT,
				String.valueOf(temp.getChargingPointId())).getText());
		vo.setExtractCount(temp.getExtractCount());
		vo.setExtractType(dataSourceService.findOptionVOById(CommonConstant.DataSourceName.PICKUP_TYPE,
				String.valueOf(temp.getExtractType())).getText());
		String extractUser = temp.getExtractUser();
		if (temp.getExtractType() == 0)
		{
			extractUser = dataSourceService.findOptionVOById(CommonConstant.DataSourceName.MERCHANT,
					temp.getExtractUser()).getText();
		}
		vo.setExtractUser(extractUser);
		vo.setOrderId(temp.getOrderId());

		BigDecimal payAmount = BigDecimalUtil.divide(new BigDecimal(temp.getPayAmount()), new BigDecimal("100"), 2,
				BigDecimal.ROUND_HALF_UP);

		vo.setPayAmount(payAmount);
		vo.setRequestIp(temp.getRequestIp());
		vo.setRequestTime(temp.getRequestTime());

		return vo;
	}
}
