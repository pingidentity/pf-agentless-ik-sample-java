package com.pingidentity.sample.servlet;


import com.pingidentity.sample.configuration.ConfigurationManager;
import com.pingidentity.sample.configuration.SpSampleConstants;
import com.pingidentity.sample.exception.ConfigurationException;
import com.pingidentity.sample.handler.RequestHandler;
import com.pingidentity.sample.handler.RequestHandlerFactory;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpHeaders;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class SpSampleServlet extends HttpServlet
{
    private RequestHandlerFactory requestHandlerFactory = new RequestHandlerFactory();

    private static final Log logger = LogFactory.getLog(SpSampleServlet.class);

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException
    {
        doPost(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException
    {
        try
        {
            ConfigurationManager.getInstance().load(request, getServletContext());
        }
        catch (ConfigurationException e)
        {
            if (e.getCause() != null)
            {
                logger.error(e.getMessage(), e.getCause());
            }
            else
            {
                logger.error("An error occurred while loading configuration: ", e);
            }
            forward(SpSampleConstants.ERROR_JSP_PATH, request, response);
        }

        RequestHandler requestHandler = requestHandlerFactory.getHandler(request);

        String nextPage = (requestHandler != null) ?
                          requestHandler.handle(request, response, getServletContext()) :
                          SpSampleConstants.NOT_FOUND_JSP_PATH;

        forward(nextPage, request, response);
    }

    private void forward(String url, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException
    {
        if (StringUtils.isNotBlank(url))
        {
            String path = getUrlPath(url);

            if (path.contains(".jsp"))
            {
                request.getRequestDispatcher(url).forward(request, response);
            }
            else if (path.contains(SpSampleConstants.AGENTLESS_SP_SAMPLE_PATH))
            {
                response.setHeader(HttpHeaders.LOCATION, url);
                response.setStatus(HttpServletResponse.SC_SEE_OTHER);
            }
            else
            {
                response.sendRedirect(url);
            }
        }
        else
        {
            request.getRequestDispatcher(SpSampleConstants.NOT_FOUND_JSP_PATH).forward(request, response);
        }
    }

    private String getUrlPath(String url) throws MalformedURLException
    {
        if (!url.startsWith("/"))
        {
            URL redirectUrl = new URL(url);
            return redirectUrl.getPath();
        }

        return url;
    }
}
