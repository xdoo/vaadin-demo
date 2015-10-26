package de.muenchen.vaadin.guilib;

import com.vaadin.navigator.Navigator;
import com.vaadin.ui.UI;
import de.muenchen.eventbus.EventBus;
import de.muenchen.vaadin.demo.i18nservice.I18nResolver;
import de.muenchen.vaadin.demo.i18nservice.I18nResolverImpl;

/**
 * Provides a simple BaseUI with out EventBus.
 *
 * @author p.mueller
 * @version 1.0
 */
public abstract class BaseUI extends UI {
    /** The one and only EventBus. */
    private final EventBus eventBus;
    private final I18nResolverImpl i18nResolver;

    public BaseUI(EventBus eventBus, I18nResolverImpl i18nResolver){
        this.eventBus = eventBus;
        this.i18nResolver = i18nResolver;
    }

    public static Navigator getCurrentNavigator() {
        return getCurrent().getNavigator();
    }

    /**
     * Get the EventBus from the current BaseUI.
     *
     * @return The EventBus.
     */
    public static EventBus getCurrentEventBus() {
        return ((BaseUI) getCurrent()).eventBus;
    }


    /**
     * Get the I18nResolver from the current BaseUI.
     *
     * @return The EventBus.
     */
    public static I18nResolver getCurrentI18nResolver() {
        return ((BaseUI) getCurrent()).i18nResolver;
    }

}
