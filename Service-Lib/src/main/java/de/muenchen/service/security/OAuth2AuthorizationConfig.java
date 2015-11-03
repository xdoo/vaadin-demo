package de.muenchen.service.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import java.util.Set;

/**
 * Created by rene.zarwel on 20.10.15.
 */

@Configuration
@EnableAuthorizationServer
public class OAuth2AuthorizationConfig extends AuthorizationServerConfigurerAdapter {

    @Value("${security.oauth2.client.access-token-validity-seconds:43200}")
    private int tokenValiditySeconds;

    @Value("${security.oauth2.client.id}")
    private String clientID;

    @Value("${security.oauth2.client.scope:defaultScope}")
    private Set<String> scopes;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.authenticationManager(authenticationManager).accessTokenConverter(jwtAccessTokenConverter());
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                .withClient(clientID)
                .autoApprove(true)
                .authorizedGrantTypes("password")
                .accessTokenValiditySeconds(tokenValiditySeconds)
                .scopes(scopes.toArray(new String[1]));
    }

    //TODO GET A OWN KEYPAIR
    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();

        //Sample for own Keypair
//        KeyPair keyPair = new KeyStoreKeyFactory(
//                new ClassPathResource("server.jks"), "123456".toCharArray())
//                .getKeyPair("mytestkey", "123456".toCharArray());
//        converter.setKeyPair(keyPair);

        return converter;
    }

}
