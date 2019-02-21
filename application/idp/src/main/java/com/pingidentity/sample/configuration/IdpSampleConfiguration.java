package com.pingidentity.sample.configuration;

import com.pingidentity.sample.authentication.User;

import java.util.List;
import java.util.Properties;

public class IdpSampleConfiguration
{
    private Properties idpAdapterConfiguration;
    private List<User> users;

    public void setIdpAdapterConfiguration(Properties idpAdapterConfiguration)
    {
        this.idpAdapterConfiguration = idpAdapterConfiguration;
    }

    public Properties getIdpAdapterConfiguration()
    {
        return idpAdapterConfiguration;
    }

    public void setUsers(List<User> users)
    {
        this.users = users;
    }

    public List<User> getUsers()
    {
        return users;
    }

}
