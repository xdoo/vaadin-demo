package de.muenchen.vaadin.ui.app.views.events;

import com.vaadin.data.util.BeanItem;
import de.muenchen.vaadin.demo.api.domain.Buerger;
import de.muenchen.vaadin.ui.util.EventType;

/**
 *
 * @author claus.straube
 */
public class BuergerEvent extends AppEvent<Buerger> {

    public BuergerEvent() {
    }

    public BuergerEvent(EventType type) {
        super(type);
    }

    public BuergerEvent(Buerger entity, EventType type) {
        super(entity, type);
    }

    public BuergerEvent(BeanItem<Buerger> item, Object itemId, EventType type) {
        super(item, itemId, type);
    }
    
}
