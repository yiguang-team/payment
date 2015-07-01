package com.yiguang.payment.payment.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.yiguang.payment.payment.entity.WhiteList;

public interface WhiteListDao extends PagingAndSortingRepository<WhiteList, Long>, JpaSpecificationExecutor<WhiteList>
{
	@Query("select cpc from WhiteList cpc where cpc.type=:type and cpc.value=:value and cpc.status=1")
	public WhiteList queryWhiteListByALL(@Param("type") int type, @Param("value") String value);
}
