function getmessage() {

	var pathName = document.location.pathname;
	var index = pathName.substr(1).indexOf("/");
	var contextpath = pathName.substr(0,index+1);
	var serviceValue =  $('#serviceid').val();
	//动态获取菜单数据
	$.ajax({  
	    cache:false,
        url : contextpath + '/merchantOperate/getmessage?serviceid=' + serviceValue,
        success : function(result) {  

            $('#sms').val(result);
        }
    });
}
function getTotal() {

	var pathName = document.location.pathname;
	var index = pathName.substr(1).indexOf("/");
	var contextpath = pathName.substr(0,index+1);
	var beginDate =  $('#beginDate').val();
	var endDate =  $('#endDate').val();
	
	//动态获取菜单数据
	$.ajax({  
	    cache:false,
        url : contextpath + '/merchantOperate/total?beginDate=' + beginDate+'&endDate=' + endDate,
        success : function(result) {  
            $('#total').val(result);
        }
    });
}
/*
 * 导出excel
 * 
 */
function exportExcel() {
	var pathName = document.location.pathname;
	var index = pathName.substr(1).indexOf("/");
	var contextpath = pathName.substr(0,index+1);
	var beginDate =  $('#beginDate').val();
	var endDate =  $('#endDate').val();
	location.href= 'excel?beginDate=' + beginDate+'&endDate=' + endDate;
}