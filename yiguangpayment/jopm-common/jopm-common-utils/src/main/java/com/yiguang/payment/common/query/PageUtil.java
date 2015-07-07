package com.yiguang.payment.common.query;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public class PageUtil
{

	/**
	 * 页查询通用方法
	 * 
	 * @param dao
	 *            实现了JpaSpecificationExecutor 接口的Dao对象
	 * @param searchParams
	 *            查询条件
	 * @param pageNumber
	 *            当前页码
	 * @param pageSize
	 *            每页条数
	 * @param sort
	 *            排序规则
	 * @return
	 */

	public static <T> Page<T> queryPage(JpaSpecificationExecutor<T> dao, Specification<T> spec, int pageNumber,
			int pageSize, Sort sort, Class<?> clazz)
	{
		PageRequest pageRequest = new PageRequest(pageNumber - 1, pageSize, sort);
		Page<T> page = dao.findAll(spec, pageRequest);
		return page;
	}

	public static <T> YcPage<T> queryYcPage(JpaSpecificationExecutor<T> dao, Specification<T> spec, int pageNumber,
			int pageSize, Sort sort, Class<?> clazz)
	{
		Page<T> page = queryPage(dao, spec, pageNumber, pageSize, sort, clazz);
		YcPage<T> ycPage = transPage2YcPage(page);
		return ycPage;
	}

	public static <T> YcPage<T> transPage2YcPage(Page<T> page)
	{
		YcPage<T> ycPage = new YcPage<T>();
		ycPage.setList(page.getContent());
		ycPage.setPageTotal(page.getTotalPages());
		ycPage.setCountTotal((int) page.getTotalElements());
		return ycPage;
	}
}
