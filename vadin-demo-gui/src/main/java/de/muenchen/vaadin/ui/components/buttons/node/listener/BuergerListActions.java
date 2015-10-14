package de.muenchen.vaadin.ui.components.buttons.node.listener;

import de.muenchen.vaadin.demo.api.local.Buerger;
import de.muenchen.vaadin.guilib.components.actions.EntityListActions;
import reactor.bus.EventBus;

import java.util.List;
import java.util.function.Supplier;

/**
 * Created by p.mueller on 09.10.15.
 */
public class BuergerListActions extends EntityListActions<Buerger> {
    public BuergerListActions(Supplier<List<Buerger>> buergerSupplier, EventBus eventBus) {
        super(buergerSupplier, Buerger.class, eventBus);
    }
}
