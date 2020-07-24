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
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

import org.apache.http.ssl.SSLContextBuilder;

// This example uses JSON simple java toolkit available at
// http://code.google.com/p/json-simple/

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class IdPSample
{
    public static void main(String[] args) throws Exception
    {
        // For simplicity, trust any certificate. Do not use in production.
        SSLContext sslContext = SSLContextBuilder.create()
                                                 .loadTrustMaterial((chain, authType) -> true)
                                                 .build();
        SSLSocketFactory socketFactory = sslContext.getSocketFactory();

        // Create a JSON Object containing user attributes
        JSONObject idpUserAttributes = new JSONObject();
        idpUserAttributes.put("attribute1", "value1");

        // Drop the attributes into PingFederate
        String dropoffLocation = "https://localhost:9031/ext/ref/dropoff";
        System.out.println(dropoffLocation);
        URL dropUrl = new URL(dropoffLocation);
        URLConnection urlConnection = dropUrl.openConnection();
        HttpsURLConnection httpsURLConnection = (HttpsURLConnection)urlConnection;
        httpsURLConnection.setSSLSocketFactory(socketFactory);
        urlConnection.setRequestProperty("ping.uname", "changeme");
        urlConnection.setRequestProperty("ping.pwd", "this is a default example and should not be used in production");
        urlConnection.setRequestProperty("ping.instanceId", "idpadapter");

        // Write the attributes in URL Connection, this example uses UTF-8 encoding
        urlConnection.setDoOutput(true);
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(urlConnection.getOutputStream(), StandardCharsets.UTF_8);
        idpUserAttributes.writeJSONString(outputStreamWriter);
        outputStreamWriter.flush();
        outputStreamWriter.close();

        // Get the response and parse it into a JSON object
        InputStream is = urlConnection.getInputStream();
        InputStreamReader streamReader = new InputStreamReader(is, StandardCharsets.UTF_8);

        JSONParser parser = new JSONParser();
        JSONObject jsonRespObj = (JSONObject)parser.parse(streamReader);

        // Grab the value of the reference Id from the JSON Object. This value 
        // must be passed to PingFederate on resumePath as the parameter 'REF'
        String referenceValue = (String)jsonRespObj.get("REF");
        System.out.println("Reference ID = " + referenceValue);
    }
}
