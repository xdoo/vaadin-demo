package de.muenchen.demo.service.domain;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.hibernate.envers.Audited;

/**
 *
 * @author claus.straube
 */
@Entity
@Table(name = "SACHBEARBEITER")
@Audited
public class Sachbearbeiter extends BaseEntity {
    
    @Column(length = 50, nullable = true, name = "SACH_FIRSTNAME")
    String firstname;
    
    @Column(length = 50, nullable = false, name = "SACH_LASTNAME")
    String lastname;
    
    @OneToMany(cascade = {CascadeType.REFRESH}, fetch = FetchType.LAZY)
    private List<Buerger> buerger;

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getLastname() {
        return lastname;
    }

    public List<Buerger> getBuerger() {
        return buerger;
    }

    public void setBuerger(List<Buerger> buerger) {
        this.buerger = buerger;
    }
    
}
