package de.muenchen.demo.service.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import de.muenchen.security.entities.User;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author claus.straube
 */
@Entity
@Table(name = "SACHBEARBEITER")
public class Sachbearbeiter extends BaseEntity {

    @Column(name = "SACH_TELEPHONE")
    @Size(max = 50)
    String telephone;

    @Column(name = "SACH_FUNKTION")
    @Size(max = 50)
    String funktion;

    @Column(name = "SACH_FAX")
    @Size(max = 50)
    String fax;

    @Column(name = "SACH_ORGANISATIONSEINHEIT")
    @Size(max = 50)
    String organisationseinheit;

    @OneToOne
    @JoinColumn(name = "User_Oid", referencedColumnName = "OID")
    private User user;

    @JsonBackReference
    @OneToMany(cascade = {CascadeType.REFRESH}, fetch = FetchType.EAGER)
    private Set<Buerger> buerger = new HashSet<>();

    ;

    public Sachbearbeiter() {
    }

    public Sachbearbeiter(Sachbearbeiter sachbearbeiter) {
        this.telephone = sachbearbeiter.telephone;
        this.fax = sachbearbeiter.fax;
        this.funktion = sachbearbeiter.funktion;
        this.organisationseinheit = sachbearbeiter.organisationseinheit;
        this.buerger.addAll(sachbearbeiter.buerger);
    }

    public Set<Buerger> getBuerger() {
        return buerger;
    }

    public void setBuerger(Set<Buerger> buerger) {
        this.buerger = buerger;
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
        return String.format("oid > %s | fax > %s | funktion > %s| organisationseinheit > %s | telephone > %s", this.getOid(), this.fax, this.funktion, this.organisationseinheit, this.telephone);
    }
}
