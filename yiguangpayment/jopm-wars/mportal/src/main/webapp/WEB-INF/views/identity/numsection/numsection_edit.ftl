﻿<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3c.org/TR/1999/REC-html401-19991224/loose.dtd">
<TITLE>添加角色</TITLE>
<META content="text/html; charset=utf-8" http-equiv=content-type>
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<#assign base=request.contextPath>
<#setting number_format="#">
<LINK rel=stylesheet type=text/css href="${base}/template/admin/css/common.css">
<SCRIPT type=text/javascript src="${base}/template/admin/js/jquery.js"></SCRIPT>
<SCRIPT type=text/javascript src="${base}/template/admin/js/jquery.tools.js"></SCRIPT>
<SCRIPT type=text/javascript src="${base}/template/admin/js/jquery.validate.js"></SCRIPT>
<SCRIPT type=text/javascript src="${base}/template/admin/js/common.js"></SCRIPT>
<SCRIPT type=text/javascript src="${base}/template/admin/js/input.js"></SCRIPT>
<script language="javascript">
function getCtiy(provinceid){
    $.ajax({
        type: "post",
        data: null,
        url: "addCity?id="+provinceid,
		async: false,
        success: function (data) {
	        var citylist=data.split("|");
	        document.getElementById("cityid").options.length=0; 
	        var i=0;
	        while(i<citylist.length-1)
	        {
	        	var city=citylist[i].split("*");
	        	document.getElementById("cityid").options.add(new Option(city[1],city[0])); 
	        	i++;
	        }
        },
        error: function () {
            alert("操作失败，请重试");
        }
    }); 
}
$(function(){
	getCtiy('${numSection.province.provinceId}');	
	var objSelect=document.getElementById("cityid");
	for (var i = 0; i < objSelect.options.length; i++) {        
        if (objSelect.options[i].value == '${numSection.city.cityId}') {        
            objSelect.options[i].selected=true;
            break;        
        }   
    }
})

function check(){  
  		 
      /******  运营商 ******/  
      var carrierInfoId = $("#carrierInfoid").val().trim();
      if(carrierInfoId==""){  
         alert('运营商 不能为空！');  
         return false;  
      } 
      /******  省份  ******/  
      var provinceId = $("#provinceid").val().trim();
      if(provinceId==""){  
         alert('省份不能为空！');  
         return false;  
      }  
      /****** 城市  ******/  
      var cityId = $("#cityid").val().trim();
      if(cityId==""){  
         alert('城市不能为空！');  
         return false;  
      }  
 }  
</script>
<body>
<div class="mg10"></div>
	<form id="inputForm" action="edit"  method="post" onsubmit="return check()" >
		<table class="input">
			<tr>
				<th>
					<span class="requiredField">*</span>号码段:
				</th>
				<td>
					<input type="hidden" id="sectionId" name="sectionId" class="ipt" maxlength="7" value="${numSection.sectionId}"/>
					${numSection.sectionId}	 
				</td>
			</tr>
			<tr>
				<th>
					<span class="requiredField">*</span>运营商:
				</th>
				<td>
					<select name="carrierInfo.carrierNo" id="carrierInfoid" class="select">
					<#list carrierInfo as list>
						<option value="${list.carrierNo}" <#if numSection.carrierInfo.carrierNo==list.carrierNo>selected=selected</#if>>
							${(list.carrierName)!""}
						</option>
					</#list>
				</td>
			</tr>
			<tr>
				<th>
					<span class="requiredField">*</span>省份:
				</th>
				<td>
					<select name="province.provinceId" id="provinceid" onchange="getCtiy(this.value)" class="select">
					<#list province as list>
						<option value="${list.provinceId}" <#if numSection.province.provinceId==list.provinceId>selected=selected</#if>>
							${(list.provinceName)!""}
						</option>
					</#list>
				</select>
				</td>
			</tr>
			<tr>
				<th>
					<span class="requiredField">*</span>城市:
				</th>
				<td>
					<select name="city.cityId" id="cityid" class="select">
					</select>
				</td>
			</tr>
			<tr>
				<th>
					号段类型:
				</th>
				<td>
					<input type="text" id="mobileType" name="mobileType" class="ipt" maxlength="50" value="${(numSection.mobileType)!""}"/>	 
				</td>
			</tr>			
			<tr>
				<th>
					&nbsp;
				</th>
				<td>
					<input type="submit" class="button" value="确&nbsp;&nbsp;定" />
					<input type="button" class="button" value="返&nbsp;&nbsp;回" onclick="location.href='list'" />
				</td>
			</tr>
		</table>
	</form>
</body>

</HTML>
