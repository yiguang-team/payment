package com.yiguang.payment.depot.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.yiguang.payment.depot.entity.ProductDepot;

public interface ProductDepotDao extends PagingAndSortingRepository<ProductDepot, Long>,
		JpaSpecificationExecutor<ProductDepot>
{
	@Query("select p from ProductDepot p where p.cardId=:cardId and p.productId=:productId")
	public ProductDepot queryProductDepotByCardId(@Param("cardId") String cardId, @Param("productId") long productId);

	@Query("select p from ProductDepot p where p.batchId=:batchId")
	public List<ProductDepot> findProductDepotByBatchId(@Param("batchId") String batchId);

	@Query("select count(p) from ProductDepot p where p.point.id=:pointId and p.status = '2'")
	public int getUsableCount(@Param("pointId") long pointId);

	// 取得请求条数待售状态的卡密信息
	// 1：未销售 2：待销售 3：已销售
	@Query("select productDepot from ProductDepot productDepot where productDepot.point.id =:pointid and productDepot.status = '2' and rownum <= :count")
	public List<ProductDepot> queryCardPwdByPointid(@Param("pointid") long pointid, @Param("count") long count);

	// 根据历史表id查询销售过的卡密信息
	// 1：未销售 2：待销售 3：已销售
	@Query("select productDepot from ProductDepot productDepot where productDepot.extractNo =:extractNo and productDepot.status = '3' ")
	public List<ProductDepot> queryCardPwdByExtractNo(@Param("extractNo") String extractNo);

}
