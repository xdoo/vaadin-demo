package de.muenchen.vaadin.ui.components.buttons.node.listener;

import de.muenchen.eventbus.events.Association;
import de.muenchen.vaadin.demo.api.local.Buerger;
import de.muenchen.vaadin.guilib.components.actions.EntityAssociationListAction;

import java.util.List;
import java.util.function.Supplier;

/**
 * Created by rene.zarwel on 15.10.15.
 */
public class BuergerAssociationListActions extends EntityAssociationListAction<Buerger> {

    /**
     * Create new AssociationActions for the Entity with the single association.
     *
     * @param clazz
     * @param association The association..
     */
    public BuergerAssociationListActions(Class clazz, Supplier<List<Association<Buerger>>> association) {
        super(clazz, association, Buerger.class);
    }
}
