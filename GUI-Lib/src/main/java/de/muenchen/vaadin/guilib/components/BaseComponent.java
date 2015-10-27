package de.muenchen.vaadin.guilib.components;

import com.vaadin.navigator.Navigator;
import com.vaadin.ui.CustomComponent;
import de.muenchen.eventbus.EventBus;
import de.muenchen.vaadin.demo.i18nservice.I18nResolver;
import de.muenchen.vaadin.guilib.BaseUI;

/**
 * Created by p.mueller on 07.10.15.
 */
public class BaseComponent extends CustomComponent {

    public I18nResolver getI18nResolver() {
        return BaseUI.getCurrentI18nResolver();
    }

    public EventBus getEventBus() {
        return BaseUI.getCurrentEventBus();
    }

    public Navigator getNavigator() {
        return BaseUI.getCurrentNavigator();
    }
}
