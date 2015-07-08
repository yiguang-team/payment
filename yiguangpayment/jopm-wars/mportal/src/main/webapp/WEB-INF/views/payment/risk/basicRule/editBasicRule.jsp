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

<title>翼光联运支付管理系统-创建商户限量规则</title>
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
<script type="text/javascript" src="${ctx}/js/index/index.js"></script>
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
<link rel="stylesheet" href="${ctx}/template/ace/assets/css/select2.css" />

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
	<!-- /section:basics/navbar.layout -->
	<div class="main-container" id="main-container">

		<!-- /section:basics/sidebar -->
		<div class="main-content">
			<div class="main-content-inner">
				<div class="breadcrumbs" id="breadcrumbs">
					<script type="text/javascript">
											try{ace.settings.check('breadcrumbs' , 'fixed')}catch(e){}
										</script>
					<ul class="breadcrumb">
						<li><i class="ace-icon fa fa-home home-icon"></i> 支付风控 <span
							class="noshow jt">></span></li>
						<li class="active">编辑限量规则</li>
					</ul>
					<!-- /.breadcrumb -->
				</div>

				<!-- /section:basics/content.breadcrumbs -->
				<div class="page-content">
					<div class="row">
						<div class="col-xs-12">

							<div class="widget-box">

								<div class="widget-body">
									<div class="widget-main">
										<!-- #section:plugins/fuelux.wizard -->
										<div id="fuelux-wizard-container">
											<!-- #section:plugins/fuelux.wizard.container -->
											<div class="step-content pos-rel">
												<div class="step-pane active" data-step="1">
													<h3 class="lighter block green">输入以下的信息</h3>
													<form class="form-horizontal" id="validation-form"
														method="post">
														<input type="hidden" name="id" id="id"
															<c:if test="${basicRule.id != null}">value="${basicRule.id}"</c:if>
															<c:if test="${basicRule.id == null}">value="0"</c:if>
															class="ipt" maxlength="20" />

														<div class="form-group" id="timeTypeDiv">
															<label
																class="control-label col-xs-12 col-sm-3 no-padding-right"
																for="timeType">限时方式:</label>

															<div class="col-xs-12 col-sm-9">
																<div class="clearfix">
																	<select name="timeType" id="timeType"  class="form-control fl" style="width: 150px; float: left;" onchange="showTimeSelect()" >
																		<c:choose>
																			<c:when test="${fn:length(timeTypeList) > 0}">
																				<c:forEach items="${timeTypeList}" var="option">
																					<option value="${option.value}"
																						<c:if test="${option.value == basicRule.timeType}">selected="true"</c:if>>${option.text}</option>
																				</c:forEach>
																			</c:when>
																			<c:otherwise>
																				<option value=""></option>
																			</c:otherwise>
																		</c:choose>
																	</select>
																</div>
															</div>
														</div>
														<div class="form-group"  id="timeUnitDiv">
															<label
																class="control-label col-xs-12 col-sm-3 no-padding-right"
																for="timeUnit">时间单位:</label>

															<div class="col-xs-12 col-sm-9">
																<div class="clearfix">
																	<select name="timeUnit" id="timeUnit"  class="form-control fl"
										style="width: 150px; float: left;">
																		<c:choose>
																			<c:when test="${fn:length(timeUnitList) > 0}">
																				<c:forEach items="${timeUnitList}" var="option">
																					<option value="${option.value}"
																						<c:if test="${option.value == basicRule.timeUnit}">selected="true"</c:if>>${option.text}</option>
																				</c:forEach>
																			</c:when>
																			<c:otherwise>
																				<option value=""></option>
																			</c:otherwise>
																		</c:choose>
																	</select>
															<!-- 		
																	<input type="text" id="startUnit" name="startUnit" length="20"
																		
																		<c:if test="${basicRule.startUnit != null}">value="${basicRule.startUnit}"</c:if> />
																	<input type="text" id="endUnit" name="endUnit"  length="20"
																		
																		<c:if test="${basicRule.endUnit != null}">value="${basicRule.endUnit}"</c:if> />
															 -->
																</div>
															</div>
														</div><div class="form-group"  id="timeAroundDiv">
															<label
																class="control-label col-xs-12 col-sm-3 no-padding-right"
																for="timeUnit">时间绝对范围:</label>

															<div class="col-xs-12 col-sm-9">
																<div class="clearfix">
																<div>
																	<input id="startTime"
																		name="startTime" type="text"
																		onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:'%y-{%M-3}-%d',maxDate:'#F{$dp.$D(\'startTime\')|| \'%y-%M-%d\'}'});"
																		value="${basicRule.startTime}" class="fl form-text" />
																		
																	<input id="endTime"
																		name="endTime" type="text"
																		onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:'#F{$dp.$D(\'endTime\') || \'%y-{%M-3}-%d\'}',maxDate:'%y-{%M+3}-%d'});"
																		value="${basicRule.endTime}" class="fl form-text" />
																</div>
																</div>
															</div>
														</div>
														<div class="form-group"  id="timeRelativeDiv">
															<label
																class="control-label col-xs-12 col-sm-3 no-padding-right"
																for="relativeUnit">时间相对范围:</label>

															<div class="col-xs-12 col-sm-9">
																<div class="clearfix">
																<div>
																<input type="text" id="relativeValue" name="relativeValue" length="20"
																		
																		<c:if test="${basicRule.relativeValue != null}">value="${basicRule.relativeValue}"</c:if> />
																	<select name="relativeUnit" id="relativeUnit"  class="form-control fl" style="width: 150px; float: left;">
																		<c:choose>
																			<c:when test="${fn:length(timeUnitList) > 0}">
																				<c:forEach items="${timeUnitList}" var="option">
																					<option value="${option.value}"
																						<c:if test="${option.value == basicRule.relativeUnit}">selected="true"</c:if>>${option.text}</option>
																				</c:forEach>
																			</c:when>
																			<c:otherwise>
																				<option value=""></option>
																			</c:otherwise>
																		</c:choose>
																	</select>
																	
	
																</div>
																</div>
															</div>
														</div>
														<div class="form-group">
															<label
																class="control-label col-xs-12 col-sm-3 no-padding-right"
																for="limitType">限量方式:</label>

															<div class="col-xs-12 col-sm-9">
																<div class="clearfix">
																	<select name="limitType" id="limitType"  class="form-control fl" style="width: 150px; float: left;">
																		<c:choose>
																			<c:when test="${fn:length(limitTypeList) > 0}">
																				<c:forEach items="${limitTypeList}" var="option">
																					<option value="${option.value}"
																						<c:if test="${option.value == basicRule.limitType}">selected="true"</c:if>>${option.text}</option>
																				</c:forEach>
																			</c:when>
																			<c:otherwise>
																				<option value=""></option>
																			</c:otherwise>
																		</c:choose>
																	</select>
																</div>
															</div>
														</div>
														<div class="form-group">
															<label
																class="control-label col-xs-12 col-sm-3 no-padding-right"
																for="volume">限制量值:</label>

															<div class="col-xs-12 col-sm-9">
																<div class="clearfix">
																	<input type="text" id="volume" name="volume"
																		class="col-xs-12 col-sm-5"
																		<c:if test="${basicRule.volume != null}">value="${basicRule.volume}"</c:if> />
																</div>
															</div>
														</div>
														<div class="form-group">
															<label
																class="control-label col-xs-12 col-sm-3 no-padding-right"
																for="action">规则:</label>

															<div class="col-xs-12 col-sm-9">
																<div class="clearfix">
								
																	<select name="action" id="action" class="form-control fl" style="width: 150px; float: left;">
																		<c:choose>
																			<c:when test="${fn:length(actionList) > 0}">
																				<c:forEach items="${actionList}" var="option">
																					<option value="${option.value}"
																						<c:if test="${option.value == basicRule.action}">selected="true"</c:if>>${option.text}</option>
																				</c:forEach>
																			</c:when>
																			<c:otherwise>
																				<option value=""></option>
																			</c:otherwise>
																		</c:choose>
																	</select>
																</div>
															</div>
														</div>
														
														<div class="form-group">
															<label
																class="control-label col-xs-12 col-sm-3 no-padding-right"
																for="channelId">号码:</label>

															<div class="col-xs-12 col-sm-9">
																<div class="clearfix">
																	<select name="mobileSelectType" id="mobileSelectType" class="form-control fl" style="width: 150px; float: left;" onchange="showHtmlSelect('mobile', 'mobileSelectType','-7')" >
																		<c:choose>
																			<c:when test="${fn:length(selectTypeList) > 0}">
																				<c:forEach items="${selectTypeList}" var="option">
																					<option value="${option.value}"
																						<c:if test="${(option.value == basicRule.mobile && (option.value == '-8' || option.value == '-9')) || (option.value == '-7' && basicRule.mobile != '-8' && basicRule.mobile != '-9' && basicRule.mobile != null)}">selected="true"</c:if>>${option.text}</option>
																				</c:forEach>
																			</c:when>
																			<c:otherwise>
																				<option value=""></option>
																			</c:otherwise>
																		</c:choose>
																	</select>
																	<input type="text" id="mobile" name="mobile"
																		class="col-xs-12 col-sm-5"
																		<c:if test="${basicRule.mobile != null}">value="${basicRule.mobile}"</c:if> />
																</div>
															</div>
														</div>
														<div class="form-group">
															<label
																class="control-label col-xs-12 col-sm-3 no-padding-right"
																for="channelId">IP:</label>

															<div class="col-xs-12 col-sm-9">
																<div class="clearfix">
																	<select name="ipSelectType" id="ipSelectType" class="form-control fl" style="width: 150px; float: left;" onchange="showHtmlSelect('ip', 'ipSelectType','-7')" >
																		<c:choose>
																			<c:when test="${fn:length(selectTypeList) > 0}">
																				<c:forEach items="${selectTypeList}" var="option">
																					<option value="${option.value}"
																						<c:if test="${(option.value == basicRule.ip && (option.value == '-8' || option.value == '-9')) || (option.value == '-7' && basicRule.ip != '-8' && basicRule.ip != '-9' && basicRule.ip != null) }">selected="true"</c:if>>${option.text}</option>
																				</c:forEach>
																			</c:when>
																			<c:otherwise>
																				<option value=""></option>
																			</c:otherwise>
																		</c:choose>
																	</select>
																	<input type="text" id="ip" name="ip"
																		class="col-xs-12 col-sm-5"
																		<c:if test="${basicRule.ip != null}">value="${basicRule.ip}"</c:if> />
																</div>
															</div>
														</div>
														<div class="form-group">
															<label
																class="control-label col-xs-12 col-sm-3 no-padding-right"
																for="channelId">充值账号:</label>

															<div class="col-xs-12 col-sm-9">
																<div class="clearfix">
																	<select name="usernameSelectType" id="usernameSelectType" class="form-control fl" style="width: 150px; float: left;" onchange="showHtmlSelect('username', 'usernameSelectType','-7')" >
																		<c:choose>
																			<c:when test="${fn:length(selectTypeList) > 0}">
																				<c:forEach items="${selectTypeList}" var="option">
																					<option value="${option.value}"
																						<c:if test="${(option.value == basicRule.username && (option.value == '-8' || option.value == '-9')) || (option.value == '-7' && basicRule.username != '-8' && basicRule.username != '-9' && basicRule.username != null) }">selected="true"</c:if>>${option.text}</option>
																				</c:forEach>
																			</c:when>
																			<c:otherwise>
																				<option value=""></option>
																			</c:otherwise>
																		</c:choose>
																	</select>
																	<input type="text" id="username" name="username"
																		class="col-xs-12 col-sm-5"
																		<c:if test="${basicRule.username != null}">value="${basicRule.username}"</c:if> />
																</div>
															</div>
														</div>
														
														<div class="form-group">
															<label
																class="control-label col-xs-12 col-sm-3 no-padding-right"
																for="channelId">渠道:</label>

															<div class="col-xs-12 col-sm-9">
																<div class="clearfix">

																	<select name="channelSelectType" id="channelSelectType"  class="form-control fl" style="width: 150px; float: left;" onchange="showHtmlSelect('channelId', 'channelSelectType','-7')" >
																		<c:choose>
																			<c:when test="${fn:length(selectTypeList) > 0}">
																				<c:forEach items="${selectTypeList}" var="option">
																					<option value="${option.value}"
																						<c:if test="${(option.value == basicRule.channelId && (option.value == '-8' || option.value == '-9')) || (option.value == '-7' && basicRule.channelId != '-8' && basicRule.channelId != '-9' && basicRule.channelId != null) }">selected="true"</c:if>>${option.text}</option>
																				</c:forEach>
																			</c:when>
																			<c:otherwise>
																				<option value=""></option>
																			</c:otherwise>
																		</c:choose>
																	</select>

																	<select name="channelId" id="channelId"  class="form-control fl" style="width: 150px; float: left;">
																		<c:choose>
																			<c:when test="${fn:length(channelList) > 0}">
																				<c:forEach items="${channelList}" var="option">
																					<option value="${option.value}"
																						<c:if test="${option.value == basicRule.channelId}">selected="true"</c:if>>${option.text}</option>
																				</c:forEach>
																			</c:when>
																			<c:otherwise>
																				<option value=""></option>
																			</c:otherwise>
																		</c:choose>
																	</select>

																</div>
															</div>
														</div>
														
														<div class="form-group">
															<label
																class="control-label col-xs-12 col-sm-3 no-padding-right"
																for="provinceId">省份:</label>
															<div class="col-xs-12 col-sm-9">
																<div class="clearfix">
																	<select name="provinceSelectType" id="provinceSelectType" class="form-control fl" style="width: 150px; float: left;" onchange="showHtmlSelect('provinceId', 'provinceSelectType','-7')" >
																		<c:choose>
																			<c:when test="${fn:length(selectTypeList) > 0}">
																				<c:forEach items="${selectTypeList}" var="option">
																					<option value="${option.value}"
																						<c:if test="${(option.value == basicRule.provinceId && (option.value == -8 || option.value == -9)) || (option.value == -7 && basicRule.provinceId != '-8' && basicRule.provinceId != '-9' && basicRule.provinceId != null) }">selected="true"</c:if>>${option.text}</option>
																				</c:forEach>
																			</c:when>
																			<c:otherwise>
																				<option value=""></option>
																			</c:otherwise>
																		</c:choose>
																	</select>
																	<select name="provinceId" id="provinceId" class="form-control fl" style="width: 150px; float: left;" onchange="changeCity()">
																		<c:choose>
																			<c:when test="${fn:length(provinceList) > 0}">
																				<c:forEach items="${provinceList}" var="option">
																					<option value="${option.value}"
																						<c:if test="${option.value == basicRule.provinceId}">selected="true"</c:if>>${option.text}</option>
																				</c:forEach>
																			</c:when>
																			<c:otherwise>
																				<option value=""></option>
																			</c:otherwise>
																		</c:choose>
																	</select>
																</div>
															</div>
														</div>
														<div class="form-group">
															<label
																class="control-label col-xs-12 col-sm-3 no-padding-right"
																for="cityId">城市:</label>
															<div class="col-xs-12 col-sm-9">
																<div class="clearfix">
																	<select name="citySelectType" id="citySelectType"  class="form-control fl" style="width: 150px; float: left;" onchange="showHtmlSelect('cityId', 'citySelectType','-7')" >
																		<c:choose>
																			<c:when test="${fn:length(selectTypeList) > 0}">
																				<c:forEach items="${selectTypeList}" var="option">
																					<option value="${option.value}"
																						<c:if test="${(option.value == basicRule.cityId && (option.value == -8 || option.value == -9)) || (option.value == -7 && basicRule.cityId !='-8' && basicRule.cityId != '-9' && basicRule.cityId != null) }">selected="true"</c:if>>${option.text}</option>
																				</c:forEach>
																			</c:when>
																			<c:otherwise>
																				<option value=""></option>
																			</c:otherwise>
																		</c:choose>
																	</select>
																	<select name="cityId" id="cityId" class="form-control fl" style="width: 150px; float: left;">

																		<c:choose>
																			<c:when test="${fn:length(cityList) > 0}">
																				<c:forEach items="${cityList}" var="option">
																					<option value="${option.value}"
																						<c:if test="${option.value == basicRule.cityId}">selected="true"</c:if>>${option.text}</option>
																				</c:forEach>
																			</c:when>
																			<c:otherwise>
																				<option value=""></option>
																			</c:otherwise>
																		</c:choose>
																	</select>
																</div>
															</div>
														</div>


														<div class="form-group">
															<label
																class="control-label col-xs-12 col-sm-3 no-padding-right"
																for="merchantId">商户:</label>

															<div class="col-xs-12 col-sm-9">
																<div class="clearfix">
																	<select name="merchantSelectType" id="merchantSelectType" class="form-control fl" style="width: 150px; float: left;" onchange="showHtmlSelect('merchantId', 'merchantSelectType','-7')" >
																		<c:choose>
																			<c:when test="${fn:length(selectTypeList) > 0}">
																				<c:forEach items="${selectTypeList}" var="option">
																					<option value="${option.value}"
																						<c:if test="${(option.value == basicRule.merchantId && (option.value == '-8' || option.value == '-9')) || (option.value == '-7' && basicRule.merchantId != '-8' && basicRule.merchantId != '-9' && basicRule.merchantId != null) }">selected="true"</c:if>>${option.text}</option>
																				</c:forEach>
																			</c:when>
																			<c:otherwise>
																				<option value=""></option>
																			</c:otherwise>
																		</c:choose>
																	</select>
																	<select name="merchantId" id="merchantId" class="form-control fl"  style="width: 150px; float: left;">
																		<c:choose>
																			<c:when test="${fn:length(merchantList) > 0}">
																				<c:forEach items="${merchantList}" var="option">
																					<option value="${option.value}"
																						<c:if test="${option.value == basicRule.merchantId}">selected="true"</c:if>>${option.text}</option>
																				</c:forEach>
																			</c:when>
																			<c:otherwise>
																				<option value=""></option>
																			</c:otherwise>
																		</c:choose>
																	</select>
																</div>
															</div>
														</div>
														<div class="form-group">
															<label
																class="control-label col-xs-12 col-sm-3 no-padding-right"
																for="merchantId">产品:</label>

															<div class="col-xs-12 col-sm-9">
																<div class="clearfix">
																	<select name="productSelectType" id="productSelectType" class="form-control fl" style="width: 150px; float: left;" onchange="showHtmlSelect('productId', 'productSelectType','-7')" >
																		<c:choose>
																			<c:when test="${fn:length(selectTypeList) > 0}">
																				<c:forEach items="${selectTypeList}" var="option">
																					<option value="${option.value}"
																						<c:if test="${(option.value == basicRule.productId && (option.value == '-8' || option.value == '-9')) || (option.value == '-7' && basicRule.productId != '-8' && basicRule.productId != '-9' && basicRule.productId != null) }">selected="true"</c:if>>${option.text}</option>
																				</c:forEach>
																			</c:when>
																			<c:otherwise>
																				<option value=""></option>
																			</c:otherwise>
																		</c:choose>
																	</select>
																	<select name="productId" id="productId"
																		 class="form-control fl" style="width: 150px; float: left;">
																		<c:choose>
																			<c:when test="${fn:length(productList) > 0}">
																				<c:forEach items="${productList}" var="option">
																					<option value="${option.value}"
																						<c:if test="${option.value == basicRule.productId}">selected="true"</c:if>>${option.text}</option>
																				</c:forEach>
																			</c:when>
																			<c:otherwise>
																				<option value=""></option>
																			</c:otherwise>
																		</c:choose>
																	</select>
																</div>
															</div>
														</div>
														<div class="form-group">
															<label
																class="control-label col-xs-12 col-sm-3 no-padding-right"
																for="merchantId">计费点:</label>

															<div class="col-xs-12 col-sm-9">
																<div class="clearfix">
																	<select name="pointSelectType" id="pointSelectType" class="form-control fl" style="width: 150px; float: left;" onchange="showHtmlSelect('pointId', 'pointSelectType','-7')" >
																		<c:choose>
																			<c:when test="${fn:length(selectTypeList) > 0}">
																				<c:forEach items="${selectTypeList}" var="option">
																					<option value="${option.value}"
																						<c:if test="${(option.value == basicRule.pointId && (option.value == '-8' || option.value == '-9')) || (option.value == '-7' && basicRule.pointId != '-8' && basicRule.pointId != '-9' && basicRule.pointId != null) }">selected="true"</c:if>>${option.text}</option>
																				</c:forEach>
																			</c:when>
																			<c:otherwise>
																				<option value=""></option>
																			</c:otherwise>
																		</c:choose>
																	</select>
																	<select name="pointId" id="pointId" class="form-control fl" style="width: 150px; float: left;">
																		<c:choose>
																			<c:when test="${fn:length(pointList) > 0}">
																				<c:forEach items="${pointList}" var="option">
																					<option value="${option.value}"
																						<c:if test="${option.value == basicRule.pointId}">selected="true"</c:if>>${option.text}</option>
																				</c:forEach>
																			</c:when>
																			<c:otherwise>
																				<option value=""></option>
																			</c:otherwise>
																		</c:choose>
																	</select>
																</div>
															</div>
														</div>
														
														<div class="form-group">
															<label
																class="control-label col-xs-12 col-sm-3 no-padding-right"
																for="status">状态:</label>

															<div class="col-xs-12 col-sm-9">
																<div class="clearfix">
								
																	<select name="status" id="status" class="form-control fl" style="width: 150px; float: left;">
																		<c:choose>
																			<c:when test="${fn:length(statusList) > 0}">
																				<c:forEach items="${statusList}" var="option">
																					<option value="${option.value}"
																						<c:if test="${option.value == basicRule.status}">selected="true"</c:if>>${option.text}</option>
																				</c:forEach>
																			</c:when>
																			<c:otherwise>
																				<option value=""></option>
																			</c:otherwise>
																		</c:choose>
																	</select>
																</div>
															</div>
														</div>

														<div class="space-2"></div>
														<button class="btn btn-sm btn-primary no-border"
															onclick="saveBasicRule()">保存</button>
													</form>
												</div>
											</div>
											<!-- /section:plugins/fuelux.wizard.container -->
										</div>
										<!-- /section:plugins/fuelux.wizard -->
									</div>
									<!-- /.widget-main -->
								</div>
								<!-- /.widget-body -->
							</div>
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

	<!-- page specific plugin scripts -->
	<script src="${ctx}/template/ace/assets/js/fuelux/fuelux.wizard.js"></script>
	<script src="${ctx}/template/ace/assets/js/jquery.validate.js"></script>
	<script src="${ctx}/template/ace/assets/js/additional-methods.js"></script>
	<script src="${ctx}/template/ace/assets/js/jquery.maskedinput.js"></script>
	<script src="${ctx}/template/ace/assets/js/select2.js"></script>

	<script src="${ctx}/template/ace/assets/js/jquery-ui.custom.js"></script>
	<script src="${ctx}/template/ace/assets/js/jquery.ui.touch-punch.js"></script>
	<script src="${ctx}/template/ace/assets/js/bootbox.js"></script>
	<script src="${ctx}/template/ace/assets/js/jquery.easypiechart.js"></script>
	<script src="${ctx}/template/ace/assets/js/jquery.gritter.js"></script>
	<script src="${ctx}/template/ace/assets/js/spin.js"></script>

	<!-- ace scripts -->
	<script src="${ctx}/template/ace/assets/js/ace/elements.scroller.js"></script>
	<script src="${ctx}/template/ace/assets/js/ace/elements.colorpicker.js"></script>
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
	<script src="${ctx}/template/ace/assets/js/ace/ace.sidebar-scroll-1.js"></script>
	<script src="${ctx}/template/ace/assets/js/ace/ace.submenu-hover.js"></script>
	<script src="${ctx}/template/ace/assets/js/ace/ace.widget-box.js"></script>
	<script src="${ctx}/template/ace/assets/js/ace/ace.settings.js"></script>
	<script src="${ctx}/template/ace/assets/js/ace/ace.settings-rtl.js"></script>
	<script src="${ctx}/template/ace/assets/js/ace/ace.settings-skin.js"></script>
	<script src="${ctx}/template/ace/assets/js/ace/ace.widget-on-reload.js"></script>
	<script
		src="${ctx}/template/ace/assets/js/ace/ace.searchbox-autocomplete.js"></script>

	<!-- inline scripts related to this page -->
	<script type="text/javascript">
			
			jQuery(function($) {
				showTimeSelect();
				showHtmlSelect('channelId', 'channelSelectType','-7');
				showHtmlSelect('provinceId', 'provinceSelectType','-7');
				showHtmlSelect('cityId', 'citySelectType','-7');
				showHtmlSelect('merchantId', 'merchantSelectType','-7');
				showHtmlSelect('productId', 'productSelectType','-7');
				showHtmlSelect('pointId', 'pointSelectType','-7');
				showHtmlSelect('mobile', 'mobileSelectType','-7');
				showHtmlSelect('ip', 'ipSelectType','-7');
				showHtmlSelect('username', 'usernameSelectType','-7');
				$('[data-rel=tooltip]').tooltip();
			
				$(".select2").css('width','200px').select2({allowClear:true})
				.on('change', function(){
					$(this).closest('form').validate().element($(this));
				}); 
			
			
				var $validation = false;
				$('#fuelux-wizard-container')
				.ace_wizard({
					//step: 2 //optional argument. wizard will jump to step "2" at first
					//buttons: '.wizard-actions:eq(0)'
				})
				.on('actionclicked.fu.wizard' , function(e, info){
					if(info.step == 1 && $validation) {
						if(!$('#validation-form').valid()) e.preventDefault();
					}
				})
				.on('finished.fu.wizard', function(e) {
					bootbox.dialog({
						message: "您的信息已成功保存!", 
						buttons: {
							"success" : {
								"label" : "确定",
								"className" : "btn-sm btn-primary"
							}
						}
					});
				}).on('stepclick.fu.wizard', function(e){
					//e.preventDefault();//this will prevent clicking and selecting steps
				});
			
			
				//jump to a step
				/**
				var wizard = $('#fuelux-wizard-container').data('fu.wizard')
				wizard.currentStep = 3;
				wizard.setState();
				*/
				$('#validation-form').validate({
					errorElement: 'div',
					errorClass: 'help-block',
					focusInvalid: false,
					ignore: "",
					rules: {
						volume: {
							required: true,
							intOrFloat: true
							
						},
						status: {
							required: true
						}
					},
			
					messages: {
						volume: {
							required: "请输入限量值",
							chinese: "输入正确数字"
						},
						status: "请选择状态"
						
					},
			
			
					highlight: function (e) {
						$(e).closest('.form-group').removeClass('has-info').addClass('has-error');
					},
			
					success: function (e) {
						$(e).closest('.form-group').removeClass('has-error');//.addClass('has-info');
						$(e).remove();
					},
			
					errorPlacement: function (error, element) {
						if(element.is('input[type=checkbox]') || element.is('input[type=radio]')) {
							var controls = element.closest('div[class*="col-"]');
							if(controls.find(':checkbox,:radio').length > 1) controls.append(error);
							else error.insertAfter(element.nextAll('.lbl:eq(0)').eq(0));
						}
						else if(element.is('.select2')) {
							error.insertAfter(element.siblings('[class*="select2-container"]:eq(0)'));
						}
						else if(element.is('.chosen-select')) {
							error.insertAfter(element.siblings('[class*="chosen-container"]:eq(0)'));
						}
						else error.insertAfter(element.parent());
					},
			
					submitHandler: function (form) {
					},
					invalidHandler: function (form) {
					}
				});
				$('#modal-wizard-container').ace_wizard();
				$('#modal-wizard .wizard-actions .btn[data-dismiss=modal]').removeAttr('disabled');

				$(document).one('ajaxloadstart.page', function(e) {
					//in ajax mode, remove remaining elements before leaving page
					$('[class*=select2]').remove();
					$.gritter.removeAll();
				});
			})
			
			jQuery.validator.addMethod("unnormal", function(value,element) {   
    						return /^[^·`~!@#$%^&*()_+<>?:"{},，、。’；【】.\/;'[\]]+$/.test(value);
					}, "输入值不能为空和包含其他非法字符");		
			jQuery.validator.addMethod("englishUpperCase", function(value,element) {   
    						return /^[A-Z]+$/.test(value);
					}, "请输入大写英文");
			jQuery.validator.addMethod("chinese", function(value,element) {   
    						return /^[\u0391-\uFFE5]+$/i.test(value);
					}, "请输入中文");
			jQuery.validator.addMethod("intOrFloat", function(value,element) {   
				return /^(-)?\d+(\.\d+)?$/i.test(value); 
		}, "请输入数字，并确保格式正确");
		</script>

	<!-- the following scripts are used in demo only for onpage help and you don't need them -->
	<link rel="stylesheet"
		href="${ctx}/template/ace/assets/css/ace.onpage-help.css" />
	<link rel="stylesheet"
		href="${ctx}/template/ace/docs/assets/js/themes/sunburst.css" />

	<script type="text/javascript"> ace.vars['base'] = '..'; </script>
	<script src="${ctx}/template/ace/assets/js/ace/elements.onpage-help.js"></script>
	<script src="${ctx}/template/ace/assets/js/ace/ace.onpage-help.js"></script>
	<script src="${ctx}/template/ace/docs/assets/js/rainbow.js"></script>
	<script src="${ctx}/template/ace/docs/assets/js/language/generic.js"></script>
	<script src="${ctx}/template/ace/docs/assets/js/language/html.js"></script>
	<script src="${ctx}/template/ace/docs/assets/js/language/css.js"></script>
	<script src="${ctx}/template/ace/docs/assets/js/language/javascript.js"></script>
</body>
<script type="text/javascript">
function showHtmlSelect(targetId, selfId,showValue)
{
	var value = $("#"+selfId).val();
	if (showValue == value)
	{
		$("#"+targetId).show();
	}
	else
	{
		$("#"+targetId).hide();
	}
}
function showTimeSelect()
{
	var value = $("#timeType").val();
	if (value == "0")
	{
		$("#timeUnitDiv").show();
		$("#timeAroundDiv").hide();
		$("#timeRelativeDiv").hide();
	}
	else if (value == "1")
	{
		$("#timeUnitDiv").hide();
		$("#timeAroundDiv").show();
		$("#timeRelativeDiv").hide();
	}
	else
	{
		$("#timeUnitDiv").hide();
		$("#timeAroundDiv").hide();
		$("#timeRelativeDiv").show();
	}
}



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
		$('#cityId').html(data);
		$("#cityId option[value='-7']").remove(); 
	}
	});
}
function saveBasicRule(){
	$('#validation-form').validate().checkForm();
	if ($('#validation-form').validate().valid())
	{
	var id = $("#id").val();
    var timeType = $("#timeType").val();
    var timeUnit = $("#timeUnit").val();
    var startTime = $("#startTime").val();
    var endTime = $("#endTime").val();
    var startUnit = $("#startUnit").val();
    var endUnit = $("#endUnit").val();
	var relativeUnit = $("#relativeUnit").val();
	var relativeValue = $("#relativeValue").val();
	
	if (timeType == '2')
	{
		if (relativeValue == '')
		{
			bootbox.dialog({
				message: "<span class='bigger-110'>请输入相对时间范围值</span>",
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
			return;
		}
	}
	else if (timeType == '1')
	{
		if (startTime == '')
		{
			bootbox.dialog({
				message: "<span class='bigger-110'>请输入起始时间</span>",
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
			return;
		}
		
		if (endTime == '')
		{
			bootbox.dialog({
				message: "<span class='bigger-110'>请输入结束时间</span>",
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
			return;
		}
	}

	if (startUnit == '')
	{
		startUnit = '0';
	}
	
	if (endUnit == '')
	{
		endUnit = '0';
	}

	if (relativeValue == '')
	{
		relativeValue = '0';
	}
	
	var limitType = $("#limitType").val();
    var volume = $("#volume").val();
    var channelSelectType = $("#channelSelectType").val();
    var channelId = $("#channelId").val();
	if (channelSelectType != '-7')
	{
		channelId = channelSelectType;
	}
	
	var mobileSelectType = $("#mobileSelectType").val(); 
    var mobile = $("#mobile").val();
	if (mobileSelectType != '-7')
	{
		mobile = mobileSelectType;
	}
	else
	{
		if (mobile == '')
		{
			bootbox.dialog({
				message: "<span class='bigger-110'>请输入号码</span>",
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
			return;
		}
	}
	
	var ipSelectType = $("#ipSelectType").val(); 
    var ip = $("#ip").val();
	if (ipSelectType != '-7')
	{
		ip = ipSelectType;
	}
	else
	{
		if (ip == '')
		{
			bootbox.dialog({
				message: "<span class='bigger-110'>请输入IP</span>",
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
			return;
		}
	}
	
	var usernameSelectType = $("#usernameSelectType").val(); 
    var username = $("#username").val();
	if (usernameSelectType != '-7')
	{
		username = usernameSelectType;
	}
	else
	{
		if (username == '')
		{
			alert(1);
			bootbox.dialog({
				message: "<span class='bigger-110'>请输入充值账号</span>",
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
			return;
		}
	}
    var provinceSelectType = $("#provinceSelectType").val(); 
    var provinceId = $("#provinceId").val();
	if (provinceSelectType != '-7')
	{
		provinceId = provinceSelectType;
	}
	
    var citySelectType = $("#citySelectType").val();
    var cityId = $("#cityId").val();
	if (citySelectType != '-7')
	{
		cityId = citySelectType;
	}
	
    var merchantSelectType = $("#merchantSelectType").val(); 
    var merchantId = $("#merchantId").val();
	if (merchantSelectType != '-7')
	{
		merchantId = merchantSelectType;
	}
	
    var productSelectType = $("#productSelectType").val(); 
    var productId = $("#productId").val();
	if (productSelectType != '-7')
	{
		productId = productSelectType;
	}
	
    var pointSelectType = $("#pointSelectType").val(); 
    var pointId = $("#pointId").val();
	if (pointSelectType != '-7')
	{
		pointId = pointSelectType;
	}
	
    var action = $("#action").val();
    var status = $("#status").val();
	
	bootbox.confirm("确定要保存吗?", 
		function(result) {
			if(result) {
				$.ajax({  
					data:{	id:id,
							timeType:timeType,
                            timeUnit:timeUnit,
							startUnit:startUnit,
							endUnit:endUnit,
                            startTime:startTime,
                            endTime:endTime,
							relativeValue:relativeValue,
							relativeUnit:relativeUnit,
                            limitType:limitType,
                            volume:volume,
                            channelId:channelId,
                            mobile:mobile,
							ip:ip,
							username:username,
                            provinceId:provinceId,
                            cityId:cityId,
                            merchantId:merchantId,
                            productId:productId,
                            pointId:pointId,
                            action:action,
                            status:status
						 },
					url : "${ctx}/payment/risk/basicRule/saveBasicRule",
					type:'post',
					cache:false,
					async:true,
					dataType:'json',
					success : function(data) {  
						if (data.result=='success')
						{
							bootbox.dialog({
								message: "<span class='bigger-110'>保存成功</span>",
								buttons: 			
								{
									"close" :
									{
										"label" : "关闭当前页",
										"className" : "btn-primary btn-sm",
										"callback": function() {
											//Example.show("great success");
											var currentTab = parent.$('#tabs').tabs('getSelected');
											var index = parent.$('#tabs').tabs('getTabIndex',currentTab);

											parent.$('#tabs').tabs('close', index);
										}
									},
									"cancel" :
									{
										"label" : "留在当前页",
										"className" : "btn-sm",
										"callback": function() {
										}
									}
								}
							});
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
}
</script>
</html>
