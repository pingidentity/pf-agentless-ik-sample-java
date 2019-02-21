package com.pingidentity.sample.handler;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class RequestHandler
{
    public abstract String handle(
            HttpServletRequest request,
            HttpServletResponse response,
            ServletContext servletContext);
}
