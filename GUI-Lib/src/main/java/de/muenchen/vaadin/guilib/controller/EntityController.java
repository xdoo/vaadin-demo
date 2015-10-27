package de.muenchen.vaadin.guilib.controller;

import de.muenchen.eventbus.selector.entity.RequestEntityKey;
import de.muenchen.eventbus.selector.entity.RequestEvent;
import de.muenchen.eventbus.selector.entity.ResponseEntityKey;


/**
 * Created by rene.zarwel on 07.10.15.
 */
public interface EntityController {

    RequestEntityKey getRequestKey(RequestEvent event);

    ResponseEntityKey getResponseKey();

}
