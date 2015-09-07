package de.muenchen.vaadin.ui.app.views.events;

import de.muenchen.vaadin.demo.api.util.EventType;

/**
 * Created by fabian.holtkoetter on 01.09.15.
 */
public class RefreshEvent extends AppEvent {
    public RefreshEvent(){
        super();
        setType(EventType.REFRESH);
    }
}
