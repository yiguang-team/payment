package com.yiguang.payment.rbac.service;

import com.yiguang.payment.common.query.YcPage;
import com.yiguang.payment.rbac.entity.Role;
import com.yiguang.payment.rbac.vo.RoleVO;

public interface RoleService
{
	public YcPage<RoleVO> queryRoleList(RoleVO conditionVO, int pageNumber, int pageSize,
			String sortType);

	public Role updateRoleStatus(Role role);

	public String deleteRole(Role role);

	public Role saveRole(Role role);

	public Role queryRole(long id);
	
//	public Role queryRoleByUserId(long userId);

	public RoleVO copyPropertiesToVO(Role role);

}
