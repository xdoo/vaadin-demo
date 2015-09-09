package de.muenchen.eventbus.events;

import de.muenchen.eventbus.types.EventType;

/**
 * Created by rene.zarwel on 24.08.15.
 */
public class ServiceEvent<T> {

    protected EventType eventType;

    private T entity;

    public ServiceEvent() {
    }

    public ServiceEvent(EventType eventType) {
        this.eventType = eventType;
    }

    public ServiceEvent(EventType eventType, T entity) {
        this.eventType = eventType;
        this.entity = entity;
    }

    public T getEntity() {
        return entity;
    }

    public EventType getEventType() {
        return eventType;
    }
}
