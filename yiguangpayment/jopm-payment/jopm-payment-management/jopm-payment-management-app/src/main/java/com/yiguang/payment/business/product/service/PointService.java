package com.yiguang.payment.business.product.service;

import java.math.BigDecimal;
import java.util.List;

import com.yiguang.payment.business.product.entity.Point;
import com.yiguang.payment.business.product.vo.PointVO;
import com.yiguang.payment.common.query.YcPage;

public interface PointService
{
	public YcPage<PointVO> queryPointList(int pageNumber, int pageSize, String sortType, long channelId, String name,
			long supplierId, long productId, String provinceId, String cityId, int chargingType, int status);

	public Point updatePointStatus(Point cf);

	public String deletePoint(Point cf);

	public Point savePoint(Point cf);

	public Point savePoint(Point cf, String pointChannelRelationIDs);

	public Point queryPoint(long id);

	public PointVO copyPropertiesToVO(Point temp);

	public Point queryPointByProductIdAndFaceAmt(long productId, BigDecimal faceAmt);
	
	public List<Point> queryPointByProductId(long productId);
	
	public List<Point> queryPointBySupplierId(long supplierId);
	
	public List<Point> queryPointByMerAndCh(long supplierId, long channelId);

	public Point queryPointByChargingCode(String chargingCode);
}
