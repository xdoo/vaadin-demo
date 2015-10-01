package de.muenchen.eventbus.events;

/**
 * Provides a simple description for an Association of an Entity.
 *
 * @author fabian.holtkoetter p.mueller
 * @version 1.0
 */
public class Association<T> implements Eventable {
    /** The entity that is the association. */
    private final T association;
    /** The String representation of the relation. */
    private final String rel;

    /**
     * Create a new Aoociation with the Entity and the relation it (should) belong(s) to.
     *
     * @param association The associated entity.
     * @param rel         The relation.
     */
    public Association(T association, String rel) {
        if (association == null) throw new IllegalArgumentException("Associated entity can't be null!");
        if (rel == null || rel.isEmpty()) throw new IllegalArgumentException("Relation can't be null or emtpy!");
        this.association = association;
        this.rel = rel;
    }

    /**
     * Get the associated entity.
     *
     * @return The associated entity.
     */
    public T getAssociation() {
        return association;
    }

    /**
     * Get the String representation of the relation.
     *
     * @return The relation.
     */
    public String getRel() {
        return rel;
    }
}
