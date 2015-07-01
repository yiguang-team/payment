package com.yiguang.payment.payment.risk.service;

public interface RiskService
{
	public void checkMerchantRejection(String mobile, String merchantB);

	public long checkBasicRule(long channelId,String provinceId,String cityId,long merchantId,long productId,long pointId,String mobile,String ip,String username, long amount);

	public void checkBlackList(String mobile, String IP, String user);

	public boolean checkWhiteList(String mobile, String IP, String user);

}
