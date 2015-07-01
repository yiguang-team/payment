package com.yiguang.payment.merchantOperate.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.rpc.RpcException;
import com.yiguang.payment.business.product.entity.Point;
import com.yiguang.payment.business.product.entity.Product;
import com.yiguang.payment.business.product.service.PointChannelRelationService;
import com.yiguang.payment.business.product.service.PointService;
import com.yiguang.payment.business.product.service.ProductService;
import com.yiguang.payment.common.CommonConstant;
import com.yiguang.payment.common.Constant;
import com.yiguang.payment.common.IpTool;
import com.yiguang.payment.common.RestConst;
import com.yiguang.payment.common.datasource.service.DataSourceService;
import com.yiguang.payment.common.datasource.vo.OptionVO;
import com.yiguang.payment.common.exception.ErrorCodeConst;
import com.yiguang.payment.common.message.MessageResolver;
import com.yiguang.payment.common.numsection.entity.NumSection;
import com.yiguang.payment.common.numsection.service.CheckNumSectionService;
import com.yiguang.payment.common.security.MD5Util;
import com.yiguang.payment.common.security.service.SecurityKeystoreService;
import com.yiguang.payment.common.utils.StringUtil;
import com.yiguang.payment.depot.order.service.DepotOrderService;
import com.yiguang.payment.depot.service.ProductDepotService;
import com.yiguang.payment.payment.entity.Merchant;
import com.yiguang.payment.payment.order.entity.MerchantOrder;
import com.yiguang.payment.payment.order.service.MerchantOrderService;
import com.yiguang.payment.payment.rest.service.CheckRiskService;
import com.yiguang.payment.payment.risk.service.RiskService;
import com.yiguang.payment.payment.service.ChannelMerchantRelationService;
import com.yiguang.payment.payment.service.ChannelService;
import com.yiguang.payment.payment.service.MerchantService;
import com.yiguang.payment.rbac.entity.RoleUser;
import com.yiguang.payment.rbac.entity.User;
import com.yiguang.payment.rbac.service.RoleUserService;
import com.yiguang.payment.rbac.service.UserService;

/*
 * 接口控制类
 */
@Controller
@RequestMapping(value = "/mall/YGPAY")
public class YiPayController
{

	private static Logger logger = LoggerFactory.getLogger(YiPayController.class);

	@Autowired
	HttpSession session;
	@Autowired
	private ChannelService channelService;
	@Autowired
	private RiskService riskService;
	@Autowired
	private DataSourceService dataSourceService;
	@Autowired
	private CheckNumSectionService checkNumSectionService;
	@Autowired
	private ChannelMerchantRelationService ChannelMerchantRelationService;
	@Autowired
	private PointChannelRelationService pointChannelRelationService;
	@Autowired
	private MerchantService merchantService;
	@Autowired
	private CheckRiskService checkRiskService;
	@Autowired
	private MerchantOrderService merchantOrderService;
	@Autowired
	private DepotOrderService depotOrderService;
	@Autowired
	private ProductDepotService productDepotService;
	@Autowired
	private SecurityKeystoreService keystoreService;
	@Autowired
	private PointService pointService;
	@Autowired
	private UserService userService;
	@Autowired
	private RoleUserService roleUserService;
	@Autowired
	private ProductService productService;
	
	@RequestMapping(value = "/buy")
	public String buy(Model model,ServletRequest request)
	{
		try{
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
				List<Merchant> merchants = merchantService.queryMerchantByChannelId(3);
				merchantList = new ArrayList<OptionVO>();
				pointList = new ArrayList<OptionVO>();
				for(Merchant merchant : merchants)
				{
					OptionVO vo = new OptionVO();
					vo.setText(merchant.getName());
					vo.setValue(String.valueOf(merchant.getId()));
					merchantList.add(vo);
					
					List<Point> plist = pointService.queryPointByMerAndCh(merchant.getId(), 3);
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
				
				List<Point> plist = pointService.queryPointByMerAndCh(merchant.getId(),3);
				
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
			model.addAttribute("pointList", pointList);
			model.addAttribute("merchantList", merchantList);
			return "mall/YGPAY/buy";
		}
		catch (Exception e)
		{
			logger.error(e.getLocalizedMessage(), e);
			return "error/500";
		}
		
	}

	@RequestMapping(value = "/checkMobile", method = RequestMethod.POST)
	public @ResponseBody String checkMobile(@RequestParam(value = "mobile", defaultValue = "") String mobile,
			@RequestParam(value = "channelId", defaultValue = "") String channelId, Model model, ServletRequest request)
	{

		String mobile_regex = "^\\d{11}$";
		if (!Pattern.matches(mobile_regex, mobile))
		{
			return "手机号码不正确";
		}

		NumSection section = checkNumSectionService.checkNum(mobile);
		if (section.getCarrierInfo() == null)
		{
			return "手机号码不正确";
		}

		if (!StringUtil.equalsIgnoreCase(String.valueOf(section.getCarrierInfo().getId()), channelId))
		{
			return "手机号码与所选渠道不匹配";
		}

		return "0";
	}

	@RequestMapping(value = "/placeOrder")
	public String placeOrder(@RequestParam(value = "pointId", defaultValue = "-1") long pointId,
			@RequestParam(value = "channelId", defaultValue = "-1") long operator,
			@RequestParam(value = "mobile", defaultValue = "") String mobile, Model model,
			ServletRequest request)
	{
		logger.info("yiguang  placeOrder start!");
		try
		{
			
			NumSection section = checkNumSectionService.checkNum(mobile);
			HttpServletRequest httpRequest = (HttpServletRequest) request;
			String requestIp = IpTool.getIpAddr(httpRequest);
			Date date = new Date();
			String timestamp = RestConst.SDF_14.format(date);
			Double num = Math.random() * 9000 + 1000;
			String random = String.valueOf(num.intValue());
			Point point = pointService.queryPoint(pointId);
			String orderid = point.getMerchantId() + "00" + timestamp + random;
			Product product = productService.queryProduct(point.getProductId());
			if (operator == 3)
			{
				// 下订单
				MerchantOrder merchantOrder = new MerchantOrder();

				merchantOrder.setOrderId(orderid);
				merchantOrder.setMerchantOrderId(orderid);
				merchantOrder.setProductId(point.getProductId());
				merchantOrder.setMobile(mobile);
				merchantOrder.setChargingType(point.getChargingType());
				merchantOrder.setChargingPointId(point.getId());
				merchantOrder.setRequestIp(requestIp);

				merchantOrder.setRequestTime(new Date());
				merchantOrder.setCarrierId(section.getCarrierInfo().getId());
				merchantOrder.setChannelType(CommonConstant.ChannelType.DUANYAN);
				merchantOrder.setSubject(point.getName());
				merchantOrder.setDescription(point.getName());
				merchantOrder.setNotifyStatus(CommonConstant.NotifyStatus.NOT_NOTIFY);
				merchantOrder.setProvinceId(section.getProvince().getProvinceId());
				merchantOrder.setCityId(section.getCity().getCityId());

				merchantOrder.setFaceAmount(point.getFaceAmount());
				merchantOrder.setDeliveryAmount(point.getDeliveryAmount());
				merchantOrder.setPayAmount(point.getPayAmount());
				merchantOrder.setMerchantId(point.getMerchantId());
				merchantOrder.setPayStatus(CommonConstant.PayStatus.NOT_PAY);
				merchantOrder.setDeliveryStatus(CommonConstant.DeliveryStatus.NOT_DELIVERY);
				merchantOrder = checkRiskService.checkRiskAndChannel(merchantOrder);
				merchantOrder = merchantOrderService.createMerchantOrder(merchantOrder);

				model.addAttribute("sms_pay_url", RestConst.TELECOM_SMS_PAY_URL);
				model.addAttribute("MERCHANTID", RestConst.CT_CPID);
				model.addAttribute("SUBMERCHANTID", "");
				model.addAttribute("ORDERSEQ", merchantOrder.getOrderId());

				Double seq = Math.random() * 9000 + 1000;
				String seqStr = String.valueOf(seq.intValue());
				model.addAttribute("ORDERREQTRANSEQ", seqStr);
				model.addAttribute("ORDERDATE", timestamp);
				model.addAttribute("ORDERAMOUNT", merchantOrder.getPayAmount());
				model.addAttribute("PRODUCTAMOUNT", merchantOrder.getPayAmount());
				model.addAttribute("ATTACHAMOUNT", "0");
				model.addAttribute("CURTYPE", "RMB");
				model.addAttribute("ENCODETYPE", "1");

				String md5 = merchantOrder.getOrderId() + RestConst.CT_KEY;

				model.addAttribute("MERCHANTURL",
						RestConst.YI_NOTIFY_URL + "?orderid=" + merchantOrder.getOrderId() + "&productId=" + product.getId()
								+ "&chargingType=" + 1 + "&sign=" + MD5Util.getMD5Sign(md5));
				model.addAttribute("BACKMERCHANTURL", RestConst.TEL_SMS_NOTIFY_URL);
				model.addAttribute("BANKID", "TELECOM_BILL");
				model.addAttribute("ATTACH", String.valueOf(point.getMerchantId()));
				model.addAttribute("BUSICODE", "0001");
				model.addAttribute("PRODUCTID", "09");
				model.addAttribute("TMNUM", merchantOrder.getMobile());
				model.addAttribute("CUSTOMERID", merchantOrder.getUsername());
				model.addAttribute("PRODUCTDESC", point.getName());

				StringBuffer md5Str = new StringBuffer("MERCHANTID=");
				md5Str.append(RestConst.CT_CPID).append("&ORDERSEQ=").append(merchantOrder.getOrderId());
				md5Str.append("&ORDERDATE=").append(timestamp).append("&ORDERAMOUNT=");
				md5Str.append(merchantOrder.getPayAmount()).append("&CLIENTIP=");
				md5Str.append(merchantOrder.getRequestIp()).append("&KEY=").append(RestConst.CT_KEY);

				model.addAttribute("MAC", MD5Util.getMD5Sign(md5Str.toString()));
				model.addAttribute("DIVDETAILS", "");
				model.addAttribute("PEDCNT", "");
				model.addAttribute("GMTOVERTIME", "");
				model.addAttribute("GOODPAYTYPE", "0");
				model.addAttribute("GOODSCODE", point.getChargingCode());
				model.addAttribute("GOODSNAME", point.getName());
				model.addAttribute("GOODSNUM", "1");
				model.addAttribute("CLIENTIP", merchantOrder.getRequestIp());
				logger.info("yiguang  placeOrder end!");
				return "mall/YGPAY/telecomPay";
			}
			else
			{
				logger.error("不支持的渠道");
				model.addAttribute("errorcode", ErrorCodeConst.ErrorCode99998);
				model.addAttribute("errormsg", MessageResolver.getMessage(ErrorCodeConst.ErrorCode99998));
				return "mall/YGPAY/error";
			}

		}
		catch (RpcException e)
		{
			model.addAttribute("errorcode", e.getCode());
			model.addAttribute("errormsg", MessageResolver.getMessage(e.getCode()));
			return "mall/YGPAY/error";

		}
		catch (Exception e)
		{
			logger.error(e.getLocalizedMessage(), e);
			model.addAttribute("errorcode", ErrorCodeConst.ErrorCode99998);
			model.addAttribute("errormsg", MessageResolver.getMessage(ErrorCodeConst.ErrorCode99998));
			return "mall/YGPAY/error";
		}
	}
	

	@RequestMapping(value = "/changePoint", method = RequestMethod.POST)
	public @ResponseBody
	String changePoint(@RequestParam(value = "merchantId", defaultValue = "-1") long merchantId,
					   @RequestParam(value = "channelId", defaultValue = "-1") long channelId,Model model)
	{

		logger.debug("changeSonLong start");
		try
		{
			List<OptionVO> pointList = new ArrayList<OptionVO>();
			List<Point> plist = pointService.queryPointByMerAndCh(merchantId, 3);
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
	
	public static void main(String[] args) {
		String md5 = "200000201506241117144089" + RestConst.CT_KEY;
		String sin = MD5Util.getMD5Sign(md5);
		System.out.println(sin);
	}
}
