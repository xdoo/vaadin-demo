package de.muenchen.vaadin.ui.components;

import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import de.muenchen.vaadin.ui.app.views.events.AppEvent;
import de.muenchen.vaadin.ui.util.EventBus;
import de.muenchen.vaadin.ui.util.EventType;

/**
 *
 * @author claus
 */
public class GenericCancelButton extends CustomComponent {

    public GenericCancelButton(String label, AppEvent event, EventBus eventBus) {
        Button cancelButton = new Button(label, (Button.ClickEvent cancel) -> {
            event.setType(EventType.CANCEL); // nur um sicher zu gehen
            eventBus.post(event);
        });
        cancelButton.setIcon(FontAwesome.TIMES);
        
        setCompositionRoot(cancelButton);
    } 
}
