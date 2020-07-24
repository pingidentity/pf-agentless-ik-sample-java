/*
 * **************************************************
 *  Copyright (C) 2020 Ping Identity Corporation
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

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.HttpsURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.apache.http.ssl.SSLContextBuilder;

// This example uses JSON simple java toolkit available at
// http://code.google.com/p/json-simple/

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class SPSample
{
    public static void main(String[] args)
    throws Exception
    {
        // For simplicity, trust any certificate. Do not use in production.
        SSLContext sslContext = SSLContextBuilder.create()
                                                 .loadTrustMaterial((chain, authType) -> true)
                                                 .build();
        SSLSocketFactory socketFactory = sslContext.getSocketFactory();

        // Grab the value of the reference Id from the request, this
        // will be sent by PingFederate as a query parameter 'REF'
        String referenceValue = "<MUST_BE_REPLACED_BY_ACTUAL_VALUE>";

        // Call back to PF to get the attributes associated with the reference
        String pickupLocation = "https://localhost:9031/ext/ref/pickup?REF=" + referenceValue;
        System.out.println(pickupLocation);
        URL pickUrl = new URL(pickupLocation);
        URLConnection urlConn = pickUrl.openConnection();
        HttpsURLConnection httpsURLConn = (HttpsURLConnection)urlConn;
        httpsURLConn.setSSLSocketFactory(socketFactory);
        urlConn.setRequestProperty("ping.uname", "changeme");
        urlConn.setRequestProperty("ping.pwd", "please change me before you go into production!");
        urlConn.setRequestProperty("ping.instanceId", "spadapter");

        // Get the response and parse it into another JSON object which are the 'user attributes'.
        // This example uses UTF-8 if encoding is not found in request.
        String encoding = urlConn.getContentEncoding();
        InputStream is = urlConn.getInputStream();
        InputStreamReader streamReader = new InputStreamReader(is, encoding != null ? encoding : "UTF-8");

        JSONParser parser = new JSONParser();
        JSONObject spUserAttributes = (JSONObject)parser.parse(streamReader);
        System.out.println("User Attributes received = " + spUserAttributes.toString());
    }
}
