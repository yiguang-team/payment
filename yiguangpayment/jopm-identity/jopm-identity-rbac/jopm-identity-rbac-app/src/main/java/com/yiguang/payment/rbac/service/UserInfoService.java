package com.yiguang.payment.rbac.service;

import com.yiguang.payment.rbac.entity.UserInfo;

public interface UserInfoService
{
//	public YcPage<UserInfo> queryUserInfoList(Map<String, Object> searchParams, int pageNumber, int pageSize,
//			String sortType);

//	public UserInfo updateUserInfoStatus(UserInfo userInfo);

	public String deleteUserInfo(UserInfo userInfo);

	public UserInfo saveUserInfo(UserInfo userInfo);

	public UserInfo queryUserInfo(long id);
	
	public UserInfo queryUserInfoByUserId(long userId);

}
