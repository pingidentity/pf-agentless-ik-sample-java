package com.pingidentity.sample.authentication;

import com.pingidentity.sample.configuration.ConfigurationManager;
import com.pingidentity.sample.configuration.IdpSampleConfiguration;

import java.util.List;

public class Authenticator
{
    public User authenticateUser(String username, String password)
    {
        IdpSampleConfiguration idpSampleConfiguration = ConfigurationManager.getInstance().getIdpSampleConfiguration();
        List<User> users = idpSampleConfiguration.getUsers();

        for (User user : users)
        {
            if (username.equals(user.getUsername()) && password.equals(user.getPassword()))
            {
                return user;
            }
        }

        return null;
    }
}
