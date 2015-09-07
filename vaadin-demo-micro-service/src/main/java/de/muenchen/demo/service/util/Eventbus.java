package de.muenchen.demo.service.util;

import org.springframework.stereotype.Component;
import reactor.core.dispatch.SynchronousDispatcher;

import java.io.Serializable;

/**
 * Created by rene.zarwel on 24.08.15.
 */
@Component
//@UIScope
public class Eventbus extends reactor.bus.EventBus implements Serializable {

    public Eventbus(){
        super(SynchronousDispatcher.INSTANCE);
    }
}
