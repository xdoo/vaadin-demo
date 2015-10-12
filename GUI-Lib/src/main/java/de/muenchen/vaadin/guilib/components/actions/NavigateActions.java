package de.muenchen.vaadin.guilib.components.actions;

import com.vaadin.navigator.Navigator;
import com.vaadin.ui.Button;
import de.muenchen.eventbus.selector.entity.ResponseEntityKey;
import reactor.bus.EventBus;

/**
 * Created by p.mueller on 08.10.15.
 */
public class NavigateActions {
    private final Navigator navigator;
    private final EventBus eventBus;
    private final String navigateTo;

    public NavigateActions(Navigator navigator, EventBus eventBus, String navigateTo) {
        this.navigator = navigator;
        this.eventBus = eventBus;
        this.navigateTo = navigateTo;
    }

    public void navigate(Button.ClickEvent clickEvent) {
        eventBus.getConsumerRegistry().unregister(new ResponseEntityKey());
        navigator.navigateTo(navigateTo);
    }
}
