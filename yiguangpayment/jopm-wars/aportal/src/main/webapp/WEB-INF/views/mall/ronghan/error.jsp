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

<title>翼光网络科技-系统错误</title>
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
<meta name="viewport"
	content="width=device-width, initial-scale=1.0, maximum-scale=1.0" />
<META http-equiv="content-type" content="text/html; charset=utf-8">
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">

<c:set var="ctx" value="${pageContext.request.contextPath}" />
<link href="${ctx}/template/default/css/1.css" type="text/css"
	rel="stylesheet">
<script src="${ctx}/template/default/js/jquery-1.9.0.js"></script>
<script src="${ctx}/template/default/layer/layer.min.js"></script>
</head>
<style type="text/css">
.btns {
	width: 380px;
	height: 51px;
	font: 18px/40px Arial, Microsoft YaHei !important;
	float: left;
	color: #fff;
	background: url(${ctx}/template/default/img/btns.gif) no-repeat;
	border: none;
	cursor: pointer;
	margin-top: 15px;
}

.form-btn {
	display: inline-block;
	border: 0px none;
	background: none repeat scroll 0% 0% #FE8353;
	color: #FFF;
	text-align: center;
	height: 30px;
	padding: 0px;
	cursor: pointer;
	vertical-align: middle;
	font: 18px/30px Arial, Microsoft YaHei !important;
	margin-top: -12px
}

.disabled {
	background: #ccc;
	color: #999;
	text-shadow: 1px 1px 0 #fff;
	cursor: default;
}

.tclayer {
	display: none;
}

.tclayer .tc-c {
	text-align: center;
	margin: 0 auto;
	z-index: 999;
	position: absolute;
	top: 25%;
	width: 1000px;
	text-align: center;
	left: 50%;
	margin-left: -500px;
}

.tclayer .tc-zhezhao {
	width: 100%;
	height: 100%;
	position: absolute;
	top: 0;
	left: 0;
	z-index: 998;
	background-color: #000;
	filter: alpha(opacity = 50);
	-moz-opacity: 0.5;
	opacity: 0.5;
}

.tclayer .loading-c {
	background-color: #fff;
	border: 5px solid #dedede;
	-moz-border-radius: 15px;
	-webkit-border-radius: 15px;
	border-radius: 15px;
	padding: 20px 30px;
	width: 300px;
	display: inline-block;
	margin-top: 100px;
}

.tclayer .loading-c img {
	float: left;
}

.tclayer .loading-c span {
	font: 18px/36px Microsoft YaHei;
	color: #333;
	margin-top: -10px;
}

.display {
	display: block;
}
</style>

<body>

	<div class="bj">
		<div class="bk">
			<div class="logo">
				<strong><a href="#"><img
						src="${ctx}/template/default/img/logo.jpg" /></a></strong> <span> <img
					border="0" SRC="${ctx}/template/default/img/TEL.jpg"> <a
					target=blank
					href=http://wpa.qq.com/msgrd?V=1&uin=3157299628&Site=短信支付&Menu=yes><img
						border="0" SRC="${ctx}/template/default/img/QQ1.jpg"
						alt="点击这里给我发消息"></a> <a target=blank
					href=http://wpa.qq.com/msgrd?V=1&uin=1767508243&Site=短信支付&Menu=yes><img
						border="0" SRC="${ctx}/template/default/img/QQ2.jpg"
						alt="点击这里给我发消息"></a>
				</span>
			</div>
			<div class="lc">
				<img src="${ctx}/template/default/img/1.jpg" />
			</div>

			<div class="ne">
				<div class="zuo">
					<form action="#" method="post" name="form1">
						<table width="582" height="236" border="0">
							<tr>
								<td width="300" align="right" style="padding-bottom: 0;">错误代码：
									<br />
								</td>
								<td width="537" style="padding-bottom: 0;">${errorcode}</td>
							</tr>
							<tr>
								<td width="300" align="right" style="padding-bottom: 0;">错误消息：
									<br />
								</td>
								<td width="537" style="padding-bottom: 0;">${errormsg}</td>
							</tr>
						</table>
					</form>
				</div>

				<div class="you">
					<p>
						<b>手机验证码支付说明：</b>
					</p>
					<p>
						验证码支付：即通过确认手机收到的验证码完成支付扣费；<br />
						支持中国移动号段：134~139、147、150~152、157~159、182、183、184、187、188；<br />
						支持中国联通号段： 130-132、145、155、156、185、186；<br /> 支持中国电信号段：
						133、153、180、181、189；<br /> 在支付过程中，如您选择的支付通道受限限制，请更换其它通道进行支付。<br />
						请按页面和验证码提示完成操作。
					</p>
					<p>
						在线客服1：<a target=blank
							href=http://wpa.qq.com/msgrd?V=1&uin=3157299628&Site=短信支付&Menu=yes><img
							border="0" SRC=http://wpa.qq.com/pa?p=1:3157299628:1
							alt="点击这里给我发消息"></a>
					</p>
					<p>
						在线客服2：<a target=blank
							href=http://wpa.qq.com/msgrd?V=1&uin=1767508243&Site=短信支付&Menu=yes><img
							border="0" SRC=http://wpa.qq.com/pa?p=1:1767508243:1
							alt="点击这里给我发消息"></a>
					</p>
				</div>

				<div class="ww">
					<p>
						<b>操作必读</b>
					</p>
					<p>
						输入支付号码：如号码归属地&quot;未知&quot;，则此号码不能完成支付，请与客服联系；<br />
						选择支付方式：如选择的支付方式下无可用支付通道，请选择其它支付方式；<br />
						选择支付通道：通常会有一个或多个支付通道，请根据每个支付通道标称的支付信息（支付费用、支付次数等），选择合适的支付通道进行提交；<br />
						提交支付：系统会根据你提交的商品、支付号码、支付通道等信息处理支付请求，如您提交的支付请求受系统限制而不能支付，请选择其它支付方式或隔日重试；<br />
						提交支付支付请求成功后，请按页面提示完成支付确认操作；<br /> 其它任务疑问，请咨询客服热线：400-829-9109。
					</p>
				</div>
			</div>
		</div>
	</div>
	<div id="pop_div" style="display: none">
		<div class="xx">
			<a href="#"></a>
		</div>
		<div class="bg" style="background: #f3f3f3;">
			<table width="470" border="0">
				<tr>
					<td>
						<div class="jf" style="float: left; margin-left: 50px">
							<a href="/rest/buy/queryOrder?order_id=${order.orderId}">付款完成</a>
						</div>
						<div class="jf" style="float: left; margin-left: 20px">
							<a href="/rest/buy?productId=50006&chargingType=1">继续购买</a>
						</div>
					</td>
				</tr>
			</table>
		</div>
	</div>

	<div class="wei">
		<p>
			<a href="#">公司概况 </a> &nbsp;|&nbsp; <a href="#">联系我们 </a>
			&nbsp;|&nbsp; <a href="#">合作伙伴</a> &nbsp;|&nbsp; <a href="#">诚聘英才</a><br />
			广州翼光网络科技有限公司 版权所有 2012-2014 粤ICP备14050898号-1
		</p>
	</div>
	<div class="tclayer">
		<div class="tc-c">
			<div class="loading-c">
				<img src="${ctx}/template/default/img/loading.gif" width="36"
					height="36" /> <span class="txt">加载中，请稍候!</span>
			</div>
		</div>
		<div class="tc-zhezhao"></div>
	</div>
</body>
<script type="text/javascript">
</html>
