package com.pingidentity.sample.handler;

import com.pingidentity.sample.configuration.SpSampleConstants;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoggedOutHandler extends RequestHandler
{
    @Override
    public String handle(HttpServletRequest request, HttpServletResponse response, ServletContext servletContext)
    {
        return SpSampleConstants.LOGGED_OUT_JSP_PATH;
    }
}
