/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.muenchen.vaadin.ui.components;

import com.google.common.eventbus.Subscribe;
import de.muenchen.vaadin.demo.api.domain.Buerger;
import de.muenchen.vaadin.demo.api.util.EventType;
import de.muenchen.vaadin.ui.app.views.events.BuergerComponentEvent;
import de.muenchen.vaadin.ui.components.buttons.TableActionButton;
import de.muenchen.vaadin.ui.controller.BuergerViewController;

/**
 *
 * @author maximilian.schug
 */
public class PartnerTable extends GenericTable<Buerger> {

    public PartnerTable(BuergerViewController controller, TableActionButton.Builder... buttonBuilders) {
        super(controller, Buerger.class, buttonBuilders);
    }

    @Subscribe
    public void update(BuergerComponentEvent event) {
        if(event.getEventType().equals(EventType.SAVE_AS_PARTNER)) {
            this.add(event.getEntity());
        }
        
       /* if(event.getEventType().equals(EventType.COPY)) {
            this.add(event.getEntity());
        }
        */
        if(event.getEventType().equals(EventType.DELETE)) {
            this.delete(event.getItemID());
        }

        if (event.getEventType().equals(EventType.UPDATE)) {
            this.add(event.getEntity());
        }

       if(event.getEventType().equals(EventType.QUERY_PARTNER)) {
            this.addAll(event.getEntities());
        }
    }
}
