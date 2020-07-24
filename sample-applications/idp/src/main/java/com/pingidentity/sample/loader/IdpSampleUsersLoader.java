package com.pingidentity.sample.loader;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pingidentity.sample.authentication.User;
import com.pingidentity.sample.configuration.IdpSampleConfiguration;
import com.pingidentity.sample.exception.ConfigurationException;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.io.StringReader;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;

public class IdpSampleUsersLoader extends ConfigurationLoader
{
    private static final String INIT_PARAM_CONFIGURATION_FILE = "idp-sample-users";

    @Override
    public void load(
            HttpServletRequest request,
            ServletContext servletContext,
            IdpSampleConfiguration idpSampleConfiguration) throws ConfigurationException
    {
        configurationPath = servletContext.getInitParameter(INIT_PARAM_CONFIGURATION_PATH);
        configurationFileName = servletContext.getInitParameter(INIT_PARAM_CONFIGURATION_FILE);
        realPathToConfigurationFile = servletContext.getRealPath(getConfigurationFilePath());

        if (lastReloaded.before(getFileLastModified()))
        {
            String rawJson = loadConfigurationFile();

            Gson gson = new Gson();
            StringReader stringReader = new StringReader(rawJson);

            Type listOfUserObject = new TypeToken<List<User>>(){}.getType();
            List<User> users = gson.fromJson(stringReader, listOfUserObject);

            idpSampleConfiguration.setUsers(users);

            lastReloaded = new Date();
        }
    }
}
