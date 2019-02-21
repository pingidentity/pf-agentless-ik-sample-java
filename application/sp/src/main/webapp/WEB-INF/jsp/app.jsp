<%@ page language = "java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import = "org.apache.commons.codec.binary.Base64" %>
<%@ page import = "com.pingidentity.referenceid.util.ReferenceIdAdapterResponse" %>
<%@ page import = "com.pingidentity.sample.util.URLUtil" %>
<%@ page import = "com.pingidentity.sample.configuration.ConfigurationManager" %>
<%@ page import = "com.pingidentity.sample.configuration.SpSampleConfiguration" %>
<%@ page import = "com.pingidentity.sample.configuration.SpSampleConstants" %>
<%@ page import = "java.util.Properties" %>
<%@ page import = "java.util.List" %>
<%@ page import = "java.util.Map.Entry" %>
<%@ page import = "org.apache.commons.lang3.StringUtils" %>
<%@ page import = "org.json.JSONObject" %>


<%	
	SpSampleConfiguration spSampleConfiguration = ConfigurationManager.getInstance().getSpSampleConfiguration();
	Properties spAdapterConfiguration = spSampleConfiguration.getSpAdapterConfiguration();

	String basePfUrl = spAdapterConfiguration.getProperty(SpSampleConstants.SP_ADAPTER_CONF_BASE_PF_URL);
	String username = spAdapterConfiguration.getProperty(SpSampleConstants.SP_ADAPTER_CONF_USERNAME);
	String password = spAdapterConfiguration.getProperty(SpSampleConstants.SP_ADAPTER_CONF_PASSPHRASE);
	String spAdapterId = spAdapterConfiguration.getProperty(SpSampleConstants.SP_ADAPTER_CONF_ADAPTER_ID);
	String outgoingAttributeFormat = spAdapterConfiguration.getProperty(
			SpSampleConstants.SP_ADAPTER_CONF_OUTGOING_ATTRIBUTE_FORMAT);
	String targetURL = spAdapterConfiguration.getProperty(SpSampleConstants.SP_ADAPTER_CONF_TARGET_URL);
	String partnerEntityId = spAdapterConfiguration.getProperty(SpSampleConstants.SP_ADAPTER_CONF_PARTNER_ENTITY_ID);

	HttpSession httpSession = request.getSession(false);

	String referenceId = "";
	ReferenceIdAdapterResponse pickupResponse = null;

	if (httpSession != null && httpSession.getAttribute(SpSampleConstants.APP_KEY_PREFIX +
	    SpSampleConstants.REF_ID_ADAPTER_REFERENCE) != null)
	{
		referenceId = (String) httpSession.getAttribute(SpSampleConstants.APP_KEY_PREFIX +
		    SpSampleConstants.REF_ID_ADAPTER_REFERENCE);
		httpSession.removeAttribute(SpSampleConstants.APP_KEY_PREFIX +
		    SpSampleConstants.REF_ID_ADAPTER_REFERENCE);
	}

	if (httpSession != null && httpSession.getAttribute(SpSampleConstants.APP_KEY_PREFIX +
	    SpSampleConstants.REF_ID_ADAPTER_RESPONSE) != null)
	{
	    pickupResponse =
	        (ReferenceIdAdapterResponse) httpSession.getAttribute(SpSampleConstants.APP_KEY_PREFIX +
	            SpSampleConstants.REF_ID_ADAPTER_RESPONSE);
	    httpSession.removeAttribute(SpSampleConstants.APP_KEY_PREFIX +
	        SpSampleConstants.REF_ID_ADAPTER_RESPONSE);
	}

	String attributes = "";

	if (StringUtils.isNotBlank(referenceId) && pickupResponse != null)
	{
		if (StringUtils.isNotBlank(pickupResponse.getResponseBody()))
		{
			attributes = pickupResponse.getResponseBody();

			if (outgoingAttributeFormat.equals(SpSampleConstants.SP_ADAPTER_CONF_OUTGOING_ATTRIBUTE_FORMAT_JSON))
			{
				attributes = new JSONObject(attributes).toString(4);
			}
		}	
	}

	StringBuilder rawRequestBuilder = new StringBuilder();
	String rawRequest = "";
	
	if (StringUtils.isNotBlank(referenceId))
	{
		rawRequestBuilder.append("GET " + 
								 basePfUrl + 
								 SpSampleConstants.REF_ID_ADAPTER_PICKUP_ENDPOINT + 
								 "?REF=" + 
								 referenceId + 
								 "\n");		
		rawRequestBuilder.append(SpSampleConstants.REF_ID_ADAPTER_INSTANCE_ID + ": " + spAdapterId + "\n");		

		String basicAuth = username + ":" + password;
		rawRequestBuilder.append("Authorization" + ": " + Base64.encodeBase64String(basicAuth.getBytes()));				

		rawRequest = rawRequestBuilder.toString();
	}

	StringBuilder rawResponseBuilder = new StringBuilder();
	String httpResponse = "";
	String rawResponse = "";
	
	if (pickupResponse != null) 
	{
		httpResponse = "HTTP/1.1 " + pickupResponse.getResponseCode() + " " + pickupResponse.getResponseMessage() + "\n";

		rawResponseBuilder.append(httpResponse);

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
		rawResponseBuilder.append(attributes);
		
		rawResponse = rawResponseBuilder.toString();
	}
%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link media="screen" href="<%=request.getContextPath()%>/css/main.css" type="text/css" rel="stylesheet">
        <link href="<%=request.getContextPath()%>/images/favicon.ico?" rel="shortcut icon" type="image/x-icon">
        <script src="<%= request.getContextPath() %>/js/common.js"></script>
        <title>Agentless Integration Kit Sample SP</title>
    </head>
    <body>
        <div class="body-container">
            <div class="ping-body-container">
                <div class="header-block">
                    <div class="header">Service Provider Sample</div>
<%
	if (StringUtils.isNotBlank(referenceId) && pickupResponse != null)
	{
%>
                    <div class="sub-header">Pick Up User-Session Attributes</div>
<%              			
	}
	else 
	{
%>
                    <div class="sub-header">Single Sign-On</div>
<%              			
	}
%>
                    <div class="left-header">
                    	<a href="<%= URLUtil.getConfigurationURL(request) %>">Configuration</a>
                    </div>
                </div>
				<div class="app-container">
<%
	if (StringUtils.isNotBlank(referenceId) && pickupResponse != null)
	{
%>
					<div class="row">
						<div class="column request-container">                    
							<p class="text-label">Reference ID SP Adapter Request</p>                	
							<p class="text-value"><%="GET " + SpSampleConstants.REF_ID_ADAPTER_PICKUP_ENDPOINT %></p>                	
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
							<p class="text-label">Reference ID SP Adapter Response</p>
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
									  rows="15" 
									  cols="60" 
									  readonly><%= attributes %></textarea>
						   </div>
					</div>
					<div class="bottom-button-container" style="padding-top:30px">
						<form method="POST" 
							  action="<%= URLUtil.getStartSLOURL(basePfUrl, 
									  							 SpSampleConstants.START_SP_SLO_ENDPOINT, 
									  							 URLUtil.getLoggedOutURL(request))%>">
							<input class="table-button" 
								   id="slo_button" 
								   name="slo_button" 
								   type="submit" 
								   value="Single Logout"/>
						</form>
					</div>
					<div class="ping-link-fragment"><%= SpSampleConstants.START_SP_SLO_ENDPOINT %></div>
					<div class="start-over-link-container">
						<a href=<%= URLUtil.getIdpSampleAppURL(request) %>><%= "< Start Over" %></a>
					</div>
<%              			
	}
	else 
	{
%>
					<form action=<%= URLUtil.getStartSSOURL(basePfUrl, 
															SpSampleConstants.START_SP_SSO_ENDPOINT, 
															partnerEntityId, 
															targetURL)%>>
						<div class="sso-button-container">
							<input class="table-button "id="sso_button" type="submit" value="Single Sign-On"/>
						</div>
					</form>
					<div class="ping-link-fragment">
						<%= SpSampleConstants.START_SP_SSO_ENDPOINT %>
					</div>
<%           			
	}
%>
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