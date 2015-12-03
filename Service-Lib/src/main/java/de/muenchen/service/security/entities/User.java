/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.muenchen.service.security.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.muenchen.service.BaseEntity;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

/**
 *
 * @author praktikant.tmar
 */
@Entity
@Indexed
@Table(name = "_USERS")
public class User extends BaseEntity implements Serializable {

    @Field
    @Column(name = "USER_USERNAME", nullable = false, updatable = false)
    private String username;

    @JsonIgnore
    @Column(name = "USER_PASSWORD")
    private String password;

    @Field
    @Column(name = "USER_FORNAME")
    private String forname;

    @Field
    @Column(name = "USER_SURNAME")
    private String surname;

    @Field
    @Column(name = "USER_BIRTHDATE")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date birthdate;

    @Field
    @Column(name = "USER_EMAIL")
    private String email;

    @Column(name = "USER_ENABLED")
    private boolean userEnabled;

    @ManyToMany(cascade = {CascadeType.REFRESH}, fetch = FetchType.EAGER)
    @JoinTable(name = "_users_authorities", joinColumns = {@JoinColumn(name = "user_oid")}, inverseJoinColumns = {@JoinColumn(name = "authority_oid")})
    private Set<Authority> authoritys;

    public User() {
    }

    public User(User user) {
        this.username = user.username;
        this.password = user.password;
        this.forname = user.forname;
        this.surname = user.surname;
        this.birthdate = user.birthdate;
        this.email = user.email;
        this.userEnabled = user.userEnabled;
        this.authoritys = user.authoritys;
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

    public boolean isUserEnabled() {
        return userEnabled;
    }

    public void setUserEnabled(boolean userEnabled) {
        this.userEnabled = userEnabled;
    }

    public Set<Authority> getAuthoritys() {
        return authoritys;
    }

    public void setAuthoritys(Set<Authority> authoritys) {
        this.authoritys = authoritys;
    }
}
