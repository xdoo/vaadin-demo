package de.muenchen.vaadin.ui.app.views.events;

import de.muenchen.vaadin.demo.api.util.EventType;

/**
 *
 * @author claus
 */
public class LoginEvent extends AppEvent {
    public LoginEvent(){
        super();
        setType(EventType.LOGIN);
    }
    
}
