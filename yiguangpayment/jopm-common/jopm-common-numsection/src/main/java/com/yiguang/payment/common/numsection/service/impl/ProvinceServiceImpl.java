package com.yiguang.payment.common.numsection.service.impl;

/**
 * 省份逻辑层
 * @author Jinger
 * @date：2013-10-18
 *
 */
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.rpc.RpcException;
import com.yiguang.payment.common.exception.ErrorCodeConst;
import com.yiguang.payment.common.numsection.entity.Province;
import com.yiguang.payment.common.numsection.repository.ProvinceDao;
import com.yiguang.payment.common.numsection.service.ProvinceService;

@Service("provinceService")
public class ProvinceServiceImpl implements ProvinceService
{
	@Autowired
	private ProvinceDao provinceDao;

	private static Logger logger = LoggerFactory.getLogger(ProvinceServiceImpl.class);

	@Override
	@CacheEvict(value={"provinceCache","dataSourceCache"},allEntries=true,beforeInvocation=true)
	public Province saveProvince(Province province)
	{
		logger.debug("[ProvinceServiceImpl:saveProvince(" + province != null ? province.toString() : null + ")]");
		String provinceName = province.getProvinceName();
		Province province2 = provinceDao.getByName(provinceName);
		// 唯一性
		if (province != null && province2 == null)
			province = provinceDao.save(province);
		return province;
	}

	@Override
	@CacheEvict(value={"provinceCache","dataSourceCache"},allEntries=true,beforeInvocation=true)
	public void deleteProvince(String provinceId)
	{
		logger.debug("[ProvinceServiceImpl:deleteProvince(" + provinceId + ")]");
		provinceDao.delete(provinceId);
	}

	@Override
	public void updateProvince(String provinceId, String status)
	{
		// return 0;
	}

	@Override
	@Cacheable(value="provinceCache",key="#root.methodName+#provinceId")
	public Province findOne(String provinceId)
	{
		logger.debug("[ProvinceServiceImpl:findOne(" + provinceId + ")]");
		return provinceDao.findOne(provinceId);
	}

	@Override
	@Cacheable(value="provinceCache",key="#root.methodName")
	public List<Province> getAllProvince()
	{
		logger.debug("[ProvinceServiceImpl:getAllProvince()]");
		return provinceDao.selectAll();
	}

	@Override
	@Cacheable(value="provinceCache",key="#root.methodName+#provinceName")
	public Province getByProvinceName(String provinceName)
	{
		logger.debug("getByProvinceName start, provinceName:[" + provinceName + "]");
		try
		{
			Province province = provinceDao.getByName(provinceName);
			logger.debug("getByProvinceName end, provinceName:[" + provinceName + "]");
			return province;
		}
		catch (Exception e)
		{
			logger.error("getByProvinceName failed, provinceName:[" + provinceName + "]");
			logger.error(e.getLocalizedMessage(), e);
			throw new RpcException(ErrorCodeConst.ErrorCode99998);
		}
	}
}
