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

<title>翼光联运支付管理系统-商品列表</title>
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

			<div class="zi">
				商品信息：<span>${productName}</span><br /> 商品订单号：<span>${orderid}</span>
			</div>

			<div class="ne">
				<div class="zuo">
					<form action="#" method="post" name="form1">
						<div style="display: none">
							<input type="hidden" name="cpid" id="cpid" value="${cpid}"></input><br />
							<input type="hidden" name="orderid" id="orderid"
								value="${orderid}"></input><br /> <input type="hidden"
								name="username" id="username" value="${username}"></input><br />
							<input type="hidden" name="serviceid" id="serviceid"
								value="${serviceid}"></input><br /> <input type="hidden"
								name="operator" id="operator" value="${operator}"></input><br />
							<input type="hidden" name="datetime" id="datetime"
								value="${datetime}"></input><br /> <input type="hidden"
								name="smssign" id="smssign" value="${smssign}"></input><br /> <input
								type="hidden" name="paysign" id="paysign" value="${paysign}"></input><br />
							<input type="hidden" name="mobile" id="mobile" value="${mobile}"></input><br />
							<input type="hidden" name="showUrl" id="showUrl"
								value="${showUrl}"></input><br /> <input type="hidden"
								name="notifyUrl" id="notifyUrl" value="${notifyUrl}"></input><br />
							<input type="hidden" name="subject" id="subject"
								value="${subject}"></input><br /> <input type="hidden"
								name="description" id="description" value="${description}"></input><br />
							<input type="hidden" name="chargingType" id="chargingType"
								value="${chargingType}"></input><br />
						</div>
						<table width="582" height="236" border="0">
							<tr>
								<td width="300" align="right" style="padding-bottom: 0;">购买产品：
									<br />
								</td>
								<td width="537" style="padding-bottom: 0;">${pointName}</td>
							</tr>
							<tr>
								<td width="300" align="right" style="padding-bottom: 0;">支付金额：
									<br />
								</td>
								<td width="537" style="padding-bottom: 0;">${payAmount}</td>
							</tr>
							<c:if test="${chargingType == 1}">
								<tr>
									<td align="right" style="padding-bottom: 0;">骏卡一卡通账号：</td>
									<td style="padding-bottom: 0;">${username}</td>
								</tr>
							</c:if>
							<tr>
								<td align="right" style="padding-bottom: 0 !important;">手机号码：</td>
								<td style="padding-bottom: 0 !important;">${mobile}</td>
							</tr>
							<tr>
								<td align="right" style="padding-bottom: 0;">验证码：</td>
								<td style="padding-bottom: 0;"><input
									style="height: 30px; width: 100px;" name="smscode" id="smscode"
									type="text" maxlength="6" /> <span id="smscode_msg"
									style="color: red"></span> <input class="form-btn"
									id="btnSendCode" type="button" value="发送验证码"
									onclick="getSmsCode()" /></td>
							</tr>
							<tr>
								<td>&nbsp;</td>
								<td><input name="pay" value="确认支付" id="pay"
									onclick="smspay()" type="button" class="btns" /></td>

							</tr>
						</table>
					</form>
				</div>

				<div class="you">
					<p>
						<b>温馨提示：</b>
					</p>
					<p>
						1、欢迎使用中国联通提供的话费支付功能，话费支付仅限于联通号码。单个联通手机号码每日限充值 20.00元，月限充值 50.00元。<br />
						2、点击获取验证码，您将收到验证码短信，您可以在本页面输入验证码充值。<br />
						3、如您在使用过程中碰到任何问题，请拨打客服电话073188920955进行咨询。
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
					<td class="sj">订单已经提交支付请求，等待渠道商进行扣款</td>
				</tr>
				<tr>
					<td>
						<div class="jf" style="float: left; margin-left: 50px">
							<a
								href="${ctx}/mall/junwang/queryOrder?orderid=${orderid}&chargingType=${chargingType}&sign=${querySign}">查看订单</a>
						</div>
						<div id="second">秒后自动跳转</div>
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
$(function(){

    $('.click1').click(function(){
        $('.bg').fadeIn(200);
        $('.content').fadeIn(400);
	});

	$('.bg').click(function(){
	    $('.bg').fadeOut(800);
	    $('.content').fadeOut(800);
	});
	
	getSmsCode();
});
function smspay()
{	
	var smscode = $("#smscode").val();
	if(smscode== ""){
		$("#smscode_msg").html("请输入验证码");
		return false;
	}
	$('.tclayer').addClass("display");
	$.post('${ctx}/payment/api/1.0/smscharge',
			{
		datetime:$("#datetime").val(),
		cpid:$("#cpid").val(),
		serviceid:$("#serviceid").val(),
		mobile:$("#mobile").val(),
		operator:$("#operator").val(),
		username:$("#username").val(),
		orderid:$("#orderid").val(),
		sign:$("#paysign").val(),
		description:$("#description").val(),
		subject:$("#subject").val(),
		showUrl:$("#showUrl").val(),
		notifyUrl:$("#notifyUrl").val(),
		smscode:smscode},function(response){
			var jsonObject = eval("(" + response + ")");
			if (jsonObject.code == '0000')
			{
				var pageii = $.layer({
					type: 1,
					title: false,
					area: ['auto', 'auto'],
					border: [0], //去掉默认边框
					shade: [0], //去掉遮罩
					closeBtn: [0, false], //去掉默认关闭按钮
					shift: 'left', //从左动画弹出
					page: {
						html: $("#pop_div").html()
					}
				});
				statusCountFunction();
			}
			else
			{
				alert(jsonObject.code + ':' + jsonObject.message);
			}
			
	});
}
function getSmsCode(){

	$('.tclayer').addClass("display");
	
	$.post('${ctx}/payment/api/1.0/sendsmscode',
			{
		datetime:$("#datetime").val(),
		cpid:$("#cpid").val(),
		orderid:$("#orderid").val(),
		serviceid:$("#serviceid").val(),
		mobile:$("#mobile").val(),
		username:$("#username").val(),
		description:$("#description").val(),
		subject:$("#subject").val(),
		operator:$("#operator").val(),
		sign:$("#smssign").val()},function(response){
		var jsonObject = eval("(" + response + ")");
		if (jsonObject.code == '0000')
		{
			sendMessage();
		}
		else
		{
			alert(jsonObject.code + ':' + jsonObject.message);
		}
		
		$(".tclayer").removeClass("display");	
	});
}

/*-------------------------------------------*/
var InterValObj; //timer变量，控制时间
var count = 60; //间隔函数，1秒执行
var curCount;//当前剩余秒数

function sendMessage() {
            curCount = count;
            
            //设置button效果，开始计时
            $("#btnSendCode").attr("disabled", "true");
            $("#btnSendCode").val("重新发送（" + curCount + "）");
			$("#btnSendCode").addClass("disabled");
            InterValObj = window.setInterval(SetRemainTime, 1000); //启动计时器，1秒执行一次
            }
        //timer处理函数
function SetRemainTime() {
            if (curCount == 0) {                
                window.clearInterval(InterValObj);//停止计时器
                $("#btnSendCode").removeAttr("disabled");//启用按钮
                $("#btnSendCode").val("重新发送");
				$("#btnSendCode").removeClass("disabled");
                code = ""; //清除验证码。如果不清除，过时间后，输入收到的验证码依然有效    
            }
            else {
                curCount--;
                $("#btnSendCode").val("重新发送（" + curCount + "）");
            }
        }
		
		/*-------------------------------------------*/
var statusInterValObj; //timer变量，控制时间
var statusCount = 5; //间隔函数，1秒执行
var statusCurCount;//当前剩余秒数

function statusCountFunction() {
            statusCurCount = statusCount;

            $("#second").val(statusCurCount);
            InterValObj = window.setInterval(SetRemainStatus, 1000); //启动计时器，1秒执行一次
            }
        //timer处理函数
function SetRemainStatus() {
            if (statusCurCount == 0) {                
				window.location.href="${ctx}/mall/junwang/queryOrder?orderid=${orderid}&chargingType=${chargingType}&sign=${querySign}";
            }
            else {
                curCount--;
                 $("#second").val(statusCurCount);
            }
        }
</script>
</html>
