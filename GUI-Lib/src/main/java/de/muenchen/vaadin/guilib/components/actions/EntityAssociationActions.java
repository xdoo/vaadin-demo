package de.muenchen.vaadin.guilib.components.actions;

import com.vaadin.ui.Button;
import de.muenchen.eventbus.events.Association;
import de.muenchen.eventbus.selector.entity.RequestEntityKey;
import de.muenchen.eventbus.selector.entity.RequestEvent;
import reactor.bus.Event;
import reactor.bus.EventBus;

import java.util.function.Supplier;

/**
 * Created by p.mueller on 09.10.15.
 */
public class EntityAssociationActions<T> {
    private final EventBus eventBus;
    private final Class<T> entityClass;
    private final Supplier<Association<?>> association;

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

    public void addAssociation(Button.ClickEvent clickEvent) {
        getEventBus().notify(new RequestEntityKey(RequestEvent.ADD_ASSOCIATION, getEntityClass()), Event.wrap(getAssociation()));
    }

    public void removeAssociation(Button.ClickEvent clickEvent) {
        getEventBus().notify(new RequestEntityKey(RequestEvent.REMOVE_ASSOCIATION, getEntityClass()), Event.wrap(getAssociation()));
    }

    public EventBus getEventBus() {
        return eventBus;
    }

    public Association<?> getAssociation() {
        return association.get();
    }

    public Class<T> getEntityClass() {
        return entityClass;
    }
}
