package com.pingidentity.sample.configuration;

import com.pingidentity.sample.exception.ConfigurationException;
import com.pingidentity.sample.loader.IdpAdapterConfigurationLoader;
import com.pingidentity.sample.loader.IdpSampleUsersLoader;
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
    private static IdpSampleConfiguration idpSampleConfiguration;
    private static IdpAdapterConfigurationLoader idpAdapterConfigurationLoader;
    private static IdpSampleUsersLoader idpSampleUsersLoader;

    private ConfigurationManager()
    {
    }

    public synchronized void load(HttpServletRequest request, ServletContext servletContext)
            throws ConfigurationException
    {
        if (idpSampleConfiguration == null)
        {
            idpSampleConfiguration = new IdpSampleConfiguration();
        }

        idpAdapterConfigurationLoader.load(request, servletContext, idpSampleConfiguration);
        idpSampleUsersLoader.load(request, servletContext, idpSampleConfiguration);
    }

    public static ConfigurationManager getInstance()
    {
        if (configurationManager == null)
        {
            configurationManager = new ConfigurationManager();
            idpAdapterConfigurationLoader = new IdpAdapterConfigurationLoader();
            idpSampleUsersLoader = new IdpSampleUsersLoader();
        }

        return configurationManager;
    }

    public synchronized IdpSampleConfiguration getIdpSampleConfiguration()
    {
        return idpSampleConfiguration;
    }

    public synchronized void save(HttpServletRequest request) throws ConfigurationException, IOException
    {
        File idpAdapterConfigurationFile = new File(idpAdapterConfigurationLoader.getRealPathToConfigurationFile());

        List<String> lines = getUserInputValues(request);

        FileUtils.writeLines(idpAdapterConfigurationFile, StandardCharsets.UTF_8.name(), lines);
    }

    private static List<String> getUserInputValues(HttpServletRequest request) throws ConfigurationException
    {
        List<String> lines = new ArrayList<>();

        String basePfUrl = parseBasePfUrlInput(
                request.getParameter(IdpSampleConstants.IDP_ADAPTER_CONF_BASE_PF_URL));
        lines.add(basePfUrl);

        String applicationUsername = parseApplicationUsernameInput(
                request.getParameter(IdpSampleConstants.IDP_ADAPTER_CONF_USERNAME));
        lines.add(applicationUsername);

        String applicationPassphrase = parseApplicationPassphraseInput(
                request.getParameter(IdpSampleConstants.IDP_ADAPTER_CONF_PASSPHRASE));
        lines.add(applicationPassphrase);

        String idpAdapterId = parseApplicationIdpAdapterIdInput(
                request.getParameter(IdpSampleConstants.IDP_ADAPTER_CONF_ADAPTER_ID));
        lines.add(idpAdapterId);

        String outgoingAttributeFormat = parseOutgoingAttributeFormatInput(
                request.getParameter(IdpSampleConstants.IDP_ADAPTER_CONF_OUTGOING_ATTRIBUTE_FORMAT));
        lines.add(outgoingAttributeFormat);

        String incomingAttributeFormat = parseIncomingAttributeFormatInput(
                request.getParameter(IdpSampleConstants.IDP_ADAPTER_CONF_INCOMING_ATTRIBUTE_FORMAT));
        lines.add(incomingAttributeFormat);

        String targetURL = parseTargetURLInput(request.getParameter(
                IdpSampleConstants.IDP_ADAPTER_CONF_TARGET_URL));
        lines.add(targetURL);

        String partnerEntityId = parsePartnerEntityIdInput(
                request.getParameter(IdpSampleConstants.IDP_ADAPTER_CONF_PARTNER_ENTITY_ID));
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
            throw new ConfigurationException("Base PF URL must be a valid URL.", e);
        }

        if (basePfUrl.endsWith("/"))
        {
            basePfUrl = basePfUrl.substring(0, basePfUrl.length() - 1);
        }

        return IdpSampleConstants.IDP_ADAPTER_CONF_BASE_PF_URL + "=" + basePfUrl;
    }

    private static String parseApplicationUsernameInput(String applicationUsername) throws ConfigurationException
    {
        if (StringUtils.isBlank(applicationUsername))
        {
            throw new ConfigurationException("Application username must not be blank.");
        }

        return IdpSampleConstants.IDP_ADAPTER_CONF_USERNAME + "=" + applicationUsername;
    }

    private static String parseApplicationPassphraseInput(String applicationPassphrase) throws ConfigurationException
    {
        if (StringUtils.isBlank(applicationPassphrase))
        {
            throw new ConfigurationException("Application passphrase must not be blank.");
        }

        return IdpSampleConstants.IDP_ADAPTER_CONF_PASSPHRASE + "=" + applicationPassphrase;
    }

    private static String parseApplicationIdpAdapterIdInput(String idpAdapterId) throws ConfigurationException
    {
        if (StringUtils.isBlank(idpAdapterId))
        {
            throw new ConfigurationException("IdP Adapter ID must not be blank.");
        }

        return IdpSampleConstants.IDP_ADAPTER_CONF_ADAPTER_ID + "=" + idpAdapterId;
    }

    private static String parseOutgoingAttributeFormatInput(String outgoingAttributeFormat)
    {
        return IdpSampleConstants.IDP_ADAPTER_CONF_OUTGOING_ATTRIBUTE_FORMAT + "=" + outgoingAttributeFormat;
    }

    private static String parseIncomingAttributeFormatInput(String incomingAttributeFormat)
    {
        return IdpSampleConstants.IDP_ADAPTER_CONF_INCOMING_ATTRIBUTE_FORMAT + "=" + incomingAttributeFormat;
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
            throw new ConfigurationException("Target URL must be a valid URL.", e);
        }

        if (targetURL.endsWith("/"))
        {
            targetURL = targetURL.substring(0, targetURL.length() - 1);
        }

        return IdpSampleConstants.IDP_ADAPTER_CONF_TARGET_URL + "=" + targetURL;
    }

    private static String parsePartnerEntityIdInput(String partnerEntityId) throws ConfigurationException
    {
        if (StringUtils.isBlank(partnerEntityId))
        {
            throw new ConfigurationException("Partner Entity ID must not be blank.");
        }

        return IdpSampleConstants.IDP_ADAPTER_CONF_PARTNER_ENTITY_ID + "=" + partnerEntityId;
    }
}
