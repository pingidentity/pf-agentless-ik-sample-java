package com.pingidentity.sample.util;

import com.pingidentity.sample.configuration.SpSampleConstants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class URLUtil
{
    private static final Log logger = LogFactory.getLog(URLUtil.class);

    public static String getSpSampleAppURL(HttpServletRequest request)
    {
        return request.getContextPath() + SpSampleConstants.APP_PATH;
    }

    public static String getIdpSampleAppURL(HttpServletRequest request)
    {
        return request.getScheme() +
               "://" +
               request.getServerName() +
               ":" +
               request.getServerPort() +
               SpSampleConstants.IDP_SAMPLE_APP_CONTEXT;
    }

    public static String getConfigurationURL(HttpServletRequest request)
    {
        return getSpSampleAppURL(request) +
               "?" +
               SpSampleConstants.ACTION_PARAM +
               "=" +
               encodeParameter(SpSampleConstants.ACTION_PARAM_CONFIGURE);
    }

    public static String getConfigurationWithErrorsURL(String errorMessage)
    {
        return SpSampleConstants.CONFIGURATION_JSP_PATH +
               "?" +
               SpSampleConstants.ERROR +
               "=" +
               encodeParameter(errorMessage);
    }

    public static String getStartSSOURL(
            String basePfUrl,
            String startSSOEndpoint,
            String partnerEntityId,
            String targetResource)
    {
        return basePfUrl +
               startSSOEndpoint +
               "?" +
               SpSampleConstants.PARTNER_IDP_ID +
               "=" +
               encodeParameter(partnerEntityId) +
               "&" +
               SpSampleConstants.TARGET_RESOURCE +
               "=" +
               encodeParameter(targetResource);
    }

    public static String getStartSLOURL(String basePfUrl, String startSLOEndpoint, String targetResource)
    {
        return basePfUrl +
               startSLOEndpoint +
               "?" +
               SpSampleConstants.TARGET_RESOURCE +
               "=" +
               encodeParameter(targetResource);
    }

    public static String getResumeURL(String basePfUrl, String resumePath, String referenceId)
    {
        return basePfUrl +
               resumePath +
               "?" +
               SpSampleConstants.REF_ID_ADAPTER_REFERENCE +
               "=" +
               referenceId;
    }

    public static String getSpSampleAppLogoutURL(HttpServletRequest request)
    {
        return getSpSampleAppURL(request) +
               "?" +
               SpSampleConstants.ACTION_PARAM +
               "=" +
               SpSampleConstants.ACTION_PARAM_LOGOUT;
    }

    public static String getLoggedOutURL(HttpServletRequest request)
    {
        return request.getScheme() +
               "://" +
               request.getServerName() +
               ":" +
               request.getServerPort() +
               request.getContextPath() +
               "?" +
               SpSampleConstants.ACTION_PARAM +
               "=" +
               SpSampleConstants.ACTION_PARAM_LOGGED_OUT;
    }

    private static String encodeParameter(String parameter)
    {
        String encodedParameter = "";

        try
        {
            encodedParameter = URLEncoder.encode(parameter, StandardCharsets.UTF_8.name());
        }
        catch (UnsupportedEncodingException e)
        {
            logger.error("An error occurred while url encoding:", e);
        }

        return encodedParameter;
    }
}
