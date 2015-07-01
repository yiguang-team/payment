package com.yiguang.payment.rbac.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import com.yiguang.payment.rbac.entity.RoleUser;

public interface RoleUserDao extends PagingAndSortingRepository<RoleUser, Long>, JpaSpecificationExecutor<RoleUser>{
	@Query("select r from RoleUser r where r.roleId=:roleId")
	public List<RoleUser> queryRoleUserByRoleId(@Param("roleId") long roleId);

	@Query("select r from RoleUser r where r.roleId=:roleId and r.userId=:userId")
	public RoleUser queryRoleUserByRoleIdandUserId(@Param("roleId") long roleId,
														@Param("userId") long userId);
	
	@Query("select r from RoleUser r where r.userId=:userId")
	public List<RoleUser> queryRoleUserByUserId(@Param("userId") long userId);
}
