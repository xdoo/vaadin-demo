package de.muenchen.vaadin.ui.components.buttons.node.listener;

import com.vaadin.ui.Button;
import de.muenchen.eventbus.events.Association;
import de.muenchen.eventbus.selector.entity.RequestEntityKey;
import de.muenchen.eventbus.selector.entity.RequestEvent;
import de.muenchen.vaadin.demo.api.local.Buerger;
import reactor.bus.Event;
import reactor.bus.EventBus;

/**
 * Created by p.mueller on 09.10.15.
 */
public class BuergerAssociationActions {
    private final EventBus eventBus;
    private Association<Buerger> association;

    public BuergerAssociationActions(EventBus eventBus) {
        if (eventBus == null)
            throw new NullPointerException();
        this.eventBus = eventBus;
    }

    public void addAssociation(Button.ClickEvent clickEvent) {
        getEventBus().notify(new RequestEntityKey(RequestEvent.ADD_ASSOCIATION, Buerger.class), Event.wrap(getAssociation()));
    }

    public void removeAssociation(Button.ClickEvent clickEvent) {
        getEventBus().notify(new RequestEntityKey(RequestEvent.REMOVE_ASSOCIATION, Buerger.class));
    }

    public EventBus getEventBus() {
        return eventBus;
    }

    public Association<Buerger> getAssociation() {
        return association;
    }
}
