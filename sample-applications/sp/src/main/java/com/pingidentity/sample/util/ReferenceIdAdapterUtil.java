package com.pingidentity.sample.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.pingidentity.referenceid.util.PickupUtil;
import com.pingidentity.referenceid.util.ReferenceIdAdapterResponse;
import com.pingidentity.sample.configuration.ConfigurationManager;
import com.pingidentity.sample.configuration.SpSampleConfiguration;
import com.pingidentity.sample.configuration.SpSampleConstants;
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
        SpSampleConfiguration spSampleConfiguration = ConfigurationManager.getInstance().getSpSampleConfiguration();
        Properties spAdapterConfiguration = spSampleConfiguration.getSpAdapterConfiguration();

        String basePfUrl = spAdapterConfiguration.getProperty(SpSampleConstants.SP_ADAPTER_CONF_BASE_PF_URL);
        String username = spAdapterConfiguration.getProperty(SpSampleConstants.SP_ADAPTER_CONF_USERNAME);
        String passphrase = spAdapterConfiguration.getProperty(SpSampleConstants.SP_ADAPTER_CONF_PASSPHRASE);
        String adapterId = spAdapterConfiguration.getProperty(SpSampleConstants.SP_ADAPTER_CONF_ADAPTER_ID);
        boolean useBearerTokenAuthentication =
                spAdapterConfiguration.getProperty(SpSampleConstants.SP_ADAPTER_CONF_USE_BEARER_TOKEN_AUTH).equals("yes") ? true: false;
        String clientId = spAdapterConfiguration.getProperty(SpSampleConstants.SP_ADAPTER_CONF_CLIENT_CREDENTIALS_CLIENT_ID);
        String clientSecret =
                spAdapterConfiguration.getProperty(SpSampleConstants.SP_ADAPTER_CONF_CLIENT_CREDENTIALS_CLIENT_SECRET);
        String scope =
                spAdapterConfiguration.getProperty(SpSampleConstants.SP_ADAPTER_CONF_CLIENT_CREDENTIALS_FLOW_SCOPE);

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

    public static String getAttribute(String outgoingAttributeFormat, String attributes, String attributeKey)
    {
        String attribute = "";

        if (SpSampleConstants.SP_ADAPTER_CONF_OUTGOING_ATTRIBUTE_FORMAT_JSON.equals(outgoingAttributeFormat))
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
