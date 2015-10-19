package de.muenchen.vaadin.ui.components.buttons.node.listener;

import de.muenchen.vaadin.demo.api.local.Buerger;
import de.muenchen.vaadin.guilib.components.actions.EntityActions;

import java.util.function.Supplier;

/**
 * Created by p.mueller on 09.10.15.
 */
public class BuergerActions extends EntityActions {

    public BuergerActions(Supplier<String> filterSupplier) {
        super(filterSupplier, Buerger.class);
    }
}
