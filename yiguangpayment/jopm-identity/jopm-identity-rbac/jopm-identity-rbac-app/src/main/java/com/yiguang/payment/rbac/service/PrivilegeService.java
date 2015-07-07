package com.yiguang.payment.rbac.service;

import java.util.List;

import com.yiguang.payment.rbac.entity.Privilege;
import com.yiguang.payment.rbac.vo.PrivilegeVO;

public interface PrivilegeService {

//	public YcPage<PrivilegeVO> queryPrivilegeList(Map<String, Object> searchParams, int pageNumber, int pageSize,
//			String sortType);

	public Privilege updatePrivilegeStatus(Privilege privilege);

	public String deletePrivilege(Privilege privilege);

	public Privilege savePrivilege(Privilege privilege);

	public Privilege queryPrivilege(long id);
	
	public Privilege queryPrivilegeByUrl(String url);
	
	public List<Privilege> findPrivilegeByLevel(int level);
	
	public PrivilegeVO copyPropertiesToVO(Privilege temp);
}
