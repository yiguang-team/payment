package com.yiguang.payment.business.product.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.yiguang.payment.business.product.entity.Product;

public interface ProductDao extends PagingAndSortingRepository<Product, Long>, JpaSpecificationExecutor<Product>
{
	@Query("select cpc from Product cpc where cpc.name=:name")
	public Product queryProductByName(@Param("name") String name);

	@Query("select cpc from Product cpc where cpc.merchantId=:merchantId and cpc.id=:id")
	public Product queryProductBySupplierIdAndId(@Param("merchantId") long merchantId, @Param("id") long id);

	@Query("select cpc from Product cpc where cpc.merchantId=:merchantId")
	public List<Product> queryProductBySupplierId(@Param("merchantId") long merchantId);
}
