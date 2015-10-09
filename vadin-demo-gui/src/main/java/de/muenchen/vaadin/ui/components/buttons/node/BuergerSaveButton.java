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

import java.util.function.Supplier;

/**
 * Created by p.mueller on 08.10.15.
 */
public class BuergerSaveButton extends BaseComponent {

    private final ActionButton save;
    private Supplier<Buerger> buerger = () -> null;

    public BuergerSaveButton(I18nResolver i18nResolver, EventBus eventBus) {
        super(i18nResolver, eventBus);
        String navigateTo = "";
        save = new ActionButton(getI18nResolver(), SimpleAction.save);
        save.addClickListener(this::update);
        setCompositionRoot(save);
    }

    private void update(Button.ClickEvent clickEvent) {
        if (getBuerger() == null)
            throw new NullPointerException();
        final RequestEntityKey key = new RequestEntityKey(RequestEvent.UPDATE, Buerger.class);
        getEventBus().notify(key, reactor.bus.Event.wrap(getBuerger()));
    }

    public Buerger getBuerger() {
        return buerger.get();
    }

    public ActionButton getSave() {
        return save;
    }

    public void setBuergerSupplier(Supplier<Buerger> buergerSupplier) {
        this.buerger = buergerSupplier;
    }
}
