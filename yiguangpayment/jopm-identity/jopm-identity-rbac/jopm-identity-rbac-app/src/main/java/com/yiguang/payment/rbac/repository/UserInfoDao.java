package com.yiguang.payment.rbac.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.yiguang.payment.rbac.entity.UserInfo;

public interface UserInfoDao extends PagingAndSortingRepository<UserInfo, Long>, JpaSpecificationExecutor<UserInfo> {

	@Query("select u from UserInfo u where u.name=:name")
	public UserInfo	 queryUserInfoByName(@Param("name") String name);
	
	@Query("select u from UserInfo u where u.userId=:userId")
	public UserInfo	 queryUserInfoByUserId(@Param("userId") long userId);

}
