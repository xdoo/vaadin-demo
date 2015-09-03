package de.muenchen.vaadin.ui.components;

import com.vaadin.event.ShortcutAction;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import de.muenchen.vaadin.demo.api.util.EventType;
import de.muenchen.vaadin.ui.app.views.events.AppEvent;
import reactor.bus.EventBus;

import static reactor.bus.Event.wrap;

/**
 *
 * @author claus
 */
public class GenericCancelButton extends CustomComponent {

    public GenericCancelButton(String label, AppEvent event, EventBus eventBus) {
        Button cancelButton = new Button(label, (Button.ClickEvent cancel) -> {
            event.setType(EventType.CANCEL); // nur um sicher zu gehen
            eventBus.notify(event.getClass(), wrap(event));
        });
        cancelButton.setIcon(FontAwesome.TIMES);
        cancelButton.setClickShortcut(ShortcutAction.KeyCode.ESCAPE);
        setId("GENERIC_CANCEL_BUTTON");
        setCompositionRoot(cancelButton);
    } 
}
