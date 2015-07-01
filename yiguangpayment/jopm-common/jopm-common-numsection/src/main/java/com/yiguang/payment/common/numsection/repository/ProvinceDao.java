package com.yiguang.payment.common.numsection.repository;

/**
 * 省份表数据层
 * 
 * @author Jinger
 * @date：2013-10-15
 */
import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.yiguang.payment.common.numsection.entity.City;
import com.yiguang.payment.common.numsection.entity.Province;

public interface ProvinceDao extends PagingAndSortingRepository<Province, String>, JpaSpecificationExecutor<Province>
{
	@Query("select p from Province p")
	List<Province> selectAll();

	@Query("select c from Province c where c.provinceName=:provinceName")
	Province getByName(@Param("provinceName") String provinceName);
}
