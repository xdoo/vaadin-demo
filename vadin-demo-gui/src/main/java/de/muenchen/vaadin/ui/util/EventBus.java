package de.muenchen.vaadin.ui.util;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import java.io.Serializable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author claus.straube
 */
@SpringComponent @UIScope
public class EventBus extends com.google.common.eventbus.EventBus implements Serializable {

    protected static final Logger LOG = LoggerFactory.getLogger(EventBus.class);
    
    public EventBus() {
        super();
        
    }
    
    
}
