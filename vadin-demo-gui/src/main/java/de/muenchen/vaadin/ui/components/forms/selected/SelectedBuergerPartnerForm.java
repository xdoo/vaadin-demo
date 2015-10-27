package de.muenchen.vaadin.ui.components.forms.selected;

import de.muenchen.vaadin.demo.api.local.Buerger;
import de.muenchen.vaadin.guilib.controller.EntityController;
import de.muenchen.vaadin.services.model.BuergerDatastore;
import de.muenchen.vaadin.ui.components.buttons.node.listener.BuergerSingleActions;
import de.muenchen.vaadin.ui.components.forms.node.BuergerForm;

/**
 * Provides a simple {@link BuergerForm} that always shows the {@link BuergerDatastore#getSelectedBuergerPartner()} ()}.
 *
 * @author p.mueller
 * @version 1.0
 */
public class SelectedBuergerPartnerForm extends BuergerForm {
    /**
     * Creates a new Buerger Partner form
     * @param entityController The controller used for everything.
     */
    public SelectedBuergerPartnerForm(EntityController entityController) {
        super(entityController);
        getEventBus().on(getResponeKey().toSelector(), this::update);
    }

    /**
     * Reloads the Partner via the Controller.
     */
    public void reLoadBuerger() {
        final BuergerSingleActions singleActions = new BuergerSingleActions(this::getBuerger);
        singleActions.reRead(null);
    }

    /**
     * Update the Buerger of this Form to the selected one form the DataStore.
     *
     * @param event A reactor Event with a {@link BuergerDatastore} as Data.
     */
    private void update(reactor.bus.Event<?> event) {
        final BuergerDatastore data = (BuergerDatastore) event.getData();
        final Buerger buerger = data.getSelectedBuergerPartner().orElse(null);
        setBuerger(buerger);
    }
}
