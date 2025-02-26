package com.pingidentity.referenceid.util;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class ReferenceIdAdapterResponse implements Serializable
{
    private Map<String, List<String>> responseHeaders;
    private String responseBody;
    private String responseCode;
    private String responseMessage;

    // tracked for display purposes
    private String requestAuthorizationHeaderValue;

    public ReferenceIdAdapterResponse(String responseCode, String responseMessage, Map<String, List<String>> responseHeaders, String responseBody)
    {
        this.responseCode = responseCode;
        this.responseMessage = responseMessage;
        this.responseHeaders = responseHeaders;
        this.responseBody = responseBody;
    }

    public String getResponseCode()
    {
        return responseCode;
    }

    public String getResponseMessage()
    {
        return responseMessage;
    }

    public Map<String, List<String>> getResponseHeaders()
    {
        return responseHeaders;
    }

    public String getResponseBody()
    {
        return responseBody;
    }

    public String getRequestAuthorizationHeaderValue()
    {
        return requestAuthorizationHeaderValue;
    }

    public void setRequestAuthorizationHeaderValue(String requestAuthorizationHeaderValue)
    {
        this.requestAuthorizationHeaderValue = requestAuthorizationHeaderValue;
    }
}
