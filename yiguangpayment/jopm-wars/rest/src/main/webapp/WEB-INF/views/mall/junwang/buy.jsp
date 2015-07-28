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
					border="0" SRC="${ctx}/template/default/img/TEL.jpg"> <a
					target=blank
					href=http://wpa.qq.com/msgrd?V=1&uin=1957518051&Site=短信支付&Menu=yes><img
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
				商品信息：<span>${productName}</span>
			</div>
			<div class="ne">
				<div class="dh">
					<ul>
						<li><a class="lt">联通短信验证码支付</a></li>
						<li class="cur"><a class="dx">电信短信验证码支付</a></li>
						<li><a class="gdlt">广东联通短信验证码支付</a></li>
					</ul>
				</div>

				<div class="zuo">
					<form action="${ctx}/mall/junwang/placeOrder" method="post"
						name="form1">
						<input style="height: 30px; width: 375px;" name="orderid"
							id="orderid" value="${orderid}" type="hidden" /> <input
							style="height: 30px; width: 375px;" name="cpid" id="cpid"
							value="1000" type="hidden" /> <input
							style="height: 30px; width: 375px;" name="channelId"
							id="channelId" value="3" type="hidden" /> <input
							style="height: 30px; width: 375px;" name="chargingType"
							id="chargingType" value="${chargingType}" type="hidden" />
						<!--<div style="color:red;margin-bottom:10px;">重要通知：因运营商成本增加，将于6月12日下午2点提高骏卡一卡通J点售价，30元1000J点，20元700J点，10元300J点，5元150J点，请各位消费者知悉~</div>-->
						<table width="582" height="236" border="0">

							<tr>
								<td width="300" align="right">产 品： <br /></td>
								<td width="537"><span id="pointerList"></span></td>
							</tr>
							<c:if test="${chargingType == 1}">
								<tr>
									<td align="right">骏卡一卡通账号：</td>
									<td><input style="height: 30px; width: 375px;"
										name="username" id="username" type="text" /> <br /> <span
										id="username_msg" style="color: red"></span></td>
								</tr>
							</c:if>
							<tr>
								<td align="right">手机号码：</td>
								<td><input style="height: 30px; width: 375px;"
									name="mobile" id="mobile" type="text" /><br /> <span
									id="mobile_msg" style="color: red"></span></td>
							</tr>
							<tr>
								<td align="right">验证码：</td>
								<td><input type="text" class="input" id="checkCode"
									maxlength="4" /> <img src="${ctx}/PictureCheckCode"
									id="CreateCheckCode" align="absmiddle" title="看不清，点击换一张">

								</td>
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
										<a href="${ctx}/mall/junwang/queryOrder?order_id=${order_id}">查看订单状态</a>
									</div>
								</td>
							</tr>
						</table>
					</div>
				</div>
				<div class="you">
					<p>
						<b>手机验证码支付说明：</b>
					</p>
					<p>
						注：广东联通用户双日消费限额20元，月消费限额100元；<br />
						验证码支付：即通过确认手机收到的验证码完成支付扣费；<br />
						支持中国移动号段：134~139、147、150~152、157~159、182、183、184、187、188；<br />
						支持中国联通号段： 130-132、145、155、156、185、186；<br /> 支持中国电信号段：
						133、153、180、181、189；<br /> 在支付过程中，如您选择的支付通道受限限制，请更换其它通道进行支付。<br />
						请按页面和验证码提示完成操作。
					</p>
					<p>
						在线客服1：<a target=blank
							href=http://wpa.qq.com/msgrd?V=1&uin=1957518051&Site=短信支付&Menu=yes><img
							border="0" SRC=http://wpa.qq.com/pa?p=1:1957518051:1
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


	<div class="wei">
		<p>
			<a href="#">公司概况 </a> &nbsp;|&nbsp; <a href="#">联系我们 </a>
			&nbsp;|&nbsp; <a href="#">合作伙伴</a> &nbsp;|&nbsp; <a href="#">诚聘英才</a><br />
			广州翼光网络科技有限公司 版权所有 2012-2014 粤ICP备14050898号-1
		</p>
	</div>
</body>
<script type="text/javascript">
$(function () { 
	showPointer('3');
	$('#channelId').val('3');
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
		else if (inArray('gdlt',optL))
		{
			showPointer('4');
			$('#channelId').val('4');
		}
		
		$('.dh').find('li').removeClass('cur'); 
		$(this).addClass('cur'); 
	});

	//算术验证
	$("#CreateCheckCode").click(function(){
		$(this).attr("src","${ctx}/PictureCheckCode?" + Math.random());
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
function showPointer(channelId){
	var chargingType = $('#chargingType').val();
	$.post('${ctx}/mall/junwang/pointList',{channelId:channelId,chargingType:chargingType},function(response){
		$('#pointerList').show().html(response);
	});
}

function dosubmit()
{
	<c:if test="${chargingType == 1}">
	var account = $("#username").val();
	var chargingType = $("#chargingType").val();
	if(chargingType == 1 && account== ""){
		$("#username_msg").html("请填写充值账号");
		return false;
	}else if(chargingType == 1 && account.length >64){
		$("#username_msg").html("账号不合法");
		return false;
	}else{
		$.post('${ctx}/mall/junwang/checkAccount',{account:account},function(response){
			if (response == 0)
			{
	</c:if>
				$("#username_msg").html('');
				var mobile = $("#mobile").val();
				if(mobile=="")
				{
				    $("#mobile_msg").html("请输入手机号码");
				    return;
				}
				else
				{
					$.post('${ctx}/mall/junwang/checkMobile',{mobile:mobile,channelId:$('#channelId').val()},function(response){
						if (response == 0)
						{
							$("#mobile_msg").html('');
							 var checkCode = $("#checkCode").val();
								$.post("${ctx}/mall/junwang/checkCode",{checkCode:checkCode},function(msg){
									if(msg!=0){
										alert("验证码错误！");
										return;
									}
									else
									{
										document.form1.submit();
									}
									
								});
						}
						else
						{
							$("#mobile_msg").html(response);
						}
					});
				}
				
	<c:if test="${chargingType == 1}">
			}
			else
			{
				$("#username_msg").html(response);
			}
		});
	}
	</c:if>
	
	
	/*re= /^(13[0-9]{9})|(15[89][0-9]{8})$/
    if(!re.test(mobile))
    {
         $("#mobile_msg").html("请输入正确的联通手机号码");
         return;
    }else{
    	 $("#mobile_msg").html("");
    }*/
    
    
    
   
}
</script>
</html>
