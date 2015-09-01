package de.muenchen.vaadin.ui.components;

import com.google.common.eventbus.Subscribe;
import de.muenchen.vaadin.demo.api.domain.Buerger;
import de.muenchen.vaadin.demo.api.util.EventType;
import de.muenchen.vaadin.ui.app.views.events.BuergerComponentEvent;
import de.muenchen.vaadin.ui.components.buttons.TableActionButton;
import de.muenchen.vaadin.ui.controller.BuergerViewController;

/**
 * Created by rene.zarwel on 01.09.15.
 */
public class BuergerTable extends GenericTable<Buerger> {

    public BuergerTable(BuergerViewController controller, TableActionButton.Builder... buttonBuilders) {
        super(controller, Buerger.class, buttonBuilders);
    }


    /**
     * Eventhandler für Eventbus
     *
     * @param event the event
     */
    @Subscribe
    public void update(BuergerComponentEvent event) {

        if(event.getEventType().equals(EventType.SAVE)) {
            this.add(event.getEntity());
        }

        if(event.getEventType().equals(EventType.COPY)) {
            this.add(event.getEntity());
        }

        if(event.getEventType().equals(EventType.DELETE)) {
            this.delete(event.getItemID());
        }

        if(event.getEventType().equals(EventType.UPDATE)) {
            this.add(event.getEntity());
        }

        if(event.getEventType().equals(EventType.QUERY)) {
            this.addAll(event.getEntities());
        }
    }
}
