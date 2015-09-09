/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.muenchen.demo.service.domain;

import org.hibernate.search.annotations.Indexed;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author praktikant.tmar
 */
@Entity
@Indexed
@Table(name = "USERS")
public class User implements Serializable {

    @Id
    @GeneratedValue
    @Column(name = "ID")
    private Long id;

    @Column(length = 30, unique = true, nullable = false, name = "OID")
    private String oid;
    @Column(name = "USER_USERNAME", nullable = false)
    private String username;

    @Column(name = "USER_PASSWORD")
    private String password;

    @Column(name = "USER_ENABLED")
    private boolean enabled;

    @Column(name = "USER_FORNAME")
    private String forname;

    @Column(name = "USER_SURNAME")
    private String surname;

    @Column(name = "USER_BIRTHDATE")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date birthdate;

    @Column(name = "USER_EMAIL")
    private String email;

    @Column(length = 255, name = "CREATED_BY")
    private String createdBy;

    @Column(name = "CREATED_DATE")
    @Temporal(javax.persistence.TemporalType.DATE)
    private java.util.Date createdDate;

    @Column(length = 255, name = "LAST_MOD_BY")
    private String lastModBy;

    @Column(name = "LAST_MOD_DATE")
    @Temporal(javax.persistence.TemporalType.DATE)
    private java.util.Date lastModDate;

    @OneToMany(cascade = {CascadeType.REFRESH}, fetch = FetchType.LAZY)
    private Set<Account> accounts = new HashSet<>();

    @ManyToOne(cascade = {CascadeType.REFRESH}, fetch = FetchType.EAGER)
    private Mandant mandant;

    public User() {
    }

    public User(User user) {
        this.username = user.username;
        this.password = user.password;
        this.enabled = user.enabled;
        this.forname = user.forname;
        this.surname = user.surname;
        this.birthdate = user.birthdate;
        this.email = user.email;
        this.createdBy = user.createdBy;
        this.createdDate = user.createdDate;
        this.lastModBy = user.lastModBy;
        this.lastModDate = user.lastModDate;
        this.mandant = user.mandant;
    }

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

    public java.util.Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(java.util.Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getLastModBy() {
        return lastModBy;
    }

    public void setLastModBy(String lastModBy) {
        this.lastModBy = lastModBy;
    }

    public java.util.Date getLastModDate() {
        return lastModDate;
    }

    public void setLastModDate(java.util.Date lastModDate) {
        this.lastModDate = lastModDate;
    }

    public Mandant getMandant() {
        return mandant;
    }

    public void setMandant(Mandant mandant) {
        this.mandant = mandant;
    }

    public Set<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(Set<Account> accounts) {
        this.accounts = accounts;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getForname() {
        return forname;
    }

    public void setForname(String forname) {
        this.forname = forname;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public Date getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(Date birthdate) {
        this.birthdate = birthdate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }



}
