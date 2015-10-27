package de.muenchen.vaadin.guilib.components.actions;

import com.vaadin.ui.Button;
import de.muenchen.eventbus.selector.entity.ResponseEntityKey;
import de.muenchen.vaadin.guilib.BaseUI;

/**
 * Provides simple Actions for Navigation.
 *
 * @author p.mueller
 * @version 1.0
 */
public class NavigateActions {
    /** The String to navigate to. */
    private String navigateTo;

    /**
     * Create new NavigateActions that will navigate based on the NavigateTo with help of the Navigator.
     * <p/>
     * This Actions are crucial because they unregister Components from the EventBus.
     *
     * @param navigateTo The String to navigate to.
     */
    public NavigateActions(String navigateTo) {
        if (navigateTo == null)
            throw new NullPointerException();
        this.navigateTo = navigateTo;
    }

    /**
     * Navigate method for use outside of clickListener.
     *
     * @return true if navigation succeeded
     */
    public boolean navigate() {
        return navigate(null);
    }

    /**
     * Navigate to the set String.
     *
     * @param clickEvent can be null
     * @return true if navigation succeeded
     */
    public boolean navigate(Button.ClickEvent clickEvent) {
        if (BaseUI.getCurrentNavigator().getState().equals(getNavigateTo()))
            return true;

        BaseUI.getCurrentEventBus().getConsumerRegistry().unregister(new ResponseEntityKey());
        BaseUI.getCurrentNavigator().navigateTo(navigateTo);
        return true;
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
        if (getNavigateTo() == null)
            throw new NullPointerException();
        this.navigateTo = navigateTo;
    }
}
