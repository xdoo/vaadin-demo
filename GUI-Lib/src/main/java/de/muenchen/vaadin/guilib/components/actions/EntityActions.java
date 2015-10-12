package de.muenchen.vaadin.guilib.components.actions;

import com.vaadin.ui.Button;
import de.muenchen.eventbus.selector.entity.RequestEntityKey;
import de.muenchen.eventbus.selector.entity.RequestEvent;
import reactor.bus.EventBus;

/**
 * Created by p.mueller on 09.10.15.
 */
public class EntityActions {
    private final EventBus eventBus;
    private final Class entityClass;

    public EntityActions(EventBus eventBus, Class entityClass) {
        if (entityClass == null)
            throw new NullPointerException();
        this.entityClass = entityClass;
        if (eventBus == null)
            throw new NullPointerException();
        this.eventBus = eventBus;
    }

    public void readList(Button.ClickEvent clickEvent) {
        getEventBus().notify(new RequestEntityKey(RequestEvent.READ_LIST, getEntityClass()));
    }

    public EventBus getEventBus() {
        return eventBus;
    }

    public Class getEntityClass() {
        return entityClass;
    }
}
