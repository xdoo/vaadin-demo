/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.muenchen.itm.infrastructure.auth.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.muenchen.service.BaseEntity;
import de.muenchen.service.PetersPerfectBridge;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.FieldBridge;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.validator.constraints.Email;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

/**
 * @author praktikant.tmar
 */
@Entity
@Indexed
@Table(name = "_USERS")
public class User extends BaseEntity implements Serializable {

    @Field
    @Column(name = "USER_USERNAME", nullable = false, updatable = false, unique = true)
    @NotNull
    @Pattern(regexp="[a-zA-Z0-9_\\.-]*")
    @Size(min=1)
    private String username;

    @JsonIgnore
    @Column(name = "USER_PASSWORD")
    private String password;

    @Field
    @Column(name = "USER_FORNAME")
    @NotNull
    @Pattern(regexp="\\p{L}*")
    @Size(min=1)
    private String forname;

    @Field
    @Column(name = "USER_SURNAME")
    @NotNull
    @Pattern(regexp = "\\p{L}*")
    @Size(min=1)
    private String surname;

    @Field
    @FieldBridge(impl = PetersPerfectBridge.class)
    @Column(name = "USER_BIRTHDATE")
    @Temporal(TemporalType.DATE)
    private Date birthdate;

    @Field
    @Column(name = "USER_EMAIL")
    @Email
    private String email;

    @Column(name = "USER_ENABLED")
    private boolean userEnabled;

    @ManyToMany(cascade = {CascadeType.REFRESH}, fetch = FetchType.EAGER)
    @JoinTable(name = "_users_authorities", joinColumns = {@JoinColumn(name = "user_oid")}, inverseJoinColumns = {@JoinColumn(name = "authority_oid")})
    private Set<Authority> authorities;

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
        this.authorities = user.authorities;
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

    public Set<Authority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Set<Authority> authorities) {
        this.authorities = authorities;
    }

    public boolean isAdmin() {
        return getAuthorities().stream().anyMatch(authority -> authority.getAuthority().equals(Authority.ADMIN_AUTHORITY));
    }
}

