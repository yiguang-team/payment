package com.yiguang.payment.depot.order.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.yiguang.payment.depot.order.entity.DepotOrder;

public interface DepotOrderDAO extends PagingAndSortingRepository<DepotOrder, Long>,
		JpaSpecificationExecutor<DepotOrder>
{

	@Query("select d from DepotOrder d where d.orderId=:orderId and d.extractUser=:merchantId")
	public List<DepotOrder> queryDepotOrderByContion(@Param("orderId") String orderId,
			@Param("merchantId") String merchantId);

}
