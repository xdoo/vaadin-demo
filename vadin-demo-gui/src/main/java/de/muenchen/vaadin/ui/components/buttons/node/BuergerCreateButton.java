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
public class BuergerCreateButton extends BaseComponent {

    private final ActionButton create;
    private Supplier<Buerger> buerger = () -> null;

    public BuergerCreateButton(I18nResolver i18nResolver, EventBus eventBus) {
        super(i18nResolver, eventBus);
        String navigateTo = "";
        create = new ActionButton(getI18nResolver(), SimpleAction.create, navigateTo);
        create.addClickListener(this::create);
        setCompositionRoot(create);
    }

    private void create(Button.ClickEvent clickEvent) {
        if (getBuerger() == null)
            throw new NullPointerException();
        final RequestEntityKey key = new RequestEntityKey(RequestEvent.CREATE, Buerger.class);
        getEventBus().notify(key, reactor.bus.Event.wrap(getBuerger()));
    }

    public Buerger getBuerger() {
        return buerger.get();
    }

    public ActionButton getCreate() {
        return create;
    }

    public void setBuergerSupplier(Supplier<Buerger> buergerSupplier) {
        this.buerger = buergerSupplier;
    }
}
