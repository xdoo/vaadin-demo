package de.muenchen.vaadin.guilib.components.actions;

import com.vaadin.ui.Button;
import de.muenchen.eventbus.selector.entity.RequestEntityKey;
import de.muenchen.eventbus.selector.entity.RequestEvent;
import reactor.bus.Event;
import reactor.bus.EventBus;

import java.util.function.Supplier;

/**
 * Created by p.mueller on 09.10.15.
 */
public class EntitySingleActions<T> {
    private final Supplier<T> buergerSupplier;
    private final EventBus eventBus;
    private final Class<T> entityClass;


    public EntitySingleActions(Supplier<T> buergerSupplier, EventBus eventBus, Class<T> entityClass) {
        this.entityClass = entityClass;
        if (eventBus == null)
            throw new NullPointerException();
        if (buergerSupplier == null)
            throw new NullPointerException();

        this.eventBus = eventBus;
        this.buergerSupplier = buergerSupplier;
    }

    public void delete(Button.ClickEvent clickEvent) {
        notifyRequest(RequestEvent.DELETE);
    }

    public void create(Button.ClickEvent clickEvent) {
        notifyRequest(RequestEvent.CREATE);
    }

    public void update(Button.ClickEvent clickEvent) {
        notifyRequest(RequestEvent.UPDATE);
    }

    public void read(Button.ClickEvent clickEvent) {
        notifyRequest(RequestEvent.READ_SELECTED);
    }

    private void notifyRequest(RequestEvent delete) {

        if (getBuerger() == null)
            throw new NullPointerException();
        final RequestEntityKey key = new RequestEntityKey(delete, getEntityClass());
        getEventBus().notify(key, Event.wrap(getBuerger()));
    }

    public T getBuerger() {
        return buergerSupplier.get();
    }

    public EventBus getEventBus() {
        return eventBus;
    }

    public Class<T> getEntityClass() {
        return entityClass;
    }
}
