package com.yiguang.payment.common.datasource.service;

import java.util.List;

import com.yiguang.payment.common.datasource.vo.OptionVO;

public interface DataSourceService
{
	public final String DS_OPEN = "OPEN";
	public final String DS_ALL = "ALL";
	
	List<OptionVO> findOpenOptions(String dataSourceCode);

	List<OptionVO> findOpenOptionsWithoutPlease(String dataSourceCode);

	List<OptionVO> findAllOptions(String dataSourceCode);

	List<OptionVO> findAllOptionsWithoutPlease(String dataSourceCode);

	OptionVO findOptionVOById(String dataSourceCode, String id);

	List<OptionVO> findOpenOptionsByParent(String dataSourceCode, String parentId);

	List<OptionVO> findAllOptionsByParent(String dataSourceCode, String parentId);

	List<OptionVO> findOpenOptionsByParentWithoutPlease(String dataSourceCode, String parentId);

	List<OptionVO> findAllOptionsByParentWithoutPlease(String dataSourceCode, String parentId);
}
