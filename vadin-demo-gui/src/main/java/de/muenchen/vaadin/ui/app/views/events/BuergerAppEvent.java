package de.muenchen.vaadin.ui.app.views.events;

import com.vaadin.data.util.BeanItem;
import de.muenchen.vaadin.demo.api.domain.Buerger;
import de.muenchen.vaadin.ui.util.EventType;

/**
 *
 * @author claus.straube
 */
public class BuergerAppEvent extends AppEvent<Buerger> {

    public BuergerAppEvent(EventType type) {
        super(type);
    }

    public BuergerAppEvent(Buerger entity, EventType type) {
        super(entity, type);
    }

    public BuergerAppEvent(BeanItem<Buerger> item, Object itemId, EventType type) {
        super(item, itemId, type);
    }
}
