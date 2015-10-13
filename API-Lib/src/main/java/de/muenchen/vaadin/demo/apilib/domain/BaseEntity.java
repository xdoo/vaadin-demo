package de.muenchen.vaadin.demo.apilib.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.envers.NotAudited;
import org.hibernate.search.annotations.IndexedEmbedded;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 *
 * @author claus.straube
 */
@MappedSuperclass
public class BaseEntity {
	@Id
	@GeneratedValue
    private Long oid;

    @IndexedEmbedded(depth = 1, prefix = "mandant")
    @NotAudited
    @Column(length = 30, unique = true, nullable = true, name = "mandant")
    @JsonIgnore
    private String mandant;

    public Long getOid() {
        return oid;
    }

    public void setOid(Long oid) {
        this.oid = oid;
    }
    public String getMandant() {
        return mandant;
    }

    public void setMandant(String mandant) {
        this.mandant = mandant;
    }

}
