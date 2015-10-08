package de.muenchen.security.entities;

import java.io.Serializable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 *
 * @author praktikant.tmar
 */
@Entity
@Table(name = "AUTHORITYS_PERMISSIONS")
public class AuthorityPermission implements Serializable {

    @EmbeddedId
    AuthPermId id;

    public AuthPermId getId() {
        return id;
    }

    public void setId(AuthPermId id) {
        this.id = id;
    }

}
