package de.muenchen.auditing;

import de.muenchen.eventbus.events.ServiceEvent;
import de.muenchen.eventbus.types.EventType;

/**
 * Created by fabian.holtkoetter on 07.09.15.
 */
public class AuditingEvent<Object> extends ServiceEvent<Object> {

    public AuditingEvent(EventType eventType, Object entity) {
        super(eventType, entity);
    }
}
