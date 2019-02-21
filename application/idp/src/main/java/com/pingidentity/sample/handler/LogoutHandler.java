package com.pingidentity.sample.handler;

import com.pingidentity.referenceid.util.ReferenceIdAdapterResponse;
import com.pingidentity.sample.configuration.ConfigurationManager;
import com.pingidentity.sample.configuration.IdpSampleConfiguration;
import com.pingidentity.sample.configuration.IdpSampleConstants;
import com.pingidentity.sample.exception.PickupException;
import com.pingidentity.sample.util.ReferenceIdAdapterUtil;
import com.pingidentity.sample.util.URLUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

public class LogoutHandler extends RequestHandler
{
    private static final Log logger = LogFactory.getLog(LogoutHandler.class);

    @Override
    public String handle(HttpServletRequest request, HttpServletResponse response, ServletContext servletContext)
    {
        String referenceId = request.getParameter(IdpSampleConstants.REF_ID_ADAPTER_REFERENCE);

        // Front channel logout
        if (StringUtils.isNotBlank(referenceId))
        {
            try
            {
                ReferenceIdAdapterResponse pickupResponse = ReferenceIdAdapterUtil.pickupAttributes(referenceId);

                IdpSampleConfiguration idpSampleConfiguration =
                        ConfigurationManager.getInstance().getIdpSampleConfiguration();
                Properties idpAdapterConfiguration = idpSampleConfiguration.getIdpAdapterConfiguration();

                String outgoingAttributeFormat = idpAdapterConfiguration.getProperty(
                        IdpSampleConstants.IDP_ADAPTER_CONF_OUTGOING_ATTRIBUTE_FORMAT);
                String basePfUrl = idpAdapterConfiguration.getProperty(IdpSampleConstants.IDP_ADAPTER_CONF_BASE_PF_URL);

                String resumePath = ReferenceIdAdapterUtil.getAttribute(
                        outgoingAttributeFormat,
                        pickupResponse.getResponseBody(),
                        IdpSampleConstants.RESUME_PATH);

                String redirectURL = URLUtil.getResumeURL(basePfUrl, resumePath, referenceId);
                String redirectURLPath = new URL(redirectURL).getPath();

                HttpSession httpSession = request.getSession(true);

                httpSession.setAttribute(IdpSampleConstants.LOGOUT_KEY_PREFIX +
                                         IdpSampleConstants.REF_ID_ADAPTER_REFERENCE, referenceId);
                httpSession.setAttribute(IdpSampleConstants.LOGOUT_KEY_PREFIX +
                                         IdpSampleConstants.REF_ID_ADAPTER_REDIRECT_URL_PATH, redirectURLPath);
                httpSession.setAttribute(IdpSampleConstants.LOGOUT_KEY_PREFIX +
                                         IdpSampleConstants.REF_ID_ADAPTER_REDIRECT_URL, redirectURL);
                httpSession.setAttribute(IdpSampleConstants.LOGOUT_KEY_PREFIX +
                                         IdpSampleConstants.REF_ID_ADAPTER_RESPONSE, pickupResponse);
            }
            catch (PickupException e)
            {
                logger.error(e.getMessage(), e.getCause());
                return IdpSampleConstants.ERROR_JSP_PATH;
            }
            catch (MalformedURLException e)
            {
                logger.error("An error occurred while constructing the redirect URL:", e);
                return IdpSampleConstants.ERROR_JSP_PATH;
            }
        }
        // Back channel logout
        else if (IdpSampleConstants.ACTION_PARAM_LOGOUT.equals(request.getParameter(IdpSampleConstants.ACTION_PARAM)))
        {
            response.setStatus(200);
        }
        else
        {
            return IdpSampleConstants.ERROR_JSP_PATH;
        }

        if (request.getMethod().equals("GET"))
        {
            return IdpSampleConstants.LOGOUT_JSP_PATH;
        }
        else if (request.getMethod().equals("POST"))
        {
            return URLUtil.getIdpSampleAppLogoutURL(request);
        }
        else
        {
            logger.error("An error occurred while serving content: invalid method " +
                         request.getMethod() + " was received.");
            return IdpSampleConstants.ERROR_JSP_PATH;
        }
    }
}
