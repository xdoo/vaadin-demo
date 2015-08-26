package de.muenchen.vaadin.ui.app.views;

import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import de.muenchen.vaadin.ui.app.MainUI;
import de.muenchen.vaadin.ui.components.ChildSearchTable;
import de.muenchen.vaadin.ui.controller.BuergerViewController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author claus
 */
@SpringView(name = ChildSelectView.NAME)
@UIScope
public class ChildSelectView extends DefaultBuergerView {
    
    public static final String NAME = "child_search_view";
    protected static final Logger LOG = LoggerFactory.getLogger(BuergerCreateView.class);
    private ChildSearchTable table;
    @Autowired
    public ChildSelectView(BuergerViewController controller, MainUI ui ){ 
        super(controller, ui);
        LOG.debug("creating 'buerger_table_view'");
        
    }

    @Override
    protected void site() {     
        
        table= this.controller.getViewFactory().generateChildSearchTable(BuergerUpdateView.NAME, BuergerDetailView.NAME, BuergerCreateView.NAME, ChildSelectView.NAME);
        addComponent(table);
    }

}