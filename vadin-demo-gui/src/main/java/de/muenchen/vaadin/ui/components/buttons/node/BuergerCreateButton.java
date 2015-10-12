package de.muenchen.vaadin.ui.components.buttons.node;

import de.muenchen.vaadin.demo.api.local.Buerger;
import de.muenchen.vaadin.demo.i18nservice.I18nResolver;
import de.muenchen.vaadin.demo.i18nservice.buttons.ActionButton;
import de.muenchen.vaadin.demo.i18nservice.buttons.SimpleAction;
import de.muenchen.vaadin.guilib.components.actions.NavigateActions;
import de.muenchen.vaadin.ui.components.BaseComponent;
import de.muenchen.vaadin.ui.components.buttons.node.listener.BuergerSingleActions;
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

        create = new ActionButton(getI18nResolver(), SimpleAction.create);

        String navigateTo = "";
        final NavigateActions navigateActions = new NavigateActions(null, null, navigateTo);
        final BuergerSingleActions buergerSingleActions = new BuergerSingleActions(buerger, eventBus);

        create.addClickListener(buergerSingleActions::create);
        create.addClickListener(navigateActions::navigate);
        setCompositionRoot(create);
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
