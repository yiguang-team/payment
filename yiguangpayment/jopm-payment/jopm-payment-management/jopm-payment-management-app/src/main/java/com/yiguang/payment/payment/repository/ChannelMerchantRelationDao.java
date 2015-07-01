package com.yiguang.payment.payment.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.yiguang.payment.payment.entity.ChannelMerchantRelation;

public interface ChannelMerchantRelationDao extends PagingAndSortingRepository<ChannelMerchantRelation, Long>,
		JpaSpecificationExecutor<ChannelMerchantRelation>
{

	@Query("select cpc from ChannelMerchantRelation cpc where cpc.merchantId=:merchantId")
	public List<ChannelMerchantRelation> queryChannelMerchantRelationByMerchantId(@Param("merchantId") long merchantId);

	@Query("select cpc from ChannelMerchantRelation cpc where cpc.channelId=:channelId")
	public List<ChannelMerchantRelation> queryChannelMerchantRelationByChannelId(@Param("channelId") long channelId);
	
	@Query("select cpc from ChannelMerchantRelation cpc where cpc.channelId=:channelId and cpc.merchantId=:merchantId")
	public ChannelMerchantRelation queryChannelMerchantRelationByCidAndMerId(@Param("channelId") long channelId,
																				@Param("merchantId") long merchantId);

	@Query("delete from ChannelMerchantRelation cpc where cpc.merchantId=:merchantId")
	public void deleteAllByMerchantId(@Param("merchantId") long merchantId);
}
