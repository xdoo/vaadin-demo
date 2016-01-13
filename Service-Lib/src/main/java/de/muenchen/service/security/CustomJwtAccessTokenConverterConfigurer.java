package de.muenchen.service.security;

import org.springframework.boot.autoconfigure.security.oauth2.resource.JwtAccessTokenConverterConfigurer;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

/**
 * Setzt den Konverter für die OAuth-Tokens des AuthService
 */
@Configuration
public class CustomJwtAccessTokenConverterConfigurer implements JwtAccessTokenConverterConfigurer {

    @Override
    public void configure(JwtAccessTokenConverter converter) {
        DefaultAccessTokenConverter accessTokenConverter = new DefaultAccessTokenConverter();
        accessTokenConverter.setUserTokenConverter(new CustomResourceUserAuthenticationConverter());
        converter.setAccessTokenConverter(accessTokenConverter);
    }
}