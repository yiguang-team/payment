package com.yiguang.payment.rbac.controller;

import java.util.ArrayList;
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

import com.alibaba.dubbo.rpc.RpcException;
import com.yiguang.payment.common.CommonConstant;
import com.yiguang.payment.common.JsonTool;
import com.yiguang.payment.common.datasource.service.DataSourceService;
import com.yiguang.payment.common.datasource.vo.OptionVO;
import com.yiguang.payment.common.message.MessageResolver;
import com.yiguang.payment.common.query.YcPage;
import com.yiguang.payment.common.utils.StringUtil;
import com.yiguang.payment.rbac.entity.Menu;
import com.yiguang.payment.rbac.entity.Privilege;
import com.yiguang.payment.rbac.entity.Role;
import com.yiguang.payment.rbac.entity.RoleMenu;
import com.yiguang.payment.rbac.entity.RolePrivilege;
import com.yiguang.payment.rbac.entity.RoleUser;
import com.yiguang.payment.rbac.entity.User;
import com.yiguang.payment.rbac.service.MenuService;
import com.yiguang.payment.rbac.service.PrivilegeService;
import com.yiguang.payment.rbac.service.RoleMenuService;
import com.yiguang.payment.rbac.service.RolePrivilegeService;
import com.yiguang.payment.rbac.service.RoleService;
import com.yiguang.payment.rbac.service.RoleUserService;
import com.yiguang.payment.rbac.service.UserService;
import com.yiguang.payment.rbac.vo.RoleVO;
import com.yiguang.payment.rbac.vo.UserVO;

/**
 * LoginController负责打开登录页面(GET请求)和登录出错页面(POST请求)， 真正登录的POST请求由Filter完成
 * 
 * @author ediosn
 * 
 */
@Controller
@RequestMapping(value = "/identity/role")
public class RoleController extends BaseControl {
	@Autowired
	HttpSession session;

	@Autowired
	private RoleService roleService;

	@Autowired
	private UserService userService;
	
	@Autowired
	private PrivilegeService privilegeService;
	
	@Autowired
	private MenuService menuService;

	@Autowired
	private RoleUserService roleUserService;

	@Autowired
	private RoleMenuService roleMenuService;
	
	@Autowired
	private RolePrivilegeService rolePrivilegeService;

	@Autowired
	private DataSourceService dataSourceService;

	private static final String PAGE_SIZE = "10";

	private static Logger logger = LoggerFactory
			.getLogger(UserController.class);

	@RequestMapping(value = "/roleList")
	public String roleList(
			@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "pageSize", defaultValue = PAGE_SIZE) int pageSize,
			@RequestParam(value = "sortType", defaultValue = "auto") String sortType,
			@RequestParam(value = "roleName", defaultValue = "") String roleName,
			@RequestParam(value = "status", defaultValue = "-1") int status,
			@RequestParam(value = "createTime", defaultValue = "") String createTime,
			Model model, ServletRequest request) {
		try {
			
			RoleVO vo = new RoleVO();
			vo.setRoleName(roleName);
			vo.setStatus(status);
			vo.setCreateTime(createTime);
			
			YcPage<RoleVO> page_list = roleService.queryRoleList(vo,
					pageNumber, pageSize, "");

			List<OptionVO> statusList = dataSourceService
					.findOpenOptions(CommonConstant.DataSourceName.COMMON_STATUS);
			model.addAttribute("status", status);
			model.addAttribute("statusList", statusList);
			model.addAttribute("createTime", createTime);
			model.addAttribute("roleName", roleName);
			model.addAttribute("mlist", page_list.getList());
			model.addAttribute("page", pageNumber);
			model.addAttribute("pageSize", pageSize);
			model.addAttribute("counttotal", page_list.getCountTotal() + "");
			model.addAttribute("pagetotal", page_list.getPageTotal() + "");

			return "identity/role/roleList";
		} catch (Exception e) {
			return "error/500";
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = "/updateRoleStatus")
	public @ResponseBody String updateRoleStatus(Role role, ModelMap model) {

		Map<String, String> result = new HashMap<String, String>();
		try {
			roleService.updateRoleStatus(role);
			result.put("result", "success");
		} catch (RpcException e) {
			result.put("result", "error");
			result.put("message",
					"操作失败[" + MessageResolver.getMessage(e.getCode()) + "]");
		}

		String json = JsonTool.beanToJson(result);

		return json;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/deleteRole")
	public @ResponseBody String deleteRole(Role role, ModelMap model) {

		Map<String, String> result = new HashMap<String, String>();
		try {
			roleService.deleteRole(role);
			result.put("result", "success");
		} catch (RpcException e) {
			result.put("result", "error");
			result.put("message",
					"操作失败[" + MessageResolver.getMessage(e.getCode()) + "]");
		}

		String json = JsonTool.beanToJson(result);

		return json;
	}

	@RequestMapping(value = "/editRole")
	public String editRole(Role role, ModelMap model) {
		try {
			role = roleService.queryRole(role.getId());
			List<OptionVO> statusList = dataSourceService
					.findOpenOptions(CommonConstant.DataSourceName.COMMON_STATUS);

			model.addAttribute("role", role);
			model.addAttribute("statusList", statusList);
			return "identity/role/editRole";
		} catch (Exception e) {
			return "error/500";
		}
	}

	@RequestMapping(value = "/createRole")
	public String createRole(Model model) {
		try {
			List<OptionVO> statusList = dataSourceService
					.findOpenOptions(CommonConstant.DataSourceName.COMMON_STATUS);

			model.addAttribute("statusList", statusList);
			return "identity/role/editRole";
		} catch (Exception e) {
			return "error/500";
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = "/saveRole")
	public @ResponseBody String saveRole(RoleVO roleVO, ModelMap model) {

		Map<String, String> result = new HashMap<String, String>();
		try {
			String regex_a = "[`~!@#$%^&*(){}'?/-=\\.<>￥%]";
			if (StringUtil.isBlank(roleVO.getRoleName())) {
				result.put("result", "error");
				result.put("message", "操作失败[" + "请填写角色名" + "]");

			} else if (Pattern.matches(regex_a, roleVO.getRoleName())) {
				result.put("result", "error");
				result.put("message", "操作失败[" + "输入值不能为空和包含其他非法字符" + "]");

			} else if (StringUtil.isBlank(String.valueOf(roleVO.getStatus()))) {
				result.put("result", "error");
				result.put("message", "操作失败[" + "请选择状态" + "]");

			} else {
				Role role = new Role();
				role.setId(roleVO.getId());
				role.setRoleName(roleVO.getRoleName());
				role.setStatus(Integer.valueOf(roleVO.getStatus()));
				role.setCreateTime(new Date());
				role.setRemark(roleVO.getRemark());
				roleService.saveRole(role);
				result.put("result", "success");
			}
		} catch (RpcException e) {
			result.put("result", "error");
			result.put("message",
					"操作失败[" + MessageResolver.getMessage(e.getCode()) + "]");
		}
		String json = JsonTool.beanToJson(result);
		return json;
	}

	@RequestMapping(value = "/initRoleMenuTree")
	public String initRoleMenuTree(Role role, ModelMap model) {
		Role new_role = roleService.queryRole(role.getId());
		List<Menu> menuList1 = menuService.findMenuByLevel(1);
		List<Menu> menuList2 = menuService.findMenuByLevel(2);

		List<Role> roleList = new ArrayList<Role>();
		roleList.add(new_role);
		List<RoleMenu> roleMenuList = roleMenuService
				.queryRoleMenuByRoleList(roleList);

		StringBuffer result = new StringBuffer("");

		for (Menu menu1 : menuList1) {
			boolean checked1 = false;
			for (RoleMenu rm : roleMenuList) {
				if (rm.getMenuId() == menu1.getId()) {
					checked1 = true;
					break;
				}
			}

			result = getPortalStr(result, menu1.getId(), menu1.getId(),
					menu1.getMenuName(), checked1, true);

			for (Menu menu2 : menuList2) {
				if (menu2.getParentId() == menu1.getId()) {

					boolean checked2 = false;
					for (RoleMenu rm : roleMenuList) {
						if (rm.getMenuId() == menu2.getId()) {
							checked2 = true;
							break;
						}
					}
					result = getPortalStr(result, menu2.getId(),
							menu2.getParentId(), menu2.getMenuName(), checked2,
							true);
				}
			}
		}
		model.addAttribute("tree", result.toString());
		model.addAttribute("titlename", "hops-mportal");

		model.addAttribute("role", role);
		model.addAttribute("id", role.getId());
		return "identity/role/role_addmenu_tree";

	}

	// 菜单提交
	@RequestMapping(value = "/saveRoleMenuTree")
	public @ResponseBody
	String listRoleTreeMenuUpdate(long id,
			String menuidstr, ModelMap model) {
		Map<String, String> result = new HashMap<String, String>();
		try {
			if (StringUtil.isNotBlank(String.valueOf(id))) {

				if (StringUtil.isNotBlank(menuidstr)) {
					roleMenuService.saveRoleMenuList(menuidstr, id);
				}
				
				result.put("result", "success");
			}
		} catch (RpcException e) {
			result.put("result", "error");
			result.put("message",
					"操作失败[" + MessageResolver.getMessage(e.getCode()) + "]");
		}
		String json = JsonTool.beanToJson(result);
		return json;
	}

	@RequestMapping(value = "/viewUserList")
	public String
	viewUserList(long id, ModelMap model){
		try {
			List<RoleUser> roleUserList = roleUserService.queryRoleUserByRole(id);
			List<UserVO> userList = new ArrayList<UserVO>();
			for(RoleUser roleUser : roleUserList){
				long userId = roleUser.getUserId();
				User user = userService.queryUser(userId);
				UserVO vo = userService.copyPropertiesToVO(user);
				vo.setStatusLabel(dataSourceService.findOptionVOById(CommonConstant.DataSourceName.COMMON_STATUS,
						String.valueOf(user.getStatus())).getText());
				userList.add(vo);
			}
			model.addAttribute("userList", userList);
			return "identity/role/viewUserList";
		} catch (Exception e) {
			return "error/500";
		}
	}
	public StringBuffer getPortalStr(StringBuffer sb, Long id, Long pid,
			String name, boolean checked, boolean openType) {
		if (sb.length() > 1)
			sb.append(",");
		sb.append("{ id:" + id + ", pId:" + pid + ", name:\"" + name
				+ "\", checked:" + checked + ",open:" + openType + "}");
		return sb;
	}
	
	@RequestMapping(value = "/initRolePrivilegeTree")
	public String initRolePrivilegeTree(Role role, ModelMap model) {
		Role new_role = roleService.queryRole(role.getId());
		List<Privilege> privileges0 = privilegeService.findPrivilegeByLevel(0);
		List<Privilege> privileges1 = privilegeService.findPrivilegeByLevel(1);
		List<Privilege> privileges2 = privilegeService.findPrivilegeByLevel(2);

		List<Role> roleList = new ArrayList<Role>();
		roleList.add(new_role);
		List<RolePrivilege> rolePrivilegeList = rolePrivilegeService
				.queryRolePrivilegeByRoleList(roleList);

		StringBuffer result = new StringBuffer("");
		for(Privilege privilege0 : privileges0) {
			boolean checked0 = false;
			for (RolePrivilege rm : rolePrivilegeList) {
				if (rm.getPrivilegeId() == privilege0.getId()) {
					checked0 = true;
					break;
				}
			}

			result = getPortalStr(result, privilege0.getId(), privilege0.getParentId(),
					privilege0.getName(), checked0, true);
			
			for (Privilege privilege1 : privileges1) {
				if (privilege1.getParentId() == privilege0.getId()) {
					boolean checked1 = false;
					for (RolePrivilege rm : rolePrivilegeList) {
						if (rm.getPrivilegeId() == privilege1.getId()) {
							checked1 = true;
							break;
						}
					}
	
					result = getPortalStr(result, privilege1.getId(), privilege1.getParentId(),
							privilege1.getName(), checked1, true);
				}
				for (Privilege privilege2 : privileges2) {
					if (privilege2.getParentId() == privilege1.getId()) {

						boolean checked2 = false;
						for (RolePrivilege rm : rolePrivilegeList) {
							if (rm.getPrivilegeId() == privilege2.getId()) {
								checked2 = true;
								break;
							}
						}
						result = getPortalStr(result, privilege2.getId(),
								privilege2.getParentId(), privilege2.getName(), checked2,
								true);
					}
				}
			}
		}
		
		model.addAttribute("tree", result.toString());
		model.addAttribute("titlename", "hops-mportal");

		model.addAttribute("role", new_role);
		model.addAttribute("id", role.getId());
		return "identity/role/role_addprivilege_tree";
	}
	
	// 权限提交
		@RequestMapping(value = "/saveRolePrivilegeTree")
		public @ResponseBody
		String listRoleTreePrivilegeUpdate(long id,
				String privilegeidstr, ModelMap model) {
			Map<String, String> result = new HashMap<String, String>();
			try {
				if (StringUtil.isNotBlank(String.valueOf(id))) {

					if (StringUtil.isNotBlank(privilegeidstr)) {
						rolePrivilegeService.saveRolePrivilegeList(privilegeidstr, id);
					}
					
					result.put("result", "success");
				}
			} catch (RpcException e) {
				result.put("result", "error");
				result.put("message",
						"操作失败[" + MessageResolver.getMessage(e.getCode()) + "]");
			}
			String json = JsonTool.beanToJson(result);
			return json;
		}
}
