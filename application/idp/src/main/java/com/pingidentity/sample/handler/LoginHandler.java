package com.pingidentity.sample.handler;

import com.pingidentity.referenceid.util.ReferenceIdAdapterResponse;
import com.pingidentity.sample.authentication.Authenticator;
import com.pingidentity.sample.authentication.User;
import com.pingidentity.sample.configuration.ConfigurationManager;
import com.pingidentity.sample.configuration.IdpSampleConfiguration;
import com.pingidentity.sample.configuration.IdpSampleConstants;
import com.pingidentity.sample.exception.DropoffException;
import com.pingidentity.sample.util.ReferenceIdAdapterUtil;
import com.pingidentity.sample.util.URLUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import static com.pingidentity.sample.configuration.IdpSampleConstants.IDP_ADAPTER_CONF_BASE_PF_URL;
import static com.pingidentity.sample.configuration.IdpSampleConstants.REF_ID_ADAPTER_REQUEST_ATTRIBUTES;
import static com.pingidentity.sample.configuration.IdpSampleConstants.REF_ID_ADAPTER_RESPONSE;

public class LoginHandler extends RequestHandler
{
    private static final Log logger = LogFactory.getLog(LoginHandler.class);

    @Override
    public String handle(HttpServletRequest request, HttpServletResponse response, ServletContext servletContext)
    {
        String username = request.getParameter(IdpSampleConstants.USERNAME);
        String password = request.getParameter(IdpSampleConstants.PASSWORD);

        if (StringUtils.isNotBlank(username))
        {
            Authenticator authenticator = new Authenticator();
            User user = authenticator.authenticateUser(username, password);

            if (user != null)
            {
                IdpSampleConfiguration idpSampleConfiguration =
                        ConfigurationManager.getInstance().getIdpSampleConfiguration();
                Properties idpAdapterConfiguration = idpSampleConfiguration.getIdpAdapterConfiguration();

                String outgoingAttributeFormat = idpAdapterConfiguration.getProperty(
                        IdpSampleConstants.IDP_ADAPTER_CONF_OUTGOING_ATTRIBUTE_FORMAT);
                String incomingAttributeFormat = idpAdapterConfiguration.getProperty(
                        IdpSampleConstants.IDP_ADAPTER_CONF_INCOMING_ATTRIBUTE_FORMAT);
                String basePfUrl = idpAdapterConfiguration.getProperty(IDP_ADAPTER_CONF_BASE_PF_URL);
                String targetURL = idpAdapterConfiguration.getProperty(IdpSampleConstants.IDP_ADAPTER_CONF_TARGET_URL);
                String partnerEntityId = idpAdapterConfiguration.getProperty(
                        IdpSampleConstants.IDP_ADAPTER_CONF_PARTNER_ENTITY_ID);

                try
                {
                    // add subject attribute
                    user.addAttribute(IdpSampleConstants.SUBJECT, user.getUsername());

                    // add authnInstant
                    SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ssZ");
                    user.addAttribute(IdpSampleConstants.AUTHN_INSTANT, dateFormat.format(new Date()));

                    String attributes = user.getAttributes(incomingAttributeFormat);
                    request.getSession(true).setAttribute(IdpSampleConstants.DROPOFF_KEY_PREFIX +
                                                          REF_ID_ADAPTER_REQUEST_ATTRIBUTES, attributes);

                    ReferenceIdAdapterResponse referenceIdAdapterResponse =
                            ReferenceIdAdapterUtil.dropoffAttributes(user);
                    request.getSession(true).setAttribute(IdpSampleConstants.DROPOFF_KEY_PREFIX +
                                                          REF_ID_ADAPTER_RESPONSE, referenceIdAdapterResponse);

                    String referenceId = ReferenceIdAdapterUtil.getAttribute(
                            outgoingAttributeFormat,
                            referenceIdAdapterResponse.getResponseBody(),
                            IdpSampleConstants.REF_ID_ADAPTER_REFERENCE);

                    String redirectURL;

                    HttpSession httpSession = request.getSession(false);

                    if (httpSession != null && httpSession.getAttribute(IdpSampleConstants.LOGIN_KEY_PREFIX +
                                                                        IdpSampleConstants.RESUME_PATH) != null)
                    {
                        String resumePath = (String) httpSession.getAttribute(IdpSampleConstants.LOGIN_KEY_PREFIX +
                                                                              IdpSampleConstants.RESUME_PATH);
                        httpSession.removeAttribute(IdpSampleConstants.LOGIN_KEY_PREFIX +
                                                    IdpSampleConstants.RESUME_PATH);

                        redirectURL = URLUtil.getResumeURL(basePfUrl, resumePath, referenceId, targetURL);
                    }
                    else
                    {
                        redirectURL = URLUtil.getStartSSOURL(
                                basePfUrl,
                                IdpSampleConstants.START_IDP_SSO_ENDPOINT,
                                partnerEntityId,
                                targetURL,
                                referenceId);
                    }

                    String redirectURLPath = new URL(redirectURL).getPath();

                    request.getSession(true).setAttribute(
                            IdpSampleConstants.RESUME_KEY_PREFIX +
                            IdpSampleConstants.REF_ID_ADAPTER_REFERENCE,
                            referenceId);
                    request.getSession(true).setAttribute(
                            IdpSampleConstants.RESUME_KEY_PREFIX +
                            IdpSampleConstants.REF_ID_ADAPTER_REDIRECT_URL_PATH,
                            redirectURLPath);
                    return URLUtil.getDropoffURL(request);
                }
                catch (DropoffException e)
                {
                    logger.error(e.getMessage(), e.getCause());
                    return IdpSampleConstants.ERROR_JSP_PATH;
                }
                catch (MalformedURLException e)
                {
                    logger.error("An error occurred while constructing the redirect URL:", e);
                    return IdpSampleConstants.ERROR_JSP_PATH;
                }
                catch (IOException e)
                {
                    logger.error("An error occurred while preparing to drop off attributes to the ReferenceID IdP Adapter:", e);
                    return IdpSampleConstants.ERROR_JSP_PATH;
                }
            }
            else
            {
                return URLUtil.getLoginWithErrorsURL("Invalid login.");
            }
        }

        return IdpSampleConstants.LOGIN_JSP_PATH;
    }
}
