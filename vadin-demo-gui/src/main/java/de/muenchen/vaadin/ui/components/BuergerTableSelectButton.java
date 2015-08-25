package de.muenchen.vaadin.ui.components;

import com.vaadin.data.util.BeanItem;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.themes.ValoTheme;
import de.muenchen.vaadin.demo.api.domain.Buerger;
import de.muenchen.vaadin.ui.app.views.BuergerTableView;
import de.muenchen.vaadin.ui.app.views.events.BuergerAppEvent;
import de.muenchen.vaadin.ui.util.EventType;
import de.muenchen.vaadin.ui.util.I18nPaths;



/**
 * Schaltfläche zum bearbeiten eines {@link Buerger}.
 * 
 * @author claus.straube
 */
public class BuergerTableSelectButton extends BuergerTableButtonBase {
    
    @Override
    public Button getCustomButton() {
        Button edit = new Button();
        edit.setIcon(FontAwesome.PENCIL);
        edit.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
        edit.addClickListener(e -> {
            BeanItem<Buerger> item = container.getItem(itemId);
            
            controller.getEventbus().post(new BuergerAppEvent(item, itemId, EventType.SAVE_AS_CHILD).navigateTo(navigateTo).from(navigateFrom));
            
        });
        return edit;
    }
}