package de.muenchen.vaadin.ui.app.views.events;

import de.muenchen.vaadin.ui.util.EventType;

/**
 *
 * @author claus.straube
 */
public class AppEvent {
   
    protected EventType type;
    protected Object itemId;
    protected String navigateTo;
    
    public EventType getType() {
        return type;
    }

    public void setType(EventType type) {
        this.type = type;
    }

    public void setItemId(Object itemId) {
        this.itemId = itemId;
    }

    public Object getItemId() {
        return itemId;
    }

    public void setNavigateTo(String navigateTo) {
        this.navigateTo = navigateTo;
    }

    public String getNavigateTo() {
        return navigateTo;
    }
    
}
