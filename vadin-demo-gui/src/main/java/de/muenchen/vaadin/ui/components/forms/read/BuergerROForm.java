package de.muenchen.vaadin.ui.components.forms.read;

import de.muenchen.eventbus.selector.entity.ResponseEntityKey;
import de.muenchen.vaadin.demo.api.local.Buerger;
import de.muenchen.vaadin.demo.i18nservice.I18nResolver;
import de.muenchen.vaadin.services.model.BuergerDatastore;
import de.muenchen.vaadin.ui.components.forms.node.BuergerForm;
import reactor.bus.EventBus;

/**
 * Created by p.mueller on 08.10.15.
 */
public class BuergerROForm extends BuergerForm {

    public static final Class<Buerger> ENTITY_CLASS = Buerger.class;

    public BuergerROForm(I18nResolver i18nResolver, EventBus eventBus) {
        super(i18nResolver, eventBus);
        setReadOnly(true);
        getEventBus().on(new ResponseEntityKey(ENTITY_CLASS).toSelector(), this::update);
    }

    private void update(reactor.bus.Event<?> event) {
        System.out.println(this.toString() + System.currentTimeMillis());
        final BuergerDatastore data = (BuergerDatastore) event.getData();
        data.getSelectedBuerger().ifPresent(this::setBuerger);
    }
}
