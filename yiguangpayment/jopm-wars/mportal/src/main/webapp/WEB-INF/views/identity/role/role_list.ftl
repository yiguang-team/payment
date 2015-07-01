<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3c.org/TR/1999/REC-html401-19991224/loose.dtd">
<HTML xmlns="http://www.w3.org/1999/xhtml"><HEAD>
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<TITLE></TITLE>
<META content="text/html; charset=utf-8" http-equiv=content-type>
<#assign base=request.contextPath>
<#setting number_format="#">
<#assign shiro=JspTaglibs["http://shiro.apache.org/tags"] />
<META name=copyright content=SHOP++>
<LINK rel=stylesheet type=text/css href="${base}/template/admin/css/common.css">
<SCRIPT type=text/javascript src="${base}/template/admin/js/jquery.js"></SCRIPT>
<SCRIPT type=text/javascript src="${base}/template/admin/js/common.js"></SCRIPT>
<SCRIPT type=text/javascript src="${base}/template/common/js/jquery.pager.js"></SCRIPT>
<SCRIPT type=text/javascript>
</SCRIPT>
<style>
#pager ul.pages { 
	display:block; 
	border:none; 
	text-transform:uppercase; 
	font-size:10px; 
	margin:10px 0 50px; 
	padding:0; 
} 
#pager ul.pages li { 
	list-style:none; 
	float:left; 
	border:1px solid #ccc; 
	text-decoration:none; 
	margin:0 5px 0 0; 
	padding:5px; 
} 
#pager ul.pages li:hover { 
	border:1px solid #003f7e; 
} 
#pager ul.pages li.pgEmpty { 
	border:1px solid #eee; 
	color:#eee; 
} 
#pager ul.pages li.pgCurrent { 
	border:1px solid #003f7e; 
	color:#000; 
	font-weight:700; 
	background-color:#eee; 
} 
</style>

<BODY>
<FORM id=listForm method=get action=list>

	<div class="line_bar">
		<INPUT id=page type=hidden name=page> 
		<INPUT id=hasExpired type=hidden name=hasExpired> 
								  角色名称:<INPUT id=searchValue maxLength=200 name=roleName class="ipt" value="${roleName!""}">
								角色类型:<select name="roleType" id="roleType" class="select w80">
										<option value="">所有</option>
										<option value="MERCHANT">商户角色</option>
										<option value="SP">系统角色</option>
									</select>
									<@shiro.user>
								  <input type="submit" class="button" value="查&nbsp;&nbsp;询" />
								  </@shiro.user>
								   <@shiro.user>
								  <input type="button" class="button" onclick="self.location.href='add?menuid=${menuid!""}';"  value="添加角色" />
									</@shiro.user>
	</div>
<TABLE id=listTable class=list>
  <TBODY>
  <TR>
    <TH><SPAN>序号</SPAN> </TH>
    <TH><SPAN>角色名称</SPAN> </TH>
    <TH><SPAN>类型</SPAN> </TH>
    <TH><SPAN>更新人</SPAN> </TH>
    <TH><SPAN>状态</SPAN> </TH>
    <TH><SPAN>创建时间</SPAN> </TH>
    <TH><SPAN>更新时间</SPAN> </TH>
    <TH><SPAN>操作</SPAN> </TH></TR>
    <#if (mlist?size>0)>
    <#list mlist as list>
  <TR>
  <td>${(page-1)*pageSize+list_index+1}</td>
	    <TD>${list.roleName}</TD>
	    <TD><#if list.roleType=="MERCHANT">商户角色<#elseif list.roleType=="SP">系统角色<#elseif list.roleType=="CUSTOMER">用户角色</#if></TD>
	   	<TD>${(list.updateUser)!""}</TD>
	    <#if list.status=="0"> <td>启用<#else><td class="tdgb">禁用</#if></TD>
	    <TD>${list.createTime?string("yyyy-MM-dd HH:mm:ss")}</TD>
	    <TD>${list.updateTime?string("yyyy-MM-dd HH:mm:ss")}</TD>
	     <TD>
	    	
	    	<@shiro.user>
	    		<A href="role_view?id=${list.roleId}">[查看]</A>
	    	</@shiro.user>
	    	<@shiro.user>
				<A href="role_edit?id=${list.roleId}">[编辑]</A>
			</@shiro.user>
			<@shiro.user>
				<A href="role_addtreemenu?id=${list.roleId}">[分配菜单]</A>
			</@shiro.user>
			<@shiro.user>
				<A href="rivilege_addtreemenu?id=${list.roleId}">[分配权限]</A>
			</@shiro.user>
			<@shiro.user>
				<A href="javascript:void(0);" onClick="delData(${list.roleId})">[删除]</A>
			</@shiro.user>
			</TD>
   </TR>
</#list>
<#else>
			<tr>
				<td colspan="7">没数据</td>
			</tr>
	</#if>
</TBODY></TABLE>
<div class="line_pages">
<div style="float:left;">
  	显示条数：
  	<select name="pageSize" id="pageSize" >
		<option value="10" <#if pageSize==10>selected=selected</#if>>10</option>
		<option value="20" <#if pageSize==20>selected=selected</#if>>20</option>
		<option value="30" <#if pageSize==30>selected=selected</#if>>30</option>
		<option value="50" <#if pageSize==40>selected=selected</#if>>50</option>
		<option value="100"<#if pageSize==100>selected=selected</#if>>100</option>	
	</select>&nbsp; 条
  </div>
<div id="pager" style="float:right;"></div>
<div class="pages_menber">(共<SPAN id=pageTotal>${counttotal}</SPAN>条记录)</div>
</div>
  <script type="text/javascript" language="javascript"> 
		$(document).ready(function() { 
			$("#pager").pager({ pagenumber: ${page}, pagecount: ${pagetotal}, buttonClickCallback: PageClick }); 
		}); 
		PageClick = function(pageclickednumber) { 
			  $("#pager").pager({ 
			       pagenumber: pageclickednumber,
				   pagecount: ${pagetotal}, 
				   buttonClickCallback: PageClick 
			});
			
			$("#page").val(pageclickednumber);
		   $("#listForm").submit();
		
		}
		
		
		function delData(id)
		{
			if (!confirm("确认要删除该数据吗?")) {
		        return false;
		    }
			window.location.href="role_delete?id="+id;
		}
</script> 
</FORM></BODY></HTML>

