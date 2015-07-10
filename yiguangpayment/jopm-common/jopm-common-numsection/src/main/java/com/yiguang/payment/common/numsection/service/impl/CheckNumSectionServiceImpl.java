package com.yiguang.payment.common.numsection.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.rpc.RpcException;
import com.yiguang.payment.common.exception.ErrorCodeConst;
import com.yiguang.payment.common.numsection.entity.CarrierInfo;
import com.yiguang.payment.common.numsection.entity.City;
import com.yiguang.payment.common.numsection.entity.NumSection;
import com.yiguang.payment.common.numsection.entity.Province;
import com.yiguang.payment.common.numsection.repository.CarrierInfoDao;
import com.yiguang.payment.common.numsection.service.CheckNumSectionService;
import com.yiguang.payment.common.numsection.service.CityService;
import com.yiguang.payment.common.numsection.service.NumSectionService;
import com.yiguang.payment.common.numsection.service.ProvinceService;
import com.yiguang.payment.common.utils.StringUtil;

@Service("checkNumSectionService")
@Transactional
public class CheckNumSectionServiceImpl implements CheckNumSectionService
{

	@Autowired
	private NumSectionService numSectionService;

	@Autowired
	private CityService cityService;

	@Autowired
	private ProvinceService provinceService;
	
	@Autowired
	private CarrierInfoDao carrierInfoDao;

	private static Logger logger = LoggerFactory.getLogger(CheckNumSectionServiceImpl.class);

	/**
	 * 手机归属地查询 本地查询没有则调用第三方接口查询 Author：Jinger 2014-01-23
	 */
	@Override
	public NumSection checkNum(String phone)
	{
		logger.info("[CheckNumSectionServiceImpl:checkNum(" + phone + ")]");
		if (phone != null && !phone.isEmpty())
		{
			String numSection = phone.substring(0, 7);
			NumSection num = numSectionService.findOne(numSection);
			if (num == null)
			{
				num = new NumSection();
				try
				{
					Map<String, Object> param = new HashMap<String, Object>();
					param.put("mobile", phone);
					String url = "http://wap.ip138.com/sim_search138.asp?mobile=" + phone;
					String response = requestPost(url, StringUtil.initString(), param);
					// 处理内容
					int begin = response.indexOf("归属地：");
					int end = response.indexOf("卡类型：");
					int end2 = response.indexOf("天气预报");
					if (begin > 0 && end > 0 && end - begin > 12 && end2 > 0)
					{
						String re = response.substring(begin + 4, end - 8);
						if (end - begin == 13)
						{
							re = response.substring(begin + 4, end - 7);
						}
						String re2 = response.substring(end + 4, end2 - 71);
						String[] provincestr = re.split(" ");
						if (!StringUtil.isNullOrEmpty(re))
						{
							City city = null;
							if (provincestr.length == 1)
							{
								String cityName = provincestr[0];
								logger.debug("查找城市，城市信息名称：" + cityName);
								logger.debug("查找城市，城市信息名称：" + cityName);
								city = cityService.getByCityName(cityName);
							}
							if (provincestr.length > 1)
							{
								String provinceName = provincestr[0];
								String cityName = provincestr[1];
								
								Province p = provinceService.getByProvinceName(provinceName);
								city = cityService.getByCityNameAndPid(cityName, p.getProvinceId());
							}
							
							if (city != null)
							{
								num.setSectionId(numSection);
								num.setCity(city);
								num.setProvince(city.getProvince());
								logger.debug("找到城市，城市信息：" + city.toString() + ",号段信息：" + num.toString());
							}
						}
						else
						{
							logger.debug("归属地查询失败，返回 null");
							return null;
						}
						if (!StringUtil.isNullOrEmpty(re2))
						{
							String YD = "移动";
							String LT = "联通";
							String DX = "电信";
							CarrierInfo carrierInfo = null;
							if (re2.indexOf(YD) != -1)
							{
								carrierInfo = carrierInfoDao.getByName(YD);
							}
							else if (re2.indexOf(LT) != -1)
							{
								carrierInfo = carrierInfoDao.getByName(LT);
							}
							else if (re2.indexOf(DX) != -1)
							{
								carrierInfo = carrierInfoDao.getByName(DX);
							}
							if (carrierInfo != null && carrierInfo.getCarrierNo() != null)
							{
								num.setCarrierInfo(carrierInfo);
								num.setMobileType(re2);
								num.setCreateTime(new Date());
								logger.debug("找到运营商，运营商信息：" + carrierInfo.toString() + ",号段信息：" + num.toString());
								num = numSectionService.saveNumSection(num);
								logger.debug("保存号段信息成功！");
							}
							logger.debug("返回号段信息：" + num.toString());
							return num;
						}
					}
					logger.debug("查询网站返回格式有误，截取失败，返回 null");
					return null;
				}
				catch (Exception e)
				{
					logger.error("checkNum failed, phone:[" + phone + "]");
					logger.error(e.getLocalizedMessage(), e);
					throw new RpcException(ErrorCodeConst.ErrorCode99998);
				}
			}
			logger.debug("数据库已保存，返回号段信息：" + num.toString());
			return num;
		}
		logger.debug("调用查询号码为空，返回 null");
		return null;
	}

	/**
	 * 通用的Http Post请求方式
	 * 
	 * @param url
	 * @param action
	 * @return
	 * @throws Exception
	 */
	public static String requestPost(String url, String action, Map<String, Object> param) throws Exception
	{
		HttpClient client = new DefaultHttpClient();
		String param_str = StringUtil.initString();
		for (Map.Entry<String, Object> entry : param.entrySet())
		{
			logger.debug(entry.getKey() + ":" + entry.getValue() + "\t");
			param_str = param_str + StringUtil.initString() + entry.getKey() + "=" + entry.getValue() + "&";
		}
		if (param_str.length() > 0)
		{
			param_str = param_str.substring(0, param_str.length() - 1);
		}
		action = action + "?" + param_str;
		// 1. 组装URL
		HttpPost post = new HttpPost(url + action);
		// //2. 添加参数
		// post.setEntity(new StringEntity("" , "utf-8"));
		// 3. 执行Http请求
		HttpResponse response = client.execute(post);
		// 4. 获取Http请求返回的实体
		HttpEntity entity = response.getEntity();
		// 5. 获取实体实际获取的对象：String
		String str = EntityUtils.toString(entity, "utf-8");
		client.getConnectionManager().shutdown();
		return str;
	}
}
