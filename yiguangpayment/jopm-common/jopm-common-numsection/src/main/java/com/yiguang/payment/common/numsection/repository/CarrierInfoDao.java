package com.yiguang.payment.common.numsection.repository;

/**
 * 运营商表数据层
 * 
 * @author Jinger
 * @date：2013-10-15
 */
import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.yiguang.payment.common.numsection.entity.CarrierInfo;

public interface CarrierInfoDao extends PagingAndSortingRepository<CarrierInfo, Long>,
		JpaSpecificationExecutor<CarrierInfo>
{
	@Query("select c from CarrierInfo c")
	List<CarrierInfo> selectAll();

	@Query("select c from CarrierInfo c where c.carrierName=:carrierName")
	CarrierInfo getByName(@Param("carrierName") String carrierName);

}
