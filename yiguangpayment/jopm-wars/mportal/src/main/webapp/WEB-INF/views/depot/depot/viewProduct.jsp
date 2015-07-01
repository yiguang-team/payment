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
						<li class="active">导入产品</li>
					</ul>
					<!-- /.breadcrumb -->
				</div>

				<!-- /section:basics/content.breadcrumbs -->
				<div class="page-content">
					<div class="row">
						<div class="col-xs-12">
							<form class="form-horizontal" id="validation-form" method="post"
								enctype="multipart/form-data">
								<input type="hidden" id="batchId" name="batchId"
									value="${batchId}" /> <input type="hidden" id="mlist"
									name="mlist" value='${mlist}' /> <input type="hidden"
									id="columnNum" name="columnNum" value="${columnNum}" /> <input
									type="hidden" id="merchantId" name="merchantId"
									value="${merchantId}" /> <input type="hidden" id="productId"
									name="productId" value="${productId}" /> <input type="hidden"
									id="config" name="config" />

								<div class="form-group">

									<p class="alert alert-info">

										您将导入${merchantLabel}商户的${productLabel}产品的卡密数据，成功导入卡密数据需要必配卡号，密码，面值列，而有效起始日期和有效结束日期是可选的<br />
										确认导入之前请尽可能确认业务类型、运营商、产品、面值是否都已经配置正确，如果有不支持的选项，将无法成功导入<br />
										下面是预览5行EXCEL数据，确认无误后请设置各列数据对应的业务含义，如果设置为非导入列，那么此列数据将不会导入到系统中<br />
										如果文件中包含有表头信息，请设置导入起始行，没有表头信息保持默认值1即代表从第1行开始导入
									</p>

									<div id="mDiv" />
								</div>
								<div class="space-4"></div>
								<div class="form-group">
									<label class="col-sm-3 control-label no-padding-right"
										for="startLineNum"> 导入起始行</label>

									<div class="col-sm-9">
										<input type="text" id="startLineNum" name="startLineNum"
											placeholder="请输入导入起始行" class="col-xs-10 col-sm-5" value="1" />
									</div>
								</div>
								<div class="form-group">
									<label class="col-sm-3 control-label no-padding-right"
										for="totalAmt"> 面值总额</label>

									<div class="col-sm-9">
										<input type="text" id="totalAmt" name="totalAmt"
											placeholder="请输入面值总额" class="col-xs-10 col-sm-5" />
									</div>
								</div>
								<div class="form-group">
									<label class="col-sm-3 control-label no-padding-right"
										for="totalNum"> 卡密总数</label>

									<div class="col-sm-9">
										<input type="text" id="totalNum" name="totalNum"
											placeholder="请输入卡密总数" class="col-xs-10 col-sm-5" />
									</div>
								</div>
								<div class="clearfix form-actions">
									<div class="col-md-offset-3 col-md-9">
										<button class="btn btn-info no-border blue" type="button"
											onclick="importProduct()">

											<i class="ace-icon fa fa-check bigger-110"></i> 确认导入
										</button>
										<!--
										&nbsp; &nbsp; &nbsp;
										<button class="btn no-border" type="reset">
											<i class="ace-icon fa fa-undo bigger-110"></i>
											重置
										</button>
										-->
									</div>
								</div>
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
				showTable();
				
				$(".select2").css('width','200px').select2({allowClear:true})
				.on('change', function(){
					$(this).closest('form').validate().element($(this));
				}); 
				
				$('#validation-form').validate({
					errorElement: 'div',
					errorClass: 'help-block',
					focusInvalid: false,
					ignore: "",
					rules: {
						totalAmt: {
							required: true
						},
						totalNum: {
							required: true
						}
					},
			
					messages: {
						totalAmt: "请输入面值总额",
						totalNum: "请输入卡密总数",
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
				
				$(document).one('ajaxloadstart.page', function(e) {
					//in ajax mode, remove remaining elements before leaving page
					$('[class*=select2]').remove();
					$.gritter.removeAll();
				});
			})
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

function importProduct(){
		var columnNum = $('#columnNum').val();

		var configStr = '[';
		for (var i=0;i<columnNum;i++)
		{
			configStr += '{"key":"'+i+'","value":"'+$('#COLUMN_'+i).val()+'"}';
			
			if (i<columnNum-1)
			{
				configStr += ',';
			}
		}
		configStr += ']';
		$('#config').val(configStr);

		var configList = eval('(' + configStr + ')');
		var exsitEq = false;;
		var exsitCardId = false;
		var exsitCardPwd = false;
		var exsitFaceAmt = false;
		
		for (var i=0;i<columnNum;i++)
		{
			for (var n=0;n<columnNum;n++)
			{
				if ((configList[i].value == configList[n].value) && (n!=i) &&(configList[n].value != ''))	
				{
					exsitEq = true;
					break;
				}
			} 	
			
			if (configList[i].value == 'CARD_ID')
			{
				exsitCardId = true;
			}
			
			if (configList[i].value == 'CARD_PWD')
			{
				exsitCardPwd = true;
			}
			
			if (configList[i].value == 'FACE_AMOUNT')
			{
				exsitFaceAmt = true;
			}
		} 

		if (exsitEq)
		{
			alert("不能设置相同的列！");
			return;
		}
		
		if (!exsitCardId)
		{
			alert("必须设置卡号对应的列！");
			return;
		}
		
		if (!exsitCardPwd)
		{
			alert("必须设置密码对应的列！");
			return;
		}
		
		if (!exsitFaceAmt)
		{
			alert("必须设置面值对应的列！");
			return;
		}
		
		bootbox.confirm("确定要导入吗?", 
		function(result) {
			if(result) {
				$.ajax({  
					data:{
							batchId:$("#batchId").val(),
							merchantId:$("#merchantId").val(),
							productId:$("#productId").val(),
							startLineNum:$("#startLineNum").val(),
							config:$("#config").val(),
							totalAmt:$("#totalAmt").val(),
							totalNum:$("#totalNum").val()
						 },
					url : "${ctx}/depot/depot/importDepot",
					type:'post',
					cache:false,
					async:true,
					dataType:'json',
					success : function(data) {  
						if (data.result=='success')
						{
							bootbox.dialog({
								message: "<span class='bigger-110'>导入成功</span>",
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
								message: "<span class='bigger-110'>"+data.message+"，请重新导入</span>",
								buttons: 			
								{
									"confirm" :
									{
										"label" : "确定",
										"className" : "btn-sm",
										"callback": function() {
											window.location.ref="${ctx}/depot/depot/toImportProduct";
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
	
	function showTable(){
		
	
	
	
		var mlist = eval('(' + $('#mlist').val() + ')');
		var columnNum = $('#columnNum').val();
		
		var html = '<table id="listTable" class="table table-striped table-bordered table-hover nospace">';
		
		var th = '<tr>';
		for (var i=0;i<columnNum;i++)
		{
			th += '<th class="center">第'+(i+1)+'列</th>';
		}
		th += '</tr>';
		html += th;
		var tbody = '<tbody>';
		
		for (var i=0;i<mlist.length;i++)
		{
			tbody += '<tr>';
			for (var n=0;n<columnNum;n++)
			{
				tbody += '<td class="center">'+eval('('+'mlist[i].COLUMN_'+n+ ')')+'</td>';
			}
			tbody += '</tr>';
		}
		
		tbody += '<tr>';

		for (var n=0;n<columnNum;n++)
		{
			var selectHtml = '本列设置为：<select id="COLUMN_'+n+'" name="COLUMN_'+n+'" class="select2" data-placeholder="请选择...">';
			selectHtml += '<option value="">非导入列</option>';
			selectHtml += '<option value="CARD_ID">卡号</option>';
			selectHtml += '<option value="CARD_PWD">密码</option>';
			selectHtml += '<option value="FACE_AMOUNT">面值</option>';
			selectHtml += '<option value="USEFUL_START_DATE">有效期起始日期</option>';
			selectHtml += '<option value="USEFUL_END_DATE">有效期结束日期</option>';
			selectHtml += '</select>';
			tbody += '<td class="center">'+selectHtml+'</td>';
		}
		tbody += '</tr>';
		
		tbody += '</tbody>';
		html += tbody;
		html += '</table>';
		$('#mDiv').html(html);
	}
	
</script>
</html>
