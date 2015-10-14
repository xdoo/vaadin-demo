package de.muenchen.vaadin.guilib.components.actions;

import com.vaadin.ui.Button;
import de.muenchen.eventbus.selector.entity.RequestEntityKey;
import de.muenchen.eventbus.selector.entity.RequestEvent;
import reactor.bus.Event;
import reactor.bus.EventBus;

/**
 * Provides simple general Actions for an Entity.
 * They are designed to be used as ClickListeners with java8 method reference.
 *
 * @author p.mueller
 * @version 1.0
 */
public class EntityActions {
    /** The EventBus for notifying the Action. */
    private final EventBus eventBus;
    /** The class of the Entity */
    private final Class entityClass;

    /** The filter for the list of buergers */
    private String q = null;

    /**
     * Creates new EntityActions defined by Class.
     *
     * @param eventBus    The eventBus notify on.
     * @param entityClass The class of the Entity.
     */
    public EntityActions(EventBus eventBus, Class entityClass) {
        if (entityClass == null)
            throw new NullPointerException();
        this.entityClass = entityClass;
        if (eventBus == null)
            throw new NullPointerException();
        this.eventBus = eventBus;
    }

    /**
     * Get the filter query for the actions.
     *
     * @return The query as String.
     */
    public String getQ() {
        return q;
    }

    /**
     * Set the filter query for the actions.
     *
     * @param q The desired query.
     */
    public void setQ(String q) {
        this.q = q;
    }

    /**
     * Read the whole list of buergers.
     * @param clickEvent can be null
     */
    public void readList(Button.ClickEvent clickEvent) {
        if (q == null) {
            getEventBus().notify(new RequestEntityKey(RequestEvent.READ_LIST, getEntityClass()));
        } else {
            getEventBus().notify(new RequestEntityKey(RequestEvent.READ_LIST, getEntityClass()), Event.wrap(q));
        }
    }

    /**
     * Get the Eventbus of the Actions.
     * @return The EventBus.
     */
    public EventBus getEventBus() {
        return eventBus;
    }

    /**
     * Get the entity class of the Actions.
     * @return The class of the Entity.
     */
    public Class getEntityClass() {
        return entityClass;
    }
}
