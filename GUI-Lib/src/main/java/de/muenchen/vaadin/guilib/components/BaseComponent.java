package de.muenchen.vaadin.guilib.components;

import com.vaadin.navigator.Navigator;
import com.vaadin.ui.CustomComponent;
import de.muenchen.eventbus.EventBus;
import de.muenchen.eventbus.selector.entity.ResponseEntityKey;
import de.muenchen.vaadin.demo.i18nservice.I18nResolver;
import de.muenchen.vaadin.guilib.controller.EntityController;

/**
 * Created by p.mueller on 07.10.15.
 */
public class BaseComponent extends CustomComponent {
    private final EntityController entityController;

    public BaseComponent(EntityController entityController) {
        this.entityController = entityController;
    }

    public ResponseEntityKey getResponeKey() {
        return this.entityController.getResponseKey();
    }

    public I18nResolver getI18nResolver() {
        return entityController.getResolver();
    }

    public EventBus getEventBus() {
        return entityController.getEventbus();

    }

    public Navigator getNavigator() {
        return entityController.getNavigator();
    }


}
