package de.muenchen.vaadin.ui.components;

import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import de.muenchen.vaadin.ui.controller.BuergerViewController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author claus.straube
 */
public class BuergerSearchTable extends CustomComponent {

    protected static final Logger LOG = LoggerFactory.getLogger(BuergerSearchTable.class);
    
    public BuergerSearchTable(final BuergerViewController controller, String navigateToForEdit, String navigateToForSelect, String navigateToForCreate, String from) {
        BuergerCreateButton create = new BuergerCreateButton(controller, navigateToForCreate, from);
        BuergerTable table = controller.generateTable(navigateToForEdit, navigateToForSelect, from);
        BuergerSearchForm search = new BuergerSearchForm(controller);
        search.setWidth("100%"); 
        
        // Layout für die Schaltflächen über der Tabelle
        HorizontalLayout hlayout = new HorizontalLayout(create, search);
        hlayout.setSpacing(true);
        // Gesamtlayout
        VerticalLayout vlayout = new VerticalLayout(hlayout, table);
        vlayout.setSpacing(true);
        
        setCompositionRoot(vlayout);
    }
    
}
