package com.yiguang.payment.payment.order.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.yiguang.payment.payment.order.entity.MerchantOrder;

/**
 * 商品订单表数据层
 * 
 * @author Shinalon
 * 
 */
public interface MerchantOrderDao extends PagingAndSortingRepository<MerchantOrder, Long>,
		JpaSpecificationExecutor<MerchantOrder>
{
	@Query("select cpc from MerchantOrder cpc where cpc.orderId=:orderId")
	public MerchantOrder queryMerchantOrderByOrderId(@Param("orderId") String orderId);
	@Query("select cpc from MerchantOrder cpc where cpc.merchantOrderId=:merchantOrderId and cpc.merchantId=:merchantId")
	public MerchantOrder queryMerchantOrderByMerchantOrderId(@Param("merchantOrderId") String merchantOrderId,@Param("merchantId") long merchantId );
	@Query("select cpc from MerchantOrder cpc where cpc.requestTime<:requestTime and cpc.payStatus =:payStatus")
	public List<MerchantOrder> queryMerchantOrderListByStatus(@Param("requestTime") Date requestTime,@Param("payStatus") int payStatus);

}
