package de.muenchen.vaadin.ui.components;

import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import de.muenchen.vaadin.ui.components.buttons.TableActionButton;
import de.muenchen.vaadin.ui.controller.BuergerViewController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author claus.straube
 */
public class BuergerSelectTable extends CustomComponent {

    protected static final Logger LOG = LoggerFactory.getLogger(BuergerSearchTable.class);

    BuergerSearchForm search;
    private GenericTable table;

    public BuergerSelectTable(final BuergerViewController controller, final TableActionButton.Builder... buttonfactory) {

        table = controller.getViewFactory().generateTable(buttonfactory);
        table.setSizeUndefined();

        search = new BuergerSearchForm(controller);
        search.setWidth("100%"); 

        // Layout für die Schaltflächen über der Tabelle
        HorizontalLayout hlayout = new HorizontalLayout(search);
        hlayout.setSpacing(true);

        // Gesamtlayout
        VerticalLayout vlayout = new VerticalLayout(hlayout, table);
        vlayout.setSpacing(true);


        setCompositionRoot(vlayout);
    }
    public GenericTable getTable(){
        return table;
    }
    public void setTable(GenericTable table){
        this.table = table;
    }

    public void refresh(){
        search.refresh();
    }
}