// /*
// * Licensed to the Apache Software Foundation (ASF) under one
// * or more contributor license agreements. See the NOTICE file
// * distributed with this work for additional information
// * regarding copyright ownership. The ASF licenses this file
// * to you under the Apache License, Version 2.0 (the
// * "License"); you may not use this file except in compliance
// * with the License. You may obtain a copy of the License at
// *
// * http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing,
// * software distributed under the License is distributed on an
// * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
// * KIND, either express or implied. See the License for the
// * specific language governing permissions and limitations
// * under the License.
// */
package com.yiguang.payment.rbac.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.stereotype.Service;

import com.yiguang.payment.common.CommonConstant;
import com.yiguang.payment.common.utils.StringUtil;
import com.yiguang.payment.common.security.service.SecurityKeystoreService;
import com.yiguang.payment.rbac.entity.Role;
import com.yiguang.payment.rbac.entity.RoleUser;
import com.yiguang.payment.rbac.entity.User;
import com.yiguang.payment.rbac.service.RoleService;
import com.yiguang.payment.rbac.service.RoleUserService;
import com.yiguang.payment.rbac.service.UserService;

@Service("shiroDbRealm")
public class ShiroDbRealm extends AuthorizingRealm
{
	private UserService userService;
	
	private RoleUserService roleUserService;
	
	private RoleService roleService;
	
	private SecurityKeystoreService securityKeystoreService;

	public UserService getUserService() {
		return userService;
	}

	public RoleUserService getRoleUserService() {
		return roleUserService;
	}

	public void setRoleUserService(RoleUserService roleUserService) {
		this.roleUserService = roleUserService;
	}

	public void setSecurityKeystoreService(SecurityKeystoreService securityKeystoreService)
	{
		this.securityKeystoreService = securityKeystoreService;
	}

	public void setUserService(UserService userService)
	{
		this.userService = userService;
	}

	
	/**
	 * 授权信息
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) throws AuthenticationException
	{
		User user = (User) principals.getPrimaryPrincipal();
		if (user != null)
		{
			
			List<RoleUser> roleUserList = roleUserService.queryRoleUserByUserId(user.getId());
			List<Role> roleList = new ArrayList<Role>();
			for(RoleUser roleUser : roleUserList) {
				long roleId = roleUser.getRoleId();
				Role role = roleService.queryRole(roleId);
				roleList.add(role);
			}
			if (roleList != null && roleList.size() != 0)
			{
				SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
				for (Role role : roleList)
				{
//					List<String> permissions = rolePrivilegeQueryService.queryPermissionsByRoleId(role.getId());
//					info.addRole(role.getRoleName());
//					info.addStringPermissions(permissions);
				}
				return info;
			}
			else
			{
				throw new AuthenticationException("授权未通过，该用户没有角色！");
			}
		}
		else
		{
			throw new AuthenticationException("授权未通过，认证用户信息不存在！");
		}
	}

	/**
	 * 认证信息
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) throws AuthenticationException
	{
		UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
		String userName = token.getUsername();
		String pwd = null;
		if (!StringUtil.isNullOrEmpty(userName))
		{
			User user = userService.queryUserByName(userName);
			if (user != null)
			{
				if (CommonConstant.CommonStatus.CLOSE == user.getStatus())
				{
					throw new LockedAccountException("认证未通过，操作员状态未开启！");
				}
				
				String loginPwd = user.getPassword();
				
				pwd = String.valueOf(token.getPassword());
				String md5Password = securityKeystoreService.getEncryptKeyByJSRSAKey(pwd, user.getId());
				if (!md5Password.equals(loginPwd))
				{
					throw new IncorrectCredentialsException("认证未通过，密码不正确！");
				}
				token.setPassword(md5Password.toCharArray());
				SimpleAuthenticationInfo simpleAuthenticationInfo = new SimpleAuthenticationInfo(user, loginPwd,
						getName());
				return simpleAuthenticationInfo;
			}
			else
			{
				throw new UnknownAccountException("认证未通过，操作员不存在!");
			}
		}
		else
		{
			throw new AuthenticationException("认证未通过，用户名为空！");
		}
	}
}
