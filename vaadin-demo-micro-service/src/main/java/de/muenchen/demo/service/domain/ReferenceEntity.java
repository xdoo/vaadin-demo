package de.muenchen.demo.service.domain;

import org.hibernate.envers.NotAudited;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;

/**
 *
 * @author claus.straube
 */
@MappedSuperclass
public abstract class ReferenceEntity implements Serializable {

    @Id
    @GeneratedValue
    @Column(name = "ID")
    private Long id;

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReferencedOid() {
        return referencedOid;
    }

    public void setReferencedOid(String referencedOid) {
        this.referencedOid = referencedOid;
    }

}
