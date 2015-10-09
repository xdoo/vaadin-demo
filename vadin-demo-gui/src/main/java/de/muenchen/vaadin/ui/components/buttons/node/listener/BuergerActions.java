package de.muenchen.vaadin.ui.components.buttons.node.listener;

import com.vaadin.ui.Button;
import de.muenchen.eventbus.selector.entity.RequestEntityKey;
import de.muenchen.eventbus.selector.entity.RequestEvent;
import de.muenchen.vaadin.demo.api.local.Buerger;
import reactor.bus.EventBus;

/**
 * Created by p.mueller on 09.10.15.
 */
public class BuergerActions {
    private final EventBus eventBus;

    public BuergerActions(EventBus eventBus) {
        if (eventBus == null)
            throw new NullPointerException();
        this.eventBus = eventBus;
    }

    public void readList(Button.ClickEvent clickEvent) {
        getEventBus().notify(new RequestEntityKey(RequestEvent.READ_LIST, Buerger.class));
    }

    public EventBus getEventBus() {
        return eventBus;
    }
}
