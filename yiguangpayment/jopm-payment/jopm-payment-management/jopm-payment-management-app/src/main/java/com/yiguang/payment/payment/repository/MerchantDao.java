package com.yiguang.payment.payment.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.yiguang.payment.payment.entity.Merchant;

public interface MerchantDao extends PagingAndSortingRepository<Merchant, Long>, JpaSpecificationExecutor<Merchant>
{
	@Query("select cpc from Merchant cpc where cpc.name=:name")
	public Merchant queryMerchantByName(@Param("name") String name);

	@Query("select cpc from Merchant cpc where cpc.adminUser=:adminUser")
	public Merchant queryMerchantByUserId(@Param("adminUser") long adminUser);
}
