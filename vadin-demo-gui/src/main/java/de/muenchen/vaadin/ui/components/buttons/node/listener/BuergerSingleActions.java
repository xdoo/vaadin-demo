package de.muenchen.vaadin.ui.components.buttons.node.listener;

import de.muenchen.eventbus.EventBus;
import de.muenchen.vaadin.demo.api.local.Buerger;
import de.muenchen.vaadin.guilib.components.actions.EntitySingleActions;

import java.util.function.Supplier;

/**
 * Created by p.mueller on 08.10.15.
 */
public class BuergerSingleActions extends EntitySingleActions<Buerger> {
    public BuergerSingleActions(Supplier<Buerger> buergerSupplier, EventBus eventBus) {
        super(buergerSupplier, eventBus, Buerger.class);
    }
}
