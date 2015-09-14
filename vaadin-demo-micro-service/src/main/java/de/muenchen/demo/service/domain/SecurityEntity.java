/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.muenchen.demo.service.domain;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.validation.constraints.Size;

/**
 *
 * @author praktikant.tmar
 */
@MappedSuperclass
public abstract class SecurityEntity  implements Serializable {

    @Id
    @GeneratedValue
    @Column(name = "ID")
    private Long id;

    @Column(unique = true, nullable = false, name = "OID")
    @Size(max = 30)
    private String oid;

    @Column(name = "CREATED_BY")
    @Size(max = 255)
    private String createdBy;

    @Column(name = "CREATED_DATE")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date createdDate;

    @Column(name = "LAST_MOD_BY")
    @Size(max = 255)
    private String lastModBy;

    @Column(name = "LAST_MOD_DATE")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date lastModDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getLastModBy() {
        return lastModBy;
    }

    public void setLastModBy(String lastModBy) {
        this.lastModBy = lastModBy;
    }

    public Date getLastModDate() {
        return lastModDate;
    }

    public void setLastModDate(Date lastModDate) {
        this.lastModDate = lastModDate;
    }

}
