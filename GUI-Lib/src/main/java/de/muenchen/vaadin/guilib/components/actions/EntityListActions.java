package de.muenchen.vaadin.guilib.components.actions;

import com.vaadin.ui.Button;
import de.muenchen.eventbus.selector.entity.RequestEntityKey;
import de.muenchen.eventbus.selector.entity.RequestEvent;
import reactor.bus.Event;
import reactor.bus.EventBus;

import java.util.List;
import java.util.function.Supplier;

/**
 * Created by p.mueller on 09.10.15.
 */
public class EntityListActions<T> {

    private final Supplier<List<T>> entityListSupplier;
    private final EventBus eventBus;
    private final Class<T> entityClass;


    public EntityListActions(Supplier<List<T>> entityListSupplier, Class<T> entityClass, EventBus eventBus) {
        if (eventBus == null)
            throw new NullPointerException();
        if (entityListSupplier == null)
            throw new NullPointerException();
        if (entityClass == null)
            throw new NullPointerException();


        this.entityClass = entityClass;
        this.eventBus = eventBus;
        this.entityListSupplier = entityListSupplier;
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

    private void notifyRequest(RequestEvent delete) {

        if (getEntityList() == null)
            throw new NullPointerException();
        getEntityList().stream().forEach(buerger -> {
            final RequestEntityKey key = new RequestEntityKey(delete, getEntityClass());
            getEventBus().notify(key, Event.wrap(buerger));
        });
    }

    public List<T> getEntityList() {
        return entityListSupplier.get();
    }

    public EventBus getEventBus() {
        return eventBus;
    }

    public Class<T> getEntityClass() {
        return entityClass;
    }
}


