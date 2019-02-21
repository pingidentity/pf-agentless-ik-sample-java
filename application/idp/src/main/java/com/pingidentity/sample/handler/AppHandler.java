package com.pingidentity.sample.handler;

import com.pingidentity.referenceid.util.ReferenceIdAdapterResponse;
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

public class AppHandler extends RequestHandler
{
    private static final Log logger = LogFactory.getLog(AppHandler.class);

    @Override
    public String handle(HttpServletRequest request, HttpServletResponse response, ServletContext servletContext)
    {
        String referenceId = request.getParameter(IdpSampleConstants.REF_ID_ADAPTER_REFERENCE);

        if (StringUtils.isNotBlank(referenceId))
        {
            try
            {
                ReferenceIdAdapterResponse pickupResponse = ReferenceIdAdapterUtil.pickupAttributes(referenceId);
                HttpSession httpSession = request.getSession(true);

                httpSession.setAttribute(
                        IdpSampleConstants.LOGIN_KEY_PREFIX + IdpSampleConstants.RESUME_PATH,
                        request.getParameter(IdpSampleConstants.RESUME_PATH));
                httpSession.setAttribute(IdpSampleConstants.LOGIN_KEY_PREFIX + IdpSampleConstants.REF_ID_ADAPTER_REFERENCE, referenceId);
                httpSession.setAttribute(IdpSampleConstants.LOGIN_KEY_PREFIX + IdpSampleConstants.REF_ID_ADAPTER_RESPONSE, pickupResponse);
            }
            catch (PickupException e)
            {
                logger.error(e.getMessage(), e.getCause());
                return IdpSampleConstants.ERROR_JSP_PATH;
            }
        }

        switch (request.getMethod())
        {
            case "GET":
                return IdpSampleConstants.LOGIN_JSP_PATH;
            case "POST":
                return URLUtil.getIdpSampleAppURL(request);
            default:
                logger.error("An error occurred while serving content: invalid method " +
                             request.getMethod() + " was received.");
                return IdpSampleConstants.ERROR_JSP_PATH;
        }
    }
}
