package de.muenchen.vaadin.guilib.components.actions;

import com.vaadin.navigator.Navigator;
import com.vaadin.ui.Button;
import de.muenchen.eventbus.selector.entity.ResponseEntityKey;
import reactor.bus.EventBus;

/**
 * Provides simple Actions for Navigation.
 *
 * @author p.mueller
 * @version 1.0
 */
public class NavigateActions {
    /** The Navigator to perform the navigation. */
    private final Navigator navigator;
    /** The EventBus. */
    private final EventBus eventBus;
    /** The String to navigate to. */
    private String navigateTo;

    /**
     * Create new NavigateActions that will navigate based on the NavigateTo with help of the Navigator.
     * <p/>
     * This Actions are crucial because they unregister Components from the EventBus.
     *
     * @param navigator  The navigator.
     * @param eventBus   The EventBus.
     * @param navigateTo The String to navigate to.
     */
    public NavigateActions(Navigator navigator, EventBus eventBus, String navigateTo) {
        this.navigator = navigator;
        this.eventBus = eventBus;
        this.navigateTo = navigateTo;
    }

    /**
     * Get the NavigateTo String.
     *
     * @return The String that is naviagted to.
     */
    public String getNavigateTo() {
        return navigateTo;
    }

    /**
     * Set the String that is navigated to.
     *
     * @param navigateTo The navigateTo String.
     */
    public void setNavigateTo(String navigateTo) {
        this.navigateTo = navigateTo;
    }

    /**
     * Navigate to the set String.
     * @param clickEvent can be null
     */
    public boolean navigate(Button.ClickEvent clickEvent) {
        eventBus.getConsumerRegistry().unregister(new ResponseEntityKey());
        navigator.navigateTo(navigateTo);
        return false;
    }
}
