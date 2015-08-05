package com.yiguang.payment.depot.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.dubbo.rpc.RpcException;
import com.yiguang.payment.business.product.vo.PointVO;
import com.yiguang.payment.common.CommonConstant;
import com.yiguang.payment.common.JsonTool;
import com.yiguang.payment.common.datasource.service.DataSourceService;
import com.yiguang.payment.common.datasource.vo.OptionVO;
import com.yiguang.payment.common.message.MessageResolver;
import com.yiguang.payment.common.query.YcPage;
import com.yiguang.payment.common.utils.BigDecimalUtil;
import com.yiguang.payment.common.utils.StringUtil;
import com.yiguang.payment.depot.service.ProductBatchService;
import com.yiguang.payment.depot.service.ProductDepotService;
import com.yiguang.payment.depot.vo.ProductDepotVO;

/**
 * LoginController负责打开登录页面(GET请求)和登录出错页面(POST请求)， 真正登录的POST请求由Filter完成,
 * 
 * @author calvin
 */
@Controller
@RequestMapping(value = "/depot/depot")
public class DepotController
{

	private static Logger logger = LoggerFactory.getLogger(DepotController.class);

	@Autowired
	HttpSession session;

	@Autowired
	private ProductDepotService productDepotService;
	@Autowired
	private ProductBatchService productBatchService;
	@Autowired
	private DataSourceService dataSourceService;

	private static final String PAGE_SIZE = "10";

	@RequestMapping(value = "/depotList")
	public String cardProductList(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "pageSize", defaultValue = PAGE_SIZE) int pageSize,
			@RequestParam(value = "sortType", defaultValue = "auto") String sortType,
			@RequestParam(value = "merchantId", defaultValue = "-1") int merchantId,
			@RequestParam(value = "productId", defaultValue = "-1") int productId,
			@RequestParam(value = "provinceId", defaultValue = "") String provinceId,
			@RequestParam(value = "cityId", defaultValue = "") String cityId,
			@RequestParam(value = "faceAmount", defaultValue = "") String faceAmount,
			@RequestParam(value = "cardId", defaultValue = "") String cardId,
			@RequestParam(value = "batchId", defaultValue = "") String batchId,
			@RequestParam(value = "status", defaultValue = "-1") int status, Model model, ServletRequest request)
	{
		
		ProductDepotVO vo = new ProductDepotVO();
		PointVO pointvo = new PointVO();
		pointvo.setMerchantId(merchantId);
		pointvo.setProductId(productId);
		pointvo.setProvinceId(provinceId);
		pointvo.setCityId(cityId);
		pointvo.setChargingType(CommonConstant.CHARGING_TYPE.CARD);
		if(StringUtil.isNotBlank(faceAmount))
		{
			pointvo.setFaceAmount(BigDecimalUtil.multiply(
					new BigDecimal(faceAmount.trim()), new BigDecimal("100"), 0, BigDecimal.ROUND_HALF_UP));
		}
		vo.setCardId(cardId);
		vo.setStatus(status);
		vo.setBatchId(batchId);
		vo.setPointVO(pointvo);
		
		YcPage<ProductDepotVO> page_list = productDepotService.queryProductDepotList(vo, pageNumber,
				pageSize, "");

		List<OptionVO> statusList = dataSourceService.findOpenOptions(CommonConstant.DataSourceName.CARD_STATUS);
		List<OptionVO> chargingTypeList = dataSourceService
				.findOpenOptions(CommonConstant.DataSourceName.CHARGING_TYPE);
		List<OptionVO> merchantList = dataSourceService.findOpenOptions(CommonConstant.DataSourceName.MERCHANT);
		List<OptionVO> productList = dataSourceService.findOpenOptions(CommonConstant.DataSourceName.PRODUCT);
		List<OptionVO> provinceList = dataSourceService.findOpenOptions(CommonConstant.DataSourceName.PROVINCE);
		List<OptionVO> cityList = dataSourceService.findOpenOptions(CommonConstant.DataSourceName.CITY);

		model.addAttribute("cardId", cardId);
		model.addAttribute("status", status);
		model.addAttribute("batchId", batchId);
		model.addAttribute("statusList", statusList);
		model.addAttribute("merchantId", merchantId);
		model.addAttribute("merchantList", merchantList);
		model.addAttribute("productId", productId);
		model.addAttribute("productList", productList);
		model.addAttribute("provinceId", provinceId);
		model.addAttribute("provinceList", provinceList);
		model.addAttribute("cityId", cityId);
		model.addAttribute("cityList", cityList);
		model.addAttribute("faceAmount", faceAmount);
		model.addAttribute("chargingTypeList", chargingTypeList);
		model.addAttribute("mlist", page_list.getList());
		model.addAttribute("page", pageNumber);
		model.addAttribute("pageSize", pageSize);
		model.addAttribute("counttotal", page_list.getCountTotal() + "");
		model.addAttribute("pagetotal", page_list.getPageTotal() + "");

		return "depot/depot/depotList";
	}

	@RequestMapping(value = "/toImportProduct")
	public String toImportProduct(Model model)
	{
		List<OptionVO> merchantList = dataSourceService.findOpenOptions(CommonConstant.DataSourceName.MERCHANT);
		List<OptionVO> productList = dataSourceService.findOpenOptions(CommonConstant.DataSourceName.PRODUCT);
		model.addAttribute("merchantList", merchantList);
		model.addAttribute("productList", productList);

		return "depot/depot/importProduct";
	}

	@RequestMapping(value = "/toViewProduct")
	public String toViewProduct(@RequestParam(value = "file", required = false) MultipartFile file,
			@RequestParam(value = "merchantId", defaultValue = "-1") int merchantId,
			@RequestParam(value = "productId", defaultValue = "-1") int productId, Model model)
	{

		String batchId = productBatchService.generateBatchId();
		List<Map<String, String>> list = viewProductDepot(file, batchId);

		String merchantLabel = "";
		if (StringUtil.isNotBlank(String.valueOf(merchantId)) && merchantId != -1)
		{
			merchantLabel = dataSourceService.findOptionVOById(CommonConstant.DataSourceName.MERCHANT,
					String.valueOf(merchantId)).getText();
		}
		String productLabel = "";
		if (StringUtil.isNotBlank(String.valueOf(productId)) && productId != -1)
		{
			productLabel = dataSourceService.findOptionVOById(CommonConstant.DataSourceName.PRODUCT,
					String.valueOf(productId)).getText();
		}

		model.addAttribute("mlist", JsonTool.beanToJson(list));
		model.addAttribute("columnNum", list.get(0).size());
		model.addAttribute("batchId", batchId);
		model.addAttribute("merchantId", merchantId);
		model.addAttribute("productId", productId);
		model.addAttribute("merchantLabel", merchantLabel);
		model.addAttribute("productLabel", productLabel);

		return "depot/depot/viewProduct";
	}

	@RequestMapping(method = RequestMethod.POST, value = "/importDepot")
	public @ResponseBody
	synchronized String importCard(@RequestParam(value = "batchId", defaultValue = "") String batchId,
			@RequestParam(value = "merchantId", defaultValue = "-1") long merchantId,
			@RequestParam(value = "productId", defaultValue = "-1") long productId,
			@RequestParam(value = "startLineNum", defaultValue = "-1") long startLineNum,
			@RequestParam(value = "config", defaultValue = "") String config,
			@RequestParam(value = "totalAmt", defaultValue = "") String totalAmt,
			@RequestParam(value = "totalNum", defaultValue = "0") long totalNum, ModelMap model)
	{
		Map<String, String> result = new HashMap<String, String>();
		try
		{

			if (StringUtil.isBlank(String.valueOf(merchantId)) || merchantId == -1)
			{
				result.put("result", "error");
				result.put("message", "操作失败[" + "导入参数错误，运营商不正确" + "]");
			}
			if (StringUtil.isBlank(String.valueOf(productId)) || productId == -1)
			{
				result.put("result", "error");
				result.put("message", "操作失败[" + "导入参数错误，产品不正确" + "]");
			}
			if (StringUtil.isBlank(String.valueOf(batchId)))
			{
				result.put("result", "error");
				result.put("message", "操作失败[" + "导入参数错误，批次ID不正确" + "]");
			}
			if (StringUtil.isBlank(String.valueOf(config)))
			{
				result.put("result", "error");
				result.put("message", "操作失败[" + "导入参数错误，配置信息不正确" + "]");
			}

			if (StringUtil.isBlank(String.valueOf(totalNum)) || totalNum == 0)
			{
				result.put("result", "error");
				result.put("message", "操作失败[" + "导入参数错误，卡密总数不正确" + "]");
			}
			if (StringUtil.isBlank(totalAmt) || new BigDecimal(totalAmt).floatValue() == 0)
			{
				result.put("result", "error");
				result.put("message", "操作失败[" + "导入参数错误，面值总额不正确" + "]");
			}
			String[][] excelData = getExcelFile(batchId);
			productDepotService.importProductDepot(batchId, config, merchantId, productId, totalAmt, totalNum,
					excelData);
			result.put("result", "success");
		}
		catch (RpcException e)
		{
			result.put("result", "error");
			result.put("message",
					"操作失败[" + (e.getMessage() != null ? e.getMessage() : MessageResolver.getMessage(e.getCode())) + "]");
		}

		String json = JsonTool.beanToJson(result);

		return json;
	}

	// 上传并预览需要处理excel文件
	public List<Map<String, String>> viewProductDepot(MultipartFile file, String batchId)
	{
		String[][] str = null;
		try
		{
			URL realPath = this.getClass().getResource("/");

			File pwd = new File(realPath.getPath() + File.separator + "upload" + File.separator);
			if (!pwd.exists())
			{
				pwd.mkdir();
			}

			String xls = file.getOriginalFilename();
			xls = xls.substring(xls.lastIndexOf("."));

			File uploadFile = new File(pwd.getCanonicalPath(), batchId + xls);

			if (!uploadFile.exists())
			{
				uploadFile.createNewFile();
			}

			FileUtils.copyInputStreamToFile(file.getInputStream(), uploadFile);

			str = parseExcel(uploadFile);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		return getViewRecord(batchId, str);

	}

	// 获得预览数据
	private List<Map<String, String>> getViewRecord(String batchId, String[][] str)
	{

		List<Map<String, String>> result = new ArrayList<Map<String, String>>();
		Map<String, String> map = null;

		int recordNum = str.length > 5 ? 5 : str.length;

		if (str != null)
		{
			for (int i = 0; i < recordNum; i++)
			{
				map = new HashMap<String, String>();
				for (int n = 0, length = str[i].length; n < length; n++)
				{
					map.put("COLUMN_" + n, str[i][n]);
				}
				result.add(map);
			}
		}
		return result;
	}

	// 解析文件
	private String[][] parseExcel(File file)
	{

		Workbook wb = null;

		Sheet sheet = null;

		Cell cell = null;

		Row row = null;

		InputStream stream = null;
		String[][] str = null;
		try
		{

			String path = file.getCanonicalPath();
			stream = new FileInputStream(path);
			if (path.endsWith(".xls"))
			{
				wb = (Workbook) new HSSFWorkbook(stream);
			}
			else if (path.endsWith(".xlsx"))
			{
				wb = (Workbook) new XSSFWorkbook(stream);
			}

			sheet = wb.getSheetAt(0);

			int count_row = sheet.getLastRowNum();
			int count_cell = sheet.getRow(0).getPhysicalNumberOfCells();

			str = new String[count_row + 1][count_cell];
			String cellValue = null;
			for (int i = 0; i <= count_row; i++)
			{

				for (int j = 0; j < count_cell; j++)
				{

					row = sheet.getRow(i);

					cell = row.getCell(j);

					int type = cell.getCellType(); // 得到单元格数据类型

					switch (type)
					{ // 判断数据类型
					case Cell.CELL_TYPE_BLANK:
						cellValue = "";
						break;
					case Cell.CELL_TYPE_BOOLEAN:
						cellValue = cell.getBooleanCellValue() + "";
						break;
					case Cell.CELL_TYPE_ERROR:
						cellValue = cell.getErrorCellValue() + "";
						break;
					case Cell.CELL_TYPE_FORMULA:
						cellValue = cell.getCellFormula();
						break;
					case Cell.CELL_TYPE_NUMERIC:
						if (DateUtil.isCellDateFormatted(cell))
						{
							cellValue = new DataFormatter().formatRawCellContents(cell.getNumericCellValue(), 0,
									"yyyyMMdd");// 格式化日期
						}
						else
						{
							cellValue = cell.getNumericCellValue() + "";
						}
						break;
					case Cell.CELL_TYPE_STRING:
						cellValue = cell.getStringCellValue();
						break;
					default:
						break;
					}

					str[i][j] = cellValue;
				}
			}
		}
		catch (IOException e)
		{

			logger.error("ProductDepotServiceImpl.parseExcel(): 解析文件失败");
			e.printStackTrace();
		}
		return str;

	}

	private String[][] getExcelFile(String batchId)
	{
		// 取得对应的导入文件
		File excelFile = null;
		URL realPath = this.getClass().getResource("/");

		File pwd = new File(realPath.getPath() + File.separator + "upload" + File.separator);
		if (!pwd.exists())
		{
			pwd.mkdir();
		}

		File[] fileList = pwd.listFiles();
		for (File file : fileList)
		{
			if (file.exists() && file.getName().startsWith(batchId))
			{
				excelFile = file;
				break;
			}
		}
		return parseExcel(excelFile);
	}

}
