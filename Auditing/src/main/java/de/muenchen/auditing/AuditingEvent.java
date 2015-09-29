package de.muenchen.auditing;

import de.muenchen.eventbus.types.RequestEvent;

/**
 * Eigenes Event welches die Listener im Producer verschicken um die Sicherung der eingetroffenen Daten anzustoßen.
 * Created by fabian.holtkoetter on 07.09.15.
 */
public class AuditingEvent {

    private RequestEvent eventType;

    private Object entity;


    public AuditingEvent(RequestEvent eventType, Object entity) {
        this.eventType = eventType;
        this.entity = entity;
    }

    public Object getEntity() {
        return entity;
    }

    public RequestEvent getEventType() {
        return eventType;
    }
}
