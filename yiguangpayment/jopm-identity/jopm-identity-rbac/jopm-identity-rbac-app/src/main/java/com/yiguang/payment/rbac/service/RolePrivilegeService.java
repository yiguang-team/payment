package com.yiguang.payment.rbac.service;

import java.util.List;

import com.yiguang.payment.rbac.entity.Role;
import com.yiguang.payment.rbac.entity.RolePrivilege;
import com.yiguang.payment.rbac.vo.RolePrivilegeVO;

public interface RolePrivilegeService {
//	public YcPage<RolePrivilegeVO> queryRolePrivilegeList(Map<String, Object> searchParams, int pageNumber, int pageSize,
//			String sortType);

	public RolePrivilege updateRolePrivilegeStatus(RolePrivilege rolePrivilege);

	public String deleteRolePrivilege(RolePrivilege rolePrivilege);

	public RolePrivilege saveRolePrivilege(RolePrivilege rolePrivilege);

	public RolePrivilege queryRolePrivilege(long id);

	public List<RolePrivilege> queryRolePrivilegeByRoleList(List<Role> roleList);
	
	public List<RolePrivilege> queryRolePrivilegeByPrivilegeId(long privilege);
	
	public void saveRolePrivilegeList(String privilegestr,long roleId);
	
	public RolePrivilegeVO copyPropertiesToVO(RolePrivilege temp);
}
