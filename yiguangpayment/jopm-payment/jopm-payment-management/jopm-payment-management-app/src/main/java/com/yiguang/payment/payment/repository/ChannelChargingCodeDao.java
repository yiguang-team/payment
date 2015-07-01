package com.yiguang.payment.payment.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.yiguang.payment.payment.entity.ChannelChargingCode;

public interface ChannelChargingCodeDao extends PagingAndSortingRepository<ChannelChargingCode, Long>,
		JpaSpecificationExecutor<ChannelChargingCode>
{

	@Query("select c from ChannelChargingCode c where c.id=:id")
	public ChannelChargingCode queryChargingCodeById(@Param("id") long id);

	@Query("select c from ChannelChargingCode c where c.channelId=:channelId and c.chargingAmount=:chargingAmount")
	public ChannelChargingCode queryChargingCodeByChannelIdAndAmount(@Param("channelId") long channelId,
			@Param("chargingAmount") BigDecimal chargingAmount);

	@Query("select c from ChannelChargingCode c where c.chargingCode=:chargingCode")
	public ChannelChargingCode queryChargingCodeByCode(@Param("chargingCode") String chargingCode);

	@Query("select c from ChannelChargingCode c where c.channelId=:channelId")
	public List<ChannelChargingCode> queryChargingCodeByChannelId(@Param("channelId") long channelId);
}
