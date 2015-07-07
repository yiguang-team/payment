package com.yiguang.payment.payment.service;

import java.util.List;

import com.yiguang.payment.common.query.YcPage;
import com.yiguang.payment.payment.entity.MerchantRejection;
import com.yiguang.payment.payment.vo.MerchantRejectionVO;

public interface MerchantRejectionService
{

	public YcPage<MerchantRejectionVO> queryMerchantRejectionList(MerchantRejectionVO conditionVO, int pageNumber,
			int pageSize, String sortType);

	public String deleteMerchantRejection(MerchantRejection merchantRejection);
	 
	public MerchantRejection updateMerchantRejection(MerchantRejection merchantRejection);

	public MerchantRejection saveMerchantRejection(MerchantRejection merchantRejection);

	public MerchantRejection queryMerchantRejection(long id);

	public MerchantRejection queryMerchantRejectionByMerchant(long merchantAId, long merchantBId);

	public List<MerchantRejection> queryMerchantRejectionByMerchantB(long merchantBId);

	public MerchantRejectionVO copyPropertiesToVO(MerchantRejection temp);
}
