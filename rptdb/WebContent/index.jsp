<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<!DOCTYPE html>
<html lang="en">
<head>
        <title>仓库信息管理</title> 
        <style> 
        .black_overlay{ 
            display: none; 
            position: absolute; 
            top: 0%; 
            left: 0%; 
            width: 100%; 
            height: 100%; 
            background-color: black; 
            z-index:1001; 
            -moz-opacity: 0.8; 
            opacity:.80; 
            filter: alpha(opacity=88); 
        } 
        .white_content { 
            display: none; 
            position: absolute; 
            top: 25%; 
            left: 25%; 
            //width: 697px; 
            //height: 550px; 
            padding: 20px; 
            //border: #ccc solid 1px; 
            background-color: white; 
            //z-index:1002; 
            overflow: auto; 
        } 
		 html,body{margin:0;padding:0;}
    .iw_poi_title {color:#CC5522;font-size:14px;font-weight:bold;overflow:hidden;padding-right:13px;white-space:nowrap}
    .iw_poi_content {font:12px arial,sans-serif;overflow:visible;padding-top:4px;white-space:-moz-pre-wrap;word-wrap:break-word}
    </style> 
    <meta charset="UTF-8"> 
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="css/bootstrap.min.css"> 
    <link rel="stylesheet" href="css/bootstrap-table.min.css">
	<link rel="stylesheet" href="css/lyz.calendar.css"  type="text/css" />
</head>
<body >
<!-- <h2 style="margin: 0 auto; width: 200px; text-align: center;">仓库信息管理</h2> -->
<div class="container">
	<form  method="post" name=myform>
    <div>
		<label><font color="#FF0000">*</font> 库名: </label><input id="dname" name="dname" type="text" maxlength=66 placeholder="名称">
        <label><font color="#FF0000">*</font> 地址: </label><input id="addr" name="addr" type="text" maxlength=666 placeholder="地址" >
		<label><font color="#FF0000">*</font> 库容: </label><input id="capa" name="capa" type="text" onkeyup="value=value.replace(/[^\d{1,}\.\d{1,}|\d{1,}]/g,'')" maxlength=17 placeholder="（万吨）" >
		<button id="insert" class="btn btn-default">插入</button>
	</div>	
	<div>	
		<input id="deleID" name="deleID"  type="text"  style="display:none;"  placeholder="dele" value=-1>
	 </div>	
        <label>经度: </label><input id="lon" name="lon"  type="text" onkeyup="value=value.replace(/[^\d{1,}\.\d{1,}|\d{1,}]/g,'')" placeholder="经度" >
		<label>纬度: </label><input id="lat" name="lat" type="text"   onkeyup="value=value.replace(/[^\d{1,}\.\d{1,}|\d{1,}]/g,'')" placeholder="纬度" >
		<label>注册日期: </label><input id="txtEndDate" name="txtEndDate"  placeholder="今日"  value="">		
		<button id="cancle" class="btn btn-default" style="display:none;" >取消</button>
	</form>	
	<form name=search  style="float:left;">	
	    <button id="search" class="btn btn-default "  >查询</button>	
	</form>
	<form action="upload" method="post" enctype="multipart/form-data">
	    <input type="file" accept="application/msexcel" name="uploadFile" class="btn btn-default " style="float:left; margin-left:10px;" placeholder="选择一个文件:">
	    <input type="submit" value="上传" class="btn btn-default " style="float:left; margin-left:10px;">
	</form>
	 <div>
	 </div>
    <table id="table"
           data-toggle="table"
           data-show-columns="true"
           data-search="true"
           data-show-refresh="true"
           data-show-toggle="true"
           data-pagination="true"
           data-height="500">
        <thead>
        <tr>
            <th data-field="id">序号</th>
            <th data-field="name">库名</th>
            <th data-field="addr">地址</th>
			<th data-field="capa">库容</th>
            <th data-field="cood">经纬度</th>
			<th data-field="d_date">注册日期</th>
			<th data-field="delete">操作</th>
        </tr>
        </thead>
    </table >
    <div id="dituContent" style="width:697px;height:550px;border:#ccc solid 1px;" class="white_content"></div> 
</div >
<script type="text/javascript" src="http://api.map.baidu.com/api?key=&v=1.1&services=true"></script>
<script src="js/jquery.min.js"></script>
<script src="js/bootstrap.min.js"></script>
<script src="js/lyz.calendar.min.js" type="text/javascript"></script>
<!--<script src="assets/bootstrap2.3/js/bootstrap.min.js"></script>-->
<script src="js/tableExport.js"></script>
<script src="js/jquery.base64.js"></script>
<script src="js/bootstrap-table.js"></script>
<script src="js/bootstrap-table-export.js"></script>
<script>
    $(function () {
    	build();
        $('#search').click(build);//.trigger('click');
        $('#insert').click(insert);//.trigger('click');
        $('#cancle').click(cancle);//.trigger('click');
    });
    $('html').click(function(){
    	document.getElementById('dituContent').style.display='none';
    }),
	$("#txtEndDate").calendar({
            //controlId: "divDate",                // 弹出的日期控件ID，默认: $(this).attr("id") + "Calendar"
            speed: 200,                                           
            readonly: true,    
            lowerLimit: new Date("1900/1/1"),                        
            upperLimit: new Date("9999/12/31"),                  
        });
	function cancle() {  
		//build();inline
		var div1 = document.getElementById('cancle');
		div1.style.display="none";
		$("#deleID").val(-1);
		document.myform.reset();
	}
    function build() { 
    	initMap();//创建和初始化地图
    	document.search.action="index" ;
    	document.search.method="get";
    	//document.search.submit();
        var  i, j, row,
        str ="${al}",
        jsonarr = ${dd}
        columns = [],
    	rows=10,
        data = [],
        cood=[],
		cellname = ["序号","库名","地址","库容","经纬度","注册日期","操作"],
		cells=7;  	  
    	if(str!=""&&$("#deleID").val()!=-1){
        	alert(str)
        	str="";
    	}
        for (i = 0; i < cells; i++) {
            columns.push({
                field: 'field' + i,
                title: cellname[i]
            });            
        }       
        for (i = 0; i <jsonarr.length; i++) {
			var tmpdate = new Date(  parseInt(jsonarr[i].createTime) ) ;
            	row = {};
				row['field'+0 ] = i+1,
				row['field' +1] = jsonarr[i].name,
				row['field' +2] = jsonarr[i].area,
				row['field' +3] = jsonarr[i].capacity,
				row['field' +4] =jsonarr[i].coordinate,
				row['field' +5] =tmpdate.format('yyyy-MM-dd'),
				cood[i]=jsonarr[i];
				//onmouseout="document.getElementById(\'dituContent\')\.style.display=\'none\';
				row['field' +6] = '\"<a href="javascript:edit('+i+')" onmouseover="show_Point(cood['+i+']);document.getElementById(\'dituContent\').style.display=\'block\';">修改</a><a href="javascript:dele('+i+')">  <font color=\"#FF0000\">删除</font></a>\"';
	            data.push(row);
            }  
        $('#table').bootstrapTable('destroy').bootstrapTable({
            columns: columns,
            data: data
        });
    }
    function setMapEvent(){
        map.enableDragging();//启用地图拖拽事件，默认启用(可不写)
        map.enableScrollWheelZoom();//启用地图滚轮放大缩小
        map.enableDoubleClickZoom();//启用鼠标双击放大，默认启用(可不写)
        map.enableKeyboard();//启用键盘上下左右键移动地图
    }
    function show_Point(pp){
        var n=pp.coordinate.split(",");
        var point = new BMap.Point(n[0],n[1]);//定义一个中心点坐标
        map.centerAndZoom(point,12);//设定地图的中心点和坐标并将地图显示在地图容器中
        var label = new BMap.Label("<div style='padding:2px;'>"+pp.name+"</div>",{point:point,offset:new BMap.Size(10,5)});      
        label.setStyle({maxWidth:"none"});
        label.setStyle({backgroundColor:"#999"});
        map.addOverlay(label);
        map.enableScrollWheelZoom();//启用地图滚轮放大缩小
        
    }
    function edit(i)
    {		 	
    	if(confirm("确定修改序号：" + (i+1) + "的记录吗？")){
    		$("#deleID").val(i+"");
    		jsonarr = ${dd}
        	$("#dname").val(jsonarr[i].name);
        	$("#addr").val(jsonarr[i].area);
        	$("#capa").val(jsonarr[i].capacity);
        	var coo=jsonarr[i].coordinate.split(",");
        	$("#lon").val(coo[0]);
        	$("#lat").val(coo[1]);
        	$("#txtEndDate").val(new Date( parseInt(jsonarr[i].createTime)).format('yyyy-MM-dd'));
    		alert("请在上方修改");
    		document.getElementById("insert").innerHTML="修改";
    		var div1 = document.getElementById('cancle');
 		   div1.style.display="inline";
    	}
    }
    function dele(deleID)
    {    	
    	if(confirm("确定删除序号：" + (deleID+1) + "的记录吗？")){
    		$("#deleID").val(deleID+"");
    		document.myform.action="/rptdb/delete";
    		document.myform.method="post";
    		document.myform.submit() ;
    		$("#deleID").val(-1);    		
    	}

    }
    //插入或修改
    function insert()
    {
        var 
        dname ="\库名:"+ $('#dname').val(),
        addr = "\n地址:"+ $('#addr').val(),
        capa = "\n库容:"+ $('#capa').val(),
        lon = $('#lon').val(),
        lat = $('#lat').val(),
        cood="\n经纬度:"+ lon+"."+lat,
        txtEndDate = "\n日期:"+$('#txtEndDate').val(),
        postdata=[dname,addr,capa,cood,txtEndDate];
    	alert(postdata);
    	if($("#deleID").val()<0){
        	document.myform.action="/rptdb/index";//插入
        	document.myform.method="post";
    		document.myform.submit() ;
    	}else{
    		document.myform.action="/rptdb/edit";//修改
    		document.myform.method="post";
    		document.myform.submit() ;
    	}   	
    }
  //创建地图函数：
    function createMap(){
        var map = new BMap.Map("dituContent");//在百度地图容器中创建一个地图
        var point = new BMap.Point(116.395645,39.929986);//定义一个中心点坐标
        map.centerAndZoom(point,10);//设定地图的中心点和坐标并将地图显示在地图容器中
        window.map = map;//将map变量存储在全局
    }

    function initMap(){
        createMap();
       addMapControl();
    }
    function addMapControl(){
	var ctrl_ove = new BMap.OverviewMapControl({anchor:BMAP_ANCHOR_BOTTOM_RIGHT,isOpen:1});
	map.addControl(ctrl_ove); 
	var ctrl_sca = new BMap.ScaleControl({anchor:BMAP_ANCHOR_BOTTOM_LEFT});
	map.addControl(ctrl_sca);
    }
    
    Date.prototype.format = function(format)
    {
     var o = {
     "M+" : this.getMonth()+1, //month
     "d+" : this.getDate(),    //day
     "h+" : this.getHours(),   //hour
     "m+" : this.getMinutes(), //minute
     "s+" : this.getSeconds(), //second
     "q+" : Math.floor((this.getMonth()+3)/3),  
     "S" : this.getMilliseconds() //millisecond
     }
     if(/(y+)/.test(format)) format=format.replace(RegExp.$1,
     (this.getFullYear()+"").substr(4 - RegExp.$1.length));
     for(var k in o)if(new RegExp("("+ k +")").test(format))
     format = format.replace(RegExp.$1,
     RegExp.$1.length==1 ? o[k] :
     ("00"+ o[k]).substr((""+ o[k]).length));
     return format;
    }
</script>
</body>
</html>
