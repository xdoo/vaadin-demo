/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.muenchen.vaadin.ui.components;

import com.google.common.eventbus.Subscribe;
import de.muenchen.vaadin.ui.app.views.events.BuergerComponentEvent;
import de.muenchen.vaadin.ui.controller.BuergerViewController;
import de.muenchen.vaadin.demo.api.util.EventType;

/**
 *
 * @author maximilian.schug
 */
public class ChildTable extends BuergerTable {

    public ChildTable(BuergerViewController controller, BuergerTableButtonFactory... buttonfactory) {
        super(controller, buttonfactory);
    }
    
    @Override
    @Subscribe
    public void update(BuergerComponentEvent event) {
        if(event.getEventType().equals(EventType.CHILDSAVE)) {
            this.add(event.getEntity());
        }
        
       /* if(event.getEventType().equals(EventType.COPY)) {
            this.add(event.getEntity());
        }
        */
        if(event.getEventType().equals(EventType.DELETE)) {
            this.delete(event.getItemID());
        }
        
        if(event.getEventType().equals(EventType.UPDATE)) {
            this.add(event.getEntity());
        }
        
        if(event.getEventType().equals(EventType.CHILDQUERY)) {
            this.addAll(event.getEntities());
        }
    }
}
