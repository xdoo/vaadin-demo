package de.muenchen.eventbus.events;

import reactor.bus.Event;

/**
 * Provides a simple Interface to mark a Class as an EventBus compatible Event.
 *
 * @author p.mueller
 * @version 1.0
 */
public interface Eventable {
    /**
     * Get the Assocation as an Event.
     * @return The Event.
     */
    default Event<?> asEvent() {
        return Event.wrap(this);
    }
}
