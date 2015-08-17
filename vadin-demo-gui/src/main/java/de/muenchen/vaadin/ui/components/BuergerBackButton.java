package de.muenchen.vaadin.ui.components;

import com.vaadin.event.ShortcutAction;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import de.muenchen.vaadin.ui.app.views.events.BuergerAppEvent;
import de.muenchen.vaadin.ui.controller.BuergerViewController;
import de.muenchen.vaadin.ui.util.EventType;
import static de.muenchen.vaadin.ui.util.I18nPaths.*;

/**
 *
 * @author claus
 */
public class BuergerBackButton extends CustomComponent {

    public BuergerBackButton(final BuergerViewController controller, final String navigateTo) {
        
        String label = controller.resolve(getFormPath(Action.back,Component.button,Type.label));
        Button back = new Button(label, FontAwesome.ANGLE_LEFT);
        back.addClickListener(e -> {
            controller.getEventbus().post(new BuergerAppEvent(EventType.CANCEL).navigateTo(navigateTo));
        });
        back.setClickShortcut(ShortcutAction.KeyCode.ARROW_LEFT);
        setCompositionRoot(back);
        
    }
    
}
