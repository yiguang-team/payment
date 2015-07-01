package com.yiguang.payment.rbac.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.yiguang.payment.rbac.entity.Role;

public interface RoleDao extends PagingAndSortingRepository<Role, Long>, JpaSpecificationExecutor<Role> {

	@Query("select r from Role r where r.roleName=:roleName")
	public Role queryRoleByName(@Param("roleName") String roleName);
	
	
}
