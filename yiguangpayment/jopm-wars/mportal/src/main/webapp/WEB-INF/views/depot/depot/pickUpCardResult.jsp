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

<title>翼光联运支付管理系统-人工提卡</title>
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
		action="${ctx}/depot/depot/pickUpCarkManually">

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
							<li><i class="ace-icon fa fa-home home-icon"></i> 卡库管理 <span
								class="noshow jt">></span></li>
							<li class="active">人工提卡</li>
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
									</select> <span class="labeltxt fl">产&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;品：</span>
									<select name="productId" id="productId"
										onchange="changeChargingPoint()" class="form-control fl"
										style="width: 150px; float: left;">
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
									</select> <span class="labeltxt fl">计费点名称：</span> <select
										name="chargingPointId" id="chargingPointId"
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



									<div class="btns">
										<input type="submit" class="btn btn-primary btn-sm no-border"
											value="提卡" />
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


										<table id="sample-table-1"
											class="table table-striped table-bordered table-hover nospace">
											<thead>
												<tr>
													<th>卡号</th>
													<th class="hidden-480">密码</th>
												</tr>
											</thead>
											<tbody>

												<c:choose>
													<c:when test="${fn:length(mlist) > 0}">
														<c:forEach items="${mlist}" var="record"
															varStatus="status">
															<tr>
																<td>${record.cardId}</td>
																<td>${record.cardPwd}</td>
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

		<!-- inline scripts related to this page -->
		<script type="text/javascript">
			jQuery(function($) {
				var oTable1 = 
				$('#sample-table-2')
				//.wrap("<div class='dataTables_borderWrap' />")   //if you are applying horizontal scrolling (sScrollX)
				.dataTable( {
					bAutoWidth: false,
					"aoColumns": [
					  { "bSortable": false },
					  null, null,null, null, null,
					  { "bSortable": false }
					],
					"aaSorting": [],
			
					//,
					//"sScrollY": "200px",
					//"bPaginate": false,
			
					//"sScrollX": "100%",
					//"sScrollXInner": "120%",
					//"bScrollCollapse": true,
					//Note: if you are applying horizontal scrolling (sScrollX) on a ".table-bordered"
					//you may want to wrap the table inside a "div.dataTables_borderWrap" element
			
					//"iDisplayLength": 50
			    } );
				/**
				var tableTools = new $.fn.dataTable.TableTools( oTable1, {
					"sSwfPath": "${ctx}/template/ace/${ctx}/template/ace/copy_csv_xls_pdf.swf",
			        "buttons": [
			            "copy",
			            "csv",
			            "xls",
						"pdf",
			            "print"
			        ]
			    } );
			    $( tableTools.fnContainer() ).insertBefore('#sample-table-2');
				*/
				
				
				//oTable1.fnAdjustColumnSizing();
			
			
				$(document).on('click', 'th input:checkbox' , function(){
					var that = this;
					$(this).closest('table').find('tr > td:first-child input:checkbox')
					.each(function(){
						this.checked = that.checked;
						$(this).closest('tr').toggleClass('selected');
					});
				});
			
			
				$('[data-rel="tooltip"]').tooltip({placement: tooltip_placement});
				function tooltip_placement(context, source) {
					var $source = $(source);
					var $parent = $source.closest('table')
					var off1 = $parent.offset();
					var w1 = $parent.width();
			
					var off2 = $source.offset();
					//var w2 = $source.width();
			
					if( parseInt(off2.left) < parseInt(off1.left) + parseInt(w1 / 2) ) return 'right';
					return 'left';
				}
				
				/**
				$('#myTab a[data-toggle="tab"]').on('shown.bs.tab', function (e) {
				  //console.log(e.target.getAttribute("href"));
				})
					
				$('#accordion').on('shown.bs.collapse', function (e) {
					//console.log($(e.target).is('#collapseTwo'))
				});
				*/
				
				$('#myTab a[data-toggle="tab"]').on('shown.bs.tab', function (e) {
					if($(e.target).attr('href') == "#home") drawChartNow();
				})
			
				
				/**
					//go to next tab, without user clicking
					$('#myTab > .active').next().find('> a').trigger('click');
				*/
			
			
				$('#accordion-style').on('click', function(ev){
					var target = $('input', ev.target);
					var which = parseInt(target.val());
					if(which == 2) $('#accordion').addClass('accordion-style2');
					 else $('#accordion').removeClass('accordion-style2');
				});
				
				//$('[href="#collapseTwo"]').trigger('click');
			
			
				var oldie = /msie\s*(8|7|6)/.test(navigator.userAgent.toLowerCase());
				$('.easy-pie-chart.percentage').each(function(){
					$(this).easyPieChart({
						barColor: $(this).data('color'),
						trackColor: '#EEEEEE',
						scaleColor: false,
						lineCap: 'butt',
						lineWidth: 8,
						animate: oldie ? false : 1000,
						size:75
					}).css('color', $(this).data('color'));
				});
			
				$('[data-rel=tooltip]').tooltip();
				$('[data-rel=popover]').popover({html:true});
			
			
				$('#gritter-regular').on(ace.click_event, function(){
					$.gritter.add({
						title: '这是一个常规的提示框!',
						text: '内容内容内容内容内容 <a href="#" class="blue">链接</a> 内容内容内容内容内容内容内容内容内容内容内容内容内容内容',
						image: $path_assets+'/avatars/avatar1.png',
						sticky: false,
						time: '',
						class_name: (!$('#gritter-light').get(0).checked ? 'gritter-light' : '')
					});
			
					return false;
				});
			
				$('#gritter-sticky').on(ace.click_event, function(){
					var unique_id = $.gritter.add({
						title: '这是一个棘手的提示!',
						text: '内容内容内容内容内容 <a href="#" class="red">链接</a> 内容内容内容内容内容',
						image: $path_assets+'/avatars/avatar.png',
						sticky: true,
						time: '',
						class_name: 'gritter-info' + (!$('#gritter-light').get(0).checked ? ' gritter-light' : '')
					});
			
					return false;
				});
			
			
				$('#gritter-without-image').on(ace.click_event, function(){
					$.gritter.add({
						// (string | mandatory) the heading of the notification
						title: '这是一个没有图片的提示框!',
						// (string | mandatory) the text inside the notification
						text: '内容内容内容内容内容 <a href="#" class="orange">链接</a> 内容内容内容内容内容内容 <a href="#" class="orange">链接</a> 内容内容内容内容内容内容内容内容内容内容内容内容内容内容',
						class_name: 'gritter-success' + (!$('#gritter-light').get(0).checked ? ' gritter-light' : '')
					});
			
					return false;
				});
			
			
				$('#gritter-max3').on(ace.click_event, function(){
					$.gritter.add({
						title: '这是提示框3!',
						text: '内容内容内容内容内容内容内容内容内容内容 <a href="#" class="green">链接</a> 内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容',
						image: $path_assets+'/avatars/avatar3.png',
						sticky: false,
						before_open: function(){
							if($('.gritter-item-wrapper').length >= 3)
							{
								return false;
							}
						},
						class_name: 'gritter-warning' + (!$('#gritter-light').get(0).checked ? ' gritter-light' : '')
					});
			
					return false;
				});
			
			
				$('#gritter-center').on(ace.click_event, function(){
					$.gritter.add({
						title: '这是一个居中的提示框',
						text: '内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容',
						class_name: 'gritter-info gritter-center' + (!$('#gritter-light').get(0).checked ? ' gritter-light' : '')
					});
			
					return false;
				});
				
				$('#gritter-error').on(ace.click_event, function(){
					$.gritter.add({
						title: '这是一个错误提示！',
						text: '内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容',
						class_name: 'gritter-error' + (!$('#gritter-light').get(0).checked ? ' gritter-light' : '')
					});
			
					return false;
				});
					
			
				$("#gritter-remove").on(ace.click_event, function(){
					$.gritter.removeAll();
					return false;
				});
					
			
				///////
			
			
				$("#bootbox-regular").on(ace.click_event, function() {
					bootbox.prompt("你的名字是什么?", function(result) {
						if (result === null) {
							
						} else {
							
						}
					});
				});
					
				$("#bootbox-confirm").on(ace.click_event, function() {
					bootbox.confirm("你确定吗?", function(result) {
						if(result) {
							//
						}
					});
				});
				
			/**
				$("#bootbox-confirm").on(ace.click_event, function() {
					bootbox.confirm({
						message: "Are you sure?",
						buttons: {
						  confirm: {
							 label: "确定",
							 className: "btn-primary btn-sm",
						  },
						  cancel: {
							 label: "取消",
							 className: "btn-sm",
						  }
						},
						callback: function(result) {
							if(result) alert(1)
						}
					  }
					);
				});
			**/
					
				$("#bootbox-options").on(ace.click_event, function() {
					bootbox.dialog({
						message: "<span class='bigger-110'>我是一个较小的自定义对话框按钮</span>",
						buttons: 			
						{
							"success" :
							 {
								"label" : "<i class='ace-icon fa fa-check'></i> 成功!",
								"className" : "btn-sm btn-success",
								"callback": function() {
									//Example.show("great success");
								}
							},
							"danger" :
							{
								"label" : "危险!",
								"className" : "btn-sm btn-danger",
								"callback": function() {
									//Example.show("uh oh, look out!");
								}
							}, 
							"click" :
							{
								"label" : "点我!",
								"className" : "btn-sm btn-primary",
								"callback": function() {
									//Example.show("Primary button");
								}
							}, 
							"button" :
							{
								"label" : "只是一个按钮...",
								"className" : "btn-sm"
							}
						}
					});
				});
			
			
			
				$('#spinner-opts small').css({display:'inline-block', width:'60px'})
			
				var slide_styles = ['', 'green','red','purple','orange', 'dark'];
				var ii = 0;
				$("#spinner-opts input[type=text]").each(function() {
					var $this = $(this);
					$this.hide().after('<span />');
					$this.next().addClass('ui-slider-small').
					addClass("inline ui-slider-"+slide_styles[ii++ % slide_styles.length]).
					css('width','125px').slider({
						value:parseInt($this.val()),
						range: "min",
						animate:true,
						min: parseInt($this.attr('data-min')),
						max: parseInt($this.attr('data-max')),
						step: parseFloat($this.attr('data-step')) || 1,
						slide: function( event, ui ) {
							$this.val(ui.value);
							spinner_update();
						}
					});
				});
			
			
			
				//CSS3 spinner
				$.fn.spin = function(opts) {
					this.each(function() {
					  var $this = $(this),
						  data = $this.data();
			
					  if (data.spinner) {
						data.spinner.stop();
						delete data.spinner;
					  }
					  if (opts !== false) {
						data.spinner = new Spinner($.extend({color: $this.css('color')}, opts)).spin(this);
					  }
					});
					return this;
				};
			
				function spinner_update() {
					var opts = {};
					$('#spinner-opts input[type=text]').each(function() {
						opts[this.name] = parseFloat(this.value);
					});
					opts['left'] = 'auto';
					$('#spinner-preview').spin(opts);
				}
			
			
			
				$('#id-pills-stacked').removeAttr('checked').on('click', function(){
					$('.nav-pills').toggleClass('nav-stacked');
				});
			
				
				
				
				
				
				///////////
				$(document).one('ajaxloadstart.page', function(e) {
					$.gritter.removeAll();
					$('.modal').modal('hide');
				});


			})
		</script>

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

	function changeChargingPoint() {
		var productId = $("#productId").val();
		$.ajax({
			data : {
				parentId : productId,
				dataSourceCode : 'POINT'
			},
			url : "${ctx}/business/dataSource/changeSonLong",
			type : 'post',
			cache : false,
			async : true,
			dataType : 'html',
			success : function(data) {
				$('#chargingPointId').show().html(data);
			}
		});

	}
	function toPage(page) {
		if (page == 0) {
			page = $("#toPageNum").val();
		}
		$("#page").val(page);
		document.listForm.submit();
	}
</script>
</html>
