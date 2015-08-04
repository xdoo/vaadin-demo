package de.muenchen.vaadin.ui.app.views;

import com.google.common.eventbus.EventBus;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import de.muenchen.vaadin.ui.app.MainUI;
import de.muenchen.vaadin.ui.controller.BuergerViewController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author claus
 */
@SpringView(name = BuergerUpdateView.NAME)
@UIScope
public class BuergerUpdateView extends DefaultBuergerView {
    
    public static final String NAME = "buerger_update_view";
    protected static final Logger LOG = LoggerFactory.getLogger(BuergerUpdateView.class);
    
    @Autowired
    public BuergerUpdateView(BuergerViewController controller, EventBus eventbus, MainUI ui) {
        super(controller, ui);
        LOG.debug("creating 'buerger_update_view'");
    }

    @Override
    protected void site() {
        addComponent(this.controller.generateUpdateForm(BuergerTableView.NAME));
    }
    
}
