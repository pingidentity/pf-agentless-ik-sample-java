package com.pingidentity.sample.handler;

import com.pingidentity.referenceid.util.ReferenceIdAdapterResponse;
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

public class AppHandler extends RequestHandler
{
    private static final Log logger = LogFactory.getLog(AppHandler.class);

    @Override
    public String handle(HttpServletRequest request, HttpServletResponse response, ServletContext servletContext)
    {
        String referenceId = request.getParameter(SpSampleConstants.REF_ID_ADAPTER_REFERENCE);

        if (StringUtils.isNotBlank(referenceId))
        {
            try
            {
                ReferenceIdAdapterResponse pickupResponse = ReferenceIdAdapterUtil.pickupAttributes(referenceId);

                HttpSession httpSession = request.getSession(true);

                httpSession.setAttribute(SpSampleConstants.APP_KEY_PREFIX +
                                         SpSampleConstants.REF_ID_ADAPTER_REFERENCE, referenceId);
                httpSession.setAttribute(SpSampleConstants.APP_KEY_PREFIX +
                                         SpSampleConstants.REF_ID_ADAPTER_RESPONSE, pickupResponse);
            }
            catch (PickupException e)
            {
                logger.error(e.getMessage(), e.getCause());
                return SpSampleConstants.ERROR_JSP_PATH;
            }
        }

        if (request.getMethod().equals("GET"))
        {
            return SpSampleConstants.APP_JSP_PATH;
        }
        else if (request.getMethod().equals("POST"))
        {
            return URLUtil.getSpSampleAppURL(request);
        }
        else
        {
            logger.error("An error occurred while serving content: invalid method " +
                         request.getMethod() + " was received.");
            return SpSampleConstants.ERROR_JSP_PATH;
        }
    }
}
