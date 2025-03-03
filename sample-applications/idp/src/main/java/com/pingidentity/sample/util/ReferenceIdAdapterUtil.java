package com.pingidentity.sample.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.pingidentity.referenceid.util.DropoffUtil;
import com.pingidentity.referenceid.util.IncomingAttributeFormat;
import com.pingidentity.referenceid.util.PickupUtil;
import com.pingidentity.referenceid.util.ReferenceIdAdapterResponse;
import com.pingidentity.sample.authentication.User;
import com.pingidentity.sample.configuration.ConfigurationManager;
import com.pingidentity.sample.configuration.IdpSampleConfiguration;
import com.pingidentity.sample.configuration.IdpSampleConstants;
import com.pingidentity.sample.exception.DropoffException;
import com.pingidentity.sample.exception.PickupException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.ssl.SSLContextBuilder;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.io.StringReader;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.Properties;

public class ReferenceIdAdapterUtil
{
    private static final Log logger = LogFactory.getLog(ReferenceIdAdapterUtil.class);

    public static ReferenceIdAdapterResponse pickupAttributes(String referenceId) throws PickupException
    {
        IdpSampleConfiguration idpSampleConfiguration = ConfigurationManager.getInstance().getIdpSampleConfiguration();
        Properties idpAdapterConfiguration = idpSampleConfiguration.getIdpAdapterConfiguration();

        String basePfUrl = idpAdapterConfiguration.getProperty(IdpSampleConstants.IDP_ADAPTER_CONF_BASE_PF_URL);
        String username = idpAdapterConfiguration.getProperty(IdpSampleConstants.IDP_ADAPTER_CONF_USERNAME);
        String passphrase = idpAdapterConfiguration.getProperty(IdpSampleConstants.IDP_ADAPTER_CONF_PASSPHRASE);
        String adapterId = idpAdapterConfiguration.getProperty(IdpSampleConstants.IDP_ADAPTER_CONF_ADAPTER_ID);
        boolean useBearerTokenAuthentication =
                idpAdapterConfiguration.getProperty(IdpSampleConstants.IDP_ADAPTER_CONF_USE_BEARER_TOKEN_AUTH).equals("yes") ? true: false;
        String clientId = idpAdapterConfiguration.getProperty(IdpSampleConstants.IDP_ADAPTER_CONF_CLIENT_CREDENTIALS_CLIENT_ID);
        String clientSecret =
                idpAdapterConfiguration.getProperty(IdpSampleConstants.IDP_ADAPTER_CONF_CLIENT_CREDENTIALS_CLIENT_SECRET);
        String scope =
                idpAdapterConfiguration.getProperty(IdpSampleConstants.IDP_ADAPTER_CONF_CLIENT_CREDENTIALS_FLOW_SCOPE);

        ReferenceIdAdapterResponse pickupResponse;

        try
        {
            // For simplicity, trust any certificate. Do not use in production.
            SSLContext sslContext = SSLContextBuilder
                    .create()
                    .loadTrustMaterial((chain, authType) -> true)
                    .build();

            pickupResponse = PickupUtil.pickupFromRefIdAdapter(
                    basePfUrl,
                    adapterId,
                    referenceId,
                    username,
                    passphrase,
                    useBearerTokenAuthentication,
                    clientId,
                    clientSecret,
                    scope,
                    sslContext,
                    (hostname, sslSession) -> true);
        }
        catch (IOException | NoSuchAlgorithmException | KeyStoreException | KeyManagementException e)
        {
            throw new PickupException("An error occurred while picking up from the Reference ID Adapter. " +
                                      "Please ensure the configuration is correct.", e);
        }

        return pickupResponse;
    }

    public static ReferenceIdAdapterResponse dropoffAttributes(User user) throws DropoffException
    {
        IdpSampleConfiguration idpSampleConfiguration = ConfigurationManager.getInstance().getIdpSampleConfiguration();
        Properties idpAdapterConfiguration = idpSampleConfiguration.getIdpAdapterConfiguration();

        String basePfUrl = idpAdapterConfiguration.getProperty(IdpSampleConstants.IDP_ADAPTER_CONF_BASE_PF_URL);
        String username = idpAdapterConfiguration.getProperty(IdpSampleConstants.IDP_ADAPTER_CONF_USERNAME);
        String passphrase = idpAdapterConfiguration.getProperty(IdpSampleConstants.IDP_ADAPTER_CONF_PASSPHRASE);
        String adapterId = idpAdapterConfiguration.getProperty(IdpSampleConstants.IDP_ADAPTER_CONF_ADAPTER_ID);
        String incomingAttributeFormatStr = idpAdapterConfiguration.getProperty(
                IdpSampleConstants.IDP_ADAPTER_CONF_INCOMING_ATTRIBUTE_FORMAT);
        boolean useBearerTokenAuthentication =
                idpAdapterConfiguration.getProperty(IdpSampleConstants.IDP_ADAPTER_CONF_USE_BEARER_TOKEN_AUTH).equals("yes") ? true : false;
        String clientId = idpAdapterConfiguration.getProperty(IdpSampleConstants.IDP_ADAPTER_CONF_CLIENT_CREDENTIALS_CLIENT_ID);
        String clientSecret =
                idpAdapterConfiguration.getProperty(IdpSampleConstants.IDP_ADAPTER_CONF_CLIENT_CREDENTIALS_CLIENT_SECRET);
        String scope =
                idpAdapterConfiguration.getProperty(IdpSampleConstants.IDP_ADAPTER_CONF_CLIENT_CREDENTIALS_FLOW_SCOPE);

        IncomingAttributeFormat incomingAttributeFormat =
                IdpSampleConstants.IDP_ADAPTER_CONF_INCOMING_ATTRIBUTE_FORMAT_JSON.equals(incomingAttributeFormatStr) ?
                IncomingAttributeFormat.JSON :
                IncomingAttributeFormat.QUERY_PARAMETER;

        ReferenceIdAdapterResponse dropoffResponse;

        try
        {
            String attributes = user.getAttributes(incomingAttributeFormatStr);

            // For simplicity, trust any certificate. Do not use in production.
            SSLContext sslContext = SSLContextBuilder
                    .create()
                    .loadTrustMaterial((chain, authType) -> true)
                    .build();

            dropoffResponse = DropoffUtil.dropoffToRefIdAdapter(
                    basePfUrl,
                    adapterId,
                    attributes,
                    username,
                    passphrase,
                    useBearerTokenAuthentication,
                    clientId,
                    clientSecret,
                    scope,
                    incomingAttributeFormat,
                    sslContext,
                    (hostname, sslSession) -> true);

        }
        catch (NoSuchAlgorithmException | KeyStoreException | KeyManagementException | IOException e)
        {
            throw new DropoffException("An error occurred while dropping off to the Reference ID Adapter. " +
                                       "Please ensure the configuration is correct.", e);
        }

        return dropoffResponse;
    }

    public static String getAttribute(String outgoingAttributeFormat, String attributes, String attributeKey)
    {
        String attribute = "";

        if (IdpSampleConstants.IDP_ADAPTER_CONF_OUTGOING_ATTRIBUTE_FORMAT_JSON.equals(outgoingAttributeFormat))
        {
            JsonParser jsonParser = new JsonParser();
            JsonElement attributesJsonElement = jsonParser.parse(attributes);
            JsonElement attributeJsonElement = attributesJsonElement.getAsJsonObject().get(attributeKey);
            attribute = attributeJsonElement.getAsString();
        }
        else // PROPERTIES
        {
            Properties attributeProperties = new Properties();

            try (StringReader stringReader = new StringReader(attributes))
            {
                attributeProperties.load(stringReader);
                attribute = attributeProperties.getProperty(attributeKey);
            }
            catch (IOException e)
            {
                logger.error("An error occurred while reading attribute properties:", e);
            }
        }

        return attribute;
    }
    
}
