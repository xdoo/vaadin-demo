package de.muenchen.vaadin.domain;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import org.hibernate.envers.Audited;

/**
 * Basis Klasse für alle in der Anwendung gespeicherten Entities.
 * 
* @author claus
 */
@MappedSuperclass
@Audited
public abstract class AuditingBaseEntity implements Serializable {

    @Id
    @GeneratedValue
    @Column(name = "ID")
    private Long id;
    
    @Column(length = 30, unique = true, nullable = false, name = "OID")
    private String oid;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public String getOid() {
        return oid;
    }
}
