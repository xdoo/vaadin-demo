package de.muenchen.vaadin.ui.components.buttons.node.listener;

import de.muenchen.eventbus.EventBus;
import de.muenchen.vaadin.demo.api.local.Buerger;
import de.muenchen.vaadin.guilib.components.actions.EntityActions;

/**
 * Created by p.mueller on 09.10.15.
 */
public class BuergerActions extends EntityActions {

    public BuergerActions(EventBus eventBus) {
        super(eventBus, Buerger.class);
    }
}
