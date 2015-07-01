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

<title>翼光网络科技-支付中</title>
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
<meta name="viewport"
	content="width=device-width, initial-scale=1.0, maximum-scale=1.0" />
<META http-equiv="content-type" content="text/html; charset=utf-8">
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">

</head>

<body>
	正在跳转到翼支付收银台，请勿进行其他操作。
	<form action="${sms_pay_url}" name="form1" method="POST">
		<input type="hidden" name="MERCHANTID" value="${MERCHANTID}"><br>
		<input type="hidden" name="SUBMERCHANTID" value="${SUBMERCHANTID}"><br>
		<input type="hidden" name="ORDERSEQ" value="${ORDERSEQ}"><br>
		<input type="hidden" name="ORDERREQTRANSEQ" value="${ORDERREQTRANSEQ}"><br>
		<input type="hidden" name="ORDERDATE" value="${ORDERDATE}"><br>
		<input type="hidden" name="ORDERAMOUNT" value="${ORDERAMOUNT}"><br>
		<input type="hidden" name="PRODUCTAMOUNT" value="${PRODUCTAMOUNT}"><br>
		<input type="hidden" name="ATTACHAMOUNT" value="${ATTACHAMOUNT}"><br>
		<input type="hidden" name="CURTYPE" value="${CURTYPE}"><br>
		<input type="hidden" name="ENCODETYPE" value="${ENCODETYPE}"><br>
		<input type="hidden" name="MERCHANTURL" value="${MERCHANTURL}"><br>
		<input type="hidden" name="BACKMERCHANTURL" value="${BACKMERCHANTURL}"><br>
		<input type="hidden" name="BANKID" value="${BANKID}"><br>
		<input type="hidden" name="ATTACH" value="${ATTACH}"><br>
		<input type="hidden" name="PRODUCTID" value="${PRODUCTID}"><br>
		<input type="hidden" name="BUSICODE" value="${BUSICODE}"><br>
		<input type="hidden" name="TMNUM" value="${TMNUM}"><br> <input
			type="hidden" name="CUSTOMERID" value="${CUSTOMERID}"><br>
		<input type="hidden" name="PRODUCTDESC" value="${PRODUCTDESC}"><br>
		<input type="hidden" name="MAC" value="${MAC}"><br> <input
			type="hidden" name="DIVDETAILS" value="${DIVDETAILS}"><br>
		<input type="hidden" name="PEDCNT" value="${PEDCNT}"><br>
		<input type="hidden" name="GMTOVERTIME" value="${GMTOVERTIME}"><br>
		<input type="hidden" name="GOODPAYTYPE" value="${GOODPAYTYPE}"><br>
		<input type="hidden" name="GOODSCODE" value="${GOODSCODE}"><br>
		<input type="hidden" name="GOODSNAME" value="${GOODSNAME}"><br>
		<input type="hidden" name="GOODSNUM" value="${GOODSNUM}"><br>
		<input type="hidden" name="CLIENTIP" value="${CLIENTIP}"><br>
		<!--<input type="submit" name="sbtn" id="sbtn"/>-->
	</form>
</body>
<script type="text/javascript">

	document.form1.submit();

</script>
</html>
