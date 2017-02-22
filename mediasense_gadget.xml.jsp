<%@ page contentType="text/xml; charset=iso-8859-1" language="java"  %>
<%
 String hostName=request.getServerName();
%>
<?xml version="1.0" encoding="UTF-8"?>
<Module>
	<ModulePrefs title="MediaSense Search and Play" height="700">
	
	</ModulePrefs>
			
		

	<Content type="html"> <![CDATA[



<html>
    <head>
        <title>MediaSense Search and Play</title>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
        <style type="text/css">

body {
    margin: 0px;
    padding: 0px;
}

/* iframe's parent node */
div#root {
    position: fixed;
    width: 100%;
    height: 100%;
}

/* iframe itself */
div#root > iframe {
    display: block;
    width: 100%;
    height: 100%;
    border: none;
    overflow: hidden;
}
</style>
        
        
    </head>
    <body>
        <div id="root">
		<iframe src="http://<%=hostName%>/Mediasense/index.jsp">
			iFrames are not supported by your browser.
		</iframe>
        </div>
    </body>
</html>



		]]>
	</Content>
</Module>


