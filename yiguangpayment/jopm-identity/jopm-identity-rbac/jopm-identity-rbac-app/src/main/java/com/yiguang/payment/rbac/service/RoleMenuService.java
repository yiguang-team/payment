package com.yiguang.payment.rbac.service;

import java.util.List;

import com.yiguang.payment.rbac.entity.Role;
import com.yiguang.payment.rbac.entity.RoleMenu;
import com.yiguang.payment.rbac.vo.RoleMenuVO;

public interface RoleMenuService
{
//	public YcPage<RoleMenuVO> queryRoleMenuList(Map<String, Object> searchParams, int pageNumber, int pageSize,
//			String sortType);

	public RoleMenu updateRoleMenuStatus(RoleMenu roleMenu);

	public String deleteRoleMenu(RoleMenu roleMenu);

	public RoleMenu saveRoleMenu(RoleMenu roleMenu);

	public RoleMenu queryRoleMenu(long id);

	public List<RoleMenu> queryRoleMenuByRoleList(List<Role> roleList);
	
	public List<RoleMenu> queryRoleMenuByMenuId(long menuId);
	
	public void saveRoleMenuList(String menuidstr,long roleId);
	
	public RoleMenuVO copyPropertiesToVO(RoleMenu temp);
}
