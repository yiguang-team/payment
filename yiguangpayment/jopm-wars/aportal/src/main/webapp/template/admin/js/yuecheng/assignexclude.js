function getCtiy(provinceid){
	if(provinceid=="0")
	{
		 document.getElementById("cityId").options.length=0; 
		 document.getElementById("cityId").options.add(new Option('--请选择--','0'));
	}else{
	    $.ajax({
	        type: "post",
	        data: null,
	        url: "addCity?id="+provinceid,
			async: false,
	        success: function (data) {
		        var citylist=data.split("|");
		        document.getElementById("cityId").options.length=0; 
		        document.getElementById("cityId").options.add(new Option('--请选择--','0'));
		        var i=0;
		        while(i<citylist.length-1)
		        {
		        	var city=citylist[i].split("*");
		        	document.getElementById("cityId").options.add(new Option(city[1],city[0])); 
		        	i++;
		        }
		        var mid=document.getElementById("merchantId").options[document.getElementById("merchantId").selectedIndex].value;
		        getProduct(mid,'2');
	        },
	        error: function () {
	            alert("操作失败，请重试");
	        }
	    }); 
    }
}


function getProduct(mid,selectType){
	mid=document.getElementById("merchantId").options[document.getElementById("merchantId").selectedIndex].value;
	if(mid=="0")
	{
		document.getElementById("productNo").options.length=0; 
	 	document.getElementById("productNo").options.add(new Option('--请选择--','0'));
	}else{
		
		var type=document.getElementById("merchantType").options[document.getElementById("merchantType").selectedIndex].value;
		var carrier=document.getElementById("carrierId").options[document.getElementById("carrierId").selectedIndex].value;
		var province="";
		var city="";
		if(selectType=='1')
		{
			province=document.getElementById("provinceId").options[document.getElementById("provinceId").selectedIndex].value;
			city=document.getElementById("cityId").options[document.getElementById("cityId").selectedIndex].value;
		}	
		if(selectType=='2')
		{
			province=document.getElementById("provinceId").options[document.getElementById("provinceId").selectedIndex].value;
		}
	    $.ajax({
	        type: "post",
	        data: null,
	        url: "getProduct?mid="+mid+"&type="+type+"&carrier="+carrier+"&province="+province+"&city="+city,
			async: false,
	        success: function (data) {
		        var productlist=data.split("|");
		        document.getElementById("productNo").options.length=0; 
		        var i=0;
				document.getElementById("productNo").options.add(new Option('--请选择--','0'));
		        while(i<productlist.length-1)
		        {
		        	var product=productlist[i].split("*");
		        	document.getElementById("productNo").options.add(new Option(product[1],product[0])); 
		        	i++;
		        }
	        },
	        error: function () {
	            alert("操作失败，请重试");
	        }
	    }); 
	}
}


function getMerchant(pid){
	if(pid=="0"){
		document.getElementById("divshelflist").innerHTML="";
	}else{
		var mid=document.getElementById("merchantId").options[document.getElementById("merchantId").selectedIndex].value;
		var ruleType=document.getElementById("ruleType").options[document.getElementById("ruleType").selectedIndex].value;
		var type=document.getElementById("merchantType").options[document.getElementById("merchantType").selectedIndex].value;
		
	    $.ajax({
	        type: "post",
	        data: null,
	        url: "getMerchant?pid="+pid+"&mid="+mid+"&ruleType="+ruleType+"&type="+type,
			async: false,
	        success: function (data) {
		        var productlist=data.split("|");
		        var strhtml=""; 
		        var i=0;
		        while(i<productlist.length-1)
		        {
		        	var product=productlist[i].split("*");
		        	if(product[2]=="0")
		        	{
						strhtml=strhtml+"<input type='checkbox' name='objectMerchantId' value='"+product[0]+"' checked/>"+product[1];
					}else
					{
						strhtml=strhtml+"<input type='checkbox' name='objectMerchantId' value='"+product[0]+"'/>"+product[1];
					}
		        	i++;
		        }
		        document.getElementById("divshelflist").innerHTML=strhtml;
	        },
	        error: function () {
	            alert("操作失败，请重试");
	        }
	    }); 
	}
}

function addAssignExclude(){
    var businessNo=document.getElementById("businessNo").options[document.getElementById("businessNo").selectedIndex].value;
    var merchantType=document.getElementById("merchantType").options[document.getElementById("merchantType").selectedIndex].value;
    var merchantId=document.getElementById("merchantId").options[document.getElementById("merchantId").selectedIndex].value;
    var productNo=document.getElementById("productNo").options[document.getElementById("productNo").selectedIndex].value;
    var ruleType=document.getElementById("ruleType").options[document.getElementById("ruleType").selectedIndex].value;
    var r=document.getElementsByName("objectMerchantId");
    var result="";
    for(var i=0;i<r.length;i++){
         if(r[i].checked){
         result=result+r[i].value+"|";
       }
    }
   if(result!=""&&result!=null){
	  
	    $.ajax({
	        type: "post",
	        data: null,
	        url: "saveAssignExclude?businessNo="+businessNo+"&merchantType="+merchantType+"&merchantId="+merchantId+"&productNo="+productNo+"&ruleType="+ruleType+"&objectMerchant="+result,
			async: false,
	        success: function (data) {
		        if(data=="true")
		        {
		                alert('操作成功');
		        }else{
		                alert('操作失败');
		        }
	        },
	        error: function () {
	            alert("操作失败，请重试");
	        }
	    }); 
        self.location.href='listAssignExclude';
        return false;
   }else{
	   alert("请选择指定排除的商户！");
 	   return false;
   }
}


function editAssignExclude(){
    var businessNo=document.getElementById("businessNo").options[document.getElementById("businessNo").selectedIndex].value;
    var merchantType=document.getElementById("merchantType").options[document.getElementById("merchantType").selectedIndex].value;
    var merchantId=document.getElementById("merchantId").options[document.getElementById("merchantId").selectedIndex].value;
    var productNo=document.getElementById("productNo").options[document.getElementById("productNo").selectedIndex].value;
    var ruleType=document.getElementById("ruleType").options[document.getElementById("ruleType").selectedIndex].value;
    var r=document.getElementsByName("objectMerchantId"); 
    var result="";
    for(var i=0;i<r.length;i++){
         if(r[i].checked){
         result=result+r[i].value+"|";
       }
    }
    if(result!=""&&result!=null){
	    $.ajax({
	        type: "post",
	        data: null,
	        url: "assignExclude_edit?businessNo="+businessNo+"&merchantType="+merchantType+"&merchantId="+merchantId+"&productNo="+productNo+"&ruleType="+ruleType+"&objectMerchant="+result,
			async: false,
	        success: function (data) {
		        if(data=="true")
		        {
		                alert('操作成功'); 
		        }else{
		                alert('操作失败');
		        }
	        },
	        error: function () {
	            alert("操作失败，请重试");
	        }
	    }); 
		  
        self.location.href='listAssignExclude';
        return false;
    }else{
 	   alert("请选择指定排除的商户！");
 	   return false;
    }
}