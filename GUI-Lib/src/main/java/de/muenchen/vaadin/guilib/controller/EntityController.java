package de.muenchen.vaadin.guilib.controller;

import com.vaadin.navigator.Navigator;
import de.muenchen.eventbus.selector.entity.RequestEntityKey;
import de.muenchen.eventbus.selector.entity.RequestEvent;
import de.muenchen.eventbus.selector.entity.ResponseEntityKey;
import de.muenchen.vaadin.demo.i18nservice.I18nResolver;
import reactor.bus.EventBus;

/**
 * Created by rene.zarwel on 07.10.15.
 */
public interface EntityController extends I18nResolver {

    EventBus getEventbus();

    Navigator getNavigator();

    RequestEntityKey getRequestKey(RequestEvent event);

    ResponseEntityKey getResponseKey();

}
