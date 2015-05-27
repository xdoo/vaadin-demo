/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.muenchen.vaadin.domain;

import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "PERSONEN")
public class Person extends AuditingBaseEntity {
    
    @Column(length = 30, nullable = false, name = "ACCOUNT_OID")
    private String accountOid;
    
    @Column(length = 50, nullable = true, name = "PERS_FIRSTNAME")
    private String firstname;
    
    @Column(length = 50, nullable = false, name = "PERS_LASTNAME")
    private String lastname;
    
    @Column(name = "PERS_BIRTHDATE")
    @Temporal(TemporalType.DATE)
    private Date birthdate;
    
    @OneToMany(mappedBy = "buerger")
    private List<Wohnung> wohnungen;
    
//    @OneToMany(mappedBy = "buerger")
//    private List<Staatsangehoerigkeit> staatsangehoerigkeiten;
    
    @OneToMany(mappedBy = "buerger")
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

    @Override
    public String toString() {
        return String.format("id: %s | firstname: %s | lastname: %s | birthdate: %s", this.getId(), this.firstname, this.lastname, this.birthdate);
    }
    
    
    
}
