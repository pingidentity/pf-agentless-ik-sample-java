<%@ page language = "java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import = "com.pingidentity.referenceid.util.ReferenceIdAdapterResponse" %>
<%@ page import = "com.pingidentity.sample.configuration.ConfigurationManager" %>
<%@ page import = "com.pingidentity.sample.configuration.IdpSampleConfiguration" %>
<%@ page import = "com.pingidentity.sample.configuration.IdpSampleConstants" %>
<%@ page import = "com.pingidentity.sample.util.URLUtil" %>
<%@ page import = "java.util.Properties" %>
<%@ page import = "java.util.List" %>
<%@ page import = "java.util.Map.Entry" %>
<%@ page import = "org.apache.commons.codec.binary.Base64" %>
<%@ page import = "org.apache.commons.lang3.StringUtils" %>
<%@ page import = "org.json.JSONObject" %>

<%
	IdpSampleConfiguration idpSampleConfiguration = ConfigurationManager.getInstance().getIdpSampleConfiguration();
	Properties idpAdapterConfiguration = idpSampleConfiguration.getIdpAdapterConfiguration();

	String basePfUrl = idpAdapterConfiguration.getProperty(IdpSampleConstants.IDP_ADAPTER_CONF_BASE_PF_URL);
	String username = idpAdapterConfiguration.getProperty(IdpSampleConstants.IDP_ADAPTER_CONF_USERNAME);
	String password = idpAdapterConfiguration.getProperty(IdpSampleConstants.IDP_ADAPTER_CONF_PASSPHRASE);
	String idpAdapterId = idpAdapterConfiguration.getProperty(IdpSampleConstants.IDP_ADAPTER_CONF_ADAPTER_ID);
	String outgoingAttributeFormat = idpAdapterConfiguration.getProperty(
			IdpSampleConstants.IDP_ADAPTER_CONF_OUTGOING_ATTRIBUTE_FORMAT);
	
	String rawRequest = "";
	String referenceId = "";

	HttpSession httpSession = request.getSession(false);

	if (httpSession != null && httpSession.getAttribute(IdpSampleConstants.LOGIN_KEY_PREFIX +
	    IdpSampleConstants.REF_ID_ADAPTER_REFERENCE) != null)
	{
	    referenceId = (String) httpSession.getAttribute(IdpSampleConstants.LOGIN_KEY_PREFIX +
	        IdpSampleConstants.REF_ID_ADAPTER_REFERENCE);
	    httpSession.removeAttribute(IdpSampleConstants.LOGIN_KEY_PREFIX + IdpSampleConstants.REF_ID_ADAPTER_REFERENCE);
	}
	
	if (StringUtils.isNotBlank(referenceId))
	{
		StringBuilder rawRequestBuilder = new StringBuilder();

		rawRequestBuilder.append("GET " + 
								 basePfUrl + 
								 IdpSampleConstants.REF_ID_ADAPTER_PICKUP_ENDPOINT + 
								 "?" +
								 IdpSampleConstants.REF_ID_ADAPTER_REFERENCE +
								 "=" +
								 referenceId + 
								 "\n");		
		rawRequestBuilder.append(IdpSampleConstants.REF_ID_ADAPTER_INSTANCE_ID + ": " + idpAdapterId + "\n");		

		String basicAuth = username + ":" + password;
		rawRequestBuilder.append("Authorization" + ": " + Base64.encodeBase64String(basicAuth.getBytes()));				
		
		rawRequest = rawRequestBuilder.toString();
	}
										
	String attributes = "";
	String rawResponse = "";

	ReferenceIdAdapterResponse pickupResponse = null;

	if (httpSession != null && httpSession.getAttribute(IdpSampleConstants.LOGIN_KEY_PREFIX +
	    IdpSampleConstants.REF_ID_ADAPTER_RESPONSE) != null)
	{
	    pickupResponse =
			(ReferenceIdAdapterResponse) httpSession.getAttribute(IdpSampleConstants.LOGIN_KEY_PREFIX +
			    IdpSampleConstants.REF_ID_ADAPTER_RESPONSE);
		httpSession.removeAttribute(IdpSampleConstants.LOGIN_KEY_PREFIX + IdpSampleConstants.REF_ID_ADAPTER_RESPONSE);
	}

	if (pickupResponse != null)
	{	
		if (StringUtils.isNotBlank(pickupResponse.getResponseBody()))
		{
			StringBuilder rawResponseBuilder = new StringBuilder();

			rawResponseBuilder.append("HTTP/1.1 " + 
									  pickupResponse.getResponseCode() + 
									  " " + 
									  pickupResponse.getResponseMessage() + "\n");
			
			for (Entry<String, List<String>> entry : pickupResponse.getResponseHeaders().entrySet())
			{
				String key = entry.getKey();
				String value = StringUtils.join(entry.getValue(), " ");

				if (StringUtils.isNotBlank(key) && StringUtils.isNotBlank(value))
				{									
					rawResponseBuilder.append(entry.getKey() + ": " + value + "\n");
				}
			}
			
			rawResponseBuilder.append("\n");

			attributes = pickupResponse.getResponseBody();

			if (outgoingAttributeFormat.equals(IdpSampleConstants.IDP_ADAPTER_CONF_OUTGOING_ATTRIBUTE_FORMAT_JSON))
			{
				attributes = new JSONObject(pickupResponse.getResponseBody()).toString(4);
			}

			rawResponseBuilder.append(attributes);	  
			
			rawResponse = rawResponseBuilder.toString();
		}	
	}
%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link media="screen" href="<%=request.getContextPath()%>/css/main.css" type="text/css" rel="stylesheet">
        <link href="<%=request.getContextPath()%>/images/favicon.ico?" rel="shortcut icon" type="image/x-icon">
        <script src="<%= request.getContextPath() %>/js/common.js"></script>
        <title>Agentless Integration Kit Sample IdP</title>
    </head>
    <body>
        <div class="body-container">
            <div class="ping-body-container">
<%
	if (referenceId != null && pickupResponse != null) 
	{
%>
                <div class="header-block">
                    <div class="header">Identity Provider Sample</div>
                    <div class="sub-header">Pick Up User-Session Attributes</div>
                    <div class="left-header">
                    	<a href="<%= URLUtil.getConfigurationURL(request) %>">Configuration</a>
                    </div>
                </div>
<%	
	}
	else
	{
%>
                <div class="header-block">
                    <div class="header">Identity Provider Sample</div>
                    <div class="sub-header">Sign On</div>
                    <div class="left-header">
                    	<a href="<%= URLUtil.getConfigurationURL(request) %>">Configuration</a>
                    </div>
                </div>
<%            			
	}

	String errorMessage = request.getParameter(IdpSampleConstants.ERROR);

	if (StringUtils.isNotBlank(errorMessage))
	{
%>
				<div class="login-failure"><%= errorMessage %></div>
<%
	}
%>
                <div class="column">
<%
	if (referenceId != null && pickupResponse != null)
	{		  
%>
					<div class="row protocol-container">
						<div class="column request-container">
							<p class="text-label">Reference ID IdP Adapter Request</p>
							<p class="text-value"><%="GET " + IdpSampleConstants.REF_ID_ADAPTER_PICKUP_ENDPOINT %></p>
							<p>
								<a id="show_raw_request_link" 
								   onclick="javascript:toggleShowRaw('raw_request_textarea', 'show_raw_request_link');">
								   	Show raw
							   	</a>
							</p>
							<p>
								<textarea id="raw_request_textarea" 
										  class="code-view" 
										  rows="5" 
										  cols="60" 
										  style="display:none" 
										  readonly><%= rawRequest %></textarea>
							</p>
							<p class="text-label">Reference ID</p>
							<p class="text-value"><%=referenceId%></p>
						</div>
						<div class="column response-container">
							<p class="text-label">Reference ID IdP Adapter Response</p>
							<p class="text-value">
								<%="HTTP Status: " + 
									pickupResponse.getResponseCode() + 
									" " + 
									pickupResponse.getResponseMessage() + 
									"\n"
								%>
							</p>
							<p>
								<a id="show_raw_response_link" 
								   onclick="javascript:toggleShowRaw('raw_response_textarea', 'show_raw_response_link');">
								   	Show raw
							   	</a>
							</p>
							<p>
								<textarea id="raw_response_textarea" 
										  class="code-view" 
										  rows="20" 
										  cols="60" 
										  style="display:none" 
										  readonly><%= rawResponse %></textarea>
							</p>
							<p class="text-label">User-Session Attributes</p>
							<textarea id="attributes_textarea" 
									  class="code-view" 
									  rows="10" 
									  cols="60" 
									  readonly><%= attributes %></textarea>
						</div>
					</div>
					<div class="header sign-on-header">Sign On</div>
<%
	}
%>
					<div class="row">
						<div class="login-container">
							<form method="post" 
								  action="<%= request.getContextPath() + 
								  			  IdpSampleConstants.APP_PATH + 
								  			  "?" + 
								  			  IdpSampleConstants.ACTION_PARAM + 
								  			  "=" + IdpSampleConstants.ACTION_PARAM_LOGIN%>">
								<div class="ping-input">
									<label class="ping-input-label">Username</label>
									<div class="tooltip">
										<input type="text" 
											   id="username_input" 
											   name="<%= IdpSampleConstants.USERNAME %>" 
											   autofocus>
										<span class="tooltiptext tooltip-right">
											<%=
												"The IdP application's default users are \"joe\", \"sarah\" and \"scott\". " +
												"They share a default password of \"test\". " +
												"These values can be configured in the " + 
												"{idpApplicationRoot}/configuration/idp-sample-users.json file."
											%>
										</span>
									</div>
								</div>
								<div class="ping-input">
									<label class="ping-input-label">Password</label>
									<input type="password" id="password_input" name="<%= IdpSampleConstants.PASSWORD %>">
								</div>
								<div class="login-button-container">
									<input id="submit_button" class="table-button" type="submit" value="Sign On"/>
								</div>
							</form>
						</div>
					</div>
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
