package com.yiguang.payment.payment.service;

import java.util.List;

import com.yiguang.payment.common.query.YcPage;
import com.yiguang.payment.payment.entity.Merchant;
import com.yiguang.payment.payment.vo.MerchantVO;

public interface MerchantService
{
	public YcPage<MerchantVO> queryMerchantList(MerchantVO conditionVO, int pageNumber, int pageSize,
			String sortType);

	public Merchant updateMerchantStatus(Merchant merchant);

	public String deleteMerchant(Merchant merchant);

	public Merchant saveMerchant(Merchant merchant);

	public Merchant saveMerchant(Merchant merchant, String channelMerchantRelationIDs);

	public Merchant queryMerchant(long id);
	
	public Merchant queryMerchantByUserId(long adminUser);
	
	public List<Merchant> queryMerchantByChannelId(long channelId);

	public MerchantVO copyPropertiesToVO(Merchant temp);
	
	public List<Merchant> findAll();

}
