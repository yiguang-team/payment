package com.yiguang.payment.rbac.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.rpc.RpcException;
import com.yiguang.payment.common.CommonConstant;
import com.yiguang.payment.common.JsonTool;
import com.yiguang.payment.common.datasource.service.DataSourceService;
import com.yiguang.payment.common.datasource.vo.OptionVO;
import com.yiguang.payment.common.message.MessageResolver;
import com.yiguang.payment.common.security.service.SecurityKeystoreService;
import com.yiguang.payment.common.utils.StringUtil;
import com.yiguang.payment.rbac.entity.User;
import com.yiguang.payment.rbac.entity.UserInfo;
import com.yiguang.payment.rbac.service.UserInfoService;
import com.yiguang.payment.rbac.service.UserService;
import com.yiguang.payment.rbac.vo.UserInfoVO;

/**
 * LoginController负责打开登录页面(GET请求)和登录出错页面(POST请求)， 真正登录的POST请求由Filter完成
 * 
 * @author ediosn
 * 
 */
@Controller
@RequestMapping(value = "/identity/userInfo")
public class UserInfoController {
	@Autowired
	HttpSession session;

	@Autowired
	private UserInfoService userInfoService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private DataSourceService dataSourceService;
	
	@Autowired
	SecurityKeystoreService securityKeystoreService;

	private static Logger logger = LoggerFactory.getLogger(UserInfoController.class);
	
	@RequestMapping(value = "/editUserInfo")
	public String editUserInfo(User user, ModelMap model) {
		try {
			logger.debug("editUserInfo : start userId"+user);
			UserInfo userInfo = userInfoService.queryUserInfo(user.getId());
			user = userService.queryUser(user.getId());
			List<OptionVO> statusList = dataSourceService.findOpenOptions(CommonConstant.DataSourceName.COMMON_STATUS);
			model.addAttribute("user", user);
			model.addAttribute("userInfo", userInfo);
			model.addAttribute("statusList", statusList);
			logger.debug("editUserInfo : end userId"+user);
			return "identity/user/editUserInfo";
		} catch (Exception e) {
			logger.error("editUserInfo : error userId"+user);
			return "error/500";
		}
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/saveUserInfo")
	public @ResponseBody
	String saveUserInfo(UserInfoVO userInfoVO, ModelMap model){
		Map<String, String> result = new HashMap<String, String>();
		try {
			String regex_a = "^1[3|4|5|8][0-9]\\d{4,8}";
			String regex_b = "^[a-z]([a-z0-9]*[-_]?[a-z0-9]+)*@([a-z0-9]*[-_]?[a-z0-9]+)+[\\.][a-z]{2,3}([\\.][a-z]{2})?";
			if (StringUtil.isBlank(userInfoVO.getMobile())) {
				result.put("result", "error");
				result.put("message", "操作失败[" + "请输入手机号码" + "]");
			} else if (! Pattern.matches(regex_a, userInfoVO.getMobile())) {
				result.put("result", "error");
				result.put("message", "操作失败[" + "手机号码格式不正确" + "]");

			} else if (StringUtil.isBlank(userInfoVO.getEmail())) {
				result.put("result", "error");
				result.put("message", "操作失败[" + "请输入邮箱" + "]");
			} else if (! Pattern.matches(regex_b, userInfoVO.getEmail())) {
				result.put("result", "error");
				result.put("message", "操作失败[" + "邮箱格式不正确" + "]");
			} else {
				UserInfo userInfo = new UserInfo();
				userInfo.setId(userInfoVO.getId());
				userInfo.setName(userInfoVO.getName());
				userInfo.setMobile(userInfoVO.getMobile());
				userInfo.setUserId(userInfoVO.getUserId());
				userInfo.setEmail(userInfoVO.getEmail());
				userInfoService.saveUserInfo(userInfo);
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
