﻿<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3c.org/TR/1999/REC-html401-19991224/loose.dtd">
<TITLE>添加角色</TITLE>
<META content="text/html; charset=utf-8" http-equiv=content-type>
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<#assign base=request.contextPath>
<#setting number_format="#">
<#assign shiro=JspTaglibs["http://shiro.apache.org/tags"] />
<LINK rel=stylesheet type=text/css href="${base}/template/admin/css/common.css">
<SCRIPT type=text/javascript src="${base}/template/admin/js/jquery.js"></SCRIPT>
<SCRIPT type=text/javascript src="${base}/template/admin/js/jquery.tools.js"></SCRIPT>
<SCRIPT type=text/javascript src="${base}/template/admin/js/jquery.validate.js"></SCRIPT>
<SCRIPT type=text/javascript src="${base}/template/admin/js/common.js"></SCRIPT>
<SCRIPT type=text/javascript src="${base}/template/admin/js/input.js"></SCRIPT>
<script language="javascript" type="text/javascript" src="${base}/template/admin/My97DatePicker/WdatePicker.js"></script>
<SCRIPT type=text/javascript src="${base}/template/admin/js/RSA/Barrett.js"></SCRIPT>
<SCRIPT type=text/javascript src="${base}/template/admin/js/RSA/BigInt.js"></SCRIPT>
<SCRIPT type=text/javascript src="${base}/template/admin/js/RSA/RSA.js"></SCRIPT>
<script>
    function rsalogin(){
    
		var customerName=$('#customerName').val();
		if(jmz.GetLength(customerName) > 20){
			   alert("用户名称长度不能超过20位字符！"); 
			   return false;
		}
		var displayName=$('#displayName').val();
		if(jmz.GetLength(displayName) > 20){
			   alert("显示名称长度不能超过20位字符！"); 
			   return false;
		}
		var payPwdInp=$("#payPwdInp").val();
		var loginPwdInp=$("#loginPwdInp").val();
		
		var modulus=$("#modulus").val();
		var exponent=$("#exponent").val();
		
		setMaxDigits(130);
		var key = new RSAKeyPair(exponent,"",modulus);
		var loginresult = encryptedString(key, encodeURIComponent(loginPwdInp));
		var payresult = encryptedString(key, encodeURIComponent(payPwdInp));
		$('#loginPwd').val(loginresult);
		$('#payPwd').val(payresult);
		
		$("#inputForm").submit();
	}
	
	// 表单验证
	$(document).ready(function() { 
		var validator = $("#inputForm").validate({
			rules: {
				'customerName':{
					required:true,
					minlength: 2,
					maxlength: 20
				},
				'displayName':{
					required:true,
					minlength: 2,
					maxlength: 20
				},
				'loginPwdInp':{
					required:true,
					minlength: 6,
					maxlength: 18
				},
				'payPwdInp':{
					required:true,
					minlength: 6,
					maxlength: 18
				}
			}
		});
	});
	
	
	
</script>
<body>
<div class="mg10"></div>
	<form id="inputForm" action="add"  method="post">
	<input type="hidden" id="exponent" name="exponent" value="${exponent!""}"/>
  	<input type="hidden" id="modulus" name="modulus" value="${modulus!""}"/>
  	<input type="hidden" id="loginPwd" name="loginPwd"/>
  	<input type="hidden" id="payPwd" name="payPwd"/>
		<table class="input">
			<tr>
				<th>
					<span class="requiredField">*</span>用户名称:
				</th>
				<td>
					<input type="text" id="customerName" name="customerName" class="ipt" maxlength="20" />	 
				</td>
			</tr>
			<tr>
				<th>
					<span class="requiredField">*</span>显示名称:
				</th>
				<td>
					<input type="text" id="displayName" name="displayName" class="ipt" maxlength="20" />	 
				</td>
			</tr>
			<tr>
				<th>
					<span class="requiredField">*</span>登录密码:
				</th>
				<td>
					<input type="password" id="loginPwdInp" name="loginPwdInp" class="ipt" maxlength="18" />	 
				</td>
			</tr>
			<tr>
				<th>
					<span class="requiredField">*</span>支付密码:
				</th>
				<td>
					<input type="password" id="payPwdInp" name="payPwdInp" class="ipt" maxlength="18" />	 
				</td>
			</tr>
			<tr>
				<th>
					性别:
				</th>
				<td>
					<INPUT id="person.sex" name="person.sex"  type="radio" value="0" checked  >男
					<INPUT id="person.sex" name="person.sex"  type="radio" value="1" checked  >女
				</td>
			</tr>
			<tr>
				<th>
					出生日期:
				</th>
				<td>
					<input type="text" id="person.birthday" name="person.birthday" class="ipt" maxlength="20"  onClick="WdatePicker({dateFmt:'yyyy-MM-dd'});" />
				</td>
			</tr>
			<tr>
				<th>
					用户类型:
				</th>
				<td>
					<select name="userType" id="userType" class="select">
					<option value="HuFei">
						跃程缴费
					</option>
				</select>
				</td>
			</tr>
			
			<tr>
				<th>
					&nbsp;
				</th>
				<td>
					<input type="button" class="button" value="确&nbsp;&nbsp;定" onclick="rsalogin();"/>
					<input type="button" class="button" value="返&nbsp;&nbsp;回" onclick="location.href='list'" />
				</td>
			</tr>
		</table>
	</form>
</body>

</HTML>
