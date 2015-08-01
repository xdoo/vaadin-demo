package de.muenchen.vaadin.ui.app.views.events;

import com.vaadin.data.util.BeanItem;
import de.muenchen.vaadin.demo.api.domain.Buerger;

/**
 *
 * @author claus
 */
public class BuergerComponentEvent extends ComponentEvent<Buerger> {

    public BuergerComponentEvent() {
    }
    
    public BuergerComponentEvent(Buerger entity) {
        super(entity);
    }

    public BuergerComponentEvent(BeanItem<Buerger> item) {
        super(item);
    }
    
    
    
}
