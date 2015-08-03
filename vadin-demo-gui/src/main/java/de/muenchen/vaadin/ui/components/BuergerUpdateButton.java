package de.muenchen.vaadin.ui.components;

import com.vaadin.data.util.BeanItem;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import de.muenchen.vaadin.demo.api.domain.Buerger;
import de.muenchen.vaadin.ui.app.views.events.BuergerEvent;
import de.muenchen.vaadin.ui.controller.BuergerViewController;
import de.muenchen.vaadin.ui.util.EventType;
import de.muenchen.vaadin.ui.util.I18nPaths;

/**
 *
 * @author claus
 */
public class BuergerUpdateButton extends CustomComponent {

    private Buerger entity;
    
    public BuergerUpdateButton(final BuergerViewController controller, final String navigateTo) {
        String label = controller.getMsg().readText(controller.getI18nBasePath(), I18nPaths.I18N_FORM_UPDATE_BUTTON_LABEL);
        Button update = new Button(label, FontAwesome.PENCIL);
        update.addClickListener(e -> {
            BuergerEvent event = new BuergerEvent(EventType.SELECT2UPDATE);
            event.setEntity(entity);
            event.setNavigateTo(navigateTo);
            controller.getEventbus().post(event);
        });
        setCompositionRoot(update);
    }

    public void setEntity(Buerger entity) {
        this.entity = entity;
    }
    
}
