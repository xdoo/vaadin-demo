package de.muenchen.vaadin.ui.components;

import com.vaadin.data.util.BeanItem;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.themes.ValoTheme;
import de.muenchen.vaadin.demo.api.domain.Buerger;
import de.muenchen.vaadin.ui.app.views.events.BuergerAppEvent;
import de.muenchen.vaadin.demo.api.util.EventType;

/**
 * Schaltfläche um die Details (inklusive Abhängigkeiten) eines {@link Buerger}
 * zu sehen.
 * 
 * @author claus.straube
 */
public class BuergerTableDetailButton extends BuergerTableButtonBase {

    @Override
    public Button getCustomButton() {
        Button detail = new Button();
        detail.setIcon(FontAwesome.FILE_O);
        detail.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
        detail.addClickListener(e -> {
            BeanItem<Buerger> item = container.getItem(itemId);
            controller.getEventbus().post(new BuergerAppEvent(item, itemId, EventType.SELECT2READ).navigateTo(navigateTo).from(navigateFrom));
        });
        return detail;
    }

}
