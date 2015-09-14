package de.muenchen.demo.service.domain;

import org.hibernate.envers.NotAudited;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import javax.validation.constraints.Size;

/**
 *
 * @author claus.straube
 */
@MappedSuperclass
public abstract class ReferenceEntity implements Serializable {

    @Column(name = "OID")
    @Size(max = 32)
    @Id
    private String oid;

    @Column(length = 100, nullable = false, name = "REF_OID")
    private String referencedOid;

    @NotAudited
    @Column(length = 30, unique = true, nullable = true, name = "mandant")
    private String mandant;

    public String getMandant() {
        return mandant;
    }

    public void setMandant(String mandant) {
        this.mandant = mandant;
    }

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public String getReferencedOid() {
        return referencedOid;
    }

    public void setReferencedOid(String referencedOid) {
        this.referencedOid = referencedOid;
    }

}
