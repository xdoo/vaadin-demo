package de.muenchen.vaadin.ui.app.views;

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
@SpringView(name = BuergerTableView.NAME)
@UIScope
public class BuergerTableView extends DefaultBuergerView {
    
    public static final String NAME = "buerger_table_view";
    protected static final Logger LOG = LoggerFactory.getLogger(BuergerCreateView.class);
    
    @Autowired
    public BuergerTableView(BuergerViewController controller, MainUI ui) {
        super(controller, ui);
        LOG.debug("creating 'buerger_table_view'");
    }

    @Override
    protected void site() {        
        addComponent(this.controller.generateSearchTable(BuergerUpdateView.NAME, BuergerReadView.NAME, BuergerCreateView.NAME, this.NAME));
    }
    
}