package de.muenchen.vaadin.ui.components.forms.selected;

import de.muenchen.vaadin.demo.api.local.Buerger;
import de.muenchen.vaadin.guilib.controller.EntityController;
import de.muenchen.vaadin.services.model.BuergerDatastore;
import de.muenchen.vaadin.ui.components.buttons.node.listener.BuergerSingleActions;
import de.muenchen.vaadin.ui.components.forms.node.BuergerForm;

/**
 * Created by p.mueller on 13.10.15.
 */
public class SelectedBuergerPartnerForm extends BuergerForm {
    /**
     * Create a new BuergerForm using the specified i18nResolver and the eventbus.
     * <p/>
     * This Form is only the plain fields for input, and has no additional components or buttons. You can use {@link
     * BuergerForm#setReadOnly(boolean)} for a readonly mode.
     *
     * @param entityController The controller used for everything.
     */
    public SelectedBuergerPartnerForm(EntityController entityController) {
        super(entityController);
        getEventBus().on(getResponeKey().toSelector(), this::update);
    }

    public void reLoadBuerger() {
        final BuergerSingleActions singleActions = new BuergerSingleActions(getI18nResolver(), this::getBuerger);
        singleActions.reRead(null);
    }

    private void update(reactor.bus.Event<?> event) {
        final BuergerDatastore data = (BuergerDatastore) event.getData();
        final Buerger buerger = data.getSelectedBuergerPartner().orElse(null);
        setBuerger(buerger);
    }
}
