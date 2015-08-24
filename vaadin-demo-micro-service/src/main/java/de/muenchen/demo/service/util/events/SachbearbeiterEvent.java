package de.muenchen.demo.service.util.events;

import de.muenchen.demo.service.domain.Sachbearbeiter;
import de.muenchen.vaadin.demo.api.util.EventType;

/**
 * Created by rene.zarwel on 24.08.15.
 */
public class SachbearbeiterEvent extends ServiceEvent<Sachbearbeiter> {

    public SachbearbeiterEvent() {
    }

    public SachbearbeiterEvent(EventType eventType) {
        super(eventType);
    }

    public SachbearbeiterEvent(EventType eventType, Sachbearbeiter entity) {
        super(eventType, entity);
    }
}
