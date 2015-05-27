package de.muenchen.vaadin.domain;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * Basis Klasse für alle in der Anwendung gespeicherten Entities.
 * 
* @author claus
 */
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class AuditingBaseEntity implements Serializable {

    @Id
    @GeneratedValue
    @Column(name = "ID")
    private Long id;
    
    @Column(length = 30, unique = true, nullable = false, name = "OID")
    private String oid;
    
    @CreatedBy
    @Column(name = "CREATED_BY")
    private String createdBy;
    
    @LastModifiedBy
    @Column(name = "LAST_MOD_BY")
    private String lastModifiedBy;
    
    @CreatedDate
    @Column(name = "CREATED_DATE")
    private Date createdDate;
    
    @LastModifiedDate
    @Column(name = "LAST_MOD_DATE")
    private Date lastModifiedDate;

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

    public String getCreatedBy() {
        return createdBy;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }
}
