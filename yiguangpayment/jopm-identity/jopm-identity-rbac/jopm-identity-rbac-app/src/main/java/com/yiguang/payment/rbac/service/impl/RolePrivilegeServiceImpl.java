package com.yiguang.payment.rbac.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.rpc.RpcException;
import com.yiguang.payment.common.CommonConstant;
import com.yiguang.payment.common.Constant;
import com.yiguang.payment.common.datasource.service.DataSourceService;
import com.yiguang.payment.common.exception.ErrorCodeConst;
import com.yiguang.payment.common.utils.BeanUtils;
import com.yiguang.payment.common.utils.StringUtil;
import com.yiguang.payment.rbac.entity.Role;
import com.yiguang.payment.rbac.entity.RolePrivilege;
import com.yiguang.payment.rbac.repository.RolePrivilegeDao;
import com.yiguang.payment.rbac.service.RolePrivilegeService;
import com.yiguang.payment.rbac.vo.RolePrivilegeVO;
@Service("rolePrivilegeService")
@Transactional
public class RolePrivilegeServiceImpl implements RolePrivilegeService {
	private static Logger logger = LoggerFactory.getLogger(RolePrivilegeServiceImpl.class);
	@Autowired
	JdbcTemplate jdbcTemplate ;
	
	@Autowired
	DataSourceService dataSourceService;
	@Autowired
	RolePrivilegeDao rolePrivilegeDao;

	@Override
	public RolePrivilege updateRolePrivilegeStatus(RolePrivilege rolePrivilege) {
		try
		{
			logger.debug("updateRolePrivilegeStatus start, rolePrivilege [" + rolePrivilege.toString() + "]");
			RolePrivilege queryUser = rolePrivilegeDao.findOne(rolePrivilege.getId());
			if (queryUser == null)
			{
				// 角色菜单不存在
				logger.error("updateRolePrivilegeStatus failed, rolePrivilege [" + rolePrivilege.toString() + "]");
				throw new RpcException(ErrorCodeConst.ErrorCode10403);
			}
			queryUser.setStatus(rolePrivilege.getStatus());
			queryUser = rolePrivilegeDao.save(queryUser);
			logger.debug("updateRolePrivilegeStatus end, rolePrivilege [" + rolePrivilege.toString() + "]");
			return queryUser;
		}
		catch (RpcException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			logger.error("updateRolePrivilegeStatus error, rolePrivilege [" + rolePrivilege.toString() + "]");
			logger.error(e.getLocalizedMessage(), e);
			throw new RpcException(ErrorCodeConst.ErrorCode99998);
		}
	}

	@Override
	public String deleteRolePrivilege(RolePrivilege rolePrivilege) {
		logger.debug("deleteRolePrivilege start, rolePrivilege [" + rolePrivilege.toString() + "]");
		try
		{
			RolePrivilege queryProduct = rolePrivilegeDao.findOne(rolePrivilege.getId());
			if (queryProduct != null)
			{
				rolePrivilegeDao.delete(queryProduct);
			}
			else
			{
				// 准备删除的用户不存在！
				logger.error("deleteRolePrivilege failed, rolePrivilege [" + rolePrivilege.toString() + "]");
				throw new RpcException(19409);
			}
			logger.debug("deleteRolePrivilege end, rolePrivilege [" + rolePrivilege.toString() + "]");
			return Constant.Common.SUCCESS;
		}
		catch (RpcException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			logger.error("deleteRolePrivilege failed, rolePrivilege [" + rolePrivilege.toString() + "]");
			logger.error(e.getLocalizedMessage(), e);
			throw new RpcException(ErrorCodeConst.ErrorCode99998);
		}
	}

	@Override
	public RolePrivilege saveRolePrivilege(RolePrivilege rolePrivilege) {
		logger.debug("saveRolePrivilege start, rolePrivilege [" + rolePrivilege.toString() + "]");
		try
		{

			RolePrivilege queryProduct = rolePrivilegeDao.queryRolePrivilegeByPrivilegeIdandMenuId(rolePrivilege.getRoleId(),rolePrivilege.getPrivilegeId());
			// 唯一性
			if (BeanUtils.isNotNull(queryProduct) && rolePrivilege.getId() == 0)
			{

				logger.error("saveRolePrivilege failed, rolePrivilege [" + rolePrivilege.toString() + "]");
				throw new RpcException(ErrorCodeConst.ErrorCode10404);
			}
			else
			{
				rolePrivilege = rolePrivilegeDao.save(rolePrivilege);
			}

			logger.debug("saveRolePrivilege end, rolePrivilege [" + rolePrivilege.toString() + "]");
			return rolePrivilege;
		}
		catch (RpcException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			logger.error("saveRolePrivilege failed, rolePrivilege [" + rolePrivilege.toString() + "]");
			logger.error(e.getLocalizedMessage(), e);
			throw new RpcException(ErrorCodeConst.ErrorCode99998);
		}
	}

	@Override
	public RolePrivilege queryRolePrivilege(long id) {
		logger.debug("queryRolePrivilege start, id [" + id + "]");
		try
		{
			RolePrivilege user = rolePrivilegeDao.findOne(id);
			logger.debug("queryRolePrivilege end, id [" + id + "]");
			return user;
		}
		catch (Exception e)
		{
			logger.error("queryRolePrivilege error, id [" + id + "]");
			logger.error(e.getLocalizedMessage(), e);
			throw new RpcException(ErrorCodeConst.ErrorCode99998);
		}
	}

	@Override
	public List<RolePrivilege> queryRolePrivilegeByRoleList(List<Role> roleList) {
		logger.debug("queryRolePrivilegeByRoleList start, roleList [" + roleList.toString() + "]");
		try
		{
			List<RolePrivilege> roleMenus = new ArrayList<RolePrivilege>(); 
			for(Role role : roleList){
				List<RolePrivilege> roleMenuList = rolePrivilegeDao.queryRolePrivilegeByRoleId(role.getId());
				for(RolePrivilege roleMenu : roleMenuList){
					roleMenus.add(roleMenu);
				}
			}
			
			logger.debug("queryRolePrivilegeByRoleList end, roleList [" +roleList.toString() + "]");
			return roleMenus;
		}
		catch (Exception e)
		{
			logger.error("queryRolePrivilegeByRoleList error, roleList [" +roleList.toString() + "]");
			logger.error(e.getLocalizedMessage(), e);
			throw new RpcException(ErrorCodeConst.ErrorCode99998);
		}
	}

	@Override
	public List<RolePrivilege> queryRolePrivilegeByPrivilegeId(long privilege) {
		logger.debug("queryRolePrivilegeByPrivilegeId start, privilege [" + privilege + "]");
		try
		{
			List<RolePrivilege> user = rolePrivilegeDao.queryRolePrivilegeByPrivilegeId(privilege);
			logger.debug("queryRolePrivilegeByPrivilegeId end, privilege [" + privilege + "]");
			return user;
		}
		catch (Exception e)
		{
			logger.error("queryRolePrivilegeByPrivilegeId error, privilege [" + privilege + "]");
			logger.error(e.getLocalizedMessage(), e);
			throw new RpcException(ErrorCodeConst.ErrorCode99998);
		}
	}

	@Override
	public void saveRolePrivilegeList(String privilegestr, long roleId) {
		logger.debug("saveRolePrivilegeList start: privilegestr,roleId [" + privilegestr + roleId+"]");
		try
		{
//			roleMenuDao.deleteRoleMenuByRoleId(roleId);
			jdbcTemplate.execute("delete from t_role_privilege t where t.role_id = " + roleId);
			String[] privilegeIdArray = privilegestr.split(Constant.StringSplitUtil.DECODE);
			List<RolePrivilege> rolePrivilegeList = new ArrayList<RolePrivilege>();
			for(String privilegeId : privilegeIdArray){
				privilegeId = privilegeId.replace(",", StringUtil.initString());
				long id = Long.valueOf(privilegeId);
				RolePrivilege rolePrivilege = new RolePrivilege();
				rolePrivilege.setPrivilegeId(id);
				rolePrivilege.setRoleId(roleId);
				rolePrivilege.setStatus(CommonConstant.CommonStatus.OPEN);
				rolePrivilegeList.add(rolePrivilege);
			}
			rolePrivilegeDao.save(rolePrivilegeList);
			
		}
		catch (Exception e)
		{
			logger.error("saveRolePrivilegeList error: privilegestr,roleId [" + privilegestr +roleId+ "]");
			logger.error(e.getLocalizedMessage(), e);
			throw new RpcException(ErrorCodeConst.ErrorCode99998);
		}

	}

	@Override
	public RolePrivilegeVO copyPropertiesToVO(RolePrivilege temp) {
		logger.debug("copyPropertiesToVO start");
		try
		{
			RolePrivilegeVO vo = new RolePrivilegeVO();
			vo.setId(temp.getId());
			vo.setPrivilegeId(temp.getPrivilegeId());
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
