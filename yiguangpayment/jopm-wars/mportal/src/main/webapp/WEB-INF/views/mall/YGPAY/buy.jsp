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

<title>翼光网络科技-商品列表</title>
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

<body>

	<div class="bj">
		<div class="bk">
			<div class="logo">
				<strong><a href="#"><img
						src="${ctx}/template/default/img/logo.jpg" /></a></strong> <span> <img
					border="0" SRC="${ctx}/template/default/img/TEL.jpg"> 
				</span>
			</div>
			<div class="lc">
				<img src="${ctx}/template/default/img/1.jpg" />
			</div>

			<div class="ne">
				<div class="dh">
					<ul>
						<li><a class="dx" >电信手机短信支付</a></li>
					</ul>
				</div>

				<div class="zuo">
					<form action="${ctx}/mall/YGPAY/placeOrder" method="post"
						name="form1">
						<input style="height: 30px; width: 375px;" 
							name="channelId" id="channelId" value="3" type="hidden" />
						
						<table width="582" height="236" border="0">
							<tr>
								<td width="300" align="right">商户： <br /></td>
								<td width="537"><span id="merchantList">
									<select name="merchantId" id="merchantId" onchange="changePoint()" class="form-control fl"
										style="height: 30px; width: 375px;">
										<c:choose>
											<c:when test="${fn:length(merchantList) > 0}">
												<c:forEach items="${merchantList}" var="option">
													<option value="${option.value}">${option.text}</option>
												</c:forEach>
											</c:when>
											<c:otherwise>
												<option value=""></option>
											</c:otherwise>
										</c:choose>
									</select>
									</span>
								</td>
							</tr>
							<tr>
								<td width="300" align="right">产品： <br /></td>
								<td width="537"><span id="pointList">
									<select name="pointId" id="pointId" class="form-control fl"
										style="height: 30px; width: 375px;">
										<c:choose>
											<c:when test="${fn:length(pointList) > 0}">
												<c:forEach items="${pointList}" var="option">
													<option value="${option.value}">${option.text}</option>
												</c:forEach>
											</c:when>
											<c:otherwise>
												<option value=""></option>
											</c:otherwise>
										</c:choose>
									</select>
									</span>
								</td>
							</tr>
							<tr>
								<td align="right">手机号码：</td>
								<td><input style="height: 30px; width: 375px;"
									name="mobile" id="mobile" type="text" /><br /> <span
									id="mobile_msg" style="color: red"></span></td>
							</tr>
							<tr>
								<td>&nbsp;</td>
								<td><img src="${ctx}/template/default/img/AN.jpg"
									onclick="dosubmit()" /></td>
							</tr>
						</table>
					</form>
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
										<a href="${ctx}/mall/YGPAY/queryOrder?order_id=${order_id}">查看订单状态</a>
									</div>
								</td>
							</tr>
						</table>
					</div>
				</div>
				

			</div>
		</div>
	</div>


	<div class="wei">
		<p>
			<a href="#">公司概况 </a> &nbsp;|&nbsp; <a href="#">联系我们 </a>
			&nbsp;|&nbsp; <a href="#">合作伙伴</a> &nbsp;|&nbsp; <a href="#">诚聘英才</a><br />
			广州翼光网络科技有限公司 版权所有 2012-2014 粤ICP备14050898号-1
		</p>
	</div>
			
</body>
<script type="text/javascript">
function changePoint(){
	var merchantId = $("#merchantId").val();
	$.ajax({
		data:{
			merchantId:merchantId
		 },
	url : "${ctx}/mall/YGPAY//changePoint",
	type:'post',
	cache:false,
	async:true,
	dataType:'html',
	success : function(data) {  
		$('#pointId').show().html(data);
	}
	});
}
 
function dosubmit()
{
	var mobile = $("#mobile").val();
	if(mobile=="")
	{
	    $("#mobile_msg").html("请输入手机号码");
	    return;
	}
	else
	{
		$.post('${ctx}/mall/YGPAY/checkMobile',{mobile:mobile,channelId:$('#channelId').val()},function(response){
			if (response == 0)
			{
				$("#mobile_msg").html('');
				document.form1.submit();
			}
			else
			{
				$("#mobile_msg").html(response);
			}
		});
	}
}
</script>
</html>
