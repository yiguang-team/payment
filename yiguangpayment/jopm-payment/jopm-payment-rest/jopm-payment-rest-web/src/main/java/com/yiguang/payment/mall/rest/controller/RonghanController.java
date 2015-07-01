package com.yiguang.payment.mall.rest.controller;

import java.math.BigDecimal;
import java.security.interfaces.RSAPublicKey;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
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
import com.yiguang.payment.business.product.entity.PointChannelRelation;
import com.yiguang.payment.business.product.entity.Product;
import com.yiguang.payment.business.product.service.PointChannelRelationService;
import com.yiguang.payment.business.product.service.PointService;
import com.yiguang.payment.business.product.service.ProductService;
import com.yiguang.payment.business.product.vo.PointChannelRelationVO;
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
import com.yiguang.payment.common.security.RSAUtils;
import com.yiguang.payment.common.security.service.SecurityKeystoreService;
import com.yiguang.payment.common.utils.BigDecimalUtil;
import com.yiguang.payment.common.utils.StringUtil;
import com.yiguang.payment.depot.order.service.DepotOrderService;
import com.yiguang.payment.depot.service.ProductDepotService;
import com.yiguang.payment.depot.vo.CardAndPwdVO;
import com.yiguang.payment.payment.entity.Merchant;
import com.yiguang.payment.payment.order.entity.MerchantOrder;
import com.yiguang.payment.payment.order.service.MerchantOrderService;
import com.yiguang.payment.payment.order.vo.MerchantOrderVO;
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
@RequestMapping(value = "/mall/ronghan")
public class RonghanController
{

	private static Logger logger = LoggerFactory.getLogger(RonghanController.class);

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
	private ProductService productService;
	@Autowired
	private UserService userService;
	@Autowired
	private RoleUserService roleUserService;
	
	@RequestMapping(value = "/buy")
	public String buy(@RequestParam(value = "chargingType", defaultValue = "-1") int chargingType, Model model,
			ServletRequest request)
	{
		chargingType = 1;
		
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

		List<Product> productList = null;
		if (isAdmin)
		{
			List<Merchant> merchants = merchantService.queryMerchantByChannelId(3);
			merchantList = new ArrayList<OptionVO>();

			for(Merchant merchant : merchants)
			{
				OptionVO vo = new OptionVO();
				vo.setText(merchant.getName());
				vo.setValue(String.valueOf(merchant.getId()));
				merchantList.add(vo);
			}
			
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
		
		String merchantId = "";
		String productId = "";
		if (merchantList.size()==1)
		{
			merchantId = merchantList.get(0).getValue();
			
			productList = productService.queryProductBySupplierId(Long.parseLong(merchantId));
			
			if (productList.size()==1)
			{
				productId = String.valueOf(productList.get(0).getId());
			}
		}
		
		
		model.addAttribute("productId", productId);
		model.addAttribute("productList", productList);
		
		model.addAttribute("merchantId", merchantId);
		model.addAttribute("merchantList", merchantList);

		model.addAttribute("chargingType", chargingType);

		return "mall/ronghan/buy";
	}

	@RequestMapping(value = "/pointList", method = RequestMethod.POST)
	public @ResponseBody String pointList(@RequestParam(value = "channelId", defaultValue = "-1") long channelId,
			@RequestParam(value = "merchantId", defaultValue = "-1") long merchantId,
			@RequestParam(value = "productId", defaultValue = "-1") long productId,
			@RequestParam(value = "chargingType", defaultValue = "-1") int chargingType, Model model,ServletRequest request)
	{
		chargingType = 1;
		String html = "<select name=\"pointId\" id=\"pointId\" style=\"width:200px;height:34px;font-size:14px;margin-top:6px\">";
		List<PointChannelRelation> list = pointChannelRelationService.queryPointChannelRelationByChannelId(channelId);
		List<Point> pointList = pointService.queryPointByProductId(productId);
		for (Point p : pointList)
		{
			for (PointChannelRelation pcr : list)
			{
				if (merchantId == p.getMerchantId() && productId == p.getProductId() && chargingType == p.getChargingType()
						&& pcr.getStatus() == CommonConstant.CommonStatus.OPEN
						&& p.getStatus() == CommonConstant.CommonStatus.OPEN && p.getId() == pcr.getPointId()
						&& chargingType == p.getChargingType())
				{
					PointChannelRelationVO vo = pointChannelRelationService.copyPropertiesToVO(pcr);
					html = html + "<option value=\"" + vo.getPointId() + "\">" + vo.getPointLabel() + "</option>";
					break;
				}
			}
		}
		
		html = html + "</select>";
		return html;
	}

//	@RequestMapping(value = "/checkCode", method = RequestMethod.POST)
//	public @ResponseBody String checkCode(@RequestParam(value = "checkCode", defaultValue = "") String checkCode,
//			Model model, ServletRequest request)
//	{
//
//		String oldCode = (String) session.getAttribute("randCheckCode");
//		if (StringUtil.isNotEmpty(oldCode))
//		{
//			oldCode = oldCode.toUpperCase();
//			checkCode = checkCode.toUpperCase();
//
//			if (oldCode.equals(checkCode))
//			{
//				return "0";
//			}
//		}
//
//		return "9";
//	}
//
//	@RequestMapping(value = "/checkAccount", method = RequestMethod.POST)
//	public @ResponseBody String checkAccount(@RequestParam(value = "account", defaultValue = "") String account,
//			Model model, ServletRequest request)
//	{
//		logger.info("MALL checkAccount start,  account[" + account + "]");
//
//		try
//		{
//			String url = RestConst.JNET_VALIDATION;
//			String dateTime = RestConst.SDF_14.format(new Date());
//
//			StringBuffer md5 = new StringBuffer("");
//			md5.append("agent_id=").append(RestConst.APP_AGENT_ID);
//			md5.append("&user_account=").append(account);
//			md5.append("&time_stamp=").append(dateTime);
//
//			String md5Key = MD5Util.getMD5Sign(md5.toString() + "|||" + RestConst.APP_AGENT_KEY);
//			logger.info("checkAccount start!");
//			url = url.concat("?").concat(md5.toString()).concat("&sign=").concat(md5Key);
//			HttpGet get = new HttpGet(url);
//			HttpClient httpClient = new DefaultHttpClient();
//
//			HttpResponse response = null;
//
//			logger.info("MALL checkAccount url[" + url + "]");
//
//			response = httpClient.execute(get);
//			HttpEntity entity = response.getEntity();
//			InputStream instream = null;
//			String deliveryReturn = "";
//			instream = entity.getContent();
//
//			BufferedReader reader = new BufferedReader(new InputStreamReader(instream, "GB2312"));
//			String linet = reader.readLine();
//			while (null != linet)
//			{
//				deliveryReturn = deliveryReturn + linet;
//				linet = reader.readLine();
//			}
//
//			logger.info("MALL checkAccount response [" + deliveryReturn + "]");
//			Map<String, String> paraMap = new HashMap<String, String>();
//			String[] keyAndValues = deliveryReturn.split("&");
//			for (String keyAndValue : keyAndValues)
//			{
//				String[] s = keyAndValue.split("=");
//				if (s.length > 1)
//				{
//					paraMap.put(s[0], s[1]);
//				}
//				else
//				{
//					paraMap.put(s[0], "");
//				}
//			}
//
//			String ret_code = paraMap.get("ret_code");
//			String ret_msg = paraMap.get("ret_msg");
//
//			logger.info("MALL checkAccount end");
//			if ("0".equals(ret_code))
//			{
//				return "0";
//			}
//			else
//			{
//				return ret_msg;
//			}
//
//		}
//		catch (Exception e)
//		{
//			// 发货失败
//			logger.error("MALL checkAccount failed");
//			logger.error(e.getLocalizedMessage(), e);
//			return "系统异常";
//		}
//	}

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
			@RequestParam(value = "cpid", defaultValue = "-1") long cpid,
			@RequestParam(value = "mobile", defaultValue = "") String mobile,
			@RequestParam(value = "username", defaultValue = "") String username,
			@RequestParam(value = "chargingType", defaultValue = "-1") int chargingType, Model model,
			ServletRequest request)
	{
		logger.info("yiguang  placeOrder start!");
		try
		{
			chargingType = 1;
			
			if (chargingType == CommonConstant.CHARGING_TYPE.CARD)
			{
				// 库存校验
				int total = productDepotService.getUsableCount(pointId);
				if (total == 0)
				{
					model.addAttribute("errorcode", ErrorCodeConst.CODE_CARD_NOTENOUGH);
					model.addAttribute("errormsg", MessageResolver.getMessage(ErrorCodeConst.CODE_CARD_NOTENOUGH));
					return "mall/ronghan/error";
				}
			}
			
			NumSection section = checkNumSectionService.checkNum(mobile);
			Point point = pointService.queryPoint(pointId);
			HttpServletRequest httpRequest = (HttpServletRequest) request;
			String requestIp = IpTool.getIpAddr(httpRequest);
			Date date = new Date();
			String timestamp = RestConst.SDF_14.format(date);
			Double num = Math.random() * 9000 + 1000;
			String random = String.valueOf(num.intValue());
			String orderid = point.getMerchantId() + "00" + timestamp + random;
			
			Merchant merchant = merchantService.queryMerchant(point.getMerchantId());

			// 联通渠道
			if (operator == 2)
			{
				String signNoMd5 = orderid + cpid + point.getChargingCode() + mobile + operator + timestamp
						+ merchant.getKey();
				String smssign = MD5Util.getMD5Sign(signNoMd5);
				model.addAttribute("orderid", orderid);
				model.addAttribute("pointName", point.getName());
				model.addAttribute("payAmount", BigDecimalUtil.divide(point.getPayAmount(), new BigDecimal(
						"100"), 2, BigDecimal.ROUND_HALF_UP));
				model.addAttribute("username", username);
				model.addAttribute("serviceid", point.getChargingCode());
				model.addAttribute("description", point.getName());
				model.addAttribute("operator",operator);
				model.addAttribute("showUrl", "");
				model.addAttribute("notifyUrl", "");
				model.addAttribute("subject", point.getName());
				model.addAttribute("cpid", cpid);
				model.addAttribute("datetime", timestamp);
				String querySign = orderid + RestConst.CT_KEY;
				model.addAttribute("querySign", MD5Util.getMD5Sign(querySign));
				model.addAttribute("mobile", mobile);

				model.addAttribute("smssign", smssign);
				model.addAttribute("productId", point.getProductId());
				String paysign = orderid + cpid + point.getChargingCode() + mobile + operator + timestamp + merchant.getKey();
				paysign = MD5Util.getMD5Sign(paysign);
				model.addAttribute("paysign", paysign);
				model.addAttribute("chargingType", chargingType);

				OptionVO product = dataSourceService.findOptionVOById(CommonConstant.DataSourceName.PRODUCT,
						String.valueOf(point.getProductId()));
				model.addAttribute("productName", product.getText());

				logger.info("yiguang  placeOrder end!");
				return "mall/ronghan/unicomPay";

				// 电信渠道
			}
			else if (operator == 3)
			{
				// 下订单
				MerchantOrder merchantOrder = new MerchantOrder();

				merchantOrder.setOrderId(orderid);
				merchantOrder.setMerchantOrderId(orderid);
				merchantOrder.setProductId(point.getProductId());
				merchantOrder.setMobile(mobile);
				merchantOrder.setUsername(username);
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
						RestConst.RONGHAN_QUERY_ORDER_URL + "?orderid=" + merchantOrder.getOrderId() + "&productId=" + point.getProductId()
								+ "&chargingType=" + chargingType + "&sign=" + MD5Util.getMD5Sign(md5));
				model.addAttribute("BACKMERCHANTURL", RestConst.TEL_SMS_NOTIFY_URL);
				model.addAttribute("BANKID", "TELECOM_BILL");
				model.addAttribute("ATTACH", point.getMerchantId());
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
				return "mall/ronghan/telecomPay";
			}
			else
			{
				logger.error("不支持的渠道");
				model.addAttribute("errorcode", ErrorCodeConst.ErrorCode99998);
				model.addAttribute("errormsg", MessageResolver.getMessage(ErrorCodeConst.ErrorCode99998));
				return "mall/ronghan/error";
			}

		}
		catch (RpcException e)
		{
			model.addAttribute("errorcode", e.getCode());
			model.addAttribute("errormsg", MessageResolver.getMessage(e.getCode()));
			return "mall/ronghan/error";

		}
		catch (Exception e)
		{
			logger.error(e.getLocalizedMessage(), e);
			model.addAttribute("errorcode", ErrorCodeConst.ErrorCode99998);
			model.addAttribute("errormsg", MessageResolver.getMessage(ErrorCodeConst.ErrorCode99998));
			return "mall/ronghan/error";
		}
	}

	@RequestMapping(value = "/queryOrder")
	public String queryOrder(@RequestParam(value = "orderid", defaultValue = "") String orderid,
			@RequestParam(value = "productId", defaultValue = "-1") int productId,
			@RequestParam(value = "chargingType", defaultValue = "-1") int chargingType,
			@RequestParam(value = "sign", defaultValue = "") String sign, Model model, ServletRequest request)
	{
		try
		{
			String md5 = orderid + RestConst.CT_KEY;
			if (!MD5Util.getMD5Sign(md5).toLowerCase().equals(sign.toLowerCase()))
			{
				throw new Exception();
			}
			Product product = productService.queryProduct(productId);
			MerchantOrder merchantOrder = merchantOrderService.queryMerchantOrderByMerchantOrderId(orderid,
					product.getMerchantId());
			MerchantOrderVO vo = merchantOrderService.copyPropertiesToVO(merchantOrder);

			OptionVO productVO = dataSourceService.findOptionVOById(CommonConstant.DataSourceName.PRODUCT,
					String.valueOf(vo.getProductId()));
			model.addAttribute("product", productVO);
			model.addAttribute("chargingType", chargingType);
			model.addAttribute("order", vo);

			List<CardAndPwdVO> list = depotOrderService.getHisCardAndPwdByCond(merchantOrder.getOrderId(),
					String.valueOf(product.getMerchantId()));
			// 获取私钥对象
			Map<String, Object> keyMap = keystoreService.getKeyObjectToMap(Constant.SecurityCredential.RSA_KEY);
			RSAPublicKey publicKey = (RSAPublicKey) RSAUtils.getPublicKeyByString((String) keyMap
					.get(Constant.RSACacheKey.RSA_PUBLICKEY));
			// 将RSA加密后的密码存入数据库
			for (CardAndPwdVO cardAndPwdVO : list)
			{
				cardAndPwdVO.setCardPwd(RSAUtils.decryptByPublicKey(cardAndPwdVO.getCardPwd(), publicKey));
			}
			model.addAttribute("mlist", list);
			return "mall/ronghan/orderinfo";
		}
		catch (Exception e)
		{
			logger.error(e.getLocalizedMessage(), e);
			model.addAttribute("errorcode", ErrorCodeConst.ErrorCode99998);
			model.addAttribute("errormsg", MessageResolver.getMessage(ErrorCodeConst.ErrorCode99998));
			return "mall/ronghan/error";
		}
	}
}
