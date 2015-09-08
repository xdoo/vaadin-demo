package de.muenchen.demo.service.util.events;

import de.muenchen.demo.service.domain.Buerger;
import de.muenchen.eventbus.types.EventType;

/**
 * Created by rene.zarwel on 24.08.15.
 */
public class BuergerEvent extends ServiceEvent<Buerger> {

    public BuergerEvent() {
    }

    public BuergerEvent(EventType eventType, Buerger entity) {
        super(eventType, entity);
    }

    public BuergerEvent(EventType eventType) {
        super(eventType);
    }
}
