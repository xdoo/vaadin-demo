package de.muenchen.vaadin.ui.components;

import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import de.muenchen.vaadin.demo.api.domain.Buerger;
import de.muenchen.vaadin.ui.app.views.events.BuergerAppEvent;
import de.muenchen.vaadin.ui.controller.BuergerViewController;
import de.muenchen.vaadin.ui.util.EventType;
import static de.muenchen.vaadin.ui.util.I18nPaths.*;


/**
 *
 * @author claus
 */
public class BuergerUpdateButton extends CustomComponent {

    private Buerger entity;
    
    public BuergerUpdateButton(final BuergerViewController controller, final String navigateTo, final String from) {
        String label = controller.resolve(getFormPath(Action.update,Component.button,Type.label));
        Button update = new Button(label, FontAwesome.PENCIL);
        update.addClickListener(e -> {
            controller.getEventbus().post(new BuergerAppEvent(EventType.SELECT2UPDATE).setEntity(entity).navigateTo(navigateTo).from(from));
        });
        setCompositionRoot(update);
    }

    public void setEntity(Buerger entity) {
        this.entity = entity;
    }
    
}
