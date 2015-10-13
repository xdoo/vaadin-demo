package de.muenchen.vaadin.ui.components.forms.selected;

import de.muenchen.eventbus.selector.entity.ResponseEntityKey;
import de.muenchen.vaadin.demo.i18nservice.I18nResolver;
import de.muenchen.vaadin.services.model.BuergerDatastore;
import de.muenchen.vaadin.ui.components.forms.node.BuergerForm;
import reactor.bus.EventBus;

/**
 * Provides a simple {@link BuergerForm} that always shows the {@link BuergerDatastore#getSelectedBuerger()}.
 *
 * @author p.mueller
 * @version 1.0
 */
public class SelectedBuergerForm extends BuergerForm {

    /**
     * Creates a new SelectedBuergerForm that updates its Buerger to the {@link BuergerDatastore#getSelectedBuerger()} from the Eventbus.
     *
     * @param i18nResolver The i18n Resolver.
     * @param eventBus     The eventbus to listen on for changes and to acquire the {@link BuergerDatastore}.
     */
    public SelectedBuergerForm(I18nResolver i18nResolver, EventBus eventBus) {
        super(i18nResolver, eventBus);
        getEventBus().on(new ResponseEntityKey(BuergerForm.ENTITY_CLASS).toSelector(), this::update);
    }

    /**
     * Update the Buerger of this Form to the selected one form the DataStore.
     *
     * @param event
     */
    private void update(reactor.bus.Event<?> event) {
        System.out.println(this.toString() + System.currentTimeMillis());
        final BuergerDatastore data = (BuergerDatastore) event.getData();
        data.getSelectedBuerger().ifPresent(this::setBuerger);
    }
}
