package com.yiguang.payment.rbac.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.yiguang.payment.rbac.entity.Privilege;

public interface PrivilegeDao extends PagingAndSortingRepository<Privilege, Long>, JpaSpecificationExecutor<Privilege>{
	@Query("select m from Privilege m where m.name=:name")
	public Privilege queryPrivilegeByName(@Param("name") String name);
	@Query("select m from Privilege m where m.url=:url")
	public Privilege queryPrivilegeByUrl(@Param("url") String url);


}
