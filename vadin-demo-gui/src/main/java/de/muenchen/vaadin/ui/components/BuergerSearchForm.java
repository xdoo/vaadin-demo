package de.muenchen.vaadin.ui.components;

import com.vaadin.event.ShortcutAction;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;
import de.muenchen.vaadin.ui.app.views.events.BuergerAppEvent;
import de.muenchen.vaadin.ui.controller.BuergerViewController;
import de.muenchen.vaadin.ui.util.EventType;

/**
 *
 * @author claus
 */
public class BuergerSearchForm extends CustomComponent {

    public BuergerSearchForm(final BuergerViewController controller) {
        
        CssLayout group = new CssLayout();
        group.addStyleName("v-component-group");
        
        TextField query = new TextField();
        query.setId(String.format("%s_QUERY_FIELD", controller.getI18nBasePath()));
        query.focus();
        query.setWidth("100%");
        // Suche Schaltfläche
        Button search = new Button(FontAwesome.SEARCH);
        search.setStyleName(ValoTheme.BUTTON_ICON_ONLY);
        search.setClickShortcut(ShortcutAction.KeyCode.ENTER);
        search.addClickListener(e -> {
            controller.getEventbus().post(new BuergerAppEvent(EventType.QUERY).query(query.getValue()));
        });
        search.setId(String.format("%s_SEARCH_BUTTON", controller.getI18nBasePath()));
        
        // Reset Schaltfläche
        Button reset = new Button(FontAwesome.TIMES);
        reset.setStyleName(ValoTheme.BUTTON_ICON_ONLY);
        reset.setClickShortcut(ShortcutAction.KeyCode.ESCAPE);
        reset.addClickListener(e -> {
            controller.getEventbus().post(new BuergerAppEvent(EventType.QUERY));
            query.setValue("");
        });
        reset.setId(String.format("%s_RESET_BUTTON", controller.getI18nBasePath()));
        group.addComponents(query, search, reset);
        
        setCompositionRoot(group);
    }  
}
