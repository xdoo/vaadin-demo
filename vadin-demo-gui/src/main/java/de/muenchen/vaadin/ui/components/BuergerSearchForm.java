package de.muenchen.vaadin.ui.components;

import com.vaadin.event.ShortcutAction;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;
import de.muenchen.eventbus.selector.entity.RequestEvent;
import de.muenchen.vaadin.ui.controller.BuergerViewController;

/**
 * @author claus
 */
public class BuergerSearchForm extends CustomComponent {
    private Button search;


    public BuergerSearchForm(final BuergerViewController controller) {

        CssLayout group = new CssLayout();
        group.addStyleName("v-component-group");

        TextField query = new TextField();
        query.setId(String.format("%s_QUERY_FIELD", BuergerViewController.I18N_BASE_PATH));
        query.focus();
        query.setWidth("100%");

        // Reset Schaltfläche
        Button reset = new Button(FontAwesome.TIMES);
        reset.setStyleName(ValoTheme.BUTTON_ICON_ONLY);
        reset.setClickShortcut(ShortcutAction.KeyCode.ESCAPE);
        reset.addClickListener(e -> {
            controller.getEventbus().notify(controller.getRequestKey(RequestEvent.READ_LIST));
            query.setValue("");
        });
        reset.setId(String.format("%s_RESET_BUTTON", BuergerViewController.I18N_BASE_PATH));
        // Suche Schaltfläche
        search = new Button(FontAwesome.SEARCH);
        search.setStyleName(ValoTheme.BUTTON_ICON_ONLY);
        search.setClickShortcut(ShortcutAction.KeyCode.ENTER);
        search.addClickListener(e -> {
            if (query.getValue() != null && query.getValue().length() > 0)
                controller.getEventbus().notify(controller.getRequestKey(RequestEvent.READ_LIST), reactor.bus.Event.wrap(query.getValue()));
            else
                reset.click();
        });
        search.setId(String.format("%s_SEARCH_BUTTON", BuergerViewController.I18N_BASE_PATH));

        group.addComponents(query, search, reset);

        setCompositionRoot(group);
    }

    public void refresh() {
        search.click();
    }
}
