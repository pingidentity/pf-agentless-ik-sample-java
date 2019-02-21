package com.pingidentity.sample.loader;

import com.pingidentity.sample.configuration.IdpSampleConfiguration;
import com.pingidentity.sample.exception.ConfigurationException;
import org.apache.commons.io.FileUtils;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;

public abstract class ConfigurationLoader
{
    static final String INIT_PARAM_CONFIGURATION_PATH = "configuration-path";
    Date lastReloaded = new Date(0);
    String configurationPath;
    String realPathToConfigurationFile;
    String configurationFileName;

    public abstract void load(
            HttpServletRequest request,
            ServletContext servletContext,
            IdpSampleConfiguration idpSampleConfiguration) throws ConfigurationException;

    public String getRealPathToConfigurationFile()
    {
        return realPathToConfigurationFile;
    }

    Date getFileLastModified() throws ConfigurationException
    {
        File configurationFile = new File(realPathToConfigurationFile);
        Date lastModified;

        if (!configurationFile.exists() || !configurationFile.isFile())
        {
            String error = String.format("File not found: %s", configurationFileName);
            throw new ConfigurationException(error);
        }

        lastModified = new Date(configurationFile.lastModified());

        return lastModified;
    }

    String getConfigurationFilePath()
    {
        return configurationPath + configurationFileName;
    }

    String loadConfigurationFile() throws ConfigurationException
    {
        String fileContents;

        try
        {
            fileContents = FileUtils.readFileToString(new File(realPathToConfigurationFile), StandardCharsets.UTF_8);
        }
        catch (IOException e)
        {
            String error = String.format("Can't load properties from file: %s", configurationFileName);
            throw new ConfigurationException(error);
        }

        return fileContents;
    }
}
