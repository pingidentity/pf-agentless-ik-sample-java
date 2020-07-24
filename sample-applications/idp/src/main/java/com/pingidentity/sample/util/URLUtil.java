package com.pingidentity.sample.util;

import com.pingidentity.sample.configuration.IdpSampleConstants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class URLUtil
{
    private static final Log logger = LogFactory.getLog(URLUtil.class);

    public static String getIdpSampleAppURL(HttpServletRequest request)
    {
        return request.getContextPath() + IdpSampleConstants.APP_PATH;
    }

    public static String getLoginWithErrorsURL(String errorMessage)
    {
        return IdpSampleConstants.LOGIN_JSP_PATH +
               "?" +
               IdpSampleConstants.ERROR +
               "=" +
               encodeParameter(errorMessage);
    }

    public static String getConfigurationURL(HttpServletRequest request)
    {
        return getIdpSampleAppURL(request) +
               "?" +
               IdpSampleConstants.ACTION_PARAM +
               "=" +
               encodeParameter(IdpSampleConstants.ACTION_PARAM_CONFIGURE);
    }

    public static String getConfigurationWithErrorsURL(String errorMessage)
    {
        return IdpSampleConstants.CONFIGURATION_JSP_PATH +
               "?" +
               IdpSampleConstants.ERROR +
               "=" +
               encodeParameter(errorMessage);
    }

    public static String getDropoffURL(HttpServletRequest request)
    {
        return getIdpSampleAppURL(request) +
               "?" +
               IdpSampleConstants.ACTION_PARAM +
               "=" +
               IdpSampleConstants.ACTION_PARAM_DROPOFF;
    }

    public static String getResumeURL(String basePfUrl, String resumePath, String referenceId)
    {
        return basePfUrl +
               resumePath +
               "?" +
               IdpSampleConstants.REF_ID_ADAPTER_REFERENCE +
               "=" +
               referenceId;
    }

    public static String getResumeURL(String basePfUrl, String resumePath, String referenceId, String targetResource)
    {
        return getResumeURL(basePfUrl, resumePath, referenceId) +
               "&" +
               IdpSampleConstants.TARGET_RESOURCE +
               "=" +
               encodeParameter(targetResource);
    }

    public static String getStartSSOURL(
            String basePfUrl,
            String startSSOEndpoint,
            String partnerEntityId,
            String targetResource,
            String referenceId)
    {
        return basePfUrl +
               startSSOEndpoint +
               "?" +
               IdpSampleConstants.PARTNER_SP_ID +
               "=" +
               encodeParameter(partnerEntityId) +
               "&" +
               IdpSampleConstants.TARGET_RESOURCE +
               "=" +
               encodeParameter(targetResource) +
               "&" +
               IdpSampleConstants.REF_ID_ADAPTER_REFERENCE +
               "=" +
               referenceId;
    }

    public static String getIdpSampleAppLogoutURL(HttpServletRequest request)
    {
        return getIdpSampleAppURL(request) +
               "?" +
               IdpSampleConstants.ACTION_PARAM +
               "=" +
               IdpSampleConstants.ACTION_PARAM_LOGOUT;
    }

    public static String encodeParameter(String parameter)
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
