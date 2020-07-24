package com.pingidentity.sample.handler;

import com.pingidentity.sample.configuration.IdpSampleConstants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class ResumeHandler extends RequestHandler
{
    private static final Log logger = LogFactory.getLog(ResumeHandler.class);

    @Override
    public String handle(HttpServletRequest request, HttpServletResponse response, ServletContext servletContext)
    {
        HttpSession httpSession = request.getSession(false);

        if (httpSession != null && httpSession.getAttribute(IdpSampleConstants.RESUME_KEY_PREFIX +
                                                            IdpSampleConstants.REF_ID_ADAPTER_REDIRECT_URL) != null)
        {
            String redirectURL = (String) httpSession.getAttribute(IdpSampleConstants.RESUME_KEY_PREFIX +
                                                                  IdpSampleConstants.REF_ID_ADAPTER_REDIRECT_URL);
            httpSession.removeAttribute(IdpSampleConstants.RESUME_KEY_PREFIX + IdpSampleConstants.REF_ID_ADAPTER_REDIRECT_URL);

            return redirectURL;
        }
        else
        {
            logger.error("An error occurred while constructing the redirect URL.");
            return IdpSampleConstants.ERROR_JSP_PATH;
        }
    }
}
