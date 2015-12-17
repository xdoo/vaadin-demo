package de.muenchen.service.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.provider.token.DefaultUserAuthenticationConverter;

import java.util.Collection;
import java.util.Map;

/**
 * Custom converter strategy to convert from an token with tenant as property.
 * Created by huningd on 17.12.15.
 */
public class CustomResourceUserAuthenticationConverter extends DefaultUserAuthenticationConverter {


    @Override
    public Authentication extractAuthentication(Map<String, ?> map) {
        if (map.containsKey(USERNAME)) {
            Collection<? extends GrantedAuthority> authorities = super.extractAuthentication(map).getAuthorities();
            Object principal = map.get(USERNAME);
            Object tenant = map.get(UserInfo.TENANT);
            UserInfo userInfo = new UserInfo((String) principal, "N/A", (String) tenant, authorities);
            return new UsernamePasswordAuthenticationToken(userInfo, "N/A", authorities);
        }
        return null;
    }

}

