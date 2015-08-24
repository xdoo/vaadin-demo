package de.muenchen.demo.service.util;

import com.google.common.eventbus.EventBus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * Created by rene.zarwel on 24.08.15.
 */
@Component
public class Eventbus extends EventBus implements Serializable{
    protected static final Logger LOG = LoggerFactory.getLogger(EventBus.class);

    public Eventbus(){
        super();
    }
}
