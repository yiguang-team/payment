package com.yiguang.payment.rbac.controller;

import java.security.interfaces.RSAPublicKey;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.rpc.RpcException;
import com.yiguang.payment.common.CommonConstant;
import com.yiguang.payment.common.Constant;
import com.yiguang.payment.common.EntityConstant;
import com.yiguang.payment.common.IpTool;
import com.yiguang.payment.common.logging.entity.LoginAndLogoutLog;
import com.yiguang.payment.common.logging.service.LoginAndLogoutLogService;
import com.yiguang.payment.common.security.RSAUtils;
import com.yiguang.payment.common.security.service.SecurityKeystoreService;
import com.yiguang.payment.common.utils.StringUtil;
import com.yiguang.payment.rbac.entity.Menu;
import com.yiguang.payment.rbac.entity.Role;
import com.yiguang.payment.rbac.entity.RoleMenu;
import com.yiguang.payment.rbac.entity.RoleUser;
import com.yiguang.payment.rbac.entity.User;
import com.yiguang.payment.rbac.service.MenuService;
import com.yiguang.payment.rbac.service.RoleMenuService;
import com.yiguang.payment.rbac.service.RoleService;
import com.yiguang.payment.rbac.service.RoleUserService;

/**
 * LoginController负责打开登录页面(GET请求)和登录出错页面(POST请求)， 真正登录的POST请求由Filter完成,
 * 
 * @author calvin
 */
@Controller
@RequestMapping(value = "/login")
public class LoginController
{

	@Autowired
	private RoleService roleService;

	@Autowired
	private MenuService menuService;
	
	@Autowired
	private HttpServletRequest request;
	
	@Autowired
	private RoleMenuService roleMenuService;
	
	@Autowired
	private RoleUserService roleUserService;

	@Autowired
	private LoginAndLogoutLogService loginAndLogoutLogService;

	@Autowired
	private SecurityKeystoreService securityKeystoreService;

	@Value("${session.timeout}")
	private int timeout;

	@Autowired
	HttpSession session;

	private static Logger logger = LoggerFactory.getLogger(LoginController.class);

	@RequestMapping(method = RequestMethod.GET)
	public String login(ModelMap model)
	{
		Object sessionUserName = session.getAttribute(Constant.Common.LOGIN_NAME_CACHE);
		String username = StringUtil.initString();
		if (sessionUserName != null)
		{
			username = (String) sessionUserName;
		}
		try
		{
			Object object = session.getAttribute(EntityConstant.Session.LOGIN_SESSION_NAME);
			if (object != null)
			{
				User user = (User) object;
				List<RoleUser> roleUserList = roleUserService.queryRoleUserByUserId(user.getId());
				List<Role> roleList = new ArrayList<Role>();
				Role role = new Role();
				for(RoleUser roleUser : roleUserList){
					long roleId = roleUser.getRoleId();
					role = roleService.queryRole(roleId);
					roleList.add(role);
				}
				List<RoleMenu> roleMenuList = roleMenuService.queryRoleMenuByRoleList(roleList);

				List<Menu> menuList1 = new ArrayList<Menu>();
				List<Menu> menuList2 = new ArrayList<Menu>();
				Menu menu = null;
				boolean isMenu1Exsit = false;
				boolean isMenu2Exsit = false;
				for (RoleMenu roleMenu : roleMenuList)
				{
					menu = menuService.queryMenu(roleMenu.getMenuId());
					
					if (1 == menu.getMenulevel())
					{
						for (Menu menu1 : menuList1)
						{
							if (menu1.getId() == menu.getId())
							{
								isMenu1Exsit = true;
								break;
							}
						}
						
						if (!isMenu1Exsit)
						{
							menuList1.add(menu);
							isMenu1Exsit = false;
						}
					}
					
					if (2 == menu.getMenulevel())
					{
						for (Menu menu2 : menuList2)
						{
							if (menu2.getId() == menu.getId())
							{
								isMenu2Exsit = true;
								break;
							}
						}
						
						if (!isMenu2Exsit)
						{
							menuList2.add(menu);
							isMenu2Exsit = false;
						}
					}
				}
				
				model.addAttribute("menulist1", menuList1);
				model.addAttribute("menulist2", menuList2);
				model.addAttribute("userid", user.getId());
				model.addAttribute("username", username);
				if(request.getContextPath().equals("/aportal"))
				{
					return "redirect:/mall/ronghan/buy";
				}
				else
				{
					return "index/index";
				}
			}

			Map<String, Object> keyMap = securityKeystoreService.getKeyObjectToMap(Constant.SecurityCredential.RSA_KEY);
			RSAPublicKey publicKey = RSAUtils.getKeyByString(keyMap.get(Constant.RSACacheKey.RSA_PUBLICKEY) + "");

			model.addAttribute("modulus", RSAUtils.getModulus(publicKey));
			model.addAttribute("exponent", RSAUtils.getPublicExponent(publicKey));
			model.addAttribute("username", username);
			return "login/login";
		}
		catch (RpcException e)
		{
			logger.error("[LoginController:login(Get)]" + e.getMessage());
			model.put("message", "操作失败[" + e.getMessage() + "]");
			model.put("canback", true);
			model.addAttribute("username", username);
			return "login/login";
		}
	}

	@RequestMapping(method = RequestMethod.POST)
	public String dologin(@RequestParam(value = "username", defaultValue = "") String username,
			@RequestParam(value = "rsaPassword", defaultValue = "") String rsaPassword, ModelMap model)
	{

		try
		{
			// 验证参数
			if (StringUtils.isBlank(username) || StringUtils.isBlank(rsaPassword))
			{
				model.addAttribute("errmsg", "用户名或密码不能为空");
				return "login/login";
			}
			session.setAttribute(Constant.Common.LOGIN_NAME_CACHE, username);
			session.setMaxInactiveInterval(timeout);
			SecurityUtils.getSubject().login(new UsernamePasswordToken(username, rsaPassword));
			User user = (User) SecurityUtils.getSubject().getPrincipals().getPrimaryPrincipal();

			if (user != null)
			{
//				if (!operator.getOperatorType().equals(OperatorType.SP_OPERATOR))
//				{
//					model.addAttribute("errmsg", "此账号没有权限使用管理后台");
//					return "login/login";
//				}

				session.setAttribute(EntityConstant.Session.LOGIN_SESSION_NAME, user);
				session.setMaxInactiveInterval(timeout);

				List<RoleUser> roleUserList = roleUserService.queryRoleUserByUserId(user.getId());
				List<Role> roleList = new ArrayList<Role>();
				Role role = new Role();
				for(RoleUser roleUser : roleUserList){
					long roleId = roleUser.getRoleId();
					role = roleService.queryRole(roleId);
					roleList.add(role);
				}
				
				if (roleList == null || roleList.size() <= 0)
				{
					model.addAttribute("errmsg", "此账号没有分配角色，请联系管理员。");
					session.setAttribute(EntityConstant.Session.LOGIN_SESSION_NAME, null);
					return "login/login";
				}
				
				List<RoleMenu> roleMenuList = roleMenuService.queryRoleMenuByRoleList(roleList);

				List<Menu> menuList1 = new ArrayList<Menu>();
				List<Menu> menuList2 = new ArrayList<Menu>();
				Menu menu = null;
				boolean isMenu1Exsit = false;
				boolean isMenu2Exsit = false;
				for (RoleMenu roleMenu : roleMenuList)
				{
					menu = menuService.queryMenu(roleMenu.getMenuId());
					
					if (1 == menu.getMenulevel())
					{
						for (Menu menu1 : menuList1)
						{
							if (menu1.getId() == menu.getId())
							{
								isMenu1Exsit = true;
								break;
							}
						}
						
						if (!isMenu1Exsit)
						{
							menuList1.add(menu);
							isMenu1Exsit = false;
						}
					}
					
					if (2 == menu.getMenulevel())
					{
						for (Menu menu2 : menuList2)
						{
							if (menu2.getId() == menu.getId())
							{
								isMenu2Exsit = true;
								break;
							}
						}
						
						if (!isMenu2Exsit)
						{
							menuList2.add(menu);
							isMenu2Exsit = false;
						}
					}
				}
				
				model.addAttribute("menulist1", menuList1);
				model.addAttribute("menulist2", menuList2);
				model.addAttribute("userid", user.getId());
				model.addAttribute("username", username);
				// 记录登陆日志 开始
				LoginAndLogoutLog log = new LoginAndLogoutLog();
				log.setUsername(username);
				log.setOperationType(CommonConstant.LoginAndLogoutType.LOGIN);
				log.setOperationIp(IpTool.getIpAddr(request));
				log.setOperationTime(new Date());
				loginAndLogoutLogService.recordLoginAndLogoutLog(log);
				// 记录登陆日志 结束
				
				if(request.getContextPath().equals("/aportal"))
				{
					return "redirect:/mall/ronghan/buy";
				}
				else
				{
					return "index/index";
				}
			}
			else
			{
				model.addAttribute("errmsg", "用户名或密码不正确");
				return "login/login";
			}
		}
		catch (UnknownAccountException uae)
		{
			model.addAttribute("errmsg", "操作失败[" + uae.getLocalizedMessage() + "]");
			return "login/login";
		}
		catch (IncorrectCredentialsException ice)
		{
			model.addAttribute("errmsg", "操作失败[" + ice.getLocalizedMessage() + "]");
			return "login/login";
		}
		catch (LockedAccountException lae)
		{
			model.addAttribute("errmsg", "操作失败[" + lae.getLocalizedMessage() + "]");
			return "login/login";
		}
		catch (RpcException e)
		{
			logger.debug("[LoginController:dologin(GET)]" + e.getMessage());
			model.addAttribute("errmsg", "操作失败[" + e.getMessage() + "]");
			session.setAttribute(EntityConstant.Session.LOGIN_SESSION_NAME, null);
			return "login/login";
		}
		catch (AuthenticationException ae)
		{
			model.addAttribute("errmsg", "操作失败[" + ae.getLocalizedMessage() + "]");
			session.setAttribute(EntityConstant.Session.LOGIN_SESSION_NAME, null);
			return "login/login";
		}
		catch (Exception ae)
		{
			logger.debug("[LoginController:dologin(GET)]" + ae.getMessage());
			model.addAttribute("errmsg", "操作失败[" + ae.getLocalizedMessage() + "]");
			session.setAttribute(EntityConstant.Session.LOGIN_SESSION_NAME, null);
			return "login/login";
		}
	}

	@RequestMapping(value = "loginout", method = RequestMethod.GET)
	public String loginout(ModelMap model)
	{
		try
		{
			logger.debug("用户登出");

			// 避免重复记录登出日志
			if (null != session.getAttribute(EntityConstant.Session.LOGIN_SESSION_NAME))
			{
				// 记录登出日志 开始
				LoginAndLogoutLog log = new LoginAndLogoutLog();
				log.setUsername(String.valueOf(session.getAttribute(Constant.Common.LOGIN_NAME_CACHE)));
				log.setOperationType(CommonConstant.LoginAndLogoutType.LOGOUT);
				log.setOperationIp(IpTool.getIpAddr(request));
				log.setOperationTime(new Date());
				loginAndLogoutLogService.recordLoginAndLogoutLog(log);
				// 记录登出日志 结束
			}
			session.setAttribute(EntityConstant.Session.LOGIN_SESSION_NAME, null);

			return "redirect:/login";
		}
		catch (RpcException e)
		{
			logger.error("[LoginController:dologin(Post)]" + e.getMessage());
			session.setAttribute(EntityConstant.Session.LOGIN_SESSION_NAME, null);
			model.put("message", "操作失败[" + e.getMessage() + "]");
			model.put("canback", true);
			return "login/login";
		}
	}
}
