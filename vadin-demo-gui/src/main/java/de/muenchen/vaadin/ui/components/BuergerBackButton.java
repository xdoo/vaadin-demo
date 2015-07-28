package de.muenchen.vaadin.ui.components;

import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import de.muenchen.vaadin.ui.app.views.events.BuergerEvent;
import de.muenchen.vaadin.ui.controller.BuergerViewController;
import de.muenchen.vaadin.ui.util.EventType;
import de.muenchen.vaadin.ui.util.I18nPaths;

/**
 *
 * @author claus
 */
public class BuergerBackButton extends CustomComponent {

    public BuergerBackButton(final BuergerViewController controller, final String navigateTo) {
        
        String label = controller.getMsg().readText(controller.getI18nBasePath(), I18nPaths.I18N_FORM_CREATE_BUTTON_LABEL);
        Button back = new Button(label, FontAwesome.ANGLE_LEFT);
        back.addClickListener(e -> {
            BuergerEvent event = new BuergerEvent(EventType.CANCEL);
            event.setNavigateTo(navigateTo);
            controller.getEventbus().post(event);
        });
        
        setCompositionRoot(back);
        
    }
    
}
