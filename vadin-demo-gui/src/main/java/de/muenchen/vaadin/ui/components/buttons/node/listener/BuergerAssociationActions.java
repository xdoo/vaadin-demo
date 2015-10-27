package de.muenchen.vaadin.ui.components.buttons.node.listener;

import de.muenchen.eventbus.events.Association;
import de.muenchen.vaadin.demo.api.local.Buerger;
import de.muenchen.vaadin.demo.i18nservice.I18nResolver;
import de.muenchen.vaadin.guilib.components.actions.EntityAssociationActions;

import java.util.function.Supplier;

/**
 * Created by p.mueller on 09.10.15.
 */
public class BuergerAssociationActions extends EntityAssociationActions<Buerger> {
    public BuergerAssociationActions(Supplier<Association<?>> associationSupplier) {
        super(associationSupplier, Buerger.class);
    }
}
