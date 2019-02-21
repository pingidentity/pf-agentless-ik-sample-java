package com.pingidentity.sample.handler;

import com.pingidentity.sample.configuration.ConfigurationManager;
import com.pingidentity.sample.configuration.IdpSampleConfiguration;
import com.pingidentity.sample.configuration.IdpSampleConstants;
import com.pingidentity.sample.util.URLUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Properties;

import static com.pingidentity.sample.configuration.IdpSampleConstants.IDP_ADAPTER_CONF_BASE_PF_URL;

public class ResumeHandler extends RequestHandler
{
    private static final Log logger = LogFactory.getLog(ResumeHandler.class);

    @Override
    public String handle(HttpServletRequest request, HttpServletResponse response, ServletContext servletContext)
    {
        IdpSampleConfiguration idpSampleConfiguration = ConfigurationManager.getInstance().getIdpSampleConfiguration();

        Properties idpAdapterConfiguration = idpSampleConfiguration.getIdpAdapterConfiguration();

        String basePfUrl = idpAdapterConfiguration.getProperty(IDP_ADAPTER_CONF_BASE_PF_URL);
        String targetURL = idpAdapterConfiguration.getProperty(IdpSampleConstants.IDP_ADAPTER_CONF_TARGET_URL);
        String partnerEntityId = idpAdapterConfiguration.getProperty(
                IdpSampleConstants.IDP_ADAPTER_CONF_PARTNER_ENTITY_ID);

        HttpSession httpSession = request.getSession(false);

        String referenceId = "";

        if (httpSession != null && httpSession.getAttribute(IdpSampleConstants.RESUME_KEY_PREFIX +
                                                            IdpSampleConstants.REF_ID_ADAPTER_REFERENCE) != null)
        {
            referenceId =
                    (String) httpSession.getAttribute(IdpSampleConstants.RESUME_KEY_PREFIX +
                                                      IdpSampleConstants.REF_ID_ADAPTER_REFERENCE);
            httpSession.removeAttribute(IdpSampleConstants.RESUME_KEY_PREFIX +
                                        IdpSampleConstants.REF_ID_ADAPTER_REFERENCE);
        }

        if (httpSession != null && httpSession.getAttribute(IdpSampleConstants.RESUME_KEY_PREFIX +
                                                            IdpSampleConstants.RESUME_PATH) != null)
        {
            String resumePath = (String) httpSession.getAttribute(IdpSampleConstants.RESUME_KEY_PREFIX +
                                                                  IdpSampleConstants.RESUME_PATH);
            httpSession.removeAttribute(IdpSampleConstants.RESUME_KEY_PREFIX + IdpSampleConstants.RESUME_PATH);

            return URLUtil.getResumeURL(basePfUrl, resumePath, referenceId, targetURL);
        }
        else
        {
            return URLUtil.getStartSSOURL(
                    basePfUrl,
                    IdpSampleConstants.START_IDP_SSO_ENDPOINT,
                    partnerEntityId,
                    targetURL,
                    referenceId);
        }
    }
}
