package com.yiguang.payment.depot.rest.controller;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.rpc.RpcException;
import com.yiguang.payment.common.DateTimeHelper;
import com.yiguang.payment.common.IpTool;
import com.yiguang.payment.common.errorcode.service.ErrorCodeService;
import com.yiguang.payment.common.exception.ErrorCodeConst;
import com.yiguang.payment.common.security.MD5Util;
import com.yiguang.payment.common.utils.StringUtil;
import com.yiguang.payment.depot.order.entity.DepotOrder;
import com.yiguang.payment.depot.order.service.DepotOrderService;
import com.yiguang.payment.depot.order.vo.DepotOrderResultVO;
import com.yiguang.payment.depot.vo.CardAndPwdVO;
import com.yiguang.payment.payment.entity.Merchant;
import com.yiguang.payment.payment.service.MerchantService;

/*
 * 接口控制类
 */
@Controller
@RequestMapping(value = "/rest")
public class PickUpCardController
{

	private static Logger logger = LoggerFactory.getLogger(PickUpCardController.class);

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private ErrorCodeService errorCodeService;

	@Autowired
	private DepotOrderService depotOrderService;

	@Autowired
	private MerchantService merchantService;

	/*
	 * 提卡接口
	 */
	@RequestMapping(value = "/pickUpCard", method = RequestMethod.POST)
	public @ResponseBody
	DepotOrderResultVO pickUpCard(String proid, String dealerid, String datetime, String sign, String orderid,
			String payamount, String count)
	{

		// 初始化返回码
		String code = ErrorCodeConst.CODE_SUCCESS;
		List<CardAndPwdVO> als = null;

		String regionString = proid + dealerid + orderid + datetime;

		try
		{
			// 验证数据的合法性(非空,类型,长度校验)
			code = validateData(proid, dealerid, datetime, sign, orderid, payamount, count);

			// 如果第一阶段校验通过则继续往下校验
			if (ErrorCodeConst.CODE_SUCCESS.equals(code))
			{

				// 验证数据来源合法性
				code = validateDataSource(datetime, sign, code, regionString, dealerid);

				// 如果第二阶段校验通过则继续往下校验
				if (ErrorCodeConst.CODE_SUCCESS.equals(code))
				{

					// 组装日志信息
					DepotOrder depotOrder = new DepotOrder();
					String requestIp = IpTool.getIpAddr(request);

					depotOrder.setRequestIp(requestIp);

					// 外部产品id对应内部计费点id
					depotOrder.setChargingPointId(Long.parseLong(proid));

					depotOrder.setExtractUser(dealerid);

					// 设置日志生成渠道为接口提卡
					depotOrder.setExtractType(0);
					depotOrder.setExtractCount(Integer.parseInt(count));
					depotOrder.setOrderId(orderid);
					depotOrder.setPayAmount(Long.parseLong(payamount));
					depotOrder.setRequestTime(DateTimeHelper.getDateTimeByString(datetime));
					depotOrder.setReturnCode(code);
					depotOrder.setReturnMessage(errorCodeService.getErrorMsgByCode(code));

					// 提取卡密并记录提卡信息
					als = depotOrderService.pickUpCard(depotOrder, Integer.parseInt(count));

				}

			}
		}
		catch (RpcException rpcex)
		{
			// 接收服务层跑出来的错误码
			String errorMsg = rpcex.getMessage();
			// 如果长度不等于4，说明不是已定义错误码，抛出提卡错误提示
			if (errorMsg.length() == 4)
			{
				code = errorMsg;
			}
			else
			{
				logger.error(errorMsg);
				code = ErrorCodeConst.CODE_PICKUP_CARD_ERR;
			}

		}
		catch (Exception e)
		{
			logger.error(e.getMessage());
			// 未知异常返回提卡失败错误码
			code = ErrorCodeConst.CODE_PICKUP_CARD_ERR;
		}

		// 获得错误码对应的错误信息
		String message = errorCodeService.getErrorMsgByCode(code);

		// 返回结果对象
		return new DepotOrderResultVO(code, message, als);
	}

	// 验证数据来源合法性
	private String validateDataSource(String datetime, String sign, String code, String regionString, String dealerid)
	{

		Date reqTime = DateTimeHelper.getDateTimeByString(datetime);

		// 客户端请求与服务段响应时间差是否合法(5分钟之内)
		if (!DateTimeHelper.IsNowBetweenMins(reqTime, 5))
		{

			code = ErrorCodeConst.CODE_REQUEST_TIMEOUT;

		}
		else
		{

			// 获取代理商
			Merchant merchant = merchantService.queryMerchant(Long.parseLong(dealerid));

			// 代理商不存在
			if (merchant == null)
			{

				code = ErrorCodeConst.CODE_MERCHANT_UNUSABLE;

			}
			else
			{

				// 密钥+字段MD5加密是否匹配
				String regionSign = MD5Util.getMD5Sign(regionString + merchant.getKey());

				if (!sign.equals(regionSign))
				{
					code = ErrorCodeConst.CODE_SIGN_MATCH_FAILED;
				}
			}

		}
		return code;
	}

	// 验证数据的合法性(非空,类型,长度校验)
	private String validateData(String proid, String dealerid, String datetime, String sign, String orderid,
			String payamount, String count)
	{

		String code = ErrorCodeConst.CODE_SUCCESS;

		// 检查产品ID格式
		if (StringUtil.isBlank(proid) || !proid.matches("[1-9]\\d*") || proid.length() > 10)
		{
			code = ErrorCodeConst.CODE_PROID_FORMATE_ERR;

		}
		// 检查代理商ID格式
		else if (StringUtil.isBlank(dealerid) || !dealerid.matches("[1-9]\\d*") || dealerid.length() > 10)
		{
			code = ErrorCodeConst.CODE_DEALERID_FORMATE_ERR;

		}
		// 检查时间日期格式
		else if (StringUtil.isBlank(datetime) || !datetime.matches("\\d{14}"))
		{
			code = ErrorCodeConst.CODE_DATETIME_FORMATE_ERR;

		}
		// 检查签名格式
		else if (StringUtil.isBlank(sign) || !sign.matches("[0-9a-zA-Z]{32}"))
		{
			code = ErrorCodeConst.CODE_SIGN_FORMATE_ERR;

		}
		// 检查订单ID格式
		else if (StringUtil.isBlank(orderid) || !orderid.matches("[0-9a-zA-Z]+") || orderid.length() > 32)
		{
			code = ErrorCodeConst.CODE_ORDERID_FORMATE_ERR;

		}
		// 检查交易金额格式
		else if (StringUtil.isBlank(payamount) || !payamount.matches("[1-9]\\d*") || payamount.length() > 8)
		{
			code = ErrorCodeConst.CODE_PAYAMOUNT_FORMATE_ERR;

		}
		// 检查请求卡密数量格式
		else if (StringUtil.isBlank(count) || !count.matches("[1-9]\\d*") || count.length() > 8)
		{
			code = ErrorCodeConst.CODE_COUNT_FORMATE_ERR;

		}
		return code;
	}

}
