package de.muenchen.vaadin.demo.apilib.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by dennis_huning on 08.12.15.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Authority {

    private String authority;

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }
}
