package com.pingidentity.sample.handler;

import com.pingidentity.sample.configuration.IdpSampleConstants;

import javax.servlet.http.HttpServletRequest;

public class RequestHandlerFactory
{
    private RequestHandler appHandler;
    private RequestHandler loginHandler;
    private RequestHandler configurationHandler;
    private RequestHandler dropoffHandler;
    private RequestHandler resumeHandler;
    private RequestHandler logoutHandler;

    public RequestHandlerFactory()
    {
        appHandler = new AppHandler();
        loginHandler = new LoginHandler();
        configurationHandler = new ConfigurationHandler();
        dropoffHandler = new DropoffHandler();
        resumeHandler = new ResumeHandler();
        logoutHandler = new LogoutHandler();
    }

    public RequestHandler getHandler(HttpServletRequest request)
    {
        RequestHandler requestHandler = null;
        String action = request.getParameter(IdpSampleConstants.ACTION_PARAM);

        if (action == null)
        {
            return appHandler;
        }

        switch (action)
        {
            case IdpSampleConstants.ACTION_PARAM_LOGIN:
                requestHandler = loginHandler;
                break;
            case IdpSampleConstants.ACTION_PARAM_CONFIGURE:
                requestHandler = configurationHandler;
                break;
            case IdpSampleConstants.ACTION_PARAM_DROPOFF:
                requestHandler = dropoffHandler;
                break;
            case IdpSampleConstants.ACTION_PARAM_RESUME:
                requestHandler = resumeHandler;
                break;
            case IdpSampleConstants.ACTION_PARAM_LOGOUT:
                requestHandler = logoutHandler;
                break;
        }

        return requestHandler;
    }
}
