package com.yiguang.payment.rbac.controller;

import java.security.interfaces.RSAPublicKey;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.rpc.RpcException;
import com.yiguang.payment.common.CommonConstant;
import com.yiguang.payment.common.Constant;
import com.yiguang.payment.common.JsonTool;
import com.yiguang.payment.common.datasource.service.DataSourceService;
import com.yiguang.payment.common.datasource.vo.OptionVO;
import com.yiguang.payment.common.message.MessageResolver;
import com.yiguang.payment.common.query.YcPage;
import com.yiguang.payment.common.security.RSAUtils;
import com.yiguang.payment.common.security.service.SecurityKeystoreService;
import com.yiguang.payment.common.utils.StringUtil;
import com.yiguang.payment.rbac.entity.RoleUser;
import com.yiguang.payment.rbac.entity.User;
import com.yiguang.payment.rbac.service.RoleUserService;
import com.yiguang.payment.rbac.service.UserService;
import com.yiguang.payment.rbac.vo.ModifyPasswordVO;
import com.yiguang.payment.rbac.vo.RoleUserVO;
import com.yiguang.payment.rbac.vo.UserVO;

/**
 * LoginController负责打开登录页面(GET请求)和登录出错页面(POST请求)， 真正登录的POST请求由Filter完成
 * 
 * @author ediosn
 * 
 */
@Controller
@RequestMapping(value = "/identity/user")
public class UserController {

	@Autowired
	HttpSession session;

	@Autowired
	private UserService userService;
	
	@Autowired
	private RoleUserService roleUserService;
	
	@Autowired
	private DataSourceService dataSourceService;
	
	@Autowired
	SecurityKeystoreService securityKeystoreService;

	private static final String PAGE_SIZE = "10";

	private static Logger logger = LoggerFactory.getLogger(UserController.class);
	
	@RequestMapping(value = "/userList")
	public String userList(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "pageSize", defaultValue = PAGE_SIZE) int pageSize,
			@RequestParam(value = "sortType", defaultValue = "auto") String sortType,
			@RequestParam(value = "username", defaultValue = "") String username,
			@RequestParam(value = "isLock", defaultValue = "-1") int isLock,
			@RequestParam(value = "status", defaultValue = "-1") int status,
			@RequestParam(value = "createTime", defaultValue = "") String createTime,Model model, ServletRequest request){
		
		try {
			UserVO vo = new UserVO();
			vo.setUsername(username);
			vo.setStatus(status);
			vo.setIsLock(isLock);
			vo.setCreateTime(createTime);

			YcPage<UserVO> page_list = userService.queryUserList(vo, pageNumber, pageSize, "");

			List<OptionVO> statusList = dataSourceService.findOpenOptions(CommonConstant.DataSourceName.COMMON_STATUS);
			List<OptionVO> isLockList = dataSourceService.findOpenOptions(CommonConstant.DataSourceName.IS_LOCK);
			model.addAttribute("status", status);
			model.addAttribute("statusList", statusList);
			model.addAttribute("isLock", isLock);
			model.addAttribute("isLockList", isLockList);
			model.addAttribute("createTime", createTime);
			model.addAttribute("username", username);
			model.addAttribute("mlist", page_list.getList());
			model.addAttribute("page", pageNumber);
			model.addAttribute("pageSize", pageSize);
			model.addAttribute("counttotal", page_list.getCountTotal() + "");
			model.addAttribute("pagetotal", page_list.getPageTotal() + "");

			return "identity/user/userList";
		} catch (Exception e) {
			return "error/500";
		}
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/updateUserStatus")
	public @ResponseBody
	String updateUserStatus(User user, ModelMap model) {

		Map<String, String> result = new HashMap<String, String>();
		try {
			userService.updateUserStatus(user);
			result.put("result", "success");
		} catch (RpcException e) {
			result.put("result", "error");
			result.put("message", "操作失败[" + MessageResolver.getMessage(e.getCode()) + "]");
		}

		String json = JsonTool.beanToJson(result);

		return json;
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/deleteUser")
	public @ResponseBody
	String deleteUser(User user, ModelMap model) {

		Map<String, String> result = new HashMap<String, String>();
		try {
			userService.deleteUser(user);
			
			result.put("result", "success");
		} catch (RpcException e) {
			result.put("result", "error");
			result.put("message", "操作失败[" + MessageResolver.getMessage(e.getCode()) + "]");
		}

		String json = JsonTool.beanToJson(result);

		return json;
	}
	
	@RequestMapping(value = "/editUser")
	public String editUser(User user, ModelMap model) {
		try {
			Map<String, Object> keyMap = securityKeystoreService.getKeyObjectToMap(Constant.SecurityCredential.RSA_KEY);
			RSAPublicKey publicKey = RSAUtils.getKeyByString(keyMap.get(Constant.RSACacheKey.RSA_PUBLICKEY) + "");
			model.addAttribute("modulus", RSAUtils.getModulus(publicKey));
			model.addAttribute("exponent", RSAUtils.getPublicExponent(publicKey));
			
			user = userService.queryUser(user.getId());
			List<OptionVO> statusList = dataSourceService.findOpenOptions(CommonConstant.DataSourceName.COMMON_STATUS);
			List<OptionVO> isLockList = dataSourceService.findOpenOptions(CommonConstant.DataSourceName.IS_LOCK );
			
			model.addAttribute("user", user);
			model.addAttribute("statusList", statusList);
			model.addAttribute("isLockList", isLockList);
			return "identity/user/editUser";
		} catch (Exception e) {
			return "error/500";
		}
	}
	
	@RequestMapping(value = "/toModifyPassword", method = RequestMethod.GET)
    public Object toModifyPassword(@RequestParam(value = "id")
    String id, ModelMap model)
    {
        try
        {
            Map<String, Object> keyMap = securityKeystoreService.getKeyObjectToMap(Constant.SecurityCredential.RSA_KEY);
            RSAPublicKey publicKey = RSAUtils.getKeyByString(keyMap.get(Constant.RSACacheKey.RSA_PUBLICKEY)
                                                             + "");

            User user = userService.queryUser(Long.valueOf(id));
            model.addAttribute("modulus", RSAUtils.getModulus(publicKey));
            model.addAttribute("exponent", RSAUtils.getPublicExponent(publicKey));
            model.addAttribute("user", user);
            return "identity/user/reset_password";
        }
        catch (RpcException e)
        {
            logger.debug("[OperatorController:toModifyPassword()]" + e.getMessage());
            return "error/500";
        }
        catch (Exception e)
        {
            logger.debug("[OperatorController:toModifyPassword()]" + e.getMessage());
            return "error/500";
        }
    }
	
	@RequestMapping(method=RequestMethod.POST,value = "/modifyPasswordDO")
    public @ResponseBody
    String modifyPasswordDO(ModifyPasswordVO modifyPasswordVO, ModelMap model)
    {
		Map<String, String> result = new HashMap<String, String>();
        try
        {
            if (StringUtils.isBlank(modifyPasswordVO.getId()) || StringUtils.isBlank(modifyPasswordVO.getRsaNewpwd())
                || StringUtils.isBlank(modifyPasswordVO.getRsaOldpwd()))
            {
            	result.put("message", "数据不完整，操作失败");
            }
            User user = userService.queryUser(Long.valueOf(modifyPasswordVO.getId()));
            String loginPwd = user.getPassword();
			
			String md5Password = securityKeystoreService.getEncryptKeyByJSRSAKey(modifyPasswordVO.getRsaOldpwd(), user.getId());
			if (!md5Password.equals(loginPwd))
			{
				result.put("message", "密码不匹配，操作失败");
			}
			else
			{
				user.setPassword(securityKeystoreService.getEncryptKeyByJSRSAKey(modifyPasswordVO.getRsaNewpwd() , user.getId()));
				userService.saveUser(user);
			
				result.put("result", "success");
			}
        }
        catch (RpcException e)
        {
        	result.put("result", "error");
        	result.put("message", "操作失败[" + MessageResolver.getMessage(e.getCode()) + "]");
        }
        String json = JsonTool.beanToJson(result);

		return json;
    }
	
	@RequestMapping(value = "/createUser")
	public String createUser(Model model) {
		try {
			Map<String, Object> keyMap = securityKeystoreService.getKeyObjectToMap(Constant.SecurityCredential.RSA_KEY);
			RSAPublicKey publicKey = RSAUtils.getKeyByString(keyMap.get(Constant.RSACacheKey.RSA_PUBLICKEY) + "");
			model.addAttribute("modulus", RSAUtils.getModulus(publicKey));
			model.addAttribute("exponent", RSAUtils.getPublicExponent(publicKey));
			
			List<OptionVO> statusList = dataSourceService.findOpenOptions(CommonConstant.DataSourceName.COMMON_STATUS);
			List<OptionVO> isLockList = dataSourceService.findOpenOptions(CommonConstant.DataSourceName.IS_LOCK );
			model.addAttribute("isLockList", isLockList);
			model.addAttribute("statusList", statusList);
			return "identity/user/editUser";
		} catch (Exception e) {
			return "error/500";
		}
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/saveUser")
	public @ResponseBody
	String saveUser(UserVO userVO, ModelMap model) {

		Map<String, String> result = new HashMap<String, String>();
		try {
			String regex_a = "[`~!@#$%^&*(){}'?/-=\\.<>￥%]";
			if (StringUtil.isBlank(userVO.getUsername())) {
				result.put("result", "error");
				result.put("message", "操作失败[" + "请填写用户名" + "]");

			} else if (Pattern.matches(regex_a, userVO.getUsername())) {
				result.put("result", "error");
				result.put("message", "操作失败[" + "输入值不能为空和包含其他非法字符" + "]");

			} else if (StringUtil.isBlank(String.valueOf(userVO.getStatus()))) {
				result.put("result", "error");
				result.put("message", "操作失败[" + "请选择状态" + "]");

			} else if (StringUtil.isBlank(String.valueOf(userVO.getIsLock()))) {
				result.put("result", "error");
				result.put("message", "操作失败[" + "请选择锁定状态" + "]");

			} else if (StringUtil.isBlank(String.valueOf(userVO.getPassword()))) {
				result.put("result", "error");
				result.put("message", "操作失败[" + "请输入密码" + "]");

			} else {
				User user = new User();
				user.setId(userVO.getId());
				user.setUsername(userVO.getUsername());
				user.setStatus(userVO.getStatus());
				user.setIsLock(userVO.getIsLock());
				user.setPassword("");
				user.setRemark(userVO.getRemark());
				user.setCreateTime(new Date());
				user = userService.saveUser(user);
				String psaaword = securityKeystoreService.getEncryptKeyByJSRSAKey(userVO.getPassword(), user.getId());
				user.setPassword(psaaword);
				user = userService.saveUser(user);
				result.put("result", "success");
			}
		} catch (RpcException e) {
			result.put("result", "error");
			result.put("message", "操作失败[" + MessageResolver.getMessage(e.getCode()) + "]");
		}
		String json = JsonTool.beanToJson(result);
		return json;
	}
	/**
	 * 设置角色
	 * @param user
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/setRole")
	public String setRole(User user, ModelMap model) {
		try {
			user = userService.queryUser(user.getId());
			List<OptionVO> statusList = dataSourceService.findOpenOptions(CommonConstant.DataSourceName.COMMON_STATUS);
			List<OptionVO> roleList = dataSourceService.findOpenOptionsWithoutPlease(CommonConstant.DataSourceName.ROLE);
			List<RoleUser> checkedlist = roleUserService.queryRoleUserByUserId(user.getId());
			model.addAttribute("checkedlist", checkedlist);
			model.addAttribute("user", user);
			model.addAttribute("roleList", roleList);
			model.addAttribute("statusList", statusList);
			return "identity/user/setRole";
		} catch (Exception e) {
			return "error/500";
		}
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/saveRoleUser")
	public @ResponseBody
	String saveRoleUser(RoleUserVO roleUserVO, ModelMap model){
		Map<String, String> result = new HashMap<String, String>();
		try {
			if (StringUtil.isBlank(roleUserVO.getRoleUserIds())) {
				result.put("result", "error");
				result.put("message", "操作失败[" + "请选择角色" + "]");
			} else if (StringUtil.isBlank(String.valueOf(roleUserVO.getStatus()))) {
				result.put("result", "error");
				result.put("message", "操作失败[" + "请选择状态" + "]");

			} else {
				RoleUser roleUser = new RoleUser();
				roleUser.setId(roleUserVO.getId());
				roleUser.setStatus(roleUserVO.getStatus());
				roleUser.setUserId(roleUserVO.getUserId());
				roleUserService.saveRoleUser(roleUser,roleUserVO.getRoleUserIds());
				result.put("result", "success");
			}
		} catch (RpcException e) {
			result.put("result", "error");
			result.put("message", "操作失败[" + MessageResolver.getMessage(e.getCode()) + "]");
		}
		String json = JsonTool.beanToJson(result);
		return json;
	}
	
}
