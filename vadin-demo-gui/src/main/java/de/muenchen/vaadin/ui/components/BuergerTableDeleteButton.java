package de.muenchen.vaadin.ui.components;

import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.themes.ValoTheme;
import de.muenchen.vaadin.demo.api.domain.Buerger;
import de.muenchen.vaadin.ui.app.views.events.BuergerAppEvent;
import de.muenchen.vaadin.ui.controller.BuergerViewController;
import de.muenchen.vaadin.ui.util.EventType;


/**
 *
 * @author claus.straube
 */
public class BuergerTableDeleteButton extends BuergerTableButtonBase {

    @Override
    public Button getCustomButton() {
        Button delete = new Button();
        delete.setIcon(FontAwesome.TRASH_O);
        delete.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
        delete.addStyleName(ValoTheme.BUTTON_DANGER);
        delete.addClickListener(e -> {
            BeanItem<Buerger> item = container.getItem(itemId);
            GenericConfirmationWindow win = new GenericConfirmationWindow(new BuergerAppEvent(item, itemId, EventType.DELETE), controller.getEventbus());
            getUI().addWindow(win);
            win.center();
            win.focus();
        });
        return delete;
    }

}
