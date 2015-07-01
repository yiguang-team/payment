package com.yiguang.payment.depot.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.yiguang.payment.depot.entity.ProductBatch;

public interface ProductBatchDao extends PagingAndSortingRepository<ProductBatch, Long>,
		JpaSpecificationExecutor<ProductBatch>
{
	@Query("select p from ProductBatch p where p.batchId=:batchId")
	public ProductBatch queryProductBatchByBatchId(@Param("batchId") String batchId);

	@Query("select p from ProductBatch p where p.batchId=:batchId and p.merchantId=:merchantId and p.productId=:productId")
	public ProductBatch queryProductBatchByOther(@Param("batchId") String batchId, @Param("merchantId") long merchantId,
			@Param("productId") long productId);

	@Query("select p from ProductBatch p where p.batchId=:batchId")
	public List<ProductBatch> findBatchsByBatchId(@Param("batchId") String batchId);
}
