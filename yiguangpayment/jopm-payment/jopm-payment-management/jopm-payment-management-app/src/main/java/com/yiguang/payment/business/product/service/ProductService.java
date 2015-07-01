package com.yiguang.payment.business.product.service;

import java.util.List;
import java.util.Map;

import com.yiguang.payment.business.product.entity.Product;
import com.yiguang.payment.business.product.vo.ProductVO;
import com.yiguang.payment.common.query.YcPage;

public interface ProductService
{
	public YcPage<ProductVO> queryProductList(Map<String, Object> searchParams, int pageNumber, int pageSize,
			String sortType);

	public Product updateProductStatus(Product cf);

	public String deleteProduct(Product cf);

	public Product saveProduct(Product cf);

	public Product queryProduct(long id);

	public ProductVO copyPropertiesToVO(Product temp);

	public Product queryProductBySupplierIdAndId(long supplierId, long productId);
	
	public List<Product> queryProductBySupplierId(long supplierId);
}
