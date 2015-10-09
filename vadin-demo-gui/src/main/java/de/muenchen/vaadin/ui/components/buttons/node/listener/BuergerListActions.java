package de.muenchen.vaadin.ui.components.buttons.node.listener;

import com.vaadin.ui.Button;
import de.muenchen.eventbus.selector.entity.RequestEntityKey;
import de.muenchen.eventbus.selector.entity.RequestEvent;
import de.muenchen.vaadin.demo.api.local.Buerger;
import reactor.bus.Event;
import reactor.bus.EventBus;

import java.util.List;
import java.util.function.Supplier;

/**
 * Created by p.mueller on 09.10.15.
 */
public class BuergerListActions {
    private final Supplier<List<Buerger>> buergerSupplier;
    private final EventBus eventBus;


    public BuergerListActions(Supplier<List<Buerger>> buergerSupplier, EventBus eventBus) {
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

    private void notifyRequest(RequestEvent delete) {

        if (getBuerger() == null)
            throw new NullPointerException();
        final RequestEntityKey key = new RequestEntityKey(delete, Buerger.class);
        getEventBus().notify(key, Event.wrap(getBuerger()));
    }

    public List<Buerger> getBuerger() {
        return buergerSupplier.get();
    }

    public EventBus getEventBus() {
        return eventBus;
    }
}
