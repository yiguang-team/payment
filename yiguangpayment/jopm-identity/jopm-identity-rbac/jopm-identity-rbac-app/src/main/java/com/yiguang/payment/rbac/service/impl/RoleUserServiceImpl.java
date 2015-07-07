package com.yiguang.payment.rbac.service.impl;

import java.util.ArrayList;
import java.util.List;

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
import com.yiguang.payment.common.CommonConstant;
import com.yiguang.payment.common.Constant;
import com.yiguang.payment.common.datasource.service.DataSourceService;
import com.yiguang.payment.common.exception.ErrorCodeConst;
import com.yiguang.payment.common.utils.BeanUtils;
import com.yiguang.payment.common.utils.StringUtil;
import com.yiguang.payment.rbac.entity.RoleUser;
import com.yiguang.payment.rbac.repository.RoleUserDao;
import com.yiguang.payment.rbac.service.RoleUserService;
import com.yiguang.payment.rbac.vo.RoleUserVO;
@Service("roleUserService")
@Transactional
public class RoleUserServiceImpl implements RoleUserService {

	private static Logger logger = LoggerFactory.getLogger(RoleUserServiceImpl.class);
	
	@Autowired
	DataSourceService dataSourceService;
	@Autowired
	RoleUserDao roleUserDao;
	
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Override
	@CacheEvict(value = { "roleUserCache", "dataSourceCache" }, allEntries = true, beforeInvocation = true)
	public RoleUser updateRoleUserStatus(RoleUser roleUser) {
		try
		{
			logger.debug("updateRoleUserStatus start, roleUser [" + roleUser.toString() + "]");
			RoleUser queryUser = roleUserDao.findOne(roleUser.getId());
			if (queryUser == null)
			{
				// 角色菜单不存在
				logger.error("updateRoleUserStatus failed, roleUser [" + roleUser.toString() + "]");
				throw new RpcException(ErrorCodeConst.ErrorCode10403);
			}
			queryUser.setStatus(roleUser.getStatus());
			queryUser = roleUserDao.save(queryUser);
			logger.debug("updateRoleUserStatus end, roleUser [" + roleUser.toString() + "]");
			return queryUser;
		}
		catch (RpcException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			logger.error("updateRoleUserStatus error, roleUser [" + roleUser.toString() + "]");
			logger.error(e.getLocalizedMessage(), e);
			throw new RpcException(ErrorCodeConst.ErrorCode99998);
		}
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Override
	@CacheEvict(value = { "roleUserCache", "dataSourceCache" }, allEntries = true, beforeInvocation = true)
	public String deleteRoleUser(RoleUser roleUser) {
		logger.debug("deleteRoleUser start, roleUser [" + roleUser.toString() + "]");
		try
		{
			RoleUser queryProduct = roleUserDao.findOne(roleUser.getId());
			if (queryProduct != null)
			{
				roleUserDao.delete(queryProduct);
			}
			else
			{
				// 准备删除的用户不存在！
				logger.error("deleteRoleUser failed, roleUser [" + roleUser.toString() + "]");
				throw new RpcException(19409);
			}
			logger.debug("deleteRoleUser end, roleUser [" + roleUser.toString() + "]");
			return Constant.Common.SUCCESS;
		}
		catch (RpcException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			logger.error("deleteRoleUser failed, roleUser [" + roleUser.toString() + "]");
			logger.error(e.getLocalizedMessage(), e);
			throw new RpcException(ErrorCodeConst.ErrorCode99998);
		}
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Override
	@CacheEvict(value = { "roleUserCache", "dataSourceCache" }, allEntries = true, beforeInvocation = true)
	public RoleUser saveRoleUser(RoleUser roleUser) {
		logger.debug("saveRoleUser start, roleUser [" + roleUser.toString() + "]");
		try
		{

			RoleUser queryProduct = roleUserDao.queryRoleUserByRoleIdandUserId(roleUser.getRoleId(),roleUser.getUserId());
			// 唯一性
			if (BeanUtils.isNotNull(queryProduct) && roleUser.getId() == 0)
			{

				logger.error("saveRoleUser failed, roleUser [" + roleUser.toString() + "]");
				throw new RpcException(ErrorCodeConst.ErrorCode10404);
			}
			else
			{
				roleUser = roleUserDao.save(roleUser);
			}

			logger.debug("saveRoleUser end, roleUser [" + roleUser.toString() + "]");
			return roleUser;
		}
		catch (RpcException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			logger.error("saveRoleUser failed, roleUser [" + roleUser.toString() + "]");
			logger.error(e.getLocalizedMessage(), e);
			throw new RpcException(ErrorCodeConst.ErrorCode99998);
		}
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Override
	@CacheEvict(value = { "roleUserCache", "dataSourceCache" }, allEntries = true, beforeInvocation = true)
	public void saveRoleUser(RoleUser roleUser ,String roleUserIds) {
		logger.debug("saveRoleUser start, roleUser [" + roleUser.toString() + "] roleUserIds ["
				+ roleUserIds + "]");
		try
		{
			if (StringUtils.isNotEmpty(roleUserIds))
			{
				String[] roleIdArray = roleUserIds.split("[,]");
				List<RoleUser> list = new ArrayList<RoleUser>();
				RoleUser roleUser2 = null;
				for (String roleId : roleIdArray)
				{
					if (StringUtil.isNotBlank(roleId))
					{
						roleUser2 = new RoleUser();
						roleUser2.setId(roleUser.getId());
						roleUser2.setRoleId(Long.valueOf(roleId.trim()));
						roleUser2.setUserId(roleUser.getUserId());
						roleUser2.setStatus(roleUser.getStatus());
						list.add(roleUser2);
					}
				}

				List<RoleUser> oldList = queryRoleUserByUserId(roleUser.getUserId());
				roleUserDao.delete(oldList);
				roleUserDao.save(list);
				
			}
			logger.debug("saveRoleUser end, roleUser [" + roleUser.toString() + "] roleUserIds ["
					+ roleUserIds + "]");
		}
		catch (RpcException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			logger.error("saveRoleUser failed, roleUser [" + roleUser.toString() + "] roleUserIds ["
					+ roleUserIds + "]");
			logger.error(e.getLocalizedMessage(), e);
			throw new RpcException(ErrorCodeConst.ErrorCode99998);
		}
	}
	
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Override
	@Cacheable(value = "roleUserCache", key = "#root.methodName+#id")
	public RoleUser queryRoleUser(long id) {
		logger.debug("queryRoleUser start, id [" + id + "]");
		try
		{
			RoleUser user = roleUserDao.findOne(id);
			logger.debug("queryRoleUser end, id [" + id + "]");
			return user;
		}
		catch (Exception e)
		{
			logger.error("queryRoleUser error, id [" + id + "]");
			logger.error(e.getLocalizedMessage(), e);
			throw new RpcException(ErrorCodeConst.ErrorCode99998);
		}
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Override
	@Cacheable(value = "roleUserCache", key = "#root.methodName+#roleId")
	public List<RoleUser> queryRoleUserByRole(long roleId) {
		logger.debug("queryRoleUserByRole start, role [" + roleId + "]");
		try
		{
			List<RoleUser> user = roleUserDao.queryRoleUserByRoleId(roleId);
			logger.debug("queryRoleUserByRole end, role [" + roleId + "]");
			return user;
		}
		catch (Exception e)
		{
			logger.error("queryRoleUserByRole error, role [" + roleId + "]");
			logger.error(e.getLocalizedMessage(), e);
			throw new RpcException(ErrorCodeConst.ErrorCode99998);
		}
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Override
	@Cacheable(value = "roleUserCache", key = "#root.methodName+#userId")
	public List<RoleUser> queryRoleUserByUserId(long userId) {
		logger.debug("queryRoleUserByUserId start, userId [" + userId + "]");
		try
		{
			List<RoleUser> user = roleUserDao.queryRoleUserByUserId(userId);
			logger.debug("queryRoleUserByUserId end, userId [" + userId + "]");
			return user;
		}
		catch (Exception e)
		{
			logger.error("queryRoleUserByUserId error, userId [" + userId + "]");
			logger.error(e.getLocalizedMessage(), e);
			throw new RpcException(ErrorCodeConst.ErrorCode99998);
		}
	}
	
//	@Override
//	@Cacheable(value = "roleUserCache")
//	public YcPage<RoleUserVO> queryRoleUserList(
//			Map<String, Object> searchParams, int pageNumber, int pageSize,
//			String sortType) {
//		logger.debug("queryRoleUserList start");
//		try
//		{
//			Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
//			YcPage<RoleUser> ycPage = PageUtil.queryYcPage(roleUserDao, filters, pageNumber, pageSize, new Sort(
//					Direction.DESC, "id"), RoleUser.class);
//
//			YcPage<RoleUserVO> result = new YcPage<RoleUserVO>();
//			result.setPageTotal(ycPage.getPageTotal());
//			result.setCountTotal(ycPage.getCountTotal());
//			List<RoleUser> list = ycPage.getList();
//			List<RoleUserVO> voList = new ArrayList<RoleUserVO>();
//			RoleUserVO vo = null;
//			for (RoleUser temp : list)
//			{
//				vo = copyPropertiesToVO(temp);
//				voList.add(vo);
//			}
//
//			result.setList(voList);
//			logger.debug("queryRoleUserList end");
//			return result;
//		}
//		catch (RpcException e)
//		{
//			throw e;
//		}
//		catch (Exception e)
//		{
//			logger.error("queryRoleUserList failed");
//			logger.error(e.getLocalizedMessage(), e);
//			throw new RpcException(ErrorCodeConst.ErrorCode99998);
//		}
//	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Override
	public RoleUserVO copyPropertiesToVO(RoleUser temp) {
		logger.debug("copyPropertiesToVO start");
		try
		{
			RoleUserVO vo = new RoleUserVO();
			vo.setId(temp.getId());
			vo.setUserId(temp.getUserId());
			vo.setRoleId(temp.getRoleId());
			vo.setStatus(temp.getStatus());

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

}
