package com.yiguang.payment.rbac.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.yiguang.payment.rbac.entity.RoleMenu;

public interface RoleMenuDao extends PagingAndSortingRepository<RoleMenu, Long>, JpaSpecificationExecutor<RoleMenu> {

	@Query("select r from RoleMenu r where r.roleId=:roleId")
	public List<RoleMenu> queryRoleMenuByRoleId(@Param("roleId") long roleId);
	
	@Query("select r from RoleMenu r where r.menuId=:menuId")
	public List<RoleMenu> queryRoleMenuByMenuId(@Param("menuId") long menuId);

	@Query("select r from RoleMenu r where r.roleId=:roleId and  r.menuId=:menuId")
	public RoleMenu queryRoleMenuByRoleIdandMenuId(@Param("roleId") long roleId,
														@Param("menuId") long menuId);
}
