package de.muenchen.vaadin.guilib.components.actions;

import com.vaadin.ui.Button;
import de.muenchen.eventbus.events.Association;
import de.muenchen.eventbus.selector.entity.RequestEntityKey;
import de.muenchen.eventbus.selector.entity.RequestEvent;
import reactor.bus.Event;
import reactor.bus.EventBus;

import java.util.function.Supplier;

/**
 * Provides simple Actions for changing an Association of the Entity.
 * They are designed to be used as ClickListeners with java8 method reference.
 *
 * @author p.mueller
 * @version 1.0
 */
public class EntityAssociationActions<T> {
    /** The EventBus for notifying the Action. */
    private final EventBus eventBus;
    /** The class of the Entity */
    private final Class<T> entityClass;
    /** The supplier for the Association. */
    private final Supplier<Association<?>> association;

    /**
     * Create new AssociationActions for the Entity with the single association.
     *
     * @param association The association.
     * @param eventBus    The EventBus.
     * @param entityClass The class of the Entity.
     */
    public EntityAssociationActions(Supplier<Association<?>> association, EventBus eventBus, Class<T> entityClass) {
        if (eventBus == null)
            throw new NullPointerException();
        if (entityClass == null)
            throw new NullPointerException();
        if (association == null)
            throw new NullPointerException();

        this.eventBus = eventBus;
        this.entityClass = entityClass;
        this.association = association;
    }

    /**
     * Add the association to the selected Buerger.
     * @param clickEvent can be null
     */
    public void addAssociation(Button.ClickEvent clickEvent) {
        getEventBus().notify(new RequestEntityKey(RequestEvent.ADD_ASSOCIATION, getEntityClass()), Event.wrap(getAssociation()));
    }

    /**
     * Remove the association from the selected Buerger.
     * @param clickEvent can be null
     */
    public void removeAssociation(Button.ClickEvent clickEvent) {
        getEventBus().notify(new RequestEntityKey(RequestEvent.REMOVE_ASSOCIATION, getEntityClass()), Event.wrap(getAssociation()));
    }

    /**
     * Get the EventBus.
     * @return The EventBus.
     */
    public EventBus getEventBus() {
        return eventBus;
    }

    /**
     * Get the Association.
     * @return The Association.
     */
    public Association<?> getAssociation() {
        return association.get();
    }

    /**
     * Get the entity class.
     * @return The class of the entity.
     */
    public Class<T> getEntityClass() {
        return entityClass;
    }
}
