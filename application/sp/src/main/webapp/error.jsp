<%@ page language = "java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isErrorPage="true" import="java.io.*" %>
<%@ page import = "org.apache.commons.logging.Log"%>
<%@ page import = "org.apache.commons.logging.LogFactory"%>
<%@ page import = "com.pingidentity.sample.util.URLUtil" %>

<%
    Log logger = LogFactory.getLog(this.getClass());
    logger.error("An internal server error has occurred:", exception);
%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link media="screen" href="<%=request.getContextPath()%>/css/main.css" type="text/css" rel="stylesheet">
        <link href="<%=request.getContextPath()%>/images/favicon.ico?" rel="shortcut icon" type="image/x-icon">
        <title>Agentless Integration Kit Sample SP</title>
    </head>
    <body>
        <div class="body-container">
            <div class="ping-body-container">
                <div class="header-block">
                    <div class="header">Error</div>
                    <div class="left-header">
                    	<a href="<%= URLUtil.getConfigurationURL(request) %>">Configuration</a>
                    </div>
                    <div class="right-header">
                    	<a href=<%= URLUtil.getSpSampleAppURL(request) %>>Back to SP</a>
                    </div>
                </div>
                <div class="ping-messages ping-nopad">
					<p>Oops</p>
					<p>
						<%= "Looks like something is not right. " + 
							"Please ensure your configuration is correct and check the server logs for more information." 
						%>
					</p>
					<p class="ping-note-text">500 Internal Server Error</p>
                </div>
			</div>
            <div class="ping-footer-container">
                <div class="ping-footer">
                    <div class="ping-credits"></div>
                    <div class="ping-copyright">
                    	Copyright © 2003-2019. Ping Identity Corporation. All rights reserved.
					</div>
                </div>
            </div>
        </div>
    </body>
</html>
