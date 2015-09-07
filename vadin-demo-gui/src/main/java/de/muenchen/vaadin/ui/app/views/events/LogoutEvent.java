package de.muenchen.vaadin.ui.app.views.events;

import de.muenchen.vaadin.demo.api.util.EventType;

/**
 * Created by maximilian.zollbrecht on 19.08.15.
 */
public class LogoutEvent extends AppEvent{
    public LogoutEvent(){
        super();
        setType(EventType.LOGOUT);
    }

}
