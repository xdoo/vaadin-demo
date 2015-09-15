
package de.muenchen.demo.service.domain;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;



/**
 *
 * @author praktikant.tmar
 */
@Embeddable
public class AuthPermId implements Serializable {

   @ManyToOne(cascade = {CascadeType.REFRESH}, fetch = FetchType.LAZY)
   @JoinColumn(name = "PERMISSION_OID", referencedColumnName = "OID")
   private Permission permission;
   @ManyToOne(cascade = {CascadeType.REFRESH}, fetch = FetchType.LAZY)
   @JoinColumn(name = "AUTHORITY_OID", referencedColumnName = "OID")
   private Authority authority;

    public AuthPermId() {
    }

    public AuthPermId(Permission permission, Authority authority) {
        this.permission = permission;
        this.authority = authority;
    }

    public Permission getPermission() {
        return permission;
    }

    public void setPermission(Permission permission) {
        this.permission = permission;
    }

    public Authority getAuthority() {
        return authority;
    }

    public void setAuthority(Authority authority) {
        this.authority = authority;
    }



}