package com.yiguang.payment.merchantOperate.controller;

import java.security.interfaces.RSAPublicKey;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.yiguang.payment.business.product.entity.Product;
import com.yiguang.payment.business.product.service.ProductService;
import com.yiguang.payment.common.CommonConstant;
import com.yiguang.payment.common.Constant;
import com.yiguang.payment.common.RestConst;
import com.yiguang.payment.common.datasource.service.DataSourceService;
import com.yiguang.payment.common.datasource.vo.OptionVO;
import com.yiguang.payment.common.exception.ErrorCodeConst;
import com.yiguang.payment.common.message.MessageResolver;
import com.yiguang.payment.common.security.MD5Util;
import com.yiguang.payment.common.security.RSAUtils;
import com.yiguang.payment.common.security.service.SecurityKeystoreService;
import com.yiguang.payment.depot.order.service.DepotOrderService;
import com.yiguang.payment.depot.vo.CardAndPwdVO;
import com.yiguang.payment.payment.order.entity.MerchantOrder;
import com.yiguang.payment.payment.order.service.MerchantOrderService;
import com.yiguang.payment.payment.order.vo.MerchantOrderVO;
/*
 * 接口控制类
 */
@Controller
@RequestMapping(value = "/rest")
public class RestController {
	private static Logger logger = LoggerFactory.getLogger(RestController.class);
	@Autowired
	private DataSourceService dataSourceService;
	@Autowired
	private MerchantOrderService merchantOrderService;
	@Autowired
	private DepotOrderService depotOrderService;
	@Autowired
	private SecurityKeystoreService keystoreService;
	@Autowired
	private ProductService productService;
	@RequestMapping(value = "/queryOrder")
	public String queryOrder(@RequestParam(value = "orderid", defaultValue = "") String orderid,
			@RequestParam(value = "chargingType", defaultValue = "-1") int chargingType,
			@RequestParam(value = "productId", defaultValue = "-1") long productId,
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
			long merchantId = product.getMerchantId();
			MerchantOrder merchantOrder = merchantOrderService.queryMerchantOrderByMerchantOrderId(orderid,
					merchantId);
			MerchantOrderVO vo = merchantOrderService.copyPropertiesToVO(merchantOrder);

			OptionVO productVO = dataSourceService.findOptionVOById(CommonConstant.DataSourceName.PRODUCT,
					String.valueOf(productId));
			model.addAttribute("product", productVO);
			model.addAttribute("chargingType", chargingType);
			model.addAttribute("order", vo);

			return "rest/orderinfo";
		}
		catch (Exception e)
		{
			logger.error(e.getLocalizedMessage(), e);
			model.addAttribute("errorcode", ErrorCodeConst.ErrorCode99998);
			model.addAttribute("errormsg", MessageResolver.getMessage(ErrorCodeConst.ErrorCode99998));
			return "rest/error";
		}
	}
}
