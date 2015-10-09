package de.muenchen.vaadin.ui.components.buttons.node;

import com.vaadin.ui.Button;
import de.muenchen.eventbus.selector.entity.RequestEntityKey;
import de.muenchen.eventbus.selector.entity.RequestEvent;
import de.muenchen.vaadin.demo.api.local.Buerger;
import de.muenchen.vaadin.demo.i18nservice.I18nResolver;
import de.muenchen.vaadin.demo.i18nservice.buttons.ActionButton;
import de.muenchen.vaadin.demo.i18nservice.buttons.SimpleAction;
import de.muenchen.vaadin.ui.components.BaseComponent;
import reactor.bus.EventBus;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.function.Supplier;

/**
 * Created by p.mueller on 08.10.15.
 */
public class BuergerDeleteButton extends BaseComponent {

    private final ActionButton delete;
    private Supplier<Collection<Buerger>> buerger = ArrayList::new;

    public BuergerDeleteButton(I18nResolver i18nResolver, EventBus eventBus) {
        super(i18nResolver, eventBus);
        String navigateTo = "";
        delete = new ActionButton(getI18nResolver(), SimpleAction.delete);
        delete.addClickListener(this::delete);
        setCompositionRoot(delete);
    }

    private void delete(Button.ClickEvent clickEvent) {
        buerger.get().forEach(this::deleteSingle);
    }

    private void deleteSingle(Buerger buerger) {
        if (buerger == null)
            throw new NullPointerException();

        final RequestEntityKey key = new RequestEntityKey(RequestEvent.DELETE, Buerger.class);
        getEventBus().notify(key, reactor.bus.Event.wrap(buerger));
    }

    public Collection<Buerger> getBuergers() {
        return buerger.get();
    }

    public ActionButton getDelete() {
        return delete;
    }

    public void setBuergerList(Supplier<Collection<Buerger>> buergerListSupplier) {
        this.buerger = buergerListSupplier;
    }

    public void setBuergerSingle(Supplier<Buerger> buergerListSupplier) {
        this.buerger = () -> Collections.singletonList(buergerListSupplier.get());
    }
}
