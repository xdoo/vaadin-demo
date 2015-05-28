/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.muenchen.vaadin.domain;

import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "PERSONEN")
public class Person extends AuditingBaseEntity {
    
    @Column(length = 50, nullable = true, name = "PERS_FIRSTNAME")
    private String firstname;
    
    @Column(length = 50, nullable = false, name = "PERS_LASTNAME")
    private String lastname;
    
    @Column(name = "PERS_BIRTHDATE")
    @Temporal(TemporalType.DATE)
    private Date birthdate;
    
    @OneToMany(cascade = {CascadeType.REFRESH}, fetch = FetchType.EAGER)
    private List<Wohnung> wohnungen;
    
    @OneToMany(cascade = {CascadeType.REFRESH}, fetch = FetchType.EAGER)
    private List<Sachbearbeiter> sachbearbeiter;
    
    @Column(length = 30, nullable = true, name = "PERS_STAATSANGEHOERIGKEIT1")
    private String staatsangehoerigkeit1;
    
    @Column(length = 30, nullable = true, name = "PERS_STAATSANGEHOERIGKEIT2")
    private String staatsangehoerigkeit2;
    
    @Column(length = 30, nullable = true, name = "PERS_STAATSANGEHOERIGKEIT3")
    private String staatsangehoerigkeit3;
    
    @Column(length = 30, nullable = true, name = "PERS_STAATSANGEHOERIGKEIT4")
    private String staatsangehoerigkeit4;
    
    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public Date getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(Date birthdate) {
        this.birthdate = birthdate;
    }

    public List<Wohnung> getWohnungen() {
        return wohnungen;
    }

    public void setWohnungen(List<Wohnung> wohnungen) {
        this.wohnungen = wohnungen;
    }

    public List<Sachbearbeiter> getSachbearbeiter() {
        return sachbearbeiter;
    }

    public void setSachbearbeiter(List<Sachbearbeiter> sachbearbeiter) {
        this.sachbearbeiter = sachbearbeiter;
    }

    public String getStaatsangehoerigkeit1() {
        return staatsangehoerigkeit1;
    }

    public void setStaatsangehoerigkeit1(String staatsangehoerigkeit1) {
        this.staatsangehoerigkeit1 = staatsangehoerigkeit1;
    }

    public String getStaatsangehoerigkeit2() {
        return staatsangehoerigkeit2;
    }

    public void setStaatsangehoerigkeit2(String staatsangehoerigkeit2) {
        this.staatsangehoerigkeit2 = staatsangehoerigkeit2;
    }

    public String getStaatsangehoerigkeit3() {
        return staatsangehoerigkeit3;
    }

    public void setStaatsangehoerigkeit3(String staatsangehoerigkeit3) {
        this.staatsangehoerigkeit3 = staatsangehoerigkeit3;
    }

    public String getStaatsangehoerigkeit4() {
        return staatsangehoerigkeit4;
    }

    public void setStaatsangehoerigkeit4(String staatsangehoerigkeit4) {
        this.staatsangehoerigkeit4 = staatsangehoerigkeit4;
    }

    @Override
    public String toString() {
        return String.format("id: %s | firstname: %s | lastname: %s | birthdate: %s", this.getId(), this.firstname, this.lastname, this.birthdate);
    }
    
    
    
}
