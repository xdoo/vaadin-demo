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
    
    // Muss über eine Referenz in das verwaltende System gelöst werden.
//    private List<Staatsangehoerigkeit> staatsangehoerigkeiten;
    
    @OneToMany(cascade = {CascadeType.REFRESH}, fetch = FetchType.EAGER)
    private List<Sachbearbeiter> sachbearbeiter;
    
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

    @Override
    public String toString() {
        return String.format("id: %s | firstname: %s | lastname: %s | birthdate: %s", this.getId(), this.firstname, this.lastname, this.birthdate);
    }
    
    
    
}
