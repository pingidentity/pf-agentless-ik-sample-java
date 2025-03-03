<%@ page language = "java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import = "com.pingidentity.sample.configuration.ConfigurationManager" %>
<%@ page import = "com.pingidentity.sample.configuration.SpSampleConfiguration" %>
<%@ page import = "com.pingidentity.sample.configuration.SpSampleConstants" %>
<%@ page import = "com.pingidentity.sample.util.URLUtil" %>
<%@ page import = "java.util.Properties" %>
<%@ page import = "org.apache.http.entity.ContentType" %>
<%@ page import = "org.apache.commons.lang3.StringUtils" %>

<%	
	response.setHeader("Cache-Control","no-cache");
	response.setHeader("Pragma","no-cache");
	response.setHeader("Expires","0");

	SpSampleConfiguration spSampleConfiguration = ConfigurationManager.getInstance().getSpSampleConfiguration();
	Properties spAdapterConfiguration = spSampleConfiguration.getSpAdapterConfiguration();
		
	String basePfUrl = spAdapterConfiguration.getProperty(SpSampleConstants.SP_ADAPTER_CONF_BASE_PF_URL);
	String username = spAdapterConfiguration.getProperty(SpSampleConstants.SP_ADAPTER_CONF_USERNAME);
	String passphrase = spAdapterConfiguration.getProperty(SpSampleConstants.SP_ADAPTER_CONF_PASSPHRASE);
	boolean isChecked = spAdapterConfiguration.getProperty(SpSampleConstants.SP_ADAPTER_CONF_USE_BEARER_TOKEN_AUTH).equals("yes") ? true: false;
	String ccClientId = spAdapterConfiguration.getProperty(SpSampleConstants.SP_ADAPTER_CONF_CLIENT_CREDENTIALS_CLIENT_ID);
    String ccClientSecret = spAdapterConfiguration.getProperty(SpSampleConstants.SP_ADAPTER_CONF_CLIENT_CREDENTIALS_CLIENT_SECRET);
    String ccScope = spAdapterConfiguration.getProperty(SpSampleConstants.SP_ADAPTER_CONF_CLIENT_CREDENTIALS_FLOW_SCOPE);
	String adapterId = spAdapterConfiguration.getProperty(SpSampleConstants.SP_ADAPTER_CONF_ADAPTER_ID);
	String outgoingAttributeFormat = spAdapterConfiguration.getProperty(
			SpSampleConstants.SP_ADAPTER_CONF_OUTGOING_ATTRIBUTE_FORMAT);
	String otherOutgoingAttributeFormat = 
			SpSampleConstants.SP_ADAPTER_CONF_OUTGOING_ATTRIBUTE_FORMAT_JSON.equals(outgoingAttributeFormat) ? 
			SpSampleConstants.SP_ADAPTER_CONF_OUTGOING_ATTRIBUTE_FORMAT_PROPERTIES : 
			SpSampleConstants.SP_ADAPTER_CONF_OUTGOING_ATTRIBUTE_FORMAT_JSON;
	String targetURL = spAdapterConfiguration.getProperty(SpSampleConstants.SP_ADAPTER_CONF_TARGET_URL);
	String partnerEntityId = spAdapterConfiguration.getProperty(SpSampleConstants.SP_ADAPTER_CONF_PARTNER_ENTITY_ID);
%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link media="screen" href="<%=request.getContextPath()%>/css/main.css" type="text/css" rel="stylesheet">
        <link href="<%=request.getContextPath()%>/images/favicon.ico?" rel="shortcut icon" type="image/x-icon">
        <script src="<%= request.getContextPath() %>/js/common.js"></script>
        <title>Agentless Integration Kit Java Sample SP</title>
    </head>
    <body>
        <div class="body-container">
            <div class="ping-body-container">
                <div class="header-block">
                    <div class="header">Service Provider Sample</div>
                    <div class="sub-header">Configuration</div>
                </div>
                <div class="config-container">
<%
	String errorMessage = request.getParameter(SpSampleConstants.ERROR);

	if (StringUtils.isNotBlank(errorMessage))
	{
%>
					<div class="configuration-failure"><%= errorMessage %></div>
<%
	}
%>
                	<form name="sp-adapter-configuration" 
                		  method="post" 
                		  enctype="<%= ContentType.APPLICATION_FORM_URLENCODED.getMimeType() %>" 
                		  action="<%= URLUtil.getConfigurationURL(request) %>">
                		<div class="grid-container">
                			<div class="ping-input grid-a">
								<label class="ping-input-label">BASE PF URL</label>
								<div class="ping-input-container">
									<input type="text" 
										   name="<%= SpSampleConstants.SP_ADAPTER_CONF_BASE_PF_URL %>" 
										   id="base_pf_url_input" 
										   value="<%= basePfUrl %>"/>
								</div>
                			</div>
                			<div class="ping-input grid-b">
								<label class="ping-input-label">APPLICATION USERNAME</label>
								<div class="ping-input-container">
									<input type="text" 
										   name="<%= SpSampleConstants.SP_ADAPTER_CONF_USERNAME %>" 
										   id="application_username_input" 
										   value="<%= username %>"/>
								</div>
                			</div>
                			<div class="ping-input grid-c">
								<label class="ping-input-label">APPLICATION PASSPHRASE</label> 
								<div class="ping-input-container">
									<input type="text" 
										   name="<%= SpSampleConstants.SP_ADAPTER_CONF_PASSPHRASE %>" 
										   id="application_passphrase_input" 
										   value="<%= passphrase %>"/> 
							    </div>
                			</div>

                			<div class="ping-input grid-d">
                			    <label class="ping-input-label"
                			        title="When selected, this triggers the client credentials flow with PF to obtain an access token for use as a bearer token.">
                			        USE BEARER TOKEN BASED AUTHENTICATION
                			    </label>
                			    <input id="use_bearertoken_input"
                			        type="checkbox"
                			        name="<%= SpSampleConstants.SP_ADAPTER_CONF_USE_BEARER_TOKEN_AUTH %>"
                			        value="yes" <%= isChecked ? "checked" : "" %> >
                                </input>
                			</div>

                			<div class="ping-input grid-e">
								<label class="ping-input-label">CLIENT CREDENTIALS FLOW: CLIENT ID</label>
								<div class="ping-input-container">
									<input type="text"
										   name="<%= SpSampleConstants.SP_ADAPTER_CONF_CLIENT_CREDENTIALS_CLIENT_ID %>"
										   id="application_ccClientId_input"
										   value="<%= ccClientId %>"/>
								</div>
                			</div>
                			<div class="ping-input grid-f">
								<label class="ping-input-label">CLIENT CREDENTIALS FLOW: CLIENT SECRET</label>
								<div class="ping-input-container">
									<input type="text"
										   name="<%= SpSampleConstants.SP_ADAPTER_CONF_CLIENT_CREDENTIALS_CLIENT_SECRET %>"
										   id="application_ccClientSecret_input"
										   value="<%= ccClientSecret %>"/>
							    </div>
                			</div>
                			<div class="ping-input grid-g">
								<label class="ping-input-label">CLIENT CREDENTIALS FLOW: SCOPE</label>
								<div class="ping-input-container">
									<input type="text"
										   name="<%= SpSampleConstants.SP_ADAPTER_CONF_CLIENT_CREDENTIALS_FLOW_SCOPE %>"
										   id="application_ccScope_input"
										   value="<%= ccScope %>"/>
							    </div>
                			</div>

                			<div class="ping-input grid-h">
								<label class="ping-input-label">SP ADAPTER ID</label>
								<div class="ping-input-container">
									<input type="text" 
										   name="<%= SpSampleConstants.SP_ADAPTER_CONF_ADAPTER_ID %>" 
										   id="adapter_id_input" 
										   value="<%= adapterId %>"/>
								</div>
                			</div>
                			<div class="ping-input grid-i">
                            	<label class="ping-input-label">OUTGOING ATTRIBUTE FORMAT</label>
								<div class="ping-input-container">
									<div class="select-wrapper">
										<select class="input-select" 
												name="<%= SpSampleConstants.SP_ADAPTER_CONF_OUTGOING_ATTRIBUTE_FORMAT %>" 
												id="outgoing_attribute_format_input">
											<option value="<%= outgoingAttributeFormat %>">
												<%= outgoingAttributeFormat %>
											</option>
											<option value="<%= otherOutgoingAttributeFormat %>">
												<%= otherOutgoingAttributeFormat %>
											</option>
										</select>
									</div>
								</div>
							</div> 	
                			<div class="ping-input grid-j">
                            	<label class="ping-input-label">TARGET URL</label>
								<div class="ping-input-container">
									<input type="text" 
										   name="<%= SpSampleConstants.SP_ADAPTER_CONF_TARGET_URL %>" 
										   id="target_url_input" 
										   value="<%= targetURL %>"/>
								</div>
							</div> 	
                			<div class="ping-input grid-k">
                            	<label class="ping-input-label">PARTNER ENTITY ID</label>
								<div class="ping-input-container">
									<input type="text" 
										   name="<%= SpSampleConstants.SP_ADAPTER_CONF_PARTNER_ENTITY_ID %>" 
										   id="partner_entity_id_input" 
										   value="<%= partnerEntityId %>"/>
								</div>
							</div> 	
                		</div>
                		<div class="configuration-buttons-container">
							<div class="ping-input-container">
								<input class="table-button" id="save_button" name="submit" type="submit" value="Save"/>
							</div>
							<div class="ping-input-container">
								<div class="cancel">
									<a onclick="javascript:back();">Cancel</a>
								</div>
							</div>
                		</div>
                	</form>
                </div>
            </div>
            <div class="ping-footer-container">
                <div class="ping-footer">
                    <div class="ping-credits"></div>
                    <div class="ping-copyright">
                    	Copyright © 2003-2025. Ping Identity Corporation. All rights reserved.
					</div>
                </div>
            </div>
        </div>
    </body>
</html>