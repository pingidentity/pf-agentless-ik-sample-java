package com.pingidentity.sample.loader;

import com.pingidentity.sample.configuration.IdpSampleConfiguration;
import com.pingidentity.sample.exception.ConfigurationException;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.StringReader;
import java.util.Date;
import java.util.Properties;

public class IdpAdapterConfigurationLoader extends ConfigurationLoader
{
    private static final String INIT_PARAM_CONFIGURATION_FILE = "idp-adapter-configuration";

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
            String rawProperties = loadConfigurationFile();
            // Escape backslashes
            rawProperties = rawProperties.replace("\\", "\\\\");
            Properties properties = new Properties();

            try (StringReader stringReader = new StringReader(rawProperties))
            {
                properties.load(stringReader);
            }
            catch (IOException e)
            {
                String error = String.format("Can't load properties from file: %s", configurationFileName);
                throw new ConfigurationException(error);
            }

            idpSampleConfiguration.setIdpAdapterConfiguration(properties);

            lastReloaded = new Date();
        }
    }
}
