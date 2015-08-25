package de.muenchen.vaadin.ui.components;

import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import de.muenchen.vaadin.ui.controller.BuergerViewController;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author claus.straube
 */
public class ChildSearchTable extends CustomComponent {
    protected static final Logger LOG = LoggerFactory.getLogger(BuergerSearchTable.class);
    private BuergerTable table;
    public ChildSearchTable(final BuergerViewController controller, String navigateToForEdit, String navigateToForBack, String navigateToForCreate, String from, final BuergerTableButtonFactory... buttonfactory) {
    
        table = controller.generateTable(navigateToForEdit, navigateToForBack, from, buttonfactory);
        BuergerSearchForm search = new BuergerSearchForm(controller);
        search.setWidth("100%"); 
        BuergerBackButton back = new BuergerBackButton(controller, navigateToForBack);
        // Layout für die Schaltflächen über der Tabelle
        HorizontalLayout hlayout = new HorizontalLayout(back, search);
        hlayout.setSpacing(true);
        // Gesamtlayout
        VerticalLayout vlayout = new VerticalLayout(hlayout, table);
        vlayout.setSpacing(true);
        
        setCompositionRoot(vlayout);
    }
    public BuergerTable getTable(){
        return table;
    }
    public void setTable(BuergerTable table){
        this.table = table;
    }
}