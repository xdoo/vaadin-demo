package de.muenchen.vaadin.ui.components.buttons.node.listener;

import de.muenchen.vaadin.demo.api.local.Buerger;
import de.muenchen.vaadin.guilib.components.actions.EntitySingleActions;

import java.util.function.Supplier;

/**
 * Created by p.mueller on 08.10.15.
 */
public class BuergerSingleActions extends EntitySingleActions<Buerger> {
    public BuergerSingleActions(Class clazz, Supplier<Buerger> buergerSupplier) {
        super(clazz, buergerSupplier, Buerger.class);
    }
}
