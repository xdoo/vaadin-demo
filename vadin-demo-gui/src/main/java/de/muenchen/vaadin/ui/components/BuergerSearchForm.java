package de.muenchen.vaadin.ui.components;

import com.vaadin.event.ShortcutAction;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;
import de.muenchen.vaadin.ui.app.views.events.BuergerEvent;
import de.muenchen.vaadin.ui.controller.BuergerViewController;
import de.muenchen.vaadin.ui.util.EventType;

/**
 *
 * @author claus
 */
public class BuergerSearchForm extends CustomComponent {

    public BuergerSearchForm(final BuergerViewController controller) {
    
//        HorizontalLayout layout = new HorizontalLayout();
//        layout.setSpacing(true);
//        layout.setWidth("100%");
        
        CssLayout group = new CssLayout();
        group.addStyleName("v-component-group");
        
        TextField query = new TextField();
        query.focus();
        query.setWidth("100%");
        Button action = new Button(FontAwesome.SEARCH);
        action.setStyleName(ValoTheme.BUTTON_ICON_ONLY);
        action.setClickShortcut(ShortcutAction.KeyCode.ENTER);
        action.addClickListener(e -> {
            BuergerEvent event = new BuergerEvent(EventType.QUERY);
            event.setQuery(query.getValue());
            controller.getEventbus().post(event);
        });
        
        group.addComponents(query, action);
//        layout.addComponents(query, action);
//        layout.setExpandRatio(query, 1);
        
        setCompositionRoot(group);
    }  
}
