package de.muenchen.vaadin.ui.components.buttons.node.listener;

import de.muenchen.eventbus.events.Association;
import de.muenchen.vaadin.demo.api.local.Buerger;
import de.muenchen.vaadin.demo.i18nservice.I18nResolver;
import de.muenchen.vaadin.guilib.components.actions.EntityAssociationListAction;
import reactor.bus.EventBus;

import java.util.List;
import java.util.function.Supplier;

/**
 * Created by rene.zarwel on 15.10.15.
 */
public class BuergerAssociationListActions extends EntityAssociationListAction<Buerger> {

    /**
     * Create new AssociationActions for the Entity with the single association.
     *
     * @param resolver
     * @param association The association.
     * @param eventBus    The EventBus.
     */
    public BuergerAssociationListActions(I18nResolver resolver, Supplier<List<Association<Buerger>>> association, EventBus eventBus) {
        super(resolver, association, eventBus, Buerger.class);
    }
}
