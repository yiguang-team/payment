package com.yiguang.payment.payment.service;

import java.util.Map;

import com.yiguang.payment.common.query.YcPage;
import com.yiguang.payment.payment.entity.BlackList;
import com.yiguang.payment.payment.vo.BlackListVO;

public interface BlackListService
{
	public YcPage<BlackListVO> queryBlackList(Map<String, Object> searchParams, int pageNumber, int pageSize,
			String sortType);

	public String deleteBlackList(BlackList blackList);
	 
	public BlackList updateBlackListStatus(BlackList blackList);

	public BlackList saveBlackList(BlackList blackList);

	public BlackList queryBlackListById(long id);

	public BlackList queryBlackListByAll(int type, String value);

	public BlackListVO copyPropertiesToVO(BlackList temp);
}
