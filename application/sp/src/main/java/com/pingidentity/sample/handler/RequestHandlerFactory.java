package com.pingidentity.sample.handler;

import com.pingidentity.sample.configuration.SpSampleConstants;

import javax.servlet.http.HttpServletRequest;

public class RequestHandlerFactory
{
    private RequestHandler appHandler;
    private RequestHandler configurationHandler;
    private RequestHandler logoutHandler;
    private RequestHandler loggedOutHandler;

    public RequestHandlerFactory()
    {
        appHandler = new AppHandler();
        configurationHandler = new ConfigurationHandler();
        logoutHandler = new LogoutHandler();
        loggedOutHandler = new LoggedOutHandler();
    }

    public RequestHandler getHandler(HttpServletRequest request)
    {
        RequestHandler requestHandler = null;
        String action = request.getParameter(SpSampleConstants.ACTION_PARAM);

        if (action == null)
        {
            return appHandler;
        }

        switch (action)
        {
            case SpSampleConstants.ACTION_PARAM_CONFIGURE:
                requestHandler = configurationHandler;
                break;
            case SpSampleConstants.ACTION_PARAM_LOGOUT:
                requestHandler = logoutHandler;
                break;
            case SpSampleConstants.ACTION_PARAM_LOGGED_OUT:
                requestHandler = loggedOutHandler;
                break;
        }

        return requestHandler;
    }
}
