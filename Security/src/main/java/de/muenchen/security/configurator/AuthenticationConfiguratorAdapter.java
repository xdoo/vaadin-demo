package de.muenchen.security.configurator;

import org.springframework.security.config.annotation.authentication.configurers.GlobalAuthenticationConfigurerAdapter;

import java.util.Iterator;
import java.util.ServiceConfigurationError;
import java.util.ServiceLoader;

/**
 * Created by rene.zarwel on 21.08.15.
 */
public abstract class AuthenticationConfiguratorAdapter extends GlobalAuthenticationConfigurerAdapter {


    public static AuthenticationConfiguratorAdapter findAdapter(String serviceTyp){
        Iterator<AuthenticationConfiguratorAdapter> secManagers = ServiceLoader.load(AuthenticationConfiguratorAdapter.class).iterator();
        while (secManagers.hasNext()) {
            try {
                AuthenticationConfiguratorAdapter secManager = secManagers.next();
                if (secManager.accepts(serviceTyp)) {
                    return secManager;
                }
            }
            catch (ServiceConfigurationError e){
                // TODO do something clever here
            }
        }
        return null;

    }

    public abstract boolean accepts(String serviceTyp);


}
