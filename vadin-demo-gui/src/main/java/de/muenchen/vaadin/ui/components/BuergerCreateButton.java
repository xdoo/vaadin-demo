package de.muenchen.vaadin.ui.components;

import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.themes.ValoTheme;
import de.muenchen.vaadin.ui.app.views.events.BuergerAppEvent;
import de.muenchen.vaadin.ui.controller.BuergerViewController;
import de.muenchen.vaadin.ui.util.EventType;
import de.muenchen.vaadin.ui.util.I18nPaths;

/**
 *
 * @author claus
 */
public class BuergerCreateButton extends CustomComponent {

    public BuergerCreateButton(final BuergerViewController controller, final String navigateTo, final String from) {
        
        String label = controller.getMsg().readText(controller.getI18nBasePath(), I18nPaths.I18N_FORM_CREATE_BUTTON_LABEL);
        Button create = new Button(label, FontAwesome.MAGIC);
        create.addStyleName(ValoTheme.BUTTON_FRIENDLY);
        create.addClickListener(e -> {
            controller.getEventbus().post(new BuergerAppEvent(EventType.CREATE).navigateTo(navigateTo).from(from));
        });
        
        setCompositionRoot(create);
    }
      
}
