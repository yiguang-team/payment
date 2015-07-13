<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"
	contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">

<title>翼光联运支付管理系统-商品订单列表</title>
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
<meta name="viewport"
	content="width=device-width, initial-scale=1.0, maximum-scale=1.0" />
<META http-equiv="content-type" content="text/html; charset=utf-8">
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">

<c:set var="ctx" value="${pageContext.request.contextPath}" />
<script type="text/javascript"
	src="${ctx}/template/jquery-easyui-1.4.1/jquery.min.js"></script>
<script type="text/javascript"
	src="${ctx}/template/jquery-easyui-1.4.1/jquery.easyui.min.js"></script>
<script type="text/javascript"
	src="${ctx}/template/jquery-easyui-1.4.1/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript"
	src="${ctx}/template/common/js/DatePicker/WdatePicker.js"></script>

<link
	href="${ctx}/template/jquery-easyui-1.4.1/themes/default/easyui.css"
	rel="stylesheet" type="text/css" />
<link href="${ctx}/template/jquery-easyui-1.4.1/themes/icon.css"
	rel="stylesheet" type="text/css" />

<!-- bootstrap & fontawesome -->
<link rel="stylesheet"
	href="${ctx}/template/ace/assets/css/bootstrap.css" />
<link rel="stylesheet"
	href="${ctx}/template/ace/assets/css/font-awesome.css" />

<!-- page specific plugin styles -->

<!-- text fonts -->
<link rel="stylesheet"
	href="${ctx}/template/ace/assets/css/ace-fonts.css" />

<!-- ace styles -->
<link rel="stylesheet" href="${ctx}/template/ace/assets/css/ace.css"
	class="ace-main-stylesheet" id="main-ace-style" />
<link rel="stylesheet" href="${ctx}/template/ace/assets/css/ace-ie7.css" />

<!--[if lte IE 9]>
			<link rel="stylesheet" href="${ctx}/template/ace/assets/css/ace-part2.css" class="ace-main-stylesheet" />
		<![endif]-->

<!--[if lte IE 9]>
		  <link rel="stylesheet" href="${ctx}/template/ace/assets/css/ace-ie.css" />
		<![endif]-->

<!-- inline styles related to this page -->

<!-- ace settings handler -->
<script src="${ctx}/template/ace/assets/js/ace-extra.js"></script>

<!-- HTML5shiv and Respond.js for IE8 to support HTML5 elements and media queries -->

<!--[if lte IE 8]>
		<script src="${ctx}/template/ace/assets/js/html5shiv.js"></script>
		<script src="${ctx}/template/ace/assets/js/respond.js"></script>
		<![endif]-->
</head>

<body class="no-skin">
	<form id="listForm" name="listForm" method="post"
		action="${ctx}/mall/order/merchantOrderList">

		<!-- /section:basics/navbar.layout -->
		<div class="main-container" id="main-container">
			<script type="text/javascript">
				try{ace.settings.check('main-container' , 'fixed')}catch(e){}
			</script>

			<!-- /section:basics/sidebar -->
			<div class="main-content">
				<div class="main-content-inner">
					<!-- #section:basics/content.breadcrumbs -->
					<div class="breadcrumbs" id="breadcrumbs">
						<script type="text/javascript">
							try{ace.settings.check('breadcrumbs' , 'fixed')}catch(e){}
						</script>
						<ul class="breadcrumb">
							<li><i class="ace-icon fa fa-home home-icon"></i> 订单管理 <span
								class="noshow jt">></span></li>
							<li class="active">商品订单列表</li>
						</ul>
						<!-- /.breadcrumb -->
					</div>

					<!-- /section:basics/content.breadcrumbs -->
					<div class="page-content">

						<!--搜索-->
						<div class="row">
							<div class="col-xs-12">
								<div class="table-header">搜索</div>
								<div class="table-container">
									<span class="labeltxt fl">开始时间：</span> <input id="beginDate"
										name="beginDate" type="text"
										onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:'%y-{%M-3}-%d',maxDate:'#F{$dp.$D(\'beginDate\')|| \'%y-%M-%d\'}'});"
										value="${beginDate}" class="fl form-text" /> <span
										class="labeltxt fl">结束时间：</span> <input id="endDate"
										name="endDate" type="text"
										onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:'#F{$dp.$D(\'endDate\') || \'%y-{%M-3}-%d\'}',maxDate:'%y-%M-%d'});"
										value="${endDate}" class="fl form-text" /> <span
										class="labeltxt fl">账&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;号：</span>
									<input class="fl form-text" type="text" id="username"
										name="username" value="${username}"> 
										<span
										class="labeltxt fl">手机号码：</span> <input class="fl form-text"
										type="text" id="mobile" name="mobile" value="${mobile}">
									<span class="labeltxt fl">运&nbsp;&nbsp;营&nbsp;&nbsp;商：</span> <select
										name="carrierId" id="carrierId" class="form-control fl"
										style="width: 150px; float: left;">
										<c:choose>
											<c:when test="${fn:length(carrierList) > 0}">
												<c:forEach items="${carrierList}" var="option">
													<option value="${option.value}"
														<c:if test="${option.value == carrierId}">selected="true"</c:if>>${option.text}</option>
												</c:forEach>
											</c:when>
											<c:otherwise>
												<option value=""></option>
											</c:otherwise>
										</c:choose>
									</select> 
									 <br /> <br /> 
									 <span class="labeltxt fl">交易单号：</span> <input
										class="fl form-text" type="text" id="orderId" name="orderId"
										value="${orderId}"> 
									<span class="labeltxt fl">商户单号：</span>
									<input class="fl form-text" type="text" id="merchantOrderId"
										name="merchantOrderId" value="${merchantOrderId}">
									<span class="labeltxt fl">支付状态：</span> <select
										name="payStatus" id="payStatus" class="form-control fl"
										style="width: 150px; float: left;">
										<c:choose>
											<c:when test="${fn:length(payStatusList) > 0}">
												<c:forEach items="${payStatusList}" var="option">
													<option value="${option.value}"
														<c:if test="${option.value == payStatus}">selected="true"</c:if>>${option.text}</option>
												</c:forEach>
											</c:when>
											<c:otherwise>
												<option value=""></option>
											</c:otherwise>
										</c:choose>
									</select> 
									<span class="labeltxt fl">发货状态：</span> <select
										name="deliveryStatus" id="deliveryStatus"
										class="form-control fl" style="width: 150px; float: left;">
										<c:choose>
											<c:when test="${fn:length(deliveryStatusList) > 0}">
												<c:forEach items="${deliveryStatusList}" var="option">
													<option value="${option.value}"
														<c:if test="${option.value == deliveryStatus}">selected="true"</c:if>>${option.text}</option>
												</c:forEach>
											</c:when>
											<c:otherwise>
												<option value=""></option>
											</c:otherwise>
										</c:choose>
									</select> 
									<span class="labeltxt fl">通知状态：</span> <select
										name="notifyStatus" id="notifyStatus" class="form-control fl"
										style="width: 150px; float: left;">
										<c:choose>
											<c:when test="${fn:length(notifyStatusList) > 0}">
												<c:forEach items="${notifyStatusList}" var="option">
													<option value="${option.value}"
														<c:if test="${option.value == notifyStatus}">selected="true"</c:if>>${option.text}</option>
												</c:forEach>
											</c:when>
											<c:otherwise>
												<option value=""></option>
											</c:otherwise>
										</c:choose>
									</select>
										<br /> <br /> 
									<span class="labeltxt fl">渠道类型：</span> <select
										name="channelType" id="channelType" class="form-control fl"
										style="width: 150px; float: left;">
										<c:choose>
											<c:when test="${fn:length(channelTypeList) > 0}">
												<c:forEach items="${channelTypeList}" var="option">
													<option value="${option.value}"
														<c:if test="${option.value == channelType}">selected="true"</c:if>>${option.text}</option>
												</c:forEach>
											</c:when>
											<c:otherwise>
												<option value=""></option>
											</c:otherwise>
										</c:choose>
									</select> 
									<span
										class="labeltxt fl">渠&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;道：</span>
									<select name="channelId" id="channelId" class="form-control fl"
										style="width: 150px; float: left;">
										<c:choose>
											<c:when test="${fn:length(channelList) > 0}">
												<c:forEach items="${channelList}" var="option">
													<option value="${option.value}"
														<c:if test="${option.value == channelId}">selected="true"</c:if>>${option.text}</option>
												</c:forEach>
											</c:when>
											<c:otherwise>
												<option value=""></option>
											</c:otherwise>
										</c:choose>
									</select>	
									<span class="labeltxt fl">省&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;份：</span>
									<select name="provinceId" id="provinceId"
										onchange="changeCity()" class="form-control fl"
										style="width: 150px; float: left;">
										<c:choose>
											<c:when test="${fn:length(provinceList) > 0}">
												<c:forEach items="${provinceList}" var="option">
													<option value="${option.value}"
														<c:if test="${option.value == provinceId}">selected="true"</c:if>>${option.text}</option>
												</c:forEach>
											</c:when>
											<c:otherwise>
												<option value=""></option>
											</c:otherwise>
										</c:choose>
									</select> <span class="labeltxt fl">城&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;市：</span>
									<select name="cityId" id="cityId" class="form-control fl"
										style="width: 150px; float: left;">
										<c:choose>
											<c:when test="${fn:length(cityList) > 0}">
												<c:forEach items="${cityList}" var="option">
													<option value="${option.value}"
														<c:if test="${option.value == cityId}">selected="true"</c:if>>${option.text}</option>
												</c:forEach>
											</c:when>
											<c:otherwise>
												<option value=""></option>
											</c:otherwise>
										</c:choose>
									</select>  
									<br /> <br />
									<span class="labeltxt fl">商&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;户：</span>
									<select name="merchantId" id="merchantId"
										onchange="changeProduct()" class="form-control fl"
										style="width: 150px; float: left;">
										<c:choose>
											<c:when test="${fn:length(merchantList) > 0}">
												<c:forEach items="${merchantList}" var="option">
													<option value="${option.value}"
														<c:if test="${option.value == merchantId}">selected="true"</c:if>>${option.text}</option>
												</c:forEach>
											</c:when>
											<c:otherwise>
												<option value=""></option>
											</c:otherwise>
										</c:choose>
									</select> 
									<span class="labeltxt fl">产&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;品：</span>
									<select name="productId" id="productId" class="form-control fl"
										style="width: 150px; float: left;" onchange="changePoint()">
										<c:choose>
											<c:when test="${fn:length(productList) > 0}">
												<c:forEach items="${productList}" var="option">
													<option value="${option.value}"
														<c:if test="${option.value == productId}">selected="true"</c:if>>${option.text}</option>
												</c:forEach>
											</c:when>
											<c:otherwise>
												<option value=""></option>
											</c:otherwise>
										</c:choose>
									</select> 
									<span class="labeltxt fl">计&nbsp;&nbsp;费&nbsp;&nbsp;点：</span>
									<select name="chargingPointId" id="chargingPointId"
										class="form-control fl" style="width: 150px; float: left;">
										<c:choose>
											<c:when test="${fn:length(pointList) > 0}">
												<c:forEach items="${pointList}" var="option">
													<option value="${option.value}"
														<c:if test="${option.value == chargingPointId}">selected="true"</c:if>>${option.text}</option>
												</c:forEach>
											</c:when>
											<c:otherwise>
												<option value=""></option>
											</c:otherwise>
										</c:choose>
									</select> 
									<span class="labeltxt fl">计费类型：</span> <select
										name="chargingType" id="chargingType" class="form-control fl"
										style="width: 150px; float: left;">
										<c:choose>
											<c:when test="${fn:length(chargingTypeList) > 0}">
												<c:forEach items="${chargingTypeList}" var="option">
													<option value="${option.value}"
														<c:if test="${option.value == chargingType}">selected="true"</c:if>>${option.text}</option>
												</c:forEach>
											</c:when>
											<c:otherwise>
												<option value=""></option>
											</c:otherwise>
										</c:choose>
									</select> <span class="labeltxt fl">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>
									<div class="btns">
										<input type="submit" class="btn btn-primary btn-sm no-border"
											value="查询" />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
										<input
											class="btn btn-sm btn-primary no-border" type="button"
												value="根据条件导出订单" onClick="exportExcel()" />
									</div>
								</div>
							</div>
						</div>

						<!--搜索 end-->

						<div class="row">
							<div class="col-xs-12">
								<!-- PAGE CONTENT BEGINS -->
								<div class="row">
									<div class="col-xs-12">

										<!--分页-->
										<div class="row">
											<div class="col-xs-12">
												<div class="pl bggray">

													<div class="dataTables_length" style="float: right;">
														<label>每页显示： <select name="pageSize" id="pageSize"
															onChange="toPage(1)">
																<option value="5"
																	<c:if test="${pageSize==5}">selected="selected"</c:if>>5</option>
																<option value="10"
																	<c:if test="${pageSize==10}">selected="selected"</c:if>>10</option>
																<option value="20"
																	<c:if test="${pageSize==20}">selected="selected"</c:if>>20</option>
																<option value="30"
																	<c:if test="${pageSize==30}">selected="selected"</c:if>>30</option>
																<option value="40"
																	<c:if test="${pageSize==40}">selected="selected"</c:if>>40</option>
																<option value="50"
																	<c:if test="${pageSize==50}">selected="selected"</c:if>>50</option>
														</select> 条
														</label>
													</div>

												</div>
											</div>
										</div>
										<!--分页 end-->
										<table id="sample-table-1"
											class="table table-striped table-bordered table-hover nospace">
											<thead>
												<tr>
													<th>交易单号<br />商户单号
													</th>
													<th>下单时间</th>
													<th>手机号码<br />运营商
													</th>
													<th>渠道<br />渠道类型
													</th>
													<th>省份<br />城市
													</th>
													<th>商户<br />产品
													</th>
													<th>计费点<br />计费类型/面值/充值账号
													</th>
													<th>支付状态/发货状态/通知状态
													</th>
													<th>操作</th>
												</tr>
											</thead>
											<tbody>

												<c:choose>
													<c:when test="${fn:length(mlist) > 0}">
														<c:forEach items="${mlist}" var="merchantOrder"
															varStatus="status">
															<tr>
																<td>${merchantOrder.orderId}<br />${merchantOrder.merchantOrderId}</td>
																<td>${merchantOrder.requestTime}</td>
																<td>${merchantOrder.mobile}</br>${merchantOrder.carrierLabel}</td>
																<td>${merchantOrder.channelLabel}<br />${merchantOrder.channelTypeLabel}</td>
																<td>${merchantOrder.provinceLabel}<br />${merchantOrder.cityLabel}</td>
																<td>${merchantOrder.merchantLabel}<br />${merchantOrder.productLabel}</td>
																<td>${merchantOrder.pointLabel}<br />${merchantOrder.chargingTypeLabel}/${merchantOrder.faceAmount}/${merchantOrder.username}</td>
																<td>${merchantOrder.payStatusLabel}/${merchantOrder.deliveryStatusLabel}/${merchantOrder.notifyStatusLabel}</td>
																<td>
																	<div class="edit" style="float: left;">
																		<a href="javascript:;"
																			onclick="viewMerchantOrderInfo('${merchantOrder.orderId}')">[详情]</a>
																		<c:if
																			test="${merchantOrder.payStatus == 0 && (merchantOrder.deliveryStatus == 1 || merchantOrder.deliveryStatus == 2) && merchantOrder.merchantId == 1000}">
																			<a href="javascript:;"
																				onclick="delivery('${merchantOrder.orderId}')">[补发货]</a>
																		</c:if>
																	</div>
																</td>
															</tr>
														</c:forEach>
													</c:when>
													<c:otherwise>
														<tr>
															<td colspan="10" class="center">没数据</td>
														</tr>
													</c:otherwise>
												</c:choose>
											</tbody>
										</table>
										<!--分页-->
										<div class="row">
											<div class="col-xs-12">
												<div class="pl bggray">
													<div class="col-xs-6">共有 ${counttotal}
														条记录，当前第${page>pagetotal? pagetotal :page} / ${pagetotal} 页
													</div>
													<div class="col-xs-6">
														<div id="sample-table-2_paginate"
															class="dataTables_paginate paging_simple_numbers">
															<ul class="pagination">
																<li
																	class="paginate_button previous <c:if test="${page <= 1}">disabled"</c:if>"><a
																	href="javascript:;" onclick="toPage(1)">首页</a></li>
																<li
																	class="paginate_button previous <c:if test="${page <= 1}">disabled"</c:if>"><a
																	href="javascript:;" onclick="toPage(${page-1})">上一页</a></li>
																<li
																	class="paginate_button next <c:if test="${page >= pagetotal}">disabled"</c:if>"><a
																	href="javascript:;" onclick="toPage(${page+1})">下一页</a></li>
																<li
																	class="paginate_button next <c:if test="${page >= pagetotal}">disabled"</c:if>"><a
																	href="javascript:;" onclick="toPage(${pagetotal})">末页</a></li>

																<li class="paginate_button2">转到第<input type="text"
																	class="ui_input_txt" id="toPageNum" name="toPageNum">页
																	<input type="hidden" id="page" name="page" />
																	<button id="toPageBtn" name="toPageBtn" type="button"
																		class="btn btn-white btn-primary btn-tz"
																		onclick="toPage(0)">跳转</button>
																</li>
															</ul>
														</div>
													</div>
												</div>
											</div>
										</div>
										<!--分页 end-->
									</div>
									<!-- /.span -->
								</div>
								<!-- /.row -->

								<!-- PAGE CONTENT ENDS -->

							</div>
							<!-- /.col -->
						</div>
						<!-- /.row -->
					</div>
					<!-- /.page-content -->
				</div>
			</div>
			<!-- /.main-content -->

			<a href="#" id="btn-scroll-up"
				class="btn-scroll-up btn btn-sm btn-inverse"> <i
				class="ace-icon fa fa-angle-double-up icon-only bigger-110"></i>
			</a>
		</div>
		<!-- /.main-container -->

		<!-- basic scripts -->

		<!--[if !IE]> -->
		<script type="text/javascript">
			window.jQuery || document.write("<script src='${ctx}/template/ace/assets/js/jquery.js'>"+"<"+"/script>");
		</script>

		<!-- <![endif]-->

		<!--[if IE]>
<script type="text/javascript">
 window.jQuery || document.write("<script src='${ctx}/template/ace/assets/js/jquery1x.js'>"+"<"+"/script>");
</script>
<![endif]-->
		<script type="text/javascript">
			if('ontouchstart' in document.documentElement) document.write("<script src='${ctx}/template/ace/assets/js/jquery.mobile.custom.js'>"+"<"+"/script>");
		</script>
		<script src="${ctx}/template/ace/assets/js/bootstrap.js"></script>
		<!--[if lte IE 8]>
		  <script src="${ctx}/template/assets/js/excanvas.js"></script>
		<![endif]-->
		<script src="${ctx}/template/ace/assets/js/jquery-ui.custom.js"></script>
		<script src="${ctx}/template/ace/assets/js/jquery.ui.touch-punch.js"></script>
		<script src="${ctx}/template/ace/assets/js/bootbox.js"></script>
		<script src="${ctx}/template/ace/assets/js/jquery.easypiechart.js"></script>
		<script src="${ctx}/template/ace/assets/js/jquery.gritter.js"></script>
		<script src="${ctx}/template/ace/assets/js/spin.js"></script>

		<!-- page specific plugin scripts -->
		<script src="${ctx}/template/ace/assets/js/jquery.dataTables.js"></script>
		<script
			src="${ctx}/template/ace/assets/js/jquery.dataTables.bootstrap.js"></script>

		<!-- ace scripts -->
		<script src="${ctx}/template/ace/assets/js/ace/elements.scroller.js"></script>
		<script
			src="${ctx}/template/ace/assets/js/ace/elements.colorpicker.js"></script>
		<script src="${ctx}/template/ace/assets/js/ace/elements.fileinput.js"></script>
		<script src="${ctx}/template/ace/assets/js/ace/elements.typeahead.js"></script>
		<script src="${ctx}/template/ace/assets/js/ace/elements.wysiwyg.js"></script>
		<script src="${ctx}/template/ace/assets/js/ace/elements.spinner.js"></script>
		<script src="${ctx}/template/ace/assets/js/ace/elements.treeview.js"></script>
		<script src="${ctx}/template/ace/assets/js/ace/elements.wizard.js"></script>
		<script src="${ctx}/template/ace/assets/js/ace/elements.aside.js"></script>
		<script src="${ctx}/template/ace/assets/js/ace/ace.js"></script>
		<script src="${ctx}/template/ace/assets/js/ace/ace.ajax-content.js"></script>
		<script src="${ctx}/template/ace/assets/js/ace/ace.touch-drag.js"></script>
		<script src="${ctx}/template/ace/assets/js/ace/ace.sidebar.js"></script>
		<script
			src="${ctx}/template/ace/assets/js/ace/ace.sidebar-scroll-1.js"></script>
		<script src="${ctx}/template/ace/assets/js/ace/ace.submenu-hover.js"></script>
		<script src="${ctx}/template/ace/assets/js/ace/ace.widget-box.js"></script>
		<script src="${ctx}/template/ace/assets/js/ace/ace.settings.js"></script>
		<script src="${ctx}/template/ace/assets/js/ace/ace.settings-rtl.js"></script>
		<script src="${ctx}/template/ace/assets/js/ace/ace.settings-skin.js"></script>
		<script
			src="${ctx}/template/ace/assets/js/ace/ace.widget-on-reload.js"></script>
		<script
			src="${ctx}/template/ace/assets/js/ace/ace.searchbox-autocomplete.js"></script>

		<!-- the following scripts are used in demo only for onpage help and you don't need them -->
		<link rel="stylesheet"
			href="${ctx}/template/ace/assets/css/ace.onpage-help.css" />
		<link rel="stylesheet"
			href="${ctx}/template/ace/docs/assets/js/themes/sunburst.css" />
		<script type="text/javascript"> ace.vars['base'] = '..'; </script>
		<script
			src="${ctx}/template/ace/assets/js/ace/elements.onpage-help.js"></script>
		<script src="${ctx}/template/ace/assets/js/ace/ace.onpage-help.js"></script>
		<script src="${ctx}/template/ace/docs/assets/js/rainbow.js"></script>
		<script src="${ctx}/template/ace/docs/assets/js/language/generic.js"></script>
		<script src="${ctx}/template/ace/docs/assets/js/language/html.js"></script>
		<script src="${ctx}/template/ace/docs/assets/js/language/css.js"></script>
		<script
			src="${ctx}/template/ace/docs/assets/js/language/javascript.js"></script>
	</form>
</body>
<script type="text/javascript">

function toPage(page)
{
	if (page == 0)
	{
		page = $("#toPageNum").val();
	}
	$("#page").val(page);
	document.listForm.submit();
}
<!--onchange="changeCity()"-->
function changeCity(){
	var provinceId = $("#provinceId").val();
	$.ajax({
		data:{
			parentId:provinceId,
			dataSourceCode:'CITY'
		 },
	url : "${ctx}/business/dataSource/changeSonString",
	type:'post',
	cache:false,
	async:true,
	dataType:'html',
	success : function(data) {  
		$('#cityId').show().html(data);
	}
	});
}
function changeProduct(){
	var merchantId = $("#merchantId").val();
	$.ajax({
		data:{
			parentId:merchantId,
			dataSourceCode:'PRODUCT'
		 },
	url : "${ctx}/business/dataSource/changeSonLong",
	type:'post',
	cache:false,
	async:true,
	dataType:'html',
	success : function(data) {  
		$('#productId').show().html(data);
	}
	});
}
function changePoint(){
	var productId = $("#productId").val();
	$.ajax({
		data:{
			parentId:productId,
			dataSourceCode:'POINT'
		 },
	url : "${ctx}/business/dataSource/changeSonLong",
	type:'post',
	cache:false,
	async:true,
	dataType:'html',
	success : function(data) {  
		$('#chargingPointId').show().html(data);
	}
	});
}
function viewMerchantOrderInfo(orderId){
	parent.addTab('商户订单详情-'+orderId,'${ctx}/mall/order/merchantOrderInfo?orderId='+orderId);
}
function delivery(orderId){

	bootbox.confirm("确定要补发货吗?", 
		function(result) {
			if(result) {
				$.ajax({  
					data:{
							orderId:orderId
						 },
					url : "${ctx}/mall/order/delivery",
					type:'post',
					cache:false,
					async:true,
					dataType:'json',
					success : function(data) {  
						if (data.result=='success')
						{
							toPage($("#page").val());
						}
						else
						{
							bootbox.dialog({
								message: "<span class='bigger-110'>"+data.message+"</span>",
								buttons: 			
								{
									"confirm" :
									{
										"label" : "确定",
										"className" : "btn-sm",
										"callback": function() {

										}
									}
								}
							});
						}
					}
				});
			}
		});
}

function exportExcel() {
	var mobile =  $('#mobile').val();
	var orderId =  $('#orderId').val();
	var username =  $('#username').val();
	var provinceId =  $('#provinceId').val();
	var beginDate =  $('#beginDate').val();
	var endDate =  $('#endDate').val();
	var merchantOrderId =  $('#merchantOrderId').val();
	var channelId =  $('#channelId').val();
	var carrierId =  $('#carrierId').val();
	var merchantId =  $('#merchantId').val();
	var cityId =  $('#cityId').val();
	var productId =  $('#productId').val();
	var payStatus =  $('#payStatus').val();
	var deliveryStatus =  $('#deliveryStatus').val();
	var chargingPointId =  $('#chargingPointId').val();
	var chargingType =  $('#chargingType').val();
	var channelType =  $('#channelType').val();
	var notifyStatus =  $('#notifyStatus').val();
	
	location.href= '${ctx}/mall/order/excel?mobile='+mobile+'&beginDate=' + beginDate+'&endDate=' + endDate+'&orderId=' + orderId+'&username=' + username+'&provinceId=' + provinceId+'&merchantOrderId=' + merchantOrderId+'&channelId=' + channelId+'&carrierId=' + carrierId+'&merchantId=' + merchantId+'&cityId=' + cityId+'&productId=' + productId+'&payStatus=' + payStatus+'&deliveryStatus=' + deliveryStatus+'&chargingPointId=' + chargingPointId+'&chargingType=' + chargingType+'&channelType=' + channelType+'&notifyStatus=' + notifyStatus;
}
</script>
</html>
