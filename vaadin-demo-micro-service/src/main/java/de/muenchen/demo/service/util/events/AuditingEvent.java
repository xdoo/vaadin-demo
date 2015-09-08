package de.muenchen.demo.service.util.events;

import de.muenchen.vaadin.demo.api.util.EventType;

/**
 * Created by fabian.holtkoetter on 07.09.15.
 */
public class AuditingEvent<BaseEntity> extends ServiceEvent<de.muenchen.demo.service.domain.BaseEntity> {

    public AuditingEvent(EventType eventType, de.muenchen.demo.service.domain.BaseEntity entity) {
        super(eventType, entity);
    }
}
