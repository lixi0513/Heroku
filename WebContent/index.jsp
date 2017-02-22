


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>       
    <title>MediaSense Search and Play Login</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<link rel="stylesheet" type="text/css" media="screen" href="jqGrid/css/ui.jqgrid.css">
	<link rel="stylesheet" type="text/css" media="screen" href="css/cupertino/jquery-ui-1.8.16.custom.css">
	<link rel="stylesheet" type="text/css" media="screen" href="css/view.css">
	<script type="text/javascript" src="js/jquery-1.7.2.min.js"></script>
	<script>
$(document).ready( function () {
  $("input:text, input:password").focus(function () { });//attach the focus event

  $('input[autofocus]').trigger('focus');//force fire it on the autofocus element
  
  $("#loggyForm").submit(function(){
	var passString = $("#passw0rd").val();
	if ( passString.substring(0, 1) == " " )
	{
		$("#errorMessage").html("Login Failed");
		return false;
	}
  });
  
});
</script>

<!--[if !IE 7]>
	<style type="text/css">
		#wrap {display:table;height:100%}
	</style>
<![endif]-->
   
	
  </head>
  <style type="text/css">
  
  #login_form
  {
  border: 1px solid;
  text-align: center;
  margin: 0 auto;
  width:300px;
  
  }
  
  
  label
  {
  font-weight: bold;
  
  }
  
  #user
  {
  margin: 20px auto 10px auto;
  }
  #pass
  {
  margin: 10px auto 0px auto;
  }
  #submit_button
  {
  margin: 0px auto 10px auto;
  border: 3px double;
  }
  
  
  /*#user
  {
  position: absolute;
  left: 20px;
  top: 20px;
  }
  #pass
  {
  position: absolute;
  left: 20px;
  top: 60px;
  }
  #submit_button
  {
  position: absolute;
  left: 120px;
  top: 100px;
  }
  */
 /* #login_caption
  {
  position: absolute;
  left: 500px;
  top: 170px;
  }
  #caption
 {
 color: blue;
 font-size: 30px;
 font-weight: bold;
 position: absolute;
 left: 500px;
 }
 */
 
  #login_caption
  {
  margin: 170px auto 0px auto;
  font-weight: bold;
  }
 
  #caption
 {
 color: blue;
 font-size: 30px;
 font-weight: bold;
 text-align: center;
 }
 
 
 /*#errorMessage 
 {
 	position: absolute;
 	 left: 45px;
 	top : 130px;
 }
 */
 #errorMessage 
 {
 color : red;
  }
 
  </style>
  
  <body>
  
  
<div id="wrap">

	<div id="main">
  <div style="width: 389px; margin: 0px auto 20px; align='center'; "><img src="images/MediaSense.png" width="389" height="194" alt="MediaSense" /></div>
  <div style="width: 183px; margin: 0px auto 40px; align='center'; position: relative; top: -140px;"><img src="images/snp.png" width="183" height="50" alt="MediaSense" /></div>
  
     
  
  
  <div id="login_form">
  
  <form class="ui-widget" name="login_form" action="servlet/LoginServlet" method="post" id="loggyForm">
	<fieldset>
		<legend>Login Details</legend>
		<div  id="user"> <label>MediaSense Username:</label><br> <input style="border:1px solid;height: 20px;width:150px;" type="text" name="username" autofocus=”true”><br><br></div>
		<div id="pass"><label>MediaSense Password:</label> <br><input style="border:1px solid;height: 20px;width:150px;" type="password" name="password" id="passw0rd"><br><br></div>
		<center><input id="submit_button" type="submit" value="Login"></center>
		<div id="errorMessage"><label></label></div>
	</fieldset>
    </form>
    
 </div>
	</div>

</div>

<!-- <div id="footer">
  <div style="width: 100%; height: 50px; margin: 0px 0; padding-top: 8px; background: url(images/spotlight.png) repeat-x center left;">
  <span></span>
  <img src="images/SpinSciLogo.png" width="128" height="42" alt="" style="margin-top: 8px; margin-left: 20%; vertical-align: middle "/>
  <span></span>
  <img src="images/CiscoClear.png" width="" height="" alt="" style="margin-right: 20%; margin-top: 8px; float:right;"/>
  </div> 
</div>  -->
		

  </body>
</html>
