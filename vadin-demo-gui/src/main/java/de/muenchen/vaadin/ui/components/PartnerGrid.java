/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.muenchen.vaadin.ui.components;

import de.muenchen.eventbus.events.ComponentEvent;
import de.muenchen.eventbus.types.EventType;
import de.muenchen.vaadin.demo.api.local.Buerger;
import de.muenchen.vaadin.guilib.components.GenericGrid;
import de.muenchen.vaadin.ui.controller.BuergerViewController;

/**
 *
 * @author maximilian.schug
 */
public class PartnerGrid extends GenericGrid<Buerger> {

    public PartnerGrid(BuergerViewController controller) {
        super(controller, Buerger.class);
    }

    @Override
    public void accept(reactor.bus.Event<ComponentEvent<Buerger>> eventWrapper) {
        //super.accept(eventWrapper);

        ComponentEvent event = eventWrapper.getData();

        if(event.getEventType().equals(EventType.SAVE_AS_PARTNER)) {
            this.add(event.getEntity());
        }
        if(event.getEventType().equals(EventType.SAVE_PARTNER)) {
            this.add(event.getEntity());
        }
        if(event.getEventType().equals(EventType.DELETE)) {
            this.delete(event.getItemID());
        }

        if (event.getEventType().equals(EventType.UPDATE_PARTNER)) {
            this.add(event.getEntity());
        }

       if(event.getEventType().equals(EventType.QUERY_PARTNER)) {
            this.addAll(event.getEntities());
        }
    }
}
