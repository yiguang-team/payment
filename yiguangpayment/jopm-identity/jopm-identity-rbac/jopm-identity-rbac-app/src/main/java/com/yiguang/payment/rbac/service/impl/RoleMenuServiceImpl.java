package com.yiguang.payment.rbac.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.rpc.RpcException;
import com.yiguang.payment.common.CommonConstant;
import com.yiguang.payment.common.Constant;
import com.yiguang.payment.common.datasource.service.DataSourceService;
import com.yiguang.payment.common.exception.ErrorCodeConst;
import com.yiguang.payment.common.utils.BeanUtils;
import com.yiguang.payment.common.utils.StringUtil;
import com.yiguang.payment.rbac.entity.Role;
import com.yiguang.payment.rbac.entity.RoleMenu;
import com.yiguang.payment.rbac.repository.RoleMenuDao;
import com.yiguang.payment.rbac.service.RoleMenuService;
import com.yiguang.payment.rbac.vo.RoleMenuVO;
@Service("roleMenuService")
@Transactional
public class RoleMenuServiceImpl implements RoleMenuService {

	private static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
	@Autowired
	JdbcTemplate jdbcTemplate ;
	
	@Autowired
	DataSourceService dataSourceService;
	@Autowired
	RoleMenuDao roleMenuDao;

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Override
	@CacheEvict(value = { "roleMenuCache", "dataSourceCache" }, allEntries = true, beforeInvocation = true)
	public RoleMenu updateRoleMenuStatus(RoleMenu roleMenu) {
		try
		{
			logger.debug("updateRoleMenuStatus start, roleMenu [" + roleMenu.toString() + "]");
			RoleMenu queryUser = roleMenuDao.findOne(roleMenu.getId());
			if (queryUser == null)
			{
				// 角色菜单不存在
				logger.error("updateRoleMenuStatus failed, roleMenu [" + roleMenu.toString() + "]");
				throw new RpcException(ErrorCodeConst.ErrorCode10403);
			}
			queryUser.setStatus(roleMenu.getStatus());
			queryUser = roleMenuDao.save(queryUser);
			logger.debug("updateRoleMenuStatus end, roleMenu [" + roleMenu.toString() + "]");
			return queryUser;
		}
		catch (RpcException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			logger.error("updateRoleMenuStatus error, roleMenu [" + roleMenu.toString() + "]");
			logger.error(e.getLocalizedMessage(), e);
			throw new RpcException(ErrorCodeConst.ErrorCode99998);
		}
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Override
	@CacheEvict(value = { "roleMenuCache", "dataSourceCache" }, allEntries = true, beforeInvocation = true)
	public String deleteRoleMenu(RoleMenu roleMenu) {
		logger.debug("deleteRoleMenu start, roleMenu [" + roleMenu.toString() + "]");
		try
		{
			RoleMenu queryProduct = roleMenuDao.findOne(roleMenu.getId());
			if (queryProduct != null)
			{
				roleMenuDao.delete(queryProduct);
			}
			else
			{
				// 准备删除的用户不存在！
				logger.error("deleteRoleMenu failed, roleMenu [" + roleMenu.toString() + "]");
				throw new RpcException(19409);
			}
			logger.debug("deleteRoleMenu end, roleMenu [" + roleMenu.toString() + "]");
			return Constant.Common.SUCCESS;
		}
		catch (RpcException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			logger.error("deleteRoleMenu failed, roleMenu [" + roleMenu.toString() + "]");
			logger.error(e.getLocalizedMessage(), e);
			throw new RpcException(ErrorCodeConst.ErrorCode99998);
		}
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Override
	@CacheEvict(value = { "roleMenuCache", "dataSourceCache" }, allEntries = true, beforeInvocation = true)
	public RoleMenu saveRoleMenu(RoleMenu roleMenu) {
		logger.debug("saveRoleMenu start, roleMenu [" + roleMenu.toString() + "]");
		try
		{

			RoleMenu queryProduct = roleMenuDao.queryRoleMenuByRoleIdandMenuId(roleMenu.getRoleId(),roleMenu.getMenuId());
			// 唯一性
			if (BeanUtils.isNotNull(queryProduct) && roleMenu.getId() == 0)
			{

				logger.error("saveRoleMenu failed, roleMenu [" + roleMenu.toString() + "]");
				throw new RpcException(ErrorCodeConst.ErrorCode10404);
			}
			else
			{
				roleMenu = roleMenuDao.save(roleMenu);
			}

			logger.debug("saveRoleMenu end, roleMenu [" + roleMenu.toString() + "]");
			return roleMenu;
		}
		catch (RpcException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			logger.error("saveRoleMenu failed, roleMenu [" + roleMenu.toString() + "]");
			logger.error(e.getLocalizedMessage(), e);
			throw new RpcException(ErrorCodeConst.ErrorCode99998);
		}
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Override
	@Cacheable(value = "roleMenuCache", key = "#root.methodName+#id")
	public RoleMenu queryRoleMenu(long id) {
		logger.debug("queryRoleMenu start, id [" + id + "]");
		try
		{
			RoleMenu user = roleMenuDao.findOne(id);
			logger.debug("queryRoleMenu end, id [" + id + "]");
			return user;
		}
		catch (Exception e)
		{
			logger.error("queryRoleMenu error, id [" + id + "]");
			logger.error(e.getLocalizedMessage(), e);
			throw new RpcException(ErrorCodeConst.ErrorCode99998);
		}
	}

	
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Override
	//@Cacheable(value = "roleMenuCache", key = "#root.methodName+#roleList")
	public List<RoleMenu> queryRoleMenuByRoleList(List<Role> roleList) {
		logger.debug("queryRoleMenu start, roleId [" + roleList.toString() + "]");
		try
		{
			List<RoleMenu> roleMenus = new ArrayList<RoleMenu>(); 
			for(Role role : roleList){
				List<RoleMenu> roleMenuList = roleMenuDao.queryRoleMenuByRoleId(role.getId());
				for(RoleMenu roleMenu : roleMenuList){
					roleMenus.add(roleMenu);
				}
			}
			
			logger.debug("queryRoleMenu end, roleId [" +roleList.toString() + "]");
			return roleMenus;
		}
		catch (Exception e)
		{
			logger.error("queryRoleMenu error, roleId [" +roleList.toString() + "]");
			logger.error(e.getLocalizedMessage(), e);
			throw new RpcException(ErrorCodeConst.ErrorCode99998);
		}
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Override
	public RoleMenuVO copyPropertiesToVO(RoleMenu temp) {
		logger.debug("copyPropertiesToVO start");
		try
		{
			RoleMenuVO vo = new RoleMenuVO();
			vo.setId(temp.getId());
			vo.setMenuId(temp.getMenuId());
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

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Override
	@CacheEvict(value = { "roleMenuCache", "dataSourceCache" }, allEntries = true, beforeInvocation = true)
	public void saveRoleMenuList(String menuidstr, long roleId) {
		logger.debug("saveRoleMenuList start: menuidstr,roleId [" + menuidstr + roleId+"]");
		try
		{
//			roleMenuDao.deleteRoleMenuByRoleId(roleId);
			jdbcTemplate.execute("delete from t_role_menu t where t.role_id = " + roleId);
			String[] menuIdArray = menuidstr.split(Constant.StringSplitUtil.DECODE);
			List<RoleMenu> roleMenuList = new ArrayList<RoleMenu>();
			for(String menuId : menuIdArray){
				menuId = menuId.replace(",", StringUtil.initString());
				long id = Long.valueOf(menuId);
				RoleMenu roleMenu = new RoleMenu();
				roleMenu.setMenuId(id);
				roleMenu.setRoleId(roleId);
				roleMenu.setStatus(CommonConstant.CommonStatus.OPEN);
				roleMenuList.add(roleMenu);
			}
			roleMenuDao.save(roleMenuList);
			
		}
		catch (Exception e)
		{
			logger.error("saveRoleMenuList error: menuidstr,roleId [" + menuidstr +roleId+ "]");
			logger.error(e.getLocalizedMessage(), e);
			throw new RpcException(ErrorCodeConst.ErrorCode99998);
		}
	}
	
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Override
	@Cacheable(value = "roleMenuCache", key = "#root.methodName+#menuId")
	public List<RoleMenu> queryRoleMenuByMenuId(long menuId) {
		logger.debug("queryRoleMenuByMenuId start, menuId [" + menuId + "]");
		try
		{
			List<RoleMenu> user = roleMenuDao.queryRoleMenuByMenuId(menuId);
			logger.debug("queryRoleMenuByMenuId end, menuId [" + menuId + "]");
			return user;
		}
		catch (Exception e)
		{
			logger.error("queryRoleMenuByMenuId error, menuId [" + menuId + "]");
			logger.error(e.getLocalizedMessage(), e);
			throw new RpcException(ErrorCodeConst.ErrorCode99998);
		}
	}

}
