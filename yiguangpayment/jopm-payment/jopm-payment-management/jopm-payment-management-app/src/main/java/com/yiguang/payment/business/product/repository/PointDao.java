package com.yiguang.payment.business.product.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.yiguang.payment.business.product.entity.Point;

public interface PointDao extends PagingAndSortingRepository<Point, Long>, JpaSpecificationExecutor<Point>
{
	@Query("select p from Point p where p.name=:name")
	public Point queryPointByName(@Param("name") String name);

	@Query("select p from Point p where  p.merchantId=:merchantId and p.productId=:productId and p.provinceId=:provinceId and p.cityId=:cityId and p.faceAmount=:faceAmount and p.chargingType=:chargingType")
	public Point queryPointByProps(@Param("merchantId") long merchantId, @Param("productId") long productId,
			@Param("provinceId") String provinceId, @Param("cityId") String cityId,
			@Param("faceAmount") BigDecimal faceAmount, @Param("chargingType") int chargingType);

	@Query("select p from Point p where p.productId=:productId and p.faceAmount=:faceAmount and p.chargingType = '2'")
	public Point queryPointByProductIdAndFaceAmt(@Param("productId") long productId,
			@Param("faceAmount") BigDecimal faceAmount);

	@Query("select p from Point p where p.merchantId=:merchantId")
	public List<Point> queryPointBySupplierId(@Param("merchantId") long supplierId);

	@Query("select p from Point p where p.productId=:productId order by p.payAmount")
	public List<Point> queryPointByProductId(@Param("productId") long productId);
	
	@Query("select p from Point p where p.chargingCode=:chargingCode")
	public Point queryPointByChargingCode(@Param("chargingCode") String chargingCode);
	
}
