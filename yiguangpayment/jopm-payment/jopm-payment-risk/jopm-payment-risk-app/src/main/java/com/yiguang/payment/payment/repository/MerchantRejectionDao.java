package com.yiguang.payment.payment.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.yiguang.payment.payment.entity.MerchantRejection;

public interface MerchantRejectionDao extends PagingAndSortingRepository<MerchantRejection, Long>,
		JpaSpecificationExecutor<MerchantRejection>
{
	@Query("select bt from MerchantRejection bt where bt.merchantA=:merchantA and bt.merchantB=:merchantB")
	public MerchantRejection queryMerchantRejectionByMerchant(@Param("merchantA") long merchantA,
			@Param("merchantB") long merchantB);

	@Query("select bt from MerchantRejection bt where bt.merchantB=:merchantB")
	public List<MerchantRejection> queryMerchantRejectionByMerchantB(@Param("merchantB") long merchantB);

}
