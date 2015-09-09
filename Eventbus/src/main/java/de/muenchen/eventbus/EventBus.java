package de.muenchen.eventbus;

import reactor.core.dispatch.SynchronousDispatcher;

import java.io.Serializable;

/**
 * Created by rene.zarwel on 04.09.15.
 */
public class EventBus extends reactor.bus.EventBus implements Serializable {

    public EventBus() {
        super(SynchronousDispatcher.INSTANCE);
    }
}
