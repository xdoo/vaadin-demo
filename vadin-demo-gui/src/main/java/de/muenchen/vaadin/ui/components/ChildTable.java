/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.muenchen.vaadin.ui.components;

import de.muenchen.vaadin.demo.api.domain.Buerger;
import de.muenchen.vaadin.demo.api.util.EventType;
import de.muenchen.vaadin.ui.app.views.events.ComponentEvent;
import de.muenchen.vaadin.ui.components.buttons.TableActionButton;
import de.muenchen.vaadin.ui.controller.BuergerViewController;
import org.springframework.hateoas.Resource;

/**
 *
 * @author maximilian.schug
 */
public class ChildTable extends GenericTable<Buerger> {

    public ChildTable(BuergerViewController controller, TableActionButton.Builder... buttonBuilders) {
        super(controller, Buerger.class, buttonBuilders);
    }

    @Override
    public void accept(reactor.bus.Event<ComponentEvent<Resource<Buerger>>> eventWrapper) {
        //super.accept(eventWrapper);
        ComponentEvent<Resource<Buerger>> event = eventWrapper.getData();

        if(event.getEventType().equals(EventType.SAVE_CHILD)) {
            event.getEntity().ifPresent(this::add);
        }
        if(event.getEventType().equals(EventType.DELETE)) {
            event.getEntity().ifPresent(this::delete);
        }
        if(event.getEventType().equals(EventType.UPDATE_CHILD)) {
            event.getEntity().ifPresent(this::add);
        }
        if(event.getEventType().equals(EventType.QUERY_CHILD)) {
            this.addAll(event.getEntities());
        }
    }
}
