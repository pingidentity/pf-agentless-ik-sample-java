package com.pingidentity.sample.handler;

import com.pingidentity.referenceid.util.ReferenceIdAdapterResponse;
import com.pingidentity.sample.configuration.ConfigurationManager;
import com.pingidentity.sample.configuration.SpSampleConfiguration;
import com.pingidentity.sample.configuration.SpSampleConstants;
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
        String referenceId = request.getParameter(SpSampleConstants.REF_ID_ADAPTER_REFERENCE);

        if (StringUtils.isNotBlank(referenceId))
        {
            try
            {
                SpSampleConfiguration spSampleConfiguration =
                        ConfigurationManager.getInstance().getSpSampleConfiguration();
                Properties spAdapterConfiguration = spSampleConfiguration.getSpAdapterConfiguration();

                String outgoingAttributeFormat = spAdapterConfiguration.getProperty(
                        SpSampleConstants.SP_ADAPTER_CONF_OUTGOING_ATTRIBUTE_FORMAT);
                String basePfUrl = spAdapterConfiguration.getProperty(SpSampleConstants.SP_ADAPTER_CONF_BASE_PF_URL);

                ReferenceIdAdapterResponse pickupResponse = ReferenceIdAdapterUtil.pickupAttributes(referenceId);

                String resumePath = ReferenceIdAdapterUtil.getAttribute(
                        outgoingAttributeFormat,
                        pickupResponse.getResponseBody(),
                        SpSampleConstants.REF_ID_ADAPTER_RESUME_PATH);

                String redirectURL = URLUtil.getResumeURL(basePfUrl, resumePath, referenceId);
                String redirectURLPath = new URL(redirectURL).getPath();

                HttpSession httpSession = request.getSession(true);

                httpSession.setAttribute(SpSampleConstants.LOGOUT_KEY_PREFIX +
                                         SpSampleConstants.REF_ID_ADAPTER_REFERENCE, referenceId);
                httpSession.setAttribute(SpSampleConstants.LOGOUT_KEY_PREFIX +
                                         SpSampleConstants.REF_ID_ADAPTER_REDIRECT_URL_PATH, redirectURLPath);
                httpSession.setAttribute(SpSampleConstants.LOGOUT_KEY_PREFIX +
                                         SpSampleConstants.REF_ID_ADAPTER_REDIRECT_URL, redirectURL);
                httpSession.setAttribute(SpSampleConstants.LOGOUT_KEY_PREFIX +
                                         SpSampleConstants.REF_ID_ADAPTER_RESPONSE, pickupResponse);
            }
            catch (PickupException e)
            {
                logger.error(e.getMessage(), e.getCause());
                return SpSampleConstants.ERROR_JSP_PATH;
            }
            catch (MalformedURLException e)
            {
                logger.error("An error occurred while constructing the redirect URL:", e);
                return SpSampleConstants.ERROR_JSP_PATH;
            }
        }
        else if (SpSampleConstants.ACTION_PARAM_LOGOUT.equals(request.getParameter(SpSampleConstants.ACTION_PARAM)))
        {
            response.setStatus(200);
        }
        else
        {
            return SpSampleConstants.ERROR_JSP_PATH;
        }

        if (request.getMethod().equals("GET"))
        {
            return SpSampleConstants.LOGOUT_JSP_PATH;
        }
        else if (request.getMethod().equals("POST"))
        {
            return URLUtil.getSpSampleAppLogoutURL(request);
        }
        else
        {
            logger.error("An error occurred while serving content: invalid method " +
                         request.getMethod() + " was received.");
            return SpSampleConstants.ERROR_JSP_PATH;
        }
    }
}
