package de.muenchen.vaadin.ui.util;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import reactor.core.dispatch.SynchronousDispatcher;

import java.io.Serializable;

/**
 * Created by rene.zarwel on 04.09.15.
 */
@SpringComponent
@UIScope
public class EventBus extends reactor.bus.EventBus implements Serializable {

    public EventBus() {
        super(SynchronousDispatcher.INSTANCE);
    }
}
