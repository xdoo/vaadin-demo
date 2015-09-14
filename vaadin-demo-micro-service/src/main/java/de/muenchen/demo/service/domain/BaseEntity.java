package de.muenchen.demo.service.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.hibernate.search.annotations.IndexedEmbedded;

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
@Audited
@MappedSuperclass
public abstract class BaseEntity implements Cloneable, Serializable {

    @Column(name = "OID")
    @Size(max = 32)
    @Id
    private String oid;

    @IndexedEmbedded(depth = 1, prefix = "mandant")

    @NotAudited
    @Column(length = 30, unique = true, nullable = true, name = "mandant")
    @JsonIgnore
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

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
