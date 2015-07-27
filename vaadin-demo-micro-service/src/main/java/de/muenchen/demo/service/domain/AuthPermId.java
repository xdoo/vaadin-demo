
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
public class AuthPermId implements Serializable {

   @ManyToOne@JoinColumn(name="PERMISSION_ID", referencedColumnName = "ID")   
   private Permission permission;
   @ManyToOne@JoinColumn(name="AUTHORITY_ID", referencedColumnName = "ID")   
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