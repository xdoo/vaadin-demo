package de.muenchen.demo.test;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.UUID;

/**
 * model of JWT token
 * based on an example of wtran
 *
 */
@JsonInclude(Include.NON_NULL)
public class JwtToken {
    private String jti;
    private Collection<String> authorities;
    private Collection<String> scope;
    private String client_id;
    private String user_name;
    private long exp;
    private String tenant;

    public static JwtTokenBuilder builder() {
        return new JwtTokenBuilder();
    }

    public static class JwtTokenBuilder {

        public static enum GrantType {
            implicit, client_credentials, authorization_code, password
        }

        private final JwtToken token;

        JwtTokenBuilder() {
            token = new JwtToken();
            token.setJti(UUID.randomUUID().toString());
            token.setExp(System.currentTimeMillis() + 1000 * 60 * 60 * 24);
        }

        public JwtTokenBuilder scopes(String... scopes) {
            if (token.getScope() == null) {
                token.setScope(new LinkedHashSet<>());
            }
            token.getScope().addAll(Arrays.asList(scopes));
            return this;
        }


        public JwtTokenBuilder authorities(String... authorities) {
            if (token.getAuthorities() == null) {
                token.setAuthorities(new LinkedHashSet<>());
            }
            token.getAuthorities().addAll(Arrays.asList(authorities));
            return this;
        }

        public JwtTokenBuilder clearScopes() {
            token.setScope(null);
            return this;
        }

        public JwtTokenBuilder clientId(String clientId) {
            token.setClient_id(clientId);
            return this;
        }

        public JwtTokenBuilder user(String userName, String tenant) {
            token.setUser_name(userName);
            token.setTenant(tenant);
            return this;
        }

        public JwtToken build() {
            LinkedHashSet<String> aud = new LinkedHashSet<>();
            if (token.getScope() != null) {
                token.getScope().forEach(scope -> aud.add(scope.substring(0, scope.lastIndexOf('.') > 0 ? scope.lastIndexOf('.') : scope.length())));
            }
            return token;
        }
    }

    public String getJti() {
        return jti;
    }

    public void setJti(String jti) {
        this.jti = jti;
    }

    public Collection<String> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Collection<String> authorities) {
        this.authorities = authorities;
    }

    public Collection<String> getScope() {
        return scope;
    }

    public void setScope(Collection<String> scope) {
        this.scope = scope;
    }

    public String getClient_id() {
        return client_id;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public long getExp() {
        return exp;
    }

    public void setExp(long exp) {
        this.exp = exp;
    }


    public void setTenant(String tenant) {
        this.tenant = tenant;
    }

    public String getTenant() {
        return tenant;
    }
}

