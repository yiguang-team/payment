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

<title>翼光联运支付管理系统</title>
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

<!--[if IE]>
<script>
(function(){if(!/*@cc_on!@*/0)return;var e = "abbr,article,aside,audio,canvas,datalist,details,dialog,eventsource,figure,footer,header,hgroup,mark,menu,meter,nav,output,progress,section,time,video".split(','),i=e.length;while(i--){document.createElement(e[i])}})()
</script>
<![endif]-->

</head>


<body class="no-skin">
	<div region="north">
		<!--------------------顶部导航 -------------------->
		<!-- #section:basics/navbar.layout -->
		<div id="navbar" class="navbar navbar-default">
			<script type="text/javascript">
				try{ace.settings.check('navbar' , 'fixed')}catch(e){}
			</script>
			<div class="navbar-container" id="navbar-container">
				<!-- 切换栏 -->
				<button type="button" class="navbar-toggle menu-toggler pull-left"
					id="menu-toggler" data-target="#sidebar">
					<span class="sr-only">切换栏</span> <span class="icon-bar"></span> <span
						class="icon-bar"></span> <span class="icon-bar"></span>
				</button>

				<!-- 切换栏 end -->
				<div class="navbar-header pull-left">
					<!-- 名称 -->
					<a href="index.html" class="navbar-brand"> <small>
							翼光联运支付管理系统 </small>
					</a>
					<!-- 名称 end -->
				</div>

				<!-- 下拉菜单 -->
				<div class="navbar-buttons navbar-header pull-right"
					role="navigation">
					<ul class="nav ace-nav">


						<!-- 用户菜单 -->
						<li class="blue"><a data-toggle="dropdown" href="#"
							class="dropdown-toggle"> <img class="nav-user-photo"
								src="${ctx}/template/ace/assets/avatars/user.jpg"
								alt="Jason's Photo" /> <span class="user-info"> <small>欢迎您,</small>
									${username}
							</span> <i class="ace-icon fa fa-caret-down"></i>
						</a>
							<ul
								class="user-menu dropdown-menu-right dropdown-menu dropdown-yellow dropdown-caret dropdown-close">
								<li><a onclick="return false;"
									href="${ctx}/identity/user/toModifyPassword?id=${userid}"> <i
										class="ace-icon fa fa-cog"></i> 修改密码
								</a></li>
								<li class="divider"></li>
								<li><a href="${ctx}/login/loginout"> <i
										class="ace-icon fa fa-power-off"></i> 退出
								</a></li>
							</ul></li>

						<!-- 用户菜单 end -->
					</ul>
				</div>

				<!-- 下拉菜单 end -->
			</div>
			<!-- /.navbar-container -->
		</div>
	</div>
	<!-- /section:basics/navbar.layout -->
	<!--------------------顶部导航 end-------------------->

	<!--------------------左导航 -------------------->
	<!-- #section:basics/sidebar -->
	<div id="sidebar" class="sidebar responsive">
		<script type="text/javascript">
					try{ace.settings.check('sidebar' , 'fixed')}catch(e){}
				</script>
		<div class="sidebar-shortcuts" id="sidebar-shortcuts">
			<h5>导航菜单</h5>
			<div class="sidebar-shortcuts-large" id="sidebar-shortcuts-large">

			</div>
			<div class="sidebar-shortcuts-mini" id="sidebar-shortcuts-mini">
				<span class="btn btn-success"></span> <span class="btn btn-info"></span>
				<span class="btn btn-warning"></span> <span class="btn btn-danger"></span>
			</div>
		</div>
		<!-- /.sidebar-shortcuts -->

		<ul class="nav nav-list">
			<c:forEach items="${menulist1}" var="menu1">
				<li class=""><a href="#" class="dropdown-toggle"> <i
						class="menu-icon fa fa-desktop"></i> <span class="menu-text">${menu1.menuName}</span>
						<b class="arrow fa fa-angle-down"></b>
				</a> <b class="arrow"></b>
					<ul class="submenu">
						<c:forEach items="${menulist2}" var="menu2">
							<c:if
								test="${menu2.parentId == menu1.id}">
								<li class=""><a onclick="return false;"
									href="${ctx}/${menu2.url}"> <i
										class="menu-icon fa fa-caret-right"></i>${menu2.menuName}</a>
									<b class="arrow"></b></li>
							</c:if>
						</c:forEach>
					</ul></li>
			</c:forEach>
		</ul>
		<!-- /.nav-list -->

		<!-- /section:basics/sidebar.layout.minimize -->
		<script type="text/javascript">
					try{ace.settings.check('sidebar' , 'collapsed')}catch(e){}
				</script>
	</div>

	<!-- /section:basics/sidebar -->

	<!--------------------左导航 end -------------------->


	<div region="center" class="main-container" id="main-container">
		<script type="text/javascript">
				try{ace.settings.check('main-container' , 'fixed')}catch(e){}
			</script>
		<!--------------------中间内容 -------------------->
		<div id="tabsMenu" class="easyui-menu"
			style="width: 120px; display: none">
			<div id="tabsMenu-tabclose">关闭当前</div>
			<div id="tabsMenu-tabcloseother">关闭其他</div>
			<div id="tabsMenu-tabcloseall">关闭所有</div>
		</div>
		<div id="mainPanle" region="center" style="overflow: hidden;">

			<div class="main-content">
				<div class="easyui-tabs" fit="false" border="false" id="tabs">
					<div class="main-content-inner" title="首页" id="mainTab">

						<!-- 面包屑 -->
						<div class="breadcrumbs" id="breadcrumbs">
							<script type="text/javascript">
							try{ace.settings.check('breadcrumbs' , 'fixed')}catch(e){}
						</script>
							<ul class="breadcrumb">
								<li class="active"><i class="ace-icon fa fa-home home-icon"></i>
									首页 <span class="noshow jt">></span></li>
							</ul>
							<!-- 面包屑 end -->


							<!-- /section:basics/content.searchbox -->
						</div>

						<!-- /section:basics/content.breadcrumbs -->
						<div class="page-content">

							<div class="row">
								<div class="col-xs-12">
									<!-- PAGE CONTENT BEGINS -->
									<div class="alert alert-block alert-success">
										<button type="button" class="close" data-dismiss="alert">
											<i class="ace-icon fa fa-times"></i>
										</button>
										<i class="ace-icon fa fa-check green"></i> 欢迎来到 <strong
											class="green"> 翼光联运支付管理系统 <small>(v1.0.0)</small>
										</strong>, 轻量级的、功能丰富的和易于使用的卡库管理系统。
									</div>
									<!-- /.row -->

									<!-- #section:custom/extra.hr -->
									<!-- /section:custom/extra.hr -->
									<!-- /.row -->
									<!-- /.row -->

									<!-- PAGE CONTENT ENDS -->
								</div>
								<!-- /.col -->
							</div>
							<!-- /.row -->
							<div class="clearfix"></div>
						</div>
						<!-- /.page-content -->

					</div>
				</div>

				<div class="footer">
					<div class="footer-inner">
						<!-- #section:basics/footer -->
						<div class="footer-content">
							<span class="bigger-120"> <span class="blue bolder">翼光联运支付管理系统</span>
								COPYRIGHT &copy; 2015-2017
							</span> &nbsp; &nbsp;
						</div>

						<!-- /section:basics/footer -->
					</div>
				</div>
			</div>

			<!-- /.main-content -->



		</div>

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

	<!--[if lte IE 8]>
		  <script src="${ctx}/template/ace/assets/js/excanvas.js"></script>
		<![endif]-->
	<script src="${ctx}/template/ace/assets/js/jquery-ui.custom.js"></script>
	<script src="${ctx}/template/ace/assets/js/jquery.ui.touch-punch.js"></script>
	<script src="${ctx}/template/ace/assets/js/jquery.easypiechart.js"></script>
	<script src="${ctx}/template/ace/assets/js/jquery.sparkline.js"></script>
	<script src="${ctx}/template/ace/assets/js/flot/jquery.flot.js"></script>
	<script src="${ctx}/template/ace/assets/js/flot/jquery.flot.pie.js"></script>
	<script src="${ctx}/template/ace/assets/js/flot/jquery.flot.resize.js"></script>

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
$(document).ready(function () {
	$('.submenu li a').click(function () {

        var tabTitle = $(this).text();
        var url = $(this).attr("href");

        addTab(tabTitle, url);

    });
	
	$('.blue li a').click(function () {

        var tabTitle = $(this).text();
        var url = $(this).attr("href");

        addTab(tabTitle, url);

    });
});
function addTab(subtitle, url) {
    if (!$('#tabs').tabs('exists', subtitle)) {
        $('#tabs').tabs('add', {
            title: subtitle,
            content: createFrame(url),
            closable: true,
            width: $('#mainPanle').width(),
            height: 1200
        });
    } else {
        $('#tabs').tabs('select', subtitle);
   }
    tabClose();
}


function createFrame(url) {
	var id = Math.random();
    var s = '<iframe id = "'+ id +'" name="mainFrame" scrolling="no" frameborder="0"  src="' + url + '" style="width:100%;height:100%" onload="resize('+id+')"></iframe>';
    return s;
}


function tabClose() {
    /*双击关闭TAB选项卡*/
    $(".tabs-inner").dblclick(function () {
        var subtitle = $(this).children("span").text();
        $('#tabs').tabs('close', subtitle);
    })

    $(".tabs-inner").bind('contextmenu', function (e) {
        $('#tabsMenu').menu('show', {
            left: e.pageX,
            top: e.pageY,
        });
        var subtitle = $(this).children("span").text();
        $('#tabsMenu').data("currtab", subtitle);
        return false;
    });
}
//实例化menu的onClick事件
	$("#tabsMenu").menu({
		onClick : function (item) {
			CloseTab(this, item.id);
		}
	});
//几个关闭事件的实现
function CloseTab(menu, type) {
	var curTabTitle = $(menu).data("currtab");
	var tabs = $("#tabs");
	
	if (type === "tabsMenu-tabclose") {
		tabs.tabs("close", curTabTitle);
		return;
	}
	
	var allTabs = tabs.tabs("tabs");
	var closeTabsTitle = [];
	
	$.each(allTabs, function () {
		var opt = $(this).panel("options");
		if (opt.closable && opt.title != curTabTitle && type === "tabsMenu-tabcloseother") {
			closeTabsTitle.push(opt.title);
		} else if (opt.closable && type === "tabsMenu-tabcloseall") {
			closeTabsTitle.push(opt.title);
		}
	});
	
	for (var i = 0; i < closeTabsTitle.length; i++) {
		tabs.tabs("close", closeTabsTitle[i]);
	}
}

function autoHeight(){
    var iframe = document.getElementById("mainFrame");
    if(iframe.Document){//ie自有属性
        iframe.style.height = iframe.Document.documentElement.scrollHeight;
    }else if(iframe.contentDocument){//ie,firefox,chrome,opera,safari
        iframe.height = iframe.contentDocument.body.offsetHeight ;
    }
}

function resize(id){ 
	
	var oEle = document.getElementById(id) ;
	 var nHeight = oEle.contentDocument.body.scrollHeight ;
	nHeight = nHeight + 10;
	oEle.style.height = nHeight + 'px' ;
} 
</script>
</html>
