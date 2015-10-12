package de.muenchen.eventbus.selector.entity;

/**
 * Stellt einen Schlüssel bereit, der einem Komsumenten die Möglichkeit gibt, bei bereitstehenden Daten benachrichtigt zu werden.
 * Produzenten versenden diesen Key um über zur verfügung stehende Daten zu benachrichtigen.
 * <p>
 * Komponenten können nur Requests versenden. Controller fressen nur requests. Controller versenden nur Responses. Komponenten empfangen nur responses.
 * </p>
 *
 * @author fabian.holtkoetter
 * @version 1.0
 */
public class ResponseEntityKey extends BaseEntityKey {

    /**
     * Create a new ResponseEntityKey on the class of an entity.
     *
     * @param entityClass The class of the entity the key is for.
     */
    public ResponseEntityKey(Class entityClass) {
        super(entityClass);
    }

    public ResponseEntityKey() {
        super(null);
    }
}
