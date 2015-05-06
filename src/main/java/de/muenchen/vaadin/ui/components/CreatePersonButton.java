package de.muenchen.vaadin.ui.components;

import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.themes.ValoTheme;
import de.muenchen.vaadin.ui.app.views.DefaultPersonView;
import de.muenchen.vaadin.ui.app.views.events.PersonEvent;
import de.muenchen.vaadin.ui.controller.PersonViewController;
import de.muenchen.vaadin.ui.util.EventType;
import de.muenchen.vaadin.ui.util.I18nPaths;

/**
 *
 * @author claus
 */
public class CreatePersonButton extends CustomComponent {

    public CreatePersonButton(final PersonViewController controller, String navigateTo) {
        
        String label = controller.getUtil().readText(controller.getI18nBasePath(), I18nPaths.I18N_FORM_CREATE_BUTTON_LABEL);
        Button create = new Button(label, FontAwesome.MAGIC);
        create.addStyleName(ValoTheme.BUTTON_FRIENDLY);
        create.addClickListener(e -> {
            PersonEvent event = new PersonEvent(EventType.CREATE);
            event.setNavigateTo(navigateTo);
            controller.getEventbus().publish(this, event);
        });
        
        setCompositionRoot(create);
    }
      
}
