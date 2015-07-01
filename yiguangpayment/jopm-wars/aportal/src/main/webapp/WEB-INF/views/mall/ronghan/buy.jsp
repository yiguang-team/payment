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
			<div class="ne">
				<div class="dh">
					<ul>
						<li><a class="lt">联通手机短信支付</a></li>
						<li class="cur"><a class="dx">电信手机短信支付</a></li>
						<li><a class="yd">移动手机短信支付</a></li>
					</ul>
				</div>

				<div class="zuo">
					<form action="${ctx}/mall/ronghan/placeOrder" method="post"
						name="form1">
						<input style="height: 30px; width: 375px;" name="orderid"
							id="orderid" value="${orderid}" type="hidden" /> 
						<input
							style="height: 30px; width: 375px;" name="channelId"
							id="channelId" value="3" type="hidden" /> <input
							style="height: 30px; width: 375px;" name="chargingType"
							id="chargingType" value="${chargingType}" type="hidden" />
						<!--<div style="color:red;margin-bottom:10px;">重要通知：因运营商成本增加，将于6月12日下午2点提高骏卡一卡通J点售价，30元1000J点，20元700J点，10元300J点，5元150J点，请各位消费者知悉~</div>-->
						<table width="582" height="236" border="0">
<c:if test="${'' == merchantId}">
							<tr>
								<td width="300" align="right">商户： <br /></td>
								<td width="537">
									<select name="cpid" id="cpid"
										onchange="changeMerchantList()" class="form-control fl"
										style="height: 30px; width: 375px;">
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
									</select>
								</td>
							</tr>
</c:if>
<c:if test="${'' != merchantId}">						
							<input	style="height: 30px; width: 375px;" name="cpid" id="cpid" value="${merchantId}" type="hidden" />
</c:if>						
							<tr>
								<td width="300" align="right">产品： <br /></td>
								<td width="537">
									<select name="productId" id="productId"
										onchange="changeProductList()" class="form-control fl"
										style="height: 30px; width: 375px;">
										
									</select>
								</td>
							</tr>

							<tr>
								<td width="300" align="right">计费点： <br /></td>
								<td width="537"><span id="pointerList"></span></td>
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
				
				<div class="you">
				    <button type="button" onclick="doviewOrder()" >查询消费记录</button>
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

</body>
<script type="text/javascript">
$(function () { 
	
	
	$('#channelId').val('3');
	
	changeMerchantList();
	showPointer('3');
	$(".zuo").find(':image').css("margin-top","10px");
	$('.dh').find('li').click(function () { 
		
		var optL=$(this).find('a').attr('class').split(' ');  
		if (inArray('lt',optL))
		{
			showPointer('2');
			$('#channelId').val('2');
		}
		else if (inArray('dx',optL))
		{
			showPointer('3');
			$('#channelId').val('3');
		}
		else if (inArray('yd',optL))
		{
			alert("暂不支持中国移动用户充值，业务即将开放~");
			return;
		}
		
		$('.dh').find('li').removeClass('cur'); 
		$(this).addClass('cur'); 
	});


}); 
function inArray(needle, array){
    var l = array.length;
    var i = 0;
    for(;i<l;i++){
        if(needle == array[i]){
            return true;
        }
    }
    return false;
}

function changeProductList()
{
	showPointer($('#channelId').val());
}
function changeMerchantList()
{
	showProduct($('#cpid').val());
	showPointer($('#channelId').val());
}
function showProduct(merchantId){

	$.ajax({
		data:{
			parentId:merchantId,
			dataSourceCode:'PRODUCT'
		 },
	url : "${ctx}/business/dataSource/changeSonLongWithoutPlease",
	type:'post',
	cache:false,
	async:false,
	dataType:'html',
	success : function(data) {  
		$('#productId').show().html(data);
	}
	});

	
}
function showPointer(channelId){
	var chargingType = $('#chargingType').val();
	var merchantId = $('#cpid').val();
	var productId = $('#productId').val();
	$.post('${ctx}/mall/ronghan/pointList',{channelId:channelId,chargingType:chargingType,merchantId:merchantId,productId:productId},function(response){
		$('#pointerList').show().html(response);
	});
}

function doviewOrder()
{
	location.href='${ctx}/merchantOperate/viewOrder';
}
function dosubmit()
{
	
	$("#username_msg").html('');
	var mobile = $("#mobile").val();
	if(mobile=="")
	{
	    $("#mobile_msg").html("请输入手机号码");
	    return;
	}
	else
	{
		$.post('${ctx}/mall/ronghan/checkMobile',{mobile:mobile,channelId:$('#channelId').val()},function(response){
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
