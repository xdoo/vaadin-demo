package de.muenchen.vaadin.ui.components;

import com.vaadin.data.util.BeanItem;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.themes.ValoTheme;
import de.muenchen.vaadin.demo.api.domain.Buerger;
import de.muenchen.vaadin.ui.app.views.events.BuergerAppEvent;
import de.muenchen.vaadin.demo.api.util.EventType;



/**
 * Schaltfläche zum bearbeiten eines {@link Buerger}.
 * 
 * @author claus.straube
 */
public class BuergerTableEditButton extends BuergerTableButtonBase {
    
    @Override
    public Button getCustomButton() {
        Button edit = new Button();
        edit.setIcon(FontAwesome.PENCIL);
        edit.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
        edit.addClickListener(e -> {
            BeanItem<Buerger> item = container.getItem(itemId);
            controller.getEventbus().post(new BuergerAppEvent(item, itemId, EventType.SELECT2UPDATE).navigateTo(navigateTo).from(navigateFrom));
        });
        return edit;
    }
}
