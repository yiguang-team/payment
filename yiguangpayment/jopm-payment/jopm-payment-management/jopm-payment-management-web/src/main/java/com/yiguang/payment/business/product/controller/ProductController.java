package com.yiguang.payment.business.product.controller;

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
import com.yiguang.payment.business.product.entity.Product;
import com.yiguang.payment.business.product.service.ProductService;
import com.yiguang.payment.business.product.vo.ProductVO;
import com.yiguang.payment.common.CommonConstant;
import com.yiguang.payment.common.JsonTool;
import com.yiguang.payment.common.datasource.service.DataSourceService;
import com.yiguang.payment.common.datasource.vo.OptionVO;
import com.yiguang.payment.common.message.MessageResolver;
import com.yiguang.payment.common.query.YcPage;
import com.yiguang.payment.common.utils.StringUtil;

/**
 * LoginController负责打开登录页面(GET请求)和登录出错页面(POST请求)， 真正登录的POST请求由Filter完成,
 * 
 * @author calvin
 */
@Controller
@RequestMapping(value = "/mall/management/product")
public class ProductController
{

	@Autowired
	HttpSession session;

	@Autowired
	private ProductService productService;

	@Autowired
	private DataSourceService dataSourceService;

	private static final String PAGE_SIZE = "10";

	private static Logger logger = LoggerFactory.getLogger(ProductController.class);

	@RequestMapping(value = "/productList")
	public String cardProductList(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "pageSize", defaultValue = PAGE_SIZE) int pageSize,
			@RequestParam(value = "sortType", defaultValue = "auto") String sortType,
			@RequestParam(value = "name", defaultValue = "") String name,
			@RequestParam(value = "merchantId", defaultValue = "-1") long merchantId,
			@RequestParam(value = "type", defaultValue = "-1") int type,
			@RequestParam(value = "status", defaultValue = "-1") int status, Model model, ServletRequest request)
	{
		try
		{
			ProductVO vo = new ProductVO();
			vo.setType(type);
			vo.setStatus(status);
			vo.setMerchantId(merchantId);
			vo.setName(name);
			
			YcPage<ProductVO> page_list = productService.queryProductList(vo, pageNumber, pageSize, "");

			List<OptionVO> typeList = dataSourceService.findOpenOptions(CommonConstant.DataSourceName.PRODUCT_TYPE);

			List<OptionVO> statusList = dataSourceService.findOpenOptions(CommonConstant.DataSourceName.COMMON_STATUS);

			List<OptionVO> merchantList = dataSourceService.findOpenOptions(CommonConstant.DataSourceName.MERCHANT);

			model.addAttribute("status", status);
			model.addAttribute("type", type);
			model.addAttribute("merchantId", merchantId);
			model.addAttribute("typeList", typeList);
			model.addAttribute("statusList", statusList);
			model.addAttribute("merchantList", merchantList);
			model.addAttribute("name", name);
			model.addAttribute("mlist", page_list.getList());
			model.addAttribute("page", pageNumber);
			model.addAttribute("pageSize", pageSize);
			model.addAttribute("counttotal", page_list.getCountTotal() + "");
			model.addAttribute("pagetotal", page_list.getPageTotal() + "");

			return "mall/management/product/productList";
		}
		catch (Exception e)
		{
			logger.error(e.getLocalizedMessage(), e);
			return "error/500";
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = "/updateProductStatus")
	public @ResponseBody
	String updateProductStatus(Product cardProduct, ModelMap model)
	{

		Map<String, String> result = new HashMap<String, String>();
		try
		{
			productService.updateProductStatus(cardProduct);

			result.put("result", "success");

		}
		catch (RpcException e)
		{
			result.put("result", "error");
			result.put("message", "操作失败[" + MessageResolver.getMessage(e.getCode()) + "]");
		}
		catch (Exception e)
		{
			logger.error(e.getLocalizedMessage(), e);
		}
		String json = JsonTool.beanToJson(result);

		return json;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/deleteProduct")
	public @ResponseBody
	String deleteProduct(Product cardProduct, ModelMap model)
	{

		Map<String, String> result = new HashMap<String, String>();
		try
		{
			productService.deleteProduct(cardProduct);
			result.put("result", "success");
		}
		catch (RpcException e)
		{
			result.put("result", "error");
			result.put("message", "操作失败[" + MessageResolver.getMessage(e.getCode()) + "]");
		}
		catch (Exception e)
		{
			logger.error(e.getLocalizedMessage(), e);
		}
		String json = JsonTool.beanToJson(result);

		return json;
	}

	@RequestMapping(value = "/editProduct")
	public String editProduct(Product product, ModelMap model)
	{
		try
		{
			product = productService.queryProduct(product.getId());
			List<OptionVO> typeList = dataSourceService.findOpenOptions(CommonConstant.DataSourceName.PRODUCT_TYPE);
			List<OptionVO> statusList = dataSourceService.findOpenOptions(CommonConstant.DataSourceName.COMMON_STATUS);
			List<OptionVO> merchantList = dataSourceService.findOpenOptions(CommonConstant.DataSourceName.MERCHANT);
			model.addAttribute("product", product);
			model.addAttribute("typeList", typeList);
			model.addAttribute("statusList", statusList);
			model.addAttribute("merchantList", merchantList);

			return "mall/management/product/editProduct";
		}
		catch (Exception e)
		{
			logger.error(e.getLocalizedMessage(), e);
			return "error/500";
		}
	}

	@RequestMapping(value = "/createProduct")
	public String createProduct(Model model)
	{
		try
		{
			List<OptionVO> typeList = dataSourceService.findOpenOptions(CommonConstant.DataSourceName.PRODUCT_TYPE);
			List<OptionVO> merchantList = dataSourceService.findOpenOptions(CommonConstant.DataSourceName.MERCHANT);
			List<OptionVO> statusList = dataSourceService.findOpenOptions(CommonConstant.DataSourceName.COMMON_STATUS);

			model.addAttribute("typeList", typeList);
			model.addAttribute("statusList", statusList);
			model.addAttribute("merchantList", merchantList);
			return "mall/management/product/editProduct";
		}
		catch (Exception e)
		{
			logger.error(e.getLocalizedMessage(), e);
			return "error/500";
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = "/saveProduct")
	public @ResponseBody
	String saveProduct(Product product, ModelMap model)
	{

		Map<String, String> result = new HashMap<String, String>();
		try
		{
			String regex_a = "[`~!@#$%^&*(){}'?/-=\\.<>￥%]";
			if (StringUtil.isBlank(String.valueOf(product.getMerchantId())))
			{
				result.put("result", "error");
				result.put("message", "操作失败[" + "请选择商户" + "]");

			}
			else if (StringUtil.isBlank(product.getName()))
			{
				result.put("result", "error");
				result.put("message", "操作失败[" + "请填写名称" + "]");

			}
			else if (Pattern.matches(regex_a, product.getName()))
			{
				result.put("result", "error");
				result.put("message", "操作失败[" + "输入值不能为空和包含其他非法字符" + "]");

			}
			else if (StringUtil.isBlank(String.valueOf(product.getType())))
			{
				result.put("result", "error");
				result.put("message", "操作失败[" + "请选择类型" + "]");

			}
			else if (StringUtil.isBlank(String.valueOf(product.getStatus())))
			{
				result.put("result", "error");
				result.put("message", "操作失败[" + "请选择状态" + "]");

			}
			else
			{
				productService.saveProduct(product);
				result.put("result", "success");
			}

		}
		catch (RpcException e)
		{
			result.put("result", "error");
			result.put("message", "操作失败[" + MessageResolver.getMessage(e.getCode()) + "]");
		}
		catch (Exception e)
		{
			logger.error(e.getLocalizedMessage(), e);
		}
		String json = JsonTool.beanToJson(result);

		return json;
	}
}
