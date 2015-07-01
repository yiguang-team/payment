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

<title>翼光联运支付管理系统-订单详情</title>
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
							<li class="active">商品订单详情</li>
						</ul>
						<!-- /.breadcrumb -->
					</div>

					<!-- /section:basics/content.breadcrumbs -->
					<div class="page-content">
						<!--搜索 end-->

						<div class="row">
							<div class="col-xs-12">
								<!-- PAGE CONTENT BEGINS -->
								<div class="row">
									<div class="col-xs-12">
										<table id="sample-table-1"
											class="table table-striped table-bordered table-hover nospace">
											<tbody>
												<tr>
													<td>交易单号</td>
													<td>${order.orderId}</td>
													<td>商户单号</td>
													<td>${order.merchantOrderId}</td>
													<td>请求IP</td>
													<td>${order.requestIp}</td>
												</tr>
												<tr>
													<td>请求时间</td>
													<td>${order.requestTime}</td>
													<td>完成时间</td>
													<td>${order.completeTime}</td>
													<td>充值账号</td>
													<td>${order.username}</td>
												</tr>
												<tr>
													<td>手机号码</td>
													<td>${order.mobile}</td>
													<td>运营商</td>
													<td>${order.carrierLabel}</td>
													<td>渠道</td>
													<td>${order.channelLabel}</td>
												</tr>
												<tr>
													<td>省份</td>
													<td>${order.provinceLabel}</td>
													<td>城市</td>
													<td>${order.cityLabel}</td>
													<td>商户</td>
													<td>${order.merchantLabel}</td>
												</tr>
												<tr>
													<td>产品</td>
													<td>${order.productLabel}</td>
													<td>计费点</td>
													<td>${order.pointLabel}</td>
													<td>计费类型</td>
													<td>${order.chargingTypeLabel}</td>
												</tr>
												<tr>
													<td>面值</td>
													<td>${order.faceAmount}</td>
													<td>支付价格</td>
													<td>${order.payAmount}</td>
													<td>发货价格</td>
													<td>${order.deliveryAmount}</td>
												</tr>
												<tr>
													<td>支付状态</td>
													<td>${order.payStatusLabel}</td>
													<td>支付结果编码</td>
													<td>${order.returnCode}</td>
													<td>支付结果消息</td>
													<td>${order.returnMessage}</td>
												</tr>
												<tr>
													<td>发货状态</td>
													<td>${order.deliveryStatusLabel}</td>
													<td>发货编号</td>
													<td>${order.deliveryNo}</td>
													<td>发货开始时间</td>
													<td>${order.deliveryRequestTime}</td>
												</tr>
												<tr>
													<td>发货结束时间</td>
													<td>${order.deliveryCompleteTime}</td>
													<td>发货结果编码</td>
													<td>${order.deliveryReturnCode}</td>
													<td>发货结果消息</td>
													<td>${order.deliveryReturnMessage}</td>
												</tr>
												<tr>
													<td>通知状态</td>
													<td>${order.notifyStatusLabel}</td>
													<td>商户产品url</td>
													<td>${order.showUrl}</td>
													<td>商户通知url</td>
													<td>${order.notifyUrl}</td>
												</tr>
												<tr>
													<td>商户产品信息</td>
													<td>${order.subject}</td>
													<td>商户产品描述</td>
													<td>${order.description}</td>
													<td>渠道交易单号</td>
													<td>${order.channelTradeNo}</td>
												</tr>
												<tr>
													<td>短信验证码</td>
													<td>${order.smscode}</td>
													<td>下单时间戳</td>
													<td>${order.orderTimestamp}</td>
													<td>支付时间戳</td>
													<td>${order.payTimestamp}</td>
												</tr>
												<tr>
												<c:if test="${order.chargingType == 2}">
													<c:choose>
														<c:when test="${fn:length(mlist) > 0}">
															<c:forEach items="${mlist}" var="option">
															<td>商品卡号</td>
															<td>${option.cardId}</td>
															<td>商品密码</td>
															<td>${option.cardPwd}</td>
															</c:forEach>
														</c:when>
													</c:choose>
												</c:if>
												</tr>
											</tbody>
										</table>
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
</script>
</html>
