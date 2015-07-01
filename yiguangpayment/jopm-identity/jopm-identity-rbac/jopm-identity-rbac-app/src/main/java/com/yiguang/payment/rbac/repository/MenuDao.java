package com.yiguang.payment.rbac.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.yiguang.payment.rbac.entity.Menu;

public interface MenuDao extends PagingAndSortingRepository<Menu, Long>, JpaSpecificationExecutor<Menu> {

	@Query("select m from Menu m where m.menuName=:menuName")
	public Menu queryMenuByName(@Param("menuName") String menuName);
	@Query("select m from Menu m where m.url=:url")
	public Menu queryMenuByUrl(@Param("url") String url);
}
