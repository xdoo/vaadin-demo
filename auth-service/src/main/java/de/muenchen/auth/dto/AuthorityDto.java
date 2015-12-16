package de.muenchen.auth.dto;

import java.util.List;

/**
 * Created by huningd on 16.12.15.
 */
public class AuthorityDto {

    String authority;

    List<String> permissions;

    public List<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<String> permissions) {
        this.permissions = permissions;
    }

    public String getAuthority() {

        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }
}
