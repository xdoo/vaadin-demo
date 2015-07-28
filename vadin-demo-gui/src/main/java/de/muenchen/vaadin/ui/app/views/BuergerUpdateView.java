package de.muenchen.vaadin.ui.app.views;

import com.google.common.eventbus.EventBus;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import de.muenchen.vaadin.ui.app.MainUI;
import de.muenchen.vaadin.ui.controller.BuergerViewController;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author claus
 */
@SpringView(name = BuergerUpdateView.NAME)
@UIScope
public class BuergerUpdateView extends DefaultBuergerView {
    
    public static final String NAME = "buerger_update_view";
    
    @Autowired
    public BuergerUpdateView(BuergerViewController controller, EventBus eventbus, MainUI ui) {
        super(controller, ui);
    }

    @Override
    protected void site() {
        addComponent(this.controller.generateUpdateBuergerForm(BuergerTableView.NAME));
    }
    
}
