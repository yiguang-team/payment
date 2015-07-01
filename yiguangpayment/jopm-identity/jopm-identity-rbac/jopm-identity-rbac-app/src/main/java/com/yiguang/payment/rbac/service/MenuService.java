package com.yiguang.payment.rbac.service;

import java.util.List;
import java.util.Map;

import com.yiguang.payment.common.query.YcPage;
import com.yiguang.payment.rbac.entity.Menu;
import com.yiguang.payment.rbac.vo.MenuVO;

public interface MenuService
{
	public YcPage<MenuVO> queryMenuList(Map<String, Object> searchParams, int pageNumber, int pageSize,
			String sortType);

	public Menu updateMenuStatus(Menu menu);

	public String deleteMenu(Menu menu);

	public Menu saveMenu(Menu menu);

	public Menu queryMenu(long id);
	
	public Menu queryMenuByUrl(String url);
	
	public List<Menu> findMenuByLevel(int level);
	
	public MenuVO copyPropertiesToVO(Menu temp);
}
