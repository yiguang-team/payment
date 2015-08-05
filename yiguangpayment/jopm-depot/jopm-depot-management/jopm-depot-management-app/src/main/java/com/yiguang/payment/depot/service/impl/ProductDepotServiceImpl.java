package com.yiguang.payment.depot.service.impl;

import java.math.BigDecimal;
import java.security.interfaces.RSAPrivateKey;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
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
import com.yiguang.payment.business.product.entity.Point;
import com.yiguang.payment.business.product.entity.Product;
import com.yiguang.payment.business.product.service.PointService;
import com.yiguang.payment.business.product.service.ProductService;
import com.yiguang.payment.common.CommonConstant;
import com.yiguang.payment.common.Constant;
import com.yiguang.payment.common.JsonTool;
import com.yiguang.payment.common.datasource.service.DataSourceService;
import com.yiguang.payment.common.datasource.vo.OptionVO;
import com.yiguang.payment.common.exception.ErrorCodeConst;
import com.yiguang.payment.common.exception.ExceptionUtil;
import com.yiguang.payment.common.message.MessageResolver;
import com.yiguang.payment.common.numsection.service.CarrierInfoService;
import com.yiguang.payment.common.query.PageUtil;
import com.yiguang.payment.common.query.YcPage;
import com.yiguang.payment.common.security.RSAUtils;
import com.yiguang.payment.common.security.service.SecurityKeystoreService;
import com.yiguang.payment.common.utils.BeanUtils;
import com.yiguang.payment.common.utils.BigDecimalUtil;
import com.yiguang.payment.common.utils.StringUtil;
import com.yiguang.payment.depot.entity.ProductBatch;
import com.yiguang.payment.depot.entity.ProductDepot;
import com.yiguang.payment.depot.repository.ProductDepotDao;
import com.yiguang.payment.depot.service.ProductBatchService;
import com.yiguang.payment.depot.service.ProductDepotService;
import com.yiguang.payment.depot.vo.ProductDepotVO;

@Service("productDepotService")
@Transactional
public class ProductDepotServiceImpl implements ProductDepotService
{

	private static Logger logger = LoggerFactory.getLogger(ProductDepotServiceImpl.class);

	@Autowired
	private ProductDepotDao productDepotDao;

	@Autowired
	PointService pointService;

	@Autowired
	ProductBatchService productBatchService;

	@Autowired
	ProductService productService;

	@Autowired
	CarrierInfoService carrierInfoService;

	@Autowired
	private DataSourceService dataSourceService;

	@Autowired
	private SecurityKeystoreService keystoreService;

	/**
	 * 解析页面配置的列与字段映射，便于excel列与card对象属性对应
	 * 
	 * @param config
	 * @return
	 */
	private Map<String, Integer> parseConfig(String config)
	{
		Map<String, Integer> map = new HashMap<String, Integer>();
		List<Map<String, Object>> list = JsonTool.jsonToList(config);

		for (Map<String, Object> temp : list)
		{
			String key = (String) temp.get("key");
			String value = (String) temp.get("value");
			map.put(value, Integer.parseInt(key));
		}

		return map;
	}

	// 卡密入库
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Override
	public synchronized void importProductDepot(String batchId, String config, long merchantId, long productId,
			String totalAmt, long totalNum, String[][] excelData)
	{
		logger.debug("[卡密入库开始][ProductDepotServiceImpl :importProductDepot()]");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String[][] str = excelData;
		Map<String, Integer> configMap = null;
		List<ProductDepot> depotList = new ArrayList<ProductDepot>();

		try
		{

			logger.debug("[卡密入库校验开始][ProductDepotServiceImpl :importProductDepot()]");
			// 运营商存在性校验
			OptionVO cVO = dataSourceService.findOptionVOById(CommonConstant.DataSourceName.MERCHANT,
					String.valueOf(merchantId));

			if (cVO == null)
			{
				logger.error("[运营商存在性校验][ProductDepotServiceImpl :importProductDepot failed]");
				throw new RpcException(ErrorCodeConst.CODE_CARRIER_NOT_EXISTS);
			}

			// 产品存在性校验
			OptionVO pVO = dataSourceService.findOptionVOById(CommonConstant.DataSourceName.PRODUCT,
					String.valueOf(productId));

			if (pVO == null)
			{
				logger.error("[产品存在性校验][ProductDepotServiceImpl :importProductDepot failed]");
				throw new RpcException(ErrorCodeConst.CODE_PRODUCT_NOT_EXISTS);
			}

			// 产品 运营商 业务类型关联校验
			Product p = productService.queryProductBySupplierIdAndId(merchantId, productId);
			if (p == null)
			{
				logger.error("[产品 运营商 业务类型关联校验][ProductDepotServiceImpl :importProductDepot failed]");
				throw new RpcException(ErrorCodeConst.CODE_PRODUCT_CARRIER_NOT_EXISTS);
			}

			// 解析页面配置的列与字段映射
			configMap = parseConfig(config);
			String cardId = null;
			String cardPwd = null;
			String faceAmountStr = null;
			BigDecimal faceAmount = null;
			Date usefulStartDate = null;
			Date usefulEndDate = null;
			Date stockInDate = null;

			String remark = null;
			BigDecimal amt = new BigDecimal("0");

			for (String[] excelCard : str)
			{
				faceAmountStr = configMap.get(CommonConstant.CardColumns.FACE_AMOUNT) == null ? null
						: excelCard[configMap.get(CommonConstant.CardColumns.FACE_AMOUNT)];

				// 面值格式校验
				try
				{
					faceAmount = new BigDecimal(faceAmountStr);
				}
				catch (Exception ex)
				{
					logger.error("[面值格式校验][ProductDepotServiceImpl :importProductDepot failed]");
					throw new RpcException(ErrorCodeConst.CODE_FACE_AMOUNT_FORMATE_ERR);

				}
				// 面值计费点存在性校验
				BigDecimal faceAmountPecent = BigDecimalUtil.multiply(faceAmount, new BigDecimal("100"), 0,
						BigDecimal.ROUND_HALF_UP);
				Point point = pointService.queryPointByProductIdAndFaceAmt(productId, faceAmountPecent);
				if (point == null || point.getStatus() == CommonConstant.CommonStatus.CLOSE)
				{
					logger.error("[面值计费点存在性校验][ProductDepotServiceImpl :importProductDepot failed]");
					throw new RpcException(ErrorCodeConst.CODE_FACE_AMOUNT_POINT_NOT_EXISTS);
				}

				// 充值类型校验 1：直冲，2：卡密
				if (point.getChargingType() != CommonConstant.CHARGING_TYPE.CARD)
				{
					logger.error("[充值类型校验][ProductDepotServiceImpl :importProductDepot failed]");
					throw new RpcException(ErrorCodeConst.CODE_POINT_CHARGING_TYPE_ERR);
				}

				amt = amt.add(faceAmount);
				cardId = configMap.get(CommonConstant.CardColumns.CARD_ID) == null ? null : excelCard[configMap
						.get(CommonConstant.CardColumns.CARD_ID)];
				// 卡号为空不能导入
				if (StringUtils.isEmpty(cardId))
				{
					logger.error("[卡号为空不能导入][ProductDepotServiceImpl :importProductDepot failed]");
					throw new RpcException(ErrorCodeConst.CODE_CARD_NO_FORMATE_ERR);
				}

				// 卡号存在性校验
				ProductDepot productDepot = productDepotDao.queryProductDepotByCardId(cardId, productId);
				if (productDepot != null)
				{
					logger.error("[卡号存在性校验][ProductDepotServiceImpl :importProductDepot failed]");
					throw new RpcException(ErrorCodeConst.CODE_CARD_NO_EXISTS, MessageResolver.getMessage(
							String.valueOf(ErrorCodeConst.CODE_CARD_NO_EXISTS), new String[] { cardId }));
				}

				// 密码为空
				cardPwd = configMap.get(CommonConstant.CardColumns.CARD_PWD) == null ? null : excelCard[configMap
						.get(CommonConstant.CardColumns.CARD_PWD)];
				if (StringUtils.isEmpty(cardPwd))
				{
					logger.error("[密码为空][ProductDepotServiceImpl :importProductDepot failed]");
					throw new RpcException(ErrorCodeConst.CODE_PASSWORD_FORMATE_ERR);
				}

				// 批次ID为空
				if (StringUtils.isEmpty(batchId))
				{
					logger.error("[批次ID为空][ProductDepotServiceImpl :importProductDepot failed]");
					throw new RpcException(ErrorCodeConst.CODE_BATCHID_FORMATE_ERR);
				}

				// 设置卡密开始时间
				if (configMap.get(CommonConstant.CardColumns.USEFUL_START_DATE) == null)
				{
					usefulStartDate = null;
				}
				else
				{
					String startDate = excelCard[configMap.get(CommonConstant.CardColumns.USEFUL_START_DATE)];
					usefulStartDate = sdf.parse(startDate);
				}

				// 设置卡密结束时间
				if (configMap.get(CommonConstant.CardColumns.USEFUL_END_DATE) == null)
				{
					usefulEndDate = null;
				}
				else
				{
					String endDate = excelCard[configMap.get(CommonConstant.CardColumns.USEFUL_END_DATE)];
					usefulEndDate = sdf.parse(endDate);
				}

				// 入库时间为当前时间
				stockInDate = new Date();

				remark = configMap.get(CommonConstant.CardColumns.REMARK) == null ? null : excelCard[configMap
						.get(CommonConstant.CardColumns.REMARK)];

				ProductDepot pd = new ProductDepot();

				pd.setProductId(productId);
				pd.setPoint(point);
				pd.setCardId(cardId);
				// 获取私钥对象
				Map<String, Object> keyMap = keystoreService.getKeyObjectToMap(Constant.SecurityCredential.RSA_KEY);
				RSAPrivateKey privateKey = (RSAPrivateKey) RSAUtils.getPrivateKeyByString((String) keyMap
						.get(Constant.RSACacheKey.RSA_PRIVATEKEY));
				// 将RSA加密后的密码存入数据库
				pd.setCardPwd(RSAUtils.encryptByPrivateKey(cardPwd, privateKey));
				pd.setStatus(CommonConstant.CardStatus.NOT_SALES);
				pd.setRemark(remark);
				pd.setUsefulEndDate(usefulEndDate);
				pd.setUsefulStartDate(usefulStartDate);
				pd.setBatchId(batchId);
				pd.setStockInDate(stockInDate);

				depotList.add(pd);

			}

			BigDecimal totalAmtB = new BigDecimal(totalAmt);

			// 面值总额不等
			if (totalAmtB.compareTo(amt) != 0)
			{
				logger.error("[面值总额不等][ProductDepotServiceImpl :importProductDepot failed]");
				throw new RpcException(91020);
			}

			// 导入卡密总量不等
			if (totalNum != depotList.size())
			{
				logger.error("[导入卡密总量不等][ProductDepotServiceImpl :importProductDepot failed]");
				throw new RpcException(91021);
			}
			logger.debug("[卡密入库校验通过][ProductDepotServiceImpl :importProductDepot()]");
			ProductBatch batch = new ProductBatch();
			batch.setMerchantId(merchantId);
			batch.setProductId(productId);
			batch.setBatchId(batchId);
			batch.setStatus(CommonConstant.CommonStatus.CLOSE);
			batch.setTotalAmt(totalAmtB);
			batch.setTotalCount(totalNum);
			batch = productBatchService.saveProductBatch(batch);
			productDepotDao.save(depotList);
			logger.debug("[卡密入库结束][ProductDepotServiceImpl :importProductDepot()]");
		}
		catch (RpcException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			logger.error("[卡密入库异常][ProductDepotServiceImpl :importProductDepot()][" + e.getMessage() + "]");
			throw new RpcException(ErrorCodeConst.CODE_IMPORT_CARD_ERR);
		}
	}

	private Specification<ProductDepot> getPageQuerySpec(final ProductDepotVO vo)
	{
		Specification<ProductDepot> spec = new Specification<ProductDepot>(){
			@Override
			public Predicate toPredicate(Root<ProductDepot> root,  
		            CriteriaQuery<?> query, CriteriaBuilder cb) {  
		        
				List<Predicate> predicateList = new ArrayList<Predicate>();
				if (vo.getPointVO().getMerchantId() != -1)
				{
					predicateList.add(cb.equal(root.get("point").get("merchantId").as(Integer.class), vo.getPointVO().getMerchantId()));  
				}
				if (vo.getPointVO().getProductId() != -1)
				{
					predicateList.add(cb.equal(root.get("point").get("productId").as(Integer.class), vo.getPointVO().getProductId()));  
				}
				if (StringUtil.isNotEmpty(vo.getPointVO().getProvinceId()))
				{							
					predicateList.add(cb.equal(root.get("point").get("provinceId").as(String.class), vo.getPointVO().getProvinceId()));  
				}		
				if (StringUtil.isNotEmpty(vo.getPointVO().getCityId()))
				{
					predicateList.add(cb.equal(root.get("point").get("cityId").as(String.class), vo.getPointVO().getCityId()));  
				}
				if (vo.getPointVO().getChargingType() != -1)
				{
					predicateList.add(cb.equal(root.get("point").get("chargingType").as(Integer.class), vo.getPointVO().getChargingType()));  
				}
				if (vo.getPointVO().getFaceAmount() != null)
				{
					predicateList.add(cb.equal(root.get("point").get("faceAmount").as(String.class), String.valueOf(vo.getPointVO().getFaceAmount())));  
				}
				if (vo.getStatus() != -1)
				{
					predicateList.add(cb.equal(root.get("status").as(Integer.class), vo.getStatus()));  
				}
				
				if (StringUtil.isNotEmpty(vo.getBatchId()))
				{
					predicateList.add(cb.equal(root.get("batchId").as(String.class), vo.getBatchId().trim()));  
				}
				
				if (StringUtil.isNotEmpty(vo.getCardId()))
				{
					predicateList.add(cb.equal(root.get("cardId").as(String.class), vo.getCardId()));  
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
	public YcPage<ProductDepotVO> queryProductDepotList(ProductDepotVO conditionVO, int pageNumber, int pageSize,
			String sortType)
	{
		Specification<ProductDepot> spec = getPageQuerySpec(conditionVO);
		YcPage<ProductDepot> ycPage = PageUtil.queryYcPage(productDepotDao, spec, pageNumber, pageSize, new Sort(
				Direction.DESC, "id"), ProductDepot.class);

		YcPage<ProductDepotVO> result = new YcPage<ProductDepotVO>();
		result.setPageTotal(ycPage.getPageTotal());
		result.setCountTotal(ycPage.getCountTotal());
		List<ProductDepot> list = ycPage.getList();
		List<ProductDepotVO> voList = new ArrayList<ProductDepotVO>();
		ProductDepotVO vo = null;
		for (ProductDepot temp : list)
		{
			vo = copyPropertiesToVO(temp);
			voList.add(vo);
		}

		result.setList(voList);

		return result;
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Override
	public ProductDepotVO copyPropertiesToVO(ProductDepot temp)
	{
		ProductDepotVO vo = new ProductDepotVO();
		vo.setId(temp.getId());
		vo.setCardId(temp.getCardId());
		vo.setBatchId(temp.getBatchId());
		vo.setCardPwd(temp.getCardPwd());
		vo.setPointVO(pointService.copyPropertiesToVO(temp.getPoint()));
		vo.setExtractNo(temp.getExtractNo());
		vo.setProductId(temp.getProductId());
		vo.setRemark(temp.getRemark());
		vo.setStatus(temp.getStatus());
		vo.setStatusLabel(dataSourceService.findOptionVOById(CommonConstant.DataSourceName.CARD_STATUS,
				String.valueOf(temp.getStatus())).getText());
		vo.setStockInDate(temp.getStockInDate());
		vo.setUsefulStartDate(temp.getUsefulEndDate());
		vo.setUsefulEndDate(temp.getUsefulEndDate());

		return vo;
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Override
	public ProductDepot updateProductDepotStatus(ProductDepot depot)
	{
		try
		{
			logger.debug("updateProductDepotStatus start : depot[" + depot.toString() + "]");
			ProductDepot productDepot = productDepotDao.findOne(depot.getId());
			if (productDepot != null)
			{
				productDepot.setStatus(depot.getStatus());
				productDepot = productDepotDao.save(productDepot);
			}
			else
			{
				// 面值不存在
				logger.error("updateProductDepotStatus failed : depot[" + depot.toString() + "]");
				throw new RpcException(ErrorCodeConst.CODE_CARD_NOT_EXISTS);
			}
			logger.debug("updateProductDepotStatus end : depot[" + depot.toString() + "]");
			return productDepot;
		}
		catch (RpcException e)
		{
			throw ExceptionUtil.throwException(e);
		}
		catch (Exception e)
		{
			logger.error("updateProductDepotStatus failed : depot[" + depot.toString() + "]");
			throw new RpcException(ErrorCodeConst.ErrorCode99998);
		}
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Override
	public String deleteProductDepot(ProductDepot depot)
	{
		try
		{
			logger.debug("deleteProductDepot start : depot:" + depot.toString() + "]");
			ProductDepot productDepot = productDepotDao.findOne(depot.getId());
			if (productDepot != null)
			{
				productDepotDao.delete(productDepot);
			}
			else
			{
				logger.error("deleteProductDepot failed : depot:" + depot.toString() + "]");
				throw new RpcException(ErrorCodeConst.ErrorCode10603);
			}
			logger.debug("deleteProductDepot end : depot:" + depot.toString() + "]");
			return Constant.Common.SUCCESS;
		}
		catch (Exception e)
		{
			logger.error("deleteProductDepot failed : depot:" + depot.toString() + "]");
			throw new RpcException(ErrorCodeConst.ErrorCode99998);
		}
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Override
	public ProductDepot saveProductDepot(ProductDepot depot)
	{
		try
		{
			logger.debug("saveProductDepot start : depot[" + depot.toString() + "]");
			ProductDepot productDepot = productDepotDao.queryProductDepotByCardId(depot.getCardId(),
					depot.getProductId());
			// 唯一性
			if (BeanUtils.isNotNull(productDepot) && depot.getId() == 0)
			{
				logger.error("saveProductDepot failed : depot:[" + depot.toString() + "]");
				throw new RpcException(ErrorCodeConst.CODE_CARD_NO_EXISTS);
			}
			else
			{
				depot = productDepotDao.save(depot);
			}

			logger.debug("saveProductDepot end : depot:[" + depot.toString() + "]");
			return depot;
		}
		catch (RpcException e)
		{
			throw ExceptionUtil.throwException(e);
		}
		catch (Exception e)
		{
			logger.error("saveProductDepot failed : depot:[" + depot.toString() + "]");
			logger.error(e.getLocalizedMessage(), e);
			throw new RpcException(ErrorCodeConst.ErrorCode99998);
		}
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Override
	public ProductDepot queryProductDepot(long id)
	{
		logger.debug("queryProductDepot start : id [" + id + "]");
		try
		{
			ProductDepot pd = productDepotDao.findOne(id);
			logger.debug("queryProductDepot end : id [" + id + "]");
			return pd;
		}
		catch (Exception e)
		{
			logger.error("queryProductDepot failed : id [" + id + "]");
			logger.error(e.getLocalizedMessage(), e);
			throw new RpcException(ErrorCodeConst.ErrorCode99998);
		}

	}

	// 获得可用卡密库存
	@Override
	public int getUsableCount(long pointId)
	{
		logger.debug("getUsableCount start : pointId [" + pointId + "]");
		try
		{
			int i = productDepotDao.getUsableCount(pointId);
			logger.debug("getUsableCount end : pointId [" + pointId + "]");
			return i;
		}
		catch (Exception e)
		{
			logger.error("getUsableCount failed : pointId [" + pointId + "]");
			logger.error(e.getLocalizedMessage(), e);
			throw new RpcException(ErrorCodeConst.ErrorCode99998);
		}

	}

	@Override
	public List<ProductDepot> queryCardPwdByPointid(long pointid, int count)
	{
		logger.debug("queryCardPwdByPointid start : pointid [" + pointid + "] count [" + count + "]");
		try
		{
			List<ProductDepot> list = productDepotDao.queryCardPwdByPointid(pointid, count);
			logger.debug("queryCardPwdByPointid end : pointid [" + pointid + "] count [" + count + "]");
			return list;
		}
		catch (Exception e)
		{
			logger.error("queryCardPwdByPointid failed : pointid [" + pointid + "] count [" + count + "]");
			logger.error(e.getLocalizedMessage(), e);
			throw new RpcException(ErrorCodeConst.ErrorCode99998);
		}

	}

	@Override
	public List<ProductDepot> queryCardPwdByExtractNo(String extractNo)
	{
		logger.debug("queryCardPwdByExtractNo start : extractNo [" + extractNo + "]");
		try
		{
			List<ProductDepot> list = productDepotDao.queryCardPwdByExtractNo(extractNo);
			logger.debug("queryCardPwdByExtractNo end : extractNo [" + extractNo + "]");
			return list;
		}
		catch (Exception e)
		{
			logger.error("queryCardPwdByExtractNo failed : extractNo [" + extractNo + "]");
			logger.error(e.getLocalizedMessage(), e);
			throw new RpcException(ErrorCodeConst.ErrorCode99998);
		}
	}

}
