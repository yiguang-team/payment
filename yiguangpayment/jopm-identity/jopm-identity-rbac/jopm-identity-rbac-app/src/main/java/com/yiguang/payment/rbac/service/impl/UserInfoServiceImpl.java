package com.yiguang.payment.rbac.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.rpc.RpcException;
import com.yiguang.payment.common.Constant;
import com.yiguang.payment.common.datasource.service.DataSourceService;
import com.yiguang.payment.common.exception.ErrorCodeConst;
import com.yiguang.payment.common.utils.BeanUtils;
import com.yiguang.payment.rbac.entity.UserInfo;
import com.yiguang.payment.rbac.repository.UserInfoDao;
import com.yiguang.payment.rbac.service.UserInfoService;

@Service("userInfoService")
@Transactional
public class UserInfoServiceImpl implements UserInfoService {

	private static Logger logger = LoggerFactory.getLogger(UserInfoServiceImpl.class);
	
	@Autowired
	DataSourceService dataSourceService;
	@Autowired
	UserInfoDao userInfoDao;

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Override
	@CacheEvict(value = { "userInfoCache", "dataSourceCache" }, allEntries = true, beforeInvocation = true)
	public String deleteUserInfo(UserInfo userInfo) {
		logger.debug("deleteUserInfo start, userInfo [" + userInfo.toString() + "]");
		try
		{
			UserInfo queryProduct = userInfoDao.findOne(userInfo.getId());
			if (queryProduct != null)
			{
				userInfoDao.delete(queryProduct);
			}
			else
			{
				// 准备删除的用户不存在！
				logger.error("deleteUserInfo failed, userInfo [" + userInfo.toString() + "]");
				throw new RpcException(19409);
			}
			logger.debug("deleteUserInfo end, userInfo [" + userInfo.toString() + "]");
			return Constant.Common.SUCCESS;
		}
		catch (RpcException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			logger.error("deleteUserInfo failed, userInfo [" + userInfo.toString() + "]");
			logger.error(e.getLocalizedMessage(), e);
			throw new RpcException(ErrorCodeConst.ErrorCode99998);
		}
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Override
	@CacheEvict(value = { "userInfoCache", "dataSourceCache" }, allEntries = true, beforeInvocation = true)
	public UserInfo saveUserInfo(UserInfo userInfo) {
		logger.debug("saveUserInfo start, userInfo [" + userInfo.toString() + "]");
		try
		{

			UserInfo queryProduct = userInfoDao.queryUserInfoByName(userInfo.getName());
			// 唯一性
			if (BeanUtils.isNotNull(queryProduct) && userInfo.getId() == 0)
			{

				logger.error("saveUserInfo failed, userInfo [" + userInfo.toString() + "]");
				throw new RpcException(ErrorCodeConst.ErrorCode10404);
			}
			else
			{
				userInfo = userInfoDao.save(userInfo);
			}

			logger.debug("saveUserInfo end, userInfo [" + userInfo.toString() + "]");
			return userInfo;
		}
		catch (RpcException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			logger.error("saveUserInfo failed, userInfo [" + userInfo.toString() + "]");
			logger.error(e.getLocalizedMessage(), e);
			throw new RpcException(ErrorCodeConst.ErrorCode99998);
		}
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Override
	@Cacheable(value = "userInfoCache", key = "#root.methodName+#id")
	public UserInfo queryUserInfo(long id) {
		logger.debug("queryUserInfo start, id [" + id + "]");
		try
		{
			UserInfo user = userInfoDao.findOne(id);
			logger.debug("queryUserInfo end, id [" + id + "]");
			return user;
		}
		catch (Exception e)
		{
			logger.error("queryUserInfo error, id [" + id + "]");
			logger.error(e.getLocalizedMessage(), e);
			throw new RpcException(ErrorCodeConst.ErrorCode99998);
		}
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Override
	@Cacheable(value = "userInfoCache", key = "#root.methodName+#userId")
	public UserInfo queryUserInfoByUserId(long userId) {
		logger.debug("queryUserInfoByUserId start, userId [" + userId + "]");
		try
		{
			UserInfo userInfo = userInfoDao.queryUserInfoByUserId(userId);
			logger.debug("queryUserInfoByUserId end, userId [" + userId + "]");
			return userInfo;
		}
		catch (Exception e)
		{
			logger.error("queryUserInfoByUserId error, userId [" + userId + "]");
			logger.error(e.getLocalizedMessage(), e);
			throw new RpcException(ErrorCodeConst.ErrorCode99998);
		}
	}
}
