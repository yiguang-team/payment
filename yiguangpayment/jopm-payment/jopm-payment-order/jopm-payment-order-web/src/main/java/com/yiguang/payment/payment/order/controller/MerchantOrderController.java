package com.yiguang.payment.payment.order.controller;

import java.io.OutputStream;
import java.security.interfaces.RSAPublicKey;
import java.util.List;
import java.util.Map;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.yiguang.payment.common.CommonConstant;
import com.yiguang.payment.common.Constant;
import com.yiguang.payment.common.RestConst;
import com.yiguang.payment.common.datasource.service.DataSourceService;
import com.yiguang.payment.common.datasource.vo.OptionVO;
import com.yiguang.payment.common.query.YcPage;
import com.yiguang.payment.common.security.RSAUtils;
import com.yiguang.payment.common.security.service.SecurityKeystoreService;
import com.yiguang.payment.common.utils.StringUtil;
import com.yiguang.payment.depot.order.service.DepotOrderService;
import com.yiguang.payment.depot.vo.CardAndPwdVO;
import com.yiguang.payment.merchantOperate.service.MerchantOperateService;
import com.yiguang.payment.payment.order.entity.MerchantOrder;
import com.yiguang.payment.payment.order.service.MerchantOrderService;
import com.yiguang.payment.payment.order.vo.MerchantOrderVO;

/**
 * LoginController负责打开登录页面(GET请求)和登录出错页面(POST请求)， 真正登录的POST请求由Filter完成,
 * 
 * @author Shinalon
 */
@Controller
@RequestMapping(value = "/mall/order")
public class MerchantOrderController
{

	@Autowired
	HttpSession session;
	@Autowired
	private SecurityKeystoreService keystoreService;
	@Autowired
	MerchantOrderService orderService;
	@Autowired
	private DepotOrderService depotOrderService;
	@Autowired
	DataSourceService dataSourceService;
	@Autowired
	private MerchantOperateService merchantOperateService;
	private static final String PAGE_SIZE = "10";

	private static Logger logger = LoggerFactory.getLogger(MerchantOrderController.class);

	@RequestMapping(value = "/merchantOrderList")
	public String merchantOrderList(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "pageSize", defaultValue = PAGE_SIZE) int pageSize,
			@RequestParam(value = "sortType", defaultValue = "auto") String sortType,
			@RequestParam(value = "orderId", defaultValue = "") String orderId,
			@RequestParam(value = "merchantOrderId", defaultValue = "") String merchantOrderId,
			@RequestParam(value = "requestIp", defaultValue = "") String requestIp,
			@RequestParam(value = "username", defaultValue = "") String username,
			@RequestParam(value = "mobile", defaultValue = "") String mobile,
			@RequestParam(value = "channelId", defaultValue = "-1") long channelId,
			@RequestParam(value = "carrierId", defaultValue = "-1") long carrierId,
			@RequestParam(value = "merchantId", defaultValue = "-1") long merchantId,
			@RequestParam(value = "provinceId", defaultValue = "-1") String provinceId,
			@RequestParam(value = "cityId", defaultValue = "-1") String cityId,
			@RequestParam(value = "productId", defaultValue = "-1") long productId,
			@RequestParam(value = "payStatus", defaultValue = "-1") int payStatus,
			@RequestParam(value = "deliveryStatus", defaultValue = "-1") int deliveryStatus,
			@RequestParam(value = "deliveryNo", defaultValue = "") String deliveryNo,
			@RequestParam(value = "chargingPointId", defaultValue = "-1") long chargingPointId,
			@RequestParam(value = "chargingType", defaultValue = "-1") int chargingType,
			@RequestParam(value = "channelType", defaultValue = "-1") int channelType,
			@RequestParam(value = "notifyStatus", defaultValue = "-1") int notifyStatus,
			@RequestParam(value = "ip", defaultValue = "") String ip,
			@RequestParam(value = "beginDate", defaultValue = "") String beginDate,
			@RequestParam(value = "endDate", defaultValue = "") String endDate, Model model, ServletRequest request)
	{
		try
		{
			if (StringUtil.isNotBlank(orderId))
			{
				orderId = orderId.trim();
			}
			if (StringUtil.isNotBlank(merchantOrderId))
			{
				merchantOrderId = merchantOrderId.trim();
			}
			if (StringUtil.isNotBlank(requestIp))
			{
				requestIp = requestIp.trim();
			}
			if (StringUtil.isNotBlank(username))
			{
				username = username.trim();
			}
			if (StringUtil.isNotBlank(mobile))
			{
				mobile = mobile.trim();
			}
			if (StringUtil.isNotBlank(deliveryNo))
			{
				deliveryNo = deliveryNo.trim();
			}
			if (StringUtil.isNotBlank(beginDate))
			{
				beginDate = beginDate.trim();
			}
			if (StringUtil.isNotBlank(endDate))
			{
				endDate = endDate.trim();
			}
			
			YcPage<MerchantOrderVO> page_list = orderService.queryMerchantOrderList(pageNumber, pageSize,
					sortType, orderId, merchantOrderId, requestIp, username, mobile, carrierId, channelId, merchantId,
					provinceId, cityId, productId, payStatus, deliveryStatus, deliveryNo, chargingPointId,
					chargingType, channelType, notifyStatus, beginDate, endDate);

			List<OptionVO> payStatusList = dataSourceService.findOpenOptions(CommonConstant.DataSourceName.PAY_STATUS);
			List<OptionVO> deliveryStatusList = dataSourceService
					.findOpenOptions(CommonConstant.DataSourceName.DELIVERY_STATUS);
			List<OptionVO> chargingTypeList = dataSourceService
					.findOpenOptions(CommonConstant.DataSourceName.CHARGING_TYPE);
			List<OptionVO> productList = dataSourceService.findOpenOptionsByParent(
					CommonConstant.DataSourceName.PRODUCT, String.valueOf(merchantId));
			List<OptionVO> cityList = dataSourceService.findOpenOptionsByParent(CommonConstant.DataSourceName.CITY,
					provinceId);
			List<OptionVO> provinceList = dataSourceService.findOpenOptions(CommonConstant.DataSourceName.PROVINCE);
			List<OptionVO> merchantList = dataSourceService.findOpenOptions(CommonConstant.DataSourceName.MERCHANT);
			List<OptionVO> pointList = dataSourceService.findOpenOptions(CommonConstant.DataSourceName.POINT);
			List<OptionVO> channelList = dataSourceService.findOpenOptions(CommonConstant.DataSourceName.CHANNEL);
			List<OptionVO> carrierList = dataSourceService.findOpenOptions(CommonConstant.DataSourceName.CARRIER);
			List<OptionVO> channelTypeList = dataSourceService
					.findOpenOptions(CommonConstant.DataSourceName.CHANNEL_TYPE);
			List<OptionVO> notifyStatusList = dataSourceService
					.findOpenOptions(CommonConstant.DataSourceName.NOTIFY_STATUS);
			model.addAttribute("orderId", orderId);
			model.addAttribute("merchantOrderId", merchantOrderId);
			model.addAttribute("requestIp", requestIp);
			model.addAttribute("username", username);
			model.addAttribute("mobile", mobile);
			model.addAttribute("channelId", channelId);
			model.addAttribute("channelList", channelList);
			model.addAttribute("channelType", channelType);
			model.addAttribute("channelTypeList", channelTypeList);
			model.addAttribute("notifyStatus", notifyStatus);
			model.addAttribute("notifyStatusList", notifyStatusList);
			model.addAttribute("carrierId", carrierId);
			model.addAttribute("carrierList", carrierList);
			model.addAttribute("merchantId", merchantId);
			model.addAttribute("merchantList", merchantList);
			model.addAttribute("provinceId", provinceId);
			model.addAttribute("provinceList", provinceList);
			model.addAttribute("cityId", cityId);
			model.addAttribute("productList", productList);
			model.addAttribute("productId", productId);
			model.addAttribute("cityList", cityList);
			model.addAttribute("deliveryNo", deliveryNo);
			model.addAttribute("payStatus", payStatus);
			model.addAttribute("payStatusList", payStatusList);
			model.addAttribute("deliveryStatus", deliveryStatus);
			model.addAttribute("deliveryStatusList", deliveryStatusList);
			model.addAttribute("chargingPointId", chargingPointId);
			model.addAttribute("pointList", pointList);
			model.addAttribute("chargingType", chargingType);
			model.addAttribute("chargingTypeList", chargingTypeList);
			model.addAttribute("mlist", page_list.getList());
			model.addAttribute("beginDate", beginDate);
			model.addAttribute("endDate", endDate);
			model.addAttribute("page", pageNumber);
			model.addAttribute("pageSize", pageSize);
			model.addAttribute("counttotal", page_list.getCountTotal() + "");
			model.addAttribute("pagetotal", page_list.getPageTotal() + "");

			return "mall/order/merchantOrderList";
		}
		catch (Exception e)
		{
			logger.error(e.getLocalizedMessage(), e);
			return "error/500";
		}
	}

	@RequestMapping(value = "/merchantOrderInfo")
	public String merchantOrderInfo(@RequestParam(value = "orderId", defaultValue = "") String orderId, Model model)
	{
		try
		{
			MerchantOrder merchantOrder = orderService.queryMerchantOrderByOrderId(orderId);
			MerchantOrderVO vo = orderService.copyPropertiesToVO(merchantOrder);
			model.addAttribute("order", vo);

			List<CardAndPwdVO> list = depotOrderService.getHisCardAndPwdByCond(merchantOrder.getOrderId(),
					RestConst.YIGUANG_MERCHANT_ID);
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

			return "mall/order/merchantOrderInfo";
		}
		catch (Exception e)
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
			String orderId = request.getParameter("orderId");
			String merchantOrderId = request.getParameter("merchantOrderId");
			String mobile = request.getParameter("mobile");
			String beginDate = request.getParameter("beginDate");
			String username = request.getParameter("username");
			String provinceId = request.getParameter("provinceId");
			String endDate = request.getParameter("endDate");
			String channelId = request.getParameter("channelId");
			String carrierId = request.getParameter("carrierId");
			String merchantId = request.getParameter("merchantId");
			String cityId = request.getParameter("cityId");
			String productId = request.getParameter("productId");
			String payStatus = request.getParameter("payStatus");
			String deliveryStatus = request.getParameter("deliveryStatus");
			String chargingPointId = request.getParameter("chargingPointId");
			String chargingType = request.getParameter("chargingType");
			String channelType = request.getParameter("channelType");
			String notifyStatus = request.getParameter("notifyStatus");
			
			List<MerchantOrderVO> list = 
					merchantOperateService.getOrderList(orderId, merchantOrderId,mobile, beginDate, username, provinceId,endDate,channelId,carrierId,merchantId,
							cityId,productId,payStatus,deliveryStatus,chargingPointId,chargingType,channelType,notifyStatus);
			logger.debug("[MerchantOperateServiceImpl: getOrderList list= (" + list.toString()
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
	
	public HSSFWorkbook export(List<MerchantOrderVO> list)
	{
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet("号码充值金额");
		HSSFRow row = sheet.createRow((int) 0);
		HSSFCellStyle style = wb.createCellStyle();
		for (int i = 0; i < list.size(); i++)
		{
			row = sheet.createRow(i);
			MerchantOrderVO mat = list.get(i);
			row.createCell(0).setCellValue(mat.getOrderId());
			row.createCell(1).setCellValue(mat.getMobile());
			row.createCell(2).setCellValue(mat.getPayAmount().doubleValue());
			if(StringUtil.isNotBlank(mat.getUsername()) && !"null".equals(mat.getUsername()))
			{
				row.createCell(3).setCellValue(mat.getUsername());
			}
			else
			{
				row.createCell(3).setCellValue("--------------------");
			}
			row.createCell(4).setCellValue(mat.getRequestTime());
		}
		return wb;
	}
}
