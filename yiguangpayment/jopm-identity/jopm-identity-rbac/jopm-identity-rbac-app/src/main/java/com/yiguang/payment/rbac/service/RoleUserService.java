package com.yiguang.payment.rbac.service;

import java.util.List;

import com.yiguang.payment.rbac.entity.RoleUser;
import com.yiguang.payment.rbac.vo.RoleUserVO;

public interface RoleUserService {
//	public YcPage<RoleUserVO> queryRoleUserList(Map<String, Object> searchParams, int pageNumber, int pageSize,
//			String sortType);

	public RoleUser updateRoleUserStatus(RoleUser roleUser);

	public String deleteRoleUser(RoleUser roleUser);

	public RoleUser saveRoleUser(RoleUser roleUser);
	
	public void saveRoleUser(RoleUser roleUser, String roleUserIds);

	public RoleUser queryRoleUser(long id);

	public List<RoleUser> queryRoleUserByRole(long roleId);
	
	public RoleUserVO copyPropertiesToVO(RoleUser temp);
	
	public List<RoleUser> queryRoleUserByUserId(long userId);
}
