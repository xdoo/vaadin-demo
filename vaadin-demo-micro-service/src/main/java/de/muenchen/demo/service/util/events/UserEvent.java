package de.muenchen.demo.service.util.events;

import de.muenchen.demo.service.domain.User;
import de.muenchen.eventbus.types.EventType;

/**
 * Created by rene.zarwel on 24.08.15.
 */
public class UserEvent extends ServiceEvent<User> {
    public UserEvent() {
    }

    public UserEvent(EventType eventType) {
        super(eventType);
    }

    public UserEvent(EventType eventType, User entity) {
        super(eventType, entity);
    }
}
