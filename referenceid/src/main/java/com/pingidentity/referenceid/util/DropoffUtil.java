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
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

public class DropoffUtil
{
    private static final String DROPOFF_SUBPATH = "/ext/ref/dropoff";

    public static ReferenceIdAdapterResponse dropoffToRefIdAdapter(
            String basePfUrl,
            String adapterId,
            String attributes,
            String username,
            String password,
            boolean useBearerTokenAuthentication,
            String clientId,
            String clientSecret,
            IncomingAttributeFormat incomingAttributeFormat,
            SSLContext sslContext,
            HostnameVerifier hostnameVerifier) throws IOException
    {
        // 1 - Get an SSL connection
        basePfUrl = StringUtils.removeEnd(basePfUrl, "/");

        StringBuilder dropoffUrlStr = new StringBuilder();
        dropoffUrlStr.append(basePfUrl);
        dropoffUrlStr.append(DROPOFF_SUBPATH);

        if (incomingAttributeFormat == IncomingAttributeFormat.QUERY_PARAMETER)
        {
            // Let the caller format the attributes as an encoded query string
            dropoffUrlStr.append("?");
            dropoffUrlStr.append(attributes);
        }

        URL dropoffUrl = new URL(dropoffUrlStr.toString());

        URLConnection urlConnection = dropoffUrl.openConnection();

        // 2 - Identify the adapter to use
        urlConnection.setRequestProperty("ping.instanceId", adapterId);
        String authorizationHeaderValue = "";

        if (useBearerTokenAuthentication)
        {
            // Get the access token
            String accessToken = ClientCredentialsUtil.getAccessToken(basePfUrl, clientId, clientSecret);
            // 3 - Authenticate with Bearer Token
            authorizationHeaderValue = "Bearer " + accessToken;
            urlConnection.setRequestProperty("Authorization", authorizationHeaderValue);
        }
        else
        {
            // 3 - Authenticate with BasicAuth
            String basicAuth = username + ":" + password;
            authorizationHeaderValue = "Basic " + Base64.encodeBase64String(basicAuth.getBytes());
            urlConnection.setRequestProperty("Authorization", authorizationHeaderValue);
        }

        // 4 - Configure certificate authentication
        HttpsURLConnection httpsURLConnection = (HttpsURLConnection) urlConnection;
        SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
        httpsURLConnection.setSSLSocketFactory(sslSocketFactory);
        httpsURLConnection.setHostnameVerifier(hostnameVerifier);

        // 5 - Send the user attributes
        if (incomingAttributeFormat == IncomingAttributeFormat.JSON)
        {
            urlConnection.setDoOutput(true);
            urlConnection.setRequestProperty("Content-Type", "application/json");
        }

        String responseCode;
        String responseMessage;
        Map<String, List<String>> responseHeaders;
        String responseBody;
        ReferenceIdAdapterResponse dropoffResponse;

        if (incomingAttributeFormat == IncomingAttributeFormat.JSON)
        {
            try (OutputStreamWriter outputStreamWriter =
                         new OutputStreamWriter(urlConnection.getOutputStream(), StandardCharsets.UTF_8))
            {
                outputStreamWriter.write(attributes);
                outputStreamWriter.flush();
            }
        }

        // 6 - Read the response and let the caller parse as JSON or Properties

        String encoding = urlConnection.getContentEncoding();
        try (InputStream inputStream = urlConnection.getInputStream())
        {
            responseCode = Integer.toString(((HttpsURLConnection) urlConnection).getResponseCode());
            responseMessage = ((HttpsURLConnection) urlConnection).getResponseMessage();
            responseHeaders = urlConnection.getHeaderFields();
            responseBody = IOUtils.toString(inputStream, (encoding != null ) ? encoding : StandardCharsets.UTF_8.name());

            dropoffResponse = new ReferenceIdAdapterResponse(
                    responseCode,
                    responseMessage,
                    responseHeaders,
                    responseBody
            );
        }

        dropoffResponse.setRequestAuthorizationHeaderValue(authorizationHeaderValue);

        return dropoffResponse;
    }

}
