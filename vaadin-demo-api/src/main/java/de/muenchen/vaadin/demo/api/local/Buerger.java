package de.muenchen.vaadin.demo.api.local;

import de.muenchen.vaadin.demo.api.domain.Augenfarbe;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.hateoas.ResourceSupport;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.Set;
import java.util.stream.Stream;

/**
 * Provides the local representation of a Buerger. This is not the DTO Resource!
 * <p>
 *     The ID of a Buerger ({@link Buerger#getId()}) is the self Relation of the DTO Resource.
 * </p>
 *
 * @author p.mueller
 * @version 1.0
 */
public class Buerger extends ResourceSupport {

    /**
     * A mapped Field from the DTO
     */
    @NotEmpty
    @Size
    @Pattern(regexp = "[A-Za-zÀ-ÿ\\-]+")
    private String vorname = "";

    /** A mapped Field from the DTO */
    @NotEmpty
    @Size
    @Pattern(regexp = "[A-Za-zÀ-ÿ\\-]+")
    private String nachname = "";

    /** A mapped Field from the DTO */
    @NotNull
    private Augenfarbe augenfarbe;
    /**
     * A mapped Field from the DTO
     */
    @NotNull
    @Past
    private Date geburtsdatum;

    /**
     * A mapped Field from the DTO
     */
    @NotNull
    private boolean alive;

    @Size(min = 1, max = 5)
    private Set<String> eigenschaften;

    /**
     * Create a new Buerger with the vorname, nachname and geburtsdatum.
     *
     * @param vorname the vorname of the Buerger.
     * @param nachname the nachname of the Buerger.
     * @param geburtsdatum the geburtsdatum of the Buerger.
     */
    public Buerger(String vorname, String nachname, Date geburtsdatum, Augenfarbe augenfarbe, boolean alive, Set<String> eigenschaften) {
        this.vorname = vorname;
        this.nachname = nachname;
        this.geburtsdatum = geburtsdatum;
        this.augenfarbe = augenfarbe;
        this.alive = alive;
        this.eigenschaften = eigenschaften;
    }

    /**
     * Create an empty Buerger.
     * <p>
     *     Only use this Constructor where it is absolutely neccessary.
     * </p>
     */
    public Buerger() {

    }

    /**
     * Get the vorname of the Buerger.
     * @return The vorname
     */
    public String getVorname() {
        return vorname;
    }

    /**
     * Set the vorname of the Buerger.
     * @param vorname The vorname to set.
     */
    public void setVorname(String vorname) {
        this.vorname = vorname;
    }

    /**
     * Get the nachname of the Buerger.
     * @return The nachname
     */
    public String getNachname() {
        return nachname;
    }

    /**
     * Set the nachname of the Buerger.
     * @param nachname The nachname to set.
     */
    public void setNachname(String nachname) {
        this.nachname = nachname;
    }

    /**
     * Get the geburtsdatum of the Buerger.
     * @return The geburtsdatum
     */
    public Date getGeburtsdatum() {
        return geburtsdatum;
    }

    /**
     * Set the geburtsdatum of the Buerger.
     * @param geburtsdatum The geburtsdatum to set.
     */
    public void setGeburtsdatum(Date geburtsdatum) {
        this.geburtsdatum = geburtsdatum;
    }

    public Augenfarbe getAugenfarbe() {
        return augenfarbe;
    }

    public void setAugenfarbe(Augenfarbe augenfarbe) {
        this.augenfarbe = augenfarbe;
    }

    public boolean getAlive(){
        return alive;
    }

    public void setAlive(boolean alive){
        this.alive = alive;
    }

    public Set<String> getEigenschaften() {
        return eigenschaften;
    }

    public void setEigenschaften(Set<String> eigenschaften) {
        this.eigenschaften = eigenschaften;
    }

    /**
     * A simple Enum for all the Fields of this Buerger.
     * <p>
     *     You can use {@link Field#name()} for the String.
     * </p>
     */
    public enum Field {
        vorname, nachname, geburtsdatum, augenfarbe, alive, eigenschaften(false);

        private final boolean field;

        Field() {
            this(true);
        }

        Field(boolean field) {
            this.field = field;
        }

        public static String[] getProperties() {
            return Stream.of(values()).filter(Field::isField).map(Field::name).toArray(String[]::new);
        }

        public boolean isField() {
            return field;
        }
    }

    /**
     * A simple Enum for all the Relations ({@link Buerger#getLink(String)} of the Buerger.
     * <p>
     *     You can use {@link Rel#name()} for the String.
     * </p>
     */
    public enum Rel {
        kinder, partner;
    }
}
