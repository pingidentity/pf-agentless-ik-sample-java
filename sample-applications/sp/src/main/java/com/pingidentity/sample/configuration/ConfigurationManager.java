package com.pingidentity.sample.configuration;

import com.pingidentity.sample.exception.ConfigurationException;
import com.pingidentity.sample.loader.SpAdapterConfigurationLoader;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class ConfigurationManager
{
    private static ConfigurationManager configurationManager;
    private static SpSampleConfiguration spSampleConfiguration;
    private static SpAdapterConfigurationLoader spAdapterConfigurationLoader;

    private ConfigurationManager()
    {
    }

    public synchronized void load(HttpServletRequest request, ServletContext servletContext)
            throws ConfigurationException
    {
        if (spSampleConfiguration == null)
        {
            spSampleConfiguration = new SpSampleConfiguration();
        }

        spAdapterConfigurationLoader.load(request, servletContext, spSampleConfiguration);
    }

    public static ConfigurationManager getInstance()
    {
        if (configurationManager == null)
        {
            configurationManager = new ConfigurationManager();
            spAdapterConfigurationLoader = new SpAdapterConfigurationLoader();
        }

        return configurationManager;
    }

    public synchronized SpSampleConfiguration getSpSampleConfiguration()
    {
        return spSampleConfiguration;
    }

    public synchronized void save(HttpServletRequest request) throws ConfigurationException, IOException
    {
        File spAdapterConfigurationFile = new File(spAdapterConfigurationLoader.getRealPathToConfigurationFile());

        List<String> lines = getUserInputValues(request);

        FileUtils.writeLines(spAdapterConfigurationFile, StandardCharsets.UTF_8.name(), lines);
    }

    private static List<String> getUserInputValues(HttpServletRequest request) throws ConfigurationException
    {
        List<String> lines = new ArrayList<>();

        String basePfUrl = parseBasePfUrlInput(
                request.getParameter(SpSampleConstants.SP_ADAPTER_CONF_BASE_PF_URL));
        lines.add(basePfUrl);

        String applicationUsername = parseApplicationUsernameInput(
                request.getParameter(SpSampleConstants.SP_ADAPTER_CONF_USERNAME));
        lines.add(applicationUsername);

        String applicationPassphrase = parseApplicationPassphraseInput(
                request.getParameter(SpSampleConstants.SP_ADAPTER_CONF_PASSPHRASE));
        lines.add(applicationPassphrase);

        String useBearerTokenAuth = parseUseBearerTokenAuthInput(
                request.getParameter(SpSampleConstants.SP_ADAPTER_CONF_USE_BEARER_TOKEN_AUTH));
        lines.add(useBearerTokenAuth);

        String ccClientId = parseClientCredentialsFlowClientIdInput(
                request.getParameter(SpSampleConstants.SP_ADAPTER_CONF_CLIENT_CREDENTIALS_CLIENT_ID));
        lines.add(ccClientId);

        String ccClientSecret = parseClientCredentialsFlowClientSecretInput(
                request.getParameter(SpSampleConstants.SP_ADAPTER_CONF_CLIENT_CREDENTIALS_CLIENT_SECRET));
        lines.add(ccClientSecret);

        String spAdapterId = parseApplicationSpAdapterIdInput(
                request.getParameter(SpSampleConstants.SP_ADAPTER_CONF_ADAPTER_ID));
        lines.add(spAdapterId);

        String outgoingAttributeFormat = parseOutgoingAttributeFormatInput(
                request.getParameter(SpSampleConstants.SP_ADAPTER_CONF_OUTGOING_ATTRIBUTE_FORMAT));
        lines.add(outgoingAttributeFormat);

        String targetURL = parseTargetURLInput(request.getParameter(
                SpSampleConstants.SP_ADAPTER_CONF_TARGET_URL));
        lines.add(targetURL);

        String partnerEntityId = parsePartnerEntityIdInput(
                request.getParameter(SpSampleConstants.SP_ADAPTER_CONF_PARTNER_ENTITY_ID));
        lines.add(partnerEntityId);

        return lines;
    }

    private static String parseBasePfUrlInput(String basePfUrl) throws ConfigurationException
    {
        if (StringUtils.isBlank(basePfUrl))
        {
            throw new ConfigurationException("Base PF URL must not be blank.");
        }

        try
        {
            URL testBasePfUrl = new URL(basePfUrl);
        }
        catch (MalformedURLException e)
        {
            throw new ConfigurationException("Base PF URL must be a valid URL.");
        }

        if (basePfUrl.endsWith("/"))
        {
            basePfUrl = basePfUrl.substring(0, basePfUrl.length() - 1);
        }

        return SpSampleConstants.SP_ADAPTER_CONF_BASE_PF_URL + "=" + basePfUrl;
    }

    private static String parseApplicationUsernameInput(String applicationUsername) throws ConfigurationException
    {
        if (StringUtils.isBlank(applicationUsername))
        {
            throw new ConfigurationException("Application username must not be blank.");
        }

        return SpSampleConstants.SP_ADAPTER_CONF_USERNAME + "=" + applicationUsername;
    }

    private static String parseApplicationPassphraseInput(String applicationPassphrase) throws ConfigurationException
    {
        if (StringUtils.isBlank(applicationPassphrase))
        {
            throw new ConfigurationException("Application passphrase must not be blank.");
        }

        return SpSampleConstants.SP_ADAPTER_CONF_PASSPHRASE + "=" + applicationPassphrase;
    }

    private static String parseClientCredentialsFlowClientIdInput(String clientId) throws ConfigurationException
    {
        return SpSampleConstants.SP_ADAPTER_CONF_CLIENT_CREDENTIALS_CLIENT_ID + "=" + clientId;
    }

    private static String parseUseBearerTokenAuthInput(String useBearerTokenAuth) throws ConfigurationException
    {
        return SpSampleConstants.SP_ADAPTER_CONF_USE_BEARER_TOKEN_AUTH + "=" + useBearerTokenAuth;
    }

    private static String parseClientCredentialsFlowClientSecretInput(String clientSecret) throws ConfigurationException
    {
        return SpSampleConstants.SP_ADAPTER_CONF_CLIENT_CREDENTIALS_CLIENT_SECRET + "=" + clientSecret;
    }

    private static String parseApplicationSpAdapterIdInput(String spAdapterId) throws ConfigurationException
    {
        if (StringUtils.isBlank(spAdapterId))
        {
            throw new ConfigurationException("SP Adapter ID must not be blank.");
        }

        return SpSampleConstants.SP_ADAPTER_CONF_ADAPTER_ID + "=" + spAdapterId;
    }

    private static String parseOutgoingAttributeFormatInput(String outgoingAttributeFormat)
    {
        return SpSampleConstants.SP_ADAPTER_CONF_OUTGOING_ATTRIBUTE_FORMAT + "=" + outgoingAttributeFormat;
    }

    private static String parseTargetURLInput(String targetURL) throws ConfigurationException
    {
        if (StringUtils.isBlank(targetURL))
        {
            throw new ConfigurationException("Target URL must not be blank.");
        }

        try
        {
            URL testTargetURL = new URL(targetURL);
        }
        catch (MalformedURLException e)
        {
            throw new ConfigurationException("Target URL must be a valid URL.");
        }

        if (targetURL.endsWith("/"))
        {
            targetURL = targetURL.substring(0, targetURL.length() - 1);
        }

        return SpSampleConstants.SP_ADAPTER_CONF_TARGET_URL + "=" + targetURL;
    }

    private static String parsePartnerEntityIdInput(String partnerEntityId) throws ConfigurationException
    {
        if (StringUtils.isBlank(partnerEntityId))
        {
            throw new ConfigurationException("Partner Entity ID must not be blank.");
        }

        return SpSampleConstants.SP_ADAPTER_CONF_PARTNER_ENTITY_ID + "=" + partnerEntityId;
    }
}
