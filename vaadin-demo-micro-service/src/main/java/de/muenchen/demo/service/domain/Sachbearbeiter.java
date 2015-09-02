package de.muenchen.demo.service.domain;

import org.hibernate.envers.Audited;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 *
 * @author claus.straube
 */
@Entity
@Audited
@Table(name = "SACHBEARBEITER")
public class Sachbearbeiter extends BaseEntity {

    @Column(length = 50, name = "SACH_TELEPHONE")
    String telephone;

    @Column(length = 50, name = "SACH_FUNKTION")
    String funktion;

    @Column(length = 50, name = "SACH_FAX")
    String fax;

    @Column(length = 50, name = "SACH_ORGANISATIONSEINHEIT")
    String organisationseinheit;

    @OneToOne
    @JoinColumn(name = "User_Id", referencedColumnName = "Id")
    private User user;

    @OneToMany(cascade = {CascadeType.REFRESH}, fetch = FetchType.EAGER)
    private Set<Buerger> buerger = new HashSet<>();

    ;

    public Set<Buerger> getBuerger() {
        return buerger;
    }

    public void setBuerger(Set<Buerger> buerger) {
        this.buerger = buerger;
    }

    public Sachbearbeiter() {
    }

    public Sachbearbeiter(Sachbearbeiter sachbearbeiter) {
        this.telephone = sachbearbeiter.telephone;
        this.fax = sachbearbeiter.fax;
        this.funktion = sachbearbeiter.funktion;
        this.organisationseinheit = sachbearbeiter.organisationseinheit;
        this.buerger.addAll(sachbearbeiter.buerger);
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getFunktion() {
        return funktion;
    }

    public void setFunktion(String funktion) {
        this.funktion = funktion;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getOrganisationseinheit() {
        return organisationseinheit;
    }

    public void setOrganisationseinheit(String organisationseinheit) {
        this.organisationseinheit = organisationseinheit;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return String.format("id > %s | oid > %s | fax > %s | funktion > %s| organisationseinheit > %s | telephone > %s", this.getId(), this.getOid(), this.fax, this.funktion, this.organisationseinheit, this.telephone);
    }
}
