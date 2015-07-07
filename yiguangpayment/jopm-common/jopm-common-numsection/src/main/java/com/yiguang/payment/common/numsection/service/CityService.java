package com.yiguang.payment.common.numsection.service;

/**
 * 城市逻辑层
 * 
 * @author Jinger
 * @date：2013-10-18
 */
import java.util.List;
import java.util.Map;

import com.yiguang.payment.common.numsection.entity.City;
import com.yiguang.payment.common.query.BSort;
import com.yiguang.payment.common.query.YcPage;

public interface CityService
{
	public City saveCity(City city);

	public void deleteCity(String cityId);

	public City findOne(String cityId);

	public City getByCityName(String cityName);

	public List<City> selectAll();

	public List<City> getCityByProvince(String provinceId);

//	public YcPage<City> queryCity(Map<String, Object> searchParams, int pageNumber, int pageSize, BSort bsort);
	
	public City getByCityNameAndPid(String cityName, String pid);

}
