package de.muenchen.vaadin.ui.components.buttons.node.listener;

import de.muenchen.eventbus.events.Association;
import de.muenchen.vaadin.demo.api.local.Buerger;
import de.muenchen.vaadin.guilib.components.actions.EntityAssociationActions;
import reactor.bus.EventBus;

/**
 * Created by p.mueller on 09.10.15.
 */
public class BuergerAssociationActions extends EntityAssociationActions<Buerger> {
    public BuergerAssociationActions(Association<Buerger> association, EventBus eventBus) {
        super(association, eventBus, Buerger.class);
    }
}
