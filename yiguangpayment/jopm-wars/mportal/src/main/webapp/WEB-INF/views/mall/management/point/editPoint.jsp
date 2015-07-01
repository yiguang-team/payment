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
<head>
<base href="<%=basePath%>">

<title>翼光联运支付管理系统-编辑计费点</title>
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
						<li><i class="ace-icon fa fa-home home-icon"></i> 产品管理 <span
							class="noshow jt">></span></li>
						<li class="active">编辑计费点</li>
					</ul>
					<!-- /.breadcrumb -->
				</div>

				<!-- /section:basics/content.breadcrumbs -->
				<div class="page-content">
					<div class="row">
						<div class="col-xs-12">

							<form class="form-horizontal" id="validation-form" method="post">
								<input type="hidden" name="id" id="id"
									<c:if test="${point.id != null}">value="${point.id}"</c:if>
									<c:if test="${point.id == null}">value="0"</c:if> />
								<div class="form-group">
									<label
										class="control-label col-xs-12 col-sm-3 no-padding-right"
										for="merchantId">商户:</label>
									<div class="col-xs-12 col-sm-9">
										<div class="clearfix">
											<select name="merchantId" id="merchantId"
												onchange='changeProduct()' class="select2"
												data-placeholder="请选择...">
												<c:choose>
													<c:when test="${fn:length(merchantList) > 0}">
														<c:forEach items="${merchantList}" var="option">
															<option value="${option.value}"
																<c:if test="${option.value == point.merchantId}">selected="true"</c:if>>${option.text}</option>
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
										for="productId">产品:</label>
									<div class="col-xs-12 col-sm-9">
										<div class="clearfix">
											<div id='productDiv'>
												<select name="productId" id="productId" class="select2"
													data-placeholder="请选择...">
													<c:choose>
														<c:when test="${fn:length(productList) > 0}">
															<c:forEach items="${productList}" var="option">
																<option value="${option.value}"
																	<c:if test="${option.value == point.productId}">selected="true"</c:if>>${option.text}</option>
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
										for="chargingType">计费类型:</label>
									<div class="col-xs-12 col-sm-9">
										<div class="clearfix">
											<select name="chargingType" id="chargingType" class="select2"
												data-placeholder="请选择...">
												<c:choose>
													<c:when test="${fn:length(chargingTypeList) > 0}">
														<c:forEach items="${chargingTypeList}" var="option">
															<option value="${option.value}"
																<c:if test="${option.value == point.chargingType}">selected="true"</c:if>>${option.text}</option>
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
										for="faceAmount">开通渠道:</label>
									<div class="col-xs-12 col-sm-9">
										<div class="clearfix">
											<c:choose>
												<c:when test="${fn:length(pointChannelRelationList) > 0}">
													<c:forEach items="${pointChannelRelationList}" var="option">
														<div class="checkbox">
															<label> <input name="channelCheckbox"
																id="channelCheckbox" type="checkbox" class="ace"
																value="${option.value}"
																<c:forEach items="${checkedlist}" var="titem"><c:if test="${option.value == titem.channelId}">checked</c:if></c:forEach> /><span
																class="lbl">${option.text}</span>
															</label>
														</div>
													</c:forEach>
												</c:when>
											</c:choose>
										</div>
									</div>
								</div>
								<div class="form-group">
									<label
										class="control-label col-xs-12 col-sm-3 no-padding-right"
										for="faceAmount">面值:</label>

									<div class="col-xs-12 col-sm-9">
										<div class="clearfix">
											<input type="text" id="faceAmount" name="faceAmount"
												class="col-xs-12 col-sm-5"
												<c:if test="${point.faceAmount != null}">value="${point.faceAmount}"</c:if> />
										</div>
									</div>
								</div>
								<div class="form-group">
									<label
										class="control-label col-xs-12 col-sm-3 no-padding-right"
										for="payAmount">支付价格:</label>

									<div class="col-xs-12 col-sm-9">
										<div class="clearfix">
											<input type="text" id="payAmount" name="payAmount"
												class="col-xs-12 col-sm-5"
												<c:if test="${point.payAmount != null}">value="${point.payAmount}"</c:if> />
										</div>
									</div>
								</div>
								<div class="form-group">
									<label
										class="control-label col-xs-12 col-sm-3 no-padding-right"
										for="deliveryAmount">发货价格:</label>

									<div class="col-xs-12 col-sm-9">
										<div class="clearfix">
											<input type="text" id="deliveryAmount" name="deliveryAmount"
												class="col-xs-12 col-sm-5"
												<c:if test="${point.deliveryAmount != null}">value="${point.deliveryAmount}"</c:if> />
										</div>
									</div>
								</div>

								<div class="form-group">
									<label
										class="control-label col-xs-12 col-sm-3 no-padding-right"
										for="status">状态:</label>

									<div class="col-xs-12 col-sm-9">
										<div class="clearfix">
											<select name="status" id="status" class="select2"
												data-placeholder="请选择...">
												<c:choose>
													<c:when test="${fn:length(statusList) > 0}">
														<c:forEach items="${statusList}" var="option">
															<option value="${option.value}"
																<c:if test="${option.value == point.status}">selected="true"</c:if>>${option.text}</option>
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
											<select name="provinceId" id="provinceId" class="select2"
												onchange="changeCity()" data-placeholder="请选择...">
												<c:choose>
													<c:when test="${fn:length(provinceList) > 0}">
														<c:forEach items="${provinceList}" var="option">
															<option value="${option.value}"
																<c:if test="${option.value.equals(point.provinceId)}">selected="true"</c:if>>${option.text}</option>
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
											<select name="cityId" id="cityId" class="select2"
												data-placeholder="请选择...">

												<c:choose>
													<c:when test="${fn:length(cityList) > 0}">
														<c:forEach items="${cityList}" var="option">
															<option value="${option.value}"
																<c:if test="${option.value.equals(point.cityId)}">selected="true"</c:if>>${option.text}</option>
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
										for="name">名称:</label>

									<div class="col-xs-12 col-sm-9">
										<div class="clearfix">
											<input type="text" id="name" name="name"
												class="col-xs-12 col-sm-5"
												<c:if test="${point.name != null}">value="${point.name}"</c:if> />
										</div>
									</div>
								</div>
								<div class="space-2"></div>
								<button class="btn btn-sm btn-primary no-border"
									onclick="savePoint()">保存</button>
							</form>
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
					debug:true,
					ignore: "",
					rules: {
						status: {
							required: true
						},
						merchantId: {
							required: true
						},
						name: {
							required: true
						},
						chargingType: {
							required: true
						},
						productId: {
							required: true
						},
						faceAmount: {
							required: true,
							intOrFloat: true
						},
						payAmount: {
							required: true,
							intOrFloat: true
						},
						deliveryAmount: {
							required: true,
							intOrFloat: true
						}
					},
			
					messages: {
						status: "请选择状态",
						merchantId: "请选择商户",
						name: "请输入名称",
						chargingType:"请选择计费类型",
						productId: {
							required:"请选择产品分类",
							intOrFloat:"请输入数字，并确保格式正确"
						},
						faceAmount: {
							required:"请输入产品面值",
							intOrFloat:"请输入数字，并确保格式正确"
						},
						payAmount: {
							required:"请输入产品支付价格",
							intOrFloat:"请输入数字，并确保格式正确"
						},
						deliveryAmount: {
							required:"请输入产品发货价格",
							intOrFloat:"请输入数字，并确保格式正确"
						}
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
				
				
				/**
				$('#date').datepicker({autoclose:true}).on('changeDate', function(ev) {
					$(this).closest('form').validate().element($(this));
				});
				
				$('#mychosen').chosen().on('change', function(ev) {
					$(this).closest('form').validate().element($(this));
				});
				*/
				
				
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
    						return /^\d+(\.\d+)?$/i.test(value); 
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
		$('#productDiv').html('');
		$('#productDiv').html('<select name="productId" id="productId"  class="select2" data-placeholder="请选择...">'+data+'</select>');
		$('#productId').css('width','200px').select2({allowClear:true})
		.on('change', function(){
			$(this).closest('form').validate().element($(this));
		}); 
	}
	});
}

function savePoint(){
	
	var str="";
	$("input[name='channelCheckbox']").each(function(){ 
		if($(this).prop("checked")){
			str += $(this).val()+","
		}
	})
	
	$('#validation-form').validate().checkForm();
	if ($('#validation-form').validate().valid())
	{
	bootbox.confirm("确定要保存吗?", 
		function(result) {
			if(result) {
				$.ajax({  
					data:{
							id:$("#id").val(),
							name:$("#name").val(),
							merchantId:$("#merchantId").val(),
							productId:$("#productId").val(),
							provinceId:$("#provinceId").val(),
							cityId:$("#cityId").val(),
							chargingType:$("#chargingType").val(),
							status:$("#status").val(),
							faceAmount:$("#faceAmount").val(),
							payAmount:$("#payAmount").val(),
							deliveryAmount:$("#deliveryAmount").val(),
							pointChannelRelationIDs:str
						 },
					url : "${ctx}/mall/management/point/savePoint",
					type:'post',
					cache:false,
					async:true,
					dataType:'json',
					success:function(data) {  
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
