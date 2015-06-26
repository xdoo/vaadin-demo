package de.muenchen.vaadin.ui.app.views.events;

import com.vaadin.data.util.BeanItem;
import com.catify.vaadin.demo.api.domain.Buerger;
import de.muenchen.vaadin.ui.util.EventType;

/**
 *
 * @author claus.straube
 */
public class BuergerEvent extends AppEvent {
    
    private Buerger buerger;
    private BeanItem<Buerger> item;

    public BuergerEvent() {
    }

    public BuergerEvent(EventType type) {
        this.type = type;
    }

    public BuergerEvent(Buerger buerger, EventType type) {
        this.buerger = buerger;
        this.type = type;
    }

    public BuergerEvent(BeanItem<Buerger> item, Object itemId, EventType type) {
        this.buerger = item.getBean();
        this.item = item;
        this.type = type;
        this.itemId = itemId;
    }
    
    // members

    public Buerger getBuerger() {
        return buerger;
    }

    public void setBuerger(Buerger buerger) {
        this.buerger = buerger;
    }
    
    public void setItem(BeanItem<Buerger> item) {
        this.buerger = item.getBean();
        this.item = item;
    }

    public BeanItem<Buerger> getItem() {
        return item;
    }
    
}
