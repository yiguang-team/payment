<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">

<title>接口测试页面</title>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<script type="text/javascript"
	src="${ctx}/template/jquery-easyui-1.4.1/jquery.min.js"></script>
<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->

</head>

<body>
	<form id="smsTestForm" name="smsTestForm" method="post" action="">

		<table>
			<tr>
				<td>商户：</td>
				<td><select name="feecpid" id="feecpid"
					onchange="changefeecpid()" style="width: 150px; float: left;">
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
				</select></td>
			</tr>
			<tr>
				<td>产品：</td>
				<td><select name="feeproductid" id="feeproductid"
					onchange="changefeeproductid()" style="width: 150px; float: left;">
				</select></td>
			</tr>
			<tr>
				<td>计费点：</td>
				<td><select name="feepointid" id="feepointid" onchange=""
					style="width: 150px; float: left;">
				</select></td>
			</tr>
			<tr>
				<td><input type="button" value="获取短信" onclick="showSms()" /></td>
				<td><input id="sms" type="text" value=""
					style="width: 1000px; float: left;" /></td>
			</tr>
		</table>


	</form>

	<form id="orderForm" name="orderForm" method="post"
		action="${ctx}/test/testOrder">

		<table>
			<tr>
				<td>商户：</td>
				<td><select name="orderappid" id="orderappid"
					onchange="changeorderappid()" style="width: 150px; float: left;">
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
				</select></td>
			</tr>
			<tr>
				<td>产品：</td>
				<td><select name="orderproductid" id="orderproductid"
					onchange="changeorderproductid()"
					style="width: 150px; float: left;">
				</select></td>
			</tr>
			<tr>
				<td>计费点：</td>
				<td><select name="orderpointid" id="orderpointid" onchange=""
					style="width: 150px; float: left;">
				</select></td>
			</tr>
			<tr>
				<td>结果：</td>
				<td><select name="hRet" id="hRet"
					style="width: 150px; float: left;">
						<option value="0">成功</option>
						<option value="1">失败</option>
				</select></td>
			</tr>
			<tr>
				<td>手机号码：</td>
				<td><input type="text" name="phonenum" id="phonenum"
					style="width: 150px; float: left;" /></td>
			</tr>
			<tr>
				<td><input type="submit" value="发送短代通知" /></td>
				<td></td>
			</tr>
		</table>


	</form>

	<form id="orderForm" name="orderForm" method="post"
		action="${ctx}/test/testSmsCallBack">

		<table>
			<tr>
				<td>结果：</td>
				<td><select name="resultcode" id="resultcode"
					style="width: 150px; float: left;">
						<option value="0000">成功</option>
						<option value="9999">失败</option>
				</select></td>
			</tr>
			<tr>
				<td>订单号：</td>
				<td><input type="text" name="orderid" id="orderid"
					style="width: 300px; float: left;" /></td>
			</tr>
			<tr>
				<td><input type="submit" value="发送短验通知" /></td>
				<td></td>
			</tr>
		</table>


	</form>
</body>
<script type="text/javascript">
function changefeecpid(){

	var feecpid = $("#feecpid").val();
	$.ajax({
		data:{
			parentId:feecpid,
			dataSourceCode:'PRODUCT'
		 },
	url : "${ctx}/business/dataSource/changeSonLong",
	type:'post',
	cache:false,
	async:true,
	dataType:'html',
	success : function(data) {  
		$('#feeproductid').show().html(data);
	}
	});
}	

function changefeeproductid(){
	var feeproductid = $("#feeproductid").val();
	$.ajax({
		data:{
			parentId:feeproductid,
			dataSourceCode:'POINT'
		 },
	url : "${ctx}/business/dataSource/changeSonLong",
	type:'post',
	cache:false,
	async:true,
	dataType:'html',
	success : function(data) {  
		$('#feepointid').show().html(data);
	}
	});
}	

function showSms(){
	var feecpid = $("#feecpid").val();
	var feepointid = $("#feepointid").val();
	$.ajax({
		data:{
			feecpid:feecpid,
			feepointid:feepointid
		 },
	url : "${ctx}/test/testFee",
	type:'get',
	cache:false,
	async:true,
	dataType:'text',
	success : function(data) {  
		$('#sms').val(data);
	}
	});
}





function changeorderappid(){
	var orderappid = $("#orderappid").val();
	$.ajax({
		data:{
			parentId:orderappid,
			dataSourceCode:'PRODUCT'
		 },
	url : "${ctx}/business/dataSource/changeSonLong",
	type:'post',
	cache:false,
	async:true,
	dataType:'html',
	success : function(data) {  
		$('#orderproductid').show().html(data);
	}
	});
}	
function changeorderproductid(){
	var orderproductid = $("#orderproductid").val();
	$.ajax({
		data:{
			parentId:orderproductid,
			dataSourceCode:'POINT'
		 },
	url : "${ctx}/business/dataSource/changeSonLong",
	type:'post',
	cache:false,
	async:true,
	dataType:'html',
	success : function(data) {  
		$('#orderpointid').show().html(data);
	}
	});
}	



</script>
</html>
