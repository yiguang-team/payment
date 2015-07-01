package com.yiguang.payment.business.product.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.yiguang.payment.business.product.entity.PointChannelRelation;

public interface PointChannelRelationDao extends PagingAndSortingRepository<PointChannelRelation, Long>,
		JpaSpecificationExecutor<PointChannelRelation>
{
	@Query("select cpc from PointChannelRelation cpc where cpc.pointId=:pointId")
	public List<PointChannelRelation> queryPointChannelRelationByPointId(@Param("pointId") long pointId);

	@Query("select cpc from PointChannelRelation cpc where cpc.channelId=:channelId")
	public List<PointChannelRelation> queryPointChannelRelationByChannelId(@Param("channelId") long channelId);

	@Query("select cpc from PointChannelRelation cpc where cpc.pointId=:pointId and cpc.channelId=:channelId")
	public PointChannelRelation queryPointChannelRelationByPointIdAndChannelId(@Param("pointId") long pointId,
			@Param("channelId") long channelId);

}
