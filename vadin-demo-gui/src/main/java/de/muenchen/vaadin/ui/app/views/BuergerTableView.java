package de.muenchen.vaadin.ui.app.views;

import com.google.common.eventbus.EventBus;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.VerticalLayout;
import de.muenchen.vaadin.ui.app.MainUI;
import de.muenchen.vaadin.ui.components.CreateBuergerButton;
import de.muenchen.vaadin.ui.components.BuergerTable;
import de.muenchen.vaadin.ui.controller.BuergerViewController;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author claus
 */
@SpringView(name = BuergerTableView.NAME)
@UIScope
public class BuergerTableView extends DefaultBuergerView {
    
    public static final String NAME = "buerger_table_view";
    
    @Autowired
    public BuergerTableView(BuergerViewController controller, MainUI ui) {
        super(controller, ui);
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
