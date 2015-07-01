package com.yiguang.payment.payment.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.yiguang.payment.payment.entity.Channel;

/**
 * 支付渠道表数据层
 * 
 * @author Shinalon
 * 
 */
public interface ChannelDao extends PagingAndSortingRepository<Channel, Long>, JpaSpecificationExecutor<Channel> {

	@Query("select bt from Channel bt where bt.name=:name")
	public Channel queryChannelByName(@Param("name") String name);
	
}
