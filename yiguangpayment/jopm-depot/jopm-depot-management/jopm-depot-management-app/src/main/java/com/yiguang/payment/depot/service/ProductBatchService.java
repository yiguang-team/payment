package com.yiguang.payment.depot.service;

import java.util.Map;

import com.yiguang.payment.common.query.YcPage;
import com.yiguang.payment.depot.entity.ProductBatch;
import com.yiguang.payment.depot.vo.ProductBatchVO;

public interface ProductBatchService
{
	public String generateBatchId();

	public YcPage<ProductBatchVO> queryProductBatchList(Map<String, Object> searchParams, int pageNumber, int pageSize,
			String sortType);

	public ProductBatch updateProductBatchStatus(ProductBatch cf);

	public String deleteProductBatch(ProductBatch cf);

	public ProductBatch saveProductBatch(ProductBatch cf);

	public ProductBatch queryProductBatch(long id);

	public ProductBatchVO copyPropertiesToVO(ProductBatch temp);

	public void updateBatchStatus(int tag, String batchId);
}
