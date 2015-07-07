package com.yiguang.payment.payment.service;

import java.math.BigDecimal;
import java.util.List;

import com.yiguang.payment.common.query.YcPage;
import com.yiguang.payment.payment.entity.ChannelChargingCode;
import com.yiguang.payment.payment.vo.ChannelChargingCodeVO;

public interface ChannelChargingCodeService
{

	public ChannelChargingCode updateChargingCodeStatus(ChannelChargingCode chargingCode);

	public String deleteChargingCode(ChannelChargingCode chargingCode);

	public ChannelChargingCode saveChargingCode(ChannelChargingCode chargingCode);

	public ChannelChargingCode queryChargingCode(long id);

	public YcPage<ChannelChargingCodeVO> queryChargingCodeList(ChannelChargingCodeVO conditionVO, int pageNumber,
			int pageSize, String sortType);

	public ChannelChargingCodeVO copyPropertiesToVO(ChannelChargingCode temp);

	public ChannelChargingCode queryChargingCodeByChannelIdAndAmount(long channelId, BigDecimal chargingAmount);

	public ChannelChargingCode queryChargingCodeByCode(String chargingCode);
	
	public List<ChannelChargingCode> queryChargingCodeByChannelId(long channelId);
}
