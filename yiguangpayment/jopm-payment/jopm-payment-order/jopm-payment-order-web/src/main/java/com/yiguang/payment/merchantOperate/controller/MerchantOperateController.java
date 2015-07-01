package com.yiguang.payment.merchantOperate.controller;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
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
import com.yiguang.payment.business.product.entity.Point;
import com.yiguang.payment.business.product.service.PointService;
import com.yiguang.payment.common.CommonConstant;
import com.yiguang.payment.common.Constant;
import com.yiguang.payment.common.datasource.service.DataSourceService;
import com.yiguang.payment.common.datasource.vo.OptionVO;
import com.yiguang.payment.merchantOperate.entity.MobileAndTotal;
import com.yiguang.payment.merchantOperate.service.MerchantOperateService;
import com.yiguang.payment.payment.entity.Merchant;
import com.yiguang.payment.payment.service.MerchantService;
import com.yiguang.payment.rbac.entity.RoleUser;
import com.yiguang.payment.rbac.entity.User;
import com.yiguang.payment.rbac.service.RoleService;
import com.yiguang.payment.rbac.service.RoleUserService;
import com.yiguang.payment.rbac.service.UserService;

/**
 * LoginController负责打开登录页面(GET请求)和登录出错页面(POST请求)， 真正登录的POST请求由Filter完成
 * 
 * @author ediosn
 * 
 */
@Controller
@RequestMapping(value = "/merchantOperate")
public class MerchantOperateController {
	@Autowired
	HttpSession session;
	@Autowired
	private MerchantService merchantService;
	@Autowired
	private UserService userService;
	@Autowired
	private RoleUserService roleUserService;
	@Autowired
	private RoleService roleService;
	@Autowired
	private PointService pointService;
	@Autowired
	private DataSourceService dataSourceService;
	@Autowired
	private MerchantOperateService merchantOperateService;
	
	private static Logger logger = LoggerFactory.getLogger(MerchantOperateController.class);
	
	@RequestMapping(value = "/getMessageList")
	public String getMessageList(Model model, ServletRequest request)
	{
		try
		{
			String username = (String) session.getAttribute(Constant.Common.LOGIN_NAME_CACHE);
			User user = userService.queryUserByName(username);
			List<RoleUser> roleUsers = roleUserService.queryRoleUserByUserId(user.getId());
			
			boolean isAdmin = false;
			boolean isMerchantAdmin  = false;
			
			for(RoleUser roleUser : roleUsers){
				if(roleUser.getRoleId() == 1){
					isAdmin = true;
				}
				else
				{
					isMerchantAdmin = true;
				}
				
				if(isAdmin &&isMerchantAdmin )
				{
					break;
				}
			}
			
			List<OptionVO> merchantList = null;
			List<OptionVO> pointList = null;
			
			if (isAdmin)
			{
				List<Merchant> merchants = merchantService.queryMerchantByChannelId(2);
				merchantList = new ArrayList<OptionVO>();
				pointList = new ArrayList<OptionVO>();
				for(Merchant merchant : merchants)
				{
					OptionVO vo = new OptionVO();
					vo.setText(merchant.getName());
					vo.setValue(String.valueOf(merchant.getId()));
					merchantList.add(vo);
					
					List<Point> plist = pointService.queryPointByMerAndCh(merchant.getId(), 2);
					OptionVO vop = null;
					for (Point p : plist)
					{
						vop = new OptionVO();
						vop.setText(p.getName());
						vop.setValue(String.valueOf(p.getId()));
						pointList.add(vop);
					}
				}
				OptionVO vo = new OptionVO();
				vo.setValue("-1");
				vo.setText("请选择");
				merchantList.add(0, vo);
				OptionVO ovo = new OptionVO();
				ovo.setValue("-1");
				ovo.setText("请选择");
				pointList.add(0, ovo);
			}
			else if (isMerchantAdmin)
			{
				merchantList = new ArrayList<OptionVO>();
				
				Merchant merchant = merchantService.queryMerchantByUserId(user.getId());
				OptionVO vo = new OptionVO();
				vo.setText(merchant.getName());
				vo.setValue(String.valueOf(merchant.getId()));
				merchantList.add(vo);
				
				List<Point> plist = pointService.queryPointByMerAndCh(merchant.getId(),2);
				
				pointList = new ArrayList<OptionVO>();
				OptionVO vop = null;
				for (Point p : plist)
				{
					vop = new OptionVO();
					vop.setText(p.getName());
					vop.setValue(String.valueOf(p.getId()));
					pointList.add(vop);
				}
			}
			else
			{
				merchantList = new ArrayList<OptionVO>();
				pointList = new ArrayList<OptionVO>();
			}
			
			
			model.addAttribute("merchantList", merchantList);
			model.addAttribute("pointList", pointList);
			return "merchantOperate/getMessageList";
		}
		catch (Exception e)
		{
			logger.error(e.getLocalizedMessage(), e);
			return "error/500";
		}
	}
	
	@RequestMapping(value = "/getmessage")
	public @ResponseBody
	String getmessage(@RequestParam(value = "merchantId", defaultValue = "") String merchantId,
					  @RequestParam(value = "serviceid", defaultValue = "") String serviceid, ModelMap model)
	{
		try{
			
			String message = merchantOperateService.getMessage(merchantId, serviceid);
//			System.out.println(message);
			return message;
		} catch(Exception e){
			e.printStackTrace();
			return "error/500";
		}
		

	}
	
	@RequestMapping(value = "/changePoint", method = RequestMethod.POST)
	public @ResponseBody
	String changePoint(@RequestParam(value = "merchantId", defaultValue = "-1") long merchantId, Model model)
	{

		logger.debug("changeSonLong start");
		try
		{
			List<Point> plist = pointService.queryPointBySupplierId(merchantId);
			
			List<OptionVO> pointList = new ArrayList<OptionVO>();
			OptionVO vop = null;
			for (Point p : plist)
			{
				vop = new OptionVO();
				vop.setText(p.getName());
				vop.setValue(String.valueOf(p.getId()));
				pointList.add(vop);
			}
			vop = new OptionVO();
			vop.setValue("-1");
			vop.setText("请选择");
			pointList.add(0, vop);
			String html = "";
			for (OptionVO op : pointList)
			{
				html = html + "<option value=\"" + op.getValue() + "\">" + op.getText() + "</option>";
			}
			logger.debug("changePoint end, html is [" + html + "]");
			return html;
		}
		catch (RpcException e)
		{
			logger.error("changePoint failed");
			return "";
		}
		catch (Exception e)
		{
			logger.error("changePoint failed");
			logger.error(e.getLocalizedMessage(), e);
			return "";
		}

	}
	
	@RequestMapping(value = "/viewOrder")
	public String viewOrder(Model model, ServletRequest request)
	{
		try
		{
			String username = (String) session.getAttribute(Constant.Common.LOGIN_NAME_CACHE);
			User user = userService.queryUserByName(username);
			List<RoleUser> roleUsers = roleUserService.queryRoleUserByUserId(user.getId());
			
			boolean isAdmin = false;
			boolean isMerchantAdmin  = false;
			
			for(RoleUser roleUser : roleUsers){
				if(roleUser.getRoleId() == 1){
					isAdmin = true;
				}
				else
				{
					isMerchantAdmin = true;
				}
				
				if(isAdmin &&isMerchantAdmin )
				{
					break;
				}
			}
			
			List<OptionVO> merchantList = null;
			
			if (isAdmin)
			{
				merchantList = dataSourceService
						.findOpenOptions(CommonConstant.DataSourceName.MERCHANT);
			}
			else if (isMerchantAdmin)
			{
				merchantList = new ArrayList<OptionVO>();
						
				Merchant merchant = merchantService.queryMerchantByUserId(user.getId());
				OptionVO vo = new OptionVO();
				vo.setText(merchant.getName());
				vo.setValue(String.valueOf(merchant.getId()));
				merchantList.add(vo);
			}
			else
			{
				merchantList = new ArrayList<OptionVO>();
			}
			model.addAttribute("merchantList", merchantList);
			return "merchantOperate/viewOrder";
		}
		catch (Exception e)
		{
			logger.error(e.getLocalizedMessage(), e);
			return "error/500";
		}
	}
	
	@RequestMapping(value = "/total")
	public @ResponseBody
	String getTotal(@RequestParam(value = "beginDate", defaultValue = "") String beginDate,
			@RequestParam(value = "endDate", defaultValue = "") String endDate,
			@RequestParam(value = "merchantId", defaultValue = "") String merchantId,ModelMap model)
	{
		try{
			String total = merchantOperateService.getTotal(merchantId, beginDate, endDate);
			return total;
		}catch (Exception e)
		{
			logger.error(e.getLocalizedMessage(), e);
			return "error/500";
		}
			
	}

	/*
	 * 导出excel表
	 */
	@RequestMapping(value = "/excel")
	public void exportExcel(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		
		try{
			String merchantId = request.getParameter("merchantId");
			String beginDate = request.getParameter("beginDate");
			String endDate = request.getParameter("endDate");
			List<MobileAndTotal> list = 
					merchantOperateService.getListMAT(merchantId, beginDate, endDate);	
			logger.debug("[MerchantOperateServiceImpl: getListMAT list= (" + list.toString()
					+ ")]");
			HSSFWorkbook wb = export(list);
			response.setContentType("application/vnd.ms-excel;charset=utf-8");
			response.setHeader("Content-disposition", "attachment;filename=order.xls");
			OutputStream ouputStream = response.getOutputStream();
			wb.write(ouputStream);
			ouputStream.flush();
			ouputStream.close();
		}catch (Exception e)
		{
			logger.error(e.getLocalizedMessage(), e);
		}
	}
	
	public HSSFWorkbook export(List<MobileAndTotal> list)
	{
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet("号码充值金额");
		HSSFRow row = sheet.createRow((int) 0);
		HSSFCellStyle style = wb.createCellStyle();
		for (int i = 0; i < list.size(); i++)
		{
			row = sheet.createRow(i);
			MobileAndTotal mat = list.get(i);
			row.createCell(0).setCellValue(mat.getMobile());
			row.createCell(1).setCellValue(mat.getTotal());
		}
		return wb;
	}
}
