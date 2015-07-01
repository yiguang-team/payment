package com.yiguang.payment.payment.service;

import java.util.Map;

import com.yiguang.payment.common.query.YcPage;
import com.yiguang.payment.payment.entity.WhiteList;
import com.yiguang.payment.payment.vo.WhiteListVO;

public interface WhiteListService
{
	public YcPage<WhiteListVO> queryWhiteList(Map<String, Object> searchParams, int pageNumber, int pageSize,
			String sortType);

	public String deleteWhiteList(WhiteList blackList);
	
	public WhiteList updateWhiteListStatus(WhiteList whiteList);

	public WhiteList saveWhiteList(WhiteList whiteList);

	public WhiteList queryWhiteList(long id);

	public WhiteList queryWhiteListByAll(int type, String value);

	public WhiteListVO copyPropertiesToVO(WhiteList temp);
}
