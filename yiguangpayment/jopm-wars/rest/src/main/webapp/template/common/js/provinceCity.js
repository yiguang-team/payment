/**
 * <tbody class="listTable">
 * 
 * 省<span class="p_${status.count}"></span>
 * 
 * 城市<span class="c_${status.count}"></span>
 * 
 * 运营商 <span class="carrierInfo_${status.count}"></span>
 */



$(document).ready(function(){
			var l=$(".listTable").find("tr").length;
			for(var i=1;i<=l;i++){
				var p=province($('.p_'+i).text());
				$('.p_'+i).text(p);
				
				var c=city($('.c_'+i).text());
				$('.c_'+i).text(c);
				
				var carrierInfoStr=carrierInfo($('.carrierInfo_'+i).text());
				$('.carrierInfo_'+i).text(carrierInfoStr);
			}
		});

 //省份
 function province(provinceStr){
	 var map = new Object();
	 map['AH_']='安徽';
	 map['BJ_']='北京';
	 map['CQ_']='重庆';
	 map['FJ_']='福建';
	 map['GD_']='广东';
	 map['GS_']='甘肃';
	 map['GX_']='广西';
	 map['GZ_']='贵州';
	 map['HEB']='河北';
	 map['HEN']='河南';
	 map['HIN']='海南';
	 map['HLJ']='黑龙江';
	 map['HUB']='湖北';
	 map['HUN']='湖南';
	 map['JN_']='吉林';
	 map['JS_']='江苏';
	 map['JX_']='江西';
	 map['LN_']='辽宁';
	 map['NMG']='内蒙古';
	 map['NX_']='宁夏';
	 map['QG_']='全国';
	 map['QH_']='青海';
	 map['SC_']='四川';
	 map['SD_']='山东';
	 map['SH_']='上海';
	 map['SX1']='陕西';
	 map['SX2']='山西';
	 map['TJ_']='天津';
	 map['XJ_']='新疆';
	 map['XZ_']='西藏';
	 map['YN_']='云南';
	 map['ZJ_']='浙江';
	 return map[provinceStr];
 }
 
 //城市
  function city(cityStr){
	  var map = new Object();
	  map['010_']='北京';
	  map['020_']='广州';
	  map['021_']='上海';
	  map['022_']='天津';
	  map['023_']='重庆';
	  map['024_']='沈阳';
	  map['025_']='南京';
	  map['027_']='武汉';
	  map['028_']='成都';
	  map['0281']='眉山';
	  map['029_']='西安';
	  map['0310']='邯郸';
	  map['0311']='石家庄';
	  map['0312']='保定';
	  map['0313']='张家口';
	  map['0314']='承德';
	  map['0315']='唐山';
	  map['0316']='廊坊';
	  map['0317']='沧州';
	  map['0318']='衡水';
	  map['0319']='邢台';
	  map['0335']='秦皇岛';
	  map['0349']='朔州';
	  map['0350']='忻州';
	  map['0351']='太原';
	  map['0352']='大同';
	  map['0353']='阳泉';
	  map['0354']='晋中';
	  map['0355']='长治';
	  map['0356']='晋城';
	  map['0357']='临汾';
	  map['0358']='吕梁地区';
	  map['0359']='运城';
	  map['0370']='商丘';
	  map['0371']='郑州';
	  map['0372']='安阳';
	  map['0373']='新乡';
	  map['0374']='许昌';
	  map['0375']='平顶山';
	  map['0376']='信阳';
	  map['0377']='南阳';
	  map['0378']='开封';
	  map['0379']='洛阳';
	  map['0391']='焦作';
	  map['0392']='鹤壁';
	  map['0393']='濮阳';
	  map['0394']='周口';
	  map['0395']='漯河';
	  map['0396']='驻马店';
	  map['0398']='三门峡';
	  map['0410']='铁岭';
	  map['0411']='大连';
	  map['0412']='鞍山';
	  map['0413']='抚顺';
	  map['0414']='本溪';
	  map['0415']='丹东';
	  map['0416']='锦州';
	  map['0417']='营口';
	  map['0418']='阜新';
	  map['0419']='辽阳';
	  map['0421']='朝阳';
	  map['0427']='盘锦';
	  map['0429']='葫芦岛';
	  map['0431']='长春';
	  map['0432']='吉林';
	  map['0433']='延边朝鲜族自治州';
	  map['0434']='四平';
	  map['0435']='通化';
	  map['0436']='白城';
	  map['0437']='辽源';
	  map['0438']='松原';
	  map['0439']='白山';
	  map['0451']='哈尔滨';
	  map['0452']='齐齐哈尔';
	  map['0453']='牡丹江';
	  map['0454']='佳木斯';
	  map['0455']='绥化';
	  map['0456']='黑河';
	  map['0457']='大兴安岭地区';
	  map['0458']='伊春';
	  map['0459']='大庆';
	  map['0464']='七台河';
	  map['0467']='鸡西';
	  map['0468']='鹤岗';
	  map['0469']='双鸭山';
	  map['0470']='呼伦贝尔';
	  map['0471']='呼和浩特';
	  map['0472']='包头';
	  map['0473']='乌海';
	  map['0474']='乌兰察布盟';
	  map['0475']='通辽';
	  map['0476']='赤峰';
	  map['0477']='鄂尔多斯';
	  map['0478']='巴彦淖尔盟';
	  map['0479']='锡林郭勒盟';
	  map['0482']='兴安盟';
	  map['0483']='阿拉善盟';
	  map['0510']='无锡';
	  map['0511']='镇江';
	  map['0512']='苏州';
	  map['0513']='南通';
	  map['0514']='扬州';
	  map['0515']='盐城';
	  map['0516']='徐州';
	  map['0517']='淮安';
	  map['0518']='连云港';
	  map['0519']='常州';
	  map['0523']='泰州';
	  map['0527']='宿迁';
	  map['0530']='菏泽';
	  map['0531']='济南';
	  map['0532']='青岛';
	  map['0533']='淄博';
	  map['0534']='德州';
	  map['0535']='烟台';
	  map['0536']='潍坊';
	  map['0537']='济宁';
	  map['0538']='泰安';
	  map['0539']='临沂';
	  map['0543']='滨州';
	  map['0546']='东营';
	  map['0550']='滁州';
	  map['0551']='合肥';
	  map['0552']='蚌埠';
	  map['0553']='芜湖';
	  map['0554']='淮南';
	  map['0555']='马鞍山';
	  map['0556']='安庆';
	  map['0557']='宿州';
	  map['0558']='亳州';
	  map['0559']='黄山';
	  map['0561']='淮北';
	  map['0562']='铜陵';
	  map['0563']='宣城';
	  map['0564']='六安';
	  map['0565']='巢湖';
	  map['0566']='池州';
	  map['0570']='衢州';
	  map['0571']='杭州';
	  map['0572']='湖州';
	  map['0573']='嘉兴';
	  map['0574']='宁波';
	  map['0575']='绍兴';
	  map['0576']='台州';
	  map['0577']='温州';
	  map['0578']='丽水';
	  map['0579']='金华';
	  map['0580']='舟山';
	  map['0591']='福州';
	  map['0592']='厦门';
	  map['0593']='宁德';
	  map['0594']='莆田';
	  map['0595']='泉州';
	  map['0596']='漳州';
	  map['0597']='龙岩';
	  map['0598']='三明';
	  map['0599']='南平';
	  map['0631']='威海';
	  map['0632']='枣庄';
	  map['0633']='日照';
	  map['0634']='莱芜';
	  map['0635']='聊城';
	  map['0660']='汕尾';
	  map['0662']='阳江';
	  map['0663']='揭阳';
	  map['0668']='茂名';
	  map['0691']='西双版纳傣族自治州';
	  map['0692']='德宏傣族景颇族自治州';
	  map['0701']='鹰潭';
	  map['0710']='襄樊';
	  map['0711']='鄂州';
	  map['0712']='孝感';
	  map['0713']='黄冈';
	  map['0714']='黄石';
	  map['0715']='咸宁';
	  map['0716']='荆州';
	  map['0717']='宜昌';
	  map['0718']='恩施';
	  map['0719']='十堰';
	  map['0722']='随州';
	  map['0724']='荆门';
	  map['0728']='天门';
	  map['0730']='岳阳';
	  map['0731']='长沙';
	  map['0732']='湘潭';
	  map['0733']='株洲';
	  map['0734']='衡阳';
	  map['0735']='郴州';
	  map['0736']='常德';
	  map['0737']='益阳';
	  map['0738']='娄底';
	  map['0739']='邵阳';
	  map['0743']='湘西土家族苗族自治州';
	  map['0744']='张家界';
	  map['0745']='怀化';
	  map['0746']='永州';
	  map['0750']='江门';
	  map['0751']='韶关';
	  map['0752']='惠州';
	  map['0753']='梅州';
	  map['0754']='汕头';
	  map['0755']='深圳';
	  map['0756']='珠海';
	  map['0757']='佛山';
	  map['0758']='肇庆';
	  map['0759']='湛江';
	  map['0760']='中山';
	  map['0762']='河源';
	  map['0763']='清远';
	  map['0766']='云浮';
	  map['0768']='潮州';
	  map['0769']='东莞';
	  map['0770']='防城港';
	  map['0771']='南宁';
	  map['0772']='柳州';
	  map['0773']='桂林';
	  map['0774']='梧州';
	  map['0775']='玉林';
	  map['0776']='百色地区';
	  map['0777']='钦州';
	  map['0778']='河池地区';
	  map['0779']='北海';
	  map['0790']='新余';
	  map['0791']='南昌';
	  map['0792']='九江';
	  map['0793']='上饶';
	  map['0794']='抚州';
	  map['0795']='宜春';
	  map['0796']='吉安';
	  map['0797']='赣州';
	  map['0798']='景德镇';
	  map['0799']='萍乡';
	  map['0812']='攀枝花';
	  map['0813']='自贡';
	  map['0816']='绵阳';
	  map['0817']='南充';
	  map['0818']='达州';
	  map['0825']='遂宁';
	  map['0826']='广安';
	  map['0827']='巴中';
	  map['0830']='泸州';
	  map['0831']='宜宾';
	  map['0832']='内江';
	  map['0833']='乐山';
	  map['0834']='凉山';
	  map['0835']='雅安';
	  map['0836']='甘孜';
	  map['0837']='阿坝';
	  map['0838']='德阳';
	  map['0839']='广元';
	  map['0851']='贵阳';
	  map['0852']='遵义';
	  map['0853']='安顺';
	  map['0854']='黔南布依族苗族自治州';
	  map['0855']='黔东南苗族侗族自治州';
	  map['0856']='铜仁地区';
	  map['0857']='毕节地区';
	  map['0858']='六盘水';
	  map['0859']='黔西南布依族苗族自治州';
	  map['0870']='昭通';
	  map['0871']='昆明';
	  map['0872']='大理白族自治州';
	  map['0873']='红河哈尼族彝族自治州';
	  map['0874']='曲靖';
	  map['0875']='保山';
	  map['0876']='文山壮族苗族自治州';
	  map['0877']='玉溪';
	  map['0878']='楚雄彝族自治州';
	  map['0879']='思茅地区';
	  map['0883']='临沧地区';
	  map['0886']='怒江傈傈族自治州';
	  map['0887']='迪庆藏族自治州';
	  map['0888']='丽江地区';
	  map['0891']='拉萨';
	  map['0892']='日喀则地区';
	  map['0893']='山南地区';
	  map['0894']='林芝地区';
	  map['0895']='昌都地区';
	  map['0896']='那曲地区';
	  map['0897']='阿里地区';
	  map['0898']='海口';
	  map['0901']='塔城地区';
	  map['0902']='哈密地区';
	  map['0903']='和田地区';
	  map['0906']='阿勒泰地区';
	  map['0908']='克孜勒苏柯尔克孜自治州';
	  map['0909']='博尔塔拉蒙古自治州';
	  map['0911']='延安';
	  map['0912']='榆林';
	  map['0913']='渭南';
	  map['0914']='商洛';
	  map['0915']='安康';
	  map['0916']='汉中';
	  map['0917']='宝鸡';
	  map['0919']='铜川';
	  map['0930']='临夏回族自治州';
	  map['0931']='兰州';
	  map['0932']='定西地区';
	  map['0933']='平凉地区';
	  map['0934']='庆阳地区';
	  map['0935']='金昌';
	  map['0936']='张掖地区';
	  map['0937']='酒泉地区';
	  map['0938']='天水';
	  map['0939']='陇南地区';
	  map['0941']='甘南藏族自治州';
	  map['0943']='白银';
	  map['0951']='银川';
	  map['0952']='石嘴山';
	  map['0953']='吴忠';
	  map['0954']='固原';
	  map['0970']='海北藏族自治州';
	  map['0971']='西宁';
	  map['0972']='海东地区';
	  map['0973']='黄南藏族自治州';
	  map['0974']='海南藏族自治州';
	  map['0975']='果洛藏族自治州';
	  map['0976']='玉树藏族自治州';
	  map['0977']='海西蒙古族藏族自治州';
	  map['0979']='格尔木';
	  map['0990']='克拉玛依';
	  map['0991']='乌鲁木齐';
	  map['0992']='奎屯';
	  map['0993']='石河子';
	  map['0994']='昌吉回族自治州';
	  map['0995']='吐鲁番地区';
	  map['0996']='巴音郭楞蒙古自治州';
	  map['0997']='阿克苏地区';
	  map['0998']='喀什地区';
	  map['0999']='伊犁哈萨克自治州';
	  return map[cityStr];
  }
  
  
//运营商
  function carrierInfo(carrierInfoStr){
	  var map = new Object();
	  map['YD']='移动';
	  map['LT']='联通';
	  map['DX']='电信';
	  return map[carrierInfoStr];
  }
 
 