package de.muenchen.vaadin.ui.components;

import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import de.muenchen.vaadin.demo.i18nservice.buttons.TableActionButton;
import de.muenchen.vaadin.guilib.components.GenericGrid;
import de.muenchen.vaadin.guilib.components.GenericTable;
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
    private GenericGrid table;

    public BuergerSelectTable(final BuergerViewController controller) {

        table = controller.getViewFactory().generateGrid();
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
    public GenericGrid getTable(){
        return table;
    }
    public void setTable(GenericGrid table){
        this.table = table;
    }

    public void refresh(){
        search.refresh();
    }
}