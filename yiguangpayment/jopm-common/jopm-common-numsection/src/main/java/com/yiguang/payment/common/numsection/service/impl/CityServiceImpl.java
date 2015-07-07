package com.yiguang.payment.common.numsection.service.impl;

/**
 * 城市逻辑层
 * @author Jinger
 * @date：2013-10-18
 *
 */
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

import com.alibaba.dubbo.rpc.RpcException;
import com.yiguang.payment.common.exception.ErrorCodeConst;
import com.yiguang.payment.common.numsection.entity.City;
import com.yiguang.payment.common.numsection.repository.CityDao;
import com.yiguang.payment.common.numsection.service.CityService;

@Service("cityService")
public class CityServiceImpl implements CityService
{
	@Autowired
	private CityDao cityDao;
	@PersistenceContext
	private EntityManager em;
	private static Logger logger = LoggerFactory.getLogger(CityServiceImpl.class);

	@Override
	@CacheEvict(value={"cityCache","dataSourceCache"},allEntries=true,beforeInvocation=true)
	public City saveCity(City city)
	{
		logger.debug("saveCity start, city (" + city != null ? city.toString() : null + ")]");
		try
		{
			String cityName = city.getCityName();
			City Qcity = cityDao.getByCityName(cityName);
			if (city != null && Qcity == null)
				city = cityDao.save(city);
			logger.debug("saveCity end, city (" + city != null ? city.toString() : null + ")]");
			return city;
		}
		catch (Exception e)
		{
			logger.error("saveCity failed, city (" + city != null ? city.toString() : null + ")]");
			logger.error(e.getLocalizedMessage(), e);
			throw new RpcException(ErrorCodeConst.ErrorCode99998);
		}
	}

	@Override
	@CacheEvict(value={"cityCache","dataSourceCache"},allEntries=true,beforeInvocation=true)
	public void deleteCity(String cityId)
	{
		logger.debug("deleteCity start, cityId is [" + cityId + "]");
		try
		{
			cityDao.delete(cityId);
			logger.debug("deleteCity end, cityId is [" + cityId + "]");
		}
		catch (Exception e)
		{
			logger.error("deleteCity failed, cityId is [" + cityId + "]");
			logger.error(e.getLocalizedMessage(), e);
			throw new RpcException(ErrorCodeConst.ErrorCode99998);
		}
	}

	@Override
	@Cacheable(value="cityCache",key="#root.methodName+#cityId")
	public City findOne(String cityId)
	{
		logger.debug("findOne start, cityId is [" + cityId + "]");
		try
		{
			City city = cityDao.findOne(cityId);
			logger.debug("findOne end, cityId is [" + cityId + "]");
			return city;
		}
		catch (Exception e)
		{
			logger.error("findOne failed, cityId is [" + cityId + "]");
			logger.error(e.getLocalizedMessage(), e);
			throw new RpcException(ErrorCodeConst.ErrorCode99998);
		}
	}

	@Override
	@Cacheable(value="cityCache",key="#root.methodName")
	public List<City> selectAll()
	{
		logger.debug("selectAll start");
		try
		{
			List<City> list = cityDao.selectAll();
			logger.debug("selectAll end");
			return list;
		}
		catch (Exception e)
		{
			logger.error("selectAll failed");
			logger.error(e.getLocalizedMessage(), e);
			throw new RpcException(ErrorCodeConst.ErrorCode99998);
		}
	}

	@Override
	@Cacheable(value="cityCache",key="#root.methodName+#provinceId")
	public List<City> getCityByProvince(String provinceId)
	{
		logger.debug("getCityByProvince start, provinceId:[" + provinceId + "]");
		try
		{
			List<City> list = cityDao.getCityList(provinceId);
			logger.debug("getCityByProvince end, provinceId:[" + provinceId + "]");
			return list;
		}
		catch (Exception e)
		{
			logger.error("getCityByProvince failed, provinceId:[" + provinceId + "]");
			logger.error(e.getLocalizedMessage(), e);
			throw new RpcException(ErrorCodeConst.ErrorCode99998);
		}
	}

	@Override
	@Cacheable(value="cityCache",key="#root.methodName+#cityName")
	public City getByCityName(String cityName)
	{
		logger.debug("getByCityName start, cityName:[" + cityName + "]");
		try
		{
			City city = cityDao.getByCityName(cityName);
			logger.debug("getByCityName end, cityName:[" + cityName + "]");
			return city;
		}
		catch (Exception e)
		{
			logger.error("getByCityName failed, cityName:[" + cityName + "]");
			logger.error(e.getLocalizedMessage(), e);
			throw new RpcException(ErrorCodeConst.ErrorCode99998);
		}
	}
	
	@Override
	@Cacheable(value="cityCache",key="#root.methodName+#cityName+#pid")
	public City getByCityNameAndPid(String cityName, String pid)
	{
		logger.debug("getByCityName start, cityName:[" + cityName + "]");
		try
		{
			String sql = "select * from t_city t where t.province_id = '" + pid + "' and t.City_Name like '%"+cityName+"%'";
			Query query = em.createNativeQuery(sql, City.class);
			@SuppressWarnings("unchecked")
			List<City> list = query.getResultList();
			logger.debug("getByCityName end, cityName:[" + cityName + "]");
			return list.get(0);
		}
		catch (Exception e)
		{
			logger.error("getByCityName failed, cityName:[" + cityName + "]");
			logger.error(e.getLocalizedMessage(), e);
			throw new RpcException(ErrorCodeConst.ErrorCode99998);
		}
	}
}
