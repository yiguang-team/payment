package com.yiguang.payment.depot.order.controller;

import java.security.interfaces.RSAPublicKey;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.alibaba.dubbo.rpc.RpcException;
import com.yiguang.payment.common.CommonConstant;
import com.yiguang.payment.common.Constant;
import com.yiguang.payment.common.IpTool;
import com.yiguang.payment.common.datasource.service.DataSourceService;
import com.yiguang.payment.common.datasource.vo.OptionVO;
import com.yiguang.payment.common.errorcode.service.ErrorCodeService;
import com.yiguang.payment.common.query.YcPage;
import com.yiguang.payment.common.security.RSAUtils;
import com.yiguang.payment.common.security.service.SecurityKeystoreService;
import com.yiguang.payment.depot.order.entity.DepotOrder;
import com.yiguang.payment.depot.order.service.DepotOrderService;
import com.yiguang.payment.depot.order.vo.DepotOrderVO;
import com.yiguang.payment.depot.vo.CardAndPwdVO;

/**
 * LoginController负责打开登录页面(GET请求)和登录出错页面(POST请求)， 真正登录的POST请求由Filter完成,
 * 
 * @author calvin
 */
@Controller
@RequestMapping(value = "/depot/depot")
public class DepotOrderController
{

	private static Logger logger = LoggerFactory.getLogger(DepotOrderController.class);

	@Autowired
	HttpSession session;
	@Autowired
	private DataSourceService dataSourceService;
	@Autowired
	private DepotOrderService depotOrderService;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private SecurityKeystoreService keystoreService;
	@Autowired
	private ErrorCodeService errorCodeService;

	private static final String PAGE_SIZE = "10";

	// 查询提卡历史
	@RequestMapping(value = "/pickUpRecordList")
	public String pickUpRecordList(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "pageSize", defaultValue = PAGE_SIZE) int pageSize,
			@RequestParam(value = "sortType", defaultValue = "auto") String sortType,
			@RequestParam(value = "extractUser", defaultValue = "-1") String extractUser,
			@RequestParam(value = "chargingPointId", defaultValue = "-1") int chargingPointId,
			@RequestParam(value = "orderId", defaultValue = "") String orderId, Model model, ServletRequest request)
	{
		logger.debug("[查询历史记录请求开始][url:/pickUpRecordList]--------------------------------------------");
		// 组装查询条件
		DepotOrderVO vo = new DepotOrderVO();
		vo.setExtractUser(extractUser);
		vo.setChargingPointId(chargingPointId);
		vo.setOrderId(orderId);
		logger.debug("[查询历史记录数据库开始][url:/pickUpRecordList]--------------------------------------------");
		// 根据查询提卡历史信息
		YcPage<DepotOrderVO> page_list = depotOrderService
				.queryPickUpRecordList(vo, pageNumber, pageSize, "");
		logger.debug("[查询历史记录数据库结束][url:/pickUpRecordList]--------------------------------------------");

		// 获取计费点名称，ID键值对
		List<OptionVO> pointList = dataSourceService.findOpenOptions(CommonConstant.DataSourceName.POINT_CARD_PWD);

		// 获取商户名称，ID键值对
		List<OptionVO> merchantList = dataSourceService.findOpenOptions(CommonConstant.DataSourceName.MERCHANT);
		
		// 将数据传入页面中
		model.addAttribute("extractUser", extractUser);
		model.addAttribute("chargingPointId", chargingPointId);
		model.addAttribute("orderId", orderId);
		model.addAttribute("mlist", page_list.getList());
		model.addAttribute("pointList", pointList);
		model.addAttribute("merchantList", merchantList);
		model.addAttribute("page", pageNumber);
		model.addAttribute("pageSize", pageSize);
		model.addAttribute("counttotal", page_list.getCountTotal() + "");
		model.addAttribute("pagetotal", page_list.getPageTotal() + "");
		logger.debug("[查询历史记录请求结束][url:/pickUpRecordList]--------------------------------------------");
		return "depot/depot/pickUpRecordList";
	}

	// 人工 提卡
	@RequestMapping(value = "/pickUpCarkManually")
	public String pickUpCarkManually(@RequestParam(value = "chargingPointId", defaultValue = "-1") int chargingPointId,
			@RequestParam(value = "merchantId", defaultValue = "-1") int merchantId,
			@RequestParam(value = "productId", defaultValue = "-1") int productId, Model model)
	{

		List<OptionVO> merchantList = dataSourceService.findOpenOptions(CommonConstant.DataSourceName.MERCHANT);

		List<OptionVO> productList = dataSourceService.findOpenOptionsByParent(CommonConstant.DataSourceName.PRODUCT,
				String.valueOf(merchantId));

		List<OptionVO> pointList = dataSourceService.findOpenOptions(CommonConstant.DataSourceName.POINT_CARD_PWD);

		model.addAttribute("chargingPointId", chargingPointId);
		model.addAttribute("pointList", pointList);
		model.addAttribute("merchantId", merchantId);
		model.addAttribute("merchantList", merchantList);
		model.addAttribute("productId", productId);
		model.addAttribute("productList", productList);
		List<CardAndPwdVO> als = new ArrayList<CardAndPwdVO>();
		if (chargingPointId == -1)
		{
			return "depot/depot/pickUpCardResult";
		}
		try
		{
			// 组装日志信息
			DepotOrder depotOrder = new DepotOrder();
			String requestIp = IpTool.getIpAddr(request);

			depotOrder.setRequestIp(requestIp);

			// 外部产品id对应内部计费点id
			depotOrder.setChargingPointId(chargingPointId);

			depotOrder.setExtractUser((String) session.getAttribute(Constant.Common.LOGIN_NAME_CACHE));

			// 设置日志生成渠道为人工提卡0:接口提卡1：人工提卡
			depotOrder.setExtractType(1);
			depotOrder.setExtractCount(1);
			depotOrder.setOrderId("88888888888");
			depotOrder.setPayAmount(0);
			depotOrder.setRequestTime(new Date());
			depotOrder.setReturnCode("0000");
			depotOrder.setReturnMessage("成功");
			// 提取卡密并记录提卡信息
			als = depotOrderService.pickUpCard(depotOrder, 1);
			// 获取公钥对象
			Map<String, Object> keyMap = keystoreService.getKeyObjectToMap(Constant.SecurityCredential.RSA_KEY);
			RSAPublicKey publicKey = (RSAPublicKey) RSAUtils.getPublicKeyByString((String) keyMap
					.get(Constant.RSACacheKey.RSA_PUBLICKEY));
			// 将密码解密
			for (CardAndPwdVO cardAndPwdVO : als)
			{
				cardAndPwdVO.setCardPwd(RSAUtils.decryptByPublicKey(cardAndPwdVO.getCardPwd(), publicKey));
			}
			model.addAttribute("mlist", als);
			return "depot/depot/pickUpCardResult";
		}
		catch (RpcException e)
		{
			String errorMsg = e.getMessage().length() == 4 ? errorCodeService.getErrorMsgByCode(e.getMessage()) : e
					.getMessage();
			logger.error("pickUpCarkManually failed: " + errorMsg);
			model.addAttribute("errorMsg", errorMsg);
			model.addAttribute("preUrl", request.getContextPath() + "/depot/depot/pickUpCarkManually");
			return "error/errorPage";
		}
		catch (Exception e)
		{
			logger.error("pickUpCarkManually failed");
			logger.error(e.getLocalizedMessage(), e);
			return "error/500";
		}
	}

}
