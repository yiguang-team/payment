package com.yiguang.payment.rbac.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.yiguang.payment.rbac.entity.RolePrivilege;

public interface RolePrivilegeDao extends PagingAndSortingRepository<RolePrivilege, Long>, JpaSpecificationExecutor<RolePrivilege>{

	@Query("select r from RolePrivilege r where r.roleId=:roleId")
	public List<RolePrivilege> queryRolePrivilegeByRoleId(@Param("roleId") long roleId);
	
	@Query("select r from RolePrivilege r where r.privilegeId=:privilegeId")
	public List<RolePrivilege> queryRolePrivilegeByPrivilegeId(@Param("privilegeId") long privilegeId);

	@Query("select r from RolePrivilege r where r.roleId=:roleId and  r.privilegeId=:privilegeId")
	public RolePrivilege queryRolePrivilegeByPrivilegeIdandMenuId(@Param("roleId") long roleId,
														@Param("privilegeId") long privilegeId);
}
