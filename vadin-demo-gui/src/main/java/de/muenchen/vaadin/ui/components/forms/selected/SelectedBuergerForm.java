package de.muenchen.vaadin.ui.components.forms.selected;

import de.muenchen.eventbus.selector.entity.ResponseEntityKey;
import de.muenchen.vaadin.demo.api.local.Buerger;
import de.muenchen.vaadin.guilib.controller.EntityController;
import de.muenchen.vaadin.services.model.BuergerDatastore;
import de.muenchen.vaadin.ui.components.buttons.node.listener.BuergerSingleActions;
import de.muenchen.vaadin.ui.components.forms.node.BuergerForm;

/**
 * Provides a simple {@link BuergerForm} that always shows the {@link BuergerDatastore#getSelectedBuerger()}.
 *
 * @author p.mueller
 * @version 1.0
 */
public class SelectedBuergerForm extends BuergerForm {

    /**
     * Creates a new SelectedBuergerForm that updates its Buerger to the {@link BuergerDatastore#getSelectedBuerger()}
     * from the Eventbus.
     *
     * @param entityController The controller used for everything.
     */
    public SelectedBuergerForm(EntityController entityController) {
        super(entityController);
        getEventBus().on(new ResponseEntityKey(BuergerForm.ENTITY_CLASS).toSelector(), this::update);
    }

    /**
     * Reloads the Buerger via the Controller.
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
        final Buerger buerger = data.getSelectedBuerger().orElse(null);
        setBuerger(buerger);
    }
}
