package de.muenchen.vaadin.ui.app.views;

import com.vaadin.ui.VerticalLayout;
import de.muenchen.vaadin.ui.app.MainUI;
import de.muenchen.vaadin.ui.components.CreateBuergerButton;
import de.muenchen.vaadin.ui.components.BuergerTable;
import de.muenchen.vaadin.ui.controller.BuergerViewController;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.annotation.VaadinUIScope;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.navigator.annotation.VaadinView;

/**
 *
 * @author claus
 */
@VaadinView(name = BuergerTableView.NAME)
@VaadinUIScope
public class BuergerTableView extends DefaultBuergerView {
    
    public static final String NAME = "buerger_table_view";
    
    @Autowired
    public BuergerTableView(BuergerViewController controller, EventBus eventbus, MainUI ui) {
        super(controller, eventbus, ui);
    }

    @Override
    protected void site() {
        CreateBuergerButton button = new CreateBuergerButton(controller, BuergerCreateView.NAME);
        BuergerTable table = this.controller.generatePersonTable(BuergerUpdateView.NAME);
        
        VerticalLayout layout = new VerticalLayout(button, table);
        layout.setSpacing(true);
        
        addComponent(layout);
    }
    
}
