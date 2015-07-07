package com.yiguang.payment.depot.service;

import java.util.List;

import com.yiguang.payment.common.query.YcPage;
import com.yiguang.payment.depot.entity.ProductDepot;
import com.yiguang.payment.depot.vo.ProductDepotVO;

public interface ProductDepotService
{
	public void importProductDepot(String batchId, String config, long carrierId, long productId, String totalAmt,
			long totalNum, String[][] excelData);

	public YcPage<ProductDepotVO> queryProductDepotList(ProductDepotVO conditionVO, int pageNumber, int pageSize,
			String sortType);

	public ProductDepot updateProductDepotStatus(ProductDepot cf);

	public String deleteProductDepot(ProductDepot cf);

	public ProductDepot saveProductDepot(ProductDepot cf);

	public ProductDepot queryProductDepot(long id);

	public ProductDepotVO copyPropertiesToVO(ProductDepot temp);

	public int getUsableCount(long pointId);

	public List<ProductDepot> queryCardPwdByPointid(long pointid, int count);

	public List<ProductDepot> queryCardPwdByExtractNo(String extractNo);
}
