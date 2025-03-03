package com.pingidentity.referenceid.util;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

public class PickupUtil
{
    private static final String PICKUP_SUBPATH = "/ext/ref/pickup";

    public static ReferenceIdAdapterResponse pickupFromRefIdAdapter(
            String basePfUrl,
            String adapterId,
            String referenceId,
            String username,
            String password,
            boolean useBearerTokenAuthentication,
            String clientId,
            String clientSecret,
            String scope,
            SSLContext sslContext,
            HostnameVerifier hostnameVerifier) throws IOException
    {
        // 1 - Get an SSL connection
        basePfUrl = StringUtils.removeEnd(basePfUrl, "/");
        URL pickupUrl = new URL(basePfUrl + PICKUP_SUBPATH + "?REF=" + referenceId);
        URLConnection urlConnection = pickupUrl.openConnection();

        // 2 - Identify the adapter to use
        urlConnection.setRequestProperty("ping.instanceId", adapterId);

        String authorizationHeaderValue = "";

        if (useBearerTokenAuthentication)
        {
            // Get the access token
            String accessToken = ClientCredentialsUtil.getAccessToken(basePfUrl, clientId, clientSecret, scope);
            // 3 - Authenticate with Bearer Token
            authorizationHeaderValue = "Bearer " + accessToken;
            urlConnection.setRequestProperty("Authorization", authorizationHeaderValue);
        }
        else
        {
            // 3 - Authenticate with BasicAuth
            String basicAuth = username + ":" + password;
            authorizationHeaderValue =  "Basic " + Base64.encodeBase64String(basicAuth.getBytes());
            urlConnection.setRequestProperty("Authorization", authorizationHeaderValue);
        }

        // 4 - Configure certificate authentication
        HttpsURLConnection httpsURLConnection = (HttpsURLConnection) urlConnection;
        SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
        httpsURLConnection.setSSLSocketFactory(sslSocketFactory);
        httpsURLConnection.setHostnameVerifier(hostnameVerifier);

        // 4 - Get the user attributes and let the caller parse as JSON or Properties
        String responseCode;
        String responseMessage;
        Map<String, List<String>> responseHeaders;
        String responseBody;
        ReferenceIdAdapterResponse pickupResponse;

        String encoding = urlConnection.getContentEncoding();

        try (InputStream inputStream = urlConnection.getInputStream())
        {
            responseCode = Integer.toString(((HttpsURLConnection) urlConnection).getResponseCode());
            responseMessage = ((HttpsURLConnection) urlConnection).getResponseMessage();
            responseHeaders = urlConnection.getHeaderFields();
            responseBody = IOUtils.toString(inputStream, (encoding != null ) ? encoding : StandardCharsets.UTF_8.name());

            pickupResponse = new ReferenceIdAdapterResponse(
                    responseCode,
                    responseMessage,
                    responseHeaders,
                    responseBody
            );
        }

        pickupResponse.setRequestAuthorizationHeaderValue(authorizationHeaderValue);
        return pickupResponse;
    }
}
