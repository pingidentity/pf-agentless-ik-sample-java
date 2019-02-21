package com.pingidentity.sample.authentication;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.pingidentity.sample.configuration.IdpSampleConstants;
import com.pingidentity.sample.util.URLUtil;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;
import java.util.Properties;

public class User
{
    private String username;
    private String password;
    private Map<String, Object> attributes;

    public String getUsername()
    {
        return username;
    }

    public String getPassword()
    {
        return password;
    }

    public Map<String, Object> getAttributes()
    {
        return attributes;
    }

    public String getAttributes(String attributeFormat) throws IOException
    {
        Gson gson = new Gson();

        if (IdpSampleConstants.IDP_ADAPTER_CONF_OUTGOING_ATTRIBUTE_FORMAT_JSON.equals(attributeFormat) ||
            IdpSampleConstants.IDP_ADAPTER_CONF_INCOMING_ATTRIBUTE_FORMAT_JSON.equals(attributeFormat))
        {
            return gson.toJson(getAttributes());
        }
        else if (IdpSampleConstants.IDP_ADAPTER_CONF_OUTGOING_ATTRIBUTE_FORMAT_PROPERTIES.equals(attributeFormat))
        {
            Properties properties = new Properties();

            JsonParser jsonParser = new JsonParser();
            JsonElement jsonElement;
            String jsonValue;

            for (Map.Entry entry : getAttributes().entrySet())
            {
                jsonValue = gson.toJson(entry.getValue());
                jsonElement = jsonParser.parse(jsonValue);

                if (jsonElement.isJsonPrimitive())
                {
                    properties.put(entry.getKey(), jsonElement.getAsString());
                }
                {
                    properties.put(entry.getKey(), gson.toJson(jsonValue));
                }
            }

            StringWriter stringWriter = new StringWriter();

            properties.store(stringWriter, "");

            return stringWriter.getBuffer().toString();
        }
        else // QUERY_PARAMETER
        {
            StringBuilder queryStringBuilder = new StringBuilder();
            String queryString;

            JsonParser jsonParser = new JsonParser();
            JsonElement jsonElement;
            String jsonValue;

            for (Map.Entry entry : getAttributes().entrySet())
            {
                queryStringBuilder.append((String) entry.getKey());
                queryStringBuilder.append("=");

                jsonValue = gson.toJson(entry.getValue());
                jsonElement = jsonParser.parse(jsonValue);

                if (jsonElement.isJsonPrimitive())
                {
                    queryStringBuilder.append(URLUtil.encodeParameter(jsonElement.getAsString()));
                }
                else
                {
                    queryStringBuilder.append(URLUtil.encodeParameter(jsonValue));
                }


                queryStringBuilder.append("&");
            }

            queryString = StringUtils.removeEnd(queryStringBuilder.toString(), "&");

            return queryString;
        }
    }

    public void addAttribute(String attributeKey, String attributeValue)
    {
        attributes.put(attributeKey, attributeValue);
    }

}
