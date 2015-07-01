package com.yiguang.payment.payment.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.yiguang.payment.payment.entity.CarrierChannelRelation;

public interface CarrierChannelRelationDao extends
		PagingAndSortingRepository<CarrierChannelRelation, Long>,
		JpaSpecificationExecutor<CarrierChannelRelation> {

	@Query("select cpc from CarrierChannelRelation cpc where cpc.channelId=:channelId")
	public List<CarrierChannelRelation> queryCarrierChannelRelationByChannelId(@Param("channelId") long channelId);

	@Query("select cpc from CarrierChannelRelation cpc where cpc.carrierId=:carrierId")
	public List<CarrierChannelRelation> queryCarrierChannelRelationByCarrierId(@Param("carrierId") long carrierId);
	
	@Query("select cpc from CarrierChannelRelation cpc where cpc.channelId=:channelId and cpc.carrierId=:carrierId")
	public CarrierChannelRelation queryCarrierChannelRelationByAll(@Param("carrierId") long carrierId,
																				@Param("channelId") long channelId);

	@Query("delete from CarrierChannelRelation cpc where cpc.channelId=:channelId")
	public void deleteAllByChannelId(@Param("channelId") long channelId);
}
