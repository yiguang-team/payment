package com.yiguang.payment.payment.service;

import java.util.List;

import com.yiguang.payment.common.query.YcPage;
import com.yiguang.payment.payment.entity.BasicRule;
import com.yiguang.payment.payment.vo.BasicRuleVO;

public interface BasicRuleService
{
	public YcPage<BasicRuleVO> queryBasicRuleList(BasicRuleVO conditionVO, int pageNumber,
			int pageSize, String sortType);

	public BasicRule updateBasicRuleStatus(BasicRule BasicRule);

	public String deleteBasicRule(BasicRule basicRule);
	
	public BasicRule saveBasicRule(BasicRule BasicRule);

	public BasicRule queryBasicRule(long id);

	public BasicRuleVO copyPropertiesToVO(BasicRule temp);
	
	public List<BasicRule> queryListByParms(long channelId,String provinceId,String cityId,long merchantId,long productId,long pointId,String mobile,String ip,String username);

}
