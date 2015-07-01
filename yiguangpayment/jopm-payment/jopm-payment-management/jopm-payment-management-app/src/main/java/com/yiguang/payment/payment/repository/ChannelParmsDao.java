package com.yiguang.payment.payment.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.yiguang.payment.payment.entity.ChannelParms;

public interface ChannelParmsDao extends PagingAndSortingRepository<ChannelParms, Long>, JpaSpecificationExecutor<ChannelParms>{
	@Query("select bt from ChannelParms bt where bt.channelId=:channelId and bt.key=:key")
	public ChannelParms queryChannelByChannelIdAndKey(@Param("channelId") long channelId,
												@Param("key") String key);
}
