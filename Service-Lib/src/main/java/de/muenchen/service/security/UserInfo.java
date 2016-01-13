package de.muenchen.service.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

/**
 * Costum UserDetails with tenant.
 * Created by huningd on 17.12.15.
 */
public class UserInfo extends User {

    public static final String TENANT = "tenant";

    private String tenant;

    public UserInfo(String username, String password, String tenant, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.tenant = tenant;
    }

    public UserInfo(String username, String password, boolean enabled, String tenant, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        this.tenant = tenant;
    }

    public String getTenant() {
        return tenant;
    }

    public void setTenant(String tenant) {
        this.tenant = tenant;
    }
}
