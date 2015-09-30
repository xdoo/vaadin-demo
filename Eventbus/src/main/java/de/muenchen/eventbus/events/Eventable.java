package de.muenchen.eventbus.events;

import reactor.bus.Event;

/**
 * Created by p.mueller on 30.09.15.
 */
public interface Eventable {

    /**
     * Get the Assocation as an Event.
     *
     * @return The Event.
     */
    default Event<?> asEvent() {
        return Event.wrap(this);
    }
}
