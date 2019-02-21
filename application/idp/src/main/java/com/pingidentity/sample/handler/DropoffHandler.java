package com.pingidentity.sample.handler;

import com.pingidentity.sample.configuration.IdpSampleConstants;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DropoffHandler extends RequestHandler
{
    @Override
    public String handle(HttpServletRequest request, HttpServletResponse response, ServletContext servletContext)
    {
        return IdpSampleConstants.DROPOFF_JSP_PATH;
    }
}
