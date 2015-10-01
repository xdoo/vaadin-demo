package de.muenchen.eventbus;

import reactor.core.dispatch.SynchronousDispatcher;

import java.io.Serializable;

/**
 * Provides a simple EventBus.
 * @author rene.zarwel
 */
public class EventBus extends reactor.bus.EventBus implements Serializable {

    public EventBus() {
        super(SynchronousDispatcher.INSTANCE);
    }
}
