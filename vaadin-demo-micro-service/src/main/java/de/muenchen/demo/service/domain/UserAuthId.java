package de.muenchen.demo.service.domain;

import java.io.Serializable;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 *
 * @author praktikant.tmar
 */
@Embeddable
public class UserAuthId implements Serializable {

    @ManyToOne
    @JoinColumn(name = "USER_ID", referencedColumnName = "ID")
    private User user;
    @ManyToOne
    @JoinColumn(name = "AUTHORITY_ID", referencedColumnName = "ID")
    private Authority authority;

    public UserAuthId() {
    }

    public UserAuthId(User user, Authority authority) {
        this.user = user;
        this.authority = authority;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Authority getAuthority() {
        return authority;
    }

    public void setAuthority(Authority authority) {
        this.authority = authority;
    }

}
