package de.muenchen.eventbus.selector;

/**
 * Stellt einen Schlüssel bereit, der einem Komsumenten die Möglichkeit gibt, bei bereitstehenden Daten benachrichtigt zu werden.
 * Produzenten versenden diesen Key um über zur verfügung stehende Daten zu benachrichtigen.
 * <p>
 *     Komponenten können nur Requests versenden. Controller fressen nur requests. Controller versenden nur Responses. Komponenten empfangen nur responses.
 * </p>
 *
 * "Also wenn du die Kindertab da hast - und dann holst du dir ein Wasser!"
 *
 * @author fabian.holtkoetter
 * @version 1
 */
public class ResponseKey extends BaseKey {

    /**
     * Create a new ResponseKey on the class of an entity.
     *
     * @param entityClass The class of the entity the key is for.
     */
    public ResponseKey(Class entityClass) {
        super(entityClass);
    }
}
