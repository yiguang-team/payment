package com.yiguang.payment.depot.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.rpc.RpcException;
import com.yiguang.payment.common.CommonConstant;
import com.yiguang.payment.common.Constant;
import com.yiguang.payment.common.IpTool;
import com.yiguang.payment.common.JsonTool;
import com.yiguang.payment.common.datasource.service.DataSourceService;
import com.yiguang.payment.common.datasource.vo.OptionVO;
import com.yiguang.payment.common.logging.entity.OperationLog;
import com.yiguang.payment.common.logging.service.OperationLogService;
import com.yiguang.payment.common.message.MessageResolver;
import com.yiguang.payment.common.query.YcPage;
import com.yiguang.payment.depot.service.ProductBatchService;
import com.yiguang.payment.depot.vo.ProductBatchVO;

/**
 * LoginController负责打开登录页面(GET请求)和登录出错页面(POST请求)， 真正登录的POST请求由Filter完成,
 * 
 * @author calvin
 */
@Controller
@RequestMapping(value = "/depot/batch")
public class BatchController
{

	@Autowired
	HttpSession session;

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private ProductBatchService productBatchService;

	@Autowired
	private DataSourceService dataSourceService;

	@Autowired
	private OperationLogService operationLogService;

	private static final String PAGE_SIZE = "10";

	@RequestMapping(value = "/batchList")
	public String cardProductList(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "pageSize", defaultValue = PAGE_SIZE) int pageSize,
			@RequestParam(value = "sortType", defaultValue = "auto") String sortType,
			@RequestParam(value = "merchantId", defaultValue = "-1") int merchantId,
			@RequestParam(value = "productId", defaultValue = "-1") int productId,
			@RequestParam(value = "batchId", defaultValue = "") String batchId,
			@RequestParam(value = "status", defaultValue = "-1") int status, Model model, ServletRequest request)
	{
		try
		{
			ProductBatchVO vo = new ProductBatchVO();
			vo.setMerchantId(merchantId);
			vo.setProductId(productId);
			vo.setBatchId(batchId);
			vo.setStatus(status);
			
			
			YcPage<ProductBatchVO> page_list = productBatchService.queryProductBatchList(vo, pageNumber,
					pageSize, "");

			List<OptionVO> statusList = dataSourceService.findOpenOptions(CommonConstant.DataSourceName.COMMON_STATUS);
			List<OptionVO> merchantList = dataSourceService.findOpenOptions(CommonConstant.DataSourceName.MERCHANT);
			List<OptionVO> productList = dataSourceService.findOpenOptions(CommonConstant.DataSourceName.PRODUCT);

			model.addAttribute("batchId", batchId);
			model.addAttribute("status", status);
			model.addAttribute("statusList", statusList);
			model.addAttribute("merchantId", merchantId);
			model.addAttribute("merchantList", merchantList);
			model.addAttribute("productId", productId);
			model.addAttribute("productList", productList);

			model.addAttribute("mlist", page_list.getList());
			model.addAttribute("page", pageNumber);
			model.addAttribute("pageSize", pageSize);
			model.addAttribute("counttotal", page_list.getCountTotal() + "");
			model.addAttribute("pagetotal", page_list.getPageTotal() + "");

			return "depot/batch/batchList";
		}
		catch (Exception e)
		{
			return "error/500";
		}

	}

	@RequestMapping(method = RequestMethod.POST, value = "/updateBatchStatus")
	public @ResponseBody String updateBatchStatus(int tag, String batchId)
	{

		Map<String, String> result = new HashMap<String, String>();
		try
		{
			productBatchService.updateBatchStatus(tag, batchId);

			// 记录操作日志 开始
			String status = "";
			if(tag == CommonConstant.CommonStatus.OPEN)
			{
				status ="上架";
			}else 
			{
				status ="下架";
			}
			OperationLog log = new OperationLog();
			log.setUsername(String.valueOf(session.getAttribute(Constant.Common.LOGIN_NAME_CACHE)));
			log.setOperationType(CommonConstant.LogOperationType.MODIFY);
			log.setOperationIp(IpTool.getIpAddr(request));
			log.setOperationTime(new Date());
			log.setOperationObj("卡密批次");
			log.setRemark("批次id： " + batchId +" "+status);
			operationLogService.recordOperationLog(log);

			// 记录操作日志 结束
			result.put("result", "success");
		}
		catch (RpcException e)
		{
			result.put("result", "error");
			result.put("message", "操作失败[" + MessageResolver.getMessage(e.getCode()) + "]");
		}

		String json = JsonTool.beanToJson(result);
		return json;
	}

}
