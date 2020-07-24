package com.pingidentity.sample.handler;

import com.pingidentity.sample.configuration.ConfigurationManager;
import com.pingidentity.sample.configuration.IdpSampleConstants;
import com.pingidentity.sample.exception.ConfigurationException;
import com.pingidentity.sample.util.URLUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.entity.ContentType;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ConfigurationHandler extends RequestHandler
{
    private static final Log logger = LogFactory.getLog(ConfigurationHandler.class);

    @Override
    public String handle(HttpServletRequest request, HttpServletResponse response, ServletContext servletContext)
    {
        String contentType = request.getContentType();
        if (StringUtils.isNotBlank(contentType) &&
            contentType.contains(ContentType.APPLICATION_FORM_URLENCODED.getMimeType()))
        {
            try
            {
                ConfigurationManager.getInstance().save(request);
            }
            catch (ConfigurationException | IOException e)
            {
                logger.error("An error occurred while saving configuration.", e);
                return URLUtil.getConfigurationWithErrorsURL(e.getMessage());
            }

            try
            {
                ConfigurationManager.getInstance().load(request, servletContext);
                return URLUtil.getIdpSampleAppURL(request);
            }
            catch (ConfigurationException e)
            {
                logger.error("An error occurred while loading configuration: ", e);
                return IdpSampleConstants.ERROR_JSP_PATH;
            }
        }

        return IdpSampleConstants.CONFIGURATION_JSP_PATH;
    }
}
