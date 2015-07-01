package com.yiguang.payment.rbac.service;

import java.util.Map;

import com.yiguang.payment.common.query.YcPage;
import com.yiguang.payment.rbac.entity.User;
import com.yiguang.payment.rbac.vo.UserVO;

public interface UserService
{
	public YcPage<UserVO> queryUserList(Map<String, Object> searchParams, int pageNumber, int pageSize,
			String sortType);

	public User updateUserStatus(User user);

	public String deleteUser(User user);

	public User saveUser(User user);

	public User queryUser(long id);

	public User queryUserByName(String username);
	
	public UserVO copyPropertiesToVO(User temp);

}
