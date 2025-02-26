/*
 * **************************************************
 *  Copyright (C) 2025 Ping Identity Corporation
 *  All rights reserved.
 *
 *  The contents of this file are subject to the terms of the
 *  Ping Identity Corporation SDK Developer Guide.
 *
 *  Ping Identity Corporation
 *  1001 17th St Suite 100
 *  Denver, CO 80202
 *  303.468.2900
 *  http://www.pingidentity.com
 * ****************************************************
 */

package com.pingidentity.referenceid.util;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class ClientCredentialsUtil
{
    public static String TOKEN_ENDPOINT = "/as/token.oauth2";

    public static String getAccessToken(String basePfUrl, String clientId, String clientSecret)
    {
        String accessToken = null;
        String tokenUrl = basePfUrl + TOKEN_ENDPOINT;
        try
        {
            URL url = new URL(tokenUrl);

            String formParams = "client_id=" + URLEncoder.encode(clientId, "UTF-8") +
                                "&client_secret=" + URLEncoder.encode(clientSecret, "UTF-8") +
                                "&grant_type=client_credentials";

            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("Accept", "application/json");
            try (OutputStream os = connection.getOutputStream())
            {
                byte[] input = formParams.getBytes("UTF-8");
                os.write(input, 0, input.length);
            }
            // Get response code
            int responseCode = connection.getResponseCode();
            if (responseCode != 200)
            {
                throw new RuntimeException("Failed to retrieve access token!");
            }
            try (InputStream is = connection.getInputStream();
                 BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"))) {

                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null)
                {
                    response.append(line);
                }
                JsonElement jsonElement = JsonParser.parseString(response.toString());
                accessToken = jsonElement.getAsJsonObject().get("access_token").getAsString();
            }
            connection.disconnect();
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }

        return accessToken;
    }

}
