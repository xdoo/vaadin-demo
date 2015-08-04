package de.muenchen.vaadin.ui.app.views.events;

import com.vaadin.data.util.BeanItem;
import de.muenchen.vaadin.demo.api.domain.Buerger;
import de.muenchen.vaadin.ui.util.EventType;

/**
 *
 * @author claus
 */
public class BuergerComponentEvent extends ComponentEvent<Buerger> {

    public BuergerComponentEvent(EventType eventType) {
        super(eventType);
    }

    public BuergerComponentEvent(BeanItem<Buerger> item, EventType eventType) {
        super(item, eventType);
    }

    public BuergerComponentEvent(Buerger entity, EventType eventType) {
        super(entity, eventType);
    }
    
}
