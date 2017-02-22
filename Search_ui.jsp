<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>Home</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
<style type="text/css">
body
{
color: white;
font-weight:bold;
font-size: 15px;
font-family: cursive;
background-image: url("images/air-balloon-gradient.jpg") ;
background-size: 100%;
}
 #main
  {
  position:absolute;
  height:400px;
  width:400px;
  left:70px;
  top: 100px;
  bgcolor: #707070;
  border: 0.5em solid #357EC7;
  
  }
 #agent{
  position:absolute;
  left:10px;
  width: 360 px;
  height:70px;
  top:30px;
  color:white;
  overflow: auto;
  }
  
  .image{
  height: 30px;
  width: 30px;
  padding-bottom:0.5em;
  }
  #image_div
  {
  position:relative;
  top: 0px;
  left:10px;
  text-align:center;
  width: 30px;
  font-size: 10px;
  }
  #table_agent{
  background-color: #F9966B;
  height:190px;
  width:390px;
  position:relative;
  left:5px;
  top:90px;
  overflow:auto;
  font-size: 5px;
  }
  #player
  {
  background-image: url("image/media_player.jpg");
  background-size: 100%;
  border: 0.5em solid #357EC7;
  
  height:300px;
  width:300px;
  position:absolute;
  left:600px;
  top:100px;
  }
  #data
  {
  height:10px;
  width:10px;
  color:black;
  }
 #call_details
 {
 width: 250px;
font-size: 12px;
font-family: cursive;
 color:white;
 background-color: #663333;
 position:relative;
 left:500px;
 top:100px;
 }
 a{
 color: white;
 text-decoration: none;
 }
 #header
 {
 color: blue;
 font-family: fantacy;
 font-size: 30px;
 font-weight: bold;
 position: absolute;
 left: 500px;
 }
 #like
 {
 position:relative;
 width:10px;
 left: 600px;
 top: 420px;
 }
 #tag
 {
 position: relative;
 left: 650px;
 top: 420px;
 }
 #comment
 {
 height: 30px;
 width: 70px;
 position: relative;
 width: 50px;
 left: 700px;
 top: 420px;
 }
 #comment_box
 {
 position: relative;
 left: 650px;
 top: 450px;
 }
 #comment_messages
 {
 position: relative;
 left: 650px;
 top: 430px;
 }
 #options
 {
 position: relative;
 top:60px;
 left:10px;
 }
 #options_tag_close
 {
 position: absolute;
 left:350px;
 top:0px
 }
 #options_tag_open
 {
 position: absolute;
 left:350px;
 top:0px
 }
 #agent_details
 {
 font-size:13px;
 }
 #live
 {
 position: absolute;
 left: 220px;
 top: 0px;
 }
 #play
 {
 position: relative;
 left: 950px;
 top: 100px;
 }
 #search_label
 {
 width: 60px;
 align: center;
 text-align: center;
 position: absolute;
 left: 170px;
 top: 25px;
 }
 #vlc_player
 {
 height: 300px;
 width:300px;
 }
  </style>
  </head>
  <script type="text/javascript" src="js/jquery-1.6.1.js"></script>
  <script type="text/javascript">
	$value="";
	$username="";
	$state_select="";
	
	var devie_index;
	function setValue(data,username)
	{
	if(!data=="000")
	{
	$value=data;
	$username=username;
	}
	else
	{
	$value=document.getElementById("search_text").value;
	$username="";
	}
	}
	function setalert(value)
	{
	device_index=value;
	
	}
	function getAlertValue()
	{
	return device_index;
	}
	
	$(document).ready(function() {
	setTimeout("document.agent.reload()",3000);
	var jsondata;
	$("#id_image").hide();
	$("#options_tag_close").hide();;
	$("#player").hide();
	$("#like").hide();
	$("#tag").hide();
	$("#comment").hide();
	$("#comment_box").hide();
	$("#options").hide();
	$("#id_search_ui").hide();
	$("#options_tag_open").click(function()
	{
	$("#options_tag_open").hide();
	$("#options_tag_close").show();
	
	$("#options").show("slide");
	});
	$("#options_tag_close").click(function()
	{
	$("#options_tag_open").show();
	$("#options_tag_close").hide();
	$("#options").hide("slide");
	});
	$(".image").click(function(){
	
	
	$("#agent_details").html("");
			//$value = document.getElementById("value").value;
			$offset = "";
			if($offset == "")
			{
			$offset=0;
			}
			$limit = document.getElementById("call_recordings").value;
			
			if($limit == "")
			{
			$limit=30;
			}
			
			$.post("servlet/GetSessions", {value:$value,offset:$offset,limit:$limit}, function(data) 
			{
		 	jsondata=eval(data);
		 	var i=0;
		 	var itr=0;
		 	if(jsondata.code=="FOUND")
		 	{
		 	$("#agent_details").append("<tr><th>ID</th><th>Name</th><th>Date</th><th>State</th></tr>");
    	 	$(jsondata.root).each(function()
    			{
    			
    			$("#agent_details").append("<tr><td><label id='data' onclick='setalert("+i+");'>"+jsondata.root[i].deviceid+"</label>&nbsp;</td><td>"+$username+"</td><td>"+jsondata.root[i].Date+"</td><td>"+jsondata.root[i].sessionstate+"</td></tr>");
    			itr=itr+1;
    			i=i+1;
    			});
    			}
    		else
    		{
    		$("#agent_details").append("no data for selected device");
    		}
    			
			});
			
		});
		
		$("#active").click(function()
		{
		$("#agent_details").html("");
		
		var i=0;
		if(jsondata.root[i].sessionstate=="ACTIVE"){	
		$(jsondata.root).each(function()
    			{   	
    				
    			 $("#agent_details").append("<tr><td><label id='data' onclick='setalert("+i+");'>"+jsondata.root[i].deviceid+"</label>&nbsp;</td><td>"+$username+"</td><td>"+jsondata.root[i].Date+"</td><td>"+jsondata.root[i].sessionstate+"</td></tr>");
    			 
    			 
    			
    			i=i+1;	
    			});
    			}
    			else
    			 {
    			  $("#agent_details").append("no active call for selected device");
    			 }
		});
		$("#closed").click(function()
		{
		$("#agent_details").html("");
		var i=0;
		$("#agent_details").append("<tr><th>ID</th><th>Name</th><th>Date</th><th>State</th></tr>");
		if(jsondata.root[i].sessionstate=="CLOSED_NORMAL"){		
		$(jsondata.root).each(function()
    			{   	
    			
    			 $("#agent_details").append("<tr><td><label id='data' onclick='setalert("+i+");'>"+jsondata.root[i].deviceid+"</label></td><td>"+$username+"</td><td>"+jsondata.root[i].Date+"</td><td>"+jsondata.root[i].sessionstate+"</td></tr>");
    			i=i+1;	
    			});
    			}
    			else
    			 {
    			  $("#agent_details").append("no active call for selected device");
    			 }
		});
		$("#agent_details").click(function()
			{
			
			$("#player").show("slide");
			var value=getAlertValue();
			if(value!=null){
			
			
			//$("#player").html("<embed type='application/x-vlc-plugin' name='VLC' autoplay='yes' loop='no' volume='100' width='300' height='300' target='"+jsondata.root[value].url+"'></embed>");
			$("#player").html("<a href='"+jsondata.root[value].url+"'><img id='vlc_player' src='http://www.ht.undp.org/_workshop/media%20player%20logo.jpg'/></a>");
			$("#like").show();
			$("#tag").show();
			$("#comment").show();
			}
		});
			
		$("#live").click(function(){
		$("#options_tag_close").click(function()
	{
	alert("options unavailable for live monitoring");
	$("#options").hide();
	});
	$("#options_tag_open").click(function()
	{
	alert("options unavailable for live monitoring");
	$("#options").hide();
	});
		$offset = "";
			if($offset == "")
			{
			$offset=0;
			}
			$limit = document.getElementById("call_recordings").value;
			
			if($limit == "")
			{
			$limit=30;
			}
		$.post("servlet/LiveMonitoring", {offset:$offset,limit:$limit}, function(data){
		
		
		jsondata=eval(data);
		if(jsondata.code=="NOT_FOUND")
		{
		 $("#agent_details").html("no active calls for monitoring");
		}
		else
		{
		var itr=0;
		 var i=0;
		 $("#agent_details").html("");
		 	$("#agent_details").append("<tr><th>ID</th><th>Name</th><th>Date</th><th>State</th></tr>");
    	 	$(jsondata.root).each(function()
    		{   
    			$("#agent_details").append("<tr><td><label id='data' onclick='setalert("+i+");'>"+jsondata.root[i].deviceid+"</label>&nbsp;</td><td>"+$username+"</td><td>"+jsondata.root[i].Date+"</td><td>"+jsondata.root[i].sessionstate+"</td></tr>");
    			itr=itr+1;
    			i=i+1;
    		});
		}	
		
			
		});
		});
		
		$("#comment").click(function(){
		$("#comment_box").show("slide");
		});
		$("#comment_button").click(function(){
		var comments=document.getElementById("comments_textbox").value;
		
		$("#comment_messages").append(comments+"<br><br>");
		$("#comment_messages").show();
		});
		$("#id_search").click(function(){
		$("#id_search").hide();
		$("#id_image").show();
	$("#agent").hide("slide");
	$("#id_search_ui").show("slide");
	});	
	$("#id_image").click(function()
	{
	$("#id_search_ui").hide();
	$("#agent").show("slide");
	$("#id_image").hide();
	$("#id_search").show();
	});
	$("#start_stream").click(function(){
	$.post("servlet/StartStreaming",{device_id:"1988",media_type:"AUDIO"},  function(data) 
			{});
	});
	$("#play_stream").click(function(){
	$.post("servlet/PlayStream", function(data) 
			{});
	});
	});
	
	</script>
  
  <body >
  <div id="header"> 
  SpinSci 
  </div>
  
  <div id="main">
  <div id="agent">
  <table>
  <tr>
 <td><div id="image_div"> <img id="1988"  class="image" onClick="setValue(1988,'surya');" title="1988" src="images/avatar100.png"/>1988</div></td>
 <td><div id="image_div"> <img id="9933"  class="image" onClick="setValue(9933,'rajit');" title="9933" src="images/avatar100.png"/>9933</div></td>
 <td><div id="image_div"> <img id="9999"  class="image" onClick="setValue(9999,'NA');" title="9999" src="images/avatar100.png"/>9999</div></td>
 <td><div id="image_div"> <img id="8659"  class="image" onClick="setValue(8659,'IVRPORT');" title="8659" src="images/avatar100.png"/>8659</div></td>
 <td><div id="image_div"> <img id="1003"  class="image" onClick="setValue(1003,'NA');" title="1003" src="images/avatar100.png"/>1003</div></td>
 <td><div id="image_div"> <img id="9911"  class="image" onClick="setValue(9911,'surya');" title="1003" src="images/avatar100.png"/>9911</div></td>
 
 <tr>
 </table>
  
    </div>
    <div id="id_search">Search by ID</div>
    <div id="id_image">Search by Image</div>
    <div id="id_search_ui">
    <input type="text" id="search_text" >
    <div id="search_label" class="image" onclick="setValue(000,'000')">Search</div>
    </div>
    <div id="live">Live Monitoring |</div>
    <div id="options_tag_open">Options</div>
    <div id="options_tag_close">Options</div>
    <div id="options">
    <input id="active" type="radio" value="active" name="state">active
    <input id="closed" type="radio" value="closed" name="state">closed<br>
   start date: <input type="text" name="date_from"><br>
   end date: <input type="text" name="date_to"><br> 
    call record: <select id="call_recordings" name="call_recordings">
    <option value="10">last 10 calls
    <option value="20">last 20 calls
    <option value="30">last 30 calls
    </select>
    </div>
    <div id="table_agent">
    <table id="agent_details">
    
    </table>
    </div>
    
  </div>
    <div id="call_details">
     
    </div>
   <div id="call_details_menu">
    <a id="like">Like</a>
    <a id="tag">Tag</a>
     <a id="comment">comment</a>
     <div id="play"></div>
    </div>
    <div id="comment_messages">
    
    </div>
    <div id="comment_box">
    <input type=text id="comments_textbox"></input>
   	<input id="comment_button" type="button" value="comment" ></input>
    </div>
    <div id="start_stream">start</div>
    <div id="play_stream">play</div>
    <div id="player">
    
    </div>
  </body>
</html>
