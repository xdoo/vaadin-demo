package de.muenchen.demo.service.rest.api;

import java.util.Date;

/**
 *
 * @author claus.straube
 */
public class BuergerResource extends BaseResource {
    
    private String firstname;
    private String lastname;
    private Date birthdate;

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public Date getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(Date birthdate) {
        this.birthdate = birthdate;
    }
    
}
