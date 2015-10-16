package de.muenchen.vaadin.guilib.components.actions;

import com.vaadin.ui.Button;
import de.muenchen.eventbus.selector.entity.RequestEntityKey;
import de.muenchen.eventbus.selector.entity.RequestEvent;
import reactor.bus.Event;
import reactor.bus.EventBus;

import java.util.function.Supplier;

/**
 * Provides simple general Actions for an Entity.
 * They are designed to be used as ClickListeners with java8 method reference.
 *
 * @author p.mueller
 * @version 1.0
 */
public class EntityActions {
    /** The supplier for the query. */
    private final Supplier<String> filterSupplier;
    /** The EventBus for notifying the Action. */
    private final EventBus eventBus;
    /** The class of the Entity */
    private final Class entityClass;

    /**
     * Creates new EntityActions defined by Class.
     *
     * @param eventBus    The eventBus notify on.
     * @param entityClass The class of the Entity.
     */
    public EntityActions(Supplier<String> filterSupplier, EventBus eventBus, Class entityClass) {
        if (entityClass == null)
            throw new NullPointerException();
        this.entityClass = entityClass;
        if (eventBus == null)
            throw new NullPointerException();

        this.filterSupplier = filterSupplier;
        this.eventBus = eventBus;
    }

    /**
     * Read the whole list of buergers.
     * @param clickEvent can be null
     */
    public void readList(Button.ClickEvent clickEvent) {
        String query = getQuery();

        if (query == null) {
            getEventBus().notify(new RequestEntityKey(RequestEvent.READ_LIST, getEntityClass()));
        } else {
            getEventBus().notify(new RequestEntityKey(RequestEvent.READ_LIST, getEntityClass()), Event.wrap(query));
        }
    }

    /**
     * Get the Query
     * @return The query.
     */
    public String getQuery() {
        return filterSupplier.get();
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
