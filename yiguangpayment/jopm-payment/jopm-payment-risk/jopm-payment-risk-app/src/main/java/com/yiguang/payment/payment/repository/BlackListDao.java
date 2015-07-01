package com.yiguang.payment.payment.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import com.yiguang.payment.payment.entity.BlackList;

public interface BlackListDao extends PagingAndSortingRepository<BlackList, Long>, JpaSpecificationExecutor<BlackList>
{
	@Query("select cpc from BlackList cpc where cpc.type=:type and cpc.value=:value and cpc.status=1")
	public BlackList queryBlackListByALL(@Param("type") int type, @Param("value") String value);
}
