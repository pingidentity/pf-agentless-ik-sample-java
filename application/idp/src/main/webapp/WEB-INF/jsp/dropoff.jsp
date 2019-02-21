<%@ page language = "java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import = "com.pingidentity.referenceid.util.ReferenceIdAdapterResponse" %>
<%@ page import = "com.pingidentity.sample.configuration.ConfigurationManager" %>
<%@ page import = "com.pingidentity.sample.configuration.IdpSampleConfiguration" %>
<%@ page import = "com.pingidentity.sample.configuration.IdpSampleConstants" %>
<%@ page import = "com.pingidentity.sample.util.URLUtil" %>
<%@ page import = "java.nio.charset.StandardCharsets" %>
<%@ page import = "java.io.IOException" %>
<%@ page import = "java.util.List" %>
<%@ page import = "java.util.Properties" %>
<%@ page import = "java.io.StringReader" %>
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
	String incomingAttributeFormat = idpAdapterConfiguration.getProperty(
		IdpSampleConstants.IDP_ADAPTER_CONF_INCOMING_ATTRIBUTE_FORMAT);

	HttpSession httpSession = request.getSession(false);
	
	String rawRequest = "";
	String requestAttributes = "";

	if (httpSession != null && httpSession.getAttribute(IdpSampleConstants.DROPOFF_KEY_PREFIX +
	    IdpSampleConstants.REF_ID_ADAPTER_REQUEST_ATTRIBUTES) != null)
	{
	    requestAttributes = (String) httpSession.getAttribute(IdpSampleConstants.DROPOFF_KEY_PREFIX +
	        IdpSampleConstants.REF_ID_ADAPTER_REQUEST_ATTRIBUTES);
	    httpSession.removeAttribute(IdpSampleConstants.DROPOFF_KEY_PREFIX +
	        IdpSampleConstants.REF_ID_ADAPTER_REQUEST_ATTRIBUTES);
	}

	if (StringUtils.isNotBlank(requestAttributes)) 
	{
		if (incomingAttributeFormat.equals(IdpSampleConstants.IDP_ADAPTER_CONF_INCOMING_ATTRIBUTE_FORMAT_JSON))
		{
			requestAttributes = new JSONObject(requestAttributes).toString(4);
		}
		
		StringBuilder rawRequestBuilder = new StringBuilder();

		rawRequestBuilder.append("POST " + basePfUrl + IdpSampleConstants.REF_ID_ADAPTER_DROPOFF_ENDPOINT);
		if (incomingAttributeFormat.equals(IdpSampleConstants.IDP_ADAPTER_CONF_INCOMING_ATTRIBUTE_FORMAT_QUERY))
		{
			rawRequestBuilder.append("?" + requestAttributes + "\n");
		}
		else
		{
			rawRequestBuilder.append("\n");
		}

		rawRequestBuilder.append("Content-Length" + 
								 ": " + 
								 requestAttributes.getBytes(StandardCharsets.UTF_8).length + 
								 "\n");

		if (incomingAttributeFormat.equals(IdpSampleConstants.IDP_ADAPTER_CONF_INCOMING_ATTRIBUTE_FORMAT_JSON))
		{
			rawRequestBuilder.append("Content-Type" + ": " + "application/json" + "\n");
		}
		else
		{
			rawRequestBuilder.append("Content-Type" + ": " + "text/html; charset=utf-8" + "\n");
		}

		rawRequestBuilder.append(IdpSampleConstants.REF_ID_ADAPTER_INSTANCE_ID + ": " + idpAdapterId + "\n");
		String basicAuth = username + ":" + password;
		rawRequestBuilder.append("Authorization" + ": " + Base64.encodeBase64String(basicAuth.getBytes()) + "\n\n");
		
		if (incomingAttributeFormat.equals(IdpSampleConstants.IDP_ADAPTER_CONF_INCOMING_ATTRIBUTE_FORMAT_JSON))
		{
			rawRequestBuilder.append(requestAttributes);
		}

		rawRequest = rawRequestBuilder.toString();
	}
		
	ReferenceIdAdapterResponse dropoffResponse = null;

	if (httpSession != null && httpSession.getAttribute(IdpSampleConstants.DROPOFF_KEY_PREFIX +
	    IdpSampleConstants.REF_ID_ADAPTER_RESPONSE) != null)
	{
	    dropoffResponse = (ReferenceIdAdapterResponse) httpSession.getAttribute(IdpSampleConstants.DROPOFF_KEY_PREFIX +
	        IdpSampleConstants.REF_ID_ADAPTER_RESPONSE);
	    httpSession.removeAttribute(IdpSampleConstants.DROPOFF_KEY_PREFIX +
	        IdpSampleConstants.REF_ID_ADAPTER_RESPONSE);
	}

	String rawResponse = "";
	String httpStatus = "";
	String redirectURLPath = "";

	if (httpSession != null && httpSession.getAttribute(IdpSampleConstants.DROPOFF_KEY_PREFIX +
	    IdpSampleConstants.REF_ID_ADAPTER_REDIRECT_URL_PATH) != null)
	{
	    redirectURLPath = (String) httpSession.getAttribute(IdpSampleConstants.DROPOFF_KEY_PREFIX +
	        IdpSampleConstants.REF_ID_ADAPTER_REDIRECT_URL_PATH);
	    httpSession.removeAttribute(IdpSampleConstants.DROPOFF_KEY_PREFIX +
	        IdpSampleConstants.REF_ID_ADAPTER_REDIRECT_URL_PATH);
	}

	String referenceId = "";

	if (dropoffResponse != null)
	{
		StringBuilder rawResponseBuilder = new StringBuilder();

		httpStatus = "HTTP/1.1 " +
					 dropoffResponse.getResponseCode() +
					 " " + 
					 dropoffResponse.getResponseMessage() + 
					 "\n";

		rawResponseBuilder.append(httpStatus);
		
		for (Entry<String, List<String>> entry : dropoffResponse.getResponseHeaders().entrySet())
		{
			String key = entry.getKey();
			String value = StringUtils.join(entry.getValue(), " ");

			if (StringUtils.isNotBlank(key) && StringUtils.isNotBlank(value))
			{									
				rawResponseBuilder.append(entry.getKey() + ": " + value + "\n");
			}
		}
		
		rawResponseBuilder.append("\n");

		if (StringUtils.isNotBlank(dropoffResponse.getResponseBody()))
		{
			String attributes = dropoffResponse.getResponseBody();

			if (outgoingAttributeFormat.equals(IdpSampleConstants.IDP_ADAPTER_CONF_OUTGOING_ATTRIBUTE_FORMAT_JSON))
			{
				attributes = new JSONObject(attributes).toString(4);
			}

			rawResponseBuilder.append(attributes);
			
			if (outgoingAttributeFormat.equals(IdpSampleConstants.IDP_ADAPTER_CONF_OUTGOING_ATTRIBUTE_FORMAT_JSON))
			{
				JSONObject jsonObject = new JSONObject(attributes);
				referenceId = jsonObject.getString(IdpSampleConstants.REF_ID_ADAPTER_REFERENCE);
			}
			else 
			{
				Properties properties = new Properties();
	            StringReader stringReader = new StringReader(attributes);
				properties.load(stringReader);
				referenceId = properties.getProperty(IdpSampleConstants.REF_ID_ADAPTER_REFERENCE);
			}
		}	  
		
		rawResponse = rawResponseBuilder.toString();
	}
%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link media="screen" href="<%= request.getContextPath() %>/css/main.css" type="text/css" rel="stylesheet">
        <link href="<%= request.getContextPath() %>/images/favicon.ico?" rel="shortcut icon" type="image/x-icon">
        <script src="<%= request.getContextPath() %>/js/common.js"></script>
        <title>Agentless Integration Kit Sample IdP</title>
    </head>
    <body>
        <div class="body-container">
            <div class="ping-body-container">
                <div class="header-block">
                    <div class="header">Identity Provider Sample</div>
                    <div class="sub-header">Drop Off User-Session Attributes</div>
                    <div class="left-header">
                    	<a href="<%= URLUtil.getConfigurationURL(request) %>">Configuration</a>
                    </div>
                </div>
<%
	if (StringUtils.isNotBlank(referenceId) && dropoffResponse != null)
	{
%>
                <div class="row dropoff-container">
					<div class="column request-container">
						<p class="text-label">Reference ID IdP Adapter Request</p>                	
						<p class="text-value"><%="POST " + IdpSampleConstants.REF_ID_ADAPTER_DROPOFF_ENDPOINT %></p>                	
						<p>
							<a id="show_raw_request_link" 
							   onclick="javascript:toggleShowRaw('raw_request_textarea', 'show_raw_request_link');">
								   Show raw
							</a>
						</p>
						<p>
							<textarea id="raw_request_textarea" 
									  class="code-view" 
									  rows="15" 
									  cols="60" 
									  style="display:none" 
									  readonly><%= rawRequest %></textarea>
						</p>							
						<p class="text-label">User-Session Attributes</p>
						<p>
							<textarea id="request_attributes" 
									  class="code-view" 
									  rows="10" 
									  cols="60" 
									  readonly><%= requestAttributes %></textarea>
						</p>
					</div>
					<div class="column response-container">
						<p class="text-label">Reference ID IdP Adapter Response</p>                	
						<p class="text-value">
							<%= "HTTP Status: " + 
								dropoffResponse.getResponseCode() + 
								" " + 
								dropoffResponse.getResponseMessage()
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
									  rows="15" 
									  cols="60" 
									  style="display:none" 
									  readonly><%= rawResponse %></textarea>
						</p>
						<p class="text-label">Reference ID </p>                	
						<p class="text-value"><%= referenceId %></p>                	
					</div>
				</div>
				<div class="bottom-button-container">
					<form method="POST" 
						  action="<%= request.getContextPath() + 
										IdpSampleConstants.APP_PATH + 
										"?" + 
										IdpSampleConstants.ACTION_PARAM + 
										"=" + 
										IdpSampleConstants.ACTION_PARAM_RESUME %>">
						<input class="table-button" id="ok_button" name="submit" type="submit" value="OK"/>
					</form>
				</div>
<%              			
	}
	else
	{
%>
				<div class="solo-text-container">
					<p class="solo-text">State from the previous request has been invalidated. Please restart the flow.</p>
				</div>
<%
	}
%>
				<div class="ping-link-fragment"><%= redirectURLPath %></div>
				<div class="start-over-link-container">
					<a href="<%= URLUtil.getIdpSampleAppURL(request) %>"><%= "< Start Over" %></a>
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