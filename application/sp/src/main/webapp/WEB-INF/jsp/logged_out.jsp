<%@ page language = "java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import = "com.pingidentity.sample.configuration.SpSampleConstants" %>
<%@ page import = "com.pingidentity.sample.util.URLUtil" %>

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
                    <div class="header">Service Provider Sample</div>
                    <div class="sub-header">Application Logout</div>
                    <div class="left-header">
                    	<a href=<%= URLUtil.getSpSampleAppURL(request) %>>Back to SP</a>
                    </div>
                    <div class="right-header">
                    	<a href=<%= URLUtil.getIdpSampleAppURL(request) %>>Back to IdP</a>
                    </div>
                </div>
				<div class="logged-out-text-container">
					<p class="logged-out-text">You are now logged out.</p>
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