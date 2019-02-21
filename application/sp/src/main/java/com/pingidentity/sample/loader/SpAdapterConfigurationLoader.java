package com.pingidentity.sample.loader;

import com.pingidentity.sample.configuration.SpSampleConfiguration;
import com.pingidentity.sample.exception.ConfigurationException;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.StringReader;
import java.util.Date;
import java.util.Properties;

public class SpAdapterConfigurationLoader extends ConfigurationLoader
{
    private static final String INIT_PARAM_CONFIGURATION_FILE = "sp-adapter-configuration";

    @Override
    public void load(
            HttpServletRequest request,
            ServletContext servletContext,
            SpSampleConfiguration spSampleConfiguration) throws ConfigurationException
    {
        configurationPath = servletContext.getInitParameter(INIT_PARAM_CONFIGURATION_PATH);
        configurationFileName = servletContext.getInitParameter(INIT_PARAM_CONFIGURATION_FILE);
        realPathToConfigurationFile = servletContext.getRealPath(getConfigurationFilePath());

        if (lastReloaded.before(getFileLastModified()))
        {
            String rawProperties = loadConfigurationFile();
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

            spSampleConfiguration.setSpAdapterConfiguration(properties);

            lastReloaded = new Date();
        }
    }
}
