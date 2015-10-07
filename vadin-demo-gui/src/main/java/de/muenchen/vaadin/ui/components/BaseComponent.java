package de.muenchen.vaadin.ui.components;

import com.vaadin.ui.CustomComponent;
import de.muenchen.vaadin.demo.i18nservice.I18nResolver;
import reactor.bus.EventBus;

/**
 * Created by p.mueller on 07.10.15.
 */
public class BaseComponent extends CustomComponent {
    private final I18nResolver i18nResolver;
    private final EventBus eventBus;

    public BaseComponent(I18nResolver i18nResolver, EventBus eventBus) {
        this.i18nResolver = i18nResolver;
        this.eventBus = eventBus;
    }

    public I18nResolver getI18nResolver() {
        return i18nResolver;
    }

    public EventBus getEventBus() {
        return eventBus;
    }
}
