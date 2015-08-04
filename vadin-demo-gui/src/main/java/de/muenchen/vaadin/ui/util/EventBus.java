package de.muenchen.vaadin.ui.util;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import java.io.Serializable;

/**
 *
 * @author claus.straube
 */
@SpringComponent @UIScope
public class EventBus extends com.google.common.eventbus.EventBus implements Serializable {
    
}
